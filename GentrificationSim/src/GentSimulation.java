/**
 * Created by Daniel on 3/31/2018.
 */
public class GentSimulation {
    private int numSteps = 0;
    private PropertyGrid propertyGrid;
    
    public static void main(String[] args) {
    	parseArgs(args);
    	boolean condition = true;
    	while (condition) {
    		simStep();
    	}
    }
    
    //A method to check that arguments are formatted correctly
    //and then initialize the simulation based on them
    private static void parseArgs(String[] args) {
    	
    }

    // one iteration of the gentrification simulation
    private static void simStep() {
        // reevaluate property values
        // check to see if people need to move out / relocate
        // move new people into grid
    }


}
