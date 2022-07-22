package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

public class PlanGoalModeController implements Initializable{

	@FXML
	HBox erbHeading;
	
	public PlanGoalModeController() {
		
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}

	public HBox getErbHeading() {
		return erbHeading;
	}

}
