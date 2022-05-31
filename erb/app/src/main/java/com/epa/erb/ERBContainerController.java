package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class ERBContainerController implements Initializable{
	
	@FXML
	VBox welcomeVBox;
	@FXML
	VBox erbContainer;
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	@FXML
	public void aboutMenuItemAction() {
		
	}
	
	@FXML
	public void glossaryMenuItemAction() {
		
	}

	public VBox getWelcomeVBox() {
		return welcomeVBox;
	}

	public VBox getErbContainer() {
		return erbContainer;
	}

	public App getApp() {
		return app;
	}

}
