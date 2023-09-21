package com.epa.erb.utility;

import java.util.HashMap;
import com.epa.erb.App;
import com.epa.erb.ERBLandingController;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.engagement_action.EngagementGoalLandingContentController;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectCreationController;
import com.epa.erb.project.ProjectSelectionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainPanelHandler {
	
	private HashMap<String, String> mainPanelIdHashMap = new HashMap<String, String>();
	
	public MainPanelHandler() {
		initMainPanelIdHashMap();
	}
	
	private void initMainPanelIdHashMap() {
		mainPanelIdHashMap.put("ERB Home", "86");
		mainPanelIdHashMap.put("Project Selection", "87");
		mainPanelIdHashMap.put("Goal Creation", "88");
		mainPanelIdHashMap.put("Engagement", "89");
		mainPanelIdHashMap.put("Project Creation", "90");
		mainPanelIdHashMap.put("ERB Dashboard", "90");

	}
	
	public Parent loadERBLanding(App app) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLanding.fxml"));
			ERBLandingController erbLandingNew2Controller = new ERBLandingController(app);
			fxmlLoader.setController(erbLandingNew2Controller);
			return fxmlLoader.load();
		} catch (Exception e) {
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
			return null;
		}
	}
	
	public VBox loadERBDashboardRoot(App app) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementGoalLandingContent.fxml"));
			EngagementGoalLandingContentController chapterLandingController = new EngagementGoalLandingContentController(app.getEngagementActionController());
			fxmlLoader.setController(chapterLandingController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			return null;
		}
	}
	
	public HashMap<String, String> getMainPanelIdHashMap() {
		return mainPanelIdHashMap;
	}	

}
