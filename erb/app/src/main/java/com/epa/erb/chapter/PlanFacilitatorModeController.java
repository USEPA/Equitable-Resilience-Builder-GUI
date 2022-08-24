package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.utility.Constants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlanFacilitatorModeController implements Initializable{

	@FXML
	HBox erbHeading;
	@FXML
	VBox mainVBox;
	
	private Chapter chapter;
	public PlanFacilitatorModeController(Chapter chapter) {
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
	}
	
	private Parent loadPlanTitledPane(Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/PlanTitledPane.fxml"));
			PlanTitledPaneController planTitledPaneController = new PlanTitledPaneController(activity, chapter);
			fxmlLoader.setController(planTitledPaneController);
			Parent root= fxmlLoader.load();
			planTitledPaneController.expand();
			return root;
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

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

}
