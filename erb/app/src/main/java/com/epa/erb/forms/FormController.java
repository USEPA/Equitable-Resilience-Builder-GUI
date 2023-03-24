package com.epa.erb.forms;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.ERBItemFinder;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.IdAssignments;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FormController {

	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;

	public FormController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
	}

	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	private IdAssignments idAssignments = new IdAssignments();
	
	public void handleHyperlink(Text text, String linkType, String link, Project project) {
		if (linkType.contentEquals("internalIntro")) {
			internalPanelLinkClicked(link);
		} else if (linkType.contentEquals("internalResource")) {
			internalPanelLinkClicked(link);
		} else if (linkType.contentEquals("internalStep")) {
			internalPanelLinkClicked(link);
		} else if (linkType.contentEquals("internalActivity")) {
			internalPanelLinkClicked(link);
		} else if (linkType.contentEquals("externalDOC")) {
			externalDOCLinkClicked(link, project);
		} else if (linkType.contentEquals("URL")) {
			urlLinkClicked(link);
		}
	}

	public void internalPanelLinkClicked(String link) {
		if (idAssignments.getResourceIdAssignments().contains(link)) {
			internalPopupLinkClicked(link);
		} else if (idAssignments.getIntroIdAssignments().contains(link)) {
			internalPopupLinkClicked(link);
		} else if (idAssignments.getBackgroundIdAssignments().contains(link)) {
			internalPopupLinkClicked(link);
		} else {
			internalContentLinkClicked(link);
		}
	}

	public void internalPopupLinkClicked(String link) {
		File formContentXMLFile = app.getErbContainerController().getFormContentXML(link);
		Pane root = app.getErbContainerController().loadMainFormContentController(formContentXMLFile);
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setWidth(1150.0);
		stage.setHeight(750.0);
		stage.setScene(scene);
		stage.showAndWait();
	}

	protected ERBContentItem parentERBContentItem = null;

	public void internalContentLinkClicked(String link) {
		if (idAssignments.getChapterIdAssignments().contains(link)) {
			for (ERBContentItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
				if (erbItem.getId() != null && erbItem.getId().contentEquals(link)) {
					TreeItem<ERBContentItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap().get(erbItem);
					engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
					engagementActionController.treeViewClicked(erbTreeItem, erbTreeItem);
				}
			}
		}else {
			parentERBContentItem = null;
			TreeItem<ERBContentItem> currentSelectedTreeItem = engagementActionController.getTreeView()
					.getSelectionModel().getSelectedItem();
			// Get parent chapter for current selected item
			getSectionParent(currentSelectedTreeItem);
			if (parentERBContentItem != null) {
				ERBContentItem item = erbItemFinder
						.getERBContentItemById(parentERBContentItem.getChildERBContentItems(), link);
				engagementActionController.ERBContentItemSelected(item);
				for (ERBContentItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
					if (erbItem.getGuid() != null && erbItem.getGuid().contentEquals(item.getGuid())) {
						TreeItem<ERBContentItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap()
								.get(erbItem);
						engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
						engagementActionController.treeViewClicked(currentSelectedTreeItem, erbTreeItem);
					}
				}
			}
		}
	}

	protected void getSectionParent(TreeItem<ERBContentItem> currentSelectedTreeItem) {
		if (idAssignments.getChapterIdAssignments().contains(currentSelectedTreeItem.getValue().getId())) {
			parentERBContentItem = currentSelectedTreeItem.getValue();
		} else {
			TreeItem<ERBContentItem> parentTreeItem = currentSelectedTreeItem.getParent();
			if (idAssignments.getChapterIdAssignments().contains(parentTreeItem.getValue().getId())) {
				parentERBContentItem = parentTreeItem.getValue();
			} else {
				getSectionParent(parentTreeItem);
			}
		}
	}

	private void externalDOCLinkClicked(String link, Project project) {
		if (link != null && link.trim().length() > 0) {
			if (engagementActionController != null) {
				FileHandler fileHandler = new FileHandler();
				Project currentProject = app.getSelectedProject();
				Goal currentGoal = engagementActionController.getCurrentGoal();
				File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(currentProject, currentGoal);
				File fileToOpen = new File(supportingDOCDirectory + "\\" + link);
				fileHandler.openFileOnDesktop(fileToOpen);
			} else {
				System.out.println("ENAGEMENT ACTION CONTROLLER IS NULL. WHY");
			}
		}
	}

	private Stage projectPopupStage;

	public void closeProjectPopupStage() {
		if (projectPopupStage != null) {
			projectPopupStage.close();
		}
	}

	private void urlLinkClicked(String link) {
		try {
			Desktop.getDesktop().browse(new URL(link).toURI());
		} catch (Exception e) {
			e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void setColor(Pane tP) {
		Constants constants = new Constants();
		if (engagementActionController != null) {
			try {
				TreeItem<ERBContentItem> erbContentItemSelected = engagementActionController.getTreeView().getSelectionModel().getSelectedItem();
				getSectionParent(erbContentItemSelected);
				ERBContentItem parent = parentERBContentItem;
				if (parent.getId().contentEquals("15")) {
					tP.setStyle("-fx-background-color: " + constants.getChapter1Color());
				} else if (parent.getId().contentEquals("16")) {
					tP.setStyle("-fx-background-color: " + constants.getChapter2Color());
				} else if (parent.getId().contentEquals("17")) {
					tP.setStyle("-fx-background-color: " + constants.getChapter3Color());
				} else if (parent.getId().contentEquals("18")) {
					tP.setStyle("-fx-background-color: " + constants.getChapter4Color());
				} else if (parent.getId().contentEquals("19")) {
					tP.setStyle("-fx-background-color: " + constants.getChapter5Color());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}
}
