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
import com.epa.erb.chapter.PlanController;
import com.epa.erb.chapter.ReflectController;
import com.epa.erb.goal.Goal;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.project.Project;
import com.epa.erb.worksheet.WorksheetContentController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EngagementActionController implements Initializable{

	@FXML
	ComboBox<Goal> goalComboBox;
	@FXML
	HBox headingHBox;
	@FXML
	Label chapterLabel;
	@FXML
	HBox erbPathwayDiagramHBox;
	@FXML
	VBox erbKeyVBox;
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
	Pane skippedKeyPane;
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
	HBox saveHBox;
	@FXML
	VBox contentVBox;
	@FXML
	ScrollPane attributePanelScrollPane;
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
	Button skipButton;
	@FXML
	Button nextButton;

	private App app;
	private Project project;
	public EngagementActionController(App app, Project project) {
		this.app = app;
		this.project = project;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	private ArrayList<Chapter> listOfChapters = new ArrayList<Chapter>();
	private Activity currentSelectedActivity = null; //tracks the current user selected activity
	private HashMap<TreeItem<String>, String> treeMap = new HashMap<TreeItem<String>, String>(); //holds the tree items mapped to a chapter or activity GUID
	private ArrayList<AttributePanelController> listOfAttributePanelControllers = new ArrayList<AttributePanelController>(); //holds all of the attribute panels
	private ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers = new ArrayList<ERBPathwayDiagramController>(); //holds all of the pathway controllers
	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillGoalComboBox();
		goalComboBox.setCellFactory(lv-> createGoalCell());
		goalComboBox.setButtonCell(createGoalCell());
		initializeGoalSelection();
		handleControls();
		handleNavigationButtonsShown(null, null);
	}
	
	private void handleControls() {
		treeView.setOnMouseClicked(e-> treeViewClicked());
		initializeStyle();
		addProgressListeners();
	}
	
	private void initializeStyle() {
		materialKeyPane.setStyle("-fx-background-color: " + constants.getMaterialsColor() + ";");
		descriptionKeyPane.setStyle("-fx-background-color: " + constants.getDescriptionColor() + ";");
		whoKeyPane.setStyle("-fx-background-color: " + constants.getWhoColor() + ";");
		completeKeyPane.setStyle("-fx-background-color: " + constants.getCompleteStatusColor() + ";");
		readyKeyPane.setStyle("-fx-background-color: " + constants.getReadyStatusColor() + ";");
		skippedKeyPane.setStyle("-fx-background-color: " + constants.getSkippedStatusColor() + ";");
		inProgressKeyPane.setStyle("-fx-background-color: " + constants.getInProgressStatusColor() + ";");
		timeKeyPane.setStyle("-fx-background-color: " + constants.getTimeColor() + ";");
		chapterProgressIndicator.setStyle(" -fx-progress-color: " + constants.getAllChaptersColor() + ";");
		saveHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	private void addProgressListeners() {
		localProgressVBox.heightProperty().addListener(e-> handleProgressListeners());
	}
	
	private void handleProgressListeners() {
		if(currentSelectedActivity != null) {
			String currentChapterName = "Chapter " + currentSelectedActivity.getChapterAssignment();
			handleLocalProgress(getChapter(currentChapterName), listOfChapters);
		} else {
			TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			if(selectedTreeItem.getValue().contains("Chapter")) {
				handleLocalProgress(getChapter(selectedTreeItem.getValue().trim()), listOfChapters);
			}
		}
	}
	
	private void initializeGoalSelection() {
		goalComboBox.getSelectionModel().select(0);
		goalSelected(project.getProjectGoals().get(0));
	}
	
	private void initializeTreeViewSelection() {
		for (TreeItem<String> treeItem : treeMap.keySet()) {
			if (treeMap.get(treeItem) == "0") {
				getTreeView().getSelectionModel().select(treeItem);
				treeViewClicked();
			}
		}
	}
			
	private void loadERBLandingContent(ArrayList<Chapter> chapters) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(chapters, this);
			fxmlLoader.setController(chapterLandingController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadChapterLandingContent(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(chapter, this);
			fxmlLoader.setController(chapterLandingController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadSampleWK(Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/worksheet/WorksheetContent.fxml"));
			WorksheetContentController worksheetContentController = new WorksheetContentController(activity);
			fxmlLoader.setController(worksheetContentController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadSampleNB(Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController(activity);
			fxmlLoader.setController(noteBoardContentController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private Parent loadAttributePanel(String attributeTitle, String attributeContent, String attributeColor) {
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
	
	private void loadChapterPlan(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/Plan.fxml"));
			PlanController planController = new PlanController(chapter);
			fxmlLoader.setController(planController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadChapterReflect(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/Reflect.fxml"));
			ReflectController reflectController = new ReflectController(chapter, this);
			fxmlLoader.setController(reflectController);
			Parent root = fxmlLoader.load();
			reflectController.initProgress(getCurrentGoal(), chapter);
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private Parent loadChapterERBPathwayDiagram(Chapter chapter, ArrayList<Chapter> chapters)  {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBChapterDiagram.fxml"));
			ERBChapterDiagramController erbChapterDiagramController = new ERBChapterDiagramController(chapter, chapters, this);
			fxmlLoader.setController(erbChapterDiagramController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	
	private Parent loadERBPathwayDiagram(Activity activity, Chapter chapter) {
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
	
	@FXML
	public void goalComboBoxAction() {
		Goal goal = goalComboBox.getSelectionModel().getSelectedItem();
		if(goal != null) {
			goalSelected(goal);
		}
	}
		
	@FXML
	public void startButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		String activityID = treeMap.get(selectedTreeItem);
		Chapter chapter = getChapter(parentTreeItem.getValue());
		Activity activity = getActivity(activityID, chapter.getChapterNum());
		activity.setStatus("in progress"); //set status of activity
		
		ERBPathwayDiagramController erbPathwayDiagramController = getErbPathwayDiagramController(activityID);
		if(erbPathwayDiagramController != null) erbPathwayDiagramController.updateStatus(); //set status of erb diagram
		
		handleLocalProgress(chapter, listOfChapters);
	}
	
	@FXML
	public void completeButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		String activityID = treeMap.get(selectedTreeItem);
		Chapter chapter = getChapter(parentTreeItem.getValue());
		Activity activity = getActivity(activityID, chapter.getChapterNum());
		activity.setStatus("complete"); //set status of activity
		
		ERBPathwayDiagramController erbPathwayDiagramController = getErbPathwayDiagramController(activityID);
		if(erbPathwayDiagramController != null) erbPathwayDiagramController.updateStatus(); //set status of erb diagram
		
		handleLocalProgress(chapter, listOfChapters);
	}
	
	@FXML
	public void previousButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
			treeView.getSelectionModel().select(parentTreeItem.getChildren().get(currentIndex - 1));
			treeViewClicked();
		}
	}
	
	@FXML
	public void skipButtonAction() {
		
	}
	
	@FXML
	public void nextButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			if (parentTreeItem.getValue().contains("ERB")) {
				treeView.getSelectionModel().select(selectedTreeItem.getChildren().get(0));
			} else {
				int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
				treeView.getSelectionModel().select(parentTreeItem.getChildren().get( currentIndex+ 1));
			}
			treeViewClicked();
		}
	}
	
	@FXML
	public void saveButtonAction() {
		Goal goal = getCurrentGoal();
		if (goal != null) {
			saveGoalData(goal);
		}
	}
	
	private void saveGoalData(Goal goal) {
		XMLManager xmlManager = new XMLManager();
		File goalXML = getGoalXMLFile(goal);
		xmlManager.writeGoalMetaXML(goalXML, listOfChapters);
	}
	
	@FXML
	public void attributePanelCollapseClicked() {
		String attributePanelCollapseString = attributePanelCollapseLabel.getText();
		if(attributePanelCollapseString.contentEquals(">")) {
			collapseAttributes();
			attributePanelScrollPane.setMinWidth(0.0);
			attributePanelCollapseLabel.setText("<");

		} else if (attributePanelCollapseString.contentEquals("<")) {
			unCollapseAttributes();
			attributePanelScrollPane.setMinWidth(200.0);
			attributePanelCollapseLabel.setText(">");
		}
	}
	
	public void treeViewClicked() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
			if (parentTreeItem != null) {
				String parentTreeItemValue = parentTreeItem.getValue().trim();
				String selectedTreeItemValue = selectedTreeItem.getValue().trim();
				if (selectedTreeItemValue.length() > 0) {
					if (parentTreeItemValue.contains("ERB")) { // Is Chapter
						handleChapterSelectedInTree(selectedTreeItemValue);
						addLocalProgressVBox(1);
						handleLocalProgress(getChapter(selectedTreeItemValue), listOfChapters);
					} else { // Is Activity
						handleActivitySelectedInTree(selectedTreeItem, parentTreeItem);
					}
				} else {
					currentSelectedActivity = null;
				}
			} else { //ERB Pathway 
				handleERBPathwaySelectedInTree(selectedTreeItem);
				removeLocalProgressVBox();
			}
		} else {
			currentSelectedActivity = null;
			handleNavigationButtonsShown(null, null);
		}
	}
	
	private void fillAndStoreTreeViewData(ArrayList<Chapter> chapters) {
		treeMap.clear();
		TreeItem<String> rootTreeItem = new TreeItem<String>("ERB Pathway");
		rootTreeItem.setExpanded(true);
		treeView.setRoot(rootTreeItem);
		treeMap.put(rootTreeItem, "0");
		for (Chapter chapter : chapters) {
			TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
			rootTreeItem.getChildren().add(chapterTreeItem);
			treeMap.put(chapterTreeItem, chapter.getNumericName());
			for (Activity activity : chapter.getAssignedActivities()) {
				TreeItem<String> activityTreeItem = new TreeItem<String>(activity.getLongName());
				chapterTreeItem.getChildren().add(activityTreeItem);
				treeMap.put(activityTreeItem, activity.getActivityID());
			}
		}
	}
	
	private void fillGoalComboBox() {
		for(Goal goal: project.getProjectGoals()) {
			goalComboBox.getItems().add(goal);
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
	
	void handleAttributePanelGeneration(String attributeTitle, String attributeContent, String attributeColor) {
		if (attributeContent.trim().length() > 0 && !containsAttribute(attributeTitle)) {
			Parent root = loadAttributePanel(attributeTitle, attributeContent, attributeColor);
			attributePanelContentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}
	}
	
	private void goalSelected(Goal goal) {
		listOfChapters = getChapterData(goal);
		if (listOfChapters != null) {
			fillAndStoreTreeViewData(listOfChapters);
			initializeTreeViewSelection();
			handleChapterERBPathwayGeneration();
		}
	}
	
	public void handleLocalProgress(Chapter chapter, ArrayList<Chapter> chapters) {
		Progress progress = new Progress();
		if (chapter != null) {
			int chapterProgress = progress.getChapterPercentDone(chapter);
			setChapterProgress(chapterProgress);
		}
		if(chapters != null) {
			int goalProgress = progress.getGoalPercentDone(chapters);
			setGoalProgress(goalProgress);
			int goalConfidence = progress.getGoalConfidencePercent(chapters);
			setGoalConfidence(goalConfidence);
		}
	}
	
	private void setChapterProgress(int chapterPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chapterProgressIndicator.setProgress(chapterPercent/100.0);
			}
		});
	}
	
	private void setGoalProgress(int goalPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalProgressVBox.getHeight();
				goalProgressBar.setMaxHeight(progressBarHeight);
				goalProgressBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalPercent / fixedCapacity;
				goalProgressBar.setPrefHeight(progressBarHeight * progress);
				goalProgressPercentLabel.setText(goalPercent + "%");
			}
		});
	}
	
	private void setGoalConfidence(int goalConfidence) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalConfidenceVBox.getHeight();
				goalConfidenceBar.setMaxHeight(progressBarHeight);
				goalConfidenceBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalConfidence / fixedCapacity;
				goalConfidenceBar.setPrefHeight(progressBarHeight * progress);
				goalConfidencePercentLabel.setText(goalConfidence + "%");
			}
		});
	}
	
	private void loadActivityContent(TreeItem<String> selectedTreeItem, Chapter chapter) {
		String activityID = treeMap.get(selectedTreeItem);
		Activity selectedActivity = getActivity(activityID, chapter.getChapterNum());
		if (selectedActivity.getActivityType().getDescription().contentEquals("worksheet")) {
			if (!selectedActivity.getLongName().contentEquals("Plan") && !selectedActivity.getLongName().contentEquals("Reflect")) {
				handleAttributePanelGeneration("Objective", selectedActivity.getObjectives(), constants.getObjectivesColor());
				handleAttributePanelGeneration("Instructions", selectedActivity.getDirections(), constants.getInstructionsColor());
				loadSampleWK(selectedActivity);
			} else if (selectedActivity.getLongName().contentEquals("Plan")) {
				removeAttributeScrollPane();
				loadChapterPlan(getChapterForActivity(selectedActivity));
			} else if (selectedActivity.getLongName().contentEquals("Reflect")) {
				removeAttributeScrollPane();
				loadChapterReflect(getChapterForActivity(selectedActivity));
			}
		} else if (selectedActivity.getActivityType().getDescription().contentEquals("noteboard")) {
			handleAttributePanelGeneration("Objective", selectedActivity.getObjectives(), constants.getObjectivesColor());
			handleAttributePanelGeneration("Instructions", selectedActivity.getDirections(), constants.getInstructionsColor());
			loadSampleNB(selectedActivity);
		}
	}
	
	private void highlightSelectedActivityDiagram(TreeItem<String> selectedTreeItem, Chapter chapter) {
		String activityID = treeMap.get(selectedTreeItem);
		Activity selectedActivity = getActivity(activityID, chapter.getChapterNum());
		for(ERBPathwayDiagramController erbPathwayDiagramController: listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getActivity() == selectedActivity) {
				erbPathwayDiagramController.highlightDiagram();
			} else {
				erbPathwayDiagramController.unHighlightDiagram();
			}
		}
	}
	
	private void handleChapterSelectedInTree(String selectedTreeItemValue) {
		Chapter currentChapter = getChapter(selectedTreeItemValue);
		currentSelectedActivity = null;
		cleanContentVBox();
		cleanAttributePanelContentVBox();
		cleanListOfAttributePanelControllers();
		addERBKeyVBox(1);
		removeAttributeScrollPane();
		loadChapterLandingContent(currentChapter);
		handleNavigationButtonsShown(null, null);
		handleActivityERBPathwayGeneration(currentChapter);
		removeStatusHBox();
	}
	
	private void handleActivitySelectedInTree(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		String activityGUID = treeMap.get(selectedTreeItem);
		Chapter currentChapter = getChapter(parentTreeItem.getValue());
		Activity selectedActivity = getActivity(activityGUID, currentChapter.getChapterNum());
		if(!selectedActivity.getActivityID().contentEquals("26")) {
			addLocalProgressVBox(1);
			handleLocalProgress(currentChapter, listOfChapters);
		} else {
			removeLocalProgressVBox();
		}
		if(!currentChapter.getStringName().contentEquals(selectedActivity.getChapterAssignment()) || currentSelectedActivity != selectedActivity) { //If a new activity is selected
			addAttributeScrollPane(1);
			addERBKeyVBox(1);
			cleanContentVBox();
			cleanAttributePanelContentVBox();
			cleanListOfAttributePanelControllers();
			loadActivityContent(selectedTreeItem, currentChapter);
			handleActivityERBPathwayGeneration(currentChapter);
			highlightSelectedActivityDiagram(selectedTreeItem, currentChapter);
			currentSelectedActivity = selectedActivity;
			addStatusHBox();
		}
		handleNavigationButtonsShown(selectedTreeItem, parentTreeItem);
	}
	
	private void handleERBPathwaySelectedInTree(TreeItem<String> selectedTreeItem) {
		if (selectedTreeItem != null) {
			currentSelectedActivity = null;
			cleanContentVBox();
			cleanAttributePanelContentVBox();
			cleanListOfAttributePanelControllers();
			removeAttributeScrollPane();
			removeERBKeyVBox();
			handleChapterERBPathwayGeneration();
			loadERBLandingContent(listOfChapters);
			handleNavigationButtonsShown(null, null);
			removeStatusHBox();
		}
	}

	private void handleChapterERBPathwayGeneration() {
		cleanERBPathwayDiagramHBox();
		setChapterLabelText("Chapters");
		for (Chapter chapter : listOfChapters) {
			Parent root = loadChapterERBPathwayDiagram(chapter, listOfChapters);
			if(root != null) erbPathwayDiagramHBox.getChildren().add(root);
		}
	}
	
	private void handleActivityERBPathwayGeneration(Chapter chapter) {
		if (chapter != null) {
			if (chapterLabel.getText() == null || !chapterLabel.getText().contentEquals(chapter.getStringName())) { // If a new chapter is selected
				cleanERBPathwayDiagramHBox();
				cleanListOfActivityDiagramControllers();
				setChapterLabelText(chapter.getStringName() + " Activities");
				for (Activity activity : chapter.getAssignedActivities()) {
					Parent root = loadERBPathwayDiagram(activity, chapter);
					if(root != null) erbPathwayDiagramHBox.getChildren().add(root);
				}
			}
		}
	}
	
	private void handleNavigationButtonsShown(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		if (selectedTreeItem != null) { //if activity is not null
			if (parentTreeItem == null) { //if erb pathway
				previousButton.setDisable(true);
				skipButton.setDisable(true);
				nextButton.setDisable(false);
			} else {
				int numberOfChildrenInParent = parentTreeItem.getChildren().size();
				if (numberOfChildrenInParent > 1) { //if more than 1 activity
					int indexOfSelectedItem = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					if (indexOfSelectedItem == 0) { //if first activity
						previousButton.setDisable(true);
						skipButton.setDisable(true); //true
						nextButton.setDisable(false);
					} else if (indexOfSelectedItem == parentTreeItem.getChildren().size() - 1) { //if last activity
						previousButton.setDisable(false);
						skipButton.setDisable(true);
						nextButton.setDisable(true);
					} else {
						previousButton.setDisable(false);
						skipButton.setDisable(true);
						nextButton.setDisable(false);
					}
				} else { //if only 1 activity
					previousButton.setDisable(true);
					skipButton.setDisable(true);
					nextButton.setDisable(true);
				}
			}
		} else { //if activity is null
			previousButton.setDisable(true);
			skipButton.setDisable(true);
			nextButton.setDisable(true);
		}
	}
	
	private boolean containsAttribute(String attributeLabel) {
		for(AttributePanelController attributePanelController : listOfAttributePanelControllers) {
			if(attributePanelController.getAttributeLabelText().contentEquals(attributeLabel)) {
				return true;
			}
		}
		return false;
	}
	
	private void collapseAttributes() {
		if(attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().remove(attributePanelContentVBox);
		}
	}
	
	private void unCollapseAttributes() {
		if(!attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().add(1, attributePanelContentVBox);
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
	
	private void cleanListOfAttributePanelControllers() {
		listOfAttributePanelControllers.clear();
	}
	
	void removeAttributePanelController(AttributePanelController attributePanelController) {
		listOfAttributePanelControllers.remove(attributePanelController);
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
		if(!body2HBox.getChildren().contains(attributePanelScrollPane)) {
			body2HBox.getChildren().add(index, attributePanelScrollPane);
		}
	}
	
	private void removeAttributeScrollPane() {
		body2HBox.getChildren().remove(attributePanelScrollPane);
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
			
	public Chapter getChapter(String chapterName) {
		for(Chapter chapter : listOfChapters) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	public Chapter getChapterForActivity(Activity activity) {
		String chapterName = "Chapter " + activity.getChapterAssignment();
		Chapter chapter = getChapter(chapterName);
		if(chapter != null) return chapter;
		logger.debug("Chapter returned is null");
		return null;
	}
	
	private Activity getActivity(String activityID, int chapterNum) {
		for(Chapter chapter: listOfChapters) {
			for(Activity activity : chapter.getAssignedActivities()) {
				if(activity.getActivityID().contentEquals(activityID) && activity.getChapterAssignment().contentEquals(String.valueOf(chapterNum))) {
					return activity;
				}
			}
		}
		logger.debug("Activity returned is null");
		return null;
	}
	
	String getSelectedActivityGUID() {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
		if (treeItem != null) {
			return treeMap.get(treeItem);
		} else {
			logger.debug("Selected Activity GUID returned is null");
			return null;
		}
	}
	
	public ERBPathwayDiagramController getErbPathwayDiagramController(String GUID) {
		for(ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getActivity().getActivityID().contentEquals(GUID)) {
				return erbPathwayDiagramController;
			}
		}
		logger.debug("ERB Pathway Diagram Controller returned is null");
		return null;
	}
	
	private ArrayList<Chapter> getChapterData(Goal goal) {
		File goalXMLFile = getGoalXMLFile(goal);
		if (goalXMLFile != null) {
			XMLManager xmlManager = new XMLManager();
			return xmlManager.parseGoalXML(goalXMLFile, app.getActivities());
		}
		logger.debug("Chapter data returned is null");
		return null;
	}
	
	private File getGoalXMLFile(Goal goal) {
		File goalMetaFile = new File(pathToERBFolder + "\\Projects\\" + project.getProjectName() + "\\Goals\\" + goal.getGoalName() + "\\Meta.xml");
		if(goalMetaFile.exists()) {
			return goalMetaFile;
		}
		logger.debug("Goal XML file returned is null.");
		return null;
	}
	
	public Goal getCurrentGoal() {
		return goalComboBox.getSelectionModel().getSelectedItem();
	}
		
	private void setChapterLabelText(String chapterLabelText) {
		chapterLabel.setText(chapterLabelText);
	}

	public HBox getHeadingHBox() {
		return headingHBox;
	}
	
	public Label getChapterLabel() {
		return chapterLabel;
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

	public Pane getSkippedKeyPane() {
		return skippedKeyPane;
	}

	public Pane getInProgressKeyPane() {
		return inProgressKeyPane;
	}

	public TreeView<String> getTreeView() {
		return treeView;
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

	public ScrollPane getAttributePanelScrollPane() {
		return attributePanelScrollPane;
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

	public Button getPreviousButton() {
		return previousButton;
	}

	public Button getSkipButton() {
		return skipButton;
	}

	public Button getNextButton() {
		return nextButton;
	}
	
	public ArrayList<Chapter> getListOfChapters() {
		return listOfChapters;
	}

	public File getDataFileToLoad() {
		return null;
	}

	public File getProjectDirectory() {
		return null;
	}

	public Activity getCurrentSelectedActivity() {
		return currentSelectedActivity;
	}
	
	public HashMap<TreeItem<String>, String> getTreeMap() {
		return treeMap;
	}

	public ArrayList<AttributePanelController> getListOfAttributePanelControllers() {
		return listOfAttributePanelControllers;
	}

	public ArrayList<ERBPathwayDiagramController> getListOfPathwayDiagramControllers() {
		return listOfPathwayDiagramControllers;
	}

}
