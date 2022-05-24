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
import javafx.stage.Stage;

public class ERBLandingController implements Initializable{
	
	private App app;
	public ERBLandingController(App app) {
		this.app = app;
	}
	
	private Logger logger = LogManager.getLogger(ERBLandingController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
			projectSelectionStage.setTitle("Project Selection");
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
