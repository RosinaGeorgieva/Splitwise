package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class Register extends SplitwiseCommand {
    public Register(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        String username = command.arguments()[0];
        Profile newProfile = new Profile(username);

        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase) database;
        splitwiseDatabase.getProfilesRepository().add(newProfile);

        String profileInfo = newProfile.toString();
        saveToFile(profileInfo, Filenames.PROFILES);

        return String.format(Messages.SUCCESSFULLY_REGISTERED, newProfile.getUsername(), Delimiters.LINE_SEPARATOR);
    }
}
