package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.project.ProjectSelectionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ERBLandingController implements Initializable{
	
	@FXML
	VBox welcomeVBox;
	@FXML
	Circle circle1;
	@FXML
	Circle circle2;
	@FXML
	Circle circle3;
	
	private App app;
	public ERBLandingController(App app) {
		this.app = app;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBLandingController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		circle1.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		circle2.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		circle3.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
	}
	
	private Stage projectSelectionStage = null;
	@FXML
	public void beginERBProcessButtonClicked() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectSelection.fxml"));
			ProjectSelectionController projectSelectionController = new ProjectSelectionController(app, this);
			fxmlLoader.setController(projectSelectionController);
			projectSelectionStage = new Stage();
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			projectSelectionStage.setScene(scene);
			projectSelectionStage.setTitle("Equitable Resilience Builder: Project Selection");
			projectSelectionStage.show();
			app.closeERBLandingStage();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void closeProjectSelectionStage() {
	if(projectSelectionStage != null) {
		projectSelectionStage.close();
	}
}

}
