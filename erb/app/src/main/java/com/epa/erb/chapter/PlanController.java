package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PlanController implements Initializable{

	@FXML
	HBox erbHeading;
	
	private Chapter chapter;
	public PlanController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(PlanController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		addActivityMaterials();
	}
	
	private void addActivityMaterials() {
		for (Activity activity : chapter.getAssignedActivities()) {
			if (activity.getMaterials() != null && activity.getMaterials().length() > 0) {
				
			}
		}
	}
	
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

}
