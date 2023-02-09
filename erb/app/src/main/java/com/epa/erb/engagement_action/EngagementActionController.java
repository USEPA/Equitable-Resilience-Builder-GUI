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
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.ERBItem;
import com.epa.erb.ERBItemFinder;
import com.epa.erb.InteractiveActivity;
import com.epa.erb.Step;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.forms.AlternativeFormController;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.forms.OutputFormController;
import com.epa.erb.goal.Goal;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import com.epa.erb.wordcloud.WordCloudController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EngagementActionController implements Initializable {

	@FXML
	VBox engagementVBox;
	@FXML
	HBox saveHBox;
	@FXML
	Label goalLabel;
	@FXML
	ComboBox<Goal> goalComboBox;
	@FXML
	Button previousButton;
	@FXML
	Button nextButton;
	@FXML
	VBox treeViewVBox;
	@FXML
	TreeView<ERBItem> treeView;
	@FXML
	VBox mainVBox;
	@FXML
	HBox body2HBox;
	@FXML
	VBox contentVBox;


	private App app;
	private Project project;
	public EngagementActionController(App app, Project project) {
		this.app = app;
		this.project = project;
	}
	ArrayList<Chapter> listOfUniqueChapters = new ArrayList<Chapter>();
	
	private Goal currentSelectedGoal = null;
	private Step currentSelectedStep = null;
	private Chapter currentSelectedChapter = null;
	private Activity currentSelectedActivity = null;
	private InteractiveActivity currentSelectedDynamicActivity = null;

	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	private LinkedHashMap<ERBItem, TreeItem<ERBItem>> treeItemGuidTreeMap = new LinkedHashMap<ERBItem, TreeItem<ERBItem>>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		treeView.setCellFactory(tv-> createTreeCell());
		if (project.getProjectType().contentEquals("Goal Mode")) {
		} else {
			initFacilitatorMode();
		}
		treeView.getStylesheets().add(getClass().getResource("/treeView.css").toString());
	}

	private void initFacilitatorMode() {
		initializeGoalSelection(project);
		addTreeViewListener();
		hideGoalSelection();
		removeSaveHBox();
	}

	private void addTreeViewListener() {
		treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> treeViewClicked(oldValue, newValue));
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

	private VBox loadNoteBoardContentController(InteractiveActivity dynamicActivity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController(dynamicActivity.getId(), dynamicActivity.getGuid(), dynamicActivity.getLongName(), dynamicActivity.getShortName(), dynamicActivity.getStatus(), app, project,currentSelectedGoal);
			fxmlLoader.setController(noteBoardContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private VBox loadWordCloudController(InteractiveActivity dynamicActivity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wordcloud/WordCloud.fxml"));
			WordCloudController wordCloudController = new WordCloudController(dynamicActivity.getId(), dynamicActivity.getGuid(), dynamicActivity.getLongName(), dynamicActivity.getShortName(), dynamicActivity.getStatus(), app, project, currentSelectedGoal);
			fxmlLoader.setController(wordCloudController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
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
	
	public void treeViewClicked(TreeItem<ERBItem> oldItem, TreeItem<ERBItem> newItem) {
		if (newItem != null) {			
			Object itemClicked = newItem.getValue().getObject();
			if (itemClicked != null) {
				if (itemClicked.toString().contains("InteractiveActivity")) {
					InteractiveActivity dynamicActivity = (InteractiveActivity) itemClicked;
					dynamicActivitySelected(newItem, dynamicActivity);
				} else if (itemClicked.toString().contains("Step")) {
					Step step = (Step) itemClicked;
					stepTreeItemSelected(newItem, step);
				} else if (itemClicked.toString().contains("Activity")) {
					Activity activity = (Activity) itemClicked;
					activityTreeItemSelected(newItem, activity);
				} else if (itemClicked.toString().contains("Chapter")) {
					Chapter chapter = (Chapter) itemClicked;
					chapterTreeItemSelected(newItem, chapter);
				}
			} else {
				erbPathwayTreeItemSelected();
			}
		} else {

		}
	}

	private void erbPathwayTreeItemSelected() {
		currentSelectedDynamicActivity = null;
		currentSelectedStep = null;
		currentSelectedActivity = null;
		currentSelectedChapter = null;
		cleanContentVBox();
		if (currentSelectedGoal != null) {
			Pane root = loadChapterLanding(listOfUniqueChapters);
			addContentToContentVBox(root, true);
		}
	}

	private void chapterTreeItemSelected(TreeItem<ERBItem> chapterTreeItem, Chapter chapter) {
		currentSelectedDynamicActivity = null;
		currentSelectedStep = null;
		currentSelectedActivity = null;
		currentSelectedChapter = chapter;
		cleanContentVBox();
		if (currentSelectedChapter != null) {
			File file = fileHandler.getStaticChapterFormAbout(currentSelectedChapter);
			Pane root = loadMainFormController(file);
			addContentToContentVBox(root, true);
		}
	}

	private void activityTreeItemSelected(TreeItem<ERBItem> activityTreeItem, Activity activity) {
		currentSelectedDynamicActivity = null;
		currentSelectedStep = null;
		currentSelectedActivity = activity;
		currentSelectedChapter = erbItemFinder.getChapterForActivity(listOfUniqueChapters, activity);

		cleanContentVBox();
		loadActivityContent(currentSelectedActivity);
		setActivityStatusRadioButton(currentSelectedActivity);
	}

	private void stepTreeItemSelected(TreeItem<ERBItem> stepTreeItem, Step step) {
		currentSelectedDynamicActivity = null;
		currentSelectedStep = step;
		currentSelectedActivity = erbItemFinder.getActivityForStep(listOfUniqueChapters, step);

		;
		if (currentSelectedActivity != null) {
			currentSelectedChapter = erbItemFinder.getChapterForActivity(listOfUniqueChapters, currentSelectedActivity);
		} else {
			currentSelectedChapter = erbItemFinder.getChapterForStep(listOfUniqueChapters, step);
		}
		cleanContentVBox();
		Pane root = loadStepContent(currentSelectedStep);
		addContentToContentVBox(root, false);
		setActivityStatusRadioButton(currentSelectedActivity);
	}

	private void dynamicActivitySelected(TreeItem<ERBItem> dynamicActivityTreeItem, InteractiveActivity dynamicActivity) {
		currentSelectedDynamicActivity = dynamicActivity;
		currentSelectedStep = erbItemFinder.getStepForInteractiveActivity(listOfUniqueChapters, dynamicActivity);
		currentSelectedActivity = erbItemFinder.getActivityForInteractiveActivity(listOfUniqueChapters, dynamicActivity);

		if (currentSelectedActivity != null) {
			currentSelectedChapter = erbItemFinder.getChapterForActivity(listOfUniqueChapters, currentSelectedActivity);
		} else {
			currentSelectedChapter = erbItemFinder.getChapterForStep(listOfUniqueChapters, currentSelectedStep);

		}
		cleanContentVBox();
		// --
		loadDynamicActivityContent(dynamicActivity);
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

	private void fillAndStoreTreeViewData_GUIDS(ArrayList<Chapter> uniqueChapters) { // HAS GUIDS
		if (uniqueChapters != null) {
			treeItemGuidTreeMap.clear();

			ERBItem rootErbItem = new ERBItem("004", null, "ERB Pathway" , "ERB Pathway", null);
			TreeItem<ERBItem> rootTreeItem = new TreeItem<ERBItem>(rootErbItem);
			rootTreeItem.setExpanded(true);
			treeView.setRoot(rootTreeItem);
			treeItemGuidTreeMap.put(rootErbItem, rootTreeItem);

			// CHAPTER
			for (Chapter uniqueChapter : uniqueChapters) {
				ERBItem chapterERBItem = new ERBItem(uniqueChapter.getId(), uniqueChapter.getGuid(), uniqueChapter.getLongName(), uniqueChapter.getShortName(), uniqueChapter);
				TreeItem<ERBItem> chapterTreeItem = new TreeItem<ERBItem>(chapterERBItem);
				rootTreeItem.getChildren().add(chapterTreeItem);
				treeItemGuidTreeMap.put(chapterERBItem, chapterTreeItem);

				// CHAPTER -> STEP
				for (Step uniqueStep : uniqueChapter.getAssignedSteps()) {
					ERBItem chapterStepERBItem = new ERBItem(uniqueStep.getId(), uniqueStep.getGuid(), uniqueStep.getLongName(), uniqueStep.getShortName(), uniqueStep);
					TreeItem<ERBItem> chapterStepTreeItem = new TreeItem<ERBItem>(chapterStepERBItem);
					if (uniqueStep.getId().length() != 4) {
						chapterTreeItem.getChildren().add(chapterStepTreeItem);
						treeItemGuidTreeMap.put(chapterStepERBItem, chapterStepTreeItem);
					}
					
					// CHAPTER -> STEP -> DYNAMIC ACTIVITY
					for (InteractiveActivity uniqueDynamicActivity : uniqueStep.getAssignedDynamicActivities()) {
						ERBItem dynamicActivityERBItem = new ERBItem(uniqueDynamicActivity.getId(), uniqueDynamicActivity.getGuid(), uniqueDynamicActivity.getLongName(), uniqueDynamicActivity.getShortName(), uniqueDynamicActivity);
						TreeItem<ERBItem> dynamicActivityTreeItem = new TreeItem<ERBItem>(dynamicActivityERBItem);
						chapterStepTreeItem.getChildren().add(dynamicActivityTreeItem);
						treeItemGuidTreeMap.put(dynamicActivityERBItem, dynamicActivityTreeItem);
					} // CHAPTER STEP DYYNAMIC ACTIVITY
				} // CHAPTER STEP

				// CHAPTER -> ACTIVITY
				for (Activity uniqueActivity : uniqueChapter.getAssignedActivities()) {
					ERBItem activityERBItem = new ERBItem(uniqueActivity.getId(), uniqueActivity.getGuid(), uniqueActivity.getLongName(), uniqueActivity.getShortName(), uniqueActivity);
					TreeItem<ERBItem> activityTreeItem = new TreeItem<ERBItem>(activityERBItem);
					chapterTreeItem.getChildren().add(activityTreeItem);
					treeItemGuidTreeMap.put(activityERBItem, activityTreeItem);
					
					// CHAPTER -> ACTIVITY -> STEP
					for (Step uniqueStep : uniqueActivity.getAssignedSteps()) {
						ERBItem stepERBItem = new ERBItem(uniqueStep.getId(), uniqueStep.getGuid(), uniqueStep.getLongName(), uniqueStep.getShortName(), uniqueStep);
						TreeItem<ERBItem> stepTreeItem = new TreeItem<ERBItem>(stepERBItem);
						if (uniqueStep.getId().length() != 4) {
							activityTreeItem.getChildren().add(stepTreeItem);
							treeItemGuidTreeMap.put(stepERBItem, stepTreeItem);
						}
						
						// CHAPTER -> ACTIVITY -> STEP -> DYNAMIC ACTIVITY
						for (InteractiveActivity uniqueDynamicActivity : uniqueStep.getAssignedDynamicActivities()) {
							ERBItem dynamicActivityERBItem = new ERBItem(uniqueDynamicActivity.getId(), uniqueDynamicActivity.getGuid(), uniqueDynamicActivity.getLongName(), uniqueDynamicActivity.getShortName(), uniqueDynamicActivity);
							TreeItem<ERBItem> dynamicActivityTreeItem = new TreeItem<ERBItem>(dynamicActivityERBItem);
							stepTreeItem.getChildren().add(dynamicActivityTreeItem);
							treeItemGuidTreeMap.put(dynamicActivityERBItem, dynamicActivityTreeItem);
						} // CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY
					} // CHAPTER ACTIVITY STEP

					// CHAPTER -> ACTIIVTY -> DYNAMIC ACTIVITY
					for (InteractiveActivity uniqueDynamicActivity : uniqueActivity.getAssignedDynamicActivities()) {
						ERBItem dynamicActivityERBItem = new ERBItem(uniqueDynamicActivity.getId(), uniqueDynamicActivity.getGuid(), uniqueDynamicActivity.getLongName(), uniqueDynamicActivity.getShortName(), uniqueDynamicActivity);
						TreeItem<ERBItem> dynamicActivityTreeItem = new TreeItem<ERBItem>(dynamicActivityERBItem);
						activityTreeItem.getChildren().add(dynamicActivityTreeItem);
						treeItemGuidTreeMap.put(dynamicActivityERBItem, dynamicActivityTreeItem);
					} // CHAPTER ACTIVITY DYNAMIC ACTIVITY
				} // CHAPTER ACTIVITY
				listOfUniqueChapters.add(uniqueChapter);
			} // CHAPTER
		} else {
			logger.error("Cannot fillAndStoreTreeViewData. chapters = " + uniqueChapters);
		}
	}
	
	private void fillAndStoreTreeViewData(ArrayList<Chapter> genericChapters) { // Does not have GUIDS
		if (genericChapters != null) {
			treeItemGuidTreeMap.clear();
			ERBItem rootErbItem = new ERBItem("004", null, "ERB Pathway" , "ERB Pathway", null);
			TreeItem<ERBItem> rootTreeItem = new TreeItem<ERBItem>(rootErbItem);
			rootTreeItem.setExpanded(true);
			treeView.setRoot(rootTreeItem);
			treeItemGuidTreeMap.put(rootErbItem, rootTreeItem);

			// CHAPTER
			for (Chapter chapter : genericChapters) {
				String chapterGUID = app.generateGUID();
				Chapter uniqueChapter = new Chapter(chapter.getId(), chapterGUID, chapter.getLongName(), chapter.getShortName(), chapter.getStatus(), chapter.getNotes(), chapter.getDescription(), chapter.getNumber());
				ERBItem chapterERBItem = new ERBItem(uniqueChapter.getId(), uniqueChapter.getGuid(), uniqueChapter.getLongName(), uniqueChapter.getShortName(), uniqueChapter);
				TreeItem<ERBItem> chapterTreeItem = new TreeItem<ERBItem>(chapterERBItem);
				rootTreeItem.getChildren().add(chapterTreeItem);
				treeItemGuidTreeMap.put(chapterERBItem, chapterTreeItem);
				
				// CHAPTER -> STEP
				for (Step step : chapter.getAssignedSteps()) {
					String stepGUID = app.generateGUID();
					Step uniqueStep = new Step(step.getId(), stepGUID, step.getLongName(), step.getShortName(), step.getStatus(), step.getNotes(), step.getRating(), step.getType());
					ERBItem chapterStepERBItem = new ERBItem(uniqueStep.getId(), uniqueStep.getGuid(), uniqueStep.getLongName(), uniqueStep.getShortName(), uniqueStep);
					TreeItem<ERBItem> chapterStepTreeItem = new TreeItem<ERBItem>(chapterStepERBItem);
					if(uniqueStep.getId().length() != 4) {
						chapterTreeItem.getChildren().add(chapterStepTreeItem);
						treeItemGuidTreeMap.put(chapterStepERBItem, chapterStepTreeItem);
					}
					
					// CHAPTER -> STEP -> DYNAMIC ACTIVITY
					for (InteractiveActivity dynamicActivity : step.getAssignedDynamicActivities()) {
						String dynamicActivityGUID = app.generateGUID();
						InteractiveActivity uniqueDynamicActivity = new InteractiveActivity(dynamicActivity.getId(), dynamicActivityGUID, dynamicActivity.getLongName(), dynamicActivity.getShortName(), dynamicActivity.getStatus());
						ERBItem dynamicActivityERBItem = new ERBItem(uniqueDynamicActivity.getId(), uniqueDynamicActivity.getGuid(), uniqueDynamicActivity.getLongName(), uniqueDynamicActivity.getShortName(), uniqueDynamicActivity);
						TreeItem<ERBItem> dynamicActivityTreeItem = new TreeItem<ERBItem>(dynamicActivityERBItem);
						chapterStepTreeItem.getChildren().add(dynamicActivityTreeItem);
						treeItemGuidTreeMap.put(dynamicActivityERBItem, dynamicActivityTreeItem);
						uniqueStep.addDynamicActivity(uniqueDynamicActivity);
					}//CHAPTER STEP DYNAMIC ACTIVITY
					uniqueChapter.addStep(uniqueStep);
				}//CHAPTER STEP

				// CHAPTER -> ACTIVITY
				for (Activity activity : chapter.getAssignedActivities()) {
					String activityGUID = app.generateGUID();
					Activity uniqueActivity = new Activity(activity.getId(), activityGUID, activity.getLongName(), activity.getShortName(), activity.getStatus(), activity.getNotes(), activity.getRating());
					ERBItem activityERBItem = new ERBItem(uniqueActivity.getId(), uniqueActivity.getGuid(), uniqueActivity.getLongName(), uniqueActivity.getShortName(), uniqueActivity);
					TreeItem<ERBItem> activityTreeItem = new TreeItem<ERBItem>(activityERBItem);
					chapterTreeItem.getChildren().add(activityTreeItem);
					treeItemGuidTreeMap.put(activityERBItem, activityTreeItem);
					
					// CHAPTER -> ACTIVITY -> STEP
					for (Step step : activity.getAssignedSteps()) {
						String stepGUID = app.generateGUID();
						Step uniqueStep = new Step(step.getId(), stepGUID, step.getLongName(), step.getShortName(), step.getStatus(), step.getNotes(), step.getRating(), step.getType());
						ERBItem stepERBItem = new ERBItem(uniqueStep.getId(), uniqueStep.getGuid(), uniqueStep.getLongName(), uniqueStep.getShortName(), uniqueStep);
						TreeItem<ERBItem> stepTreeItem = new TreeItem<ERBItem>(stepERBItem);
						if(uniqueStep.getId().length() != 4) {
							activityTreeItem.getChildren().add(stepTreeItem);
							treeItemGuidTreeMap.put(stepERBItem, stepTreeItem);
						}
						
						// CHAPTER -> ACTIVITY -> STEP -> DYNAMIC ACTIVITY
						for (InteractiveActivity dynamicActivity : step.getAssignedDynamicActivities()) {
							String dynamicActivityGUID = app.generateGUID();
							InteractiveActivity uniqueDynamicActivity = new InteractiveActivity(dynamicActivity.getId(), dynamicActivityGUID, dynamicActivity.getLongName(), dynamicActivity.getShortName(), dynamicActivity.getStatus());
							ERBItem dynamicActivityERBItem = new ERBItem(uniqueDynamicActivity.getId(), uniqueDynamicActivity.getGuid(), uniqueDynamicActivity.getLongName(), uniqueDynamicActivity.getShortName(), uniqueDynamicActivity);
							TreeItem<ERBItem> dynamicActivityTreeItem = new TreeItem<ERBItem>(dynamicActivityERBItem);
							stepTreeItem.getChildren().add(dynamicActivityTreeItem);
							treeItemGuidTreeMap.put(dynamicActivityERBItem, dynamicActivityTreeItem);
							uniqueStep.addDynamicActivity(uniqueDynamicActivity);
						}//CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY
						uniqueActivity.addStep(uniqueStep);
					}//CHAPTER ACTIVITY STEP

					// CHAPTER -> ACTIIVTY -> DYNAMIC ACTIVITY
					for (InteractiveActivity dynamicActivity : activity.getAssignedDynamicActivities()) {
						String dynamicActivityGUID = app.generateGUID();
						InteractiveActivity uniqueDynamicActivity = new InteractiveActivity(dynamicActivity.getId(), dynamicActivityGUID, dynamicActivity.getLongName(), dynamicActivity.getShortName(), dynamicActivity.getStatus());
						ERBItem dynamicActivityERBItem = new ERBItem(uniqueDynamicActivity.getId(), uniqueDynamicActivity.getGuid(), uniqueDynamicActivity.getLongName(), uniqueDynamicActivity.getShortName(), uniqueDynamicActivity);
						TreeItem<ERBItem> dynamicActivityTreeItem = new TreeItem<ERBItem>(dynamicActivityERBItem);
						activityTreeItem.getChildren().add(dynamicActivityTreeItem);
						treeItemGuidTreeMap.put(dynamicActivityERBItem, dynamicActivityTreeItem);
						uniqueActivity.addDynamicActivity(uniqueDynamicActivity);
					}//CHAPTER ACTIVITY DYNAMIC ACTIVITY
					uniqueChapter.addActivity(uniqueActivity);
				}//CHAPTER ACTIVITY
				listOfUniqueChapters.add(uniqueChapter);
			}//CHAPTER
		} else {
			logger.error("Cannot fillAndStoreTreeViewData. chapters = " + genericChapters);
		}
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
					String color = getTextColorForERBItem(item);
					if (color != null) {
						setStyle("-fx-text-fill: " + color);
					}else {
						setStyle("-fx-text-fill: black");
					}
				}
			}
		};
	}
	
	private String getTextColorForERBItem(ERBItem erbItem) {
		Chapter chapter = erbItemFinder.findChapterForGuid(listOfUniqueChapters, erbItem.getGuid());
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
				if(hasGUIDs) {
					fillAndStoreTreeViewData_GUIDS(listOfChapters); //LIST OF CHAPTERS IS ALREADY UNIQUE
				} else {
					fillAndStoreTreeViewData(listOfChapters); //LIST OF CHAPTERS IS NOT UNIQUE
				}
				initializeTreeViewSelection();
			}
		} else {
			logger.error("Cannot execute goalSelected. goal = " + goal);
		}
	}

	private void loadActivityContent(Activity activity) {
		if (activity != null) {
			if (project.getProjectType().contentEquals("Facilitator Mode")) {
				loadActivityContentFacilitatorMode(activity);
			} else {
				loadActivityContentGoalMode(activity);
			}
		} else {
			logger.error("Cannot loadActivityContent. activity = " + activity);
		}
	}

	private void loadActivityContentFacilitatorMode(Activity activity) {
		File file = fileHandler.getStaticActivityFormText(activity);
		Pane root = loadMainFormController(file);
		addContentToContentVBox(root, false);
	}

	private void loadActivityContentGoalMode(Activity activity) {
		File file = fileHandler.getStaticActivityFormText(activity);
		Pane root = loadMainFormController(file);
		addContentToContentVBox(root, false);

	}

	private void loadDynamicActivityContent(InteractiveActivity dynamicActivity) {
		if (dynamicActivity != null) {
			//TODO: Somehow make this not static
			if (dynamicActivity.getId().contentEquals("00000001")) {
				Pane root = loadNoteBoardContentController(dynamicActivity);
				addContentToContentVBox(root, false);
			} else if (dynamicActivity.getId().contentEquals("00000002")) {
				Pane root = loadWordCloudController(dynamicActivity);
				addContentToContentVBox(root, false);
			}
		} else {
			logger.error("Cannot loadDynamicActivityContent. InteractiveActivity = " + dynamicActivity);

		}
	}

	public Pane loadStepContent(Step step) {
		if (step != null) {
			File file = fileHandler.getStaticStepFormText(step);
			if (step.getType().contentEquals("mainForm")) {
				Pane root = loadMainFormController(file);
				return root;
			} else if (step.getType().contentEquals("outputForm")) {
				Pane root = loadOutputFormController(file, step);
				return root;
			} else if (step.getType().contentEquals("alternativeForm")) {
				Pane root = loadAlternativeFormController(file, step);
				return root;
			}
		} else {
			logger.error("Cannot loadStepContent. step = " + step);
		}
		return null;
	}
	
	private VBox loadAlternativeFormController(File xmlFileToParse, Step step) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/AlternativeForm.fxml"));
			AlternativeFormController alternativeFormController = new AlternativeFormController(app, xmlFileToParse, this);
			fxmlLoader.setController(alternativeFormController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private VBox loadOutputFormController(File xmlContentFileToParse, Step step) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/OutputForm.fxml"));
			OutputFormController outputFormController = new OutputFormController(step.getId(), step.getGuid(), step.getLongName(), step.getShortName(), step.getStatus(),app, xmlContentFileToParse, this);
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

	private void setActivityStatusRadioButton(Activity activity) {
		if (activity != null) {
			if (activity.getStatus().contentEquals("ready")) {
			} else if (activity.getStatus().contentEquals("in progress")) {
			} else if (activity.getStatus().contentEquals("complete")) {
			}
		} else {
			logger.error("Cannot setActivityStatusRadioButton. activity = " + activity);
		}
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

	public Chapter getCurrentChapter() {
		return currentSelectedChapter;
	}

	public Activity getCurrentActivity() {
		return currentSelectedActivity;
	}

	public Goal getCurrentGoal() {
		return currentSelectedGoal;
	}

	public void setCurrentSelectedGoal(Goal currentSelectedGoal) {
		this.currentSelectedGoal = currentSelectedGoal;
	}

	public Step getCurrentSelectedStep() {
		return currentSelectedStep;
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

	public HashMap<ERBItem, TreeItem<ERBItem>> getTreeItemIdTreeMap() {
		return treeItemGuidTreeMap;
	}

	public void setTreeItemIdTreeMap(LinkedHashMap<ERBItem, TreeItem<ERBItem>> treeItemIdTreeMap) {
		this.treeItemGuidTreeMap = treeItemIdTreeMap;
	}

	public ComboBox<Goal> getGoalComboBox() {
		return goalComboBox;
	}
	public VBox getTreeViewVBox() {
		return treeViewVBox;
	}
	
	public TreeView<ERBItem> getTreeView() {
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

	public ArrayList<Chapter> getListOfUniqueChapters() {
		return listOfUniqueChapters;
	}

	public InteractiveActivity getCurrentSelectedDynamicActivity() {
		return currentSelectedDynamicActivity;
	}

	public void setCurrentSelectedDynamicActivity(InteractiveActivity currentSelectedDynamicActivity) {
		this.currentSelectedDynamicActivity = currentSelectedDynamicActivity;
	}

}
