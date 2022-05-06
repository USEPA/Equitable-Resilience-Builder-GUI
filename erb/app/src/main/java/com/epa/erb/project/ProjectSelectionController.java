package com.epa.erb.project;

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

import com.epa.erb.DataSelectionController;
import com.epa.erb.ERBMainController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
	HBox newProjectHBox;
	@FXML
	VBox projectSelectionVBox;
	@FXML
	TextField newProjectTextField;
	@FXML
	ListView<String> projectsListView;
	
	private boolean setup;
	private boolean action;
	private ERBMainController erbMainController;
	private ArrayList<File> listOfProjectDirectories;
	public ProjectSelectionController(ArrayList<File> listOfProjectDirectories, ERBMainController erbMainController, boolean setup, boolean action) {
		this.listOfProjectDirectories = listOfProjectDirectories;
		this.erbMainController = erbMainController;
		this.setup = setup;
		this.action = action;
	}
	
	private File dataFileToLoadInActionTool = null;
	private Logger logger = LogManager.getLogger(ProjectSelectionController.class);
	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillProjectsListView();
		handleControls();
	}
	
	private void handleControls() {
		newProjectTextField.setFocusTraversable(false);
		if(action && !setup) removeNewProjectHBox();
	}
	
	@FXML
	public void doneButtonAction() {
		String selectedProjectName = projectsListView.getSelectionModel().getSelectedItem();
		if (selectedProjectName != null && selectedProjectName.length() > 0) { //if selected project is not null
			File projectDirectory = getProjectDirectory(selectedProjectName); //get project directory (accounts for setup or action)
			if (projectDirectory != null) handleToolLogicAndLoad(projectDirectory);
			erbMainController.closeProjectSelectionStage();
		} else {
			logger.error("Cannot launch. Selected project name = " + selectedProjectName);
		}
	}
	
	private void handleToolLogicAndLoad(File projectDirectory) {
		if (setup) {
			handleProjectSetupLogic(projectDirectory);
			erbMainController.loadSetupTool(projectDirectory);
		} else if (action) {
			boolean launch = handleProjectActionLogic(projectDirectory);
			if (launch) { //if ok to launch
				erbMainController.loadActionTool(projectDirectory, dataFileToLoadInActionTool);
			}
		}
	}
	
	private boolean handleProjectActionLogic(File projectActionDirectory) {
		File projectSetupDirectory = new File(pathToERBFolder + "\\EngagementSetupTool\\" + projectActionDirectory.getName());
		File setupDataFile = new File(projectSetupDirectory.getPath() + "\\Data.xml");
		File actionDataFile = new File(projectActionDirectory.getPath() + "\\Data.xml");
		if (projectDirectoryHasDataFile(projectActionDirectory)) { //if there is a data file in action
			if (setupDataFile.exists() && actionDataFile.exists()) { //if there is a data file in setup & action
				if (!filesAreSame(setupDataFile, actionDataFile)) { //if data files are not the same
					loadDateSelection(setupDataFile, actionDataFile); //prompt user to choose data file
				}
			}
			return true; //ok to launch
		} else { //if there is not a data file in action
			if (projectDirectoryHasDataFile(projectSetupDirectory)) { //if there is a data file in setup
				copyFile(setupDataFile, actionDataFile); //copy the data file in setup -> action
				return true; //ok to launch
			} else { //if there is not a data file in setup
				showActionProjectDataNonExistentAlert(); //prompt user to create data in setup
				return false; //not ok to launch
			}
		}
	}
	
	private void handleProjectSetupLogic(File projectSetupDirectory) {
		if (projectDirectoryHasDataFile(projectSetupDirectory)) { //if there is a data file in setup
			Optional<ButtonType> result = showSetupProjectDataExistsAlert(); //prompt user to load or overwrite data
			if (result.get().getButtonData() == ButtonData.OTHER) { //if not cancel button selected
				if (result.get().getText().contains("Overwrite")) { //if overwrite
					removeExistingProjectSetupData(projectSetupDirectory); //remove data file in setup
				}
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
		if (projectNameText != null && projectNameText.length() > 0) { //if new project name is not null
			if (!isDuplicateProjectName(projectNameText)) { // if new project name is not a duplicate name
				File newProjectDirectory = createNewProjectDirectories(projectNameText); //create new project directories
				if (newProjectDirectory != null) { //if new project directory is not null
					listOfProjectDirectories.add(newProjectDirectory);
					fillProjectsListView();
				}
			}
		} else {
			logger.error("Cannot create new project. Project name = " + projectNameText);
		}
	}
	
	private File getProjectDirectory(String projectName) {
		for(File projectDirectory: listOfProjectDirectories) {
			if(projectDirectory.getName().contentEquals(projectName)) {
				return projectDirectory;
			}
		}
		logger.debug("Project Directory returned was null.");
		return null;
	}
	
	private File createNewProjectDirectories(String newProjectName) {
		File newProjectActionDirectory = createNewProjectActionDirectory(newProjectName);
		File newProjectSetupDirectory = createNewProjectSetupDirectory(newProjectName);
		if(setup) {
			return newProjectSetupDirectory;
		} else if (action) {
			return newProjectActionDirectory;
		} else {
			logger.debug("New Project Directory returned was null.");
			return null;
		}
	}
	
	private File createNewProjectSetupDirectory(String newProjectName) {
		// Setup directory
		File newProjectDirectory = new File(pathToERBFolder + "\\EngagementSetupTool\\" + newProjectName + "\\");
		if (!newProjectDirectory.exists()) {
			newProjectDirectory.mkdir();
		}
		return newProjectDirectory;
	}
	
	private File createNewProjectActionDirectory(String newProjectName) {
		// Action directory
		File newProjectDirectory = new File(pathToERBFolder + "\\EngagementActionTool\\" + newProjectName + "\\");
		if (!newProjectDirectory.exists()) {
			newProjectDirectory.mkdir();
		}
		return newProjectDirectory;
	}
	
	private boolean isDuplicateProjectName(String projectNameToCreate) {
		for (File projectDirectory : listOfProjectDirectories) {
			if(projectDirectory.getName().contentEquals(projectNameToCreate)) {
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
	
	public void closeDataSelectionStage() {
		if(dataSelectionStage != null) {
			dataSelectionStage.close();
		}
	}
	
	void fillProjectsListView() {
		projectsListView.getItems().clear();
		for(File projectDirectory : listOfProjectDirectories) {
			projectsListView.getItems().add(projectDirectory.getName());
		}
	}
	
	void removeNewProjectHBox() {
		if(projectSelectionVBox.getChildren().contains(newProjectHBox)) {
			projectSelectionVBox.getChildren().remove(newProjectHBox);
		}
	}
	
	public HBox getNewProjectHBox() {
		return newProjectHBox;
	}

	public VBox getProjectSelectionVBox() {
		return projectSelectionVBox;
	}
	
	public TextField getNewProjectTextField() {
		return newProjectTextField;
	}
	
	public ListView<String> getProjectsListView() {
		return projectsListView;
	}
	
	public boolean isSetup() {
		return setup;
	}

	public void setSetup(boolean setup) {
		this.setup = setup;
	}

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}
	
	public ArrayList<File> getListOfProjectDirectories() {
		return listOfProjectDirectories;
	}

	public void setListOfProjectDirectories(ArrayList<File> listOfProjectDirectories) {
		this.listOfProjectDirectories = listOfProjectDirectories;
	}

	public ERBMainController getErbMainController() {
		return erbMainController;
	}

	public void setErbMainController(ERBMainController erbMainController) {
		this.erbMainController = erbMainController;
	}

	public File getDataFileToLoadInActionTool() {
		return dataFileToLoadInActionTool;
	}

	public void setDataFileToLoadInActionTool(File dataFileToLoadInActionTool) {
		this.dataFileToLoadInActionTool = dataFileToLoadInActionTool;
	}
	
}
