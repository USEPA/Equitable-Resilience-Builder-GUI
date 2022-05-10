package com.epa.erb.engagement_setup;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.ActivityType;
import com.epa.erb.XMLManager;
import com.epa.erb.chapter.Chapter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class EngagementSetupController implements Initializable {

	@FXML
	VBox selectedActivitesVBox;
	@FXML
	TextArea shortNameTextField;
	@FXML
	TextArea longNameTextField;
	@FXML
	TextArea descriptionTextField;
	@FXML
	TextArea directionsTextField;
	@FXML
	TextArea objectivesTextField;
	@FXML
	Hyperlink fileNameHyperlink;
	@FXML
	TextArea activityTypeDescriptionTextField;
	@FXML
	ListView<Activity> customizedActivitiesListView;
	@FXML
	ListView<ActivityType> activitityTypeListView;
	
	private File projectDirectory;
	public EngagementSetupController(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}
	
	private String selectedChapter = null;	//tracks the current user selected chapter
	private Logger logger = LogManager.getLogger(EngagementSetupController.class);
	private ArrayList<Chapter> chaptersCreated = new ArrayList<Chapter>(); //list of chapter objects created by the user
	private ArrayList<Activity> customizedActivities = new ArrayList<Activity>(); //list of activities parsed from .xml file
	private ArrayList<ActivityType> activityTypes = new ArrayList<ActivityType>(); //list of activity types parsed from .xml file
	private ArrayList<ChapterTitledPaneController> chapterTitledPaneControllers = new ArrayList<ChapterTitledPaneController>(); //List of ChapterTitledPaneControllers for all chapters created by the user
	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parseActivityTypes();
		parseAvailableActivities();
		handleControls();
		fillActivityTypesListView();
		setActivityTypeListViewCellFactory();
		setActivityTypeListViewDrag(activitityTypeListView);
		checkToLoadExistingProjectData(projectDirectory);
	}
	
	private void handleControls() {
		fileNameHyperlink.setOnMouseClicked(e-> fileNameHyperlinkClicked());
		activitityTypeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateActivityTypeDescriptionTextArea());
		customizedActivitiesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateCustomizedActivityInfo());
	}
	
	private void checkToLoadExistingProjectData(File projectDirectory) {
		if(projectDirectoryHasDataFile(projectDirectory)) {
			loadExisitingProjectData();
		}
	}
	
	private void loadExisitingProjectData() {
		File dataFileToLoad = new File(projectDirectory.getPath() + "\\Data.xml");
		XMLManager xmlManager = new XMLManager();
//		ArrayList<Chapter> chapters = xmlManager.parseDataXML(dataFileToLoad);
//		loadChapterDataIntoTool(chapters);
	}
	
	private void loadChapterDataIntoTool(ArrayList<Chapter> chapters) {
		for (Chapter chapter : chapters) { //add chapters
			chaptersCreated.add(chapter);
			ChapterTitledPaneController chapterTitledPaneController = loadChapterTitledPaneController(chapter);
			if (chapterTitledPaneController != null) {
				handleTitledPaneListViewAccess(chapterTitledPaneController);
				storeTitledPaneController(chapterTitledPaneController);
			}
			for (Activity activity : chapter.getAssignedActivities()) {
				if (!activity.getLongName().contentEquals("Plan") && !activity.getLongName().contentEquals("Reflect")) { //add activities to chapter
					ActivityType activityType = activity.getActivityType();
					SelectedActivity selectedActivity = new SelectedActivity(activityType.getLongName(),activityType.getLongName());
					chapterTitledPaneController.getTitledPaneListView().getItems().add(selectedActivity);
					selectedActivity.setActivityID(activity.getActivityID());
					selectedActivity.setShowName(activity.getLongName());
					setTitledPaneListViewCellFactory(chapterTitledPaneController.getTitledPaneListView());
				}
			}
		}
	}
			
	private void fillActivityTypesListView() {
		for (ActivityType activityType : activityTypes) {
			activitityTypeListView.getItems().add(activityType);
		}
	}

	private void setActivityTypeListViewCellFactory() {
		activitityTypeListView.setCellFactory(new Callback<ListView<ActivityType>, ListCell<ActivityType>>() {
			@Override
			public ListCell<ActivityType> call(ListView<ActivityType> param) {
				ListCell<ActivityType> cell = new ListCell<ActivityType>() {
					@Override
					protected void updateItem(ActivityType item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getLongName());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private void fillCustomizedActivitiesListView(String activityTypeName) {
		cleanCustomizedActivitiesListView();
		for (Activity customActivity : customizedActivities) {
			if (customActivity.getActivityType().getLongName().contentEquals(activityTypeName) && !customActivity.getShortName().contentEquals("Plan") && !customActivity.getShortName().contentEquals("Reflect")) {
				if(!customizedActivitiesListView.getItems().contains(customActivity)) {
					customizedActivitiesListView.getItems().add(customActivity);
				}
			}
		}
	}
	
	private void setCustomizedActivityListViewCellFactory() {
		customizedActivitiesListView.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
			@Override
			public ListCell<Activity> call(ListView<Activity> param) {
				ListCell<Activity> cell = new ListCell<Activity>() {
					@Override
					protected void updateItem(Activity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getLongName());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private void setTitledPaneListViewCellFactory(ListView<SelectedActivity> titledPaneListView) {
		titledPaneListView.setCellFactory(new Callback<ListView<SelectedActivity>, ListCell<SelectedActivity>>() {
			@Override
			public ListCell<SelectedActivity> call(ListView<SelectedActivity> param) {
				ListCell<SelectedActivity> cell = new ListCell<SelectedActivity>() {
					@Override
					protected void updateItem(SelectedActivity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getShowName());
							setContextMenu(createActivityContextMenu());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private ContextMenu createActivityContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem removeMenuItem = new MenuItem("Remove");
		removeMenuItem.setOnAction(e-> removeActivityMenuItemAction());
		contextMenu.getItems().add(removeMenuItem);
		return contextMenu;
	}
	
	private void removeActivityMenuItemAction() {
		removeChapterTitledPaneActivity();
		cleanCustomizedActivitiesListView();
	}
					
	@FXML
	public void addChapterButtonAction() {
		Chapter chapter = createChapter(chaptersCreated.size() + 1, 0.0, 0.0, 0.0);
		chaptersCreated.add(chapter);
		ChapterTitledPaneController chapterTitledPaneController = loadChapterTitledPaneController(chapter);
		if (chapterTitledPaneController != null) {
			handleTitledPaneListViewAccess(chapterTitledPaneController);
			storeTitledPaneController(chapterTitledPaneController);
		}
	}
	
	private Chapter createChapter(int chapterNum, double planStatus, double engageStatus, double reflectStatus) {
		return new Chapter(chapterNum, chapterNum + ".0", "Chapter " + chapterNum, "");
//		return new Chapter(chapterNum, chapterNum + ".0", "Chapter " + chapterNum, "", planStatus, engageStatus, reflectStatus);
	}
	
	private ChapterTitledPaneController loadChapterTitledPaneController(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_setup/ChapterTitledPane.fxml"));
			ChapterTitledPaneController chapterTitledPaneController = new ChapterTitledPaneController(chapter);
			fxmlLoader.setController(chapterTitledPaneController);
			TitledPane pane = fxmlLoader.load();
			VBox.setVgrow(pane, Priority.ALWAYS);
			selectedActivitesVBox.getChildren().add(pane);
			return chapterTitledPaneController;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
		
	private void handleTitledPaneListViewAccess(ChapterTitledPaneController chapterTitledPaneController) {
		ListView<SelectedActivity> titledPaneListView = chapterTitledPaneController.getTitledPaneListView();
		setTitledPaneListViewCellFactory(titledPaneListView);
		setSelectedActivityListViewDrag(titledPaneListView);
		titledPaneListView.setOnMouseClicked(e -> selectedActivityListViewClicked(e, titledPaneListView, chapterTitledPaneController.getTitledPaneText()));
	}
	
	private void storeTitledPaneController(ChapterTitledPaneController chapterTitledPaneController) {
		chapterTitledPaneControllers.add(chapterTitledPaneController);
	}

	@FXML
	public void assignButtonAction() {
		Activity selectedCustomizedActivity = customizedActivitiesListView.getSelectionModel().getSelectedItem();
		for (ChapterTitledPaneController chapterTitledPaneController : chapterTitledPaneControllers) {
			if (chapterTitledPaneController.getTitledPaneText().contentEquals(selectedChapter)) {
				SelectedActivity selectedActivity = chapterTitledPaneController.getTitledPaneListView().getSelectionModel().getSelectedItem();
				selectedActivity.setActivityID(selectedCustomizedActivity.getActivityID());
				selectedActivity.setShowName(selectedCustomizedActivity.getLongName());
				setTitledPaneListViewCellFactory(chapterTitledPaneController.getTitledPaneListView());
			}
		}
	}
	
	@FXML
	public void saveDataButtonAction() {
		if(chaptersCreated.size() > 0) {
			addFinalSelectedActivitiesToChapters();
			storeFinalSelectedActivitiesAndChapters();
		}
	}
	
	private void storeFinalSelectedActivitiesAndChapters() {
			File dataFile = new File(pathToERBFolder + "\\EngagementSetupTool\\" + projectDirectory.getName() + "\\Data.xml");
//			XMLManager xmlManager = new XMLManager();
//			xmlManager.writeDataXML(dataFile, chaptersCreated);
	}
	
	private void addFinalSelectedActivitiesToChapters() {
		for (ChapterTitledPaneController chapterTitledPaneController : chapterTitledPaneControllers) {
			Chapter chapter = getChapter(chapterTitledPaneController.getTitledPaneText());
			chapter.getAssignedActivities().clear();
			ListView<SelectedActivity> listView = chapterTitledPaneController.getTitledPaneListView();
			chapter.addActivity(getCustomizedPlanActivity());
			ObservableList<SelectedActivity> selectedActivities = listView.getItems();
			for (SelectedActivity selectedActivity : selectedActivities) {
				chapter.addActivity(getCustomizedActivity(selectedActivity.getActivityID()));
			}
			chapter.addActivity(getCustomizedReflectActivity());
		}
	}
	
	private void selectedActivityListViewClicked(MouseEvent mouseEvent, ListView<SelectedActivity> titledPaneListView, String paneTitle) {
		SelectedActivity selectedActivity = titledPaneListView.getSelectionModel().getSelectedItem();
		if (selectedActivity != null) {
			selectedChapter = paneTitle;
			clearCustomizedActivityInfo();
			fillCustomizedActivitiesListView(selectedActivity.getActivityType());
			setCustomizedActivityListViewCellFactory();
		}
	}
	
	private void fileNameHyperlinkClicked() {
		try {
			String fileName = fileNameHyperlink.getAccessibleText().trim();
			File file = new File(pathToERBFolder + "\\Activities\\ChapterActivities\\" + fileName);
			if (file.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				logger.error(file.getPath() + " either does not exist or desktop is not supported.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void updateActivityTypeDescriptionTextArea() {
		ActivityType selectedActivityType = activitityTypeListView.getSelectionModel().getSelectedItem();
		for(ActivityType activityType: activityTypes) {
			if(activityType.getLongName().contentEquals(selectedActivityType.getLongName())) {
				activityTypeDescriptionTextField.setText(activityType.getDescription());
			}
		}
	}
	
	private void updateCustomizedActivityInfo() {
		Activity selectedCustomizedActivity = customizedActivitiesListView.getSelectionModel().getSelectedItem();
		if (selectedCustomizedActivity != null) {
			for (Activity customActivity : customizedActivities) {
				if (customActivity.getLongName().contentEquals(selectedCustomizedActivity.getLongName())) {
					shortNameTextField.setText(customActivity.getShortName().replace("\r", "\n"));
					longNameTextField.setText(customActivity.getLongName().replace("\r", "\n"));
					descriptionTextField.setText(customActivity.getDescription().replace("\r", "\n"));
					directionsTextField.setText(customActivity.getDirections().replace("\r", "\n"));
					objectivesTextField.setText(customActivity.getObjectives().replace("\r", "\n"));
					fileNameHyperlink.setText(customActivity.getFileName().substring(2, customActivity.getFileName().length()));
					fileNameHyperlink.setAccessibleText(customActivity.getFileName());
				}
			}
		}
	}
	
	private void removeChapterTitledPaneActivity() {
		for (ChapterTitledPaneController chapterTitledPaneController : chapterTitledPaneControllers) {
			if (chapterTitledPaneController.getTitledPaneText().contentEquals(selectedChapter)) {
				SelectedActivity selectedActivity = chapterTitledPaneController.getTitledPaneListView().getSelectionModel().getSelectedItem();
				chapterTitledPaneController.getTitledPaneListView().getItems().remove(selectedActivity);
				setTitledPaneListViewCellFactory(chapterTitledPaneController.getTitledPaneListView());
			}
		}
	}
	
	private void clearCustomizedActivityInfo() {
		shortNameTextField.setText(null);
		longNameTextField.setText(null);
		descriptionTextField.setText(null);
		directionsTextField.setText(null);
		objectivesTextField.setText(null);
		fileNameHyperlink.setText(null);
	}
	
	private boolean projectDirectoryHasDataFile(File projectDirectory) {
		File [] projectFiles = projectDirectory.listFiles();
		if(projectFiles != null && projectFiles.length > 0) {
			for(File projectFile : projectFiles) {
				if(projectFile.getName().contentEquals("Data.xml")) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void setActivityTypeListViewDrag(ListView<ActivityType> listView) {
		String TAB_DRAG_KEY = "listView";
		ObjectProperty<ListView<ActivityType>> draggingTab = new SimpleObjectProperty<ListView<ActivityType>>();
		listView.setOnDragDetected(event-> {
			Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(listView);
			event.consume();
		});
	}
	
	private void setSelectedActivityListViewDrag(ListView<SelectedActivity> draggedListView) {
		String TAB_DRAG_KEY = "listView";
		ObjectProperty<ListView<SelectedActivity>> draggingTab = new SimpleObjectProperty<ListView<SelectedActivity>>();
		draggedListView.setOnDragOver(event-> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		draggedListView.setOnDragDropped(event-> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasString()) {			
				Object source = event.getGestureSource();				
				boolean equals = false;
				if(source == draggedListView) equals = true;
				
				if(equals == true) { //Would handle drag and drop within titled pane
				}else {
					@SuppressWarnings("unchecked")
					ListView<ActivityType> sourceListView = (ListView<ActivityType>) source;
					ActivityType selectedActivityType = (ActivityType) sourceListView.getSelectionModel().getSelectedItem();
					SelectedActivity selectedActivity = new SelectedActivity(selectedActivityType.getLongName(), selectedActivityType.getLongName());
					draggedListView.getItems().add(selectedActivity);
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		draggedListView.setOnDragDetected(event-> {
			Dragboard dragboard = draggedListView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(draggedListView);
			event.consume();
		});
	}
	
	private void parseActivityTypes() {
		activityTypes.clear();
		File activityTypesFile = new File(pathToERBFolder + "\\Activities\\Activity_Types.xml");
		XMLManager xmlManager = new XMLManager();
//		activityTypes = xmlManager.parseActivityTypesXML(activityTypesFile);
	}

	private void parseAvailableActivities() {
		customizedActivities.clear();
		File activitesFile = new File(pathToERBFolder + "\\Activities\\Available_Activites.xml");
		XMLManager xmlManager = new XMLManager();
//		customizedActivities = xmlManager.parseAvailableActivitiesXML(activitesFile, activityTypes);
	}
		
	private void cleanCustomizedActivitiesListView() {
		customizedActivitiesListView.getItems().clear();
	}
	
	private Chapter getChapter(String chapterName) {
		for(Chapter chapter: chaptersCreated) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
		
	private Activity getCustomizedActivity(String activityID) {
		for(Activity activity: customizedActivities) {
			if(activity.getActivityID().contentEquals(activityID)) {
				return activity;
			}
		}
		logger.debug("Customized Activity returned is null.");
		return null;
	}
		
	private Activity getCustomizedPlanActivity() {
		for(Activity activity: customizedActivities) {
			if(activity.getShortName().contentEquals("Plan")) {
				return activity;
			}
		}
		logger.debug("Customized Plan Activity returned is null.");
		return null;
	}
	
	private Activity getCustomizedReflectActivity() {
		for(Activity activity: customizedActivities) {
			if(activity.getShortName().contentEquals("Reflect")) {
				return activity;
			}
		}
		logger.debug("Customized Reflect Activity returned is null.");
		return null;
	}

	public VBox getSelectedActivitesVBox() {
		return selectedActivitesVBox;
	}

	public TextArea getShortNameTextField() {
		return shortNameTextField;
	}

	public TextArea getLongNameTextField() {
		return longNameTextField;
	}

	public TextArea getDescriptionTextField() {
		return descriptionTextField;
	}

	public TextArea getDirectionsTextField() {
		return directionsTextField;
	}

	public TextArea getObjectivesTextField() {
		return objectivesTextField;
	}

	public Hyperlink getFileNameHyperlink() {
		return fileNameHyperlink;
	}

	public TextArea getActivityTypeDescriptionTextField() {
		return activityTypeDescriptionTextField;
	}
	
	public ListView<Activity> getCustomizedActivitiesListView() {
		return customizedActivitiesListView;
	}

	public ListView<ActivityType> getActivitityTypeListView() {
		return activitityTypeListView;
	}

	public File getProjectDirectory() {
		return projectDirectory;
	}

	public void setProjectDirectory(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}

	public String getSelectedChapter() {
		return selectedChapter;
	}

	public ArrayList<Chapter> getChaptersCreated() {
		return chaptersCreated;
	}

	public ArrayList<Activity> getCustomizedActivities() {
		return customizedActivities;
	}

	public ArrayList<ActivityType> getActivityTypes() {
		return activityTypes;
	}

	public ArrayList<ChapterTitledPaneController> getChapterTitledPaneControllers() {
		return chapterTitledPaneControllers;
	}
	
}
