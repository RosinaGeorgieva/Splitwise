package bg.sofia.uni.fmi.mjt.splitwise.server.command.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.SplitwiseCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.repository.DebtRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.record.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Formats;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Payed extends SplitwiseCommand {
    public Payed(Command command, String authResponse) {
        super(command, authResponse);
    }

    @Override
    public String execute(Database database) {
        if (!authResponse.contains(Messages.OK)) {
            return authResponse;
        }

        SplitwiseDatabase splitwiseDatabase = (SplitwiseDatabase) database;

        DebtRepository debtRepository = splitwiseDatabase.getDebtRepository();

        String requestingUser = authResponse.split(Delimiters.HASHTAG)[1];
        double sumToBePayed = Double.valueOf(command.arguments()[0]);
        String requestedUser = command.arguments()[1];

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

        StringBuilder message = new StringBuilder();
        message.append(String.format(Formats.PAYED_FORMAT, requestedUser, sumToBePayed, Delimiters.LINE_SEPARATOR));

        return message.toString(); //da dobavq currently owes you, ako ostane vreme
    }
}
