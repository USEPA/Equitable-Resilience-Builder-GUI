package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class EngagementGoalLandingContentController implements Initializable {

	@FXML
	VBox vBox;
	
	
	private EngagementActionController engagementActionController;
	public EngagementGoalLandingContentController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}

	private void handleControls() {

	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

}
