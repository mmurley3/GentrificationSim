/**
 * Created by Daniel on 3/31/2018.
 */
public class Tile {
    private double propertyValue;
    private Property household;

    public Tile(double propertyValue, Property household) {
        this.propertyValue = propertyValue;
        this.household = household;
    }

    double getPropertyValue() {
        return this.propertyValue;
    }

    Property getHouseholds() {
        return this.household;
    }
}
