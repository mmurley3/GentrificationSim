/**
 * Created by Daniel on 3/31/2018.
 */
public class Property {
    private double propertyValue;
    private PropertyType type;
    private Household[] households;

    public Property(double propertyValue, Household[] households, PropertyType type) {
        this.propertyValue = propertyValue;
        this.households = households;
        this.type = type;
    }

    public void setPropertyValue(double value) {
    	this.propertyValue = value;
    }

    public double getPropertyValue() {
        return this.propertyValue;
    }

    public PropertyType getType() {
    	return type;
    }

    public Household[] getHousehold() {
    	if (type != PropertyType.RESIDENTIAL) {
    		return null;
    	}
    	return this.households;
    }

    public double averageHouseholdIncome() {
    	if (type != PropertyType.RESIDENTIAL) {
    		return 0;
    	}
    	int avg = 0;
    	for (Household h: households) {
    		avg += h.getRentBudgetHigh();
    	}
    	return avg;
    }
}
