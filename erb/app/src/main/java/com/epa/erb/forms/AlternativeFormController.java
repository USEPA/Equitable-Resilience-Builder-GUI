package com.epa.erb.forms;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.XMLManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
public class AlternativeFormController implements Initializable{
	
	@FXML
	VBox containerVBox;
	@FXML
	VBox titleVBox;
	@FXML
	VBox nodeVBox;
	@FXML
	HBox hBox;
	@FXML
	VBox leftVBox;
	@FXML
	VBox centerVBox;
	@FXML
	VBox rightVBox;
	@FXML
	Pane lP;
	@FXML
	Pane tP;
	@FXML
	Pane rP;
	@FXML
	Pane bP;
	
	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;
	public AlternativeFormController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);
		HashMap<String, ArrayList<TextFlow>> formContentHashMap = xmlManager.parseAlternativeFormContentXML(xmlContentFileToParse, this);
		addContent(formContentHashMap);
		hideEmptyControls();
		setColor();
	}
	
	private void setColor() {
		Constants constants = new Constants();
		if (engagementActionController != null && engagementActionController.getCurrentChapter() != null) {
			if (engagementActionController.getTreeView().getSelectionModel().getSelectedItem() != null) {
				if (engagementActionController.getCurrentChapter().getNumber() == 1) {
					tP.setStyle("-fx-background-color: " + constants.getChapter1Color());
				} else if (engagementActionController.getCurrentChapter().getNumber() == 2) {
					tP.setStyle("-fx-background-color: " + constants.getChapter2Color());
				} else if (engagementActionController.getCurrentChapter().getNumber() == 3) {
					tP.setStyle("-fx-background-color: " + constants.getChapter3Color());
				} else if (engagementActionController.getCurrentChapter().getNumber() == 4) {
					tP.setStyle("-fx-background-color: " + constants.getChapter4Color());
				} else if (engagementActionController.getCurrentChapter().getNumber() == 5) {
					tP.setStyle("-fx-background-color: " + constants.getChapter5Color());
				}
			}
		}
	}
	
	public void addContent(HashMap<String, ArrayList<TextFlow>> formContentHashMap) {
		if(formContentHashMap != null) {
			ArrayList<TextFlow> titleContentTextFlows = formContentHashMap.get("titleVBox");
			addTextFlowsToVBox(titleVBox, titleContentTextFlows);
			ArrayList<TextFlow> formContentTextFlows = formContentHashMap.get("leftVBox");
			addTextFlowsToVBox(leftVBox, formContentTextFlows);
			ArrayList<TextFlow> topPanelTextFlows = formContentHashMap.get("centerVBox");
			addTextFlowsToVBox(centerVBox, topPanelTextFlows);
			ArrayList<TextFlow> bottomPanelTextFlows = formContentHashMap.get("rightVBox");
			addTextFlowsToVBox(rightVBox, bottomPanelTextFlows);
		}
	}
	
	private void hideEmptyControls() {
		if(rightVBox.getChildren().size() == 0) {
			hBox.getChildren().remove(rightVBox);
		}
		if(centerVBox.getChildren().size() ==0) {
			hBox.getChildren().remove(centerVBox);
		}
		if(leftVBox.getChildren().size() ==0) {
			hBox.getChildren().remove(leftVBox);
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
