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
	public GoalContainerController(App app, Project project, ProjectSelectionController projectSelectionController ) {
		this.app = app;
		this.project = project;
		this.projectSelectionController = projectSelectionController;
	}
	
	private Logger logger = LogManager.getLogger(GoalContainerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Parent goalCreationRoot = loadGoalCreation();
		VBox.setVgrow(goalCreationRoot, Priority.ALWAYS);
		goalContainerVBox.getChildren().add(goalCreationRoot);
	}
	
	private Parent loadGoalCreation() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalCreation.fxml"));
			GoalCreationController goalCreationController = new GoalCreationController(app, project, projectSelectionController);
			fxmlLoader.setController(goalCreationController);
			return fxmlLoader.load();
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public VBox getGoalContainerVBox() {
		return goalContainerVBox;
	}

	public App getApp() {
		return app;
	}

	public Project getProject() {
		return project;
	}

	public ProjectSelectionController getProjectSelectionController() {
		return projectSelectionController;
	}

	public void setProjectSelectionController(ProjectSelectionController projectSelectionController) {
		this.projectSelectionController = projectSelectionController;
	}
		
}
