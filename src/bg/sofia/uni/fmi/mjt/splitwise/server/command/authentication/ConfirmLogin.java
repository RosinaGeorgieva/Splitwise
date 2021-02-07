package bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.AuthenticationCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.AuthDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.UserCredentials;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class ConfirmLogin extends AuthenticationCommand {
    public ConfirmLogin(Command command) {
        super(command);
    }

    @Override
    public String execute(Database database) {
        AuthDatabase authDatabase = (AuthDatabase)database;

        String username = command.arguments()[0];
        String password = command.arguments()[1];

        UserCredentials userCredentials = new UserCredentials(username, password);
        if (!authDatabase.getUserCredentialsRepository().contains(userCredentials)) {
            return String.format(Messages.UNSUCCESSFUL_LOGIN, Delimiters.LINE_SEPARATOR);
        }

        Integer sessionId = command.sessionId();
        if (authDatabase.getSessionRepository().contains(sessionId)) {
            return String.format(Messages.ALREADY_LOGGED_IN, Delimiters.LINE_SEPARATOR);
        }

        authDatabase.getSessionRepository().put(sessionId, userCredentials);
        return String.format(Messages.SUCCESSFUL_LOGIN, username, Delimiters.LINE_SEPARATOR);
    }
}
