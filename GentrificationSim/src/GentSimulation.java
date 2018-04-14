import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;

import java.net.*;

import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
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
    }

    @Override
    public void start(Stage stage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("interface.fxml"));
    
        Scene scene = new Scene(root, 800, 600);
    
        stage.setTitle("GentSim");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void step(int x, Scene scene) {
    	for (int i = 0; i < x; i++) {
    		simStep();
    		numSteps++;
    	}
		generateGridUI(scene, scene.getHeight() * 0.6, scene.getWidth() * 0.6);
    }

    public static void generateGridUI(Scene scene, double gUIHeight, double gUIWidth) {
    	int gridWidth = propertyGrid.getWidth();
    	int gridHeight = propertyGrid.getHeight();

		GridPane grid = (GridPane) scene.lookup("#property_grid");
		grid.getChildren().clear();
		grid.setPadding(new Insets(10, 10, 10, 10));
		double gridUIWidth = gUIWidth;
		double gridUIHeight = gUIHeight;

		grid.setVgap(-1.0);
		grid.setHgap(0.0);

		for(int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {

				Rectangle rec = new Rectangle();

				Property prop = propertyGrid.getProperty(i, j);
				double v = Math.abs(prop.getPropertyValue());
				double vr = (double) Math.round(v*100)/100;
				Text value = new Text(Double.toString(vr));
				value.setFill(Color.WHITE);
				GridPane.setHalignment(value, HPos.CENTER);

				double mappingValue = 8;
				v = v / mappingValue;
				v = Math.min(v, 1.0);

				if (prop.getType() == PropertyType.RESIDENTIAL) {

					rec.setFill(new Color(0.0, 0.0, v , 1.0));
				} else if (prop.getType() == PropertyType.COMMERCIAL) {

					rec.setFill(new Color(v, 0.0, v, 1.0));
				} else if (prop.getType() == PropertyType.PARK) {

					rec.setFill(new Color(0.0, v, 0.0, 1.0));
				} else if (prop.getType() == PropertyType.INDUSTRIAL) {

					rec.setFill(new Color(v, 0.0, 0.0, 1.0));
				}

				rec.setStrokeWidth(2.0);
				rec.setStroke(Color.BLACK);
				rec.setWidth(gridUIWidth / gridWidth);
				rec.setHeight(gridUIHeight / gridHeight);

				grid.add(rec, j, i);
				grid.add(value, j, i);

			}
		}
	}
    
    //A method to check that arguments are formatted correctly
    //and then initialize the simulation based on them
    public static void parseInput(String file) {
    	FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(file);
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
