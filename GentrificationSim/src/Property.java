
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Daniel on 3/31/2018.
 */
public class Property {
    private double propertyValue;
    private PropertyType type;
    private ArrayList<Household> households;
    private int maxHouseholds;

    public Property(double propertyValue, ArrayList<Household> households, int maxHouseholds, PropertyType type) {
        this.propertyValue = propertyValue;
        this.households  = households;
        this.maxHouseholds = maxHouseholds;
        this.type = type;
    }

    public void setMaxHouseholds(int maxHouseholds) {
        this.maxHouseholds = maxHouseholds;
    }

    public int getMaxHouseholds() {
        return this.maxHouseholds;
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

    public ArrayList<Household> getHouseholds() {
    	if (type != PropertyType.RESIDENTIAL) {
    		return null;
    	}
    	return this.households;
    }
    
    public void addHousehold(Household h) {
    	households.add(h);
    }
    
    public void removeHousehold(Household h) {
    	if (households.size() == 0) {
    		System.out.println("No Households remain");
    	} else if (!households.contains(h)) {
    		System.out.println("That Household does not exist");
    	} else {
    		households.remove(h);
    	}
    }

    public double averageHouseholdIncome() {
    	if (type != PropertyType.RESIDENTIAL || households.size() == 0) {
    		return 0;
    	}
    	double sum = 0.0;
    	for (Household h: households) {
            if (h != null) {
                sum += h.getRentBudgetHigh();
            }
    	}

    	return sum / households.size();
    }

    public void printType() {
        if (type == PropertyType.COMMERCIAL) {
            System.out.println("COMM");
        } else if (type == PropertyType.INDUSTRIAL) {
            System.out.println("IND");
        } else if (type == PropertyType.RESIDENTIAL) {
            System.out.println("RES");
        } else if (type == PropertyType.PARK) {
            System.out.println("PARK");
        }
    }
}
