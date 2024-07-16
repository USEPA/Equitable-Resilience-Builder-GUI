package com.epa.erb.utility;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.epa.erb.App;
import com.epa.erb.ERBLandingController;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.engagement_action.ProjectCenterController;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectCreationController;
import com.epa.erb.project.ProjectSelectionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class MainPanelHandler {

	private Logger logger;
	
	private App app;
	public MainPanelHandler(App app) {
		this.app = app;
		
		logger = app.getLogger();
	}

	public Parent loadERBLanding(App app) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLanding.fxml"));
			ERBLandingController erbLandingNew2Controller = new ERBLandingController(app);
			fxmlLoader.setController(erbLandingNew2Controller);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load ERBLanding.fxml.");
			logger.log(Level.FINER, "Failed to load ERBLanding.fxml: " + e.getStackTrace());
			return null;
		}
	}

	public Parent loadProjectSelectionRoot(App app) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectSelection.fxml"));
			ProjectSelectionController projectSelectionController = new ProjectSelectionController(app);
			fxmlLoader.setController(projectSelectionController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load ProjectSelection.fxml.");
			logger.log(Level.FINER, "Failed to load ProjectSelection.fxml: " + e.getStackTrace());
			return null;
		}
	}

	public Parent loadProjectCreationRoot(App app) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectCreation.fxml"));
			ProjectCreationController projectCreationController = new ProjectCreationController(app);
			fxmlLoader.setController(projectCreationController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load ProjectCreation.fxml.");
			logger.log(Level.FINER, "Failed to load ProjectCreation.fxml: " + e.getStackTrace());
			return null;
		}
	}

	public Parent loadEngagementActionRoot(App app, Project project) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			EngagementActionController engagementActionController = new EngagementActionController(app, project);
			app.setEngagementActionController(engagementActionController);
			fxmlLoader.setController(engagementActionController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load EngagementAction.fxml.");
			logger.log(Level.FINER, "Failed to load EngagementAction.fxml: " + e.getStackTrace());
			return null;
		}
	}

	public VBox loadERBProjectCenterRoot(App app) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ProjectCenter.fxml"));
			ProjectCenterController chapterLandingController = new ProjectCenterController(
					app.getEngagementActionController());
			fxmlLoader.setController(chapterLandingController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load ProjectCenter.fxml.");
			logger.log(Level.FINER, "Failed to load ProjectCenter.fxml: " + e.getStackTrace());
			return null;
		}
	}

	public HashMap<String, String> getMainPanelIdHashMap() {
		HashMap<String, String> mainPanelIdHashMap = new HashMap<String, String>();
		mainPanelIdHashMap.put("ERB Home", "86");
		mainPanelIdHashMap.put("Project Selection", "87");
		mainPanelIdHashMap.put("Goal Creation", "88");
		mainPanelIdHashMap.put("Engagement", "89");
		mainPanelIdHashMap.put("Project Creation", "90");
		mainPanelIdHashMap.put("ERB Dashboard", "90");
		return mainPanelIdHashMap;
	}

	public App getApp() {
		return app;
	}

}
