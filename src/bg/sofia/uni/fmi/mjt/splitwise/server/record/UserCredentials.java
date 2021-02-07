package bg.sofia.uni.fmi.mjt.splitwise.server.record;

import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

import java.util.Objects;

public record UserCredentials(String username, String password) {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCredentials userCredentials = (UserCredentials) o;
        return Objects.equals(username, userCredentials.username) && Objects.equals(password, userCredentials.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return username + Delimiters.HASHTAG + password;
    }
}
