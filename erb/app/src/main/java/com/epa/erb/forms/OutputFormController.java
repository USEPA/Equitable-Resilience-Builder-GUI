package com.epa.erb.forms;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class OutputFormController implements Initializable {

	@FXML
	VBox nodeVBox;
	@FXML
	VBox formVBox;
	
	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;
	public OutputFormController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
	}
	
	FileHandler fileHandler = new FileHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);
		boolean hasExistingContent = checkForExisitingContent();
		if(!hasExistingContent) {
			HashMap<ArrayList<Text>, String> contentHashMap = xmlManager.parseOutputFormContentXML(xmlContentFileToParse);
			addContent(contentHashMap);
		} else {
			File dataXMLFile = fileHandler.getStepDataXMLFile(engagementActionController.getProject(), engagementActionController.getCurrentGoal(), engagementActionController.getCurrentSelectedStep());
			HashMap<ArrayList<Text>, String> contentHashMap = xmlManager.parseOutputFormContentXML(dataXMLFile);
			addContent(contentHashMap);
		}
		
	}
	
	private boolean checkForExisitingContent() {
		File dataXMLFile = fileHandler.getStepDataXMLFile(engagementActionController.getProject(), engagementActionController.getCurrentGoal(), engagementActionController.getCurrentSelectedStep());
		if(dataXMLFile != null && dataXMLFile.exists()) {
			return true;
		}
		return false;
	}
	
	@FXML
	public void saveButtonAction() {
		XMLManager xmlManager = new XMLManager(app);
		ArrayList<Node> listOfChildren = new ArrayList<Node>();
		for(int i =0; i < formVBox.getChildren().size(); i++) {
			listOfChildren.add(formVBox.getChildren().get(i));
		}
		File saveLocation = fileHandler.getStepDataXMLFile(engagementActionController.getProject(), engagementActionController.getCurrentGoal(), engagementActionController.getCurrentSelectedStep());
		xmlManager.writeOutputFormDataXML(saveLocation, listOfChildren);
	}
	
	private void addContent(HashMap<ArrayList<Text>, String> contentHashMap) {
		for(ArrayList<Text> textBlock: contentHashMap.keySet()) {
			String type = contentHashMap.get(textBlock);
			if(type.contentEquals("block")) {
				TextFlow textFlow = createTextFlow(textBlock);
				formVBox.getChildren().add(textFlow);
			} else if (type.contentEquals("area")) {
				TextArea textArea = createTextArea(textBlock);
				formVBox.getChildren().add(textArea);
			}
		}
	}
	
	private TextFlow createTextFlow(ArrayList<Text> textBlock) {
		TextFlow textFlow = new TextFlow();
		for(Text text: textBlock) {
			textFlow.getChildren().add(text);
		}
		textFlow.setId("block");
		return textFlow;
	}
	
	private TextArea createTextArea(ArrayList<Text> textBlock) {
		TextArea textArea = new TextArea();
		
		String textString = "";
		String textType = null;
		for(Text text: textBlock) {
			textString = textString + text.getText();
			textType = text.getId();
		}
		
		if(textType != null) {
			if(textType.contentEquals("Prompt")) {
				textArea.setPromptText(textString);
			} else if (textType.contentEquals("Regular")) {
				textArea.setText(textString);
			}
		}
		textArea.setId("area");
		return textArea;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public File getXmlContentFileToParse() {
		return xmlContentFileToParse;
	}

	public void setXmlContentFileToParse(File xmlContentFileToParse) {
		this.xmlContentFileToParse = xmlContentFileToParse;
	}


}
