package com.epa.erb.indicators;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.utility.FileHandler;


public class IndicatorCenterController implements Initializable{

	private FileHandler fileHandler = new FileHandler();
	
	private EngagementActionController eAC;
	public IndicatorCenterController(EngagementActionController eAC) {
		this.eAC = eAC;
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
			inPersonIndicatorSelectionStage.setWidth(eAC.getApp().getPopUpPrefWidth());
			inPersonIndicatorSelectionStage.setHeight(eAC.getApp().getPopUpPrefHeight());
			inPersonIndicatorSelectionStage.setTitle("Indicator Selection");
			Scene scene = new Scene(root);
			inPersonIndicatorSelectionStage.setScene(scene);
			inPersonIndicatorSelectionStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			virtualIndicatorSelectionStage.setWidth(eAC.getApp().getPopUpPrefWidth());
			virtualIndicatorSelectionStage.setHeight(eAC.getApp().getPopUpPrefHeight());
			virtualIndicatorSelectionStage.setTitle("Indicator Selection");
			Scene scene = new Scene(root);
			virtualIndicatorSelectionStage.setScene(scene);
			virtualIndicatorSelectionStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Stage getInPersonIndicatorSelectionStage() {
		return inPersonIndicatorSelectionStage;
	}

	public Stage getVirtualIndicatorSelectionStage() {
		return virtualIndicatorSelectionStage;
	}

}
