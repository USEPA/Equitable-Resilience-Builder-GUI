package com.epa.erb.form_activities;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionPopupController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class FormContentController implements Initializable{
	
	@FXML
	VBox nodeVBox;
	@FXML
	HBox hBox;
	@FXML
	VBox formVBox;
	@FXML
	VBox rightPanelVBox;
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
		
	private Project project;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);
		HashMap<String, ArrayList<TextFlow>> formContentHashMap = xmlManager.parseFormContentXML(xmlContentFileToParse, this);
		addContent(formContentHashMap);
		hideEmptyControls();
	}
	
	public void handleHyperlink(Text text, String linkType, String link) {
		if(linkType.contentEquals("internalIntro")) {
			internalIntroLinkClicked(link);
		} else if (linkType.contentEquals("internalResource")){
			internalResourceLinkClicked(link);
	    } else if (linkType.contentEquals("internalStep")) {
			internalStepLinkClicked(link);
		} else if (linkType.contentEquals("internalActivity")) {
			internalActivityLinkClicked(link);
		} else if (linkType.contentEquals("externalDOC")) {
			externalDOCLinkClicked(link);
		} else if (linkType.contentEquals("URL")) {
			urlLinkClicked(link);
		}
	}
	
	private void internalIntroLinkClicked(String link) {
		if (!link.contentEquals("00")) {
			File formContentXMLFile = app.getErbContainerController().getFormContentXML(link, false);
			Parent root = app.getErbContainerController().loadFormContentController(formContentXMLFile);
			app.loadNodeToERBContainer(root);
		} else {
			app.launchERBLandingNew2();
		}
	}
	
	private void internalResourceLinkClicked(String link) {
		File formContentXMLFile = app.getErbContainerController().getFormContentXML(link, true);
		Parent root = app.getErbContainerController().loadFormContentController(formContentXMLFile);
		app.loadNodeToERBContainer(root);
	}
	
	private void internalStepLinkClicked(String link) {
		if (engagementActionController != null) {
			TreeItem<String> currentSelectedTreeItem = engagementActionController.getTreeView().getSelectionModel().getSelectedItem();
			for (TreeItem<String> stepTreeItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
				String stepTreeItemId = engagementActionController.getTreeItemIdTreeMap().get(stepTreeItem);
				if (stepTreeItemId.contentEquals(link)) {
					engagementActionController.getTreeView().getSelectionModel().select(stepTreeItem);
					engagementActionController.treeViewClicked(currentSelectedTreeItem, stepTreeItem);
				}
			}
		} else {
			loadProjectSelectionPopup();
			if (project != null) {
				app.setSelectedProject(project);
				engagementActionController = new EngagementActionController(app, project);
				loadEngagementActionToContainer(engagementActionController);
				engagementActionController.setCurrentSelectedGoal(project.getProjectGoals().get(0));
				internalStepLinkClicked(link);
			}
		}
	}
	
	private void internalActivityLinkClicked(String link) {
		if (engagementActionController != null) {
			TreeItem<String> currentSelectedTreeItem = engagementActionController.getTreeView().getSelectionModel().getSelectedItem();
			for (TreeItem<String> activityTreeItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
				String activityTreeItemId = engagementActionController.getTreeItemIdTreeMap().get(activityTreeItem);
				if (activityTreeItemId.contentEquals(link)) {
					engagementActionController.getTreeView().getSelectionModel().select(activityTreeItem);
					engagementActionController.treeViewClicked(currentSelectedTreeItem, activityTreeItem);
				}
			}
		} else {
			loadProjectSelectionPopup();
			if (project != null) {
				app.setSelectedProject(project);
				engagementActionController = new EngagementActionController(app, project);
				loadEngagementActionToContainer(engagementActionController);
				engagementActionController.setCurrentSelectedGoal(project.getProjectGoals().get(0));
				internalActivityLinkClicked(link);
			}
		}
	}
	
	public void loadEngagementActionToContainer(EngagementActionController engagementActionController) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			fxmlLoader.setController(engagementActionController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			app.loadNodeToERBContainer(root);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void externalDOCLinkClicked(String link) {
		if (engagementActionController != null) {
			FileHandler fileHandler = new FileHandler();
			Project currentProject = app.getSelectedProject();
			Goal currentGoal = engagementActionController.getCurrentGoal();
			File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(currentProject, currentGoal);
			File fileToOpen = new File(supportingDOCDirectory + "\\" + link);
			fileHandler.openFileOnDesktop(fileToOpen);
		} else {
			loadProjectSelectionPopup();
			if (project != null) {
				app.setSelectedProject(project);
				engagementActionController = new EngagementActionController(app, project);
				engagementActionController.setCurrentSelectedGoal(project.getProjectGoals().get(0));
				externalDOCLinkClicked(link);
			}
		}
	}
	
	public void closeProjectPopupStage() {
		if(projectPopupStage != null) {
			projectPopupStage.close();
		}
	}
	
	private Stage projectPopupStage;
	private void loadProjectSelectionPopup() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectSelectionPopup.fxml"));
			ProjectSelectionPopupController projectSelectionPopupController = new ProjectSelectionPopupController(app,this);
			fxmlLoader.setController(projectSelectionPopupController);
			VBox root = fxmlLoader.load();
			Scene scene = new Scene(root);
			projectPopupStage = new Stage();
			projectPopupStage.setScene(scene);
			projectPopupStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void urlLinkClicked(String link) {
		try {
			Desktop.getDesktop().browse(new URL(link).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	private void hideEmptyControls() {
		if(topPanelVBox.getChildren().size() == 0 && bottomPanelVBox.getChildren().size() == 0) {
			hBox.getChildren().remove(rightPanelVBox);
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
