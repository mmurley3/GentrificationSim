/**
 * Created by Daniel on 3/31/2018.
 */
public class Property {
    private int id;
    private double rentBudgetHigh;
    private double rentBudgetLow;
    private boolean isVacant;

    public Property(int id, double rentBudgetHigh, double rentBudgetLow) {
        this.id = id;
        this.rentBudgetHigh = rentBudgetHigh;
        this.rentBudgetLow = rentBudgetLow;
    }

    int getId() {
        return this.id;
    }

    double getrentBudgetHigh() {
        return this.rentBudgetHigh;
    }

    public double getRentBudgetLow() {
        return this.rentBudgetLow;
    }
}
