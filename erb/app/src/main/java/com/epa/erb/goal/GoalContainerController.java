package com.epa.erb.goal;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GoalContainerController implements Initializable{

	@FXML
	VBox goalContainerVBox;
	
	private App app;
	private Project project;
	private ProjectSelectionController projectSelectionController;
	public GoalContainerController(App app, Project project, ProjectSelectionController projectSelectionController) {
		this.app = app;
		this.project = project;
		this.projectSelectionController = projectSelectionController;
	}
	
	private Logger logger = LogManager.getLogger(GoalContainerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadGoalIntro();
	}
	
	private void loadGoalIntro() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalIntro.fxml"));
			GoalIntroController goalIntroController = new GoalIntroController(app, project, this, projectSelectionController);
			fxmlLoader.setController(goalIntroController);
			Parent root = fxmlLoader.load();
			VBox.setVgrow(root, Priority.ALWAYS);
			goalContainerVBox.getChildren().add(root);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public VBox getGoalContainerVBox() {
		return goalContainerVBox;
	}
	
}