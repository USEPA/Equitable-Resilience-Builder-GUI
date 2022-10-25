package com.epa.erb.progress;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GlobalProgressTrackerController implements Initializable{

	@FXML
	HBox headingHBox;
	@FXML
	HBox goalHBox;
	@FXML
	ProgressBar erbProgressBar;
	@FXML
	Label goalProgressLabel;
	
	private App app;
	private Project project;
	public GlobalProgressTrackerController(App app, Project project) {
		this.app = app;
		this.project = project;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(GlobalProgressTrackerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		addGoalTrackers(project);
		updateERBProgressBar();
	}
	
	private void handleControls() {
		headingHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		erbProgressBar.getStylesheets().add(getClass().getResource("/progressBar.css").toString());
	}
	
	private void updateERBProgressBar() {
		Progress progress = new Progress();
		int maxPercent = project.getProjectGoals().size() * 100;
		int totalPercent = progress.getProjectTotalPercent(project);
		double erbProgress = ((double) totalPercent/ maxPercent);
		double erbProgressPercent = erbProgress* 100;
		progress.setERBProgress(erbProgressBar, goalProgressLabel, (int) erbProgressPercent);
	}
	
	private void addGoalTrackers(Project project) {
		if (project != null) {
			for (Goal goal : project.getProjectGoals()) {
				Parent root = loadGoalTracker(goal);
				if (root != null) {
					goalHBox.getChildren().add(root);
					HBox.setHgrow(root, Priority.ALWAYS);
				}
			}
		} else {
			logger.error("Cannot addGoalTrackers. project is null.");
		}
	}
	
	private Parent loadGoalTracker(Goal goal) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/progress/LocalProgressTracker.fxml"));
			LocalProgressTrackerController goalTrackerController = new LocalProgressTrackerController(goal);
			fxmlLoader.setController(goalTrackerController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public HBox getHeadingHBox() {
		return headingHBox;
	}

	public HBox getGoalHBox() {
		return goalHBox;
	}

	public ProgressBar getErbProgressBar() {
		return erbProgressBar;
	}

	public Label getGoalProgressLabel() {
		return goalProgressLabel;
	}
	
}