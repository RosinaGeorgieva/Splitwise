package bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.AuthenticationCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class ConfirmHelp extends AuthenticationCommand {
    public ConfirmHelp(Command command) {
        super(command);
    }

    @Override
    public String execute(Database database) {
        return Messages.OK;
    }
}
