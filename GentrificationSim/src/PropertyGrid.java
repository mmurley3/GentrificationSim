/**
 * Created by Daniel on 3/31/2018.
 */

public class PropertyGrid {
    private int height;
    private int width;
    private Tile[][] grid;

    public PropertyGrid(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Tile[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Property h = new Property(0, 100, 50);
                this.grid[i][j] = new Tile(0, h);
            }
        }
    }

}
