package com.epa.erb.goal;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.Progress;
import com.epa.erb.chapter.Chapter;
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setGoalTitleLabelText(goal.getGoalName());
		updateGoalProgress();
		addProgressListener();
	}

	private void addProgressListener() {
		goalVBox.heightProperty().addListener(e -> handleProgressBarsAfterPaneSizeChange());
	}
	
	private void handleProgressBarsAfterPaneSizeChange() {
		updateGoalProgress();
	}
	
	private void updateGoalProgress() {
		Progress progress = new Progress();
		ArrayList<Chapter> listOfChapters = goal.getChapters();
		int goalPercentDone = progress.getGoalPercentDone(listOfChapters);
		progress.setGoalRating(goalCompletionVBox, goalCompletionBar, completionPercentLabel, goalPercentDone);
		int goalConfidencePercent = progress.getGoalConfidencePercent(listOfChapters);
		progress.setGoalRating(goalConfidenceVBox, goalConfidenceBar, confidencePercentLabel, goalConfidencePercent);
	}
	
	private void setGoalTitleLabelText(String text) {
		goalTitleLabel.setText(text);
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Label getGoalTitleLabel() {
		return goalTitleLabel;
	}

	public VBox getGoalCompletionVBox() {
		return goalCompletionVBox;
	}

	public VBox getGoalCompletionBar() {
		return goalCompletionBar;
	}

	public Label getCompletionPercentLabel() {
		return completionPercentLabel;
	}

	public VBox getGoalConfidenceVBox() {
		return goalConfidenceVBox;
	}

	public VBox getGoalConfidenceBar() {
		return goalConfidenceBar;
	}

	public Label getConfidencePercentLabel() {
		return confidencePercentLabel;
	}

	public VBox getGoalVBox() {
		return goalVBox;
	}
	
}
