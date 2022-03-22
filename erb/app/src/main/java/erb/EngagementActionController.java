package erb;

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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;

public class EngagementActionController implements Initializable{

	@FXML
	Label chapterLabel;
	@FXML
	Label erbPathwayLabel;
	@FXML
	HBox pathwayHBox;
	@FXML
	TreeView<String> treeView;
	@FXML
	Button previousButton;
	@FXML
	Button skipButton;
	@FXML
	Button nextButton;
	
	String currentChapter = null;
	ArrayList<Chapter> dataChapters = new ArrayList<Chapter>();
	ArrayList<TreeItem<String>> allTreeItems = new ArrayList<TreeItem<String>>();
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";

	public EngagementActionController() {
		parseDataFromSetup();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
		handleNavigationButtonsShown(null, null);
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
	
	public void treeViewClicked() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
			if (parentTreeItem != null) {
				String parentTreeItemValue = parentTreeItem.getValue().trim();
				String selectedTreeItemValue = selectedTreeItem.getValue().trim();
				if (selectedTreeItemValue.length() > 0) {
					if (parentTreeItemValue.contentEquals("ERB")) {
						currentChapter = selectedTreeItemValue;
						loadChapterPathway(selectedTreeItemValue);
						handleNavigationButtonsShown(selectedTreeItem, null);
					} else {
						currentChapter = parentTreeItemValue;
						loadChapterPathway(parentTreeItemValue);
						handleNavigationButtonsShown(selectedTreeItem, parentTreeItem);
					}
				}
			}
		} else {
			handleNavigationButtonsShown(null, null);
		}
	}
	
	public void handleNavigationButtonsShown(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		if (selectedTreeItem != null) {
			if (parentTreeItem == null) {
				previousButton.setVisible(false);
				skipButton.setVisible(false);
				nextButton.setVisible(true);
			} else {
				int numberOfChildrenInParent = parentTreeItem.getChildren().size();
				if (numberOfChildrenInParent > 1) {
					int indexOfSelectedItem = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					if (indexOfSelectedItem == 0) {
						previousButton.setVisible(false);
						skipButton.setVisible(false); //true
						nextButton.setVisible(true);
					} else if (indexOfSelectedItem == parentTreeItem.getChildren().size() - 1) {
						previousButton.setVisible(true);
						skipButton.setVisible(false);
						nextButton.setVisible(false);
					} else {
						previousButton.setVisible(true);
						skipButton.setVisible(false); //true
						nextButton.setVisible(true);
					}
				} else {
					previousButton.setVisible(false);
					skipButton.setVisible(false);
					nextButton.setVisible(false);
				}
			}
		} else {
			previousButton.setVisible(false);
			skipButton.setVisible(false);
			nextButton.setVisible(false);
		}
	}
	
	public void loadChapterPathway(String chapterName) {
		pathwayHBox.getChildren().remove(erbPathwayLabel);
		Chapter chapter = getChapter(chapterName);
		if (chapter != null) {
			if (chapterLabel.getText() == null || !chapterLabel.getText().contentEquals(chapter.getStringName())) {
				chapterLabel.setText(chapter.getStringName());
				pathwayHBox.getChildren().clear();
				for (Activity activity : chapter.getUserSelectedActivities()) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ERBPathwayDiagram.fxml"));
						ERBPathwayDiagramController erbPathwayDiagramController = new ERBPathwayDiagramController(activity);
						fxmlLoader.setController(erbPathwayDiagramController);
						Parent root = fxmlLoader.load();
						pathwayHBox.getChildren().add(root);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
	}
	
	public void fillTreeView() {
		TreeItem<String> rootTreeItem = new TreeItem<String>("ERB");
		allTreeItems.add(rootTreeItem);
		rootTreeItem.setExpanded(true);
		treeView.setRoot(rootTreeItem);
		for (Chapter chapter : dataChapters) {
			TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
			allTreeItems.add(chapterTreeItem);
			rootTreeItem.getChildren().add(chapterTreeItem);
			for (Activity activity : chapter.getUserSelectedActivities()) {
				TreeItem<String> activityTreeItem = new TreeItem<String>(activity.getLongName());
				allTreeItems.add(activityTreeItem);
				chapterTreeItem.getChildren().add(activityTreeItem);
			}
		}
	}
	
	public Chapter getChapter(String chapterName) {
		for(Chapter chapter : dataChapters) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		return null;
	}
	
	public void parseDataFromSetup() {
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
								String activityShortName = activityElement.getAttribute("shortName");
								String activityLongName = activityElement.getAttribute("longName");
								String activityFileName = activityElement.getAttribute("fileName");
								String activityDirections = activityElement.getAttribute("directions");
								String activityObjectives = activityElement.getAttribute("objectives");
								String activityDescription = activityElement.getAttribute("description");
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
										Activity activity = new Activity(activityType, activityShortName,
												activityLongName, activityFileName, activityDirections,
												activityObjectives, activityDescription);
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
