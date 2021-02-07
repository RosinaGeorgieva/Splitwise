package bg.sofia.uni.fmi.mjt.splitwise.server.command;

import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Formats;

import java.util.Arrays;
import java.util.stream.Collectors;

public record Command(Integer sessionId, String command, String[] arguments) {
    public String toRequest() {
        String argumentsString = Arrays.stream(arguments()).collect(Collectors.joining(Delimiters.SPACE));
        return String.format(Formats.COMMAND_FORMAT, sessionId, command, argumentsString, Delimiters.LINE_SEPARATOR);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Command)) {
            return false;
        }
        Command c = (Command) o;
        return sessionId.equals(c.sessionId) && command.equals(c.command) && Arrays.equals(arguments, c.arguments);
    }
}
