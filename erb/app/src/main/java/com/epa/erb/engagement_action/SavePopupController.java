package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.XMLManager;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class SavePopupController implements Initializable {

	@FXML
	Label statusLabel;
	@FXML
	ProgressIndicator progressIndicator;

	private App app;
	private ArrayList<Project> projects;
	private EngagementActionController engagementActionController;
	public SavePopupController(App app, ArrayList<Project> projects,EngagementActionController engagementActionController) {
		this.app = app;
		this.projects = projects;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	private String pathToERBProjectsFolder = constants.getPathToLocalERBProjectsFolder();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		runTask();
	}

	private void runTask() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (Project project : projects) {
					saveData(project);
					setAllToSaved(project);
				}
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

	private void saveData(Project project) {
		if (!project.isSaved()) {
			for (Goal goal : project.getProjectGoals()) {
				if (!goal.isSaved()) {
					saveGoalData(project, goal);
					for (Chapter chapter : goal.getChapters()) {
						if (!chapter.isSaved()) {
							for (Activity activity : chapter.getAssignedActivities()) {
								if (!activity.isSaved()) {
									saveActivityData(project, goal, activity);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void saveGoalData(Project project, Goal goal) {
		XMLManager xmlManager = new XMLManager(app);
		xmlManager.writeGoalMetaXML(getGoalXMLFile(project, goal), goal.getChapters());
	}
	
	public void saveActivityData(Project project, Goal goal, Activity activity) {
		if(activity.getActivityType().getLongName().contentEquals("Worksheet")) {
			saveWorksheetData();
		} else if(activity.getActivityType().getLongName().contentEquals("Workbook")) {
			saveWorkbookData();
		} else if(activity.getActivityType().getLongName().contentEquals("Noteboard")) {
			saveNoteboardData(activity, goal, project);
		}
	}
	
	private void saveNoteboardData(Activity activity, Goal goal, Project project) {
//		XMLManager xmlManager = new XMLManager(app);
//		for(NoteBoardContentController noteBoardContentController: listOfAllNoteBoardContentControllers) {
//			if(noteBoardContentController.getActivity().getActivityID().contentEquals(activity.getActivityID())) {
//				noteBoardContentController.setCategoryPostIts();
//				ArrayList<CategorySectionController> categories = noteBoardContentController.getCategorySectionControllers();
//				xmlManager.writeNoteboardDataXML(getActivityXMLFile(project, goal, activity), categories);
//			}
//		}
	}
	
	private void saveWorkbookData() {
		
	}
	
	private void saveWorksheetData() {
		
	}
	
	public void setAllToSaved(Project project) {
		for (Goal goal : project.getProjectGoals()) {
			for (Chapter chapter : goal.getChapters()) {
				chapter.setSaved(true);
				for (Activity activity : chapter.getAssignedActivities()) {
					activity.setSaved(true);
				}
			}
		}
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

	private void setStatusLabelText(String text) {
		statusLabel.setText(text);
	}
	
	private File getGoalXMLFile(Project project, Goal goal) {
		File goalMetaFile = new File(pathToERBProjectsFolder + "\\" + project.getProjectName() + "\\Goals\\" + goal.getGoalName() + "\\Meta.xml");
		if(goalMetaFile.exists()) {
			return goalMetaFile;
		}
		return null;
	}
	
	private File getActivityXMLFile(Project project, Goal goal, Activity activity) {
		File activityDataFile = new File(pathToERBProjectsFolder + "\\" + project.getProjectName() + "\\Goals\\" + goal.getGoalName() + "\\Activities\\" + activity.getActivityID() + "\\Data.xml");
		return activityDataFile;
	}

}
