import java.util.ArrayList;

/**
 * Created by Daniel on 3/31/2018.
 */

public class PropertyGrid {
    private int height;
    private int width;
    private Property[][] grid;

    private double parkConst = 0.2;
    private double commercialConst = 0.2;
    private double industrialConst = -0.2;
    private double adjPropertyConst = 0.2;
    private double economicStatusConst = 0.2;

    public PropertyGrid(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Property[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {


            }
        }
    }
    
    public PropertyGrid(Property[][] grid) {
    	this.grid = grid;
    	this.height = grid.length;
    	this.width = grid[0].length;
    }
    
    public Property getProperty(int height, int width) {
    	return grid[height][width];
    }

    public void evaluateAllPropertyValues() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j].getType() == PropertyType.RESIDENTIAL) {
                    double newPropValue = evaluatePropertyValue(i, j);
                    grid[i][j].setPropertyValue(newPropValue);
                }
            }
        }
    }

    public double evaluatePropertyValue(int i , int j) {
        double parkValue = parkConst * getPropertyTypeValue(i, j, PropertyType.PARK);
        double commercialValue = commercialConst * getPropertyTypeValue(i, j, PropertyType.COMMERCIAL);
        double industrialValue = industrialConst * getPropertyTypeValue(i, j, PropertyType.INDUSTRIAL);
        double adjPropValue = adjPropertyConst * getAdjPropValues(i, j);
        double economicStatusValue = economicStatusConst * grid[i][j].averageHouseholdIncome();

        return parkValue + commercialValue + industrialValue + adjPropValue + economicStatusValue;
    }

    public double getPropertyTypeValue(int x, int y, PropertyType type) {
        double propValSum = 0.0;
        int numProps = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j].getType() == type) {
                    double dist = getDistance(i, j, x, y);
                    double val = grid[i][j].getPropertyValue();
                    propValSum += val / dist;
                    numProps++;
                }
            }
        }

        if (numProps == 0) {
            return 0.0;
        } else {
            return propValSum / numProps;
        }

    }

    public double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    double getAdjPropValues(int i, int j) {
        double sum = 0.0;
        int num = 0;
        if (j != height-1) {
            double up = grid[i][j+1].getPropertyValue();
            sum += up;
            num++;
        }
        if (j != 0) {
            double down = grid[i][j-1].getPropertyValue();
            sum += down;
            num++;
        }
        if (i != width-1) {
            double right = grid[i+1][j].getPropertyValue();
            sum += right;
            num++;
        }
        if (i != 0) {
            double left = grid[i-1][j].getPropertyValue();
            sum += left;
            num++;
        }

        return sum / num;
    }

    public void printPropertyValues() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(grid[i][j].getPropertyValue() + ", ");
            }
            System.out.println();
        }
    }
    
    public ArrayList<Household> evaluateResidences() {
    	ArrayList<Household> displaced = new ArrayList<Household>();
    	for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j].getType() == PropertyType.RESIDENTIAL) {
                    ArrayList<Household> houses = grid[i][j].getHouseholds();
                    for (Household h: houses) {
                    	if (grid[i][j].getPropertyValue() < h.getRentBudgetLow()
                    			|| grid[i][j].getPropertyValue() > h.getRentBudgetHigh()) {
                    		displaced.add(h);
                    		grid[i][j].removeHousehold(h);
                    	}
                    }
                }
            }
        }
    	return displaced;
    }
}
