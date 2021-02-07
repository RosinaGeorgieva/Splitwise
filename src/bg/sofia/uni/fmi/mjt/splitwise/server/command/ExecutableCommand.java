package bg.sofia.uni.fmi.mjt.splitwise.server.command;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class ExecutableCommand {
    protected final Command command;

    public ExecutableCommand(Command command) {
        this.command = command;
    }

    protected void saveToFile(String toBeWritten, String filename) {
        try (var printWriter = new PrintWriter(new FileWriter(filename, true))) {
            printWriter.println(toBeWritten);
        } catch (IOException exception) {
            //TODO
        }
    }
    public abstract String execute(Database database);
}
