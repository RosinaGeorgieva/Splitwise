package bg.sofia.uni.fmi.mjt.splitwise.server.command;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication.ConfirmDisconnect;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication.ConfirmHelp;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication.ConfirmLoggedIn;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication.ConfirmLogin;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication.ConfirmLogout;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication.ConfirmRegistration;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.AddFriend;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.CreateGroup;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.Disconnect;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.GetHistory;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.GetStatus;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.Help;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.Login;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.Logout;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.Payed;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.Register;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.Split;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise.SplitGroup;
import bg.sofia.uni.fmi.mjt.splitwise.server.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Commands;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.util.Arrays;
import java.util.Set;

public class ExecutableCommandCreator {
    private static final Set<String> POSSIBLE_CMD = Commands.getAll();

    public static ExecutableCommand createSplitwiseCommand(String... request) throws UnknownCommandException {
        Integer sessionId = Integer.valueOf(extractIthWordFrom(request[0], 0));
        String commandStringRepres = extractIthWordFrom(request[0], 1);

        if (!POSSIBLE_CMD.contains(commandStringRepres)) {
            throw new UnknownCommandException(Messages.UNKNOWN_COMMAND);
        }

        String[] allWords = request[0].replaceAll(Delimiters.LINE_SEPARATOR, Delimiters.EMPTY_STRING).split(Delimiters.SPACE);
        String[] arguments = Arrays.copyOfRange(allWords, 2, allWords.length);

        if (!hasEnoughArguments(commandStringRepres, arguments)) {
            throw new UnknownCommandException(Messages.UNKNOWN_COMMAND);
        }

        Command command = new Command(sessionId, commandStringRepres, arguments);
        if(request.length == 1) {
            return createAuthenticationCommand(command);
        }

        return createSplitwiseCommand(command, request);
    }

    private static ExecutableCommand createAuthenticationCommand(Command command) {
        return switch (command.command()) {
            case Commands.ADD_FRIEND,
                    Commands.CREATE_GROUP,
                    Commands.SPLIT,
                    Commands.SPLIT_GROUP,
                    Commands.PAYED,
                    Commands.GET_STATUS,
                    Commands.GET_HISTORY -> new ConfirmLoggedIn(command);
            case Commands.HELP -> new ConfirmHelp(command);
            case Commands.REGISTER -> new ConfirmRegistration(command);
            case Commands.LOGIN -> new ConfirmLogin(command);
            case Commands.LOGOUT -> new ConfirmLogout(command);
            case Commands.DISCONNECT -> new ConfirmDisconnect(command);
            default -> new UnknownCommand(command);
        };
    }

    private static ExecutableCommand createSplitwiseCommand(Command command, String... request) { //da go opravq s design pattern
        return switch (command.command()) {
            case Commands.REGISTER -> new Register(command, request[1]);
            case Commands.LOGIN -> new Login(command, request[1]);
            case Commands.LOGOUT -> new Logout(command, request[1]);
            case Commands.DISCONNECT -> new Disconnect(command, request[1]);
            case Commands.ADD_FRIEND -> new AddFriend(command, request[1]);
            case Commands.CREATE_GROUP -> new CreateGroup(command, request[1]);
            case Commands.GET_STATUS -> new GetStatus(command, request[1]);
            case Commands.SPLIT -> new Split(command, request[1]);
            case Commands.SPLIT_GROUP -> new SplitGroup(command, request[1]);
            case Commands.PAYED -> new Payed(command, request[1]);
            case Commands.GET_HISTORY -> new GetHistory(command, request[1]);
            case Commands.HELP -> new Help(command, request[1]);
            default -> new UnknownCommand(command);
        };
    }

    private static String extractIthWordFrom(String request, int i) {
        return request.replaceAll(Delimiters.LINE_SEPARATOR, Delimiters.EMPTY_STRING).split(Delimiters.SPACE)[i];
    }

    private static boolean hasEnoughArguments(String commandName, String[] arguments) { //150000 switch-a?
        switch (commandName) {
            case Commands.LOGOUT:
            case Commands.DISCONNECT:
            case Commands.GET_STATUS:
            case Commands.HELP:
            case Commands.GET_HISTORY:
                return arguments.length == 0;
            case Commands.ADD_FRIEND:
                return arguments.length == 1;
            case Commands.REGISTER:
            case Commands.LOGIN:
            case Commands.PAYED:
                return arguments.length == 2;
            case Commands.SPLIT:
            case Commands.SPLIT_GROUP:
            case Commands.CREATE_GROUP:
                return arguments.length >= 3;
            default:
                return arguments.length >= 2; //ОТ ТУК ЕВЕНТУАЛНО ГРЕШКА
        }
    }
}
