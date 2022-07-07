package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private XMLManager xmlManager = new XMLManager(app);
	private Logger logger = LogManager.getLogger(SavePopupController.class);
	private String pathToERBProjectsFolder = constants.getPathToERBProjectsFolder();

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
				setSavePopupDone();
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
		if (listOfProjects != null) {
			for (Project project : listOfProjects) {
				if (!project.isSaved()) {
					saveProject(project);
				}
			}
		} else {
			logger.error("Cannot saveAllProjects. listOfProjects is null.");
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
		if (goal != null) {
			File goalXMLFile = getGoalXMLFile(project, goal);
			xmlManager.writeGoalMetaXML(goalXMLFile, goal.getChapters());
		} else {
			logger.error("Cannot callToWriteGoalDataXML. goal is null.");
		}
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
		if (activity != null) {
			NoteBoardContentController noteBoardContentController = activity.getNoteBoardContentController();
			if (noteBoardContentController != null) {
				noteBoardContentController.setCategoryPostIts();
				ArrayList<CategorySectionController> categories = noteBoardContentController.getCategorySectionControllers();
				xmlManager.writeNoteboardDataXML(getActivityXMLFile(project, goal, activity), categories);
			}
		} else {
			logger.error("Cannot callToWriteNoteboardDataXML. activity is null.");
		}
	}
	
	private void callToWriteWorkbookDataXML() {
		
	}
	
	private void callToWriteWorksheetDataXML() {
		
	}

	public void setSavePopupDone() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setStatusLabelText("Done saving.");
				progressIndicator.setVisible(false);
			}
		});
	}
	
	private File getGoalXMLFile(Project project, Goal goal) {
		if (project != null && goal != null) {
			File goalMetaFile = new File(pathToERBProjectsFolder + "\\" + project.getProjectName() + "\\Goals\\" + goal.getGoalName() + "\\Meta.xml");
			if (goalMetaFile.exists()) {
				return goalMetaFile;
			}
		} else {
			logger.error("Cannot getGoalXMLFile. project or goal is null.");
		}
		return null;
	}
	
	private File getActivityXMLFile(Project project, Goal goal, Activity activity) {
		if (project != null && goal != null && activity != null) {
			File activityDataFile = new File(pathToERBProjectsFolder + "\\" + project.getProjectName() + "\\Goals\\"+ goal.getGoalName() + "\\Activities\\" + activity.getActivityID() + "\\Data.xml");
			return activityDataFile;
		} else {
			logger.error("Cannot getActivityXMLFile. param is null.");
			return null;
		}
	}

	private void setStatusLabelText(String text) {
		statusLabel.setText(text);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public SaveHandler getSaveHandler() {
		return saveHandler;
	}

	public void setSaveHandler(SaveHandler saveHandler) {
		this.saveHandler = saveHandler;
	}

	public Label getStatusLabel() {
		return statusLabel;
	}

	public ProgressIndicator getProgressIndicator() {
		return progressIndicator;
	}
	
}
