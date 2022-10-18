package com.epa.erb.forms;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.Step;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionPopupController;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class MainFormController implements Initializable{
	
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
	@FXML
	Pane lP;
	@FXML
	Pane tP;
	@FXML
	Pane rP;
	@FXML
	Pane bP;
	@FXML
	Separator separator;
	
	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;
	public MainFormController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
	}
		
	private Project project;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);
		HashMap<String, ArrayList<TextFlow>> formContentHashMap = xmlManager.parseMainFormContentXML(xmlContentFileToParse, this);
		addContent(formContentHashMap);
		hideEmptyControls();
		setColor();
	}
	
	private void setColor() {
		Constants constants = new Constants();
		if (engagementActionController != null && engagementActionController.getCurrentChapter() != null) {
			if (engagementActionController.getTreeView().getSelectionModel().getSelectedItem() != null) {
				if (engagementActionController.getCurrentChapter().getChapterNum() == 1) {
					tP.setStyle("-fx-background-color: " + constants.getChapter1Color());
				} else if (engagementActionController.getCurrentChapter().getChapterNum() == 2) {
					tP.setStyle("-fx-background-color: " + constants.getChapter2Color());
				} else if (engagementActionController.getCurrentChapter().getChapterNum() == 3) {
					tP.setStyle("-fx-background-color: " + constants.getChapter3Color());
				} else if (engagementActionController.getCurrentChapter().getChapterNum() == 4) {
					tP.setStyle("-fx-background-color: " + constants.getChapter4Color());
				} else if (engagementActionController.getCurrentChapter().getChapterNum() == 5) {
					tP.setStyle("-fx-background-color: " + constants.getChapter5Color());
				}
			}
		}
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
			Pane root = app.getErbContainerController().loadMainFormContentController(formContentXMLFile);
			if(app.getEngagementActionController() != null) {
				app.getEngagementActionController().cleanContentVBox();
				app.getEngagementActionController().removeStatusHBox();
				app.getEngagementActionController().getTreeView().getSelectionModel().select(null);
				app.getEngagementActionController().addContentToContentVBox(root, true);
			}
		} else {
			app.launchERBLandingNew2();
		}
	}
	
	private void internalResourceLinkClicked(String link) {
		File formContentXMLFile = app.getErbContainerController().getFormContentXML(link, true);
		Pane root = app.getErbContainerController().loadMainFormContentController(formContentXMLFile);
		if(app.getEngagementActionController() != null) {
			app.getEngagementActionController().cleanContentVBox();
			app.getEngagementActionController().removeStatusHBox();
			app.getEngagementActionController().getTreeView().getSelectionModel().select(null);
			app.getEngagementActionController().addContentToContentVBox(root, true);
		}
	}
	
	private void internalStepLinkClicked(String link) {
		if (engagementActionController != null) {
			TreeItem<String> currentSelectedTreeItem = engagementActionController.getTreeView().getSelectionModel().getSelectedItem();
			Step step = findStep(link);
			if(step != null) {
				for(TreeItem<String> treeItem: engagementActionController.getTreeItemIdTreeMap().keySet()) {
					if(engagementActionController.getTreeItemIdTreeMap().get(treeItem).contentEquals(step.getGUID())) {
						engagementActionController.getTreeView().getSelectionModel().select(treeItem);
						engagementActionController.treeViewClicked(currentSelectedTreeItem, treeItem);
					}
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
			TreeItem<String> currentSelectedTreeItem = engagementActionController.getTreeView().getSelectionModel()
					.getSelectedItem();
			if (link.endsWith("0")) {
				Chapter chapter = findChapter(link);
				if (chapter != null) {
					for (TreeItem<String> treeItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
						if (engagementActionController.getTreeItemIdTreeMap().get(treeItem).contentEquals(chapter.getGUID())) {
							engagementActionController.getTreeView().getSelectionModel().select(treeItem);
							engagementActionController.treeViewClicked(currentSelectedTreeItem, treeItem);
						}
					}
				}
			}
			Activity activity = findActivity(link);
			if (activity != null) {
				for (TreeItem<String> treeItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
					if (engagementActionController.getTreeItemIdTreeMap().get(treeItem)
							.contentEquals(activity.getGUID())) {
						engagementActionController.getTreeView().getSelectionModel().select(treeItem);
						engagementActionController.treeViewClicked(currentSelectedTreeItem, treeItem);
					}
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
	
	private Chapter findChapter(String chapterId) {
		for(Chapter chapter: engagementActionController.getListOfUniqueChapters()) {
			if((chapter.getChapterNum() + "0").contentEquals(chapterId)) {
				return chapter;
			}
		}
		return null;
	}
	
	private Step findStep(String stepId) {
		for (Chapter chapter : engagementActionController.getListOfUniqueChapters()) {
			for (Step chapterStep : chapter.getAssignedSteps()) {
				if (chapterStep.getStepID().contentEquals(stepId)) {
					return chapterStep;
				}
			}
			for (Activity activity : chapter.getAssignedActivities()) {
				for (Step activityStep : activity.getAssignedSteps()) {
					if (activityStep.getStepID().contentEquals(stepId)) {
						return activityStep;
					}
				}
			}
		}
		return null;
	}
	
	private Activity findActivity(String activityId) {
		for (Chapter chapter : engagementActionController.getListOfUniqueChapters()) {
			for (Activity activity : chapter.getAssignedActivities()) {
				if (activity.getActivityID().contentEquals(activityId)) {
					return activity;
				}
			}
		}
		return null;
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
			separator.setVisible(false);
		}
		if(topPanelVBox.getChildren().size() ==0) {
			if(rightPanelVBox.getChildren().contains(topPanelVBox)) rightPanelVBox.getChildren().remove(topPanelVBox);
			separator.setVisible(false);
		}
		if(bottomPanelVBox.getChildren().size() ==0) {
			if(rightPanelVBox.getChildren().contains(bottomPanelVBox)) rightPanelVBox.getChildren().remove(bottomPanelVBox);
			separator.setVisible(false);
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
