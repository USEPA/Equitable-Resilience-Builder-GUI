package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.MappingController;
import com.epa.erb.finalReport.FinalReportSelectionController;
import com.epa.erb.forms.AlternativeFormController;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.forms.OutputFormController;
import com.epa.erb.goal.Goal;
import com.epa.erb.indicators.IndicatorCenterController;
import com.epa.erb.noteboard.IndicatorSetupFormController;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.MainPanelHandler;
import com.epa.erb.utility.XMLManager;
import com.epa.erb.wordcloud.WordCloudController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EngagementActionController implements Initializable {

	private Logger logger;
	private FileHandler fileHandler;
	private MainPanelHandler mainPanelHandler;
	private Goal currentSelectedGoal = null;
	private ERBContentItem currentSelectedERBContentItem = null;
	private ArrayList<ERBContentItem> listOfUniqueERBContentItems = new ArrayList<ERBContentItem>();
	private LinkedHashMap<ERBContentItem, TreeItem<ERBContentItem>> treeItemGuidTreeMap = new LinkedHashMap<ERBContentItem, TreeItem<ERBContentItem>>();
	
	private App app;
	private Project project;
	public EngagementActionController(App app, Project project) {
		this.app = app;
		this.project = project;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
		mainPanelHandler = new MainPanelHandler(app);
	}

	@FXML
	HBox saveHBox, body2HBox;
	@FXML
	ComboBox<Goal> goalComboBox;
	@FXML
	Label goalLabel, exploreModeLabel;
	@FXML
	Button previousButton, nextButton;
	@FXML
	TreeView<ERBContentItem> treeView;
	@FXML
	VBox engagementVBox, treeViewVBox, mainVBox,contentVBox;
	@FXML
	Button worksheetIndexButton, myPortfolioButton, uploadFileButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		treeView.setCellFactory(tv-> createTreeCell());
		
		if (project.getProjectType().contentEquals("Goal Mode")) {
		} else {
			initFacilitatorMode();
		}
		if(!project.getProjectName().contentEquals("Explore")) {
			engagementVBox.getChildren().remove(exploreModeLabel);
		}
	}

	private void initFacilitatorMode() {
		initializeGoalSelection(project);
		addTreeViewListener();
		hideGoalSelection();
		removeSaveHBox();
	}
	
	@FXML
	public void mappingButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Mapping.fxml"));
			MappingController controller = new MappingController(app);
			fxmlLoader.setController(controller);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Mapping");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load Mapping.fxml: " + e.getMessage());
		}
	}
	
	@FXML
	public void finalReportButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/finalReport/FinalReportSelection.fxml"));
			FinalReportSelectionController controller = new FinalReportSelectionController(app);
			fxmlLoader.setController(controller);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Summary Report Generator");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load FinalReportSelection.fxml: " + e.getMessage());
			e.printStackTrace();

		}
	}
	
	@FXML
	public void myPortfolioButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/MyPortfolio.fxml"));
			MyPortfolioController controller = new MyPortfolioController(app, project, currentSelectedGoal);
			fxmlLoader.setController(controller);
			VBox root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("My Portfolio");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load MyPortfolio.fxml: " + e.getMessage());
		}
	}
	
	@FXML
	public void worksheetIndexAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/WorksheetIndex.fxml"));
			WorksheetIndexController controller = new WorksheetIndexController(app, project, currentSelectedGoal);
			fxmlLoader.setController(controller);
			VBox root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Worksheet Index");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load WorksheetIndex.fxml: " + e.getMessage());
		}
	}

	private void addTreeViewListener() {
		treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> treeViewClicked(oldValue, newValue));
	}

	private void initializeGoalSelection(Project project) {
		if (project != null) {
			goalComboBox.getSelectionModel().select(0);
			goalSelected(project.getProjectGoals().get(0));
		} else {
			logger.log(Level.WARNING, "Could not initialize goal selection. Project is null.");
		}
	}

	private void initializeTreeViewSelection() {
		for (ERBContentItem erbItem : treeItemGuidTreeMap.keySet()) {
			TreeItem<ERBContentItem> treeItem = treeItemGuidTreeMap.get(erbItem);
			if (treeItem.getValue().getId() == "91") {
				getTreeView().getSelectionModel().select(treeItem);
				treeViewClicked(null, treeItem);
			}
		}
	}
	
	@FXML
	private void externalDocUploadButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ExternalFileUploader.fxml"));
			ExternalFileUploaderController conteoller = new ExternalFileUploaderController(app, this);
			fxmlLoader.setController(conteoller);
			VBox root = fxmlLoader.load();
			conteoller.launch();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Upload File");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load ExternalFileUploader.fxml: " + e.getMessage());
		}
	}
	
	public Pane loadIndicatorSetupFormController(ERBContentItem erbContentItem) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/IndicatorSetupForm.fxml"));
			IndicatorSetupFormController indicatorSetupFormController = new IndicatorSetupFormController(app,this, erbContentItem);
			fxmlLoader.setController(indicatorSetupFormController);
			VBox root = fxmlLoader.load();
			indicatorSetupFormController.setUp();
			return root;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load IndicatorSetupForm.fxml: " + e.getMessage());
			return null;
		}
	}
	
	private VBox loadWordCloudController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wordcloud/WordCloud.fxml"));
			WordCloudController wordCloudController = new WordCloudController(app, project, currentSelectedGoal, currentSelectedERBContentItem);
			fxmlLoader.setController(wordCloudController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load WordCloud.fxml: " + e.getMessage());
			return null;
		}
	}
	
	private ScrollPane loadIndicatorCenterController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorCenter.fxml"));
			IndicatorCenterController indicatorCenterController = new IndicatorCenterController(app, this);
			fxmlLoader.setController(indicatorCenterController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load IndicatorCenter.fxml: " + e.getMessage());
			return null;
		}
	}

	private void setDynamicDimensions(Pane pane, ScrollPane scrollPane) {
		if (pane != null) {
			pane.setPrefHeight(app.getPrefHeight());
			pane.setPrefWidth(app.getPrefWidth() - 10);
			VBox.setVgrow(pane, Priority.ALWAYS);
			HBox.setHgrow(pane, Priority.ALWAYS);
		} else if (scrollPane != null) {
			scrollPane.setPrefHeight(app.getPrefHeight());
			scrollPane.setPrefWidth(app.getPrefWidth());
			VBox.setVgrow(scrollPane, Priority.ALWAYS);
			HBox.setHgrow(scrollPane, Priority.ALWAYS);
		}
	}

	@FXML
	public void previousButtonAction() {
		TreeItem<ERBContentItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<ERBContentItem> previousTreeItem = getPreviousTreeItem(selectedTreeItem.getValue());
			if (previousTreeItem != null) {
				treeView.getSelectionModel().select(previousTreeItem);
				treeViewClicked(null, previousTreeItem);
			}
		}
	}

	@FXML
	public void nextButtonAction() {
		TreeItem<ERBContentItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<ERBContentItem> nextTreeItem = getNextTreeItem(selectedTreeItem.getValue());
			if (nextTreeItem != null) {
				treeView.getSelectionModel().select(nextTreeItem);
				treeViewClicked(null, nextTreeItem);
			}
		}
	}
	
	public TreeItem<ERBContentItem> getNextTreeItem(ERBContentItem currentErbItem) {
		Iterator<ERBContentItem> iterator = treeItemGuidTreeMap.keySet().iterator();
		while (iterator.hasNext()) {
			ERBContentItem erbItem = iterator.next();
			if (currentErbItem.getGuid() != null) {
				if (erbItem.getGuid() != null && (erbItem.getGuid().contentEquals(currentErbItem.getGuid()))) {
					if (iterator.hasNext()) {
						ERBContentItem nextErbItem = iterator.next();
						return treeItemGuidTreeMap.get(nextErbItem);
					}
				}
			} else {
				if (erbItem.getId().contentEquals(currentErbItem.getId())) {
					if (iterator.hasNext()) {
						ERBContentItem nextErbItem = iterator.next();
						return treeItemGuidTreeMap.get(nextErbItem);
					}
				}
			}
		}
		logger.log(Level.WARNING, "Next TreeItem is null. Check this.");
		return null;
	}
	
	public TreeItem<ERBContentItem> getPreviousTreeItem(ERBContentItem currentErbItem) {
		ListIterator<ERBContentItem> iterator = treeItemGuidTreeMap.keySet().stream().collect(Collectors.toList()).listIterator();
		while (iterator.hasNext()) {
			ERBContentItem erbItem = iterator.next();
			if (currentErbItem.getGuid() != null) {
				if (erbItem.getGuid() != null && (erbItem.getGuid().contentEquals(currentErbItem.getGuid()))) {
					if (iterator.hasPrevious()) {
						ERBContentItem previousErbItem = iterator.previous();
						previousErbItem = iterator.previous();
						return treeItemGuidTreeMap.get(previousErbItem);
					}
				}
			} else {
				if (erbItem.getId().contentEquals(currentErbItem.getId())) {
					if (iterator.hasPrevious()) {
						ERBContentItem previousErbItem = iterator.previous();
						try {
							previousErbItem = iterator.previous();
							return treeItemGuidTreeMap.get(previousErbItem);
						} catch (Exception e) {
							return null;
						}
					}
				}
			}
		}
		logger.log(Level.WARNING, "Previous TreeItem. Check this.");
		return null;
	}

	@FXML
	public void saveButtonAction() {		
		XMLManager xmlManager = new XMLManager(app);
		xmlManager.writeGoalMetaXML(fileHandler.getGoalMetaXMLFile(project, currentSelectedGoal), listOfUniqueERBContentItems);
		fileHandler.createGUIDDirectoriesForGoal2(project, currentSelectedGoal, listOfUniqueERBContentItems);
	}
	
	public void treeViewClicked(TreeItem<ERBContentItem> oldItem, TreeItem<ERBContentItem> newItem) {
		if (newItem != null) {			
			Object itemClicked = newItem.getValue();
			if (itemClicked != null) {
				ERBContentItem erbContentItem = (ERBContentItem) itemClicked;
				currentSelectedERBContentItem = erbContentItem;
				ERBContentItemSelected(erbContentItem);
			} else {
				erbPathwayTreeItemSelected();
			}
		} else {
			logger.log(Level.WARNING, "New TreeItem is null. Check this.");
		}
	}
	
	public void ERBContentItemSelected(ERBContentItem erbContentItem) {
		if (erbContentItem != null) {
			cleanContentVBox();
			if (erbContentItem.getId().contentEquals("91")) {
				Pane root = mainPanelHandler.loadERBProjectCenterRoot(app);
				addContentToContentVBox(root, true);
			} else {
				if (erbContentItem.getType().contentEquals("mainForm")) {
					File contentXMLFile = fileHandler.getFormContentFileFromResources(erbContentItem.getId());
					Pane root = loadMainFormController(contentXMLFile);
					addContentToContentVBox(root, true);
				} else if (erbContentItem.getType().contentEquals("outputForm")) {
					File contentXMLFile = fileHandler.getFormContentFileFromResources(erbContentItem.getId());
					Pane root = loadOutputFormController2(contentXMLFile, erbContentItem);
					addContentToContentVBox(root, true);
				} else if (erbContentItem.getType().contentEquals("alternativeForm")) {
					File contentXMLFile = fileHandler.getFormContentFileFromResources(erbContentItem.getId());
					Pane root = loadAlternativeFormController(contentXMLFile);
					addContentToContentVBox(root, true);
				} else if (erbContentItem.getType().contentEquals("interactiveActivity")) {
					if (erbContentItem.getLongName().contentEquals("Word Cloud")) {
						Pane root = loadWordCloudController();
						addContentToContentVBox(root, true);
					} else if (erbContentItem.getLongName().contentEquals("Noteboard")) {
						Pane root = loadIndicatorSetupFormController(erbContentItem);
						addContentToContentVBox(root, true);
					} else if (erbContentItem.getLongName().contentEquals("Indicator Center")) {
						ScrollPane root = loadIndicatorCenterController();
						addContentToContentVBox(root, true);
					}
				}
			}
		}
		logger.log(Level.WARNING, "ERBContentItem is null. Check this.");
	}

	private void erbPathwayTreeItemSelected() {
		cleanContentVBox();
		if (currentSelectedGoal != null) {
			Pane root = mainPanelHandler.loadERBProjectCenterRoot(app);
			addContentToContentVBox(root, true);
		}
	}

	public void addContentToContentVBox(Pane root, boolean setDynamicDimensions) {
		if (root != null) {
			contentVBox.getChildren().add(root);
			if (setDynamicDimensions) {
				setDynamicDimensions(root, null);
			} else {
				VBox.setVgrow(root, Priority.ALWAYS);
			}
		}
	}
	
	public void addContentToContentVBox(ScrollPane root, boolean setDynamicDimensions) {
		if (root != null) {
			contentVBox.getChildren().add(root);
			if (setDynamicDimensions) {
				setDynamicDimensions(null, root);
			} else {
				VBox.setVgrow(root, Priority.ALWAYS);
			}
		}
	}
	
	private void fillAndStoreTreeViewData_hasGUIDS(ArrayList<ERBContentItem> uniqueChapters) { // HAS GUIDS
		if (uniqueChapters != null) {
			treeItemGuidTreeMap.clear();
			listOfUniqueERBContentItems.clear();

			ERBContentItem rootERBContentItem = new ERBContentItem("91", null, "mainForm", null, "ERB Sections", "ERB Sections");
			TreeItem<ERBContentItem> rootTreeItem = new TreeItem<ERBContentItem>(rootERBContentItem);
			rootTreeItem.setExpanded(true);
			treeView.setRoot(rootTreeItem);
			treeItemGuidTreeMap.put(rootERBContentItem, rootTreeItem);

			for (ERBContentItem contentItem : uniqueChapters) {
				addChildrenToTreeView_hasGUIDS(contentItem, null, rootTreeItem);
			}
			listOfUniqueERBContentItems = uniqueChapters;

		}
	}
	
	public void addChildrenToTreeView_hasGUIDS(ERBContentItem contentItem, ERBContentItem contentItemParent, TreeItem<ERBContentItem> rootTreeItem) {
		TreeItem<ERBContentItem> treeItem = new TreeItem<ERBContentItem>(contentItem);
		rootTreeItem.getChildren().add(treeItem);
		treeItemGuidTreeMap.put(contentItem, treeItem);
		if (contentItem.getChildERBContentItems().size() > 0) {
			for (ERBContentItem erbContentItemChild : contentItem.getChildERBContentItems()) {
				addChildrenToTreeView_hasGUIDS(erbContentItemChild, contentItem, treeItem);
			}
		}
	}
	
	private void fillAndStoreTreeViewData_noGUIDS(ArrayList<ERBContentItem> genericContentItems) {
			if (genericContentItems != null) {
				treeItemGuidTreeMap.clear();
				listOfUniqueERBContentItems.clear();
				ERBContentItem rootERBContentItem = new ERBContentItem("91", null, "mainForm", null, "ERB Sections", "ERB Sections");
				TreeItem<ERBContentItem> rootTreeItem = new TreeItem<ERBContentItem>(rootERBContentItem);
				rootTreeItem.setExpanded(true);
				treeView.setRoot(rootTreeItem);
				treeItemGuidTreeMap.put(rootERBContentItem, rootTreeItem);

				for (ERBContentItem contentItem : genericContentItems) {
					addChildrenToTreeView_noGUIDS(contentItem, null, rootTreeItem);
				}
				
				listOfUniqueERBContentItems = genericContentItems;

			}
		}
		
		public void addChildrenToTreeView_noGUIDS(ERBContentItem contentItem, ERBContentItem contentItemParent, TreeItem<ERBContentItem> rootTreeItem) {
			String guid = app.generateGUID();
			contentItem.setGuid(guid);
			TreeItem<ERBContentItem> treeItem = new TreeItem<ERBContentItem>(contentItem);
			rootTreeItem.getChildren().add(treeItem);
			treeItemGuidTreeMap.put(contentItem, treeItem);
			if (contentItem.getChildERBContentItems().size() > 0) {
				for (ERBContentItem erbContentItemChild : contentItem.getChildERBContentItems()) {
					addChildrenToTreeView_noGUIDS(erbContentItemChild, contentItem, treeItem);
				}
			}
		}
	
	private TreeCell<ERBContentItem> createTreeCell() {
		return new TreeCell<ERBContentItem>() {
			@Override
			protected void updateItem(ERBContentItem item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getShortName());
				}
			}
		};
	}

	private void goalSelected(Goal goal) {
		if (goal != null) {
			currentSelectedGoal = goal;
			
			ArrayList<ERBContentItem> contentItems = goal.getListOfContentItemsInGoal();
			if(contentItems != null) {
				boolean hasGUIDs = false;
				if(contentItems.get(0) != null) {
					if(contentItems.get(0).getGuid() != null && contentItems.get(0).getGuid().length() > 0) {
						hasGUIDs= true;
					}
				}
				if(hasGUIDs) {
					fillAndStoreTreeViewData_hasGUIDS(contentItems); //LIST OF CHAPTERS IS ALREADY UNIQUE
				} else {
					fillAndStoreTreeViewData_noGUIDS(contentItems); //LIST OF CHAPTERS IS NOT UNIQUE

				}
				initializeTreeViewSelection();
			}
		}
	}
	
	private VBox loadAlternativeFormController(File xmlFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/AlternativeForm.fxml"));
			AlternativeFormController alternativeFormController = new AlternativeFormController(app, xmlFileToParse, this);
			fxmlLoader.setController(alternativeFormController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load AlternativeForm.fxml: " + e.getMessage());
			return null;
		}
	}
	
	private VBox loadOutputFormController2(File xmlContentFileToParse, ERBContentItem item) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/OutputForm.fxml"));
			OutputFormController outputFormController = new OutputFormController(item.getId(), item.getGuid(), item.getLongName(), item.getShortName(), item.getStatus(),app, xmlContentFileToParse, this);
			fxmlLoader.setController(outputFormController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load OutputForm.fxml: " + e.getMessage());
			return null;
		}
	}

	private VBox loadMainFormController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/MainForm.fxml"));
			MainFormController formContentController = new MainFormController(app, xmlContentFileToParse, this);
			fxmlLoader.setController(formContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load MainForm.fxml: " + e.getMessage());
			return null;
		}
	}

	public TreeItem<ERBContentItem> findTreeItem(String guid) {
		if (guid != null) {
			for (ERBContentItem erbItem : treeItemGuidTreeMap.keySet()) {
				if (erbItem.getGuid() != null && erbItem.getGuid().contentEquals(guid)) {
					return treeItemGuidTreeMap.get(erbItem);
				}
			}
			return null;
		}
		return null;
	}
	
	public ERBContentItem findUniqueERBContentItem(String id) {
		if (id != null) {
			for (ERBContentItem erbContentItem : listOfUniqueERBContentItems) {
				if (erbContentItem.getId().contentEquals(id)) {
					return erbContentItem;
				}
			}
		}
		logger.log(Level.WARNING, "Null ERBContentItem. Check this.");
		return null;
	}


	public void cleanContentVBox() {
		contentVBox.getChildren().clear();
	}

	private void hideGoalSelection() {
		goalLabel.setVisible(false);
		goalComboBox.setVisible(false);
	}

	private void removeSaveHBox() {
		if (engagementVBox.getChildren().contains(saveHBox)) {
			engagementVBox.getChildren().remove(saveHBox);
		}
	}

	public Goal getCurrentGoal() {
		return currentSelectedGoal;
	}

	public void setCurrentSelectedGoal(Goal currentSelectedGoal) {
		this.currentSelectedGoal = currentSelectedGoal;
	}

	public ERBContentItem getCurrentSelectedERBContentItem() {
		return currentSelectedERBContentItem;
	}

	public void setCurrentSelectedERBContentItem(ERBContentItem currentSelectedERBContentItem) {
		this.currentSelectedERBContentItem = currentSelectedERBContentItem;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public HashMap<ERBContentItem, TreeItem<ERBContentItem>> getTreeItemIdTreeMap() {
		return treeItemGuidTreeMap;
	}

	public void setTreeItemIdTreeMap(LinkedHashMap<ERBContentItem, TreeItem<ERBContentItem>> treeItemIdTreeMap) {
		this.treeItemGuidTreeMap = treeItemIdTreeMap;
	}

	public ComboBox<Goal> getGoalComboBox() {
		return goalComboBox;
	}
	public VBox getTreeViewVBox() {
		return treeViewVBox;
	}
	
	public TreeView<ERBContentItem> getTreeView() {
		return treeView;
	}
	
	public VBox getMainVBox() {
		return mainVBox;
	}

	public HBox getBody2HBox() {
		return body2HBox;
	}

	public VBox getContentVBox() {
		return contentVBox;
	}

	public Button getPreviousButton() {
		return previousButton;
	}

	public Button getNextButton() {
		return nextButton;
	}

	public VBox getEngagementVBox() {
		return engagementVBox;
	}

	public ArrayList<ERBContentItem> getListOfUniqueERBContentItems() {
		return listOfUniqueERBContentItems;
	}

}
