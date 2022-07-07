package com.epa.erb.engagement_action;

import java.util.ArrayList;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SaveHandler {
	
	private App app;
	private String saveOrigin;
	private Activity activity;
	private Chapter chapter;
	private Goal goal;
	private Project project;
	private ArrayList<Project> projects;
	public SaveHandler(App app, String saveOrigin, Activity activity, Chapter chapter, Goal goal, Project project, ArrayList<Project> projects) {
		this.app = app;
		this.saveOrigin = saveOrigin;
		this.activity = activity;
		this.chapter = chapter;
		this.goal = goal;
		this.project = project;
		this.projects = projects;
	}
	
	private Logger logger = LogManager.getLogger(SaveHandler.class);

	public void beginSave() {
		String saveType = determineSavePrompt();
		if (!saveType.contentEquals("NO")) {
			Parent savePopupRoot = loadSavePopup(activity, chapter, goal, project, projects, saveType);
			showSavePopup(savePopupRoot);
		}
	}
	
	private String determineSavePrompt() {
		if (saveOrigin.contentEquals("close")) {
			if (!allProjectsSaved(projects)) {
				return showToolSaveNeeded();
			}
		} else if (saveOrigin.contentEquals("projectChange")) {
			if (!project.isSaved()) {
				return showProjectSaveNeeded();
			}
		} else if (saveOrigin.contentEquals("saveButton")) {
			if(!project.isSaved()) {
				return "PROJECT";
			}
		} else if (saveOrigin.contentEquals("goalChange")) {
			if (!goal.isSaved()) {
				return showGoalSaveNeeded();
			}
		} else if (saveOrigin.contentEquals("chapterChange")) {
			if (!chapter.isSaved()) {
				String resultString = showChapterSaveNeeded();
				if(resultString.contentEquals("NO")) {
					chapter.setSaved(true);
				}
				return resultString;
			}
		} else if (saveOrigin.contentEquals("activityChange")) {
			if (!activity.isSaved()) {
				String resultString = showActivitySaveNeeded();
				if(resultString.contentEquals("NO")) {
					activity.setSaved(true);
				}
				return resultString;
			}
		}
		return "NO";
	}
	
	public Parent loadSavePopup(Activity activity, Chapter chapter, Goal goal, Project project,ArrayList<Project> projects, String saveType) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/SavePopup.fxml"));
			SavePopupController savePopupController = new SavePopupController(activity, chapter, goal, project, app, saveType, projects, this);
			fxmlLoader.setController(savePopupController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}	
	
	private Stage savePopupStage = null; // Save prompt on app close
	private void showSavePopup(Parent savePopupRoot) {
		if (savePopupRoot != null) {
			savePopupStage = new Stage();
			Scene scene = new Scene(savePopupRoot);
			savePopupStage.setScene(scene);
			savePopupStage.setTitle("Saving...");
			savePopupStage.show();
		} else {
			logger.error("Cannot showSavePopup. savePopupRoot is null.");
		}
	}
	
	private boolean allProjectsSaved(ArrayList<Project> projects) {
		if (projects != null) {
			for (Project project : projects) {
				if (!project.isSaved()) {
					return false;
				}
			}
		}
		return true;
	}
	
	private String showActivitySaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this activity?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "ACTIVITY";
		return "NO" ;
	}
	
	private String showChapterSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this chapter?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "CHAPTER";
		return "NO" ;
	}
	
	private String showGoalSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this goal?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "GOAL";
		return "NO" ;
	}
	
	private String showProjectSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this project?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "PROJECT";
		return "NO" ;
	}
	
	private String showToolSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this tool?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "ALL";
		return "NO" ;
	}
	
	public void closeSavePopupStage() {
		if (savePopupStage != null) {
			savePopupStage.close();
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getSaveOrigin() {
		return saveOrigin;
	}

	public void setSaveOrigin(String saveOrigin) {
		this.saveOrigin = saveOrigin;
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

	public Stage getSavePopupStage() {
		return savePopupStage;
	}

	public void setSavePopupStage(Stage savePopupStage) {
		this.savePopupStage = savePopupStage;
	}

}
