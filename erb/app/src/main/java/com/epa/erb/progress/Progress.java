package com.epa.erb.progress;

import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

public class Progress {
	
	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(Progress.class);
	
	public Progress() {
		
	}
	
	public int getChapterPercentDone(Chapter chapter) {
//		if (chapter != null) {
//			double numberOfActivitiesInChapter = 0;
//			double numberOfCompletedActivitiesInChapter = 0;
//			for (Activity activity : chapter.getAssignedActivities()) {
//				numberOfActivitiesInChapter++;
//				if (activity.getStatus().contentEquals("complete")) {
//					numberOfCompletedActivitiesInChapter++;
//				}
//			}
//			return (int) ((numberOfCompletedActivitiesInChapter / numberOfActivitiesInChapter) * 100);
//		} else {
//			logger.error("Cannot getChapterPercentDone. chapter is null.");
			return -1;
//		}
	}
	
	public int getGoalPercentDone(ArrayList<Chapter> listOfChaptersInGoal) {
//		if (listOfChaptersInGoal != null) {
//			double numberOfActivitiesInGoal = 0;
//			double numberOfCompletedActivitiesInGoal = 0;
//			for (Chapter chapter : listOfChaptersInGoal) {
//				for (Activity activity : chapter.getAssignedActivities()) {
//					numberOfActivitiesInGoal++;
//					if (activity.getStatus().contentEquals("complete")) {
//						numberOfCompletedActivitiesInGoal++;
//					}
//				}
//			}
//			return (int) ((numberOfCompletedActivitiesInGoal / numberOfActivitiesInGoal) * 100);
//		} else {
//			logger.error("Cannot getGoalPercentDone. listOfChaptersInGoal is null.");
			return -1;
//		}
	}
	
	public int getProjectTotalPercent(Project project) {
		if (project != null) {
			double totalPercent = 0;
			for (Goal goal : project.getProjectGoals()) {
				File goalMetaXMLFile = fileHandler.getGoalMetaXMLFile(project, goal);
				if (goalMetaXMLFile != null && goalMetaXMLFile.exists()) {
					ArrayList<Chapter> chapters = goal.getChapters();
					int goalCompletePercent = getGoalPercentDone(chapters);
					totalPercent = totalPercent + goalCompletePercent;
				}
			}
			return (int) totalPercent;
		} else {
			logger.error("Cannot getProjectTotalPercent. project is null.");
			return -1;
		}
	}
	
	public void setChapterProgress(ProgressIndicator chapterProgressIndicator, int chapterPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chapterProgressIndicator.setProgress(chapterPercent/100.0);
			}
		});
	}
	
	public void setGoalProgress(VBox goalProgressVBox, VBox goalProgressBar, Label goalProgressPercentLabel, int goalPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalProgressVBox.getHeight();
				goalProgressBar.setMaxHeight(progressBarHeight);
				goalProgressBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalPercent / fixedCapacity;
				goalProgressBar.setPrefHeight(progressBarHeight * progress);
				goalProgressPercentLabel.setText(goalPercent + "%");
			}
		});
	}
	
	public void setGoalRating(VBox goalConfidenceVBox, VBox goalConfidenceBar, Label goalConfidencePercentLabel, int goalConfidence) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalConfidenceVBox.getHeight();
				goalConfidenceBar.setMaxHeight(progressBarHeight);
				goalConfidenceBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalConfidence / fixedCapacity;
				goalConfidenceBar.setPrefHeight(progressBarHeight * progress);
				goalConfidencePercentLabel.setText(goalConfidence + "%");
			}
		});
	}
	
	public void setERBProgress(ProgressBar erbProgressBar, Label erbProgressPercentLabel, int erbPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				erbProgressBar.setProgress(erbPercent/100.0);
				erbProgressPercentLabel.setText(erbPercent + "%");
			}
		});
	}

}
