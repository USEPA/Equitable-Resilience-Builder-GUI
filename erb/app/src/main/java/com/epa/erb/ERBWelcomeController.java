package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class ERBWelcomeController implements Initializable{

	@FXML
	VBox welcomeVBox;
	@FXML
	VBox circleDiagramVBox;
	
	private App app;
	public ERBWelcomeController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadCircleDiagram();
		
	}
	
	private void loadCircleDiagram() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/CircleDiagram.fxml"));
			CircleDiagramController circleDiagramController = new CircleDiagramController();
			fxmlLoader.setController(circleDiagramController);
			Parent circleDiagramRoot = fxmlLoader.load();
			circleDiagramVBox.getChildren().add(circleDiagramRoot);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@FXML
	public void exploreButtonAction() {
		Parent erbDashboardRoot = loadERBDashboard();
		app.loadNodeToERBContainer(erbDashboardRoot);
	}
	
	private Parent loadERBDashboard() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBDashboard.fxml"));
			ERBDashboardController erbDashboardController = new ERBDashboardController(app);
			fxmlLoader.setController(erbDashboardController);
			return fxmlLoader.load();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
