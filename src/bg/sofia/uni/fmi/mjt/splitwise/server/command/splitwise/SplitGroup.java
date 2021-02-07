package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.GroupsRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.ProfilesRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Formats;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.util.Set;

public class SplitGroup  extends SplitwiseCommand {
    public SplitGroup(Command command, String authResponse) {
        super(command, authResponse);
    }
//N.B. NQMAM VALIDACIQ NIKYDE!!!!
    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase)database;

        String groupName = command.arguments()[1];
        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];

        GroupsRepository groupsRepository = splitwiseDatabase.getGroupsRepository();
        Group group = groupsRepository.getByName(groupName);

        if(group == null) {
            return String.format(Messages.NO_SUCH_GROUP, groupName, Delimiters.LINE_SEPARATOR);
        }

        String reason = command.arguments()[2];
        int numberOfMembersInGroup = group.numberOfMembers();
        Double amountOfDebt = Double.parseDouble(command.arguments()[0]) / numberOfMembersInGroup;

        ProfilesRepository profilesRepository = splitwiseDatabase.getProfilesRepository();
        Profile requestingUserProfile = profilesRepository.getByUsername(requestingUser);

        Set<Profile> members = group.members();
        for (Profile member : members) {
            if(!member.equals(requestingUserProfile)) {
                Debt debt = new Debt(member.getUsername(), requestingUser, amountOfDebt, reason);
                splitwiseDatabase.getDebtRepository().add(debt);
                if(member.hasSetNotifications()) {
                    String notification = String.format(Formats.YOU_PAYED_FORMAT, requestingUser, amountOfDebt);
                    ((SplitwiseDatabase) database).addNotification(member.getUsername(), notification);
                }
            }
        }

        Double totalAmountOwed = (numberOfMembersInGroup - 1) * amountOfDebt;
        return String.format(Messages.OBLIGATION_ADDED_SUCCESSFULLY, totalAmountOwed, group.groupName(), reason, Delimiters.LINE_SEPARATOR);
    }
}
