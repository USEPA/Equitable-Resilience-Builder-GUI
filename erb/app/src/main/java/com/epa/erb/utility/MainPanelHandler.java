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
			logger.log(Level.SEVERE, "Failed to load ERBLanding.fxml: " + e.getMessage());
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
			logger.log(Level.SEVERE, "Failed to load ProjectSelection.fxml: " + e.getMessage());
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
			logger.log(Level.SEVERE, "Failed to load ProjectCreation.fxml: " + e.getMessage());
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
			logger.log(Level.SEVERE, "Failed to load EngagementAction.fxml: " + e.getMessage());
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
			logger.log(Level.SEVERE, "Failed to load ProjectCenter.fxml: " + e.getMessage());
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
		return mainPanelIdHashMap;
	}
	
	// Although this does not remove the hard-coded IDs,
	// it does make it simpler to reference the meaning of each ID.
	public void loadPanel(String mainPanelId, Project project) {
		if(mainPanelId.contentEquals("86")) {
			Parent root = loadERBLanding(app);
			app.addNodeToERBContainer(root);
		} else if(mainPanelId.contentEquals("87")) {
			Parent root = loadProjectSelectionRoot(app);
			app.addNodeToERBContainer(root);
		} else if(mainPanelId.contentEquals("88")) {
			// Don't allow right now
		} else if(mainPanelId.contentEquals("89")) {
			Parent root = loadEngagementActionRoot(app, project);
			app.addNodeToERBContainer(root);
		} else if(mainPanelId.contentEquals("90")){
			Parent root = loadProjectCreationRoot(app);
			app.addNodeToERBContainer(root);
		} else { }
	}

	public App getApp() {
		return app;
	}

}
