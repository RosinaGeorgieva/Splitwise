package bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.AuthenticationCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.AuthDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class ConfirmLoggedIn extends AuthenticationCommand {

    public ConfirmLoggedIn(Command command) {
        super(command);
    }

    @Override
    public String execute(Database database) {
        AuthDatabase authDatabase = (AuthDatabase)database;

        Integer sessionId = command.sessionId();
        if (!authDatabase.getSessionRepository().contains(sessionId)) {
            return String.format(Messages.NOT_LOGGED_IN, Delimiters.LINE_SEPARATOR);
        }

        String requestingUser = authDatabase.getSessionRepository().get(sessionId).username();
        return Messages.OK + Delimiters.HASHTAG + requestingUser + Delimiters.HASHTAG + Delimiters.LINE_SEPARATOR;
    }
}
