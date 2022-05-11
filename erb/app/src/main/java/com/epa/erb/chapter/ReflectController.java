package com.epa.erb.chapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ReflectController implements Initializable{
	
	@FXML
	HBox erbHeading;
	@FXML
	VBox goalProgressVBox;
	@FXML
	VBox goalProgressBar;
	@FXML
	VBox statusVBox;
	@FXML
	VBox activityVBox;
	@FXML
	VBox ratingVBox;
	@FXML
	VBox percentVBox;
	@FXML
	VBox notesVBox;
	@FXML
	VBox confidenceRatingVBox;
	@FXML
	VBox confidenceRatingBar;
	@FXML
	ProgressBar chapterRatingProgressBar;
	@FXML
	Label goalPercentLabel;
	@FXML
	Label confidencePercentLabel;
	@FXML
	Label chapterRatingPercentLabel;
	@FXML
	Hyperlink appChapterNotesHyperlink;
	
	private Chapter chapter;
	private EngagementActionController engagementActionController;
	public ReflectController (Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	private ArrayList<Activity> chapterActivities = new ArrayList<Activity>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		fillChapterActivitiesList();
		populateControlsForChapterActivities();
	}
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		goalProgressVBox.heightProperty().addListener(e-> initProgress(engagementActionController.getCurrentGoal(), chapter));
	}
	
	private void fillChapterActivitiesList() {
		for(Activity activity : chapter.getAssignedActivities()) {
			if(!activity.getActivityID().contentEquals("25") && !activity.getActivityID().contentEquals("26")) {
				chapterActivities.add(activity);
			}
		}
	}
	
	private void populateControlsForChapterActivities() {
		populateStatusCheckBoxes(chapterActivities);
		populateActivityLabels(chapterActivities);
		populateRatingSliders(chapterActivities);
		populateNotesHyperlinks(chapterActivities);
		populatePercentLabels(chapterActivities);
	}
	
	private void populateNotesHyperlinks(ArrayList<Activity> activities) {
		ArrayList<Hyperlink> notesHyperlinks = createNotesHyperlinks(activities);
		addNotesHyperlinks(notesHyperlinks);
	}
	
	private ArrayList<Hyperlink> createNotesHyperlinks(ArrayList<Activity> activities){
		ArrayList<Hyperlink> listOfHyperlinks = new ArrayList<Hyperlink>();
		for(Activity activity: activities) {
			Hyperlink hyperlink = new Hyperlink("Add Notes");
			hyperlink.setId(activity.getActivityID());
			hyperlink.setFont(new Font(13.0));
			hyperlink.setOnAction(e-> notesHyperlinkClicked(hyperlink, activity));
			listOfHyperlinks.add(hyperlink);
		}
		return listOfHyperlinks;
	}
	
	private void addNotesHyperlinks(ArrayList<Hyperlink> notesHyperlinks) {
		for(Hyperlink hyperlink: notesHyperlinks) {
			notesVBox.getChildren().add(hyperlink);
		}
	}
	
	private void notesHyperlinkClicked(Hyperlink hyperlink, Activity activity) {
		System.out.println("Hyperlink clicked for " + activity.getLongName());
	}
	
	private void populateRatingSliders(ArrayList<Activity> activities) {
		ArrayList<Slider> ratingSliders = createRatingSliders(activities);
		addRatingSliders(ratingSliders);
	}
	
	private ArrayList<Slider> createRatingSliders(ArrayList<Activity> activities){
		ArrayList<Slider> listOfSliders = new ArrayList<Slider>();
		for(Activity activity: activities) {
			Slider slider = new Slider();
			slider.setId(activity.getActivityID());
			slider.setOnMouseClicked(e-> ratingSliderAdjusted(slider, activity));
			listOfSliders.add(slider);
		}
		return listOfSliders;
	}
	
	private void addRatingSliders(ArrayList<Slider> ratingSliders) {
		for(Slider slider : ratingSliders) {
			ratingVBox.getChildren().add(slider);
		}
	}
	
	private void ratingSliderAdjusted(Slider slider, Activity activity) {
		System.out.println("Slider adjusted for " + activity.getLongName());
		updatePercentLabel(activity, slider.getValue());
	}
	
	private void populatePercentLabels(ArrayList<Activity> activities) {
		ArrayList<Label> percentLabels = createPercentLabels(activities);
		addPercentLabels(percentLabels);
	}
	
	private ArrayList<Label> createPercentLabels(ArrayList<Activity> activities){
		ArrayList<Label> listOfLabels = new ArrayList<Label>();
		for(Activity activity : activities) {
			Label label = new Label("0%");
			label.setFont(new Font(14.0));
			label.setId(activity.getActivityID());
			listOfLabels.add(label);
		}
		return listOfLabels;
	}
	
	private void addPercentLabels(ArrayList<Label> percentLabels) {
		for(Label label : percentLabels) {
			percentVBox.getChildren().add(label);
		}
	}
	
	private void updatePercentLabel(Activity activity, double value) {
		int valueToDisplay = (int) value;
		Label percentLabel = getPercentLabel(activity);
		if(percentLabel != null) {
			percentLabel.setText(valueToDisplay + "%");
		}
	}
	
	private Label getPercentLabel(Activity activity) {
		for(int i =0; i < percentVBox.getChildren().size(); i++) {
			Label label = (Label)percentVBox.getChildren().get(i);
			if(label.getId().contentEquals(activity.getActivityID())) {
				return label;
			}
		}
		return null;
		//TODO: lOGGER DEBUG
	}
	
	private void populateActivityLabels(ArrayList<Activity> activities) {
		ArrayList<Label> activityLabels = createActivityLabels(activities);
		addActivityLabels(activityLabels);
	}
	
	private ArrayList<Label> createActivityLabels(ArrayList<Activity> activities){
		ArrayList<Label> listOfLabels = new ArrayList<Label>();
		for(Activity activity : activities) {
			Label label = new Label(activity.getLongName());
			label.setFont(new Font(14.0));
			label.setId(activity.getActivityID());
			listOfLabels.add(label);
		}
		return listOfLabels;
	}
	
	private void addActivityLabels(ArrayList<Label> activityLabels) {
		for(Label label : activityLabels) {
			activityVBox.getChildren().add(label);
		}
	}
	
	private void populateStatusCheckBoxes(ArrayList<Activity> activities) {
		ArrayList<CheckBox> statusCheckBoxes = createActivityStatusCheckBoxes(activities);
		addActivityStatusCheckBoxes(statusCheckBoxes);
	}
	
	private ArrayList<CheckBox> createActivityStatusCheckBoxes(ArrayList<Activity> activities){
		ArrayList<CheckBox> listOfCheckBoxes = new ArrayList<CheckBox>();
		for(Activity activity : activities) {
			CheckBox checkBox = new CheckBox();
			checkBox.setDisable(true);
			checkBox.setStyle("-fx-opacity: 1");
			checkBox.setId(activity.getActivityID());
			setActivityStatusCheckBox(checkBox, activity);
			listOfCheckBoxes.add(checkBox);
		}
		return listOfCheckBoxes;
	}	
	
	private void addActivityStatusCheckBoxes(ArrayList<CheckBox> statusCheckBoxes) {
		for(CheckBox checkBox: statusCheckBoxes) {
			statusVBox.getChildren().add(checkBox);
		}
	}
	
	private void setActivityStatusCheckBox(CheckBox checkBox, Activity activity) {
		if(activity.getStatus().contentEquals("complete")) {
			checkBox.setSelected(true);
		} else {
			checkBox.setSelected(false);
		}
	}
	
	public void initProgress(Goal goal, Chapter chapter) {
		handleGoalProgressBar(goal);
		handleConfidenceProgressBar(goal);
		handleChapterProgressBar(chapter);
	}
	
	private void handleGoalProgressBar(Goal goal) {
		int goalPercentDone = getGoalPercentDone(goal);
		setGoalProgress(goalPercentDone);
	}
	
	private int getGoalPercentDone(Goal goal) {
		ArrayList<Chapter> listOfChaptersInGoal = engagementActionController.getListOfChapters();
		double numberOfActivitiesInGoal = 0;
		double numberOfCompletedActivitiesInGoal = 0;
		for(Chapter chapter: listOfChaptersInGoal) {
			for(Activity activity: chapter.getAssignedActivities()) {
				if(!activity.getActivityID().contentEquals("25") && !activity.getActivityID().contentEquals("26")) {
					numberOfActivitiesInGoal++;
					if(activity.getStatus().contentEquals("complete")) {
						numberOfCompletedActivitiesInGoal++;
					}
				}
			}
		}
		return (int) ((numberOfCompletedActivitiesInGoal/numberOfActivitiesInGoal) * 100);
	}	
	
	private void setGoalProgress(int percent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalProgressVBox.getHeight();
				goalProgressBar.setMaxHeight(progressBarHeight);
				goalProgressBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = percent/fixedCapacity;
				goalProgressBar.setPrefHeight(progressBarHeight*progress);
				goalPercentLabel.setText(percent + "%");
			}
		});
	}
	
	private void handleConfidenceProgressBar(Goal goal) {
		int confidencePercent = getConfidencePercent(goal);
		setConfidenceRating(confidencePercent);
	}
	
	private int getConfidencePercent(Goal goal) {
		return (100);
	}
	
	private void setConfidenceRating(int percent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = confidenceRatingVBox.getHeight();
				confidenceRatingBar.setMaxHeight(progressBarHeight);
				confidenceRatingBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = percent / fixedCapacity;
				confidenceRatingBar.setPrefHeight(progressBarHeight * progress);
				confidencePercentLabel.setText(percent + "%");
			}
		});
	}
	
	private void handleChapterProgressBar(Chapter chapter) {
		int chapterPercent = getChapterPercentDone(chapter);
		setChapterRating(chapterPercent);
	}
	
	private int getChapterPercentDone(Chapter chapter) {
		double numberOfActivitiesInChapter =0;
		double numberOfCompletedActivitiesInChapter = 0;
		for (Activity activity : chapter.getAssignedActivities()) {
			if (!activity.getActivityID().contentEquals("25") && !activity.getActivityID().contentEquals("26")) {
				numberOfActivitiesInChapter++;
				if (activity.getStatus().contentEquals("complete")) {
					numberOfCompletedActivitiesInChapter++;
				}
			}
		}
		return (int) ((numberOfCompletedActivitiesInChapter / numberOfActivitiesInChapter) * 100);
	}
	
	private void setChapterRating(int percent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chapterRatingProgressBar.setProgress(percent / 100.0);
				chapterRatingPercentLabel.setText(percent + "%");
			}
		});
	}
	
}
