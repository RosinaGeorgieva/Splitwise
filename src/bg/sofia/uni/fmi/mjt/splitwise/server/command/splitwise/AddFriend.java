package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.util.Set;

public class AddFriend extends SplitwiseCommand {
    public AddFriend(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase) database;

        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        String requestedUser = command.arguments()[0];

        if(!profileExists(splitwiseDatabase, requestedUser)) {
            return String.format(Messages.NO_SUCH_REGISTERED, requestedUser, Delimiters.LINE_SEPARATOR);
        }

        Profile requestedUserProfile = splitwiseDatabase.getProfilesRepository().getByUsername(requestedUser);

        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];

        Group friendGroup = new Group(requestingUser, Delimiters.TWO_FRIEND_GROUP, Set.of(requestedUserProfile));

        if (splitwiseDatabase.getGroupsRepository().contains(friendGroup)) {
            return String.format(Messages.ALREADY_FRIENDS, requestedUserProfile.getUsername(), Delimiters.LINE_SEPARATOR);
        }

        splitwiseDatabase.getGroupsRepository().add(friendGroup);
        Profile requestingUserProfile = splitwiseDatabase.getProfilesRepository().getByUsername(requestingUser);

        //тук да сложа добавяне на нов приятел в профилите и на двамата
        requestingUserProfile.addFriend(requestedUserProfile);
        requestedUserProfile.addFriend(requestingUserProfile);

        String groupInfo = friendGroup.toString();
        saveToFile(groupInfo, Filenames.GROUPS);

        return String.format(Messages.SUCCESSFULLY_ADDED_FRIEND, requestedUserProfile.getUsername(), Delimiters.LINE_SEPARATOR);
    }

    private boolean profileExists(SplitwiseDatabase database, String username) {
        return database.getProfilesRepository().getAllUsernames().contains(username);
    }
}
