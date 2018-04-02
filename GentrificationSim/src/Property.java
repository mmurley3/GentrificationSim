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
    	if (type != PropertyType.RESIDENTIAL || households == null) {
    		return 0;
    	}
    	double sum = 0.0;
    	for (Household h: households) {
            if (h != null) {
                sum += h.getRentBudgetHigh();
            }
    	}
    	return sum / households.length;
    }
}
