package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.engagement_setup.EngagementSetupController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ERBMainController implements Initializable{

	@FXML
	HBox erbHeading1;
	@FXML
	HBox erbHeading2;
	@FXML
	Button setupLaunchButton;
	@FXML
	Button actionLaunchButton;
	
	public ERBMainController() {
		
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBMainController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	public void handleControls() {
		erbHeading1.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		erbHeading2.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	//ERB Tool Pt 1
	Stage setupStage = null;
	@FXML
	public void setupLaunchButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_setup/EngagementSetup.fxml"));
			EngagementSetupController engagementSetupController = new EngagementSetupController(this);
			fxmlLoader.setController(engagementSetupController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			setupStage = new Stage();
			setupStage.setScene(scene);
			setupStage.setTitle("ERB: Equitable Resilience Builder");
			setupStage.show();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}
	
	//ERB Tool Pt 2
	Stage actionStage = null;
	@FXML
	public void actionLaunchButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			EngagementActionController engagementActionController = new EngagementActionController();
			fxmlLoader.setController(engagementActionController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			actionStage = new Stage();
			actionStage.setScene(scene);
			actionStage.setTitle("ERB: Equitable Resilience Builder");
			actionStage.show();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}
	
	public void closeSetupStage() {
		if(setupStage != null) {
			setupStage.close();
		}
	}
	
	public void closeActionStage() {
		if(actionStage != null) {
			actionStage.close();
		}
	}
}
