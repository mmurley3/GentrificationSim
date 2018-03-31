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
                Household h = new Household(0, 100, 50);
                this.grid[i][j] = new Property(0, h);
            }
        }
    }
    
    public Property getProperty(int height, int width) {
    	return grid[height][width];
    }

}
