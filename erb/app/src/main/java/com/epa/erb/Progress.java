package com.epa.erb;

import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

public class Progress {
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(Progress.class);
	
	public Progress() {
		
	}
	
	public int getChapterPercentDone(Chapter chapter) {
		if (chapter != null) {
			double numberOfActivitiesInChapter = 0;
			double numberOfCompletedActivitiesInChapter = 0;
			for (Activity activity : chapter.getAssignedActivities()) {
				numberOfActivitiesInChapter++;
				if (activity.getStatus().contentEquals("complete")) {
					numberOfCompletedActivitiesInChapter++;
				}
			}
			return (int) ((numberOfCompletedActivitiesInChapter / numberOfActivitiesInChapter) * 100);
		} else {
			logger.error("Cannot getChapterPercentDone. chapter is null.");
			return -1;
		}
	}
	
	public int getGoalPercentDone(ArrayList<Chapter> listOfChaptersInGoal) {
		if (listOfChaptersInGoal != null) {
			double numberOfActivitiesInGoal = 0;
			double numberOfCompletedActivitiesInGoal = 0;
			for (Chapter chapter : listOfChaptersInGoal) {
				for (Activity activity : chapter.getAssignedActivities()) {
					numberOfActivitiesInGoal++;
					if (activity.getStatus().contentEquals("complete")) {
						numberOfCompletedActivitiesInGoal++;
					}
				}
			}
			return (int) ((numberOfCompletedActivitiesInGoal / numberOfActivitiesInGoal) * 100);
		} else {
			logger.error("Cannot getGoalPercentDone. listOfChaptersInGoal is null.");
			return -1;
		}
	}
	
	public int getChapterConfidencePercent(Chapter chapter) {
		if (chapter != null) {
			double max = (chapter.getNumberOfAssignedActivities()) * 100;
			double rating = 0;
			for (Activity activity : chapter.getAssignedActivities()) {
				int activityRating = Integer.parseInt(activity.getRating());
				if (activityRating >= 0) {
					rating = rating + activityRating;
				}
			}
			return (int) (((rating / max) * 100));
		} else {
			logger.error("Cannot getChapterConfidencePercent. chapter is null.");
			return -1;
		}
	}
	
	public int getGoalConfidencePercent(ArrayList<Chapter> listOfChaptersInGoal) {
		if (listOfChaptersInGoal != null) {
			double max = getMaxPercent(listOfChaptersInGoal);
			double rating = 0;
			for (Chapter chapter : listOfChaptersInGoal) {
				for (Activity activity : chapter.getAssignedActivities()) {
					int activityRating = Integer.parseInt(activity.getRating());
					if (activityRating >= 0) {
						rating = rating + activityRating;
					}
				}
			}
			return (int) (((rating / max) * 100));
		} else {
			logger.error("Cannot getGoalConfidencePercent. listOfChaptersInGoal is null.");
			return -1;
		}
	}
	
	private double getMaxPercent(ArrayList<Chapter> listOfChaptersInGoal) {
		if (listOfChaptersInGoal != null) {
			int numActivities = 0;
			for (Chapter chapter : listOfChaptersInGoal) {
				for (Activity activity : chapter.getAssignedActivities()) {
					numActivities++;
				}
			}
			return numActivities * 100.0;
		} else {
			logger.error("Cannot getMaxPercent. listOfChaptersInGoal is null");
			return -1;
		}
	}
	
	public double getProjectTotalPercent(Project project) {
		if (project != null) {
			double totalPercent = 0;
			for (Goal goal : project.getProjectGoals()) {
				File goalMetaXMLFile = getGoalMetaXMLFile(goal, project);
				if (goalMetaXMLFile != null) {
					ArrayList<Chapter> chapters = goal.getChapters();
					int goalCompletePercent = getGoalPercentDone(chapters);
					totalPercent = totalPercent + goalCompletePercent;
				}
			}
			return totalPercent;
		} else {
			logger.error("Cannot getProjectTotalPercent. project is null.");
			return -1;
		}
	}
	
	private File getGoalMetaXMLFile(Goal goal, Project project) {
		if (goal != null && project != null) {
			File goalXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName()+ "\\Goals\\" + goal.getGoalName() + "\\Meta.xml");
			if (goalXMLFile.exists()) {
				return goalXMLFile;
			}
			return null;
		} else {
			logger.error("Cannot getGoalMetaXMLFile. goal or project is null.");
			return null;
		}
	}
	
	public void setChapterProgress(VBox chapterProgressVBox, VBox chapterProgressBar, Label chapterPercentLabel, int chapterPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = chapterProgressVBox.getHeight();
				chapterProgressBar.setMaxHeight(progressBarHeight);
				chapterProgressBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = chapterPercent/fixedCapacity;
				chapterProgressBar.setPrefHeight(progressBarHeight*progress);
				chapterPercentLabel.setText(chapterPercent + "%");
			}
		});
	}
	
	public void setChapterProgress(ProgressIndicator chapterProgressIndicator, int chapterPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chapterProgressIndicator.setProgress(chapterPercent/100.0);
			}
		});
	}
	
	public void setChapterRating(VBox chapterConfidenceVBox, VBox chapterConfidenceBar, Label chaperConfidencePercentLabel, int chapterConfidence) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = chapterConfidenceVBox.getHeight();
				chapterConfidenceBar.setMaxHeight(progressBarHeight);
				chapterConfidenceBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = chapterConfidence / fixedCapacity;
				chapterConfidenceBar.setPrefHeight(progressBarHeight * progress);
				chaperConfidencePercentLabel.setText(chapterConfidence + "%");
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
	
	public void setGoalProgress(ProgressBar goalProgressBar, Label goalProgressPercentLabel, int goalPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				goalProgressBar.setProgress(goalPercent / 100.0);
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
	
	public void setERBProgress(ProgressBar erbProgressBar, Label erbProgressPercentLabel, double erbPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				erbProgressBar.setProgress(erbPercent/100.0);
				erbProgressPercentLabel.setText(erbPercent + "%");
			}
		});
	}

}
