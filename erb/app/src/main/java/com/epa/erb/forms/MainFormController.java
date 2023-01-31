package com.epa.erb.forms;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.ContentPanel;
import com.epa.erb.ERBItem;
import com.epa.erb.ERBItemFinder;
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
	private FileHandler fileHandler = new FileHandler();
	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	
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
	
	public void handleHyperlink(Text text, String linkType, String link) {		
//		if(linkType.contentEquals("internalIntro")) {
//			internalIntroLinkClicked(link);
//		} else if (linkType.contentEquals("internalResource")){
//			internalResourceLinkClicked(link);
//	    } else if (linkType.contentEquals("internalStep")) {
//			internalStepLinkClicked(link);
//		} else if (linkType.contentEquals("internalActivity")) {
//			internalActivityLinkClicked(link);
//		} else if (linkType.contentEquals("externalDOC")) {
//			externalDOCLinkClicked(link);
//		} else if (linkType.contentEquals("URL")) {
//			urlLinkClicked(link);
//		}
	}
	
	public void showFormTextXML(String link) {
		if (!link.contentEquals("00")) {
			File formContentXMLFile = fileHandler.getStaticSupportingFormTextXML(link);
			Pane root = app.getErbContainerController().loadMainFormContentController(formContentXMLFile);
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setWidth(1150.0);
			stage.setHeight(750.0);
			stage.setScene(scene);
			stage.showAndWait();
		} else {
			app.launchERBLanding();
		}
	}
	
	private void internalStepLinkClicked(String link) {
//		if (engagementActionController != null) {
//			TreeItem<ERBItem> currentSelectedTreeItem = engagementActionController.getTreeView().getSelectionModel().getSelectedItem();
//			Step step = erbItemFinder.findStepById(engagementActionController.getListOfUniqueChapters(), link);
//			if (step != null) {
//				if (step.getId().length() == 4) {
//					Pane root = engagementActionController.loadStepContent(step);
//					Stage stage = new Stage();
//					Scene scene = new Scene(root);
//					stage.setWidth(1150.0);
//					stage.setHeight(750.0);
//					stage.setScene(scene);
//					stage.setTitle(step.getLongName());
//					stage.showAndWait();
//				} else {
//					ERBItem currentErbItem = engagementActionController.findTreeItem(step.getGuid()).getValue();
////					System.out.println("HYPERLINK FROM " + currentErbItem.getLongName());
////					System.out.println("Hyperlink made us jump 1");
//					for (ERBItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
//						if (erbItem.getGuid() != null && erbItem.getGuid().contentEquals(step.getGuid())) {
//							TreeItem<ERBItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap().get(erbItem);
//							engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
//							engagementActionController.treeViewClicked(currentSelectedTreeItem, erbTreeItem);
//						}
//					}
//				}
//			}
//		} else {
//			loadProjectSelectionPopup();
//			if (project != null) {
//				app.setSelectedProject(project);
//				engagementActionController = new EngagementActionController(app, project);
//				loadEngagementActionToContainer(engagementActionController);
//				engagementActionController.setCurrentSelectedGoal(project.getProjectGoals().get(0));
//				internalStepLinkClicked(link);
//			}
//		}
	}
	
	private void internalActivityLinkClicked(String link) {
//		if (engagementActionController != null) {
//			TreeItem<ERBItem> currentSelectedTreeItem = engagementActionController.getTreeView().getSelectionModel().getSelectedItem();
//			if (link.length() == 5) {
//				Chapter chapter = erbItemFinder.findChapterForGuid( engagementActionController.getListOfUniqueChapters(), link);
//				if (chapter != null) {
//					ERBItem currentErbItem = engagementActionController.findTreeItem(chapter.getGuid()).getValue();
////					System.out.println("HYPERLINK FROM " + currentErbItem.getLongName());
////					System.out.println("Hyperlink made us jump 2");
//					for (ERBItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
//						if (erbItem.getGuid()!= null && erbItem.getGuid().contentEquals(chapter.getGuid())) {
//							TreeItem<ERBItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap().get(erbItem);
//							engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
//							engagementActionController.treeViewClicked(currentSelectedTreeItem, erbTreeItem);
//						}
//					}
//				}
//			}
//			ContentPanel activity = erbItemFinder.findContentPanelById(engagementActionController.getListOfUniqueChapters(),link);
//			if (activity != null) {
//				ERBItem currentErbItem = engagementActionController.findTreeItem(activity.getGuid()).getValue();
////				System.out.println("HYPERLINK FROM " + currentErbItem.getLongName());
////				System.out.println("Hyperlink made us jump 3");
//				for (ERBItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
//					if (erbItem.getGuid().contentEquals(activity.getGuid())) {
//						TreeItem<ERBItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap().get(erbItem);
//						engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
//						engagementActionController.treeViewClicked(currentSelectedTreeItem, erbTreeItem);
//					}
//				}
//			}
//		} else {
//			loadProjectSelectionPopup();
//			if (project != null) {
//				app.setSelectedProject(project);
//				engagementActionController = new EngagementActionController(app, project);
//				loadEngagementActionToContainer(engagementActionController);
//				engagementActionController.setCurrentSelectedGoal(project.getProjectGoals().get(0));
//				internalActivityLinkClicked(link);
//			}
//		}
	}
	
	private void hyperlinkJump(ERBItem erbItem) {
		app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(erbItem.getShortName(), erbItem.getId());
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
