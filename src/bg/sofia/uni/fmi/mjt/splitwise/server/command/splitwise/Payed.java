package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.DebtRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Filenames;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Formats;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Payed extends SplitwiseCommand {
    public Payed(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase)database;

        DebtRepository debtRepository = splitwiseDatabase.getDebtRepository();
        double sumToBePayed = Double.valueOf(command.arguments()[0]);
        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];
        String requestedUser = command.arguments()[1];
        payDebts(debtRepository, sumToBePayed, requestingUser, requestedUser);

        String historyFilename = Filenames.RESOURCE_DIR + requestedUser + Filenames.HISTORY;
        String notification = String.format(Formats.YOU_PAYED_FORMAT, requestingUser, sumToBePayed);

        saveToFile(notification, historyFilename);

        Profile friendProfile = splitwiseDatabase.getProfilesRepository().getByUsername(requestedUser);
        if(friendProfile.hasSetNotifications()) {
            splitwiseDatabase.addNotification(requestedUser, notification);
        }

        return String.format(Formats.FRIEND_PAYED_FORMAT, requestedUser, sumToBePayed, Delimiters.LINE_SEPARATOR);
    }

    private void payDebts(DebtRepository debtRepository, double sumToBePayed, String requestingUser, String requestedUser) {
        double remainingSumFromPayment = sumToBePayed;
        Set<Debt> copyOfRepository = new HashSet<>(debtRepository.getAll());
        for (Debt debt : copyOfRepository) {
            if(debt.whoHasDebt().equals(requestedUser) && debt.toWhom().equals(requestingUser)) {
                Double amountOfDebt = debt.amountOfDebt();
                if(amountOfDebt > sumToBePayed) {
                    Debt debtWithLoweredSum = new Debt(debt.whoHasDebt(), debt.toWhom(), amountOfDebt - remainingSumFromPayment, debt.reason());
                    debtRepository.remove(debt);
                    debtRepository.add(debtWithLoweredSum);
                    break;
                } else if (amountOfDebt == sumToBePayed) {
                    debtRepository.remove(debt);
                } else {
                    debtRepository.remove(debt);
                    remainingSumFromPayment = remainingSumFromPayment - debt.amountOfDebt();
                }
            }
        }
    }
}
