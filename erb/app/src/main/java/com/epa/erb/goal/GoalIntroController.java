package com.epa.erb.goal;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionController;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class GoalIntroController implements Initializable{

	@FXML
	WebView webView;
	@FXML
	HBox headingHBox;
	
	private App app;
	private Project project;
	private GoalContainerController goalContainerController;
	private ProjectSelectionController projectSelectionController;
	public GoalIntroController(App app, Project project, GoalContainerController goalContainerController, ProjectSelectionController projectSelectionController) {
		this.app = app;
		this.project = project;
		this.goalContainerController = goalContainerController;
		this.projectSelectionController = projectSelectionController;
	}
	
	private FileHandler fileHandler = new FileHandler();
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(GoalIntroController.class);
	private String pathToERBStaticDataFolder = constants.getPathToERBStaticDataFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	private void handleControls() {
		app.getErbContainerController().setTitleLabelText("ERB: Goal Intro");
	}
	
	@FXML
	public void openButtonAction() {
		openGoalActivity();
	}
	
	@FXML
	public void printButtonAction() {
		
	}
	
	private void openGoalActivity() {
		File file = new File(pathToERBStaticDataFolder + "\\Activities\\Supporting_DOC\\Ch1_Goals_Worksheet.docx");
		fileHandler.openFileOnDesktop(file);
	}
	
	private Parent loadGoalCreation() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalCreation.fxml"));
			GoalCreationController goalCreationController = new GoalCreationController(app, project, projectSelectionController);
			fxmlLoader.setController(goalCreationController);
			return fxmlLoader.load();
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	@FXML
	public void nextButtonAction() {
		Parent goalCreationRoot = loadGoalCreation();
		VBox.setVgrow(goalCreationRoot, Priority.ALWAYS);
		goalContainerController.getGoalContainerVBox().getChildren().clear();
		goalContainerController.getGoalContainerVBox().getChildren().add(goalCreationRoot);
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public GoalContainerController getGoalContainerController() {
		return goalContainerController;
	}

	public void setGoalContainerController(GoalContainerController goalContainerController) {
		this.goalContainerController = goalContainerController;
	}

	public ProjectSelectionController getProjectSelectionController() {
		return projectSelectionController;
	}

	public void setProjectSelectionController(ProjectSelectionController projectSelectionController) {
		this.projectSelectionController = projectSelectionController;
	}

	public WebView getWebView() {
		return webView;
	}

	public HBox getHeadingHBox() {
		return headingHBox;
	}

}
