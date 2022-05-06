package com.epa.erb.project;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.goal.GoalCreationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ProjectSelectionController implements Initializable{

	@FXML
	VBox welcomeVBox;
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
	//private String pathToERBProjectsFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBProjectsFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\Projects";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setProjectsListViewCellFactory();
		fillProjectsListView();
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	@FXML
	public void createButtonAction() {
		String newProjectName = projectNameTextField.getText();
		if(isValidNewProjectName(newProjectName)) {
			Project project = new Project(newProjectName);
			createNewProjectDirectory(project);
			addProjectToListView(project);
		}
	}
	
	private boolean isValidNewProjectName(String projectName) {
		if(projectName!= null && projectName.length() > 0) {
			if(!isDuplicateProjectName(projectName)) {
				return true;
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
	
	private void showIsDuplicateProjectNameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText("Duplicate project name. Please enter a new name.");
		alert.setTitle("Error");
		alert.showAndWait();
	}

	@FXML
	public void launchButtonAction() {
		Project selectedProject = projectsListView.getSelectionModel().getSelectedItem();
		if(selectedProject != null) {
			loadGoalCreation(selectedProject);
		}
		app.closeProjectSelectionStage();
	}
	
	private Stage goalCreationStage = null;
	private void loadGoalCreation(Project project) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalCreation.fxml"));
			GoalCreationController goalCreationController = new GoalCreationController(app, project, this);
			fxmlLoader.setController(goalCreationController);
			Parent root = fxmlLoader.load();
			goalCreationStage = new Stage();
			Scene scene = new Scene(root);
			goalCreationStage.setScene(scene);
			goalCreationStage.setTitle("Goals");
			goalCreationStage.showAndWait();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
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
	
	void closeGoalCreationStage() {
		if(goalCreationStage != null) {
			goalCreationStage.close();
		}
	}
	
}
