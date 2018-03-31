/**
 * Created by Daniel on 3/31/2018.
 */
public class Household {
    private int id;
    private double rentBudgetHigh;
    private double rentBudgetLow;

    public Household(int id, double rentBudgetHigh, double rentBudgetLow) {
        this.id = id;
        this.rentBudgetHigh = rentBudgetHigh;
        this.rentBudgetLow = rentBudgetLow;
    }

    int getId() {
        return this.id;
    }

    double getRentBudgetHigh() {
        return this.rentBudgetHigh;
    }

    public double getRentBudgetLow() {
        return this.rentBudgetLow;
    }
}
