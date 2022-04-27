package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.engagement_setup.EngagementSetupController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ERBMainController implements Initializable{

	@FXML
	HBox erbHeading1;
	@FXML
	HBox erbHeading2;
	@FXML
	Button setupLaunchButton;
	@FXML
	Button actionLaunchButton;
	@FXML
	TextFlow setupTextFlow;
	@FXML
	TextFlow actionTextFlow;
	
	public ERBMainController() {
		
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBMainController.class);
	
	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		fillSetupTextFlow();
		fillActionTextFlow();
	}
	
	private void handleControls() {
		erbHeading1.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		erbHeading2.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	private void fillSetupTextFlow() {
		String setupString = "The setup portion of the tool can be used to do the following: " + "\n" +
							 "\t" + "-Create chapters, add activities to chapters, and assign chapter descriptions." + "\n" + 
							 "\t" + "-View and add activity types to created chapters." + "\n" + 
							 "\t" + "-View and assign available activities to corresponding activity types in created chapters.";
		Text setupText = new Text(setupString);
		setupText.setFont(new Font(15.0));
		setupTextFlow.getChildren().add(setupText);
	}
	
	private void fillActionTextFlow() {
		String actionString = "The action portion of the tool can be used to do the following: " + "\n" +
							"\t" + "-View all activites created for the ERB." + "\n" + 
							"\t" + "-Engage in selected actitivites." + "\n" + 
							"\t" + "-Track progress inside of the ERB.";
		Text actionText = new Text(actionString);
		actionText.setFont(new Font(15.0));
		actionTextFlow.getChildren().add(actionText);
	}
	
	private Stage setupStage = null;
	@FXML
	public void setupLaunchButtonAction() {
	 	ArrayList<File> listOfProjects = getProjectDirectoriesInSetup();
	 	showProjectSelection(listOfProjects, false, true, false);
	}
	
	//ERB Tool Pt 1
	void loadSetupTool(File projectDirectory) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_setup/EngagementSetup.fxml"));
			EngagementSetupController engagementSetupController = new EngagementSetupController(projectDirectory);
			fxmlLoader.setController(engagementSetupController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			setupStage = new Stage();
			setupStage.setScene(scene);
			setupStage.setTitle("ERB: Equitable Resilience Builder");
			setupStage.show();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}
	
	private Stage actionStage = null;
	@FXML
	public void actionLaunchButtonAction() {
		ArrayList<File> listOfProjects = getProjectDirectoriesInAction();
		if(listOfProjects.size() > 0) {
	 		showProjectSelection(listOfProjects, true, false, true);
	 	}
	}
	
	//ERB Tool Pt 2
	void loadActionTool(File projectDirectory, File dataFile) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			EngagementActionController engagementActionController = new EngagementActionController(projectDirectory, dataFile);
			fxmlLoader.setController(engagementActionController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			actionStage = new Stage();
			actionStage.setScene(scene);
			actionStage.setTitle("ERB: Equitable Resilience Builder");
			actionStage.show();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}
	
	private Stage projectSelectionStage = null;
	private void showProjectSelection(ArrayList<File> listOfProjects, boolean removeNewProjectOption, boolean setup, boolean action) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ProjectSelection.fxml"));
			ProjectSelectionController projectSelectionController = new ProjectSelectionController(listOfProjects, this, setup, action);
			fxmlLoader.setController(projectSelectionController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			projectSelectionStage = new Stage();
			projectSelectionStage.setScene(scene);
			projectSelectionStage.setTitle("Project Selection");
			projectSelectionStage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private ArrayList<File> getProjectDirectoriesInSetup() {
		ArrayList<File> projectDirectories = new ArrayList<File>();
		File setupDirectory = new File(pathToERBFolder + "\\EngagementSetupTool");
		if(setupDirectory.exists()) {
			for(File file : setupDirectory.listFiles()) {
				if(file.isDirectory()) {
					projectDirectories.add(file);
				}
			}
		} else {
			logger.debug(setupDirectory.getPath() + " does not exist. Returning empty list of projects in setup.");
		}
		return projectDirectories;
	}
	
	private ArrayList<File> getProjectDirectoriesInAction(){
		ArrayList<File> projectDirectories = new ArrayList<File>();
		File actionDirectory = new File(pathToERBFolder + "\\EngagementActionTool");
		if(actionDirectory.exists()) {
			for(File file : actionDirectory.listFiles()) {
				if(file.isDirectory()) {
					projectDirectories.add(file);
				}
			}
		} else {
			logger.debug(actionDirectory.getPath() + " does not exist. Returning empty list of projects in action.");
		}
		return projectDirectories;
	}
	
	public void closeProjectSelectionStage() {
		if(projectSelectionStage != null) {
			projectSelectionStage.close();
		}
	}
	
	public void closeSetupStage() {
		if(setupStage != null) {
			setupStage.close();
		}
	}
	
	public void closeActionStage() {
		if(actionStage != null) {
			actionStage.close();
		}
	}
}
