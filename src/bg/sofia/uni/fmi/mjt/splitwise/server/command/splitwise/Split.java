package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.DebtRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.ProfilesRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class Split extends SplitwiseCommand {
    public Split(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }
//da ne moga da si split-vam sys sebe si;
        //da ne moga da split-vam s nesyshtestvuvasht
        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];
        Double debtAmount = Double.valueOf(command.arguments()[0]) / 2; //da vida dali e consistent s tova, koeto iskat
        String requestedUser = command.arguments()[1];
        String reason = command.arguments()[2];

        ProfilesRepository profilesRepository = ((SplitwiseDatabase) database).getProfilesRepository();
        if(!profilesRepository.getAllUsernames().contains(requestedUser)) {
            return String.format(Messages.NO_SUCH_REGISTERED, requestedUser, Delimiters.LINE_SEPARATOR);
        }

        DebtRepository debtRepository = ((SplitwiseDatabase)database).getDebtRepository();
        Debt newDebt = new Debt(requestedUser, requestingUser, debtAmount, reason); //debt da ima vuzmojnost za poveche ot 1 ednakyv debt
        debtRepository.add(newDebt);

        saveToFile(newDebt.toString(), Filenames.DEBTS);
//        System.out.println(debtRepository.getAll());
        return String.format(Messages.OBLIGATION_ADDED_SUCCESSFULLY, debtAmount, requestedUser, reason, Delimiters.LINE_SEPARATOR);
    }
}
