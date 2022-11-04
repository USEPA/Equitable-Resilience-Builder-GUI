package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.ERBItem;
import com.epa.erb.InteractiveActivity;
import com.epa.erb.Step;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.input.MouseEvent;
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
	VBox detailsKeyPaneVBox;
	@FXML
	Pane materialKeyPane;
	@FXML
	Pane descriptionKeyPane;
	@FXML
	Pane whoKeyPane;
	@FXML
	Pane timeKeyPane;
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
	HBox attributePanelHBox;
	@FXML
	Label attributePanelCollapseLabel;
	@FXML
	VBox attributePanelContentVBox;
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
	CheckBox showDetailsCheckBox;
	@FXML
	VBox titledPaneVBox;
	@FXML
	HBox showDetailsHBox;

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
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	private HashMap<ERBItem, TreeItem<ERBItem>> treeItemGuidTreeMap = new HashMap<ERBItem, TreeItem<ERBItem>>();
	private ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers = new ArrayList<ERBPathwayDiagramController>();
	private ArrayList<ERBChapterDiagramController> listOfChapterDiagramControllers = new ArrayList<ERBChapterDiagramController>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		removeShowDetailsCheckBox(); // Default setting for removing spokes
		removeDetailsKeyPaneVBox(); // Default setting for removing spokes
		removeAttributeScrollPane(); // Default setting
		treeView.setCellFactory(tv-> createTreeCell());
		if (project.getProjectType().contentEquals("Goal Mode")) {
			initGoalMode();
		} else {
			initFacilitatorMode();
		}
		app.getErbContainerController().setTitleLabelText("ERB: " + project.getProjectName() + ": " + project.getProjectType());
	}

	private void initGoalMode() {
		fillGoalComboBox(project);
		goalComboBox.setCellFactory(lv -> createGoalCell());
		goalComboBox.setButtonCell(createGoalCell());
		initializeGoalSelection(project);
		setNavigationButtonsDisability(null, null);
		handleControls();
	}

	private void initFacilitatorMode() {
		initializeGoalSelection(project);
		setNavigationButtonsDisability(null, null);
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
		materialKeyPane.setStyle("-fx-background-color: " + constants.getMaterialsColor() + ";");
		descriptionKeyPane.setStyle("-fx-background-color: " + constants.getDescriptionColor() + ";");
		whoKeyPane.setStyle("-fx-background-color: " + constants.getWhoColor() + ";");
		completeKeyPane.setStyle("-fx-background-color: " + constants.getCompleteStatusColor() + ";");
		readyKeyPane.setStyle("-fx-background-color: " + constants.getReadyStatusColor() + ";");
		inProgressKeyPane.setStyle("-fx-background-color: " + constants.getInProgressStatusColor() + ";");
		timeKeyPane.setStyle("-fx-background-color: " + constants.getTimeColor() + ";");
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
		if (currentSelectedActivity != null) {
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
		} else {
			TreeItem<ERBItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			if (selectedTreeItem != null && selectedTreeItem.getValue().getShortName().contains("Chapter")) {
				updateLocalProgress(getChapterForNameInGoal(selectedTreeItem.getValue().getShortName().trim(), currentSelectedGoal),currentSelectedGoal.getChapters());
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
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController(app, project,currentSelectedGoal, dynamicActivity);
			fxmlLoader.setController(noteBoardContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
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

	private Parent loadERBPathwayDiagramRoot(Activity activity, Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBPathwayDiagram.fxml"));
			ERBPathwayDiagramController erbPathwayDiagramController = new ERBPathwayDiagramController(activity, chapter, this);
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
				if (itemClicked.toString().contains("InteractiveActivity")) {
					InteractiveActivity dynamicActivity = (InteractiveActivity) itemClicked;
					dynamicActivity.setStatus(status);
				} else if (itemClicked.toString().contains("Step")) {
					Step step = (Step) itemClicked;
					step.setStatus(status);
				} else if (itemClicked.toString().contains("Activity")) {
					Activity activity = (Activity) itemClicked;
					activity.setStatus(status);
				} else if (itemClicked.toString().contains("Chapter")) {
					Chapter chapter = (Chapter) itemClicked;
				}
			}
			for (ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
				if (erbPathwayDiagramController.getActivity().getGuid().contentEquals(GUIDOfItemClicked)) {
					erbPathwayDiagramController.getActivity().setStatus(status);
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
		TreeItem<ERBItem> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
			treeView.getSelectionModel().select(parentTreeItem.getChildren().get(currentIndex - 1));
			treeViewClicked(null, parentTreeItem.getChildren().get(currentIndex - 1));
		}
	}

	@FXML
	public void nextButtonAction() {
		TreeItem<ERBItem> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<ERBItem> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			if (parentTreeItem.getValue().getShortName().contains("ERB")) {
				treeView.getSelectionModel().select(selectedTreeItem.getChildren().get(0));
				treeViewClicked(null, selectedTreeItem.getChildren().get(0));
			} else {
				int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
				treeView.getSelectionModel().select(parentTreeItem.getChildren().get(currentIndex + 1));
				treeViewClicked(null, parentTreeItem.getChildren().get(currentIndex + 1));
			}
		}
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

	@FXML
	public void attributePanelCollapseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.getSource().toString().contains("VBox")) { // Don't allow for label
			String attributePanelCollapseString = attributePanelCollapseLabel.getText();
			if (attributePanelCollapseString.contentEquals(">") && !attributePanelCollapsed()) {
				collapseAttributePanel();
				attributePanelHBox.setMinWidth(0.0);
				attributePanelCollapseLabel.setText("<");
			} else if (attributePanelCollapseString.contentEquals("<") && attributePanelCollapsed()) {
				unCollapseAttributePanel();
				attributePanelHBox.setMinWidth(200.0);
				attributePanelCollapseLabel.setText(">");
			}
		}
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

	public Chapter getChapterForActivity(Activity activity) {
		for (Chapter chapter : listOfUniqueChapters) {
			for (Activity chapterActivity : chapter.getAssignedActivities()) {
				if (chapterActivity.getGuid().contentEquals(activity.getGuid())) {
					return chapter;
				}
			}
		}
		return null;
	}

	public Chapter getChapterForStep(Step step) {
		for (Chapter chapter : listOfUniqueChapters) {
			for (Step chapterStep : chapter.getAssignedSteps()) {
				if (chapterStep.getGuid().contentEquals(step.getGuid())) {
					return chapter;
				}
			}
		}
		return null;
	}

	public Activity getActivityForStep(Step step) {
		for (Chapter chapter : listOfUniqueChapters) {
			for (Activity chapterActivity : chapter.getAssignedActivities()) {
				for (Step activityStep : chapterActivity.getAssignedSteps()) {
					if (activityStep.getGuid().contentEquals(step.getGuid())) {
						return chapterActivity;
					}
				}
			}
		}
		return null;
	}

	public Step getStepForDynamicActivity(InteractiveActivity dynamicActivity) {
		for (Chapter chapter : listOfUniqueChapters) {
			for (Activity chapterActivity : chapter.getAssignedActivities()) {
				for (Step activityStep : chapterActivity.getAssignedSteps()) {
					for (InteractiveActivity stepDynamicActivity : activityStep.getAssignedDynamicActivities()) {
						if (stepDynamicActivity.getGuid().contentEquals(dynamicActivity.getGuid())) {
							return activityStep;
						}
					}
				}
			}
		}
		return null;
	}

	public Activity getActivityForDynamicActivity(InteractiveActivity dynamicActivity) {
		for (Chapter chapter : listOfUniqueChapters) {
			for (Activity chapterActivity : chapter.getAssignedActivities()) {
				for (InteractiveActivity activityDynamicActivity : chapterActivity.getAssignedDynamicActivities()) {
					if (activityDynamicActivity.getGuid().contentEquals(dynamicActivity.getGuid())) {
						return chapterActivity;
					}
				}
			}
		}
		return null;
	}

	private void erbPathwayTreeItemSelected() {
		currentSelectedDynamicActivity = null;
		currentSelectedStep = null;
		currentSelectedActivity = null;
		currentSelectedChapter = null;
		cleanContentVBox();
		removeStatusHBox();
		removeLocalProgressVBox();
		removeAttributeScrollPane();
		addERBKeyVBox(1);
		generateChapterERBPathway(currentSelectedGoal);
		setNavigationButtonsDisability(null, null);
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
		removeStatusHBox();
		removeAttributeScrollPane();
		addERBKeyVBox(1);
		if (project.getProjectType().contentEquals("Goal Mode"))
			addLocalProgressVBox(1);
		generateActivityERBPathway(currentSelectedChapter);
		setNavigationButtonsDisability(null, null);
		if (currentSelectedChapter != null) {
			File file = fileHandler.getStaticChapterFormAbout(currentSelectedChapter);
			Pane root = loadMainFormController(file);
			addContentToContentVBox(root, true);
		}
		if (currentSelectedChapter != null && currentSelectedGoal != null)
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
	}

	private void activityTreeItemSelected(TreeItem<ERBItem> activityTreeItem, Activity activity) {
		currentSelectedDynamicActivity = null;
		currentSelectedStep = null;
		currentSelectedActivity = activity;
		currentSelectedChapter = getChapterForActivity(activity);
		cleanContentVBox();
		if (project.getProjectType().contentEquals("Goal Mode")) {
			addStatusHBox();
			addLocalProgressVBox(1);
		}
		addERBKeyVBox(1);
		loadActivityContent(currentSelectedActivity);
		generateActivityERBPathway(currentSelectedChapter);
		setActivityStatusRadioButton(currentSelectedActivity);
		highlightActivityDiagram(currentSelectedActivity);
		setNavigationButtonsDisability(activityTreeItem, activityTreeItem.getParent());
		if (currentSelectedChapter != null && currentSelectedGoal != null)
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
	}

	private void stepTreeItemSelected(TreeItem<ERBItem> stepTreeItem, Step step) {
		currentSelectedDynamicActivity = null;
		currentSelectedStep = step;
		currentSelectedActivity = getActivityForStep(step);
		;
		if (currentSelectedActivity != null) {
			currentSelectedChapter = getChapterForActivity(currentSelectedActivity);
		} else {
			currentSelectedChapter = getChapterForStep(step);
		}
		cleanContentVBox();
		if (project.getProjectType().contentEquals("Goal Mode")) {
			if(currentSelectedActivity != null) {
				addStatusHBox();
			} else {
				removeStatusHBox();
			}
			addLocalProgressVBox(1);
		}
		addERBKeyVBox(1);
		Pane root = loadStepContent(currentSelectedStep);
		addContentToContentVBox(root, false);
		generateActivityERBPathway(currentSelectedChapter);
		setActivityStatusRadioButton(currentSelectedActivity);
		highlightActivityDiagram(currentSelectedActivity);
		setNavigationButtonsDisability(stepTreeItem, stepTreeItem.getParent());
		if (currentSelectedChapter != null && currentSelectedGoal != null)
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
	}

	private void dynamicActivitySelected(TreeItem<ERBItem> dynamicActivityTreeItem, InteractiveActivity dynamicActivity) {
		currentSelectedDynamicActivity = dynamicActivity;
		currentSelectedStep = getStepForDynamicActivity(dynamicActivity);
		currentSelectedActivity = getActivityForDynamicActivity(dynamicActivity);
		if (currentSelectedActivity != null) {
			currentSelectedChapter = getChapterForActivity(currentSelectedActivity);
		} else {
			currentSelectedChapter = getChapterForStep(currentSelectedStep);
		}
		cleanContentVBox();
		if (project.getProjectType().contentEquals("Goal Mode")) {
			addStatusHBox();
			addLocalProgressVBox(1);
		}
		addERBKeyVBox(1);
		// --
		loadDynamicActivityContent(dynamicActivity);
		generateActivityERBPathway(currentSelectedChapter);
		setNavigationButtonsDisability(dynamicActivityTreeItem, dynamicActivityTreeItem.getParent());
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
							activityTreeItem.getChildren().add(dynamicActivityTreeItem);
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
							activityTreeItem.getChildren().add(dynamicActivityTreeItem);
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
	
	private TreeCell<ERBItem> createTreeCell(){
		return new TreeCell<ERBItem>() {
			@Override
			protected void updateItem(ERBItem item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getLongName());
				}
			}
		};
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
				Pane root = loadOutputFormController(file);
				return root;
			}
		} else {
			logger.error("Cannot loadStepContent. step = " + step);
		}
		return null;
	}

	private VBox loadOutputFormController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/OutputForm.fxml"));
			OutputFormController outputFormController = new OutputFormController(app, xmlContentFileToParse, this);
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

	public Activity retrieveNextActivity(ERBPathwayDiagramController currentErbPathwayDiagramController) {
		int indexOfCurrentController = listOfPathwayDiagramControllers.indexOf(currentErbPathwayDiagramController);
		if (indexOfCurrentController >= 0) {
			int indexOfNextController = indexOfCurrentController + 1;
			ERBPathwayDiagramController nextErbPathwayDiagramController = listOfPathwayDiagramControllers.get(indexOfNextController);
			return nextErbPathwayDiagramController.getActivity();
		}
		return null;
	}

	private void highlightActivityDiagram(Activity activity) {
		if(activity != null && activity.getGuid() != null) {
		for (ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
			if (erbPathwayDiagramController.getActivity().getGuid().contentEquals(activity.getGuid())) {
				erbPathwayDiagramController.highlightDiagram();
			} else {
				erbPathwayDiagramController.unHighlightDiagram();
			}
		}
		}
	}

	private void setActivityStatusRadioButton(Activity activity) {
		if (activity != null) {
			if (activity.getStatus().contentEquals("ready")) {
				readyRadioButton.setSelected(true);
			} else if (activity.getStatus().contentEquals("in progress")) {
				inProgressRadioButton.setSelected(true);
			} else if (activity.getStatus().contentEquals("complete")) {
				completeRadioButton.setSelected(true);
			}
		} else {
			logger.error("Cannot setActivityStatusRadioButton. activity = " + activity);
		}
	}

	private void generateChapterERBPathway(Goal goal) {
		if (goal != null) {
			cleanERBPathwayDiagramHBox();
			cleanListOfChapterDiagramControllers();
			removeShowDetailsCheckBox();
			removeDetailsKeyPaneVBox();
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
		if (chapter != null) {
			cleanERBPathwayDiagramHBox();
			cleanListOfActivityDiagramControllers();
			for (Activity activity : chapter.getAssignedActivities()) {
				Parent root = loadERBPathwayDiagramRoot(activity, chapter);
				pathwayTitledPane.setText(chapter.getLongName() + " Pathway");
				if (root != null)
					erbPathwayDiagramHBox.getChildren().add(root);
			}
		} else {
			logger.error("Cannot generateActivityERBPathway. chapter = " + chapter);
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

	private void setNavigationButtonsDisability(TreeItem<ERBItem> selectedTreeItem, TreeItem<ERBItem> parentTreeItem) {
		if (selectedTreeItem != null) { // if activity is not null
			if (parentTreeItem == null) { // if erb pathway
				previousButton.setDisable(true);
				nextButton.setDisable(false);
			} else {
				int numberOfChildrenInParent = parentTreeItem.getChildren().size();
				if (numberOfChildrenInParent > 1) { // if more than 1 activity
					int indexOfSelectedItem = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					if (indexOfSelectedItem == 0) { // if first activity
						previousButton.setDisable(true);
						nextButton.setDisable(false);
					} else if (indexOfSelectedItem == parentTreeItem.getChildren().size() - 1) { // if last activity
						previousButton.setDisable(false);
						nextButton.setDisable(true);
					} else {
						previousButton.setDisable(false);
						nextButton.setDisable(false);
					}
				} else { // if only 1 activity
					previousButton.setDisable(true);
					nextButton.setDisable(true);
				}
			}
		} else { // if activity is null
			previousButton.setDisable(true);
			nextButton.setDisable(true);
		}
	}

	private void collapseAttributePanel() {
		if (attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().remove(attributePanelContentVBox);
		}
	}

	private void unCollapseAttributePanel() {
		if (!attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().add(1, attributePanelContentVBox);
		}
	}

	private boolean attributePanelCollapsed() {
		if (!attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			return true;
		} else {
			return false;
		}
	}

	private void cleanAttributePanelContentVBox() {
		attributePanelContentVBox.getChildren().clear();
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

	private void removeDetailsKeyPaneVBox() {
		keyPaneHBox.getChildren().remove(detailsKeyPaneVBox);
	}

	private void removeShowDetailsCheckBox() {
		titledPaneVBox.getChildren().remove(showDetailsHBox);
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

	private void removeAttributeScrollPane() {
		body2HBox.getChildren().remove(attributePanelHBox);
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

	public Chapter getChapterForNameInGoal(String chapterName, Goal goal) {
		if (chapterName != null && goal != null) {
			for (Chapter chapter : goal.getChapters()) {
				if (chapter.getLongName().contentEquals(chapterName)) {
					return chapter;
				}
			}
			logger.debug("Chapter returned is null");
			return null;
		} else {
			logger.error("Cannot getChapterForNameInGoal. chapterName or goal is null.");
			return null;
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

	public void setTreeItemIdTreeMap(HashMap<ERBItem, TreeItem<ERBItem>> treeItemIdTreeMap) {
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

	public Pane getMaterialKeyPane() {
		return materialKeyPane;
	}

	public Pane getDescriptionKeyPane() {
		return descriptionKeyPane;
	}

	public Pane getWhoKeyPane() {
		return whoKeyPane;
	}

	public Pane getTimeKeyPane() {
		return timeKeyPane;
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

	public HBox getAttributePanelHBox() {
		return attributePanelHBox;
	}

	public Label getAttributePanelCollapseLabel() {
		return attributePanelCollapseLabel;
	}

	public VBox getAttributePanelContentVBox() {
		return attributePanelContentVBox;
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

	public CheckBox getShowDetailsCheckBox() {
		return showDetailsCheckBox;
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
	
//-----------------------------Keep for attribute panel-----------------------------------------
//	private Parent loadAttributePaneRoot(String attributeTitle, String attributeContent, String attributeColor) {
//		try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/AttributePane.fxml"));
//			AttributePanelController attributePanelController = new AttributePanelController(attributeTitle, attributeContent, attributeColor, this);
//			listOfAttributePanelControllers.add(attributePanelController);
//			fxmlLoader.setController(attributePanelController);
//			return fxmlLoader.load();
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return null;
//		}
//	}
//	private boolean attributePanelContainsAttribute(String attributeLabel) {
//		if (attributeLabel != null) {
//			for (AttributePanelController attributePanelController : listOfAttributePanelControllers) {
//				if (attributePanelController.getAttributeLabelText().contentEquals(attributeLabel)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//	private void addBaseAttributesToAttributePanel(Activity activity) {
//		// Example: generateAttribute("Objective", activity.getObjectives(),
//	}
//	void generateAttribute(String attributeTitle, String attributeContent, String attributeColor) {
//		if (attributeContent!= null && attributeContent.trim().length() > 0 && !attributePanelContainsAttribute(attributeTitle)) {
//			Parent root = loadAttributePaneRoot(attributeTitle, attributeContent, attributeColor);
//			attributePanelContentVBox.getChildren().add(root);
//			VBox.setVgrow(root, Priority.ALWAYS);
//		}
//	}
}
