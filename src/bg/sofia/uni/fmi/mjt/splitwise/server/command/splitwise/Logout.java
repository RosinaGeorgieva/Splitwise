package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;

public class Logout extends SplitwiseCommand {
    public Logout(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        return authResponse;
    }
}
