package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.DebtRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.GroupsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.ProfilesRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Formats;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetStatus extends SplitwiseCommand { //silno ne raboti

    private static final String FRIENDS_TITLE = "Friends:" + Delimiters.LINE_SEPARATOR;
    private static final String GROUPS_TITLE = "Groups:" + Delimiters.LINE_SEPARATOR;
    private static final String OWED_BY_YOU = "Owed by you: " + Delimiters.LINE_SEPARATOR;
    private static final String OWED_BY_FRIENDS = "Owed by them to you: " + Delimiters.LINE_SEPARATOR;

    public GetStatus(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];
        String friendsDebts = friendsDebtsStatus(database, requestingUser);
        String groupDebts = groupsDebtsStatus(database, requestingUser);

        return friendsDebts + Delimiters.LINE_SEPARATOR + groupDebts;
    }

    private String groupsDebtsStatus(Database database, String requestingUser) {
        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase) database;

        DebtRepository debtRepository = splitwiseDatabase.getDebtRepository();
        GroupsRepository groupsRepository = splitwiseDatabase.getGroupsRepository();

        List<String> owedByFriends = new ArrayList<>();
        List<String> owedByMe = new ArrayList<>();

        for (Group group : groupsRepository.getAll()) {
            if (!group.groupName().equals(Delimiters.TWO_FRIEND_GROUP)) {
                owedByFriends.add(String.format(Formats.GROUP_TITLE_FORMAT, group.groupName(), Delimiters.LINE_SEPARATOR));
                owedByMe.add(String.format(Formats.GROUP_TITLE_FORMAT, group.groupName(), Delimiters.LINE_SEPARATOR));

                Set<Profile> members = group.members();
                for (Profile member : members) {
                    String friendUsername = member.getUsername();
                    if (!friendUsername.equals(requestingUser)) {
                        Double sumByMe = owedSum(debtRepository.getAll(), requestingUser, friendUsername);
                        owedByMe.add(String.format(Formats.YOU_OWE_TO_GROUP_FORMAT, friendUsername, sumByMe, Delimiters.LINE_SEPARATOR));

                        Double sumByThem = owedSum(debtRepository.getAll(), friendUsername, requestingUser);
                        owedByFriends.add(String.format(Formats.FRIEND_FROM_GROUP_OWES_YOU_FORMAT, friendUsername, sumByThem, Delimiters.LINE_SEPARATOR));
                    }
                }
            }
        }

        StringBuilder status = new StringBuilder();
        status.append(GROUPS_TITLE);
        status.append(OWED_BY_FRIENDS);
        status.append(owedByFriends.stream().collect(Collectors.joining())).append(Delimiters.LINE_SEPARATOR);

        status.append(OWED_BY_YOU);
        status.append(owedByMe.stream().collect(Collectors.joining()));
        return status.toString();
    }

    private String friendsDebtsStatus(Database database, String requestingUser) {
        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase) database;
        ProfilesRepository profilesRepository = splitwiseDatabase.getProfilesRepository();
        DebtRepository debtRepository = splitwiseDatabase.getDebtRepository();

        List<String> owedByFriends = new ArrayList<>();
        List<String> owedByMe = new ArrayList<>();

        for (Profile friend : profilesRepository.getByUsername(requestingUser).getFriends()) {
            String friendUsername = friend.getUsername();
            if (!friendUsername.equals(requestingUser)) {
                Double sumByMe = owedSum(debtRepository.getAll(), requestingUser, friendUsername);
                owedByMe.add(String.format(Formats.YOU_OWE_FORMAT, friendUsername, sumByMe, Delimiters.LINE_SEPARATOR));

                Double sumByThem = owedSum(debtRepository.getAll(), friendUsername, requestingUser);
                owedByFriends.add(String.format(Formats.FRIEND_OWES_YOU_FORMAT, friendUsername, sumByThem, Delimiters.LINE_SEPARATOR));
            }
        } //formatiraneto

        StringBuilder status = new StringBuilder();
        status.append(FRIENDS_TITLE);
        status.append(OWED_BY_FRIENDS);
        status.append(owedByFriends.stream().collect(Collectors.joining()));
        status.append(Delimiters.LINE_SEPARATOR);
//neshto ne bachka kakto trqbwa

        status.append(OWED_BY_YOU);
        status.append(owedByMe.stream().collect(Collectors.joining()));
        return status.toString();
    }

    private double owedSum(Collection<Debt> debts, String whoOwes, String toWhom) {
        return debts.stream()
                .filter(debt -> debt.whoHasDebt().equals(whoOwes) && debt.toWhom().equals(toWhom))
                .mapToDouble(Debt::amountOfDebt)
                .sum();
    }
}
