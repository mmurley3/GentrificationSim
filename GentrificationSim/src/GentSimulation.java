import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Daniel on 3/31/2018.
 */
public class GentSimulation {
    private int numSteps = 0;
    private PropertyGrid propertyGrid;
    
    public static void main(String[] args) {
    	parseInput(args);
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
        String line;
        try {
			while ((line = br.readLine()) != null)   {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    // one iteration of the gentrification simulation
    private static void simStep() {
        // reevaluate property values
        // check to see if people need to move out / relocate
        // move new people into grid
    }


}
