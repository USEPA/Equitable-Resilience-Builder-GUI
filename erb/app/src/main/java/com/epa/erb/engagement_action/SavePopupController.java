package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.XMLManager;
import com.epa.erb.goal.Goal;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class SavePopupController implements Initializable{

	@FXML
	Label goalLabel;
	@FXML
	Label statusLabel;
	@FXML
	ProgressIndicator progressIndicator;
	
	private App app;
	private Goal goal;
	private File goalXMLFile;
	private EngagementActionController engagementActionController;
	public SavePopupController(App app, Goal goal, File goalXMLFile, EngagementActionController engagementActionController) {
		this.app = app;
		this.goal = goal;
		this.goalXMLFile = goalXMLFile;
		this.engagementActionController = engagementActionController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		runTask();
	}
	
	private void runTask() {
		setGoalLabelText(goal.getGoalName());
		Task<Void> task = new Task<Void>() {
			@Override
            protected Void call() throws Exception {
               saveGoalData();
               Thread.sleep(2000);
               setGoalSaveDone();
               Thread.sleep(1500);
               return null;
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				engagementActionController.closeSavePopupStage();
			}
		});
        
        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
	}
	
	public void saveGoalData() {
		XMLManager xmlManager = new XMLManager(app);
		xmlManager.writeGoalMetaXML(goalXMLFile, goal.getChapters());
	}
	
	public void setGoalSaveDone() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setStatusLabelText("Done saving.");
				progressIndicator.setVisible(false);
			}
		});
	}
	
	private void setGoalLabelText(String text) {
		goalLabel.setText(text);
	}
	
	private void setStatusLabelText(String text) {
		statusLabel.setText(text);
	}

}
