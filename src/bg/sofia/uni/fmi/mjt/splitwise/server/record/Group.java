package bg.sofia.uni.fmi.mjt.splitwise.server.record;

import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Group(String creator, String groupName, Set<Profile> members) {
    @Override
    public String toString() {
        return  creator + Delimiters.HASHTAG + groupName + Delimiters.HASHTAG +
                members.stream().map(Profile::getUsername).collect(Collectors.toSet()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(creator, group.creator) && Objects.equals(groupName, group.groupName) && Objects.equals(members, group.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, groupName, members);
    }

    public int numberOfMembers() {
        return members.size();
    }
}
