package bg.sofia.uni.fmi.mjt.splitwise.server.database.repository;

import bg.sofia.uni.fmi.mjt.splitwise.server.record.UserCredentials;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

import java.util.Set;
import java.util.stream.Collectors;

public class UserCredentialsRepository extends AbstractRepository<UserCredentials> {

    public UserCredentialsRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected UserCredentials makeObjectFromLine(String line) {
        String username = line.split(Delimiters.HASHTAG)[0];
        String password = line.split(Delimiters.HASHTAG)[1];
        return new UserCredentials(username, password);
    }

    public Set<String> getAllUsernames() {
        return collection.stream().map(UserCredentials::username).collect(Collectors.toSet());
    }
}
