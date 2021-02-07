package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Help extends SplitwiseCommand {

    public Help(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        try {
            Stream<String> lines = Files.lines(Path.of(Filenames.MAN));
            String response = lines.collect(Collectors.joining(Delimiters.LINE_SEPARATOR));
            return response + Delimiters.LINE_SEPARATOR;
        } catch (IOException exception) {
            throw new RuntimeException(exception); //todo
        }
    }
}
