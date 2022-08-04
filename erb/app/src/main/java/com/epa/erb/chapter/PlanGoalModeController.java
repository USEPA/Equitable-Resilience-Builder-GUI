package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.Activity;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlanGoalModeController implements Initializable{

	@FXML
	HBox erbHeading;
	@FXML
	VBox mainVBox;
	
	private Chapter chapter;
	public PlanGoalModeController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		addTitledPanes();
	}
	
	private void addTitledPanes() {
		Parent chapterPlanTitledPaneRoot = loadPlanTitledPane(null);
		mainVBox.getChildren().add(chapterPlanTitledPaneRoot);
		for(Activity activity: chapter.getAssignedActivities()) {
			Parent planTitledPaneRoot = loadPlanTitledPane(activity);
			mainVBox.getChildren().add(planTitledPaneRoot);
		}
	}
	
	private Parent loadPlanTitledPane(Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/PlanTitledPane.fxml"));
			PlanTitledPaneController planTitledPaneController = new PlanTitledPaneController(activity, chapter);
			fxmlLoader.setController(planTitledPaneController);
			return fxmlLoader.load();
		} catch (Exception e) {
			return null;
		}
	}
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}

	public HBox getErbHeading() {
		return erbHeading;
	}

}
