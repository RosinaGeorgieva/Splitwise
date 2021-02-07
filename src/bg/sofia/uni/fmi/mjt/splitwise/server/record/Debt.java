package bg.sofia.uni.fmi.mjt.splitwise.server.record;

import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters;

import java.util.Objects;

public record Debt(String whoHasDebt, String toWhom, Double amountOfDebt, String reason) {
    @Override
    public String toString() {
        return whoHasDebt + Delimiters.HASHTAG + toWhom + Delimiters.HASHTAG + amountOfDebt + Delimiters.HASHTAG + reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Objects.equals(whoHasDebt, debt.whoHasDebt) && Objects.equals(toWhom, debt.toWhom) && Objects.equals(amountOfDebt, debt.amountOfDebt) && Objects.equals(reason, debt.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whoHasDebt, toWhom, amountOfDebt, reason);
    }
}
