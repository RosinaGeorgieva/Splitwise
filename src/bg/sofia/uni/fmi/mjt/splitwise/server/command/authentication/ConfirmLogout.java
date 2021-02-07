package bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.AuthenticationCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.AuthDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class ConfirmLogout extends AuthenticationCommand {
    public ConfirmLogout(Command command) {
        super(command);
    }

    @Override
    public String execute(Database database) {
        AuthDatabase authDatabase = (AuthDatabase) database;

        if (!authDatabase.getSessionRepository().contains(command.sessionId())) {
            return String.format(Messages.NOT_LOGGED_IN, Delimiters.LINE_SEPARATOR);
        }

        authDatabase.getSessionRepository().remove(command.sessionId());
        return String.format(Messages.SUCCESSFUL_LOGOUT, Delimiters.LINE_SEPARATOR);
    }
}
