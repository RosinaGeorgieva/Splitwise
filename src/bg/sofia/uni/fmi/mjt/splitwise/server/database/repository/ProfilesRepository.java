package bg.sofia.uni.fmi.mjt.splitwise.server.database.repository;

import bg.sofia.uni.fmi.mjt.splitwise.server.record.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProfilesRepository extends AbstractRepository<Profile> {
    public ProfilesRepository(UserCredentialsRepository userCredentialsRepository, GroupsRepository groupsRepository, DebtRepository debtRepository) {
        for(String username : userCredentialsRepository.getAllUsernames()) {
            Set<Profile> friends = getFriends(groupsRepository, username);
            Set<Group> groups = getGroups(groupsRepository, username);

            collection.add(new Profile(username, friends, groups));
        }
    }

    @Override
    protected Profile makeObjectFromLine(String line) {
        return null;//TODO
    }

    public Profile getByUsername(String username) {
        for(Profile profile : collection) {
            if(profile.getUsername().equals(username)) {
                return profile;
            }
        }
        return null; //TODO
    }

    public Set<String> getAllUsernames() {
        return collection.stream().map(Profile::getUsername).collect(Collectors.toSet());
    }
//da opravq - vuv faila se pazqt kakto predi se pazeha s #[]#[]
    private Set<Profile> getFriends(GroupsRepository groupsRepository, String username) {
        Collection<Group> groups = groupsRepository.getAll();
        Set<Profile> friends = new HashSet<>();
        for(Group group : groups) {
            if(group.groupName().equals(Delimiters.TWO_FRIEND_GROUP)) {
                if(group.creator().equals(username)) {
                    Profile friend = null;
                    for(Profile member : group.members()) {
                        if(!member.equals(new Profile(username))) {
                            friend = member;
                        }
                    }
                    friends.add(friend);
                }
                if(group.members().contains(new Profile(username))) {
                    friends.add(new Profile(group.creator()));
                }
            }
        }
        return friends;
    }

    private Set<Group> getGroups(GroupsRepository groupsRepository, String username) {
        return groupsRepository.getAll().stream()
                .filter(group -> group.creator().equals(username) || group.members().contains(new Profile(username)))
                .filter(group -> !group.groupName().equals(Delimiters.TWO_FRIEND_GROUP))
                .collect(Collectors.toSet());
    }
}

