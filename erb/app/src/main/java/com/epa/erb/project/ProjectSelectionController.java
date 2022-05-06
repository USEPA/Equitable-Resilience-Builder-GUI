package com.epa.erb.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epa.erb.App;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class ProjectSelectionController implements Initializable{

	@FXML
	VBox welcomeVBox;
	@FXML
	TextField projectNameTextField;
	@FXML
	ListView<String> projectsListView;
	
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
//		handleControls();
//		fillProjectsListView();
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	@FXML
	public void createButtonAction() {
	
	}
	
	
	@FXML
	public void launchButtonAction() {
		
	}
	
	private void fillProjectsListView() {
		if(projectsExist(new File(pathToERBProjectsFolder))) {
			ArrayList<String> existingProjectNames = getExistingProjectNames(new File(pathToERBProjectsFolder));
			for(String projectName : existingProjectNames) {
				projectsListView.getItems().add(projectName);
			}
		}
	}
	
	private boolean projectsExist(File erbProjectDirectory) {
		if(erbProjectDirectory.exists()) {
			File [] projectDirectories = erbProjectDirectory.listFiles();
			if(projectDirectories != null && projectDirectories.length > 0) {
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<String> getExistingProjectNames(File erbProjectDirectory){
		ArrayList<String> projectNames = new ArrayList<String>();
		if(erbProjectDirectory.exists()) {
			File [] projectDirectories = erbProjectDirectory.listFiles();
			if(projectDirectories != null && projectDirectories.length > 0) {
				for(File projectDirectory : projectDirectories) {
					if(projectDirectory.isDirectory()) {
						projectNames.add(projectDirectory.getName());
					}
				}
			}
		}
		return projectNames;
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
	
	private void showProjectDataNonExistentAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText("There is no data for this project. Please use Pt.1 of the tool to create data.");
		alert.setTitle("No Data Alert");
		alert.showAndWait();
	}

	private void removeExistingProjectData(File projectDirectory) {
		File setupDataFile = new File(projectDirectory.getPath() + "\\Data.xml");
		if(setupDataFile.exists()) {
			setupDataFile.delete();
		}
	}
	

	private File createNewProjectSetupDirectory(String newProjectName) {
		// Setup directory
		File newProjectDirectory = new File(pathToERBProjectsFolder + "\\EngagementSetupTool\\" + newProjectName + "\\");
		if (!newProjectDirectory.exists()) {
			newProjectDirectory.mkdir();
		}
		return newProjectDirectory;
	}
	
	private File createNewProjectActionDirectory(String newProjectName) {
		// Action directory
		File newProjectDirectory = new File(pathToERBProjectsFolder + "\\EngagementActionTool\\" + newProjectName + "\\");
		if (!newProjectDirectory.exists()) {
			newProjectDirectory.mkdir();
		}
		return newProjectDirectory;
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
	
}
