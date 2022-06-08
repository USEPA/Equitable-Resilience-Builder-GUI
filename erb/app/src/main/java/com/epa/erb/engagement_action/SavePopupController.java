package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class SavePopupController implements Initializable {

	@FXML
	Label statusLabel;
	@FXML
	ProgressIndicator progressIndicator;

	ArrayList<Project> projects;
	private EngagementActionController engagementActionController;
	public SavePopupController(ArrayList<Project> projects,EngagementActionController engagementActionController) {
		this.projects = projects;
		this.engagementActionController = engagementActionController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		runTask();
	}

	private void runTask() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (Project project : projects) {
					saveData(project);
					engagementActionController.setAllToSaved(project);
				}
				Thread.sleep(2000);
				setGoalSaveDone();
				Thread.sleep(1500);
				return null;
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				engagementActionController.closeSavePopupStage();
			}
		});

		progressIndicator.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
	}

	private void saveData(Project project) {
		if (!project.isSaved()) {
			for (Goal goal : project.getProjectGoals()) {
				if (!goal.isSaved()) {
					engagementActionController.saveGoalData(goal);
					for (Chapter chapter : goal.getChapters()) {
						if (!chapter.isSaved()) {
							for (Activity activity : chapter.getAssignedActivities()) {
								if (!activity.isSaved()) {
									engagementActionController.saveActivityData(project, goal, activity);
								}
							}
						}
					}
				}
			}
		}
	}

	public void setGoalSaveDone() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setStatusLabelText("Done saving.");
				progressIndicator.setVisible(false);
			}
		});
	}

	private void setStatusLabelText(String text) {
		statusLabel.setText(text);
	}

}
