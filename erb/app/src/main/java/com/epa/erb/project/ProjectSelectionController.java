package com.epa.erb.project;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.GoalContainerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class ProjectSelectionController implements Initializable{

	@FXML
	TextField projectNameTextField;
	@FXML
	ListView<Project> projectsListView;
	
	private App app;
	public ProjectSelectionController(App app) {
		this.app = app;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ProjectSelectionController.class);
	private String pathToERBProjectsFolder = constants.getPathToERBProjectsFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setProjectsListViewCellFactory();
		fillProjectsListView();
	}
	
	private void handleControls() {
	}
	
	@FXML
	public void createButtonAction() {
		String newProjectName = projectNameTextField.getText().trim();
		if(isValidNewProjectName(newProjectName)) {
			Project project = new Project(newProjectName);
			createNewProjectDirectory(project);
			addProjectToListView(project);
			projectsListView.getSelectionModel().select(project);
			launchButtonAction();
		}
	}
	
	@FXML
	public void launchButtonAction() {
		Project selectedProject = projectsListView.getSelectionModel().getSelectedItem();
		if(selectedProject != null) {
			if(isProjectNew(selectedProject)) {
				loadGoalCreationToContainer(selectedProject);
			} else {
				loadEngagementActionToContainer(selectedProject);
			}
			app.setSelectedProject(selectedProject);
		}
	}
	
	private void loadGoalCreationToContainer(Project project) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalContainer.fxml"));
			GoalContainerController goalContainerController = new GoalContainerController(app, project);
			fxmlLoader.setController(goalContainerController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			app.loadContentToERBContainer(root);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadEngagementActionToContainer(Project project) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			EngagementActionController engagementActionController = new EngagementActionController(app, project);
			fxmlLoader.setController(engagementActionController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			app.loadContentToERBContainer(root);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private boolean isValidNewProjectName(String projectName) {
		if(projectName!= null && projectName.length() > 0) {
			if(!isDuplicateProjectName(projectName)) {
				if(isCharAllowableProjectName(projectName)) {
					return true;
				} else {
					showIsNotCharAllowableProjectName();
				}
			} else {
				showIsDuplicateProjectNameAlert();
			}
		}
		return false;
	}
	
	private boolean isDuplicateProjectName(String projectName) {
		ArrayList<Project> existingProjects = app.getProjects();
		for(Project project : existingProjects) {
			if(projectName.contentEquals(project.getProjectName())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isCharAllowableProjectName(String projectName) {
		String regexString = "^[A-za-z0-9-_]{1,255}$";
		if(projectName != null && projectName.length() > 0) {
			return projectName.matches(regexString);
		}else {
			return false;
		}
	}
	
	private boolean isProjectNew(Project project) {
		if(project.getProjectGoals() == null || project.getProjectGoals().size() ==0) {
			return true;
		} else {
			return false;
		}
	}
	
	private void showIsDuplicateProjectNameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText("Duplicate project name. Please enter a new project name.");
		alert.setTitle("Error");
		alert.showAndWait();
	}
	
	private void showIsNotCharAllowableProjectName() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText("Please enter a valid project name. (Allowable characters include letters, numbers, \"-\", \"_\")");
		alert.setTitle("Error");
		alert.showAndWait();
	}

	private void createNewProjectDirectory(Project project) {
		File newProjectDirectory = new File(pathToERBProjectsFolder + "\\" + project.getProjectName());
		if(!newProjectDirectory.exists()) {
			newProjectDirectory.mkdir();
		}
	}
	
	private void addProjectToListView(Project project) {
		if(!projectsListView.getItems().contains(project)) {
			projectsListView.getItems().add(project);
		}
	}
	
	private void fillProjectsListView() {
		ArrayList<Project> projects = app.getProjects();
		for (Project project : projects) {
			projectsListView.getItems().add(project);
		}
	}
	
	private void setProjectsListViewCellFactory() {
		projectsListView.setCellFactory(new Callback<ListView<Project>, ListCell<Project>>() {
			@Override
			public ListCell<Project> call(ListView<Project> param) {
				ListCell<Project> cell = new ListCell<Project>() {
					@Override
					protected void updateItem(Project item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getProjectName());
							setFont(new Font(14.0));
						}
					}
				};
				return cell;
			}
		});
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public TextField getProjectNameTextField() {
		return projectNameTextField;
	}

	public ListView<Project> getProjectsListView() {
		return projectsListView;
	}
	
}
