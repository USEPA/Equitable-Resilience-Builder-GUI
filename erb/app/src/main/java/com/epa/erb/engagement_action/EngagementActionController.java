package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.ContentPanel;
import com.epa.erb.ERBItem;
import com.epa.erb.ERBItemFinder;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.forms.OutputFormController;
import com.epa.erb.goal.Goal;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.progress.GlobalProgressTrackerController;
import com.epa.erb.progress.Progress;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import com.epa.erb.wordcloud.WordCloudController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EngagementActionController implements Initializable {

	@FXML
	VBox engagementVBox;
	@FXML
	Label goalLabel;
	@FXML
	ComboBox<Goal> goalComboBox;
	@FXML
	HBox headingHBox;
	@FXML
	HBox saveHBox;
	@FXML
	TitledPane pathwayTitledPane;
	@FXML
	HBox erbPathwayDiagramHBox;
	@FXML
	ScrollPane erbPathwayDiagramScrollPane;
	@FXML
	VBox erbKeyVBox;
	@FXML
	HBox keyPaneHBox;
	@FXML
	Pane completeKeyPane;
	@FXML
	Pane readyKeyPane;
	@FXML
	Pane inProgressKeyPane;
	@FXML
	VBox treeViewVBox;
	@FXML
	TreeView<ERBItem> treeView;
	@FXML
	VBox localProgressVBox;
	@FXML
	VBox mainVBox;
	@FXML
	HBox statusHBox;
	@FXML
	HBox body2HBox;
	@FXML
	VBox contentVBox;
	@FXML
	Label goalProgressPercentLabel;
	@FXML
	Label goalConfidencePercentLabel;
	@FXML
	VBox goalProgressVBox;
	@FXML
	VBox goalProgressBar;
	@FXML
	ProgressIndicator chapterProgressIndicator;
	@FXML
	Button previousButton;
	@FXML
	Button nextButton;
	@FXML
	ToggleGroup activityStatusToggleGroup;
	@FXML
	RadioButton readyRadioButton;
	@FXML
	RadioButton inProgressRadioButton;
	@FXML
	RadioButton completeRadioButton;
	@FXML
	VBox titledPaneVBox;


	private App app;
	private Project project;
	public EngagementActionController(App app, Project project) {
		this.app = app;
		this.project = project;
	}
	
	ArrayList<Chapter> listOfUniqueChapters = new ArrayList<Chapter>();
	
	private Goal currentSelectedGoal = null;
	private Chapter currentSelectedChapter = null;
	private ContentPanel currentSelectedContentPanel = null;

	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	private LinkedHashMap<ERBItem, TreeItem<ERBItem>> treeItemGuidTreeMap = new LinkedHashMap<ERBItem, TreeItem<ERBItem>>();
	private ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers = new ArrayList<ERBPathwayDiagramController>();
	private ArrayList<ERBChapterDiagramController> listOfChapterDiagramControllers = new ArrayList<ERBChapterDiagramController>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		treeView.setCellFactory(tv-> createTreeCell());
		if (project.getProjectType().contentEquals("Goal Mode")) {
			initGoalMode();
		} else {
			initFacilitatorMode();
		}
		treeView.getStylesheets().add(getClass().getResource("/treeView.css").toString());
	}

	private void initGoalMode() {
		fillGoalComboBox(project);
		goalComboBox.setCellFactory(lv -> createGoalCell());
		goalComboBox.setButtonCell(createGoalCell());
		initializeGoalSelection(project);
		handleControls();
	}

	private void initFacilitatorMode() {
		initializeGoalSelection(project);
		addTreeViewListener();
		initializeStyle();
		hideGoalSelection();
		removeSaveHBox();
		removePathwayPane();
		removeLocalProgressPane();
	}

	private void handleControls() {
		initializeStyle();
		addProgressListeners();
		addGoalChangeListener();
		addTreeViewListener();
	}

	private void initializeStyle() {
		pathwayTitledPane.setStyle("-fx-box-border: transparent;");
		completeKeyPane.setStyle("-fx-background-color: " + constants.getCompleteStatusColor() + ";");
		readyKeyPane.setStyle("-fx-background-color: " + constants.getReadyStatusColor() + ";");
		inProgressKeyPane.setStyle("-fx-background-color: " + constants.getInProgressStatusColor() + ";");
		chapterProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
	}

	private void addTreeViewListener() {
		treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> treeViewClicked(oldValue, newValue));
	}

	private void addProgressListeners() {
		localProgressVBox.heightProperty().addListener(e -> localProgressPaneSizeChanged());
		localProgressVBox.widthProperty().addListener(e -> localProgressPaneSizeChanged());
	}

	private void addGoalChangeListener() {
		goalComboBox.valueProperty().addListener(new ChangeListener<Goal>() {
			@Override
			public void changed(ObservableValue ov, Goal origG, Goal newG) {
				goalChanged(origG, newG);
			}
		});
	}

	private void localProgressPaneSizeChanged() {
		if (currentSelectedContentPanel != null) {
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
		} else {
			TreeItem<ERBItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			if (selectedTreeItem != null && selectedTreeItem.getValue().getShortName().contains("Chapter")) {
				updateLocalProgress(erbItemFinder.getChapterForNameInGoal(selectedTreeItem.getValue().getShortName().trim(), currentSelectedGoal),currentSelectedGoal.getChapters());
			}
		}
	}

	private void initializeGoalSelection(Project project) {
		if (project != null) {
			goalComboBox.getSelectionModel().select(0);
			goalSelected(project.getProjectGoals().get(0));
		} else {
			logger.error("Cannot initializeGoalSelection. project = " + project);
		}
	}

	private void initializeTreeViewSelection() {
		for (ERBItem erbItem : treeItemGuidTreeMap.keySet()) {
			TreeItem<ERBItem> treeItem = treeItemGuidTreeMap.get(erbItem);
			//TODO: Made not static
			if (treeItem.getValue().getId() == "004") {
				getTreeView().getSelectionModel().select(treeItem);
				treeViewClicked(null, treeItem);
			}
		}
	}

	private VBox loadChapterLanding(ArrayList<Chapter> chapters) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementGoalLandingContent.fxml"));
			EngagementGoalLandingContentController chapterLandingController = new EngagementGoalLandingContentController(chapters, this);
			fxmlLoader.setController(chapterLandingController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private VBox loadNoteBoardContentController(ContentPanel contentPanel) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController(contentPanel.getId(), contentPanel.getGuid(), contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType(), app, project,currentSelectedGoal);
			fxmlLoader.setController(noteBoardContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private VBox loadWordCloudController(ContentPanel contentPanel) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wordcloud/WordCloud.fxml"));
			WordCloudController wordCloudController = new WordCloudController(contentPanel.getId(), contentPanel.getGuid(), contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType(), app, project, currentSelectedGoal);
			fxmlLoader.setController(wordCloudController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private Parent loadERBChapterDiagramRoot(Chapter chapter, ArrayList<Chapter> chapters) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBChapterDiagram.fxml"));
			ERBChapterDiagramController erbChapterDiagramController = new ERBChapterDiagramController(chapter, chapters, this);
			listOfChapterDiagramControllers.add(erbChapterDiagramController);
			fxmlLoader.setController(erbChapterDiagramController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private Parent loadERBPathwayDiagramRoot(ContentPanel contentPanel, Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBPathwayDiagram.fxml"));
			ERBPathwayDiagramController erbPathwayDiagramController = new ERBPathwayDiagramController(contentPanel, chapter, this);
			listOfPathwayDiagramControllers.add(erbPathwayDiagramController);
			fxmlLoader.setController(erbPathwayDiagramController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private Parent loadGlobalGoalTracker() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/progress/GlobalProgressTracker.fxml"));
			GlobalProgressTrackerController globalGoalTrackerController = new GlobalProgressTrackerController(app, project);
			fxmlLoader.setController(globalGoalTrackerController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private Stage globalGoalTrackerStage = null;

	private void showGlobalGoalTracker(Parent globalGoalTrackerRoot) {
		if (globalGoalTrackerRoot != null) {
			globalGoalTrackerStage = new Stage();
			Scene scene = new Scene(globalGoalTrackerRoot);
			globalGoalTrackerStage.setScene(scene);
			globalGoalTrackerStage.setTitle("ERB: Goal Tracker");
			globalGoalTrackerStage.show();
		} else {
			logger.error("Cannot showGlobalGoalTracker. globalGoalTrackerRoot = " + globalGoalTrackerRoot);
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

	private void goalChanged(Goal origGoal, Goal newGoal) {
		if (origGoal != null) {
			goalSelected(newGoal);
		}
	}

	@FXML
	public void showDetailsCheckboxAction() {

	}

	@FXML
	public void activityStatusRadioButtonAction() {
		RadioButton radioButton = (RadioButton) activityStatusToggleGroup.getSelectedToggle();
		TreeItem<ERBItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			ERBItem selectedErbItem = selectedTreeItem.getValue();
			String GUIDOfItemClicked = selectedTreeItem.getValue().getGuid();
			Object itemClicked = selectedErbItem.getObject();
			String status = getActivityStatusForRadioButtonState(radioButton);
			if (itemClicked != null) {
				if(itemClicked.toString().contains("ContentPanel")) {
					ContentPanel contentPanel = (ContentPanel) itemClicked;
					contentPanel.setStatus(status);
				} else if (itemClicked.toString().contains("Chapter")) {
					Chapter chapter = (Chapter) itemClicked;
				}
			}
			for (ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
				if (erbPathwayDiagramController.getContentPanel().getGuid().contentEquals(GUIDOfItemClicked)) {
					erbPathwayDiagramController.getContentPanel().setStatus(status);
					erbPathwayDiagramController.updateStatus();
				}
			}
		}
		updateLocalProgress(currentSelectedChapter, listOfUniqueChapters);
	}

	private String getActivityStatusForRadioButtonState(RadioButton radioButton) {
		if (radioButton != null) {
			if (radioButton.getText().contentEquals("Ready")) {
				return "ready";
			} else if (radioButton.getText().contentEquals("In Progress")) {
				return "in progress";
			} else if (radioButton.getText().contentEquals("Complete")) {
				return "complete";
			}
		} else {
			logger.error("Cannot changeActivityStatus. radioButton is null.");
		}
		return "ready"; //default
	}

	@FXML
	public void previousButtonAction() {
		TreeItem<ERBItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<ERBItem> previousTreeItem = getPreviousTreeItem(selectedTreeItem.getValue());
			if (previousTreeItem != null) {
				treeView.getSelectionModel().select(previousTreeItem);
				treeViewClicked(null, previousTreeItem);
			}
		}
	}

	@FXML
	public void nextButtonAction() {
		TreeItem<ERBItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<ERBItem> nextTreeItem = getNextTreeItem(selectedTreeItem.getValue());
			if (nextTreeItem != null) {
				treeView.getSelectionModel().select(nextTreeItem);
				treeViewClicked(null, nextTreeItem);
			}
		}
	}
	
	public TreeItem<ERBItem> getNextTreeItem(ERBItem currentErbItem) {
		Iterator<ERBItem> iterator = treeItemGuidTreeMap.keySet().iterator();
		while (iterator.hasNext()) {
			ERBItem erbItem = iterator.next();
			if (currentErbItem.getGuid() != null) {
				if (erbItem.getGuid() != null && (erbItem.getGuid().contentEquals(currentErbItem.getGuid()))) {
					if (iterator.hasNext()) {
						ERBItem nextErbItem = iterator.next();
						return treeItemGuidTreeMap.get(nextErbItem);
					}
				}
			} else {
				if (erbItem.getId().contentEquals(currentErbItem.getId())) {
					if (iterator.hasNext()) {
						ERBItem nextErbItem = iterator.next();
						return treeItemGuidTreeMap.get(nextErbItem);
					}
				}
			}
		}
		return null;
	}
	
	public TreeItem<ERBItem> getPreviousTreeItem(ERBItem currentErbItem) {
		ListIterator<ERBItem> iterator = treeItemGuidTreeMap.keySet().stream().collect(Collectors.toList())
				.listIterator();
		while (iterator.hasNext()) {
			ERBItem erbItem = iterator.next();
			if (currentErbItem.getGuid() != null) {
				if (erbItem.getGuid() != null && (erbItem.getGuid().contentEquals(currentErbItem.getGuid()))) {
					if (iterator.hasPrevious()) {
						ERBItem previousErbItem = iterator.previous();
						previousErbItem = iterator.previous();
						return treeItemGuidTreeMap.get(previousErbItem);
					}
				}
			} else {
				if (erbItem.getId().contentEquals(currentErbItem.getId())) {
					if (iterator.hasPrevious()) {
						ERBItem previousErbItem = iterator.previous();
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
		return null;
	}

	@FXML
	public void saveButtonAction() {		
		XMLManager xmlManager = new XMLManager(app);
		xmlManager.writeGoalMetaXML(fileHandler.getGoalMetaXMLFile(project, currentSelectedGoal), listOfUniqueChapters);
		fileHandler.createGUIDDirectoriesForGoal(project, currentSelectedGoal, listOfUniqueChapters);
	}

	@FXML
	public void goalLabelClicked() {
		Parent globalGoalTrackerRoot = loadGlobalGoalTracker();
		showGlobalGoalTracker(globalGoalTrackerRoot);
	}
	
	public void treeViewClicked(TreeItem<ERBItem> oldItem, TreeItem<ERBItem> newItem) {
		if (newItem != null) {			
			Object itemClicked = newItem.getValue().getObject();
			if (itemClicked != null) {
				if(itemClicked.toString().contains("ContentPanel")) {
					ContentPanel contentPanel = (ContentPanel) itemClicked;
					contentPanelTreeItemSelected(newItem, contentPanel);
				} else if (itemClicked.toString().contains("Chapter")) {
					Chapter chapter = (Chapter) itemClicked;
					chapterTreeItemSelected(newItem, chapter);
				}
			} else {
				erbPathwayTreeItemSelected();
			}
		}
	}

	private void erbPathwayTreeItemSelected() {
		currentSelectedChapter = null;
		cleanContentVBox();
		removeStatusHBox();
		removeLocalProgressVBox();
		addERBKeyVBox(1);
		generateChapterERBPathway(currentSelectedGoal);
		if (currentSelectedGoal != null) {
			Pane root = loadChapterLanding(listOfUniqueChapters);
			addContentToContentVBox(root, true);
		}
	}
	
	private void contentPanelTreeItemSelected(TreeItem<ERBItem> contentPanelTreeItem, ContentPanel contentPanel) {
		currentSelectedContentPanel = contentPanel;
		currentSelectedChapter = erbItemFinder.getChapterForContentPanel(listOfUniqueChapters, contentPanel);
		cleanContentVBox();
		addERBKeyVBox(1);

		File file = fileHandler.getStaticSupportingFormTextXML(contentPanel.getId());
		//TODO: CHECK IF NOT MAIN FORM
		Pane root = loadMainFormController(file);
		addContentToContentVBox(root, false);
		
		generateActivityERBPathway(currentSelectedChapter);
		setContentPanelStatusRadioButton(contentPanel);
		highlightContentPanelDiagram(contentPanel);
		
		if (currentSelectedChapter != null && currentSelectedGoal != null)
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
	}

	private void chapterTreeItemSelected(TreeItem<ERBItem> chapterTreeItem, Chapter chapter) {
		currentSelectedChapter = chapter;
		cleanContentVBox();
		removeStatusHBox();
		addERBKeyVBox(1);
		if (project.getProjectType().contentEquals("Goal Mode"))
			addLocalProgressVBox(1);
		generateActivityERBPathway(currentSelectedChapter);
		if (currentSelectedChapter != null) {
			File file = fileHandler.getStaticSupportingFormTextXML(currentSelectedChapter.getId());
			Pane root = loadMainFormController(file);
			addContentToContentVBox(root, true);
		}
		if (currentSelectedChapter != null && currentSelectedGoal != null)
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
	}

	public void addContentToContentVBox(Pane root, boolean setDynamicDimensions) {
		if (root != null) {
			contentVBox.getChildren().add(root);
			if (setDynamicDimensions) {
				setDynamicDimensions(root, null);
			} else {
				VBox.setVgrow(root, Priority.ALWAYS);
			}
		} else {
			logger.error("Cannot addContentToContentVBox. root is null.");
		}
	}
	
	private ArrayList<ContentPanel> uniqueContentPanels = new ArrayList<ContentPanel>();
	private HashMap<ContentPanel, String> childContentPanelsToParentGUID = new HashMap<ContentPanel, String>();
	private void fillAndStoreTreeViewData_New(ArrayList<Chapter> chapters, boolean hasGUIDS) {
		if(chapters != null) {
			treeItemGuidTreeMap.clear();
			ERBItem rootERBItem = new ERBItem("004", null, "ERB Pathway" , "ERB Pathway", null);
			TreeItem<ERBItem> rootTreeItem = new TreeItem<ERBItem>(rootERBItem);
			rootTreeItem.setExpanded(true);
			treeView.setRoot(rootTreeItem);
			treeItemGuidTreeMap.put(rootERBItem, rootTreeItem);
			
			for(Chapter chapter: chapters) {
				Chapter uniqueChapter = null;
				if(chapter.getGuid() != null) {
					uniqueChapter = chapter;
				}else {
					String chapterGUID = app.generateGUID();
					uniqueChapter = new Chapter(chapter.getId(), chapterGUID, chapter.getLongName(),chapter.getShortName(), chapter.getStatus(), chapter.getNotes(), chapter.getDescription(),chapter.getNumber());
					uniqueChapter.setContentPanels(chapter.getContentPanels());
				}
				ERBItem chapterERBItem = new ERBItem(uniqueChapter.getId(), uniqueChapter.getGuid(), uniqueChapter.getLongName(), uniqueChapter.getShortName(), uniqueChapter);
				TreeItem<ERBItem> chapterTreeItem = new TreeItem<ERBItem>(chapterERBItem);
				rootTreeItem.getChildren().add(chapterTreeItem);
				treeItemGuidTreeMap.put(chapterERBItem, chapterTreeItem);
				parseForContentPanels_New(chapterERBItem, null, chapterTreeItem, null);
				
				if(!hasGUIDS) {
				//------------------------
				for(ContentPanel cp: childContentPanelsToParentGUID.keySet()) {
					String guidOfParentToAddTo = childContentPanelsToParentGUID.get(cp);
					for(ContentPanel parentCP: uniqueContentPanels) {
						if(parentCP.getGuid().contentEquals(guidOfParentToAddTo)) {
							parentCP.addNextLayerContentPanel(cp);
							break;
						}
					}
				}
				uniqueChapter.getContentPanels().clear();
				//------------------------
				for(ContentPanel parent: uniqueContentPanels) {
					uniqueChapter.addContentPanel(parent);
				}
				uniqueContentPanels.clear();
				childContentPanelsToParentGUID.clear();
				}
				
				listOfUniqueChapters.add(uniqueChapter);
			}
		}
	}
	
	private TreeItem<ERBItem> titp = null;
	
	private void parseForContentPanels_New(ERBItem item,ERBItem parent, TreeItem<ERBItem> chapterTreeItem, TreeItem<ERBItem> cpTreeItem) {
		if(item.getObject().toString().contains("Chapter")) { //is chapter
			Chapter chapter = (Chapter) item.getObject();		
			for(ContentPanel childContentPanel: chapter.getContentPanels()) {
				ERBItem pass = new ERBItem(childContentPanel.getId(), childContentPanel.getGuid(), childContentPanel.getLongName(), childContentPanel.getShortName(), childContentPanel);
				parseForContentPanels_New(pass, item, chapterTreeItem, null);
			}
		} else { //is not chapter
			ContentPanel contentPanel = (ContentPanel) item.getObject();
			if(contentPanel.getNextLayerContentPanels().size() > 0) { //has children
				ContentPanel uniqueContentPanel = null;
				String contentPanelGUID = null;
				if(contentPanel.getGuid() != null) {
					contentPanelGUID = contentPanel.getGuid();
					uniqueContentPanel = contentPanel;
				} else {
					contentPanelGUID = app.generateGUID();
					uniqueContentPanel = new ContentPanel(contentPanel.getId(), contentPanelGUID, contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType());
					uniqueContentPanel.setNextLayerContentPanels(contentPanel.getNextLayerContentPanels());
				}
				ERBItem uniqueContentPanelERBItem = new ERBItem(uniqueContentPanel.getId(), uniqueContentPanel.getGuid(), uniqueContentPanel.getLongName(), uniqueContentPanel.getShortName(), uniqueContentPanel);
				TreeItem<ERBItem> uniqueContentPanelTreeItem = new TreeItem<ERBItem>(uniqueContentPanelERBItem);
				treeItemGuidTreeMap.put(uniqueContentPanelERBItem, uniqueContentPanelTreeItem);
				titp = uniqueContentPanelTreeItem;
				
				if (parent.getObject().toString().contains("Chapter")) { //parent is chapter
					//CREATE NEW TREE ITEM FOR CONTENT PANE: ADD TO CHAPTER
					item.setGuid(contentPanelGUID);
					chapterTreeItem.getChildren().add(uniqueContentPanelTreeItem);
					uniqueContentPanels.add(uniqueContentPanel);
				}else { //parent is not chapter
					//CREATE NEW TREE ITEM FOR CONTENT PANE: ADD TO PARENT CONTENT
					cpTreeItem.getChildren().add(uniqueContentPanelTreeItem);
					//Add to unique parent
					childContentPanelsToParentGUID.put(uniqueContentPanel, parent.getGuid());
				}
				for(ContentPanel layerContentPanel: contentPanel.getNextLayerContentPanels()) {
					ERBItem pass = new ERBItem(layerContentPanel.getId(), layerContentPanel.getGuid(), layerContentPanel.getLongName(), layerContentPanel.getShortName(), layerContentPanel);
					parseForContentPanels_New(pass, item, chapterTreeItem, titp);
				}
			} else { //no children
				//CREATE NEW TREE ITEM FOR CONTENT PANE: ADD TO CHAPTER
				ContentPanel uniqueContentPanel = null;
				if(contentPanel.getGuid() != null) {
					uniqueContentPanel = contentPanel;
				} else {
					String contentPanelGUID = app.generateGUID();
					uniqueContentPanel = new ContentPanel(contentPanel.getId(), contentPanelGUID, contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType());
				}
				ERBItem contentPanelERBItem = new ERBItem(uniqueContentPanel.getId(), uniqueContentPanel.getGuid(), uniqueContentPanel.getLongName(), uniqueContentPanel.getShortName(), uniqueContentPanel);
				TreeItem<ERBItem> contentPanelTreeItem = new TreeItem<ERBItem>(contentPanelERBItem);
				chapterTreeItem.getChildren().add(contentPanelTreeItem);
				treeItemGuidTreeMap.put(contentPanelERBItem, contentPanelTreeItem);
				uniqueContentPanels.add(uniqueContentPanel);
			}
		}
	}
	
	
	
	
//	private void fillAndStoreTreeViewData_GUIDS(ArrayList<Chapter> uniqueChapters) {
//		
//	}



	
//	private void fillAndStoreTreeViewData(ArrayList<Chapter> genericChapters) { // Does not have GUIDS
//		if (genericChapters != null) {
//			treeItemGuidTreeMap.clear();
//			ERBItem rootErbItem = new ERBItem("004", null, "ERB Pathway", "ERB Pathway", null);
//			TreeItem<ERBItem> rootTreeItem = new TreeItem<ERBItem>(rootErbItem);
//			rootTreeItem.setExpanded(true);
//			treeView.setRoot(rootTreeItem);
//			treeItemGuidTreeMap.put(rootErbItem, rootTreeItem);
//
//			// CHAPTER
//			for (Chapter chapter : genericChapters) {
//				String chapterGUID = app.generateGUID();
//				Chapter uniqueChapter = new Chapter(chapter.getId(), chapterGUID, chapter.getLongName(),chapter.getShortName(), chapter.getStatus(), chapter.getNotes(), chapter.getDescription(),chapter.getNumber());
//				uniqueChapter.setContentPanels(chapter.getContentPanels());
//				ERBItem chapterERBItem = new ERBItem(uniqueChapter.getId(), uniqueChapter.getGuid(), uniqueChapter.getLongName(), uniqueChapter.getShortName(), uniqueChapter);
//				TreeItem<ERBItem> chapterTreeItem = new TreeItem<ERBItem>(chapterERBItem);
//				rootTreeItem.getChildren().add(chapterTreeItem);
//				treeItemGuidTreeMap.put(chapterERBItem, chapterTreeItem);
//				parseForContentPanels(chapterERBItem, null, chapterTreeItem, null );
//				
//				//------------------------
//				for(ContentPanel cp: childContentPanelsToParentGUID.keySet()) {
//					String guidOfParentToAddTo = childContentPanelsToParentGUID.get(cp);
//					for(ContentPanel parentCP: uniqueContentPanels) {
//						if(parentCP.getGuid().contentEquals(guidOfParentToAddTo)) {
//							parentCP.addNextLayerContentPanel(cp);
//							break;
//						}
//					}
//				}
//				uniqueChapter.getContentPanels().clear();
//				//------------------------
//				for(ContentPanel parent: uniqueContentPanels) {
//					uniqueChapter.addContentPanel(parent);
//				}
//				uniqueContentPanels.clear();
//				childContentPanelsToParentGUID.clear();
//				listOfUniqueChapters.add(uniqueChapter);
//			} // CHAPTER
//		} else {
//			logger.error("Cannot fillAndStoreTreeViewData. chapters = " + genericChapters);
//		}
//	}
//	

//
//	private void parseForContentPanels(ERBItem item,ERBItem parent, TreeItem<ERBItem> chapterTreeItem, TreeItem<ERBItem> cpTreeItem) {
//		if(item.getObject().toString().contains("Chapter")) { //is chapter
//			Chapter chapter = (Chapter) item.getObject();		
//			for(ContentPanel childContentPanel: chapter.getContentPanels()) {
//				ERBItem pass = new ERBItem(childContentPanel.getId(), childContentPanel.getGuid(), childContentPanel.getLongName(), childContentPanel.getShortName(), childContentPanel);
//				parseForContentPanels(pass, item, chapterTreeItem, null);
//			}
//		} else { //is not chapter
//			ContentPanel contentPanel = (ContentPanel) item.getObject();
//			if(contentPanel.getNextLayerContentPanels().size() > 0) { //has children
//				if (parent.getObject().toString().contains("Chapter")) { //parent is chapter
//					//CREATE NEW TREE ITEM FOR CONTENT PANE: ADD TO CHAPTER
//					String contentPanelGUID = app.generateGUID();
//					ContentPanel uniqueParentContentPanel = new ContentPanel(contentPanel.getId(), contentPanelGUID, contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType());
//					uniqueParentContentPanel.setNextLayerContentPanels(contentPanel.getNextLayerContentPanels());
//					ERBItem parentContentPanelERBItem = new ERBItem(uniqueParentContentPanel.getId(), uniqueParentContentPanel.getGuid(), uniqueParentContentPanel.getLongName(), uniqueParentContentPanel.getShortName(), uniqueParentContentPanel);
//					item.setGuid(contentPanelGUID);
//					TreeItem<ERBItem> parentContentPanelTreeItem = new TreeItem<ERBItem>(parentContentPanelERBItem);
//					chapterTreeItem.getChildren().add(parentContentPanelTreeItem);
//					treeItemGuidTreeMap.put(parentContentPanelERBItem, parentContentPanelTreeItem);
//					titp = parentContentPanelTreeItem;
//					uniqueContentPanels.add(uniqueParentContentPanel);
//				}else { //parent is not chapter
//					//CREATE NEW TREE ITEM FOR CONTENT PANE: ADD TO PARENT CONTENT
//					String contentPanelGUID = app.generateGUID();
//					ContentPanel uniqueChildContentPanel = new ContentPanel(contentPanel.getId(), contentPanelGUID, contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType());
//					uniqueChildContentPanel.setNextLayerContentPanels(contentPanel.getNextLayerContentPanels());
//					ERBItem childContentPanelERBItem = new ERBItem(uniqueChildContentPanel.getId(), uniqueChildContentPanel.getGuid(), uniqueChildContentPanel.getLongName(), uniqueChildContentPanel.getShortName(), uniqueChildContentPanel);
//					TreeItem<ERBItem> childContentPanelTreeItem = new TreeItem<ERBItem>(childContentPanelERBItem);
//					cpTreeItem.getChildren().add(childContentPanelTreeItem);
//					treeItemGuidTreeMap.put(childContentPanelERBItem, childContentPanelTreeItem);
//					titp = childContentPanelTreeItem;
//					//Add to unique parent
//					childContentPanelsToParentGUID.put(uniqueChildContentPanel, parent.getGuid());
//					
//				}
//				for(ContentPanel layerContentPanel: contentPanel.getNextLayerContentPanels()) {
//					ERBItem pass = new ERBItem(layerContentPanel.getId(), layerContentPanel.getGuid(), layerContentPanel.getLongName(), layerContentPanel.getShortName(), layerContentPanel);
//					parseForContentPanels(pass, item, chapterTreeItem, titp);
//				}
//			} else { //no children
//				//CREATE NEW TREE ITEM FOR CONTENT PANE: ADD TO CHAPTER
//				String contentPanelGUID = app.generateGUID();
//				ContentPanel uniqueContentPanel = new ContentPanel(contentPanel.getId(), contentPanelGUID, contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType());
//				ERBItem contentPanelERBItem = new ERBItem(uniqueContentPanel.getId(), uniqueContentPanel.getGuid(), uniqueContentPanel.getLongName(), uniqueContentPanel.getShortName(), uniqueContentPanel);
//				TreeItem<ERBItem> contentPanelTreeItem = new TreeItem<ERBItem>(contentPanelERBItem);
//				chapterTreeItem.getChildren().add(contentPanelTreeItem);
//				treeItemGuidTreeMap.put(contentPanelERBItem, contentPanelTreeItem);
//				uniqueContentPanels.add(uniqueContentPanel);
//			}
//		}
//	}

	private void fillGoalComboBox(Project project) {
		if (project != null) {
			for (Goal goal : project.getProjectGoals()) {
				goalComboBox.getItems().add(goal);
			}
		} else {
			logger.error("Cannot fillGoalComboBox. project = " + project);
		}
	}

	private ListCell<Goal> createGoalCell() {
		return new ListCell<Goal>() {
			@Override
			protected void updateItem(Goal item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getGoalName());
				}
			}
		};
	}
	
	private TreeCell<ERBItem> createTreeCell() {
		return new TreeCell<ERBItem>() {
			@Override
			protected void updateItem(ERBItem item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getLongName());
					if(item.getObject() != null) {
						String color = getTextColorForERBItem(item);
						if (color != null) {
							setStyle("-fx-text-fill: " + color);
						}else {
							setStyle("-fx-text-fill: black");
						}
					}
				}
			}
		};
	}
	
	private String getTextColorForERBItem(ERBItem erbItem) {
		Chapter chapter = null;
		if (erbItem.getObject().toString().contains("Chapter")) {
			chapter = (Chapter) erbItem.getObject();
		} else {
			ContentPanel cp = (ContentPanel) erbItem.getObject();
			chapter = erbItemFinder.getChapterForContentPanel(listOfUniqueChapters, (ContentPanel) erbItem.getObject());
//			System.out.println("Chapter for: " + cp.getLongName() + " is " + chapter.getShortName());
		}
		if (chapter != null) {
			if (chapter.getNumber() == 1) {
				return constants.getChapter1Color();
			} else if (chapter.getNumber() == 2) {
				return constants.getChapter2Color();
			} else if (chapter.getNumber() == 3) {
				return constants.getChapter3Color();
			} else if (chapter.getNumber() == 4) {
				return constants.getChapter4Color();
			} else if (chapter.getNumber() == 5) {
				return constants.getChapter5Color();
			}
		}
		return null;
	}

	private void goalSelected(Goal goal) {
		if (goal != null) {
			currentSelectedGoal = goal;
			ArrayList<Chapter> listOfChapters = goal.getChapters();
			if (listOfChapters != null) {
				boolean hasGUIDs = false;
				if(listOfChapters.get(0) != null) {
					if(listOfChapters.get(0).getGuid() != null && listOfChapters.get(0).getGuid().length() > 0) {
						hasGUIDs= true;
					}
				}
				fillAndStoreTreeViewData_New(listOfChapters,hasGUIDs);
//				if(hasGUIDs) {
//					fillAndStoreTreeViewData_GUIDS(listOfChapters); //LIST OF CHAPTERS IS ALREADY UNIQUE
//				} else {
//					fillAndStoreTreeViewData(listOfChapters); //LIST OF CHAPTERS IS NOT UNIQUE
//				}
				initializeTreeViewSelection();
				generateChapterERBPathway(currentSelectedGoal);
			}
		} else {
			logger.error("Cannot execute goalSelected. goal = " + goal);
		}
	}

	public void updateLocalProgress(Chapter chapter, ArrayList<Chapter> chapters) {
		Progress progress = new Progress();
		if (chapter != null) {
			int chapterProgress = progress.getChapterPercentDone(chapter);
			progress.setChapterProgress(chapterProgressIndicator, chapterProgress);
		}
		if (chapters != null) {
			int goalProgress = progress.getGoalPercentDone(chapters);
			progress.setGoalProgress(goalProgressVBox, goalProgressBar, goalProgressPercentLabel, goalProgress);
		}
	}
	private void loadDynamicActivityContent(ContentPanel contentPanel) {
		if (contentPanel != null) {
			//TODO: Somehow make this not static
			if (contentPanel.getId().contentEquals("00000001")) {
				Pane root = loadNoteBoardContentController(contentPanel);
				addContentToContentVBox(root, false);
			} else if (contentPanel.getId().contentEquals("00000002")) {
				Pane root = loadWordCloudController(contentPanel);
				addContentToContentVBox(root, false);
			}
		} else {
			logger.error("Cannot loadDynamicActivityContent. InteractiveActivity = " + contentPanel);

		}
	}

	public Pane loadStepContent(ContentPanel contentPanel) {
		if (contentPanel != null) {
			File file = fileHandler.getStaticSupportingFormTextXML(contentPanel.getId());
			if (contentPanel.getType().contentEquals("mainForm")) {
				Pane root = loadMainFormController(file);
				return root;
			} else if (contentPanel.getType().contentEquals("outputForm")) {
				Pane root = loadOutputFormController(file, contentPanel);
				return root;
			}
		} else {
			logger.error("Cannot loadStepContent. step = " + contentPanel);
		}
		return null;
	}

	private VBox loadOutputFormController(File xmlContentFileToParse, ContentPanel contentPanel) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/OutputForm.fxml"));
			OutputFormController outputFormController = new OutputFormController(contentPanel.getId(), contentPanel.getGuid(), contentPanel.getLongName(), contentPanel.getShortName(), contentPanel.getStatus(), contentPanel.getType(), app, xmlContentFileToParse, this);
			fxmlLoader.setController(outputFormController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
			return null;
		}
	}

	public ContentPanel retrieveNextActivity(ERBPathwayDiagramController currentErbPathwayDiagramController) {
		int indexOfCurrentController = listOfPathwayDiagramControllers.indexOf(currentErbPathwayDiagramController);
		if (indexOfCurrentController >= 0) {
			int indexOfNextController = indexOfCurrentController + 1;
			ERBPathwayDiagramController nextErbPathwayDiagramController = listOfPathwayDiagramControllers.get(indexOfNextController);
			return nextErbPathwayDiagramController.getContentPanel();
		}
		return null;
	}	

	private void highlightContentPanelDiagram(ContentPanel contentPanel) {
		if (contentPanel != null && contentPanel.getGuid() != null) {
			for (ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
				if (erbPathwayDiagramController.getContentPanel().getGuid().contentEquals(contentPanel.getGuid())) {
					erbPathwayDiagramController.highlightDiagram();
				} else {
					erbPathwayDiagramController.unHighlightDiagram();
				}
			}
		}
	}
	
	private void setContentPanelStatusRadioButton(ContentPanel contentPanel) {
		if (contentPanel != null) {
			if (contentPanel.getStatus().contentEquals("ready")) {
				readyRadioButton.setSelected(true);
			} else if (contentPanel.getStatus().contentEquals("in progress")) {
				inProgressRadioButton.setSelected(true);
			} else if (contentPanel.getStatus().contentEquals("complete")) {
				completeRadioButton.setSelected(true);
			}
		} else {
			logger.error("Cannot setContentPanelStatusRadioButton. ContentPanel = " + contentPanel);
		}
	}

	private void generateChapterERBPathway(Goal goal) {
		if (goal != null) {
			cleanERBPathwayDiagramHBox();
			cleanListOfChapterDiagramControllers();
			for (Chapter chapter : goal.getChapters()) {
				Parent root = loadERBChapterDiagramRoot(chapter, goal.getChapters());
				pathwayTitledPane.setText(goal.getGoalName() + " Pathway");
				if (root != null)
					erbPathwayDiagramHBox.getChildren().add(root);
			}
		} else {
			logger.error("Cannot generateChapterERBPathway. goal = " + goal);
		}
	}

	private void generateActivityERBPathway(Chapter chapter) {
//		if (chapter != null) {
//			cleanERBPathwayDiagramHBox();
//			cleanListOfActivityDiagramControllers();
//			for (Activity activity : chapter.getAssignedActivities()) {
//				Parent root = loadERBPathwayDiagramRoot(activity, chapter);
//				pathwayTitledPane.setText(chapter.getLongName() + " Pathway");
//				if (root != null)
//					erbPathwayDiagramHBox.getChildren().add(root);
//			}
//		} else {
//			logger.error("Cannot generateActivityERBPathway. chapter = " + chapter);
//		}
	}

	public TreeItem<ERBItem> findTreeItem(String guid) {
		if (guid != null) {
			for (ERBItem erbItem : treeItemGuidTreeMap.keySet()) {
				if (erbItem.getGuid() != null && erbItem.getGuid().contentEquals(guid)) {
					return treeItemGuidTreeMap.get(erbItem);
				}
			}
			return null;
		} else {
			logger.error("Cannot findTreeItem. guid = " + guid);
		}
		return null;
	}

	private void cleanERBPathwayDiagramHBox() {
		erbPathwayDiagramHBox.getChildren().clear();
	}

	public void cleanContentVBox() {
		contentVBox.getChildren().clear();
	}

	private void cleanListOfActivityDiagramControllers() {
		listOfPathwayDiagramControllers.clear();
	}

	private void cleanListOfChapterDiagramControllers() {
		listOfChapterDiagramControllers.clear();
	}

	private void addLocalProgressVBox(int index) {
		if (!treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().add(index, localProgressVBox);
		}
	}

	private void removeLocalProgressVBox() {
		if (treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().remove(localProgressVBox);
		}
	}

	private void addERBKeyVBox(int index) {
		if (!headingHBox.getChildren().contains(erbKeyVBox)) {
			headingHBox.getChildren().add(index, erbKeyVBox);
		}
	}

	private void removeERBKeyVBox() {
		headingHBox.getChildren().remove(erbKeyVBox);
	}
	
	public void removeStatusHBox() {
		if (mainVBox.getChildren().contains(statusHBox)) {
			mainVBox.getChildren().remove(statusHBox);
		}
	}

	private void addStatusHBox() {
		if (!mainVBox.getChildren().contains(statusHBox)) {
			mainVBox.getChildren().add(0, statusHBox);
		}
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

	private void removePathwayPane() {
		if (engagementVBox.getChildren().contains(pathwayTitledPane)) {
			engagementVBox.getChildren().remove(pathwayTitledPane);
		}
	}

	private void removeLocalProgressPane() {
		if (treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().remove(localProgressVBox);
		}
	}

	public void closeGlobalGoalTrackerStage() {
		if (globalGoalTrackerStage != null) {
			globalGoalTrackerStage.close();
		}
	}

	public Chapter getCurrentChapter() {
		return currentSelectedChapter;
	}

	public Goal getCurrentGoal() {
		return currentSelectedGoal;
	}

	public void setCurrentSelectedGoal(Goal currentSelectedGoal) {
		this.currentSelectedGoal = currentSelectedGoal;
	}
	public App getApp() {
		return app;
	}

	public ContentPanel getCurrentSelectedContentPanel() {
		return currentSelectedContentPanel;
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

	public HashMap<ERBItem, TreeItem<ERBItem>> getTreeItemIdTreeMap() {
		return treeItemGuidTreeMap;
	}

	public void setTreeItemIdTreeMap(LinkedHashMap<ERBItem, TreeItem<ERBItem>> treeItemIdTreeMap) {
		this.treeItemGuidTreeMap = treeItemIdTreeMap;
	}
	public ArrayList<ERBPathwayDiagramController> getListOfPathwayDiagramControllers() {
		return listOfPathwayDiagramControllers;
	}
	public void setListOfPathwayDiagramControllers(
			ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers) {
		this.listOfPathwayDiagramControllers = listOfPathwayDiagramControllers;
	}
	public Stage getGlobalGoalTrackerStage() {
		return globalGoalTrackerStage;
	}
	public void setGlobalGoalTrackerStage(Stage globalGoalTrackerStage) {
		this.globalGoalTrackerStage = globalGoalTrackerStage;
	}
	public ComboBox<Goal> getGoalComboBox() {
		return goalComboBox;
	}
	public HBox getHeadingHBox() {
		return headingHBox;
	}
	public HBox getErbPathwayDiagramHBox() {
		return erbPathwayDiagramHBox;
	}
	public VBox getErbKeyVBox() {
		return erbKeyVBox;
	}
	public Pane getCompleteKeyPane() {
		return completeKeyPane;
	}
	public Pane getReadyKeyPane() {
		return readyKeyPane;
	}
	public Pane getInProgressKeyPane() {
		return inProgressKeyPane;
	}
	public VBox getTreeViewVBox() {
		return treeViewVBox;
	}
	
	public TreeView<ERBItem> getTreeView() {
		return treeView;
	}
	public VBox getLocalProgressVBox() {
		return localProgressVBox;
	}
	public VBox getMainVBox() {
		return mainVBox;
	}
	public HBox getStatusHBox() {
		return statusHBox;
	}
	public HBox getBody2HBox() {
		return body2HBox;
	}
	public VBox getContentVBox() {
		return contentVBox;
	}
	public Label getGoalProgressPercentLabel() {
		return goalProgressPercentLabel;
	}
	public Label getGoalConfidencePercentLabel() {
		return goalConfidencePercentLabel;
	}
	public VBox getGoalProgressVBox() {
		return goalProgressVBox;
	}
	public VBox getGoalProgressBar() {
		return goalProgressBar;
	}
	public ProgressIndicator getChapterProgressIndicator() {
		return chapterProgressIndicator;
	}
	public Button getPreviousButton() {
		return previousButton;
	}
	public Button getNextButton() {
		return nextButton;
	}
	public ToggleGroup getActivityStatusToggleGroup() {
		return activityStatusToggleGroup;
	}
	public RadioButton getReadyRadioButton() {
		return readyRadioButton;
	}
	public RadioButton getInProgressRadioButton() {
		return inProgressRadioButton;
	}
	public RadioButton getCompleteRadioButton() {
		return completeRadioButton;
	}
	public VBox getEngagementVBox() {
		return engagementVBox;
	}

	public ArrayList<Chapter> getListOfUniqueChapters() {
		return listOfUniqueChapters;
	}

}
