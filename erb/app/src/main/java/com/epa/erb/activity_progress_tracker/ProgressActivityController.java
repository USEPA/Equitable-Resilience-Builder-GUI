package com.epa.erb.activity_progress_tracker;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class ProgressActivityController implements Initializable{

	@FXML
	Label activityLabel;
	@FXML
	Circle activityCircle;

	private Activity activity;
	public ProgressActivityController(Activity activity) {
		this.activity = activity;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	private void handleControls() {
		colorCircle();
		setActivityLabelText(activity.getShortName());
	}
	
	private void colorCircle() {
		if(activity.getStatus().contentEquals("ready")) {
			activityCircle.setStyle("-fx-fill: " + constants.getReadyStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("complete")) {
			activityCircle.setStyle("-fx-fill: " + constants.getCompleteStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("skipped")) {
			activityCircle.setStyle("-fx-fill: " + constants.getSkippedStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("in progress")) {
			activityCircle.setStyle("-fx-fill: " + constants.getInProgressStatusColor() + ";");
		}
	}
	
	private void setActivityLabelText(String text) {
		activityLabel.setText(text);
	}

	public Label getActivityLabel() {
		return activityLabel;
	}

	public Circle getActivityCircle() {
		return activityCircle;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
