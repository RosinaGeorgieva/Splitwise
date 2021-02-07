package bg.sofia.uni.fmi.mjt.splitwise.server.database;

import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.SessionsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.UserCredentialsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;

public class AuthDatabase implements Database {
    private final SessionsRepository sessionRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    public AuthDatabase() {
        this.sessionRepository = new SessionsRepository();
        this.userCredentialsRepository = new UserCredentialsRepository(Filenames.USER_CREDENTIALS);
    }

    public SessionsRepository getSessionRepository() {
        return this.sessionRepository;
    }

    public UserCredentialsRepository getUserCredentialsRepository() {
        return this.userCredentialsRepository;
    }
}
