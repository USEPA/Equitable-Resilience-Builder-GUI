package com.epa.erb.project;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.goal.GoalCreationController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.MainPanelHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class ProjectCreationController implements Initializable {

	@FXML
	ToggleGroup modeToggleGroup;
	@FXML
	TextField projectNameTextField;

	private String mode = "Facilitator Mode";
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(ProjectSelectionController.class);
	
	private App app;
	public ProjectCreationController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modeToggleGroup.selectedToggleProperty().addListener((changed, oldVal, newVal) -> modeChanged(oldVal, newVal));
	}
	
	private void modeChanged(Toggle oldToggle, Toggle newToggle) {
		RadioButton newRadioButton = (RadioButton) newToggle;
		mode = newRadioButton.getText();
	}

	@FXML
	public void createButtonAction() {
		String newProjectName = projectNameTextField.getText().trim();
		if (isValidNewProjectName(newProjectName)) {
			String cleanedProjectName = cleanStringForWindows(newProjectName);
			Project project = new Project(newProjectName, mode, cleanedProjectName);
			createNewProjectDirectory(project);
			launchProject(project);
		}
	}

	private boolean isValidNewProjectName(String projectName) {
		if (projectName != null && projectName.length() > 0) {
			if (!isDuplicateProjectName(projectName)) {
				return true;
			} else {
				showIsDuplicateProjectNameAlert();
			}
		}
		return false;
	}

	private String cleanStringForWindows(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "_");
	}

	private boolean isDuplicateProjectName(String projectName) {
		if (projectName != null) {
			ArrayList<Project> existingProjects = app.getProjects();
			for (Project project : existingProjects) {
				if (projectName.contentEquals(project.getProjectName())) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	private void showIsDuplicateProjectNameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText("Duplicate project name. Please enter a new project name.");
		alert.setTitle("Error");
		alert.showAndWait();
	}

	private void createNewProjectDirectory(Project project) {
		if (project != null) {
			File newProjectDirectory = fileHandler.getProjectDirectory(project);
			if (newProjectDirectory != null && !newProjectDirectory.exists()) {
				newProjectDirectory.mkdir();
			}
		} else {
			logger.error("Cannot createNewProjectDirectory. project is null.");
		}
	}
	
	private boolean isProjectNew(Project project) {
		if (project != null) {
			if (project.getProjectGoals() == null || project.getProjectGoals().size() == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private GoalCreationController createFacilitatorProject(Project project) {
		GoalCreationController goalCreationController = new GoalCreationController(app, project, this);
		ArrayList<Goal> projectGoals = createFacilitatorProjectGoals(goalCreationController, project);
		createFacilitatorProjectFiles(goalCreationController, project, projectGoals);
		return goalCreationController;
	}
	
	private ArrayList<Goal> createFacilitatorProjectGoals(GoalCreationController goalCreationController, Project project) {
		Goal defaultGoal = createGoal(goalCreationController);
		ArrayList<Goal> goals = new ArrayList<Goal>(Arrays.asList(defaultGoal));
		project.setProjectGoals(goals);
		return goals;
	}
	
	private void createFacilitatorProjectFiles(GoalCreationController goalCreationController, Project project, ArrayList<Goal> projectGoals) {
		goalCreationController.writeProjectMetaData(project);
		goalCreationController.writeGoalsMetaData(projectGoals);
	}
	
	private Goal createGoal(GoalCreationController goalCreationController) {
		String goalName = "Default Goal";
		String goalCleanedName = goalCreationController.cleanStringForWindows(goalName);
		String goalDescription = "Default goal holding all chapters and activities.";
		GoalCategory goalCategory = goalCreationController.getGoalCategory("All activities");
		ArrayList<GoalCategory> goalCategories = new ArrayList<GoalCategory>(Arrays.asList(goalCategory));
		Goal goal = new Goal(app, goalName, goalCleanedName, goalDescription, goalCategories);
		goal.setChapters(app.getAvailableActivities(), goalCreationController.getProject());
		return goal;
	}
	
	public void loadEngagementActionToContainer(Project project) {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		Parent engagementActionRoot = mainPanelHandler.loadEngagementActionRoot(app, project);
		app.loadNodeToERBContainer(engagementActionRoot);
	}
	
	
	private void launchProject(Project selectedProject) {
		if (selectedProject != null) {
			MainPanelHandler mainPanelHandler = new MainPanelHandler();
			app.setSelectedProject(selectedProject);
			if (isProjectNew(selectedProject)) {
				if (mode.contentEquals("Goal Mode")) {
					Parent goalCreationRoot = mainPanelHandler.loadGoalCreationToContainer(app, selectedProject, this);
					app.loadNodeToERBContainer(goalCreationRoot);
				} else {
					createFacilitatorProject(selectedProject);
					loadEngagementActionToContainer(selectedProject);
				}
				app.getErbContainerController().getMyBreadCrumbBar().setProject(selectedProject);
				app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(
						selectedProject.getProjectName() + " Landing",
						mainPanelHandler.getMainPanelIdHashMap().get("Engagement"));
			}
		}

	}

}
