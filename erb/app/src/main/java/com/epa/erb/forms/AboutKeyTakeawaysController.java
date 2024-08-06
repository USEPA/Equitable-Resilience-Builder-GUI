package com.epa.erb.forms;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.epa.erb.App;
import com.epa.erb.engagement_action.MyPortfolioController;
import com.epa.erb.finalReport.FinalReportSelectionController;
import com.epa.erb.project.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AboutKeyTakeawaysController implements Initializable{
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	private App app;
	private Project project;
	private Logger logger;
	public AboutKeyTakeawaysController(App app, Project project, Logger logger) {
		this.app = app;
		this.project = project;
		this.logger = logger;
	}
	
	@FXML
	public void myPortfolioHyperlinkAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/MyPortfolio.fxml"));
			MyPortfolioController controller = new MyPortfolioController(app, project, app.getEngagementActionController().getCurrentGoal());
			fxmlLoader.setController(controller);
			VBox root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("My Portfolio");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load MyPortfolio.fxml: " + e.getMessage());
		}
	}
	
	@FXML
	public void reportWidgetButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/finalReport/FinalReportSelection.fxml"));
			FinalReportSelectionController controller = new FinalReportSelectionController(app);
			fxmlLoader.setController(controller);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Summary Report Generator");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load FinalReportSelection.fxml: " + e.getMessage());
		}
	}

}
