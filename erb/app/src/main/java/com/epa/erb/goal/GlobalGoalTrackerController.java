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
import com.epa.erb.engagement_action.EngagementActionController;
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
	HBox goalHBox;
	@FXML
	ProgressBar erbProgressBar;
	@FXML
	Label goalProgressLabel;
	
	private App app;
	private Project project;
	private EngagementActionController engagementActionController;
	public GlobalGoalTrackerController(App app, Project project, EngagementActionController engagementActionController) {
		this.app = app;
		this.project = project;
		this.engagementActionController = engagementActionController;
	}
	
	private Logger logger = LogManager.getLogger(GlobalGoalTrackerController.class);
	private Constants constants = new Constants();
	private String pathToERBProjectsFolder = constants.getPathToLocalERBProjectsFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		erbProgressBar.getStylesheets().add(getClass().getResource("/ProgressBar.css").toString());
		addGoalTrackers(project);
		handleERBProgress();
	}
	
	private void handleERBProgress() {
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
	
	@FXML
	public void doneButtonAction() {
		engagementActionController.closeGlobalGoalTrackerStage();
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

}
