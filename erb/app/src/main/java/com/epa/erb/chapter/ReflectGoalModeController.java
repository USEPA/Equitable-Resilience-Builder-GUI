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

public class ReflectGoalModeController implements Initializable{
	
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
	public ReflectGoalModeController (Goal goal, Chapter chapter, EngagementActionController engagementActionController) {
		this.goal = goal;
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private Progress progress = new Progress();
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ReflectGoalModeController.class);
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
		goalProgressBar.getStylesheets().add(getClass().getResource("/progressBar.css").toString());
		engagementActionController.getMainVBox().heightProperty().addListener(e-> mainPaneSizeChanged());
		engagementActionController.getMainVBox().widthProperty().addListener(e-> mainPaneSizeChanged());
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
		if (chapter != null) {
			for (Activity activity : chapter.getAssignedActivities()) {
				chapterActivities.add(activity);
			}
		} else {
			logger.error("Cannot fillChapterActivitiesList. chapter is null.");
		}
	}
	
	private void mainPaneSizeChanged() {
		updateChapterProgressBar(chapter);
		updateChapterConfidenceProgressBar(chapter);
	}
	
	private void populateControlsForChapterActivities() {
		populateStatusLabels(chapterActivities);
		populateActivityLabels(chapterActivities);
		populateRatingSliders(chapterActivities);
		populateNotesHyperlinks(chapterActivities);
	}
	
	private void populateNotesHyperlinks(ArrayList<Activity> activities) {
		ArrayList<Hyperlink> notesHyperlinks = createNotesHyperlinks(activities);
		addNotesHyperlinks(notesHyperlinks);
	}
	
	private ArrayList<Hyperlink> createNotesHyperlinks(ArrayList<Activity> activities) {
		ArrayList<Hyperlink> listOfHyperlinks = new ArrayList<Hyperlink>();
		if (activities != null) {
			for (Activity activity : activities) {
				Hyperlink hyperlink = new Hyperlink("Notes");
				hyperlink.setId(activity.getActivityID());
				hyperlink.setFont(new Font(13.0));
				hyperlink.setOnAction(e -> notesHyperlinkClicked(activity));
				listOfHyperlinks.add(hyperlink);
			}
		} else {
			logger.error("Cannot createNotedHyperlinks. activites is null.");
		}
		return listOfHyperlinks;
	}
	
	private void addNotesHyperlinks(ArrayList<Hyperlink> notesHyperlinks) {
		if (notesHyperlinks != null) {
			for (Hyperlink hyperlink : notesHyperlinks) {
				notesVBox.getChildren().add(hyperlink);
			}
		} else {
			logger.error("Cannot addNotesHyperlinks. notesHyperlinks is null.");
		}
	}
	
	private void notesHyperlinkClicked(Activity activity) {
		Parent reflectNotesRoot = loadReflectNotes(activity, null);
		showReflectNotes(reflectNotesRoot);
	}
	
	@FXML
	private void addChapterNotesAction() {
		Parent reflectNotesRoot = loadReflectNotes(null, chapter);
		showReflectNotes(reflectNotesRoot);
	}
	
	private Parent loadReflectNotes(Activity activity, Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ReflectNotes.fxml"));
			ReflectNotesController reflectNotesController = null;
			if (activity != null) reflectNotesController = new ReflectNotesController(activity, this);
			if (chapter != null) reflectNotesController = new ReflectNotesController(chapter, this);
			if (reflectNotesController != null) fxmlLoader.setController(reflectNotesController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private Stage reflectNotesStage = null;
	private void showReflectNotes(Parent reflectNotesRoot) {
		if (reflectNotesRoot != null) {
			reflectNotesStage = new Stage();
			Scene scene = new Scene(reflectNotesRoot);
			reflectNotesStage.setScene(scene);
			reflectNotesStage.setTitle("ERB: Notes");
			reflectNotesStage.show();
		} else {
			logger.error("Cannot showReflectNotes. reflectNotesRoot is null.");
		}
	}
	
	private void populateRatingSliders(ArrayList<Activity> activities) {
		ArrayList<Slider> ratingSliders = createRatingSliders(activities);
		addRatingSliders(ratingSliders);
	}
	
	private ArrayList<Slider> createRatingSliders(ArrayList<Activity> activities) {
		ArrayList<Slider> listOfSliders = new ArrayList<Slider>();
		if (activities != null) {
			for (Activity activity : activities) {
				Slider slider = new Slider();
				slider.setMin(0);
				slider.setMax(100);
				slider.setSnapToTicks(true);
				slider.setShowTickMarks(true);
				slider.setShowTickLabels(true);
				setSliderLabels(slider);
				slider.setId(activity.getActivityID());
				slider.setOnMouseClicked(e -> ratingSliderAdjusted(slider, activity));
				slider.setValue(Double.parseDouble(activity.getRating()));
				listOfSliders.add(slider);
			}
		} else {
			logger.error("Cannot createRatingSliders. activites is null.");
		}
		return listOfSliders;
	}
	
	private void setSliderLabels(Slider slider) {
		if (slider != null) {
			slider.setLabelFormatter(new StringConverter<Double>() {
				@Override
				public String toString(Double n) {
					if (n <= 25)
						return "Not useful";
					if (n <= 75 && n > 25)
						return "Somewhat useful";
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
		} else {
			logger.error("Cannot setSliderLabels. slider is null.");
		}
	}
	
	private void addRatingSliders(ArrayList<Slider> ratingSliders) {
		if(ratingSliders != null) {
		for(Slider slider : ratingSliders) {
			ratingVBox.getChildren().add(slider);
		}
		} else {
			logger.error("Cannot addRatingSliders. ratingSliders is null.");
		}
	}
	
	private void ratingSliderAdjusted(Slider slider, Activity activity) {
		activity.setRating(String.valueOf((int) slider.getValue()));
		updateChapterConfidenceProgressBar(chapter);
		engagementActionController.updateLocalProgress(chapter,goal.getChapters());
		activity.setSaved(false);
	}
	
	private void populateActivityLabels(ArrayList<Activity> activities) {
		ArrayList<Label> activityLabels = createActivityLabels(activities);
		addActivityLabels(activityLabels);
	}
	
	private ArrayList<Label> createActivityLabels(ArrayList<Activity> activities) {
		ArrayList<Label> listOfLabels = new ArrayList<Label>();
		if (activities != null) {
			for (Activity activity : activities) {
				Label label = new Label(activity.getLongName());
				label.setFont(new Font(14.0));
				label.setId(activity.getActivityID());
				listOfLabels.add(label);
			}
		} else {
			logger.error("Cannot createActivityLabels. activites is null.");
		}
		return listOfLabels;
	}
	
	private void addActivityLabels(ArrayList<Label> activityLabels) {
		if (activityLabels != null) {
			for (Label label : activityLabels) {
				activityVBox.getChildren().add(label);
			}
		} else {
			logger.error("Cannot addActivityLabels. activityLabels is null.");
		}
	}
	
	private void populateStatusLabels(ArrayList<Activity> activities) {
		ArrayList<Label> statusLabels = createActivityStatusLabels(activities);
		addActivityStatusLabel(statusLabels);
	}	
	
	private ArrayList<Label> createActivityStatusLabels(ArrayList<Activity> activities) {
		ArrayList<Label> listOfLabels = new ArrayList<Label>();
		if (activities != null) {
			for (Activity activity : activities) {
				Label label = new Label();
				label.setFont(new Font(14.0));
				label.setId(activity.getActivityID());
				setActivityStatusLabel(label, activity);
				listOfLabels.add(label);
			}
		} else {
			logger.error("Cannot createActivityStatusLabels. activities is null.");
		}
		return listOfLabels;
	}
	
	private void addActivityStatusLabel(ArrayList<Label> statusLabels) {
		if (statusLabels != null) {
			for (Label label : statusLabels) {
				statusVBox.getChildren().add(label);
			}
		} else {
			logger.error("Cannot addActivityStatusLabel. statusLabels is null.");
		}
	}
	
	private void setActivityStatusLabel(Label label, Activity activity) {
		label.setText(activity.getStatus());
	}
	
	public void initProgress(Goal goal, Chapter chapter) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateChapterProgressBar(chapter);
				updateChapterConfidenceProgressBar(chapter);
				updateGoalProgressBar(chapter);
			}
		});
	}
	
	private void updateChapterProgressBar(Chapter chapter) {
		int chapterPercent = progress.getChapterPercentDone(chapter);
		if(chapterPercent >= 0) {
			progress.setChapterProgress(chapterProgressVBox, chapterProgressBar, chapterPercentLabel, chapterPercent);
		}
	}
	
	private void updateChapterConfidenceProgressBar(Chapter chapter) {
		int confidencePercent = progress.getChapterConfidencePercent(chapter);
		if (confidencePercent >= 0) {
			progress.setChapterRating(confidenceRatingVBox, confidenceRatingBar, confidencePercentLabel,confidencePercent);
		}
	}
	
	private void updateGoalProgressBar(Chapter chapter) {
		int goalPercent = progress.getGoalPercentDone(goal.getChapters());
		if(goalPercent >= 0) {
			progress.setGoalProgress(goalProgressBar, goalProgressLabel, goalPercent);
		}
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
