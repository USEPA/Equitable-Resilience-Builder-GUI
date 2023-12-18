package com.epa.erb.indicators;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import com.epa.erb.App;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.forms.FormController;
import com.epa.erb.utility.FileHandler;

public class IndicatorCenterController implements Initializable{

	private Logger logger;
	private FileHandler fileHandler;
	
	private App app;
	private EngagementActionController eAC;
	public IndicatorCenterController(App app, EngagementActionController eAC) {
		this.app = app;
		this.eAC = eAC;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void indicatorsListHyperlinkAction() {
		File indicatorsListFile = new File(fileHandler.getSupportingDOCDirectory(eAC.getProject(), eAC.getCurrentGoal()) + "\\Indicators_List.xlsx");
		if(indicatorsListFile.exists()) {
			fileHandler.openFileOnDesktop(indicatorsListFile);
		}
	}
	
	private Stage inPersonIndicatorSelectionStage = null;
	@FXML
	public void indicatorsInPersonHyperlinkAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorSelection_InPerson.fxml"));
			IndicatorSelection_InPersonController iSIP = new IndicatorSelection_InPersonController(eAC.getApp(), this);
			fxmlLoader.setController(iSIP);
			VBox root = fxmlLoader.load();
			inPersonIndicatorSelectionStage = new Stage();
			inPersonIndicatorSelectionStage.getIcons().add(new Image("/bridge_tool_logo.png"));
			inPersonIndicatorSelectionStage.setWidth(eAC.getApp().getPopUpPrefWidth());
			inPersonIndicatorSelectionStage.setHeight(eAC.getApp().getPopUpPrefHeight());
			inPersonIndicatorSelectionStage.setTitle("Indicator Selection");
			Scene scene = new Scene(root);
			inPersonIndicatorSelectionStage.setScene(scene);
			inPersonIndicatorSelectionStage.show();
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load IndicatorSelection_InPerson.fxml.");
			logger.log(Level.FINER, "Failed to load IndicatorSelection_InPerson.fxml: " + e.getStackTrace());
			}
	}
	
	@FXML
	public void resilienceIndicatorsBackgroundHyperlinkAction() {
		FormController controller = new FormController(eAC.getApp(), eAC);
		controller.internalPopupLinkClicked("06");
	}
	
	private Stage virtualIndicatorSelectionStage = null;
	@FXML
	public void indicatorsVirtualHyperlinkAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorSelection_Virtual.fxml"));
			IndicatorSelection_VirtualController iSV = new IndicatorSelection_VirtualController(eAC.getApp(), this);
			fxmlLoader.setController(iSV);
			VBox root = fxmlLoader.load();
			virtualIndicatorSelectionStage = new Stage();
			virtualIndicatorSelectionStage.getIcons().add(new Image("/bridge_tool_logo.png"));
			virtualIndicatorSelectionStage.setWidth(eAC.getApp().getPopUpPrefWidth());
			virtualIndicatorSelectionStage.setHeight(eAC.getApp().getPopUpPrefHeight());
			virtualIndicatorSelectionStage.setTitle("Indicator Selection");
			Scene scene = new Scene(root);
			virtualIndicatorSelectionStage.setScene(scene);
			virtualIndicatorSelectionStage.show();
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load IndicatorSelection_Virtual.fxml.");
			logger.log(Level.FINER, "Failed to load IndicatorSelection_Virtual.fxml: " + e.getStackTrace());		}
	}

	public Stage getInPersonIndicatorSelectionStage() {
		return inPersonIndicatorSelectionStage;
	}

	public Stage getVirtualIndicatorSelectionStage() {
		return virtualIndicatorSelectionStage;
	}

}
