package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class GetHistory extends SplitwiseCommand {
    public GetHistory(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];

        String historyFilename = "resource\\" + requestingUser + "-history.txt";
        StringBuilder history = new StringBuilder();
        try {
            Stream<String> lines = Files.lines(Path.of(historyFilename));
            lines.forEach(line -> history.append(line).append(Delimiters.LINE_SEPARATOR));
        } catch (IOException exception) {
            //todo
        }

        if(history.toString().equals(Delimiters.EMPTY_STRING)) {
            return String.format(Messages.NO_PAYMENTS_EXECUTED, Delimiters.LINE_SEPARATOR);
        }

        return history.toString();
    }
}
