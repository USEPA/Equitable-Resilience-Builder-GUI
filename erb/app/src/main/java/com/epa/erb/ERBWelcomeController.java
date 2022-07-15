package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class ERBWelcomeController implements Initializable{

	@FXML
	VBox welcomeVBox;
	@FXML
	VBox circleDiagramVBox;
	@FXML
	SVGPath svgPath;
	
	private App app;
	public ERBWelcomeController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadCircleDiagram();
		svgPath.setContent("M 150 350 Q 150 350 450 150 Q 450 150 400 100 Q 400 100 550 100 C 550 100 550 250 550 250 C 550 250 500 200 500 200 C 500 200 350 250 150 350 ");
	}
	
	private void loadCircleDiagram() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/CircleDiagram.fxml"));
			CircleDiagramController circleDiagramController = new CircleDiagramController();
			fxmlLoader.setController(circleDiagramController);
			Parent circleDiagramRoot = fxmlLoader.load();
			circleDiagramVBox.getChildren().add(circleDiagramRoot);
		} catch (Exception e) {
			
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
