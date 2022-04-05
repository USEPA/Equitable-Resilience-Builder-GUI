package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.epa.erb.Activity;
import com.epa.erb.ActivityType;
import com.epa.erb.Chapter;
//import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.worksheet.WorksheetContentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EngagementActionController implements Initializable{

	@FXML
	HBox headingHBox;
	@FXML
	Label chapterLabel;
	@FXML
	Label erbPathwayLabel;
	@FXML
	HBox pathwayHBox;
	@FXML
	TreeView<String> treeView;
	@FXML
	VBox contentVBox;
	@FXML
	VBox attributeVBox;
	@FXML
	Button previousButton;
	@FXML
	Button skipButton;
	@FXML
	Button nextButton;
	@FXML
	VBox keyVBox;
	@FXML
	HBox mainHBox;
	@FXML
	ScrollPane attributeScrollPane;
	
	public EngagementActionController() {
		
	}
	
	private String currentChapter = null; //Tracks the current user selected chapter
	private ArrayList<Chapter> dataChapters = new ArrayList<Chapter>(); //List of chapter objects parsed from .xml file 
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	HashMap<TreeItem<String>, String> treeMap = new HashMap<TreeItem<String>, String>(); //Holds the tree items mapped to a chapter name or activity GUID
	private ArrayList<AttributePanelController> listOfAttributePanelControllers = new ArrayList<AttributePanelController>(); //Holds all of the attribute panel that are loaded
	
	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parseDataFromSetup();
		fillTreeView();
		handleNavigationButtonsShown(null, null);
		handleControls();
		loadChapterERBPathway();
		
	}
	
	private void loadERBLandingContent() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(dataChapters);
			fxmlLoader.setController(chapterLandingController);
			Parent root = fxmlLoader.load();
			chapterLandingController.setAboutText();
			chapterLandingController.setActivitiesText();
			chapterLandingController.setHeadingLabel();
			contentVBox.getChildren().clear();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadChapterLandingContent(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(chapter);
			fxmlLoader.setController(chapterLandingController);
			Parent root = fxmlLoader.load();
			chapterLandingController.setAboutText();
			chapterLandingController.setActivitiesText();
			chapterLandingController.setHeadingLabel();
			contentVBox.getChildren().clear();
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
//	
//	private void loadSampleNB() {
//		try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
//			NoteBoardContentController noteBoardContentController = new NoteBoardContentController();
//			fxmlLoader.setController(noteBoardContentController);
//			Parent root = fxmlLoader.load();
//			contentVBox.getChildren().add(root);
//			VBox.setVgrow(root, Priority.ALWAYS);
//		}catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//	}
	
	private void loadSampleContent() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/SampleContent.fxml"));
			SampleContentController sampleContentController = new SampleContentController();
			fxmlLoader.setController(sampleContentController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void loadAttributeInfo(String attributeLabel, String attributeText, String attributeColor) {
		if (attributeText.trim().length() > 0 && !containsAttribute(attributeLabel)) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/AttributePane.fxml"));
				AttributePanelController attributePanelController = new AttributePanelController();
				fxmlLoader.setController(attributePanelController);
				Parent root = fxmlLoader.load();
				attributePanelController.setAttributeLabel(attributeLabel);
				attributePanelController.setAttributeTextFlow(attributeText);
				attributePanelController.setAttributeLabelColor(attributeColor);
				attributeVBox.getChildren().add(root);
				VBox.setVgrow(root, Priority.ALWAYS);
				listOfAttributePanelControllers.add(attributePanelController);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	private void handleControls() {
		treeView.setOnMouseClicked(e-> treeViewClicked());
	}
	
	@FXML
	public void previousButtonAction() {
		if(currentChapter != null) {
			TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
			if(selectedTreeItem != null && parentTreeItem != null) {
				int currentIndex =  parentTreeItem.getChildren().indexOf(selectedTreeItem);
				TreeItem<String> itemToSelect = parentTreeItem.getChildren().get(currentIndex-1);
				treeView.getSelectionModel().select(itemToSelect);
				treeViewClicked();
			}
		}
	}
	
	@FXML
	public void skipButtonAction() {
		
	}
	
	@FXML
	public void nextButtonAction() {
		if (currentChapter != null) {
			TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
			if (selectedTreeItem != null && parentTreeItem != null) {
				if (parentTreeItem.getValue().contains("ERB")) {
					TreeItem<String> itemToSelect = selectedTreeItem.getChildren().get(0);
					treeView.getSelectionModel().select(itemToSelect);
					treeViewClicked();
				} else {
					int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					TreeItem<String> itemToSelect = parentTreeItem.getChildren().get(currentIndex + 1);
					treeView.getSelectionModel().select(itemToSelect);
					treeViewClicked();
				}
			}
		}
	}
	
	void treeViewClicked() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
			if (parentTreeItem != null) {
				String parentTreeItemValue = parentTreeItem.getValue().trim();
				String selectedTreeItemValue = selectedTreeItem.getValue().trim();
				if (selectedTreeItemValue.length() > 0) {
					if (parentTreeItemValue.contains("ERB")) { //Is Chapter 
						currentChapter = selectedTreeItemValue;
						contentVBox.getChildren().clear();
						cleanAttributeVBox();
						mainHBox.getChildren().remove(attributeScrollPane);
						loadChapterLandingContent(getChapter(currentChapter));
						loadActivityERBPathway(selectedTreeItemValue);
						handleNavigationButtonsShown(selectedTreeItem, null);
					} else { //Is Activity
						currentChapter = parentTreeItemValue;
						if(!mainHBox.getChildren().contains(attributeScrollPane)) {
							mainHBox.getChildren().add(1,attributeScrollPane);
						}
						loadActivityERBPathway(parentTreeItemValue);
						loadActivityContentPanel(selectedTreeItem);
						handleNavigationButtonsShown(selectedTreeItem, parentTreeItem);
					}
				}
			} else {
				if(selectedTreeItem != null) {
					contentVBox.getChildren().clear();
					cleanAttributeVBox();						
					loadChapterERBPathway();
					loadERBLandingContent();
				}
			}
		} else {
			handleNavigationButtonsShown(null, null);
		}
	}
	
	private void loadActivityContentPanel(TreeItem<String> selectedTreeItem) {
		contentVBox.getChildren().clear();
		cleanAttributeVBox();	
		if(selectedTreeItem.getValue().contentEquals("Social Vulnerability Activity Template")) {
			String GUID = treeMap.get(selectedTreeItem);
			Activity selectedActivity = getActivity(GUID);
			loadAttributeInfo("Objective", selectedActivity.getObjectives(), "#92A6EF");
			loadAttributeInfo("Instructions", "1. On a white board, list the hazards of concern to your community in a column down the left side, and draw horizontal lines between them. (see example)" + "\n" + 
					"2. Reflect on the stories and data that were shared at the beginning of the workshop. Who were the groups of people that were mentioned who experienced impacts from hazards and disasters? What were the impacts they experienced?" + "\n" + 
					"3. As you go, add additional who's and what's for current or potential future impacts" + "\n" +
					"4. After about 30 minutes or when the discussion is at a lull, begin discussing why these impacts happen. Give everyone a few minutes to think and write some \"why's\" on pink post-it notes. Then have people place the pink notes near the blue and yellow notes, and share their thoughts with the group" + "\n" +
					"5. After about 15 minutes, introduce the phases of disaster mitigation-response-recovery. Use colored dots to label each of the \"why's\" with one or more phases", "#86E596");
			loadSampleContent();
		} else if (selectedTreeItem.getValue().contentEquals("Mapping Activity Instructions")) {
			String GUID = treeMap.get(selectedTreeItem);
			Activity selectedActivity = getActivity(GUID);
			loadAttributeInfo("Objective", selectedActivity.getObjectives(), "#92A6EF");
			loadAttributeInfo("Instructions", selectedActivity.getDirections(), "#86E596");
			loadSampleWK(selectedActivity);
		}
	}
	
	private void loadChapterERBPathway() {
		pathwayHBox.getChildren().clear();
		headingHBox.getChildren().remove(keyVBox);
		chapterLabel.setText("Chapters");
		int count = 0;
		for (Chapter chapter : dataChapters) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBChapterDiagram.fxml"));
				ERBChapterDiagramController erbChapterDiagramController = new ERBChapterDiagramController(chapter);
				fxmlLoader.setController(erbChapterDiagramController);
				Parent root = fxmlLoader.load();
				if(dataChapters.size() == 1 && count == 0) {
					erbChapterDiagramController.hideLeftLeadingLine();
					erbChapterDiagramController.hideRightLeadingLine();
				} else if(count == dataChapters.size()-1) {
					erbChapterDiagramController.hideRightLeadingLine();
				} else if(count ==0) {
					erbChapterDiagramController.hideLeftLeadingLine();
				}
				pathwayHBox.getChildren().add(root);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			count++;
		}
	}

	private void loadActivityERBPathway(String chapterName) {
		if(!headingHBox.getChildren().contains(keyVBox)) {
			headingHBox.getChildren().add(1,keyVBox);
		}
		Chapter chapter = getChapter(chapterName);
		if (chapter != null) {
			if (chapterLabel.getText() == null || !chapterLabel.getText().contentEquals(chapter.getStringName())) {
				pathwayHBox.getChildren().clear();
				chapterLabel.setText(chapter.getStringName() + " Activities");
				int count =0;
				for (Activity activity : chapter.getUserSelectedActivities()) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBPathwayDiagram.fxml"));
						ERBPathwayDiagramController erbPathwayDiagramController = new ERBPathwayDiagramController(activity, this);
						fxmlLoader.setController(erbPathwayDiagramController);
						Parent root = fxmlLoader.load();
						if(chapter.getNumberOfUserSelectedActivities() ==1) {
							erbPathwayDiagramController.hideLeftLeadingLine();
							erbPathwayDiagramController.hideRightLeadingLine();
						}else if(count == chapter.getNumberOfUserSelectedActivities()-1) {
							erbPathwayDiagramController.hideRightLeadingLine();
						} else if(count ==0) {
							erbPathwayDiagramController.hideLeftLeadingLine();
						}
						pathwayHBox.getChildren().add(root);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					count++;
				}
			}
		}
	}
	
	private void fillTreeView() {
		TreeItem<String> rootTreeItem = new TreeItem<String>("ERB Pathway");
		rootTreeItem.setExpanded(true);
		treeView.setRoot(rootTreeItem);
		treeMap.put(rootTreeItem, "0");
		for (Chapter chapter : dataChapters) {
			TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
			rootTreeItem.getChildren().add(chapterTreeItem);
			treeMap.put(chapterTreeItem, chapter.getNumericName());
			for (Activity activity : chapter.getUserSelectedActivities()) {
				TreeItem<String> activityTreeItem = new TreeItem<String>(activity.getLongName());
				chapterTreeItem.getChildren().add(activityTreeItem);
				treeMap.put(activityTreeItem, activity.getGUID());
			}
		}
	}
	
	private void handleNavigationButtonsShown(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		if (selectedTreeItem != null) {
			if (parentTreeItem == null) {
				previousButton.setDisable(true);
				skipButton.setDisable(true);
				nextButton.setDisable(false);
			} else {
				int numberOfChildrenInParent = parentTreeItem.getChildren().size();
				if (numberOfChildrenInParent > 1) {
					int indexOfSelectedItem = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					if (indexOfSelectedItem == 0) {
						previousButton.setDisable(true);
						skipButton.setDisable(true); //true
						nextButton.setDisable(false);
					} else if (indexOfSelectedItem == parentTreeItem.getChildren().size() - 1) {
						previousButton.setDisable(false);
						skipButton.setDisable(true);
						nextButton.setDisable(true);
					} else {
						previousButton.setDisable(false);
						skipButton.setDisable(true); //true
						nextButton.setDisable(false);
					}
				} else {
					previousButton.setDisable(true);
					skipButton.setDisable(true);
					nextButton.setDisable(true);
				}
			}
		} else {
			previousButton.setDisable(true);
			skipButton.setDisable(true);
			nextButton.setDisable(true);
		}
	}
	
	private Chapter getChapter(String chapterName) {
		for(Chapter chapter : dataChapters) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	private Activity getActivity(String GUID) {
		for(Chapter chapter: dataChapters) {
			for(Activity activity : chapter.getUserSelectedActivities()) {
				if(activity.getGUID().contentEquals(GUID)) {
					return activity;
				}
			}
		}
		logger.debug("Activity returned is null");
		return null;
	}
	
	String getSelectedGUID() {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
		if(treeItem != null) {
			return treeMap.get(treeItem);
		} else {
			logger.debug("Selected GUID is null");
			return null;
		}
	}
	
	void cleanAttributeVBox() {
		attributeVBox.getChildren().clear();
		listOfAttributePanelControllers.clear();
	}
	
	boolean containsAttribute(String attributeLabel) {
		for(AttributePanelController attributePanelController : listOfAttributePanelControllers) {
			if(attributePanelController.getAttributeLabelText().contentEquals(attributeLabel)) {
				return true;
			}
		}
		return false;
	}
		
	TreeView<String> getTreeView() {
		return treeView;
	}
	
	HashMap<TreeItem<String>, String> getTreeMap() {
		return treeMap;
	}

	/**
	 * Parses the chapter data that was create by the user in pt. 1 of the tool
	 */
	private void parseDataFromSetup() {
		File dataFile = new File(pathToERBFolder + "\\EngagementSetupTool\\Data.xml");
		if (dataFile.exists() && dataFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(dataFile);
				doc.getDocumentElement().normalize();
				NodeList chapterNodeList = doc.getElementsByTagName("chapter");
				for (int i = 0; i < chapterNodeList.getLength(); i++) {
					Node chapterNode = chapterNodeList.item(i);
					// Chapter
					if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element chapterElement = (Element) chapterNode;
						int chapterNum = Integer.parseInt(chapterElement.getAttribute("chapterNum"));
						String chapterNumericName = chapterElement.getAttribute("numericName");
						String chapterStringName = chapterElement.getAttribute("stringName");
						String chapterDescription = chapterElement.getAttribute("description");
						Chapter chapter = new Chapter(chapterNum, chapterNumericName, chapterStringName,
								chapterDescription);
						NodeList activityNodeList = chapterNode.getChildNodes();
						for (int j = 0; j < activityNodeList.getLength(); j++) {
							Node activityNode = activityNodeList.item(j);
							// Activity
							if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element activityElement = (Element) activityNode;
								String activityStatus = activityElement.getAttribute("status");
								String activityShortName = activityElement.getAttribute("shortName");
								String activityLongName = activityElement.getAttribute("longName");
								String activityFileName = activityElement.getAttribute("fileName");
								String activityDirections = activityElement.getAttribute("directions");
								String activityObjectives = activityElement.getAttribute("objectives");
								String activityDescription = activityElement.getAttribute("description");
								String activityMaterials= activityElement.getAttribute("materials");
								String activityTime = activityElement.getAttribute("time");
								String activityWho = activityElement.getAttribute("who");
								NodeList activityTypeNodeList = activityNode.getChildNodes();
								for (int k = 0; k < activityTypeNodeList.getLength(); k++) {
									Node activityTypeNode = activityTypeNodeList.item(k);
									// ActivityType
									if (activityTypeNode.getNodeType() == Node.ELEMENT_NODE) {
										Element activityTypeElement = (Element) activityTypeNode;
										String activityTypeLongName = activityTypeElement.getAttribute("longName");
										String activityTypeShortName = activityTypeElement.getAttribute("shortName");
										String activityTypeDescription = activityTypeElement
												.getAttribute("description");
										String activityTypeFileExt = activityTypeElement.getAttribute("fileExt");
										ActivityType activityType = new ActivityType(activityTypeLongName,
												activityTypeShortName, activityTypeDescription, activityTypeFileExt);
										Activity activity = new Activity(activityType, activityStatus, activityShortName,
												activityLongName, activityFileName, activityDirections,
												activityObjectives, activityDescription, activityMaterials, activityTime, activityWho);
										chapter.addUserSelectedActivity(activity);
									}
								}
							}
						}
						dataChapters.add(chapter);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(dataFile.getPath() + " either does not exist or cannot be read");
		}
	}
	
}
