package com.epa.erb.form_activities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class FormContentController implements Initializable{
	
	@FXML
	VBox formVBox;
	@FXML
	VBox topPanelVBox;
	@FXML
	VBox bottomPanelVBox;
	
	private App app;
	private File xmlContentFileToParse;
	public FormContentController(App app, File xmlContentFileToParse) {
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
	}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);

		//File xmlContentFileToParse = fileHandler.getActivityFormContentXML(project, goal, activity);
		HashMap<String, ArrayList<TextFlow>> formContentHashMap = xmlManager.parseFormContentXML(xmlContentFileToParse);
		addContent(formContentHashMap);
	}
	
	public void addContent(HashMap<String, ArrayList<TextFlow>> formContentHashMap) {
		if(formContentHashMap != null) {
			ArrayList<TextFlow> formContentTextFlows = formContentHashMap.get("formVBox");
			addTextFlowsToVBox(formVBox, formContentTextFlows);
			ArrayList<TextFlow> topPanelTextFlows = formContentHashMap.get("topPanelVBox");
			addTextFlowsToVBox(topPanelVBox, topPanelTextFlows);
			ArrayList<TextFlow> bottomPanelTextFlows = formContentHashMap.get("bottomPanelVBox");
			addTextFlowsToVBox(bottomPanelVBox, bottomPanelTextFlows);
		}
	}
	
	public void addTextFlowsToVBox(VBox vBox, ArrayList<TextFlow> textFlows) {
		if (textFlows != null && vBox != null) {
			for (TextFlow textFlow : textFlows) {
				vBox.getChildren().add(textFlow);
			}
		}
	}

}
