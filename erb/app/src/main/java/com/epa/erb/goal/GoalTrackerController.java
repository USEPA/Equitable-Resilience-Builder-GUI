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
	VBox goalVBox;
	
	private Goal goal;
	public GoalTrackerController(Goal goal) {
		this.goal = goal;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setGoalTitleLabelText(goal.getGoalName());
		setGoalTitleLabelWidth();
		updateGoalProgress();
		addProgressListener();
	}
	
	public void setGoalTitleLabelWidth() {
		double width =  goal.getGoalName().length() * 10.0;
		goalTitleLabel.setMinWidth(width);
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

	public VBox getGoalVBox() {
		return goalVBox;
	}
	
}
