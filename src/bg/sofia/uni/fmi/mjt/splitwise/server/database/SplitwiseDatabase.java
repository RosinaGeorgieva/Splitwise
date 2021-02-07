package bg.sofia.uni.fmi.mjt.splitwise.server.database;

import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.DebtRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.GroupsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.ProfilesRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.UserCredentialsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplitwiseDatabase implements Database {
    private final ProfilesRepository profilesRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final GroupsRepository groupsRepository;
    private final DebtRepository debtRepository;
    private final Map<String, ArrayList<String>> notifications;

    public SplitwiseDatabase() {
        this.userCredentialsRepository = new UserCredentialsRepository(Filenames.USER_CREDENTIALS);
        this.groupsRepository = new GroupsRepository(Filenames.GROUPS);
        this.debtRepository = new DebtRepository();
        this.profilesRepository = new ProfilesRepository(this.userCredentialsRepository, this.groupsRepository, this.debtRepository);
        this.notifications = new HashMap<>();
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

    public ArrayList<String> popNotifications(String username) { //ime?
        if(!this.notifications.containsKey(username)) {
            return new ArrayList<>();
        }
        ArrayList<String> notifications = this.notifications.get(username);
        this.notifications.remove(username);
        return notifications;
    }

    public void addNotification(String username, String notification) {
        if(this.notifications.containsKey(username)) {
            this.notifications.get(username).add(notification);
        } else {
            ArrayList<String> notifications = new ArrayList<>();
            notifications.add(notification);
            this.notifications.put(username, notifications);
        }
    }
}
