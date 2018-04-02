import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Daniel on 3/31/2018.
 */
public class GentSimulation {
    private static int numSteps = 0;
    private static int houseID = 0;
    private static PropertyGrid propertyGrid;
    
    public static void main(String[] args) {
    	parseInput(args);
		System.out.println(propertyGrid.getProperty(0, 0).getHouseholds().size());
    	simStep();



		//boolean condition = true;
    	//while (condition) {
    	//	simStep();
    	//}
    }
    
    //A method to check that arguments are formatted correctly
    //and then initialize the simulation based on them
    private static void parseInput(String[] args) {
    	FileInputStream fstream = null;
		try {
			fstream = new FileInputStream("data.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        int height;
        int width;
        
        try {
			line = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        height = Integer.parseInt(line.split(",")[0]);
        width = Integer.parseInt(line.split(",")[1]);
        Property[][] grid = new Property[height][];
        int h = 0;
        try {
			while ((line = br.readLine()) != null)   {
				int w = 0;
				Property[] row = new Property[width];
				for (String entry: line.split(";")) {
					
					String[] bits = entry.split(",");
					ArrayList<Household> households = new ArrayList<>();
					int houseNum = Integer.parseInt(bits[2]);
					if (houseNum > 0) {
						for (int i = 3; i < 3 + houseNum * 2; i += 2) {
							households.add(new Household(houseID, Double.parseDouble(bits[i]), Double.parseDouble(bits[i + 1])));
							//households[i/2 - 1] = new Household(houseID, Double.parseDouble(bits[i]), Double.parseDouble(bits[i + 1]) );
							houseID++;
						}
					}
					
					//Note: these properties are initialized with a null household field

					Property newProp = new Property(Double.parseDouble(bits[0]), households, PropertyType.valueOf(bits[1]));
					row[w] = newProp;
					w++;
				}
				grid[h] = row;
				h++;
				System.out.println(line);
			}
			propertyGrid = new PropertyGrid(grid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    // one iteration of the gentrification simulation
    private static void simStep() {
        // reevaluate property values
        propertyGrid.evaluateAllPropertyValues();

        // check to see if people need to move out / relocate
		ArrayList<Household> relocateHouseholds = propertyGrid.evaluateResidences();

        // move new people into grid
		ArrayList<Household> newHouseholds = new ArrayList<Household>();
		if (Math.random() > 0.7) {
			int numNewHouseholds = (int)(Math.random() * 10);
			newHouseholds = propertyGrid.generateNewHouseholds(houseID, numNewHouseholds);
		}
		relocateHouseholds.addAll(newHouseholds);

		// relocate residents
		propertyGrid.relocateResidents(relocateHouseholds);


        numSteps++;
    }



}
