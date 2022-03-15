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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class EngagementSetupController implements Initializable {

	@FXML
	HBox erbPathwayHBox;
	@FXML
	ListView<ActivityType> activitityTypeListView;
	@FXML
	TextArea activityTypeDescriptionTextField;
	@FXML
	Button addActivityButton;
	@FXML
	Button removeActivityButton;
	@FXML
	Button addChapterButton;
	@FXML
	TreeView<String> selectedActivitiesTreeView;
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
	
	ArrayList<Chapter> chaptersInTree = new ArrayList<Chapter>();
	TreeItem<String> rootTreeItem = new TreeItem<String>("Activities");
	ArrayList<Activity> customizedActivities = new ArrayList<Activity>();
	ArrayList<ActivityType> activityTypes = new ArrayList<ActivityType>();
	ArrayList<TreeItem<String>> allTreeItems = new ArrayList<TreeItem<String>>();

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
		rootTreeItem.setExpanded(true);
		selectedActivitiesTreeView.setRoot(rootTreeItem);
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
	public void addActivityButtonAction() {
		ActivityType selectedActivityType = activitityTypeListView.getSelectionModel().getSelectedItem();
		String selectedChapter = selectedActivitiesTreeView.getSelectionModel().getSelectedItem().getValue();
		if (selectedActivityType.getLongName().length() > 0 && selectedChapter != null) {
			TreeItem<String> activityTypeTreeItem = new TreeItem<String>(selectedActivityType.getLongName());
			TreeItem<String> chapterTreeItem = getTreeItem(selectedChapter);
			if(chapterTreeItem!= null) {
				allTreeItems.add(activityTypeTreeItem);
				chapterTreeItem.getChildren().add(activityTypeTreeItem);
			}
		} else {
			logger.error("Selected Activity Type = " + selectedActivityType + " | Selected Chapter = " + selectedChapter);
		}
	}
	
	@FXML
	public void removeActivityButtonAction() {

	}
	
	@FXML
	public void addChapterButtonAction() {
		int chapterNum = chaptersInTree.size() + 1;
		Chapter chapter = new Chapter(chapterNum, chapterNum + ".0", "Chapter " + chapterNum, "");
		TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
		chapterTreeItem.setExpanded(true);
		rootTreeItem.getChildren().add(chapterTreeItem);
		allTreeItems.add(chapterTreeItem);
		chaptersInTree.add(chapter);
	}
	
	@FXML
	public void assignButtonAction() {
		Activity selectedCustomizedActivity = customizedActivitiesListView.getSelectionModel().getSelectedItem();
		for(Activity customActivity : customizedActivities) {
			if(customActivity.getLongName().contentEquals(selectedCustomizedActivity.getLongName())) {
				String parentName = selectedActivitiesTreeView.getSelectionModel().getSelectedItem().getParent().getValue();
				Chapter chapter =  getChapter(parentName);
				if(chapter != null) {
					chapter.addUserSelectedActivity(selectedCustomizedActivity);
				}
			}
		}
		printAllChapters();
	}
	
	public void printAllChapters() {
		for(Chapter chapter: chaptersInTree) {
			System.out.println(chapter.toString());
		}
	}
	
	@FXML
	public void treeItemSelected() {
		TreeItem<String> selectedTreeItem = selectedActivitiesTreeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			if(!selectedTreeItem.getValue().contains("Chapter") && !selectedTreeItem.getValue().contentEquals("Activities")) {
				fillCustomizedActivitiesListView(selectedTreeItem.getValue());
				setCustomizedActivityListViewCellFactory();
			}
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
	
	public Chapter getChapter(String chapterName) {
		for(Chapter chapter: chaptersInTree) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	public TreeItem<String> getTreeItem(String chapterName){
		for(TreeItem<String> treeItem: allTreeItems) {
			if(treeItem.getValue().contentEquals(chapterName)) {
				return treeItem;
			}
		}
		logger.debug("TreeView returned is null");
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
