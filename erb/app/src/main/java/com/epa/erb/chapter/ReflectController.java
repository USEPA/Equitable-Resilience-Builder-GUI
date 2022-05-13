package com.epa.erb.chapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import com.epa.erb.Progress;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ReflectController implements Initializable{
	
	@FXML
	HBox erbHeading;
	@FXML
	VBox chapterProgressVBox;
	@FXML
	VBox chapterProgressBar;
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
	ProgressBar goalProgressBar;
	@FXML
	Label chapterPercentLabel;
	@FXML
	Label confidencePercentLabel;
	@FXML
	Label goalProgressLabel;
	
	private Chapter chapter;
	private EngagementActionController engagementActionController;
	public ReflectController (Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	private Progress progress = new Progress();
	private ArrayList<Activity> chapterActivities = new ArrayList<Activity>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		fillChapterActivitiesList();
		populateControlsForChapterActivities();
	}
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		goalProgressBar.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
		chapterProgressVBox.heightProperty().addListener(e-> initProgress(engagementActionController.getCurrentGoal(), chapter));
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
			Hyperlink hyperlink = new Hyperlink("Notes");
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
		loadReflectNotes(activity, null);
	}
	
	@FXML
	private void addChapterNotesAction() {
		loadReflectNotes(null, chapter);
	}
	
	private Stage reflectNotesStage = null;

	private void loadReflectNotes(Activity activity, Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ReflectNotes.fxml"));
			ReflectNotesController reflectNotesController = null;
			if (activity != null) reflectNotesController = new ReflectNotesController(activity, this);
			if (chapter != null) reflectNotesController = new ReflectNotesController(chapter, this);
			if (reflectNotesController != null) {
				fxmlLoader.setController(reflectNotesController);
				Parent root = fxmlLoader.load();
				reflectNotesStage = new Stage();
				Scene scene = new Scene(root);
				reflectNotesStage.setScene(scene);
				reflectNotesStage.setTitle("Notes");
				reflectNotesStage.show();
			}
		} catch (Exception e) {
			// TODO: Add logger statement
		}
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
			slider.setValue(Double.parseDouble(activity.getRating()));
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
		updatePercentLabel(activity, slider.getValue());
		activity.setRating(String.valueOf((int) slider.getValue()));
		handleChapterConfidenceProgressBar(chapter);
		engagementActionController.handleLocalProgress(chapter, engagementActionController.getListOfChapters());
	}
	
	private void populatePercentLabels(ArrayList<Activity> activities) {
		ArrayList<Label> percentLabels = createPercentLabels(activities);
		addPercentLabels(percentLabels);
	}
	
	private ArrayList<Label> createPercentLabels(ArrayList<Activity> activities){
		ArrayList<Label> listOfLabels = new ArrayList<Label>();
		for(Activity activity : activities) {
			Label label = new Label(activity.getRating() + "%");
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
		handleChapterProgressBar(goal);
		handleChapterConfidenceProgressBar(chapter);
		handleGoalProgressBar(chapter);
	}
	
	private void handleChapterProgressBar(Goal goal) {
		int chapterPercent = progress.getChapterPercentDone(chapter);
		setChapterProgress(chapterPercent);
	}
	
	private void setChapterProgress(int percent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = chapterProgressVBox.getHeight();
				chapterProgressBar.setMaxHeight(progressBarHeight);
				chapterProgressBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = percent/fixedCapacity;
				chapterProgressBar.setPrefHeight(progressBarHeight*progress);
				chapterPercentLabel.setText(percent + "%");
			}
		});
	}
	
	private void handleChapterConfidenceProgressBar(Chapter chapter) {
		int confidencePercent = progress.getChapterConfidencePercent(chapter);
		setChapterConfidenceRating(confidencePercent);
	}
	
	private void setChapterConfidenceRating(int percent) {
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
	
	private void handleGoalProgressBar(Chapter chapter) {
		int goalPercent = progress.getGoalPercentDone(engagementActionController.getListOfChapters());
		setGoalRating(goalPercent);
	}

	private void setGoalRating(int percent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				goalProgressBar.setProgress(percent / 100.0);
				goalProgressLabel.setText(percent + "%");
			}
		});
	}
	
	public void closeReflectNotesStage() {
		if(reflectNotesStage!=null) {
			reflectNotesStage.close();
		}
	}
	
}
