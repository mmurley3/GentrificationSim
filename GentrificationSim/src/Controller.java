import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class Controller {
	
	@FXML
    private Button importButton;
	
	@FXML
	private void handleButtonAction(ActionEvent event) {
	    // Button was clicked, do something...
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Open Resource File");
	    File file = fileChooser.showOpenDialog(importButton.getScene().getWindow());
        if (file != null) {
        	String name = file.toString();
        	System.out.println(name);
            GentSimulation.parseInput(name);
        }
	}

}
