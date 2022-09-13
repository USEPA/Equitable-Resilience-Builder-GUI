package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.intro_panels.EquityAndResilienceController;
import com.epa.erb.intro_panels.HowDoesItWorkController;
import com.epa.erb.intro_panels.HowERBMadeController;
import com.epa.erb.intro_panels.WhatMakesERBDifferentController;
import com.epa.erb.intro_panels.WhoAreERBUsersController;
import com.epa.erb.project.ProjectSelectionController;
import com.epa.erb.utility.Constants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ERBLandingNew2Controller implements Initializable{

	@FXML
	HBox welcomeHBox;
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
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadArrowDiagram();
	}
	
	private void handleControls() {
		app.getErbContainerController().removeHeaderPanel();
		welcomeHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");		
		modeToggleGroup.selectedToggleProperty().addListener((changed, oldVal, newVal) -> modeChanged(oldVal, newVal));
	}
	
	private void modeChanged(Toggle oldToggle, Toggle newToggle) {
		RadioButton newRadioButton = (RadioButton) newToggle;
		mode = newRadioButton.getText();
	}
	
	@FXML
	public void equitableResilienceHyperlinkClicked() {
		ERBContainerController erbContainerController = new ERBContainerController(app);
		erbContainerController.glossaryMenuItemAction();
	}
	
	@FXML
	public void equityAndResilienceHyperlinkClicked() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/EquityAndResilience.fxml"));
			EquityAndResilienceController equityAndResilienceController = new EquityAndResilienceController(app);
			fxmlLoader.setController(equityAndResilienceController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void howERBMadeHyperlinkClicked() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/HowERBMade.fxml"));
			HowERBMadeController howERBMadeController = new HowERBMadeController(app, this);
			fxmlLoader.setController(howERBMadeController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void howDoesItWorkButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/HowDoesItWork.fxml"));
			HowDoesItWorkController howDoesItWorkController = new HowDoesItWorkController(app);
			fxmlLoader.setController(howDoesItWorkController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void howOthersUsingERBHyperlinkClicked() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/HowOthersAreUsingERB.fxml"));
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void exploreButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/ExploreERB.fxml"));
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void whoAreERBUsersButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/WhoAreERBUsers.fxml"));
			WhoAreERBUsersController whoAreERBUsersController = new WhoAreERBUsersController(app);
			fxmlLoader.setController(whoAreERBUsersController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void whatMakesERBDifferentButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/WhatMakesERBDifferent.fxml"));
			WhatMakesERBDifferentController whatMakesERBDifferentController = new WhatMakesERBDifferentController(app);
			fxmlLoader.setController(whatMakesERBDifferentController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
