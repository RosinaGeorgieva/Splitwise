package bg.sofia.uni.fmi.mjt.splitwise.server.database.repository;

import bg.sofia.uni.fmi.mjt.splitwise.server.record.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupsRepository extends AbstractRepository<Group> {
    public GroupsRepository(String groupFileName) {
        super(groupFileName);
    }

    @Override
    protected Group makeObjectFromLine(String line) {
        String[] groupData = line.split(Delimiters.HASHTAG);

        String creator = groupData[0];
        String title = groupData[1];
        String[] members;
        if (groupData[2].contains(Delimiters.COMMA)) {
            members = groupData[2].replaceAll(Delimiters.OPENING_BRACKET, Delimiters.EMPTY_STRING)
                    .replaceAll(Delimiters.CLOSING_BRACKET, Delimiters.EMPTY_STRING)
                    .replaceAll(Delimiters.SPACE, Delimiters.EMPTY_STRING).split(Delimiters.COMMA);
        } else {
            members = new String[1];
            members[0] = groupData[2].replaceAll(Delimiters.OPENING_BRACKET, Delimiters.EMPTY_STRING)
                    .replaceAll(Delimiters.CLOSING_BRACKET, Delimiters.EMPTY_STRING);
        }

        Set<Profile> profilesOfMembers = Arrays.stream(members)
                .map(member -> new Profile(member))
                .collect(Collectors.toSet());

        return new Group(creator, title, profilesOfMembers);
    }

    public Group getByName(String name) {
        for(Group group : collection) {
            if(group.groupName().equals(name)) {
                return group;
            }
        }
        return null; //TODO
    }
}
