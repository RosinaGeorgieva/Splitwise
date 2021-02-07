package bg.sofia.uni.fmi.mjt.splitwise.server.record;

import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Profile { // ne v tozi paket
    private String username;
    //tuk nqkakwi fajlove v koito se pazi statusyt
    private Set<Profile> friends;
    private Set<Group> groups;

    public Profile(String username) {
        this.username = username;
        this.friends = new HashSet<>();
        this.groups = new HashSet<>();
    }

    public Profile(String username, Set<Profile> friends, Set<Group> groups) {
        this.username = username;
        this.friends = friends;
        this.groups = groups;
    }

    @Override
    public String toString() {
        return username + Delimiters.HASHTAG;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(username, profile.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public String getUsername() {
        return this.username;
    }

    public Set<Profile> getFriends() {
        return this.friends;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public void addFriend(Profile profile) {
        friends.add(profile);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }
}
