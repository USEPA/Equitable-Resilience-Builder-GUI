package com.epa.erb.erb_progress_tracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import com.epa.erb.activity_progress_tracker.ProgressTrackerController;
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
		planActivity = chapter.getPlanActivity();
		reflectActivity = chapter.getReflectActivity();
		handleControls();
		setInitialProgress();
	}
	
	private void setInitialProgress() {
		if(planActivity.getStatus().contentEquals("complete")) {
			handlePlanProgress(1.0);
		} else {
			handlePlanProgress(chapter.getPlanStatus());
		}
		handleEngageProgress();
		if(reflectActivity.getStatus().contentEquals("complete")) {
			handleReflectProgress(1.0);
		} else {
			handleReflectProgress(chapter.getReflectStatus());
		}
	}
	
	private void handleControls() {
		chapterLabel.setText(chapter.getStringName());
		planProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
		engageProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
		reflectProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
	}
	
	void handlePlanProgress(double progress) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				planProgressIndicator.setProgress(progress);
				if (progress > 0.0 && progress < 1.0) {
					planActivity.setStatus("in progress");
				} else if (progress == 1.0) {
					planActivity.setStatus("complete");
				}
				chapter.setPlanStatus(progress/100.0);
				ERBPathwayDiagramController erbPathwayDiagramController = engagementActionController.getErbPathwayDiagramController(planActivity.getGUID());
				if(erbPathwayDiagramController != null) erbPathwayDiagramController.updateStatus();
			}
		});
	}
	
	private void handleEngageProgress() {
		double numberOfActivitiesInChapter = chapter.getNumberOfUserSelectedActivities() -2; //Subtract Plan & Reflect
		double numberOfCompletedActivitiesInChapter = chapter.getNumberOfCompletedActivities();
		double complete = (numberOfCompletedActivitiesInChapter/numberOfActivitiesInChapter);		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				engageProgressIndicator.setProgress(complete);
				chapter.setEngageStatus(complete);
			}
		});
	}
	
	void handleReflectProgress(double progress) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				reflectProgressIndicator.setProgress(progress);
				if (progress > 0.0 && progress < 1.0) {
					reflectActivity.setStatus("in progress");
				} else if (progress == 1.0) {
					reflectActivity.setStatus("complete");
				}
				chapter.setReflectStatus(progress/100.0);
				ERBPathwayDiagramController erbPathwayDiagramController = engagementActionController.getErbPathwayDiagramController(reflectActivity.getGUID());
				if(erbPathwayDiagramController != null) erbPathwayDiagramController.updateStatus();
			}
		});
	}
	
	private Stage progressPlanAdjusterStage = null;
	@FXML
	public void planProgressIndicatorClicked() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb_progress_tracker/ProgressAdjuster.fxml"));
			ProgressAdjusterController progressAdjusterController = new ProgressAdjusterController("Plan", this);
			fxmlLoader.setController(progressAdjusterController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			progressPlanAdjusterStage = new Stage();
			progressPlanAdjusterStage.setScene(scene);
			progressPlanAdjusterStage.setTitle("Progress Adjuster");
			progressPlanAdjusterStage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void engageProgressIndicatorClicked() {
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
	
	private Stage progressReflectAdjusterStage = null;
	@FXML
	public void reflectProgressIndicatorClicked() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb_progress_tracker/ProgressAdjuster.fxml"));
			ProgressAdjusterController progressAdjusterController = new ProgressAdjusterController("Reflect", this);
			fxmlLoader.setController(progressAdjusterController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			progressReflectAdjusterStage = new Stage();
			progressReflectAdjusterStage.setScene(scene);
			progressReflectAdjusterStage.setTitle("Progress Adjuster");
			progressReflectAdjusterStage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void closeProgressPlanAdjusterStage() {
		if(progressPlanAdjusterStage != null) {
			progressPlanAdjusterStage.close();
		}
	}
	
	double getPlanProgress() {
		return planProgressIndicator.getProgress();
	}
	
	public void closeProgressReflectAdjusterStage() {
		if(progressReflectAdjusterStage != null) {
			progressReflectAdjusterStage.close();
		}
	}
	
	double getReflectProgress() {
		return reflectProgressIndicator.getProgress();
	}
	
}
