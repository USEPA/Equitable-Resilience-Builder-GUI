package com.epa.erb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
	
	File actionDataFile = null;
	
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
		if (selectedProjectName != null && selectedProjectName.length() > 0) {
			File projectDirectory = getProjectDirectory(selectedProjectName);
			if (setup) {
				handleProjectSetupLogic(projectDirectory);
				erbMainController.loadSetupTool(projectDirectory);
			} else if (action) {
				handleProjectActionLogic(projectDirectory);
				erbMainController.loadActionTool(projectDirectory, actionDataFile);
			}
			erbMainController.closeProjectSelectionStage();
		} else {
			logger.debug("Cannot launch the setup tool. Selected project name = " + selectedProjectName);
		}
	}
	
	private void handleProjectActionLogic(File projectDirectory) {
		File projectSetupDirectory = new File(pathToERBFolder + "\\EngagementSetupTool\\" + projectDirectory.getName());
		if(projectDirectoryHasDataFile(projectDirectory)) {
			File setupFile = new File(projectSetupDirectory.getPath() + "\\Data.xml");
			File actionFile = new File(projectDirectory.getPath() + "\\Data.xml");
			if(setupFile.exists() && actionFile.exists()) {
				if(!filesAreSame(setupFile, actionFile)) {
					loadDateSelection(setupFile, actionFile);
				}
			}
		} else {
			if(projectDirectoryHasDataFile(projectSetupDirectory)) { //Copy data from setup
				File sourceFile = new File(projectSetupDirectory.getPath() + "\\Data.xml");
				File destFile = new File(projectDirectory.getPath() + "\\Data.xml");
				copyFile(sourceFile, destFile);
			} else { //Prompt user to create data in setup
				showActionProjectDataNonExistentAlert();
			}
		}
	}
	
	private Stage dataSelectionStage = null;
	private void loadDateSelection(File setupFile, File actionFile) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/DataSelection.fxml"));
			DataSelectionController dataSelectionController = new DataSelectionController(setupFile, actionFile, this);
			fxmlLoader.setController(dataSelectionController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			dataSelectionStage = new Stage();
			dataSelectionStage.setScene(scene);
			dataSelectionStage.setTitle("Data Selection");
			dataSelectionStage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private boolean filesAreSame(File sourceFile, File destFile) {
		try {
			return FileUtils.contentEquals(sourceFile, destFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	private void copyFile(File sourceFile, File destFile) {
		InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(sourceFile);
	        os = new FileOutputStream(destFile);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	        is.close();
	        os.close();
	    } catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void handleProjectSetupLogic(File projectDirectory) {
		if (projectDirectoryHasDataFile(projectDirectory)) {
			Optional<ButtonType> result = showSetupProjectDataExistsAlert();
			if (result.get().getButtonData() == ButtonData.OTHER) {
				if (result.get().getText().contains("Overwrite")) {
					removeExistingProjectSetupData(projectDirectory);
				}
			}
		}
	}
	
	private void showActionProjectDataNonExistentAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText("There is no data for this project. Please use Pt.1 of the tool to create data.");
		alert.setTitle("No Data Alert");
		alert.showAndWait();
	}

	private void removeExistingProjectSetupData(File projectDirectory) {
		File setupDataFile = new File(projectDirectory.getPath() + "\\Data.xml");
		if(setupDataFile.exists()) {
			setupDataFile.delete();
		}
	}
	
	private Optional<ButtonType> showSetupProjectDataExistsAlert() {
		ButtonType loadButtonType = new ButtonType("Load existing data", ButtonData.OTHER);
		ButtonType overwriteButtonType = new ButtonType("Overwrite existing data", ButtonData.OTHER);
		ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.CONFIRMATION, "This project contains data. How would you like to proceed?", loadButtonType, overwriteButtonType, cancelButtonType);
		alert.setTitle("Project Selection");
		alert.setHeaderText(null);
		Optional<ButtonType> result = alert.showAndWait();
		return result;
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
	
	private boolean projectDirectoryHasDataFile(File projectDirectory) {
		File [] projectFiles = projectDirectory.listFiles();
		if(projectFiles != null && projectFiles.length > 0) {
			for(File projectFile : projectFiles) {
				if(projectFile.getName().contentEquals("Data.xml")) {
					return true;
				}
			}
		}
		return false;
	}
	
	void closeDataSelectionStage() {
		if(dataSelectionStage != null) {
			dataSelectionStage.close();
		}
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
	
	void setActionDataFile(File dataFile) {
		actionDataFile = dataFile;
	}

}
