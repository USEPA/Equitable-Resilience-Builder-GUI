package erb;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class EngagementSetupController implements Initializable {

	@FXML
	HBox erbPathwayHBox;
	@FXML
	ListView<ActivityType> activitityTypeListView;
	@FXML
	TextArea activityTypeDescriptionTextField;
	@FXML
	Button addChapterButton;
	@FXML
	VBox selectedActivitesVBox;
	@FXML
	ListView<Activity> customizedActivitiesListView;
	@FXML
	TextField shortNameTextField;
	@FXML
	TextField longNameTextField;
	@FXML
	TextArea descriptionTextField;
	@FXML
	TextArea directionsTextField;
	@FXML
	TextArea objectivesTextField;
	@FXML
	Hyperlink fileNameHyperlink;
	@FXML
	Button assignButton;
	
	String selectedChapter = null;
	
	ArrayList<Chapter> chaptersCreated = new ArrayList<Chapter>();
	ArrayList<Activity> customizedActivities = new ArrayList<Activity>();
	ArrayList<ActivityType> activityTypes = new ArrayList<ActivityType>();
	ArrayList<ChapterTitledPaneController> chapterTitledPaneControllers = new ArrayList<ChapterTitledPaneController>();

	private Logger logger = LogManager.getLogger(EngagementSetupController.class);

	String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";

	public EngagementSetupController() {
		parseActivityTypes();
		parseAvailableActivities();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		fillActivitiesListView();
		setActivityTypeListViewCellFactory();
	}
	
	public void handleControls() {
		setActivityTypeListViewDrag(activitityTypeListView);
		fileNameHyperlink.setOnMouseClicked(e-> fileNameHyperlinkClicked());
		activitityTypeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateActivityTypeDescriptionTextArea());
		customizedActivitiesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateCustomizedActivityInfo());
	}
	
	public void fillActivitiesListView() {
		for (ActivityType activityType : activityTypes) {
			activitityTypeListView.getItems().add(activityType);
		}
	}

	public void setActivityTypeListViewCellFactory() {
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
	
	public void fillCustomizedActivitiesListView(String activityTypeName) {
		customizedActivitiesListView.getItems().clear();
		clearCustomizedActivityInfo();
		for (Activity customActivity : customizedActivities) {
			if (customActivity.getActivityType().getLongName().contentEquals(activityTypeName)) {
				if(!customizedActivitiesListView.getItems().contains(customActivity)) {
					customizedActivitiesListView.getItems().add(customActivity);
				}
			}
		}
	}
	
	public void setCustomizedActivityListViewCellFactory() {
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
		
	public void loadPathway() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ERBPathway.fxml"));
			ERBPathwayController erbPathwayController = new ERBPathwayController(customizedActivities.get(0));
			fxmlLoader.setController(erbPathwayController);
			Parent root = fxmlLoader.load();
			erbPathwayHBox.getChildren().add(root);
		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}
				
	@FXML
	public void addChapterButtonAction() {
		int chapterNum = chaptersCreated.size() + 1;
		Chapter chapter = new Chapter(chapterNum, chapterNum + ".0", "Chapter " + chapterNum, "");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ChapterTitledPane.fxml"));
			ChapterTitledPaneController chapterTitledPaneController = new ChapterTitledPaneController(chapter.getStringName());
			fxmlLoader.setController(chapterTitledPaneController);
			TitledPane pane = fxmlLoader.load();
			ListView<SelectedActivity> titledPaneListView = chapterTitledPaneController.getTitledPaneListView();
			setSelectedActivityListViewDrag(titledPaneListView);
			titledPaneListView.setOnMouseClicked(e -> selectedActivityListViewClicked(titledPaneListView, chapterTitledPaneController.getPaneTitle()));
			VBox.setVgrow(pane, Priority.ALWAYS);
			selectedActivitesVBox.getChildren().add(pane);
			chaptersCreated.add(chapter);
			chapterTitledPaneControllers.add(chapterTitledPaneController);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@FXML
	public void assignButtonAction() {
		Activity selectedCustomizedActivity = customizedActivitiesListView.getSelectionModel().getSelectedItem();
		for (ChapterTitledPaneController chapterTitledPaneController : chapterTitledPaneControllers) {
			if (chapterTitledPaneController.getPaneTitle().contentEquals(selectedChapter)) {
				SelectedActivity selectedActivity = chapterTitledPaneController.getTitledPaneListView().getSelectionModel().getSelectedItem();
				selectedActivity.setActivityGUID(selectedCustomizedActivity.getGUID());
				selectedActivity.setShowName(selectedCustomizedActivity.getLongName());
				chapterTitledPaneController.setTitledPaneListViewCellFactory();
			}
		}
	}
	
	public void selectedActivityListViewClicked(ListView<SelectedActivity> titledPaneListView, String paneTitle) {
		SelectedActivity selectedActivity = titledPaneListView.getSelectionModel().getSelectedItem();
		if(selectedActivity != null) {
			selectedChapter = paneTitle;
			fillCustomizedActivitiesListView(selectedActivity.getActivityType());
			setCustomizedActivityListViewCellFactory();
		}
	}
		
	public void fileNameHyperlinkClicked() {
		try {
			String fileName = fileNameHyperlink.getText().trim();
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
	
	public void updateActivityTypeDescriptionTextArea() {
		ActivityType selectedActivityType = activitityTypeListView.getSelectionModel().getSelectedItem();
		for(ActivityType activityType: activityTypes) {
			if(activityType.getLongName().contentEquals(selectedActivityType.getLongName())) {
				activityTypeDescriptionTextField.setText(activityType.getDescription());
			}
		}
	}
	
	public void updateCustomizedActivityInfo() {
		Activity selectedCustomizedActivity = customizedActivitiesListView.getSelectionModel().getSelectedItem();
		if (selectedCustomizedActivity != null) {
			for (Activity customActivity : customizedActivities) {
				if (customActivity.getLongName().contentEquals(selectedCustomizedActivity.getLongName())) {
					shortNameTextField.setText(customActivity.getShortName());
					longNameTextField.setText(customActivity.getLongName());
					descriptionTextField.setText(customActivity.getDescription());
					directionsTextField.setText(customActivity.getDirections());
					objectivesTextField.setText(customActivity.getObjectives());
					fileNameHyperlink.setText(customActivity.getFileName());
				}
			}
		}
	}
	
	public void clearCustomizedActivityInfo() {
		shortNameTextField.setText(null);
		longNameTextField.setText(null);
		descriptionTextField.setText(null);
		directionsTextField.setText(null);
		objectivesTextField.setText(null);
		fileNameHyperlink.setText(null);
	}
	
	public Chapter getChapter(String chapterName) {
		for(Chapter chapter: chaptersCreated) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	public ActivityType getActivityType(String activityTypeName) {
		for(ActivityType activityType: activityTypes) {
			if(activityType.getLongName().contentEquals(activityTypeName)) {
				return activityType;
			}
		}
		logger.debug("ActivityType returned is null.");
		return null;
	}
	
	void setActivityTypeListViewDrag(ListView<ActivityType> listView) {
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
	
	void setSelectedActivityListViewDrag(ListView<SelectedActivity> draggedListView) {
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
//					Pane parent = (Pane) draggedListView.getParent();
//					System.out.println("Drag Drop Parent: " + parent);					
//					System.out.println("Selected Item: " + draggedListView.getSelectionModel().getSelectedIndex());
//					int sourceIndex = parent.getChildren().indexOf(source);		
//					System.out.println("Drag Source index: " + sourceIndex);
//					int targetIndex = parent.getChildren().indexOf(draggedListView);				
//					System.out.println("Drag Target index: " + targetIndex);
//					List<javafx.scene.Node> nodes = new ArrayList<>(parent.getChildren());
//					if (sourceIndex >= 0) {
//						if (sourceIndex < targetIndex) {
//							Collections.rotate(nodes.subList(sourceIndex, targetIndex + 1), -1);
//						} else {
//							Collections.rotate(nodes.subList(targetIndex, sourceIndex + 1), 1);
//						}
//					}
//					parent.getChildren().clear();
//					parent.getChildren().addAll(nodes);
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
	
	public void parseActivityTypes() {
		activityTypes.clear();
		File activityTypesFile = new File(pathToERBFolder + "\\Activities\\Activity_Types.xml");
		if (activityTypesFile.exists() && activityTypesFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(activityTypesFile);
				doc.getDocumentElement().normalize();
				NodeList activityTypeNodeList = doc.getElementsByTagName("activityType");
				for (int i = 0; i < activityTypeNodeList.getLength(); i++) {
					Node activityTypeNode = activityTypeNodeList.item(i);
					// Activity Type
					if (activityTypeNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityTypeElement = (Element) activityTypeNode;
						String longName = activityTypeElement.getAttribute("longName");
						String shortName = activityTypeElement.getAttribute("shortName");
						String description = activityTypeElement.getAttribute("description");
						String fileExt = activityTypeElement.getAttribute("fileExt");
						ActivityType activityType = new ActivityType(longName, shortName, description, fileExt);
						activityTypes.add(activityType);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(activityTypesFile.getPath() + " either does not exist or cannot be read");
		}
	}

	public void parseAvailableActivities() {
		customizedActivities.clear();
		File activitesFile = new File(pathToERBFolder + "\\Activities\\Available_Activites.xml");
		if (activitesFile.exists() && activitesFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(activitesFile);
				doc.getDocumentElement().normalize();
				NodeList activitiesNodeList = doc.getElementsByTagName("activity");
				for (int i = 0; i < activitiesNodeList.getLength(); i++) {
					Node activityNode = activitiesNodeList.item(i);
					// Activity
					if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityElement = (Element) activityNode;
						String activityTypeName = activityElement.getAttribute("activityType");
						ActivityType activityType = getActivityType(activityTypeName);
						String shortName = activityElement.getAttribute("shortName");
						String longName = activityElement.getAttribute("longName");
						String fileName = activityElement.getAttribute("fileName");
						String directions = activityElement.getAttribute("directions");
						String objectives = activityElement.getAttribute("objectives");
						String description = activityElement.getAttribute("description");
						Activity activity = new Activity(activityType, shortName, longName, fileName, directions,
								objectives, description);
						customizedActivities.add(activity);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(activitesFile.getPath() + " either does not exist or cannot be read");
		}
	}
	
}
