package bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.AuthenticationCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.AuthDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.UserCredentials;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.Validator;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class ConfirmRegistration extends AuthenticationCommand {

    public ConfirmRegistration(Command command) {
        super(command);
    }

    @Override
    public String execute(Database database) {
        AuthDatabase authDatabase = (AuthDatabase)database;
        String username = command.arguments()[0];

        if (!Validator.containsOnlyValidSymbols(username)) {
            return String.format(Messages.INVALID_USERNAME, username, Delimiters.LINE_SEPARATOR);
        }

        if (authDatabase.getUserCredentialsRepository().getAllUsernames().contains(username)) {
            return String.format(Messages.USERNAME_ALREADY_TAKEN, username, Delimiters.LINE_SEPARATOR);
        }

        String password = command.arguments()[1];
        UserCredentials newUser = new UserCredentials(username, password);
        Integer sessionId = command.sessionId();

        authDatabase.getUserCredentialsRepository().add(newUser);
        authDatabase.getSessionRepository().put(sessionId, newUser);

        String registrationInfo = newUser.toString();
        saveToFile(registrationInfo, Filenames.USER_CREDENTIALS);

        return String.format(Messages.OK, Delimiters.LINE_SEPARATOR);
    }
}
