package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

public class Logout extends SplitwiseCommand {
    public Logout(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        String requestingUser = extractUsername(authResponse);

        Profile requestedUserProfile = ((SplitwiseDatabase)database).getProfilesRepository().getByUsername(requestingUser);
        requestedUserProfile.setNotifications(true);

        return authResponse;
    }

    private String extractUsername(String authResponse) {
        int startOfName = authResponse.indexOf(Delimiters.DASH) + 1;
        int endOfName = authResponse.lastIndexOf(Delimiters.DASH);
        return authResponse.substring(startOfName, endOfName);
    }
}
