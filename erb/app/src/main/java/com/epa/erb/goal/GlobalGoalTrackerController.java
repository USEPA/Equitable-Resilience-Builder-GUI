package com.epa.erb.goal;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.Progress;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.project.Project;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GlobalGoalTrackerController implements Initializable{

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
	public GlobalGoalTrackerController(App app, Project project) {
		this.app = app;
		this.project = project;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(GlobalGoalTrackerController.class);
	private String pathToERBProjectsFolder = constants.getPathToLocalERBProjectsFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		addGoalTrackers(project);
		updateERBProgressBar();
	}
	
	private void handleControls() {
		headingHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		erbProgressBar.getStylesheets().add(getClass().getResource("/ProgressBar.css").toString());
	}
	
	private void updateERBProgressBar() {
		Progress progress = new Progress();
		double maxPercent = project.getProjectGoals().size() * 100;
		double totalPercent = 0;
		for (Goal goal : project.getProjectGoals()) {
			File goalMetaXMLFile = getGoalMetaXMLFile(goal, project);
			if (goalMetaXMLFile != null) {
				ArrayList<Chapter> chapters = goal.getChapters();
				int goalCompletePercent = progress.getGoalPercentDone(chapters);
				totalPercent = totalPercent + goalCompletePercent;
			}
		}
		double erbProgressPercent = (totalPercent/maxPercent)* 100;
		setERBProgress((int) erbProgressPercent);
	}
	
	private void setERBProgress(int percent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				erbProgressBar.setProgress(percent/100.0);
				goalProgressLabel.setText(percent + "%");
			}
		});
	}
	
	private void addGoalTrackers(Project project) {
		for (Goal goal : project.getProjectGoals()) {
			File goalMetaXMLFile = getGoalMetaXMLFile(goal, project);
			if (goalMetaXMLFile != null) {
				Parent root = loadGoalTracker(app, goal, goalMetaXMLFile);
				if (root != null) {
					goalHBox.getChildren().add(root);
				}
			}
		}
	}
	
	private Parent loadGoalTracker(App app, Goal goal, File goalMetaXMLFile) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalTracker.fxml"));
			GoalTrackerController goalTrackerController = new GoalTrackerController(goal);
			fxmlLoader.setController(goalTrackerController);
			Parent rootParent = fxmlLoader.load();
			HBox.setHgrow(rootParent, Priority.ALWAYS);
			return rootParent;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private File getGoalMetaXMLFile(Goal goal, Project project) {
		File goalXMLFile = new File(pathToERBProjectsFolder + "\\" + project.getProjectName() + "\\Goals\\" + goal.getGoalName() + "\\Meta.xml");
		if(goalXMLFile.exists()) {
			return goalXMLFile;
		} else {
			logger.debug("Goal Meta XML file returned is null.");
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
