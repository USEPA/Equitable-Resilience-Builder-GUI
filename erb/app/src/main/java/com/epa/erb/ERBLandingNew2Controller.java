package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class ERBLandingNew2Controller implements Initializable{

	@FXML
	VBox arrowVBox;
	
	private App app;
	public ERBLandingNew2Controller(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadArrowDiagram();
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
	public void continueButtonAction() {
		app.launchERBLanding(false);
	}

}
