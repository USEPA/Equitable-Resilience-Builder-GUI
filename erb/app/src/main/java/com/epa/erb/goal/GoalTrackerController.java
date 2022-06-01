package com.epa.erb.goal;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Constants;
import com.epa.erb.Progress;
import com.epa.erb.chapter.Chapter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GoalTrackerController implements Initializable{

	@FXML
	Label goalTitleLabel;
	@FXML
	VBox goalCompletionVBox;
	@FXML
	VBox goalCompletionBar;
	@FXML
	Label completionPercentLabel;
	@FXML
	VBox goalConfidenceVBox;
	@FXML
	VBox goalConfidenceBar;
	@FXML
	Label confidencePercentLabel;
	@FXML
	VBox goalVBox;
	
	private Goal goal;
	public GoalTrackerController(Goal goal) {
		this.goal = goal;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(GoalTrackerController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setGoalTitleLabelText(goal.getGoalName());
		handleGoalProgress();
		addProgressListener();
	}
	
	private void setGoalTitleLabelText(String text) {
		goalTitleLabel.setText(text);
	}
	
	private void addProgressListener() {
		goalVBox.heightProperty().addListener(e -> handleProgressListener());
	}
	
	private void handleProgressListener() {
		handleGoalProgress();
	}
	
	private void handleGoalProgress() {
		Progress progress = new Progress();
		ArrayList<Chapter> listOfChapters = goal.getChapters();
		int goalPercentDone = progress.getGoalPercentDone(listOfChapters);
		setGoalCompletion(goalPercentDone);
		int goalConfidencePercent = progress.getGoalConfidencePercent(listOfChapters);
		setGoalConfidence(goalConfidencePercent);
	}
	
	private void setGoalCompletion(int goalPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalCompletionVBox.getHeight();
				goalCompletionBar.setMaxHeight(progressBarHeight);
				goalCompletionBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalPercent / fixedCapacity;
				goalCompletionBar.setPrefHeight(progressBarHeight * progress);
				completionPercentLabel.setText(goalPercent + "%");
			}
		});
	}
	
	private void setGoalConfidence(int goalConfidence) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalConfidenceVBox.getHeight();
				goalConfidenceBar.setMaxHeight(progressBarHeight);
				goalConfidenceBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalConfidence / fixedCapacity;
				goalConfidenceBar.setPrefHeight(progressBarHeight * progress);
				confidencePercentLabel.setText(goalConfidence + "%");
			}
		});
	}
	
}
