package bg.sofia.uni.fmi.mjt.splitwise.server.database;

import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.DebtRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.GroupsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.ProfilesRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.UserCredentialsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;

public class SplitwiseDatabase implements Database {
    private final ProfilesRepository profilesRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final GroupsRepository groupsRepository;
    private final DebtRepository debtRepository;

    public SplitwiseDatabase() {
        this.userCredentialsRepository = new UserCredentialsRepository(Filenames.USER_CREDENTIALS);
        this.groupsRepository = new GroupsRepository(Filenames.GROUPS);
        this.debtRepository = new DebtRepository(Filenames.DEBTS);
        this.profilesRepository = new ProfilesRepository(this.userCredentialsRepository, this.groupsRepository, this.debtRepository);
    }

    public ProfilesRepository getProfilesRepository() {
        return this.profilesRepository;
    }

    public GroupsRepository getGroupsRepository() {
        return this.groupsRepository;
    }

    public DebtRepository getDebtRepository() {
        return this.debtRepository;
    }
}
