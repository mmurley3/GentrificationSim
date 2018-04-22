import java.util.ArrayList;

/**
 * Created by Daniel on 3/31/2018.
 */

public class PropertyGrid {
    private int height;
    private int width;
    private Property[][] grid;

    private double originalConst = 0.6;
    private double parkConst = 0.3;
    private double commercialConst = 0.3;
    private double industrialConst = -0.3;
    private double adjPropertyConst = 0.05;
    private double economicStatusConst = 0.05;

    public PropertyGrid(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Property[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {


            }
        }
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
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
                if (grid[j][i].getType() == PropertyType.RESIDENTIAL) {
                    double newPropValue = evaluatePropertyValue(i, j);
                    grid[j][i].setPropertyValue(newPropValue);
                }
            }
        }
    }

    public double evaluatePropertyValue(int i , int j) {
        double originalPropValue = originalConst * grid[j][i].getPropertyValue();
        double parkValue = parkConst * getPropertyTypeValue(i, j, PropertyType.PARK);
        double commercialValue = commercialConst * getPropertyTypeValue(i, j, PropertyType.COMMERCIAL);
        double industrialValue = industrialConst * getPropertyTypeValue(i, j, PropertyType.INDUSTRIAL);
        double adjPropValue = adjPropertyConst * getAdjPropValues(i, j);
        double economicStatusValue = economicStatusConst * grid[j][i].averageHouseholdIncome();

        //System.out.println(originalPropValue +" + "+ parkValue +" + "+ commercialValue +" + "+ industrialValue +" + "+ adjPropValue +" + "+ economicStatusValue);

        return originalPropValue + parkValue + commercialValue + industrialValue + adjPropValue + economicStatusValue;
    }

    public double getPropertyTypeValue(int x, int y, PropertyType type) {
        double propValSum = 0.0;
        int numProps = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[j][i].getType() == type) {
                    double dist = getDistance(i, j, x, y);
                    double val = grid[j][i].getPropertyValue();
                    propValSum += val / (dist*0.5);
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
            double up = grid[j+1][i].getPropertyValue();
            sum += up;
            num++;
        }
        if (j != 0) {
            double down = grid[j-1][i].getPropertyValue();
            sum += down;
            num++;
        }
        if (i != width-1) {
            double right = grid[j][i+1].getPropertyValue();
            sum += right;
            num++;
        }
        if (i != 0) {
            double left = grid[j][i-1].getPropertyValue();
            sum += left;
            num++;
        }

        return sum / num;
    }

    public void printPropertyValues() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(grid[j][i].getPropertyValue() + ", ");
            }
            System.out.println();
        }
    }

    public ArrayList<Household> evaluateResidences() {
    	ArrayList<Household> displaced = new ArrayList<>();
    	for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[j][i].getType() == PropertyType.RESIDENTIAL) {
                    ArrayList<Household> houses = grid[j][i].getHouseholds();
                    for (int hI = 0; hI < houses.size(); hI++) {
                        Household h = houses.get(hI);
                    	if (grid[j][i].getPropertyValue() < h.getRentBudgetLow() || grid[j][i].getPropertyValue() > h.getRentBudgetHigh()) {
                            displaced.add(h);
                            grid[j][i].removeHousehold(h);
                    	}
                    }
                }
            }
        }
    	return displaced;
    }

    ArrayList<Household> generateNewHouseholds(int houseId, int numHouseholds) {
        ArrayList<Household> newHouseholds = new ArrayList<>();
        for (int i = 0; i < numHouseholds; i++) {
            newHouseholds.add(generateNewHousehold(houseId));
        }
        return newHouseholds;
    }

    Household generateNewHousehold(int houseId) {
        double maxRent = getMaxPropertyValue();
        double minRent = getMinPropertyValue();

        double houseMaxRent = minRent + Math.random() * (maxRent - minRent);
        double houseMinRent = minRent + Math.random() * (houseMaxRent - minRent);

        return new Household(houseId++, houseMaxRent, houseMinRent);
    }


    public double getMaxPropertyValue() {
        double max = 0.0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[j][i].getPropertyValue() > max) {
                    max = grid[j][i].getPropertyValue();
                }
            }
        }
        return max;
    }

    public double getMinPropertyValue() {
        double min = 0.0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[j][i].getPropertyValue() < min) {
                    min = grid[j][i].getPropertyValue();
                }
            }
        }
        return min;
    }

    public ArrayList<Household> relocateResidents(ArrayList<Household> relocatedHouseholds) {
        ArrayList<Household> movedOutOfGrid = new ArrayList<>();
        for (int h = 0; h < relocatedHouseholds.size(); h++) {
            int hI = 0;
            int hJ = 0;
            double rentBudgetDiff = Double.MAX_VALUE;
            Household res = relocatedHouseholds.get(h);
            boolean propertyFound = false;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (grid[j][i].getType() == PropertyType.RESIDENTIAL) {
                        Property prop = grid[j][i];
                        double propValue = prop.getPropertyValue();
                        if (prop.getHouseholds().size() < prop.getMaxHouseholds()) {
                            if (propValue <= res.getRentBudgetHigh() && propValue >= res.getRentBudgetLow()) {
                                double diff = res.getRentBudgetHigh() - propValue;
                                if (rentBudgetDiff > diff) {
                                    rentBudgetDiff = diff;
                                }
                                hI = i;
                                hJ = j;
                                propertyFound = true;
                            }
                        }
                    }
                }
            }
            if (propertyFound) {
                grid[hJ][hI].addHousehold(res);
            } else {
                movedOutOfGrid.add(res);
            }
        }
        return movedOutOfGrid;
    }
}
