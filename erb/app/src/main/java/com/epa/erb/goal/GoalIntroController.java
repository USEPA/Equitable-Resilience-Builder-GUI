package com.epa.erb.goal;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionController;
import com.epa.erb.utility.Constants;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
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
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(GoalIntroController.class);
	private String pathToERBStaticDataFolder = constants.getPathToERBStaticDataFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadGoalsWorksheetPDFToWebView(getPDFFileToLoad());
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
		try {
			File file = new File(pathToERBStaticDataFolder + "\\Activities\\ChapterActivities_DOC\\1_Goals.docx");
			if (file.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				logger.error(file.getPath() + " either does not exist or desktop is not supported.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private File getPDFFileToLoad() {
		File pdfFileToLoad = new File(pathToERBStaticDataFolder + "\\Activities\\ChapterActivities_PDF\\1_Goals.pdf");
		return pdfFileToLoad;
	}
	
	private void loadGoalsWorksheetPDFToWebView(File pdfFileToLoad) {
		if (pdfFileToLoad != null) {
			try {
				WebEngine webEngine = webView.getEngine();
				String url = getClass().getResource("/pdfjs-2.8.335-legacy-dist/web/viewer.html").toExternalForm();
				webEngine.setUserStyleSheetLocation(getClass().getResource("/web.css").toExternalForm());
				webEngine.setJavaScriptEnabled(true);
				webEngine.load(url);

				webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue ov, State oldState, State newState) {
						if (newState == Worker.State.SUCCEEDED) {
							try {
								byte[] data = FileUtils.readFileToByteArray(pdfFileToLoad);
								String base64 = Base64.getEncoder().encodeToString(data);
								webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
							} catch (Exception ex) {
								logger.error(ex.getMessage());
							}
						}
					}
				});
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot loadGoalsWorksheetPDFToWebView. pdfFileToLoad is null.");
		}
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
