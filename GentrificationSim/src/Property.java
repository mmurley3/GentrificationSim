/**
 * Created by Daniel on 3/31/2018.
 */
public class Property {
    private double propertyValue;
    private PropertyType type;
    private Household household;

    public Property(double propertyValue, Household household) {
        this.propertyValue = propertyValue;
        this.household = household;
    }

    double getPropertyValue() {
        return this.propertyValue;
    }

    Household getHousehold() {
        return this.household;
    }
}
