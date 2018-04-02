/**
 * Created by Daniel on 3/31/2018.
 */

public class PropertyGrid {
    private int height;
    private int width;
    private Property[][] grid;

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

    public double evaluatePropertyValue(int i , int j) {
        double parkValue = getPropertyTypeValue(i, j, PropertyType.PARK);
        double commercialValue = getPropertyTypeValue(i, j, PropertyType.COMMERCIAL);
        double industrialValue = getPropertyTypeValue(i, j, PropertyType.INDUSTRIAL);
        double adjPropVals = getAdjPropValues(i, j);

        return (parkValue + commercialValue + industrialValue + adjPropVals) / 4;
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

        return propValSum / numProps;
    }

    public double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    double getAdjPropValues(int i, int j) {
        double up = grid[i][j+1].getPropertyValue();
        double down = grid[i][j-1].getPropertyValue();
        double left = grid[i-1][j].getPropertyValue();
        double right = grid[i+1][j+1].getPropertyValue();
        return (up + down + left + right) / 4;
    }
}
