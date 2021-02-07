package bg.sofia.uni.fmi.mjt.splitwise.server.command;

import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;

public class UnknownCommand extends ExecutableCommand { //TODO
    public UnknownCommand(Command command) {
        super(command);
    }

    @Override
    public String execute(Database database) {
        return null;
    }
}
