import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import java.net.*;

import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.application.Application;

/**
 * Created by Daniel on 3/31/2018.
 */
public class GentSimulation extends Application{
	private static int totalNumSteps = 10;
    private static int numSteps = 0;
    private static int houseID = 0;
    private static PropertyGrid propertyGrid;
	private static double CHANCE_TO_ADD_NEW_HOUSEHOLD = 0.7;
    private static int MAX_NUM_HOUSEHOLDS_TO_ADD = 10;


	private  static ArrayList<Household> totalRelocatedHousehold = new ArrayList<>();
	private  static ArrayList<Household> totalMovedOutHousehold = new ArrayList<>();


    public static void main(String[] args) {
    	launch();

    	parseInput(args);

		propertyGrid.printPropertyValues();

		System.out.println();

		for (int i = 0; i < totalNumSteps; i++) {
			simStep();
		}

		propertyGrid.printPropertyValues();
		System.out.println();
		printHouseHoldRelocateStats();

    }
    @Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("interface.fxml"));
    
        Scene scene = new Scene(root, 800, 600);
    
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }
    
    //A method to check that arguments are formatted correctly
    //and then initialize the simulation based on them
    private static void parseInput(String[] args) {
    	FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(args[0]);
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

					Property newProp = new Property(Double.parseDouble(bits[0]), households, 5, PropertyType.valueOf(bits[1]));
					row[w] = newProp;
					w++;
				}
				grid[h] = row;
				h++;

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
		totalRelocatedHousehold.addAll(relocateHouseholds);

        // move new people into grid
		ArrayList<Household> newHouseholds = new ArrayList<>();
		if (Math.random() > CHANCE_TO_ADD_NEW_HOUSEHOLD) {
			int numNewHouseholds = (int)(Math.random() * MAX_NUM_HOUSEHOLDS_TO_ADD);
			newHouseholds = propertyGrid.generateNewHouseholds(houseID, numNewHouseholds);
		}
		relocateHouseholds.addAll(newHouseholds);

		// relocate residents
		ArrayList<Household> movedOutHouseholds = propertyGrid.relocateResidents(relocateHouseholds);
		totalMovedOutHousehold.addAll(movedOutHouseholds);

        numSteps++;
    }

    public static void printHouseHoldRelocateStats() {
    	Map<Household, Integer> relocatedStats = new HashMap<>();
    	for (int i = 0; i < totalRelocatedHousehold.size(); i++) {
    		Household h = totalRelocatedHousehold.get(i);
    		if (relocatedStats.containsKey(h)) {
    			relocatedStats.put(h, relocatedStats.get(h) + 1);
			} else {
    			relocatedStats.put(h, 1);
			}
		}

		for (Map.Entry<Household, Integer> entry : relocatedStats.entrySet()) {
    		if (totalMovedOutHousehold.contains(entry.getKey())) {
				System.out.println("ID: " + entry.getKey().getId() + ", Economic Status: " + entry.getKey().getRentBudgetHigh() + ", Relocated: " + entry.getValue() + ", Moved OUt of Grid");
			} else {
				System.out.println("ID: " + entry.getKey().getId() + ", Economic Status: " + entry.getKey().getRentBudgetHigh() + ", Relocated: " + entry.getValue());
			}
    	}
	}



}
