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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateGroup extends SplitwiseCommand {
    public CreateGroup(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase)database;

        String[] members = Arrays.copyOfRange(command.arguments(), 1, command.arguments().length);
        Set<String> allUsernames = splitwiseDatabase.getProfilesRepository().getAllUsernames();
        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];
        for (String member : members) {
            if (!allUsernames.contains(member)) {
                return String.format(Messages.NO_SUCH_REGISTERED, member, Delimiters.LINE_SEPARATOR);
            }
            if(member.equals(requestingUser)) {
                return String.format(Messages.ADD_YOURSELF_TO_GROUP, Delimiters.LINE_SEPARATOR);
            }
        }
//NB DA PODDRYJA TOZI TEST CASE
// => split 16 sanya x-factor neshto
//[ Successfully added 8,00LV owed by sanya for x-factor ]
        if (members.length < 2) {
            return String.format(Messages.NOT_ENOUGH_USERS, Delimiters.LINE_SEPARATOR);
        }

        Set<Profile> profilesOfMembers = Arrays.stream(members).map(member -> new Profile(member)).collect(Collectors.toSet());
        profilesOfMembers.add(splitwiseDatabase.getProfilesRepository().getByUsername(requestingUser));
        Group friendGroup = new Group(requestingUser, command.arguments()[0], profilesOfMembers);

        if (splitwiseDatabase.getGroupsRepository().contains(friendGroup)) {
            return String.format(Messages.GROUP_ALREADY_EXISTS, Delimiters.LINE_SEPARATOR);
        }

        splitwiseDatabase.getGroupsRepository().add(friendGroup);

        //тук да сложа добавяне на нова група в профилите na wsichki
        for(Profile member : profilesOfMembers) {
            member.addGroup(friendGroup);
        }

        String groupInfo = friendGroup.toString();
        saveToFile(groupInfo, Filenames.GROUPS);

        System.out.println(splitwiseDatabase.getGroupsRepository().getAll());
        return String.format(Messages.SUCCESSFULLY_CREATED_GROUP, command.arguments()[0], requestingUser, Delimiters.LINE_SEPARATOR);
    }
}
