package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Login extends SplitwiseCommand {
    public Login(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        String requestedUser = command.arguments()[0];

        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase) database;
        ArrayList<String> notifications = splitwiseDatabase.popNotifications(requestedUser);

        Profile requestedUserProfile = splitwiseDatabase.getProfilesRepository().getByUsername(requestedUser);
        requestedUserProfile.setNotifications(false);

        if (!loginIsSuccessful()) {
            return authResponse;
        }

        StringBuilder response = new StringBuilder();
        response.append(authResponse);
        response.append(Messages.NOTIFICATIONS);

        if (notifications.size() == 0) {
            response.append(String.format(Messages.NO_NOTIFICATIONS, Delimiters.LINE_SEPARATOR));
        } else {
            response.append(notifications.stream().collect(Collectors.joining()));
            response.append(Delimiters.LINE_SEPARATOR);
        }

        return response.toString();
    }

    private boolean loginIsSuccessful() {
        return !authResponse.equals(Messages.INCORRECT_CREDENTIALS)
                && !authResponse.equals(Messages.ALREADY_LOGGED_IN);
    }
}
