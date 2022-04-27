package com.epa.erb.erb_progress_tracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import com.epa.erb.activity_progress_tracker.ProgressTrackerController;
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
	public ProgressColumnController(Chapter chapter, ArrayList<Chapter> listOfAllChapters) {
		this.chapter = chapter;
		this.listOfAllChapters = listOfAllChapters;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ProgressColumnController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		handlePlanArc(0.0);
		handleEngageArc();
		handleReflectArc(0.0);
	}
	
	private void handleControls() {
		chapterLabel.setText(chapter.getStringName());
		planProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
		engageProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
		reflectProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
	}
	
	void handlePlanArc(double progress) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				planProgressIndicator.setProgress(progress);
			}
		});
	}
	
	private void handleEngageArc() {
		double numberOfActivitiesInChapter = chapter.getNumberOfUserSelectedActivities();
		double numberOfCompletedActivitiesInChapter = chapter.getNumberOfCompleteActivities();
		double complete = (numberOfCompletedActivitiesInChapter/numberOfActivitiesInChapter);		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				engageProgressIndicator.setProgress(complete);
			}
		});
	}
	
	void handleReflectArc(double progress) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				reflectProgressIndicator.setProgress(progress);
			}
		});
	}
	
	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
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
