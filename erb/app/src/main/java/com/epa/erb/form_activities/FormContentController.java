package com.epa.erb.form_activities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import com.epa.erb.intro_panels.IntroPanelLoader;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FormContentController implements Initializable{
	
	@FXML
	VBox nodeVBox;
	@FXML
	VBox formVBox;
	@FXML
	VBox topPanelVBox;
	@FXML
	VBox bottomPanelVBox;
	
	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;
	public FormContentController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
	}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);

		//File xmlContentFileToParse = fileHandler.getActivityFormContentXML(project, goal, activity);
		HashMap<String, ArrayList<TextFlow>> formContentHashMap = xmlManager.parseFormContentXML(xmlContentFileToParse, this);
		addContent(formContentHashMap);
	}
	
	public void handleHyperlink(Text text, String linkType, String link) {
		if(linkType.contentEquals("internalPanel")) {
			internalPanelLinkClicked(link);
		} else if (linkType.contentEquals("externalXML")) {
			externalXMLLinkClicked(link);
		} else if (linkType.contentEquals("externalDOC")) {
			externalDOCLinkClicked(link);
		}
	}
	
	private void backButtonClickedInternalPanel() {
		Node previousNode = engagementActionController.getEngagementVBox();
		app.loadNodeToERBContainer(previousNode);
		app.getErbContainerController().removeBackButton();
	}
	
	private void internalPanelLinkClicked(String link) {
		app.getErbContainerController().addBackButton();
		app.getErbContainerController().getBackButton().setOnMouseClicked(e-> backButtonClickedInternalPanel());
		IntroPanelLoader introPanelLoader = new IntroPanelLoader(app);
		if(link.contentEquals("equityAndResilience")) {
			introPanelLoader.loadEquityAndResiliencePanel();
		}
		
	}
	
	private void backButtonClickedExternalXML(ArrayList<Node> previousFormNodes, ArrayList<Node> previousTopNodes, ArrayList<Node> previousBottomNodes) {
		clearContent();
		formVBox.getChildren().addAll(previousFormNodes);
		topPanelVBox.getChildren().addAll(previousTopNodes);
		bottomPanelVBox.getChildren().addAll(previousBottomNodes);
		app.getErbContainerController().removeBackButton();
	}
	
	private void externalXMLLinkClicked(String link) {
		app.getErbContainerController().addBackButton();
		FileHandler fileHandler = new FileHandler();
		XMLManager xmlManager = new XMLManager(app);
		File xmlFile = fileHandler.getStaticSupportingXML(link);
		ArrayList<Node> pFN = new ArrayList<Node>(formVBox.getChildren());
		ArrayList<Node> pTN = new ArrayList<Node>(topPanelVBox.getChildren());
		ArrayList<Node> pBN = new ArrayList<Node>(bottomPanelVBox.getChildren());
		app.getErbContainerController().getBackButton().setOnMouseClicked(e-> backButtonClickedExternalXML(pFN, pTN, pBN));
		HashMap<String, ArrayList<TextFlow>> formContentHashMap = xmlManager.parseFormContentXML(xmlFile, this);
		clearContent();
		addContent(formContentHashMap);
	}
	
	private void externalDOCLinkClicked(String link) {
		FileHandler fileHandler = new FileHandler();
		Project currentProject = app.getSelectedProject();
		Goal currentGoal = engagementActionController.getCurrentGoal();
		File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(currentProject, currentGoal);
		File fileToOpen = new File(supportingDOCDirectory + "\\" + link);
		fileHandler.openFile(fileToOpen);
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
	
	public void clearContent() {
		clearFormVBox();
		clearTopVBox();
		clearBottomVBox();
	}
	
	private void clearFormVBox() {
		formVBox.getChildren().clear();
	}
	
	private void clearTopVBox() {
		topPanelVBox.getChildren().clear();
	}
	
	private void clearBottomVBox() {
		bottomPanelVBox.getChildren().clear();
	}
	
	public void addTextFlowsToVBox(VBox vBox, ArrayList<TextFlow> textFlows) {
		if (textFlows != null && vBox != null) {
			for (TextFlow textFlow : textFlows) {
				vBox.getChildren().add(textFlow);
			}
		}
	}

}
