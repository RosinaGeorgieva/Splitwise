package bg.sofia.uni.fmi.mjt.splitwise.server.database.repository;

import bg.sofia.uni.fmi.mjt.splitwise.server.record.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

public class DebtRepository extends AbstractRepository<Debt> {

    public DebtRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected Debt makeObjectFromLine(String line) {
        String[] debtData = line.split(Delimiters.HASHTAG);

        String whoOwes = debtData[0];
        String toWhom = debtData[1];
        Double amount = Double.valueOf(debtData[2]);
        String reason = debtData[3];

        return new Debt(whoOwes, toWhom, amount, reason);
    }
}
