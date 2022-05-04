package com.epa.erb.chapter_progress_tracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import com.epa.erb.activity_progress_tracker.ProgressTrackerController;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.engagement_action.ERBPathwayDiagramController;
import com.epa.erb.engagement_action.EngagementActionController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class ProgressColumnController implements Initializable{

	@FXML
	Label chapterLabel;
	@FXML
	ProgressIndicator planProgressIndicator;
	@FXML
	ProgressIndicator engageProgressIndicator;
	@FXML
	ProgressIndicator reflectProgressIndicator;
	
	private Chapter chapter;
	private ArrayList<Chapter> listOfAllChapters;
	private EngagementActionController engagementActionController;
	public ProgressColumnController(Chapter chapter, ArrayList<Chapter> listOfAllChapters, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.listOfAllChapters = listOfAllChapters;
		this.engagementActionController = engagementActionController;
	}
	
	private Activity planActivity = null;
	private Activity reflectActivity = null;
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ProgressColumnController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		assignActivities();
		setInitialProgress();
		setChapterLabelText(chapter.getStringName());
	}
	
	private void assignActivities() {
		planActivity = chapter.getPlanActivity();
		reflectActivity = chapter.getReflectActivity();
	}
	
	private void setInitialProgress() {
		setInitialPlanProgress();
		handleEngageProgressChange();
		setInitialReflectProgress();
	}
	
	private void setInitialPlanProgress() {
		if(planActivity.getStatus().contentEquals("complete")) {
			handlePlanProgressChange(1.0);
		} else {
			handlePlanProgressChange(chapter.getPlanStatus());
		}
	}
	
	private void setInitialReflectProgress() {
		if(reflectActivity.getStatus().contentEquals("complete")) {
			handleReflectProgressChange(1.0);
		} else {
			handleReflectProgressChange(chapter.getReflectStatus());
		}
	}
	
	private void handleControls() {
		planProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
		engageProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
		reflectProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
	}
	
	void handlePlanProgressChange(double progress) {
		updateProgressIndicator(planProgressIndicator, progress);
		updateActivityStatus(planActivity, progress);
		chapter.setPlanStatus(progress/100.0);
		updateERBDiagram(planActivity.getGUID());
	}
	
	private void handleEngageProgressChange() {
		double engageCompleteProgress = getEngageProgress();
		updateProgressIndicator(engageProgressIndicator, engageCompleteProgress);
		chapter.setEngageStatus(engageCompleteProgress);
	}
	
	private double getEngageProgress() {
		double numberOfActivitiesInChapter = chapter.getNumberOfUserSelectedActivities() -2; //Subtract Plan & Reflect
		double numberOfCompletedActivitiesInChapter = chapter.getNumberOfCompletedActivities();
		return (numberOfCompletedActivitiesInChapter/numberOfActivitiesInChapter);	
	}
	
	void handleReflectProgressChange(double progress) {
		updateProgressIndicator(reflectProgressIndicator, progress);
		updateActivityStatus(reflectActivity, progress);
		chapter.setReflectStatus(progress/100.0);
		updateERBDiagram(reflectActivity.getGUID());
	}
	
	private void updateActivityStatus(Activity activity, double progress) {
		if (progress > 0.0 && progress < 1.0) {
			activity.setStatus("in progress");
		} else if (progress == 1.0) {
			activity.setStatus("complete");
		} else if(progress == 0.0) {
			activity.setStatus("ready");
		}
	}
	
	private void updateProgressIndicator(ProgressIndicator progressIndicator, double progress) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				progressIndicator.setProgress(progress);
			}
		});
	}
	
	private void updateERBDiagram(String GUID) {
		ERBPathwayDiagramController erbPathwayDiagramController = engagementActionController.getErbPathwayDiagramController(GUID);
		if(erbPathwayDiagramController != null) erbPathwayDiagramController.updateStatus();
	}
	
	@FXML
	public void engageProgressIndicatorClicked() {
		loadProgressTracker();
	}
	
	private void loadProgressTracker() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/activity_progress_tracker/ProgressTracker.fxml"));
			ProgressTrackerController progressTrackerController = new ProgressTrackerController(listOfAllChapters);
			fxmlLoader.setController(progressTrackerController);
			Parent root = fxmlLoader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Activity Progress Tracker");
			stage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void planProgressIndicatorClicked() {
		loadProgressAdjuster("Plan");
	}

	@FXML
	public void reflectProgressIndicatorClicked() {
		loadProgressAdjuster("Reflect");
	}
	
	private Stage progressAdjusterStage = null;
	private void loadProgressAdjuster(String label) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter_progress_tracker/ProgressAdjuster.fxml"));
			ProgressAdjusterController progressAdjusterController = new ProgressAdjusterController(label, this);
			fxmlLoader.setController(progressAdjusterController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			progressAdjusterStage = new Stage();
			progressAdjusterStage.setScene(scene);
			progressAdjusterStage.setTitle("Progress Adjuster");
			progressAdjusterStage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void setChapterLabelText(String text) {
		chapterLabel.setText(text);
	}
	
	public void closeProgressAdjusterStage() {
		if(progressAdjusterStage != null) {
			progressAdjusterStage.close();
		}
	}
	
	double getPlanProgress() {
		return planProgressIndicator.getProgress();
	}
	
	double getReflectProgress() {
		return reflectProgressIndicator.getProgress();
	}

	public Label getChapterLabel() {
		return chapterLabel;
	}

	public ProgressIndicator getPlanProgressIndicator() {
		return planProgressIndicator;
	}

	public ProgressIndicator getEngageProgressIndicator() {
		return engageProgressIndicator;
	}
	
	public ProgressIndicator getReflectProgressIndicator() {
		return reflectProgressIndicator;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public ArrayList<Chapter> getListOfAllChapters() {
		return listOfAllChapters;
	}

	public void setListOfAllChapters(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public Activity getPlanActivity() {
		return planActivity;
	}

	public Activity getReflectActivity() {
		return reflectActivity;
	}	
	
}
