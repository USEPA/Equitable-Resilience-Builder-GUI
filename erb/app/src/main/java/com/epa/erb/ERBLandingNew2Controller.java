package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.project.ProjectSelectionController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ERBLandingNew2Controller implements Initializable{

	@FXML
	VBox arrowVBox;
	@FXML
	ToggleGroup modeToggleGroup;
	@FXML
	RadioButton facilitatorModeRadioButton;
	@FXML
	RadioButton goalModeRadioButton;
	
	private App app;
	public ERBLandingNew2Controller(App app) {
		this.app = app;
	}
	
	private String mode = "Goal Mode";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadArrowDiagram();
	}
	
	private void handleControls() {
		modeToggleGroup.selectedToggleProperty().addListener((changed, oldVal, newVal) -> modeChanged(oldVal, newVal));
	}
	
	private void modeChanged(Toggle oldToggle, Toggle newToggle) {
		RadioButton newRadioButton = (RadioButton) newToggle;
		mode = newRadioButton.getText();
	}
	
	private void loadArrowDiagram() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ArrowDiagram.fxml"));
			ArrowDiagramController arrowDiagramController = new ArrowDiagramController();
			fxmlLoader.setController(arrowDiagramController);
			Parent arrowDiagramRoot = fxmlLoader.load();
			arrowVBox.getChildren().add(arrowDiagramRoot);
		} catch (Exception e) {
			
		}
	}
	
	@FXML
	public void projectButtonAction() {
		Parent projectSelectionRoot = loadProjectSelectionToContainer();
		app.loadNodeToERBContainer(projectSelectionRoot);			
	}
	
	private Parent loadProjectSelectionToContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectSelection.fxml"));
			ProjectSelectionController projectSelectionController = new ProjectSelectionController(app, false, mode);
			fxmlLoader.setController(projectSelectionController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			return null;
		}
	}
	
	@FXML
	public void continueButtonAction() {
		app.launchERBLanding(false);
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
