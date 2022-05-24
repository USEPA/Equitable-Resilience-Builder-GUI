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
import com.epa.erb.project.ProjectSelectionController;
import com.epa.erb.worksheet.JavaBridge;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class GoalIntroController implements Initializable{

	@FXML
	WebView webView;
	
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
	
	private Logger logger = LogManager.getLogger(GoalIntroController.class);
	private Constants constants = new Constants();
	private String pathToERBStaticDataFolder = constants.getPathToLocalERBStaticDataFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadGoalsWorksheetPDF(getPDFFileToLoad());
	}
	
	private File getPDFFileToLoad() {
		File pdfFileToLoad = new File(pathToERBStaticDataFolder + "\\Activities\\ChapterActivities_PDF\\1_Goals.pdf");
		return pdfFileToLoad;
	}
	
	private void loadGoalsWorksheetPDF(File pdfFileToLoad) {
		JavaBridge javaBridge = new JavaBridge();
		try {
			WebEngine webEngine = webView.getEngine();
			String url = getClass().getResource("/pdfjs-2.14.305-legacy-dist/web/viewer.html").toExternalForm();
			webEngine.setUserStyleSheetLocation(getClass().getResource("/pdfjs-2.14.305-legacy-dist/web.css").toExternalForm());
			webEngine.setJavaScriptEnabled(true);
			webEngine.load(url);
			
			webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
				@Override
				public void changed(ObservableValue ov, State oldState, State newState) {
					if (newState == Worker.State.SUCCEEDED) {
						try {
							byte[] data = FileUtils.readFileToByteArray(pdfFileToLoad);
							String base64 = Base64.getEncoder().encodeToString(data);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									JSObject window = (JSObject) webEngine.executeScript("window");
								    window.setMember("java", javaBridge);
								    webEngine.executeScript("console.log = function(message)\n" +"{\n" + "    java.log(message);\n" + "};"); 
								    webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
								}
							});
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void nextButtonAction() {
		loadGoalCreation();
	}
	
	private void loadGoalCreation() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GoalCreation.fxml"));
			GoalCreationController goalCreationController = new GoalCreationController(app, project, projectSelectionController);
			fxmlLoader.setController(goalCreationController);
			Parent root = fxmlLoader.load();
			VBox.setVgrow(root, Priority.ALWAYS);
			goalContainerController.getGoalContainerVBox().getChildren().clear();
			goalContainerController.getGoalContainerVBox().getChildren().add(root);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
