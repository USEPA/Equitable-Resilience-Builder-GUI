package com.epa.erb.utility;

import java.util.HashMap;
import com.epa.erb.App;
import com.epa.erb.ERBLandingController;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.GoalContainerController;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectCreationController;
import com.epa.erb.project.ProjectSelectionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class MainPanelHandler {

	private HashMap<String, String> mainPanelIdHashMap = new HashMap<String, String>();

	public MainPanelHandler() {
		initMainPanelIdHashMap();
	}

	private void initMainPanelIdHashMap() {
		mainPanelIdHashMap.put("ERB Landing", "001");
		mainPanelIdHashMap.put("Project Selection", "002");
		mainPanelIdHashMap.put("Goal Creation", "003");
		mainPanelIdHashMap.put("Engagement", "004");
		mainPanelIdHashMap.put("Project Creation", "005");
	}

	public Parent loadERBLanding(App app) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLanding.fxml"));
			ERBLandingController erbLandingController = new ERBLandingController(app);
			fxmlLoader.setController(erbLandingController);
			return fxmlLoader.load();
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
			return null;
		}
	}

	public Parent loadGoalCreationToContainer(App app, Project project,
			ProjectCreationController projectCreationController) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalContainer.fxml"));
			GoalContainerController goalContainerController = new GoalContainerController(app, project,
					projectCreationController);
			fxmlLoader.setController(goalContainerController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<String, String> getMainPanelIdHashMap() {
		return mainPanelIdHashMap;
	}

}
