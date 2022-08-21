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
import com.epa.erb.Constants;
import com.epa.erb.Progress;
import com.epa.erb.XMLManager;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.chapter.ChapterLandingController;
import com.epa.erb.chapter.PlanFacilitatorModeController;
import com.epa.erb.chapter.PlanGoalModeController;
import com.epa.erb.chapter.ReflectFacilitatorModeController;
import com.epa.erb.chapter.ReflectGoalModeController;
import com.epa.erb.form_activities.Controller14;
import com.epa.erb.goal.GlobalGoalTrackerController;
import com.epa.erb.goal.Goal;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.project.Project;
import com.epa.erb.worksheet.WorksheetContentController;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EngagementActionController implements Initializable{

	@FXML
	VBox engagementVBox;
	@FXML
	Label goalLabel;
	@FXML
	ComboBox<Goal> goalComboBox;
	@FXML
	HBox headingHBox;
	@FXML
	TitledPane pathwayTitledPane;
	@FXML
	HBox erbPathwayDiagramHBox;
	@FXML
	VBox erbPathwayVBox;
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
	TreeView<String> treeView;
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
	VBox goalConfidenceVBox;
	@FXML
	VBox goalConfidenceBar;
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
	
	private Goal currentSelectedGoal = null;
	private Chapter currentSelectedChapter = null; 
	private Activity currentSelectedActivity = null;
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	private HashMap<TreeItem<String>, String> treeItemActivityIdTreeMap = new HashMap<TreeItem<String>, String>(); //holds the tree items mapped to a chapter or activity GUID
	private ArrayList<AttributePanelController> listOfAttributePanelControllers = new ArrayList<AttributePanelController>(); //holds all of the attribute panels
	private ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers = new ArrayList<ERBPathwayDiagramController>(); //holds all of the pathway controllers
	private ArrayList<ERBChapterDiagramController> listOfChapterDiagramControllers = new ArrayList<ERBChapterDiagramController>(); //holds all of the chapter controllers
	private ArrayList<WorksheetContentController> listOfAllWorksheetContentControllers = new ArrayList<WorksheetContentController>();
	private ArrayList<NoteBoardContentController> listOfAllNoteBoardContentControllers = new ArrayList<NoteBoardContentController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(project.getProjectType().contentEquals("Goal Mode")) {
			fillGoalComboBox(project);
			goalComboBox.setCellFactory(lv-> createGoalCell());
			goalComboBox.setButtonCell(createGoalCell());
			initializeGoalSelection(project);
			setNavigationButtonsDisability(null, null);
			handleControls();
		} else {
			initializeGoalSelection(project);
			setNavigationButtonsDisability(null, null);

			addTreeViewListener();
			
			hideGoalSelection();
			removePathwayPane();
			removeLocalProgressPane();
		}
		app.getErbContainerController().setTitleLabelText("ERB: " + project.getProjectName() + ": " + project.getProjectType());
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
		localProgressVBox.heightProperty().addListener(e-> localProgressPaneSizeChanged());
		localProgressVBox.widthProperty().addListener(e-> localProgressPaneSizeChanged());
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
		if(currentSelectedActivity != null) {
			updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
		} else {
			TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			if(selectedTreeItem != null && selectedTreeItem.getValue().contains("Chapter")) {
				updateLocalProgress(getChapterForNameInGoal(selectedTreeItem.getValue().trim(), currentSelectedGoal), currentSelectedGoal.getChapters());
			}
		}
	}
	
	private void initializeGoalSelection(Project project) {
		if (project != null) {
			goalComboBox.getSelectionModel().select(0);
			goalSelected(project.getProjectGoals().get(0));
		} else {
			logger.error("Cannot initializeGoalSelection. project is null.");
		}
	}
	
	private void initializeTreeViewSelection() {
		for (TreeItem<String> treeItem : treeItemActivityIdTreeMap.keySet()) {
			if (treeItemActivityIdTreeMap.get(treeItem) == "0") {
				getTreeView().getSelectionModel().select(treeItem);
				treeViewClicked(null, treeItem);
			}
		}
	}
			
	private VBox loadERBPathwayLandingRoot(ArrayList<Chapter> chapters) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(chapters, this);
			fxmlLoader.setController(chapterLandingController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private VBox loadChapterLandingRoot(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(chapter, this);
			fxmlLoader.setController(chapterLandingController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private VBox loadWorksheetContentController(Activity activity) {
		if (activity != null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/worksheet/WorksheetContent.fxml"));
				WorksheetContentController worksheetContentController = new WorksheetContentController(activity, project, currentSelectedGoal, app);
				fxmlLoader.setController(worksheetContentController);
				VBox root = fxmlLoader.load();
				activity.setWorksheetContentController(worksheetContentController);
				listOfAllWorksheetContentControllers.add(worksheetContentController);
				return root;
			} catch (Exception e) {
				logger.error(e.getMessage());
				return null;
			}
		} else {
			logger.error("Cannot loadWorksheetContentController. activity is null.");
			return null;
		}
	}

	private VBox loadNoteBoardContentController(Activity activity) {
		if (activity != null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
				NoteBoardContentController noteBoardContentController = new NoteBoardContentController(app, project,
						currentSelectedGoal, activity);
				fxmlLoader.setController(noteBoardContentController);
				VBox root = fxmlLoader.load();
				activity.setNoteBoardContentController(noteBoardContentController);
				listOfAllNoteBoardContentControllers.add(noteBoardContentController);
				return root;
			} catch (Exception e) {
				logger.error(e.getMessage());
				return null;
			}
		} else {
			logger.error("Cannot loadNoteBoardContentController. activity is null.");
			return null;
		}
	}
	
	private Parent loadAttributePaneRoot(String attributeTitle, String attributeContent, String attributeColor) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/AttributePane.fxml"));
			AttributePanelController attributePanelController = new AttributePanelController(attributeTitle, attributeContent, attributeColor, this);
			listOfAttributePanelControllers.add(attributePanelController);
			fxmlLoader.setController(attributePanelController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private VBox loadPlanRoot(Activity activity) {
		if (activity != null) {			
			try {
				FXMLLoader fxmlLoader = null;
				if(project.getProjectType().contentEquals("Facilitator Mode")) {
					fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/PlanFacilitatorMode.fxml"));
					PlanFacilitatorModeController planController = new PlanFacilitatorModeController(currentSelectedChapter);
					fxmlLoader.setController(planController);
					activity.setPlanFacilitatorModeController(planController);
				} else {
					fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/PlanGoalMode.fxml"));
					PlanGoalModeController planController = new PlanGoalModeController(currentSelectedChapter);
					fxmlLoader.setController(planController);
					activity.setPlanGoalModeController(planController);
				}
				return fxmlLoader.load();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}
		} else {
			logger.error("Cannot loadPlanRoot. activity is null.");
			return null;
		}
	}
	
	private ScrollPane loadReflectRoot(Chapter chapter, Activity activity) {
		if (activity != null) {
			try {
				FXMLLoader fxmlLoader = null;
				if(project.getProjectType().contentEquals("Facilitator Mode")) {
					fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ReflectFacilitatorMode.fxml"));
					ReflectFacilitatorModeController reflectFacilitatorModeController = new ReflectFacilitatorModeController(currentSelectedChapter);
					fxmlLoader.setController(reflectFacilitatorModeController);
					activity.setReflectFacilitatorModeController(reflectFacilitatorModeController);
				} else {
					fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ReflectGoalMode.fxml"));
					ReflectGoalModeController reflectController = new ReflectGoalModeController(currentSelectedGoal, chapter, this);
					fxmlLoader.setController(reflectController);
					activity.setReflectGoalModeController(reflectController);
					reflectController.initProgress(currentSelectedGoal, chapter);
				}
				return fxmlLoader.load();
			} catch (Exception e) {
				logger.error(e.getMessage());
				return null;
			}
		} else {
			logger.error("Cannot loadReflectRoot. activity is null.");
			return null;
		}
	}
	
	private Parent loadERBChapterDiagramRoot(Chapter chapter, ArrayList<Chapter> chapters)  {
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
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GlobalGoalTracker.fxml"));
			GlobalGoalTrackerController globalGoalTrackerController = new GlobalGoalTrackerController(app, project);
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
			logger.error("Cannot showGlobalGoalTracker. globalGoalTrackerRoot is null.");
		}
	}
	
	private void setDynamicDimensions(Pane pane) {
		if (pane != null) {
			pane.setPrefHeight(app.getPrefHeight());
			pane.setPrefWidth(app.getPrefWidth()-10);
			VBox.setVgrow(pane, Priority.ALWAYS);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}
	}
	
	private void setDynamicDimensions(ScrollPane pane) {
		if (pane != null) {
			pane.setPrefHeight(app.getPrefHeight());
			pane.setPrefWidth(app.getPrefWidth());
			VBox.setVgrow(pane, Priority.ALWAYS);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}
	}
	
	private void goalChanged(Goal origGoal, Goal newGoal) {
		if(origGoal != null) {
			app.initSaveHandler(null, null, origGoal, project, null, "goalChange");
			goalSelected(newGoal);
		}
	}
	
	@FXML
	public void showDetailsCheckboxAction() {
		if (currentSelectedChapter != null) {
			for (Activity activity : currentSelectedChapter.getAssignedActivities()) {
				ERBPathwayDiagramController erbPathwayDiagramController = getErbPathwayDiagramController(activity.getActivityID());
				if (erbPathwayDiagramController != null) {
					if (showDetailsCheckBox.isSelected()) {
						erbPathwayDiagramScrollPane.setMinHeight(130);
						erbPathwayDiagramController.showDetails();
						addDetailsKeyPaneVBox();
					} else {
						erbPathwayDiagramScrollPane.setMinHeight(60);
						erbPathwayDiagramController.hideDetails();
						removeDetailsKeyPaneVBox();
					}
				}
			}
		}
	}
	
	@FXML
	public void activityStatusRadioButtonAction () {
		RadioButton radioButton = (RadioButton) activityStatusToggleGroup.getSelectedToggle();
		changeActivityStatus(currentSelectedActivity, radioButton);
		ERBPathwayDiagramController erbPathwayDiagramController = getErbPathwayDiagramController(currentSelectedActivity.getActivityID());
		ERBChapterDiagramController erbChapterDiagramController = getErbChapterDiagramController(currentSelectedActivity.getChapterAssignment());
		if(erbChapterDiagramController != null) erbChapterDiagramController.fillChapterCircleBasedOnStatus();
		if(erbPathwayDiagramController != null) erbPathwayDiagramController.updateStatus(); //set status of erb diagram
		updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
		currentSelectedActivity.setSaved(false);
	}
	
	private void changeActivityStatus(Activity activity, RadioButton radioButton) {
		if (activity != null && radioButton != null) {
			if (radioButton.getText().contentEquals("Ready")) {
				activity.setStatus("ready");
			} else if (radioButton.getText().contentEquals("In Progress")) {
				activity.setStatus("in progress");
			} else if (radioButton.getText().contentEquals("Complete")) {
				activity.setStatus("complete");
			}
		} else {
			logger.error("Cannot changeActivityStatus. activity or radioButton is null.");
		}
	}
	
	@FXML
	public void previousButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
			treeView.getSelectionModel().select(parentTreeItem.getChildren().get(currentIndex - 1));
			treeViewClicked(null, parentTreeItem.getChildren().get(currentIndex - 1));
		}
	}
	
	@FXML
	public void nextButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			if (parentTreeItem.getValue().contains("ERB")) {
				treeView.getSelectionModel().select(selectedTreeItem.getChildren().get(0));
				treeViewClicked(null, selectedTreeItem.getChildren().get(0));
			} else {
				int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
				treeView.getSelectionModel().select(parentTreeItem.getChildren().get( currentIndex+ 1));
				treeViewClicked(null, parentTreeItem.getChildren().get( currentIndex+ 1));
			}
		}
	}
	
	@FXML
	public void saveButtonAction() {
		app.initSaveHandler(null, null, null, project, null, "saveButton");
	}
	
	@FXML
	public void goalLabelClicked() {
		Parent globalGoalTrackerRoot = loadGlobalGoalTracker();
		showGlobalGoalTracker(globalGoalTrackerRoot);
	}
		
	@FXML
	public void attributePanelCollapseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.getSource().toString().contains("VBox")) { //Don't allow for label
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
	
	public void treeViewClicked(TreeItem<String> oldItem, TreeItem<String> newItem) {
		if (newItem != null) {
			if (isTreeItemActivityInGoal(newItem, currentSelectedGoal)) {
				activityTreeItemSelected(newItem);
			} else if (isTreeItemChapterInGoal(newItem, currentSelectedGoal)) {
				chapterTreeItemSelected(newItem);
			} else if (!isTreeItemActivityInGoal(newItem, currentSelectedGoal) && !isTreeItemChapterInGoal(newItem, currentSelectedGoal)) {
				erbPathwayTreeItemSelected();
			}
		}
	}
	
	private void erbPathwayTreeItemSelected() {
		if(currentSelectedActivity != null) app.initSaveHandler(null, getChapterForActivityInGoal(currentSelectedActivity, currentSelectedGoal), currentSelectedGoal, project, null, "projectChange");
		//--
		currentSelectedActivity = null;
		currentSelectedChapter = null;
		//--
		cleanContentVBox();
		cleanAttributePanelContentVBox();
		cleanListOfAttributePanelControllers();
		//--
		removeStatusHBox();
		removeLocalProgressVBox();
		removeAttributeScrollPane();
		//--
		addERBKeyVBox(1);
		//--
		generateChapterERBPathway(currentSelectedGoal);
		setNavigationButtonsDisability(null, null);
		if(currentSelectedGoal != null) {
			Pane root = loadERBPathwayLandingRoot(currentSelectedGoal.getChapters());
			addContentToContentVBox(root, true);
		}
	}
	
	private void chapterTreeItemSelected(TreeItem<String> chapterTreeItem) {
		if(currentSelectedActivity != null) {
			Chapter chapter = getChapterForActivityInGoal(currentSelectedActivity, currentSelectedGoal);
			if(chapter != null) {
				app.initSaveHandler(null, chapter, currentSelectedGoal, project, null, "chapterChange");
			}
		}
		//--
		currentSelectedActivity = null;
		currentSelectedChapter = getChapterForNameInGoal(chapterTreeItem.getValue(), currentSelectedGoal);
		
		checkForUpdatedActivityMetaData(currentSelectedChapter);
		//--
		cleanContentVBox();
		cleanAttributePanelContentVBox();
		cleanListOfAttributePanelControllers();
		//--
		removeStatusHBox();
		removeAttributeScrollPane();
		//--
		addERBKeyVBox(1);
		if(project.getProjectType().contentEquals("Goal Mode")) addLocalProgressVBox(1);
		//--
		generateActivityERBPathway(currentSelectedChapter);
		setNavigationButtonsDisability(null, null);
		if(currentSelectedChapter != null) {
			Pane root = loadChapterLandingRoot(currentSelectedChapter);
			addContentToContentVBox(root, true);
		}
		if(currentSelectedChapter != null && currentSelectedGoal != null) updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
	}
	
	private void activityTreeItemSelected(TreeItem<String> activityTreeItem) {
		if(currentSelectedActivity != null) app.initSaveHandler(currentSelectedActivity, null, currentSelectedGoal, project, null, "activityChange");
		//--
		String activityId = treeItemActivityIdTreeMap.get(activityTreeItem);
		currentSelectedActivity = getActivityForIDInGoal(activityId, currentSelectedGoal);
		Chapter chapterForActivity = getChapterForNameInGoal(activityTreeItem.getParent().getValue(), currentSelectedGoal);
		if(currentSelectedChapter != null && (chapterForActivity!= currentSelectedChapter)) {
			app.initSaveHandler(null, currentSelectedChapter, currentSelectedGoal, project, null, "chapterChange");
		}
		currentSelectedChapter = chapterForActivity;
		//--
		cleanContentVBox();
		cleanAttributePanelContentVBox();
		cleanListOfAttributePanelControllers();
		//--
		if(currentSelectedActivity.getActivityID().contentEquals("25") || currentSelectedActivity.getActivityID().contentEquals("26")) {
			removeStatusHBox();
		} else {
			if(project.getProjectType().contentEquals("Goal Mode")) addStatusHBox();
		}
		//--
		addERBKeyVBox(1);
		if(project.getProjectType().contentEquals("Goal Mode")) addLocalProgressVBox(1);
		if(project.getProjectType().contentEquals("Goal Mode")) addAttributeScrollPane(1);
		//--
		loadActivityContent(currentSelectedActivity);
		generateActivityERBPathway(currentSelectedChapter);
		setActivityStatusRadioButton(currentSelectedActivity);
		highlightActivityDiagram(currentSelectedActivity);
		setNavigationButtonsDisability(activityTreeItem, activityTreeItem.getParent());
		if(currentSelectedChapter != null && currentSelectedGoal != null) updateLocalProgress(currentSelectedChapter, currentSelectedGoal.getChapters());
	}
	
	private void checkForUpdatedActivityMetaData(Chapter chapter) {
		for(Activity activity: chapter.getAssignedActivities()) {
			checkForUpdatedActivityMetaData(activity);
		}
	}
	
	private void checkForUpdatedActivityMetaData(Activity activity) {
		XMLManager xmlManager = new XMLManager(app);
		HashMap<String, String> planHashMap = xmlManager.parseActivityMetaXML(constants.getActivityMetaXMLFile(project, currentSelectedGoal, activity));
		activity.setPlanHashMap(planHashMap);
	}
	
	private void addContentToContentVBox(Pane root, boolean setDynamicDimensions) {
		if (root != null) {
			contentVBox.getChildren().add(root);
			if (setDynamicDimensions) {
				setDynamicDimensions(root);
			} else {
				VBox.setVgrow(root, Priority.ALWAYS);
			}
		} else {
			logger.error("Cannot addContentToContentVBox. root is null.");
		}
	}
	
	private void addContentToContentVBox(ScrollPane root, boolean setDynamicDimensions) {
		if (root != null) {
			contentVBox.getChildren().add(root);
			if (setDynamicDimensions) {
				setDynamicDimensions(root);
			} else {
				VBox.setVgrow(root, Priority.ALWAYS);
			}
		} else {
			logger.error("Cannot addContentToContentVBox. root is null.");
		}
	}

	private void fillAndStoreTreeViewData(ArrayList<Chapter> chapters) {
		if (chapters != null) {
			treeItemActivityIdTreeMap.clear();
			TreeItem<String> rootTreeItem = new TreeItem<String>("ERB Pathway");
			rootTreeItem.setExpanded(true);
			treeView.setRoot(rootTreeItem);
			treeItemActivityIdTreeMap.put(rootTreeItem, "0");
			for (Chapter chapter : chapters) {
				TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
				rootTreeItem.getChildren().add(chapterTreeItem);
				treeItemActivityIdTreeMap.put(chapterTreeItem, chapter.getNumericName());
				addPlanTreeItem(chapterTreeItem);
				for (Activity activity : chapter.getAssignedActivities()) {
					TreeItem<String> activityTreeItem = new TreeItem<String>(activity.getLongName());
					chapterTreeItem.getChildren().add(activityTreeItem);
					treeItemActivityIdTreeMap.put(activityTreeItem, activity.getActivityID());
				}
				addReflectTreeItem(chapterTreeItem);
			}
		} else {
			logger.error("Cannot fillAndStoreTreeViewData. chapters is null.");
		}
	}
	
	private void addPlanTreeItem(TreeItem<String> chapterTreeItem) {
		if (chapterTreeItem != null) {
			TreeItem<String> planTreeItem = new TreeItem<String>("Plan");
			chapterTreeItem.getChildren().add(planTreeItem);
			treeItemActivityIdTreeMap.put(planTreeItem, "25");
		} else {
			logger.error("Cannot addPlanTreeItem. chapterTreeItem is null.");
		}
	}
	
	private void addReflectTreeItem(TreeItem<String> chapterTreeItem) {
		if (chapterTreeItem != null) {
			TreeItem<String> reflectTreeItem = new TreeItem<String>("Reflect");
			chapterTreeItem.getChildren().add(reflectTreeItem);
			treeItemActivityIdTreeMap.put(reflectTreeItem, "26");
		} else {
			logger.error("Cannot addReflectTreeItem. chapterTreeItem is null.");
		}
	}
	
	private void fillGoalComboBox(Project project) {
		if (project != null) {
			for (Goal goal : project.getProjectGoals()) {
				goalComboBox.getItems().add(goal);
			}
		} else {
			logger.error("Cannot fillGoalComboBox. project is null.");
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
	
	void generateAttribute(String attributeTitle, String attributeContent, String attributeColor) {
		if (attributeContent!= null && attributeContent.trim().length() > 0 && !attributePanelContainsAttribute(attributeTitle)) {
			Parent root = loadAttributePaneRoot(attributeTitle, attributeContent, attributeColor);
			attributePanelContentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}
	}
	
	private void goalSelected(Goal goal) {
		if (goal != null) {
			currentSelectedGoal = goal;
			ArrayList<Chapter> listOfChapters = goal.getChapters();
			if (listOfChapters != null) {
				fillAndStoreTreeViewData(listOfChapters);
				initializeTreeViewSelection();
				generateChapterERBPathway(currentSelectedGoal);
			}
		} else {
			logger.error("Cannot execute goalSelected. goal is null.");
		}
	}
	
	public void updateLocalProgress(Chapter chapter, ArrayList<Chapter> chapters) {
		Progress progress = new Progress();
		if (chapter != null) {
			int chapterProgress = progress.getChapterPercentDone(chapter);
			progress.setChapterProgress(chapterProgressIndicator, chapterProgress);
		}
		if(chapters != null) {
			int goalProgress = progress.getGoalPercentDone(chapters);
			progress.setGoalProgress(goalProgressVBox, goalProgressBar, goalProgressPercentLabel, goalProgress);
			int goalConfidence = progress.getGoalConfidencePercent(chapters);
			progress.setGoalRating(goalConfidenceVBox, goalConfidenceBar, goalConfidencePercentLabel, goalConfidence);
		}
	}
	
	private void loadActivityContent(Activity activity) {
		if (activity != null) {
			if (project.getProjectType().contentEquals("Facilitator Mode")) {
				if (activity.getActivityType().getDescription().contentEquals("worksheet") || activity.getActivityType().getDescription().contentEquals("noteboard")) {
					if (!activity.getLongName().contentEquals("Plan") && !activity.getLongName().contentEquals("Reflect")) { // Normal Activity
						addBaseAttributesToAttributePanel(activity);
						Pane root = loadWorksheetContentController(activity);
						addContentToContentVBox(root, false);
					} else if (activity.getLongName().contentEquals("Plan")) { // Plan Activity
						removeAttributeScrollPane();
						Pane root = loadPlanRoot(activity);
						addContentToContentVBox(root, true);
					} else if (activity.getLongName().contentEquals("Reflect")) { // Reflect Activity
						removeAttributeScrollPane();
						ScrollPane root = loadReflectRoot(currentSelectedChapter, activity);
						addContentToContentVBox(root, false);
					}
				}
			} else {
				if (activity.getActivityType().getDescription().contentEquals("worksheet")) {
					if (!activity.getLongName().contentEquals("Plan") && !activity.getLongName().contentEquals("Reflect")) { // Normal Activity
						addBaseAttributesToAttributePanel(activity);
						if (activity.getActivityID().contentEquals("14")) {
							Pane root = loadController14Content(activity);
							addContentToContentVBox(root, false);
						} else {
							Pane root = loadWorksheetContentController(activity);
							addContentToContentVBox(root, false);
						}
					} else if (activity.getLongName().contentEquals("Plan")) { // Plan Activity
						removeAttributeScrollPane();
						Pane root = loadPlanRoot(activity);
						addContentToContentVBox(root, true);
					} else if (activity.getLongName().contentEquals("Reflect")) { // Reflect Activity
						removeAttributeScrollPane();
						ScrollPane root = loadReflectRoot(currentSelectedChapter, activity);
						addContentToContentVBox(root, false);
					}
				} else if (activity.getActivityType().getDescription().contentEquals("noteboard")) {
					addBaseAttributesToAttributePanel(activity);
					Pane root = loadNoteBoardContentController(activity);
					addContentToContentVBox(root, false);
				}
			}
		} else {
			logger.error("Cannot loadActivityContent. activity is null.");
		}
	}
	
	private VBox loadController14Content(Activity activity) {
		if (activity != null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/form_activities/14.fxml"));
				Controller14 controller14 = new Controller14(activity);
				fxmlLoader.setController(controller14);
				VBox root = fxmlLoader.load();
				return root;
			} catch (Exception e) {
				logger.error(e.getMessage());
				return null;
			}
		} else {
			logger.error("Cannot loadController14Content. activity is null.");
			return null;
		}
	}
	
	private void addBaseAttributesToAttributePanel(Activity activity) {
		if (activity != null) {
			generateAttribute("Objective", activity.getObjectives(), constants.getObjectivesColor());
			generateAttribute("Instructions", activity.getInstructions(), constants.getInstructionsColor());
		} else {
			logger.error("Cannot addBaseAttributesToAttributePanel. activity is null.");
		}
	}
	
	public Activity retrieveNextActivity(ERBPathwayDiagramController currentErbPathwayDiagramController) {
		int indexOfCurrentController = listOfPathwayDiagramControllers.indexOf(currentErbPathwayDiagramController);
		if(indexOfCurrentController >= 0) {
			int indexOfNextController = indexOfCurrentController + 1;
			ERBPathwayDiagramController nextErbPathwayDiagramController = listOfPathwayDiagramControllers.get(indexOfNextController);
			return nextErbPathwayDiagramController.getActivity();
		}
		return null;
	}
	
	private void highlightActivityDiagram(Activity activity) {
		for(ERBPathwayDiagramController erbPathwayDiagramController: listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getActivity() == activity) {
				erbPathwayDiagramController.highlightDiagram();
			} else {
				erbPathwayDiagramController.unHighlightDiagram();
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
			logger.error("Cannot setActivityStatusRadioButton. activity is null.");
		}
	}
	
	private void generateChapterERBPathway(Goal goal) {
		if (goal != null) {
			cleanERBPathwayDiagramHBox();
			cleanListOfChapterDiagramControllers();
			removeShowDetailsCheckBox();
			removeDetailsKeyPaneVBox();
			erbPathwayDiagramScrollPane.setMinHeight(50);
			for (Chapter chapter : goal.getChapters()) {
				Parent root = loadERBChapterDiagramRoot(chapter, goal.getChapters());
				pathwayTitledPane.setText(goal.getGoalName() + " Pathway");
				if (root != null) erbPathwayDiagramHBox.getChildren().add(root);
			}
		} else {
			logger.error("Cannot generateChapterERBPathway. goal is null.");
		}
	}
	
	private void generateActivityERBPathway(Chapter chapter) {
		if (chapter != null) {
			cleanERBPathwayDiagramHBox();
			cleanListOfActivityDiagramControllers();
			addShowDetailsCheckBox();
			addDetailsKeyPaneVBox();
			erbPathwayDiagramScrollPane.setMinHeight(130);
			for (Activity activity : chapter.getAssignedActivities()) {
				Parent root = loadERBPathwayDiagramRoot(activity, chapter);
				pathwayTitledPane.setText(chapter.getStringName() + " Pathway");
				if (root != null)erbPathwayDiagramHBox.getChildren().add(root);
			}
		} else {
			logger.error("Cannot generateActivityERBPathway. chapter is null.");
		}
	}
	
	public TreeItem<String> findTreeItem(Activity activity) {
		if (activity != null) {
			HashMap<TreeItem<String>, String> treeMap = getTreeItemActivityIdTreeMap();
			for (TreeItem<String> treeItem : treeMap.keySet()) {
				String treeItemValue = treeItem.getValue();
				TreeItem<String> parentTreeItem = treeItem.getParent();
				if (parentTreeItem != null) {
					String chapterString = "Chapter " + activity.getChapterAssignment();
					if (parentTreeItem.getValue().contentEquals(chapterString)) {
						if (treeItemValue.contentEquals(activity.getLongName())) { // if tree item value matches activity name
							String treeItemActivityID = treeMap.get(treeItem);
							if (treeItemActivityID.contentEquals(activity.getActivityID())) { // if tree item GUID matches
								return treeItem;
							}
						}
					}
				} else {
					if (treeItemValue.contentEquals(activity.getLongName())) { // if tree item value matches activity name
						String treeItemActivityID = treeMap.get(treeItem);
						if (treeItemActivityID.contentEquals(activity.getActivityID())) { // if tree item GUID matches
							return treeItem;
						}
					}
				}
			}
			return null;
		} else {
			logger.error("Cannot findTreeItem. activity is null.");
		}
		return null;
	}

	private void setNavigationButtonsDisability(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		if (selectedTreeItem != null) { //if activity is not null
			if (parentTreeItem == null) { //if erb pathway
				previousButton.setDisable(true);
				nextButton.setDisable(false);
			} else {
				int numberOfChildrenInParent = parentTreeItem.getChildren().size();
				if (numberOfChildrenInParent > 1) { //if more than 1 activity
					int indexOfSelectedItem = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					if (indexOfSelectedItem == 0) { //if first activity
						previousButton.setDisable(true);
						nextButton.setDisable(false);
					} else if (indexOfSelectedItem == parentTreeItem.getChildren().size() - 1) { //if last activity
						previousButton.setDisable(false);
						nextButton.setDisable(true);
					} else {
						previousButton.setDisable(false);
						nextButton.setDisable(false);
					}
				} else { //if only 1 activity
					previousButton.setDisable(true);
					nextButton.setDisable(true);
				}
			}
		} else { //if activity is null
			previousButton.setDisable(true);
			nextButton.setDisable(true);
		}
	}
	
	private boolean attributePanelContainsAttribute(String attributeLabel) {
		if (attributeLabel != null) {
			for (AttributePanelController attributePanelController : listOfAttributePanelControllers) {
				if (attributePanelController.getAttributeLabelText().contentEquals(attributeLabel)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void collapseAttributePanel() {
		if(attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().remove(attributePanelContentVBox);
		}
	}
	
	private void unCollapseAttributePanel() {
		if(!attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().add(1, attributePanelContentVBox);
		}
	}
	
	private boolean attributePanelCollapsed() {
		if(!attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
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

	private void cleanContentVBox() {
		contentVBox.getChildren().clear();
	}
	
	private void cleanListOfActivityDiagramControllers() {
		listOfPathwayDiagramControllers.clear();
	}
	
	private void cleanListOfChapterDiagramControllers() {
		listOfChapterDiagramControllers.clear();
	}
	
	private void cleanListOfAttributePanelControllers() {
		listOfAttributePanelControllers.clear();
	}
	
	void removeAttributePanelController(AttributePanelController attributePanelController) {
		listOfAttributePanelControllers.remove(attributePanelController);
	}
	
	private void addDetailsKeyPaneVBox() {
		if(!keyPaneHBox.getChildren().contains(detailsKeyPaneVBox)) {
			keyPaneHBox.getChildren().add(1, detailsKeyPaneVBox);
		}
	}
	
	private void removeDetailsKeyPaneVBox() {
		keyPaneHBox.getChildren().remove(detailsKeyPaneVBox);
	}
	
	private void addShowDetailsCheckBox() {
		if(!titledPaneVBox.getChildren().contains(showDetailsHBox)) {
			titledPaneVBox.getChildren().add(1, showDetailsHBox);
		}
	}
	
	private void removeShowDetailsCheckBox() {
		titledPaneVBox.getChildren().remove(showDetailsHBox);
	}
	
	private void addLocalProgressVBox(int index) {
		if(!treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().add(index, localProgressVBox);
		}
	}
	
	private void removeLocalProgressVBox() {
		if(treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().remove(localProgressVBox);
		}
	}
	
	private void addERBKeyVBox(int index) {
		if(!headingHBox.getChildren().contains(erbKeyVBox)) {
			headingHBox.getChildren().add(index, erbKeyVBox);
		}
	}
	
	private void removeERBKeyVBox() {
		headingHBox.getChildren().remove(erbKeyVBox);
	}
	
	private void addAttributeScrollPane(int index) {
		if(!body2HBox.getChildren().contains(attributePanelHBox)) {
			body2HBox.getChildren().add(index, attributePanelHBox);
		}
	}
	
	private void removeAttributeScrollPane() {
		body2HBox.getChildren().remove(attributePanelHBox);
	}
	
	private void removeStatusHBox() {
		if(mainVBox.getChildren().contains(statusHBox)) {
			mainVBox.getChildren().remove(statusHBox);
		}
	}
	
	private void addStatusHBox() {
		if(!mainVBox.getChildren().contains(statusHBox)) {
			mainVBox.getChildren().add(0, statusHBox);
		}
	}
	
	private void hideGoalSelection() {
		goalLabel.setVisible(false);
		goalComboBox.setVisible(false);
	}
	
	private void removePathwayPane() {
		if(engagementVBox.getChildren().contains(pathwayTitledPane)) {
			engagementVBox.getChildren().remove(pathwayTitledPane);
		}
	}
	
	private void removeLocalProgressPane() {
		if(treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().remove(localProgressVBox);
		}
	}
	
	public void closeGlobalGoalTrackerStage() {
		if(globalGoalTrackerStage!=null) {
			globalGoalTrackerStage.close();
		}
	}

	private boolean isTreeItemActivityInGoal(TreeItem<String> treeItem, Goal goal) {
		if (treeItem != null && goal != null) {
			if (treeItem.getValue().contentEquals("Plan") || treeItem.getValue().contentEquals("Reflect")) {
				return true;
			} else {
				for (Chapter chapter : goal.getChapters()) {
					for (Activity activity : chapter.getAssignedActivities()) {
						if (treeItem.getValue().contentEquals(activity.getLongName())) {
							return true;
						}
					}
				}
			}
		} else {
			logger.error("Cannot execute isTreeItemActivityInGoal. treeItem or goal is null.");
		}
		return false;
	}

	private boolean isTreeItemChapterInGoal(TreeItem<String> treeItem, Goal goal) {
		if (treeItem != null && goal != null) {
			for (Chapter chapter : goal.getChapters()) {
				if (treeItem.getValue().contentEquals(chapter.getStringName())) {
					return true;
				}
			}
		} else {
			logger.error("Cannot execute isTreeItemChapterInGoal. treeItem or goal is null.");

		}
		return false;
	}
	
	public ERBPathwayDiagramController getErbPathwayDiagramController(String GUID) {
		if (GUID != null) {
			for (ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
				if (erbPathwayDiagramController.getActivity().getActivityID().contentEquals(GUID)) {
					return erbPathwayDiagramController;
				}
			}
			logger.debug("ERB Pathway Diagram Controller returned is null");
			return null;
		} else {
			logger.error("Cannot getErbPathwayDiagramController. GUID is null.");
			return null;
		}
	}
	
	public ERBPathwayDiagramController getErbPathwayDiagramController(int chapterNum) {
		for(ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getChapter().getChapterNum() == chapterNum) {
				return erbPathwayDiagramController;
			}
		}
		logger.debug("ERB Pathway Diagram Controller returned is null");
		return null;
	}
	
	public ERBChapterDiagramController getErbChapterDiagramController(String selectedChapter) {
		if (selectedChapter != null) {
			for (ERBChapterDiagramController erbChapterDiagramController : listOfChapterDiagramControllers) {
				if (String.valueOf(erbChapterDiagramController.getChapter().getChapterNum())
						.contentEquals(selectedChapter)) {
					return erbChapterDiagramController;
				}
			}
			logger.debug("ERB Chapter Diagram Controller returned is null");
			return null;
		} else {
			logger.error("Cannot getErbChapterDiagramController. selectedChapter is null.");
			return null;
		}
	}
	
	public Chapter getChapterForNameInGoal(String chapterName, Goal goal) {
		if (chapterName != null && goal != null) {
			for (Chapter chapter : goal.getChapters()) {
				if (chapter.getStringName().contentEquals(chapterName)) {
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
	
	public Chapter getChapterForActivityInGoal(Activity activity, Goal goal) {
		if (activity != null) {
			String chapterName = "Chapter " + activity.getChapterAssignment();
			Chapter chapter = getChapterForNameInGoal(chapterName, goal);
			if (chapter != null) {
				return chapter;
			} else {
				logger.debug("Chapter returned is null");
				return null;
			}
		} else {
			logger.error("Cannot getChapterForActivityInGoal. activity is null.");
			return null;
		}
	}
	
	public Activity getActivityForIDInGoal(String activityID, Goal goal) {
		if (activityID != null && goal != null) {
			if (activityID.contentEquals("25") || activityID.contentEquals("26")) {
				return app.getActivity(activityID, app.getActivities());
			} else {
				for (Chapter chapter : goal.getChapters()) {
					for (Activity activity : chapter.getAssignedActivities()) {
						if (activity.getActivityID().contentEquals(activityID)) {
							return activity;
						}
					}
				}
				logger.debug("Activity returned is null");
				return null;
			}
		} else {
			logger.error("Cannot getActivityForIDInGoal. activityID or goal is null.");
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

	public HashMap<TreeItem<String>, String> getTreeItemActivityIdTreeMap() {
		return treeItemActivityIdTreeMap;
	}

	public void setTreeItemActivityIdTreeMap(HashMap<TreeItem<String>, String> treeItemActivityIdTreeMap) {
		this.treeItemActivityIdTreeMap = treeItemActivityIdTreeMap;
	}

	public ArrayList<AttributePanelController> getListOfAttributePanelControllers() {
		return listOfAttributePanelControllers;
	}

	public void setListOfAttributePanelControllers(ArrayList<AttributePanelController> listOfAttributePanelControllers) {
		this.listOfAttributePanelControllers = listOfAttributePanelControllers;
	}

	public ArrayList<ERBPathwayDiagramController> getListOfPathwayDiagramControllers() {
		return listOfPathwayDiagramControllers;
	}

	public void setListOfPathwayDiagramControllers(ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers) {
		this.listOfPathwayDiagramControllers = listOfPathwayDiagramControllers;
	}

	public ArrayList<WorksheetContentController> getListOfAllWorksheetContentControllers() {
		return listOfAllWorksheetContentControllers;
	}

	public void setListOfAllWorksheetContentControllers(ArrayList<WorksheetContentController> listOfAllWorksheetContentControllers) {
		this.listOfAllWorksheetContentControllers = listOfAllWorksheetContentControllers;
	}

	public ArrayList<NoteBoardContentController> getListOfAllNoteBoardContentControllers() {
		return listOfAllNoteBoardContentControllers;
	}

	public void setListOfAllNoteBoardContentControllers(ArrayList<NoteBoardContentController> listOfAllNoteBoardContentControllers) {
		this.listOfAllNoteBoardContentControllers = listOfAllNoteBoardContentControllers;
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

	public TreeView<String> getTreeView() {
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

	public VBox getGoalConfidenceVBox() {
		return goalConfidenceVBox;
	}

	public VBox getGoalConfidenceBar() {
		return goalConfidenceBar;
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

}
