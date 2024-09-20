package com.epa.erb.project;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.goal.GoalCreation;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.MainPanelHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProjectCreationController implements Initializable {

	private FileHandler fileHandler;
	private String mode = "Facilitator Mode";
	
	private App app;
	public ProjectCreationController(App app) {
		this.app = app;
		
		fileHandler = new FileHandler(app);
	}
	
	@FXML
	VBox vBox;
	@FXML
	HBox goalModeHBox;
	@FXML
	ToggleGroup modeToggleGroup;
	@FXML
	TextField projectNameTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		goalModeHBox.setVisible(false);
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
			String projectDescription = "";
			Project project = new Project(newProjectName, mode, cleanedProjectName, projectDescription);
			createNewProjectDirectory(project);
			app.updateAvailableProjectsList();
			launchProject(project);
		}
	}
	
	public void createExploreProject() {
		String newProjectName = "Explore";
		String cleanedProjectName = cleanStringForWindows(newProjectName);
		String projectDescription = "Explore Project";
		Project project = new Project(newProjectName, mode, cleanedProjectName, projectDescription);
		createFacilitatorProject(project);
	}

	boolean isValidNewProjectName(String projectName) {
		if (projectName != null && projectName.length() > 0) {
			if (!isDuplicateProjectName(projectName)) {
				return true;
			} else {
				showIsDuplicateProjectNameAlert();
			}
		}
		return false;
	}

	String cleanStringForWindows(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "_");
	}

	private boolean isDuplicateProjectName(String projectName) {
		ArrayList<Project> existingProjects = app.getProjects();
		if (existingProjects != null) {
			for (Project project : existingProjects) {
				if (projectName.contentEquals(project.getProjectName())) {
					return true;
				}
			}
		}
		return false;
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
	
	private GoalCreation createFacilitatorProject(Project project) {
		GoalCreation goalCreationController = new GoalCreation(app, project);
		ArrayList<Goal> projectGoals = createFacilitatorProjectGoals(goalCreationController, project);
		createFacilitatorProjectFiles(goalCreationController, project, projectGoals);
		return goalCreationController;
	}
	
	private ArrayList<Goal> createFacilitatorProjectGoals(GoalCreation goalCreationController, Project project) {
		Goal defaultGoal = createGoal(goalCreationController, project);
		ArrayList<Goal> goals = new ArrayList<Goal>(Arrays.asList(defaultGoal));
		project.setProjectGoals(goals);
		return goals;
	}
	
	private void createFacilitatorProjectFiles(GoalCreation goalCreationController, Project project, ArrayList<Goal> projectGoals) {
		goalCreationController.writeProjectMetaData(project);
		goalCreationController.writeGoalsMetaData(projectGoals);
	}
	
	private Goal createGoal(GoalCreation goalCreationController, Project project) {
		String goalName = "Default Goal";
		String goalCleanedName = goalCreationController.cleanStringForWindows(goalName);
		String goalDescription = "Default goal holding all chapters and activities.";
		GoalCategory goalCategory = goalCreationController.getGoalCategory("All activities");
		ArrayList<GoalCategory> goalCategories = new ArrayList<GoalCategory>(Arrays.asList(goalCategory));
		Goal goal = new Goal(app, goalName, goalCleanedName, goalDescription, goalCategories);
		goal.setContentItems(project);
		return goal;
	}
	
	public void loadEngagementActionToContainer(Project project) {
		MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
		Parent engagementActionRoot = mainPanelHandler.loadEngagementActionRoot(app, project);
		app.addNodeToERBContainer(engagementActionRoot);
	}
	
	
	private void launchProject(Project selectedProject) {
		if (selectedProject != null) {
			MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
			app.setSelectedProject(selectedProject);
			if (isProjectNew(selectedProject)) {
				if (!mode.contentEquals("Goal Mode")) {
					createFacilitatorProject(selectedProject);
					loadEngagementActionToContainer(selectedProject);
				}
				app.getErbContainerController().getMyBreadCrumbBar().setProject(selectedProject);
				app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(selectedProject.getProjectName() + " Project", mainPanelHandler.getMainPanelIdHashMap().get("Engagement"));
			}
		}

	}

}
