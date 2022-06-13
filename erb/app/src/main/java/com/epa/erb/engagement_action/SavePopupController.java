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
import com.epa.erb.noteboard.CategorySectionController;
import com.epa.erb.noteboard.NoteBoardContentController;
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

	private Activity activity;
	private Chapter chapter;
	private Goal goal;
	private Project project;
	private ArrayList<Project> projects;
	private App app;
	private String saveType;
	private SaveHandler saveHandler;
	public SavePopupController(Activity activity, Chapter chapter, Goal goal, Project project, App app, String saveType, ArrayList<Project> projects, SaveHandler saveHandler) {
		this.activity = activity;
		this.chapter = chapter;
		this.goal = goal;
		this.project = project;
		this.projects = projects;
		this.app = app;
		this.saveType = saveType;
		this.saveHandler = saveHandler;
	}
	
	private Constants constants = new Constants();
	private String pathToERBProjectsFolder = constants.getPathToDynamicERBProjectsFolder();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		runTask();
	}

	private void runTask() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (saveType.contentEquals("ALL")) {
					saveAllProjects(projects);
				} else if (saveType.contentEquals("PROJECT")) {
					saveProject(project);
				} else if (saveType.contentEquals("GOAL")) {
					saveGoal(project, goal);
				} else if (saveType.contentEquals("CHAPTER")) {
					saveChapter(project, goal, chapter);
				} else if (saveType.contentEquals("ACTIVITY")) {
					saveActivity(project, goal, activity);
				}
				Thread.sleep(1000);
				setGoalSaveDone();
				Thread.sleep(1000);
				return null;
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				saveHandler.closeSavePopupStage();
			}
		});
		progressIndicator.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
	}
	
	private void saveAllProjects(ArrayList<Project> listOfProjects) {
		for (Project project : listOfProjects) {
			if (!project.isSaved()) {
				saveProject(project);
			}
		}
	}
	
	private void saveProject(Project project) {
		for (Goal goal : project.getProjectGoals()) {
			if (!goal.isSaved()) {
				saveGoal(project, goal);
			}
		}
	}
	
	private void saveGoal(Project project, Goal goal) {
		for (Chapter chapter : goal.getChapters()) {
			if (!chapter.isSaved()) {
				saveChapter(project, goal, chapter);
			}
		}
	}
	
	private void saveChapter(Project project, Goal goal, Chapter chapter) {
		for (Activity activity : chapter.getAssignedActivities()) {
			if (!activity.isSaved()) {
				saveActivity(project, goal, activity);
			}
		}
		chapter.setSaved(true);
	}
	
	private void saveActivity(Project project, Goal goal, Activity activity) {
		callToWriteActivityDataXML(project, goal, activity);
		callToWriteGoalDataXML(project, goal);
		activity.setSaved(true);
	}
	
	public void callToWriteGoalDataXML(Project project, Goal goal) {
		XMLManager xmlManager = new XMLManager(app);
		xmlManager.writeGoalMetaXML(getGoalXMLFile(project, goal), goal.getChapters());
	}
	
	public void callToWriteActivityDataXML(Project project, Goal goal, Activity activity) {
		if(activity.getActivityType().getLongName().contentEquals("Worksheet")) {
			callToWriteWorksheetDataXML();
		} else if(activity.getActivityType().getLongName().contentEquals("Workbook")) {
			callToWriteWorkbookDataXML();
		} else if(activity.getActivityType().getLongName().contentEquals("Noteboard")) {
			callToWriteNoteboardDataXML(activity, goal, project);
		}
	}
	
	private void callToWriteNoteboardDataXML(Activity activity, Goal goal, Project project) {	
		XMLManager xmlManager = new XMLManager(app);
		NoteBoardContentController noteBoardContentController = activity.getNoteBoardContentController();
		if(noteBoardContentController != null) {
			noteBoardContentController.setCategoryPostIts();
			ArrayList<CategorySectionController> categories = noteBoardContentController.getCategorySectionControllers();
			xmlManager.writeNoteboardDataXML(getActivityXMLFile(project, goal, activity), categories);	
		}
	}
	
	private void callToWriteWorkbookDataXML() {
		
	}
	
	private void callToWriteWorksheetDataXML() {
		
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
