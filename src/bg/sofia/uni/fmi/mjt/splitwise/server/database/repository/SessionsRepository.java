package bg.sofia.uni.fmi.mjt.splitwise.server.database.repository;

import bg.sofia.uni.fmi.mjt.splitwise.server.record.UserCredentials;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//накрая да си създам един .jar с класчетата и да си ги ползвам в трите проекта
public class SessionsRepository { //da vidq nakraq dali shte imam osven username, i istinsko ime
    protected final Map<Integer, UserCredentials> userCredentialsBySessionId;

    public SessionsRepository() {
        this.userCredentialsBySessionId = new HashMap<>();
    }

    public void put(Integer key, UserCredentials value) {
        if (!userCredentialsBySessionId.keySet().contains(key)) {
            userCredentialsBySessionId.put(key, value);
        }
    }

    public void remove(Integer key) {
        userCredentialsBySessionId.remove(key);
    }

    public UserCredentials get(Integer key) {
        return this.userCredentialsBySessionId.get(key);
    }

    public Set<Integer> keySet() {
        return userCredentialsBySessionId.keySet();
    }

    public boolean contains(Integer key) {
        return this.userCredentialsBySessionId.keySet().contains(key);
    }

    public boolean containsValue(Integer value) {
        return this.userCredentialsBySessionId.values().contains(value);
    }

    public Collection<UserCredentials> values() {
        return this.userCredentialsBySessionId.values();
    }
}
