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
		File indicatorsListFile = new File(fileHandler.getSupportingDOCDirectory(eAC.getProject(), eAC.getCurrentGoal()) + "\\Indicators_Master_List.xlsx");
		if(indicatorsListFile.exists()) {
			fileHandler.openFileOnDesktop(indicatorsListFile);
		}
	}
	
	@FXML
	public void indicatorsInPersonHyperlinkAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorSelection_InPerson.fxml"));
			IndicatorSelection_InPersonController iSIP = new IndicatorSelection_InPersonController(eAC.getApp());
			fxmlLoader.setController(iSIP);
			VBox root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Indicator Selection");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
