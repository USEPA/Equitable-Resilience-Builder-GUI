package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProjectSelectionController implements Initializable{

	@FXML
	TextField newProjectTextField;
	@FXML
	Button createButton;
	@FXML
	ListView<String> projectsListView;
	@FXML
	Button doneButton;
	@FXML
	VBox projectSelectionVBox;
	@FXML
	HBox newProjectHBox;
	
	private ArrayList<File> listOfProjects;
	private ERBMainController erbMainController;
	private boolean setup;
	private boolean action;
	public ProjectSelectionController(ArrayList<File> listOfProjects, ERBMainController erbMainController, boolean setup, boolean action) {
		this.listOfProjects = listOfProjects;
		this.erbMainController = erbMainController;
		this.setup = setup;
		this.action = action;
	}
	
	private Logger logger = LogManager.getLogger(ProjectSelectionController.class);
	
	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillProjectsListView();
		newProjectTextField.setFocusTraversable(false);
	}
	
	@FXML
	public void doneButtonAction() {
		String selectedProjectName = projectsListView.getSelectionModel().getSelectedItem();
		if(selectedProjectName != null && selectedProjectName.length() > 0) {
			File projectDirectory = getProjectDirectory(selectedProjectName);
			if (setup) {
				erbMainController.loadSetupTool(projectDirectory);
			} else if (action) {
				erbMainController.loadActionTool(projectDirectory);
			}
			erbMainController.closeProjectSelectionStage();
		} else {
			logger.debug("Cannot launch the setup tool. Selected project name = " + selectedProjectName);
		}
	}
	
	@FXML
	public void createButtonAction() {
		String projectNameText = newProjectTextField.getText();
		if (projectNameText != null && projectNameText.length() > 0) {
			if (!isDuplicateProjectName(projectNameText)) { // check for dupe name
				File newProjectDirectory = createNewProjectDirectories(projectNameText);
				listOfProjects.add(newProjectDirectory);
				fillProjectsListView();
			}
		} else {
			logger.debug("Cannot create new project. Project name = " + projectNameText);
		}
	}
	
	private File getProjectDirectory(String projectName) {
		for(File projectDirectory: listOfProjects) {
			if(projectDirectory.getName().contentEquals(projectName)) {
				return projectDirectory;
			}
		}
		logger.error("Project Directory returned was null.");
		return null;
	}
	
	private File createNewProjectDirectories(String newProjectName) {
		//Action directory
		File newProjectDirectory = new File(pathToERBFolder + "\\EngagementSetupTool\\" + newProjectName + "\\");
		if(!newProjectDirectory.exists()) {
			newProjectDirectory.mkdir();
		}
		//Setup directory
		newProjectDirectory = new File(pathToERBFolder + "\\EngagementActionTool\\" + newProjectName + "\\");
		if(!newProjectDirectory.exists()) {
			newProjectDirectory.mkdir();
		}
		return newProjectDirectory;
	}
	
	private boolean isDuplicateProjectName(String projectNameToCreate) {
		for (File file : listOfProjects) {
			if(file.getName().contentEquals(projectNameToCreate)) {
				return true;
			}
		}
		return false;
	}
	
	void fillProjectsListView() {
		projectsListView.getItems().clear();
		for(File file : listOfProjects) {
			projectsListView.getItems().add(file.getName());
		}
	}
	
	void removeNewProjectHBox() {
		if(projectSelectionVBox.getChildren().contains(newProjectHBox)) {
			projectSelectionVBox.getChildren().remove(newProjectHBox);
		}
	}

}
