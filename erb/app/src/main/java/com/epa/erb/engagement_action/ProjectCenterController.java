package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class ProjectCenterController implements Initializable {

	private EngagementActionController engagementActionController;
	public ProjectCenterController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}
	
	@FXML
	VBox vBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

}
