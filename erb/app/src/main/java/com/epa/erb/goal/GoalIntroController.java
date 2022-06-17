package com.epa.erb.goal;

import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.project.Project;
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
	public GoalIntroController(App app, Project project, GoalContainerController goalContainerController) {
		this.app = app;
		this.project = project;
		this.goalContainerController = goalContainerController;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(GoalIntroController.class);
	private String pathToERBStaticDataFolder = constants.getPathToLocalERBStaticDataFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadGoalsWorksheetPDFToWebView(getPDFFileToLoad());
	}
	
	private void handleControls() {
	}
	
	private File getPDFFileToLoad() {
		File pdfFileToLoad = new File(pathToERBStaticDataFolder + "\\Activities\\ChapterActivities_PDF\\1_Goals.pdf");
		return pdfFileToLoad;
	}
	
	private void loadGoalsWorksheetPDFToWebView(File pdfFileToLoad) {
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
	}
	
	private void loadGoalCreation() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalCreation.fxml"));
			GoalCreationController goalCreationController = new GoalCreationController(app, project);
			fxmlLoader.setController(goalCreationController);
			Parent root = fxmlLoader.load();
			VBox.setVgrow(root, Priority.ALWAYS);
			goalContainerController.getGoalContainerVBox().getChildren().clear();
			goalContainerController.getGoalContainerVBox().getChildren().add(root);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void nextButtonAction() {
		loadGoalCreation();
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

	public WebView getWebView() {
		return webView;
	}

	public HBox getHeadingHBox() {
		return headingHBox;
	}

}
