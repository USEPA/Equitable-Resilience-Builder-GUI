package com.epa.erb.chapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ReflectController implements Initializable{
	
	@FXML
	HBox erbHeading;
	@FXML
	VBox chapterProgressVBox;
	@FXML
	VBox chapterProgressBar;
	@FXML
	Label chapterCompletionInfoLabel;
	@FXML
	VBox statusVBox;
	@FXML
	VBox activityVBox;
	@FXML
	VBox ratingVBox;
	@FXML
	Label confidenceInfoLabel;
	@FXML
	VBox notesVBox;
	@FXML
	VBox confidenceRatingVBox;
	@FXML
	VBox confidenceRatingBar;
	@FXML
	Label chapterConfidenceInfoLabel;
	@FXML
	ProgressBar goalProgressBar;
	@FXML
	Label chapterPercentLabel;
	@FXML
	Label confidencePercentLabel;
	@FXML
	Label goalProgressLabel;
	@FXML
	Label goalCompletionInfoLabel;
	@FXML
	ScrollPane reflectScrollPane;
	@FXML
	VBox reflectVBox;
	
	private Goal goal;
	private Chapter chapter;
	private EngagementActionController engagementActionController;
	public ReflectController (Goal goal, Chapter chapter, EngagementActionController engagementActionController) {
		this.goal = goal;
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private Progress progress = new Progress();
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ReflectController.class);
	private ArrayList<Activity> chapterActivities = new ArrayList<Activity>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		addInfoToolTips();
		fillChapterActivitiesList();
		populateControlsForChapterActivities();
	}
	
	private void handleControls() {
		reflectScrollPane.widthProperty().addListener(e-> scrollPaneWidthChange());
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		goalProgressBar.getStylesheets().add(getClass().getResource("/ProgressBar.css").toString());
		engagementActionController.getMainVBox().heightProperty().addListener(e-> handleProgressBarsAfterPaneSizeChange());
		engagementActionController.getMainVBox().widthProperty().addListener(e-> handleProgressBarsAfterPaneSizeChange());
	}
	
	private void scrollPaneWidthChange() {
		reflectVBox.setMinWidth(reflectScrollPane.getWidth());
	}
	
	private void addInfoToolTips() {
		chapterCompletionInfoLabel.setTooltip(new Tooltip("Represents the total percent of activites complete in this chapter."));
		chapterCompletionInfoLabel.getTooltip().setFont(new Font(12.0));
		chapterConfidenceInfoLabel.setTooltip(new Tooltip("Represents the total percent of confidence that this chapter is helping to achieve the current goal."));
		chapterConfidenceInfoLabel.getTooltip().setFont(new Font(12.0));
		goalCompletionInfoLabel.setTooltip(new Tooltip("Represents the total percent of activites complete in this goal."));
		goalCompletionInfoLabel.getTooltip().setFont(new Font(12.0));
		confidenceInfoLabel.setTooltip(new Tooltip("Drag the slider on an activity to represet how confident you are that the activty helped achieve the current goal."));
		confidenceInfoLabel.getTooltip().setFont(new Font(12.0));
	}
	
	private void fillChapterActivitiesList() {
		for(Activity activity : chapter.getAssignedActivities()) {
			if(!activity.getActivityID().contentEquals("25") && !activity.getActivityID().contentEquals("26")) {
				chapterActivities.add(activity);
			}
		}
	}
	
	private void handleProgressBarsAfterPaneSizeChange() {
		Goal goal = engagementActionController.getCurrentGoal();
		if(goal != null) handleChapterProgressBar(goal);
		if(chapter != null) handleChapterConfidenceProgressBar(chapter);
	}
	
	private void populateControlsForChapterActivities() {
		populateStatusCheckBoxes(chapterActivities);
		populateActivityLabels(chapterActivities);
		populateRatingSliders(chapterActivities);
		populateNotesHyperlinks(chapterActivities);
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
				reflectNotesStage.setTitle("ERB: Notes");
				reflectNotesStage.show();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
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
			slider.setMin(0);
			slider.setMax(100);
	        slider.setSnapToTicks(true);
	        slider.setShowTickMarks(true);
	        slider.setShowTickLabels(true);
			setSliderLabels(slider);
			slider.setId(activity.getActivityID());
			slider.setOnMouseClicked(e-> ratingSliderAdjusted(slider, activity));
			slider.setValue(Double.parseDouble(activity.getRating()));
			listOfSliders.add(slider);
		}
		return listOfSliders;
	}
	
	private void setSliderLabels(Slider slider) {
		slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n <= 25) return "Not useful";
                if (n <= 75 && n > 25) return "Somewhat useful";
                return "Very useful";
            }
            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "Not useful":
                        return 0d;
                    case "Very useful":
                        return 100d;
                    default:
                        return 50d;
                }
            }
        });
	}
	
	private void addRatingSliders(ArrayList<Slider> ratingSliders) {
		for(Slider slider : ratingSliders) {
			ratingVBox.getChildren().add(slider);
		}
	}
	
	private void ratingSliderAdjusted(Slider slider, Activity activity) {
		activity.setRating(String.valueOf((int) slider.getValue()));
		handleChapterConfidenceProgressBar(chapter);
		engagementActionController.updateLocalProgress(chapter,goal.getChapters());
		activity.setSaved(false);
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
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				handleChapterProgressBar(goal);
				handleChapterConfidenceProgressBar(chapter);
				handleGoalProgressBar(chapter);
			}
		});
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
		int goalPercent = progress.getGoalPercentDone(goal.getChapters());
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

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public ArrayList<Activity> getChapterActivities() {
		return chapterActivities;
	}

	public void setChapterActivities(ArrayList<Activity> chapterActivities) {
		this.chapterActivities = chapterActivities;
	}

	public HBox getErbHeading() {
		return erbHeading;
	}

	public VBox getChapterProgressVBox() {
		return chapterProgressVBox;
	}

	public VBox getChapterProgressBar() {
		return chapterProgressBar;
	}

	public Label getChapterCompletionInfoLabel() {
		return chapterCompletionInfoLabel;
	}

	public VBox getStatusVBox() {
		return statusVBox;
	}

	public VBox getActivityVBox() {
		return activityVBox;
	}

	public VBox getRatingVBox() {
		return ratingVBox;
	}

	public Label getConfidenceInfoLabel() {
		return confidenceInfoLabel;
	}

	public VBox getNotesVBox() {
		return notesVBox;
	}

	public VBox getConfidenceRatingVBox() {
		return confidenceRatingVBox;
	}

	public VBox getConfidenceRatingBar() {
		return confidenceRatingBar;
	}

	public Label getChapterConfidenceInfoLabel() {
		return chapterConfidenceInfoLabel;
	}

	public ProgressBar getGoalProgressBar() {
		return goalProgressBar;
	}

	public Label getChapterPercentLabel() {
		return chapterPercentLabel;
	}

	public Label getConfidencePercentLabel() {
		return confidencePercentLabel;
	}

	public Label getGoalProgressLabel() {
		return goalProgressLabel;
	}

	public Label getGoalCompletionInfoLabel() {
		return goalCompletionInfoLabel;
	}

	public ScrollPane getReflectScrollPane() {
		return reflectScrollPane;
	}

	public VBox getReflectVBox() {
		return reflectVBox;
	}

	public Stage getReflectNotesStage() {
		return reflectNotesStage;
	}
	
}
