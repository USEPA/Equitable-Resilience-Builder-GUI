package erb;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class WizardContainerController implements Initializable{
	
	Wizard wizard;
	
	public WizardContainerController(Wizard wizard) {
		this.wizard = wizard;
	}
	
	ArrayList<TreeItem<String>> listOfTreeItems = new ArrayList<TreeItem<String>>();
	
	@FXML
	VBox wizardVBox;
	@FXML
	TreeView<String> treeView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
		loadWizardPanel(0);
	}
	
	public void loadWizardPanel(int wizardPanelIndex) {
		clearContent();
		addContent(wizardPanelIndex);
	}
	
	public void loadWizardPanel(String wizardPanelAccessibleText) {
		clearContent();
		addContent(wizardPanelAccessibleText);
	}
	
	private void clearContent() {
		wizardVBox.getChildren().clear();
	}
	
	private void addContent(int wizardPanelIndex) {
		Node panelToAdd = wizard.getPanel(wizardPanelIndex);
		if (panelToAdd != null) {
			wizardVBox.getChildren().add(panelToAdd);
			VBox.setVgrow(panelToAdd, Priority.ALWAYS);
		} else {
			System.out.println("ERROR: Panel to add is null");
		}
	}
	
	private void addContent(String wizardPanelAccessibleText) {
		Node panelToAdd = wizard.getPanel(wizardPanelAccessibleText);
		if(panelToAdd != null) {
			wizardVBox.getChildren().add(panelToAdd);
			VBox.setVgrow(panelToAdd, Priority.ALWAYS);
		}else {
			System.out.println("ERROR: Panel to add is null");
		}
	}
	
	public Node getCurrentWizardPanel() {
		return wizardVBox.getChildren().get(0);
	}
	
	public int getCurrentWizardPanelIndex() {
		Node currentPanelNode = wizardVBox.getChildren().get(0);
		if(currentPanelNode != null) {
			return wizard.getPanelIndex(currentPanelNode);	
		}else {
			return -1;
		}
	}
	
	public String getCurrentWizardPanelAccessibleText() {
		Node currentPanelNode = wizardVBox.getChildren().get(0);
		if(currentPanelNode != null) {
			return wizard.getPanelAccessibleText(currentPanelNode);	
		}else {
			return null;
		}
	}
	
	public int getCurrentChapter() {
		Node currentPanelNode = wizardVBox.getChildren().get(0);
		return wizard.getChapter(currentPanelNode);
	}
	
	public int getCurrentStep() {
		Node currentPanelNode = wizardVBox.getChildren().get(0);
		return wizard.getStep(currentPanelNode);
	}
	
	public int getCurrentSubStep() {
		Node currentPanelNode = wizardVBox.getChildren().get(0);
		return wizard.getSubStep(currentPanelNode);
	}
	
	public void handleTreeViewItemSelection() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		String panelId = parsePanelId(selectedTreeItem);
		if(panelId != null && panelId.length() > 0) {
			loadWizardPanel(panelId);
		}
	}
	
	public String parsePanelId(TreeItem<String> treeItem) {
		String treeItemText = treeItem.getValue().toString();
		StringBuilder stringBuilder = new StringBuilder();
		if (treeItemText != null) {
			Pattern numberPattern = Pattern.compile("([0-9])");
			Matcher matcher = numberPattern.matcher(treeItemText);
			while (matcher.find()) {
				stringBuilder.append(matcher.group(1) + ".");
			}
			if (stringBuilder.length() > 0) {
				if (treeItemText.contains("Landing")) {
					stringBuilder.append("0");
				} else {
					stringBuilder.append("1");
				}
			} else {
				return "0.0";
			}

		}
		return stringBuilder.toString();
	}
	
	public void updateSelectedTreeItem(String panelAccessibleText) {
		if(panelAccessibleText.length() > 3) {
			Pattern numberPattern = Pattern.compile("([0-9])");
			Matcher matcher = numberPattern.matcher(panelAccessibleText);
			ArrayList<String> matches = new ArrayList<String>();
			while (matcher.find()) {
				matches.add(matcher.group(1));
			}
			if(matches.size() == 3 && matches.get(2).contentEquals("0")) {
				String treeString = "Chapter " + matches.get(0) + " Step " + matches.get(1) + " Landing";
				selectTreeItem(treeString);
			}else if (matches.size() == 3) {
				String treeString = "Chapter " + matches.get(0) + " Step " + matches.get(1);
				selectTreeItem(treeString);
			}
		}else if (panelAccessibleText.length() == 3){
			String replacedString = "Chapter " + panelAccessibleText.replaceAll(".0", "").trim() + " Landing";
			selectTreeItem(replacedString);
		}
	}
	
	public void selectTreeItem(String treeItemText) {
		for(TreeItem<String> treeItem: listOfTreeItems) {
			if(treeItem.getValue().contentEquals(treeItemText)) {
				treeView.getSelectionModel().select(treeItem);
			}
		}
	}
	
	public void fillTreeView() {
		TreeItem<String> rootTreeItem = new TreeItem<String>("Wizard Landing");
		treeView.setRoot(rootTreeItem);
		rootTreeItem.setExpanded(true);
		
		ArrayList<TreeItem<String>> chapter1TreeViewContent = createChapter1TreeViewContent(rootTreeItem);
		listOfTreeItems.addAll(chapter1TreeViewContent);
		ArrayList<TreeItem<String>> chapter2TreeViewContent = createChapter2TreeViewContent(rootTreeItem);
		listOfTreeItems.addAll(chapter2TreeViewContent);
		ArrayList<TreeItem<String>> chapter3TreeViewContent = createChapter3TreeViewContent(rootTreeItem);
		listOfTreeItems.addAll(chapter3TreeViewContent);
		ArrayList<TreeItem<String>> chapter4TreeViewContent = createChapter4TreeViewContent(rootTreeItem);
		listOfTreeItems.addAll(chapter4TreeViewContent);
		ArrayList<TreeItem<String>> chapter5TreeViewContent = createChapter5TreeViewContent(rootTreeItem);
		listOfTreeItems.addAll(chapter5TreeViewContent);
		
		treeView.getStylesheets().add("/TreeView.css");
		treeView.setOnMouseClicked(e-> handleTreeViewItemSelection());
		treeView.setCellFactory((t) -> new TreeCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(item);
				if (item != null && item.contains("Chapter 1")) {
					setStyle("-fx-text-fill: #C67B7B;");
				} else if (item != null && item.contains("Chapter 2")) {
					setStyle("-fx-text-fill: #8aa165;");
				} else if (item != null && item.contains("Chapter 3")) {
					setStyle("-fx-text-fill: #8c638d;");
				} else if (item != null && item.contains("Chapter 4")) {
					setStyle("-fx-text-fill: #5f76a4;");
				} else if (item != null && item.contains("Chapter 5")) {
					setStyle("-fx-text-fill: #da70c5;");
				} else {
					setStyle("-fx-text-fill: #000000;");
				}
			}
		});
	}
	
	public ArrayList<TreeItem<String>> createChapter1TreeViewContent(TreeItem<String> rootTreeItem) {
		ArrayList<TreeItem<String>> chapter1TreeViewContent = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> chapter1RootTreeItem = new TreeItem<String>("Chapter 1 Landing");
		chapter1TreeViewContent.add(chapter1RootTreeItem);
		
		TreeItem<String> chapter1Step1LandingTreeItem = new TreeItem<String>("Chapter 1 Step 1 Landing");
		TreeItem<String> chapter1Step1TreeItem = new TreeItem<String>("Chapter 1 Step 1");
		chapter1Step1LandingTreeItem.getChildren().add(chapter1Step1TreeItem);
		chapter1Step1LandingTreeItem.setExpanded(true);
		chapter1TreeViewContent.add(chapter1Step1LandingTreeItem);
		chapter1TreeViewContent.add(chapter1Step1TreeItem);
		
		TreeItem<String> chapter1Step2LandingTreeItem = new TreeItem<String>("Chapter 1 Step 2 Landing");
		TreeItem<String> chapter1Step2TreeItem = new TreeItem<String>("Chapter 1 Step 2");
		chapter1Step2LandingTreeItem.getChildren().add(chapter1Step2TreeItem);
		chapter1Step2LandingTreeItem.setExpanded(true);
		chapter1TreeViewContent.add(chapter1Step2LandingTreeItem);
		chapter1TreeViewContent.add(chapter1Step2TreeItem);
		
		TreeItem<String> chapter1Step3LandingTreeItem = new TreeItem<String>("Chapter 1 Step 3 Landing");
		TreeItem<String> chapter1Step3TreeItem = new TreeItem<String>("Chapter 1 Step 3");
		chapter1Step3LandingTreeItem.getChildren().add(chapter1Step3TreeItem);
		chapter1Step3LandingTreeItem.setExpanded(true);
		chapter1TreeViewContent.add(chapter1Step3LandingTreeItem);
		chapter1TreeViewContent.add(chapter1Step3TreeItem);
		
		TreeItem<String> chapter1Step4LandingTreeItem = new TreeItem<String>("Chapter 1 Step 4 Landing");
		TreeItem<String> chapter1Step4TreeItem = new TreeItem<String>("Chapter 1 Step 4");
		chapter1Step4LandingTreeItem.getChildren().add(chapter1Step4TreeItem);
		chapter1Step4LandingTreeItem.setExpanded(true);
		chapter1TreeViewContent.add(chapter1Step4LandingTreeItem);
		chapter1TreeViewContent.add(chapter1Step4TreeItem);
		
		TreeItem<String> chapter1Step5LandingTreeItem = new TreeItem<String>("Chapter 1 Step 5 Landing");
		TreeItem<String> chapter1Step5TreeItem = new TreeItem<String>("Chapter 1 Step 5");
		chapter1Step5LandingTreeItem.getChildren().add(chapter1Step5TreeItem);
		chapter1Step5LandingTreeItem.setExpanded(true);
		chapter1TreeViewContent.add(chapter1Step5LandingTreeItem);
		chapter1TreeViewContent.add(chapter1Step5TreeItem);
		
		chapter1RootTreeItem.getChildren().add(chapter1Step1LandingTreeItem);
		chapter1RootTreeItem.getChildren().add(chapter1Step2LandingTreeItem);
		chapter1RootTreeItem.getChildren().add(chapter1Step3LandingTreeItem);
		chapter1RootTreeItem.getChildren().add(chapter1Step4LandingTreeItem);
		chapter1RootTreeItem.getChildren().add(chapter1Step5LandingTreeItem);
		chapter1RootTreeItem.setExpanded(true);
		
		rootTreeItem.getChildren().add(chapter1RootTreeItem);
		
		return chapter1TreeViewContent;
	}
	
	public ArrayList<TreeItem<String>> createChapter2TreeViewContent(TreeItem<String> rootTreeItem) {
		ArrayList<TreeItem<String>> chapter2TreeViewContent = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> chapter2RootTreeItem = new TreeItem<String>("Chapter 2 Landing");
		chapter2TreeViewContent.add(chapter2RootTreeItem);
		
		TreeItem<String> chapter2Step1LandingTreeItem = new TreeItem<String>("Chapter 2 Step 1 Landing");
		TreeItem<String> chapter2Step1TreeItem = new TreeItem<String>("Chapter 2 Step 1");
		chapter2Step1LandingTreeItem.getChildren().add(chapter2Step1TreeItem);
		chapter2Step1LandingTreeItem.setExpanded(true);
		chapter2TreeViewContent.add(chapter2Step1LandingTreeItem);
		chapter2TreeViewContent.add(chapter2Step1TreeItem);
		
		TreeItem<String> chapter2Step2LandingTreeItem = new TreeItem<String>("Chapter 2 Step 2 Landing");
		TreeItem<String> chapter2Step2TreeItem = new TreeItem<String>("Chapter 2 Step 2");
		chapter2Step2LandingTreeItem.getChildren().add(chapter2Step2TreeItem);
		chapter2Step2LandingTreeItem.setExpanded(true);
		chapter2TreeViewContent.add(chapter2Step2LandingTreeItem);
		chapter2TreeViewContent.add(chapter2Step2TreeItem);
		
		TreeItem<String> chapter2Step3LandingTreeItem = new TreeItem<String>("Chapter 2 Step 3 Landing");
		TreeItem<String> chapter2Step3TreeItem = new TreeItem<String>("Chapter 2 Step 3");
		chapter2Step3LandingTreeItem.getChildren().add(chapter2Step3TreeItem);
		chapter2Step3LandingTreeItem.setExpanded(true);
		chapter2TreeViewContent.add(chapter2Step3LandingTreeItem);
		chapter2TreeViewContent.add(chapter2Step3TreeItem);
		
		TreeItem<String> chapter2Step4LandingTreeItem = new TreeItem<String>("Chapter 2 Step 4 Landing");
		TreeItem<String> chapter2Step4TreeItem = new TreeItem<String>("Chapter 2 Step 4");
		chapter2Step4LandingTreeItem.getChildren().add(chapter2Step4TreeItem);
		chapter2Step4LandingTreeItem.setExpanded(true);
		chapter2TreeViewContent.add(chapter2Step4LandingTreeItem);
		chapter2TreeViewContent.add(chapter2Step4TreeItem);
		
		TreeItem<String> chapter2Step5LandingTreeItem = new TreeItem<String>("Chapter 2 Step 5 Landing");
		TreeItem<String> chapter2Step5TreeItem = new TreeItem<String>("Chapter 2 Step 5");
		chapter2Step5LandingTreeItem.getChildren().add(chapter2Step5TreeItem);
		chapter2Step5LandingTreeItem.setExpanded(true);
		chapter2TreeViewContent.add(chapter2Step5LandingTreeItem);
		chapter2TreeViewContent.add(chapter2Step5TreeItem);
		
		chapter2RootTreeItem.getChildren().add(chapter2Step1LandingTreeItem);
		chapter2RootTreeItem.getChildren().add(chapter2Step2LandingTreeItem);
		chapter2RootTreeItem.getChildren().add(chapter2Step3LandingTreeItem);
		chapter2RootTreeItem.getChildren().add(chapter2Step4LandingTreeItem);
		chapter2RootTreeItem.getChildren().add(chapter2Step5LandingTreeItem);
		chapter2RootTreeItem.setExpanded(true);
		
		rootTreeItem.getChildren().add(chapter2RootTreeItem);
		
		return chapter2TreeViewContent;
	}
	
	public ArrayList<TreeItem<String>> createChapter3TreeViewContent(TreeItem<String> rootTreeItem) {
		ArrayList<TreeItem<String>> chapter3TreeViewContent = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> chapter3RootTreeItem = new TreeItem<String>("Chapter 3 Landing");
		chapter3TreeViewContent.add(chapter3RootTreeItem);

		TreeItem<String> chapter3Step1LandingTreeItem = new TreeItem<String>("Chapter 3 Step 1 Landing");
		TreeItem<String> chapter3Step1TreeItem = new TreeItem<String>("Chapter 3 Step 1");
		chapter3Step1LandingTreeItem.getChildren().add(chapter3Step1TreeItem);
		chapter3Step1LandingTreeItem.setExpanded(true);
		chapter3TreeViewContent.add(chapter3Step1LandingTreeItem);
		chapter3TreeViewContent.add(chapter3Step1TreeItem);
		
		TreeItem<String> chapter3Step2LandingTreeItem = new TreeItem<String>("Chapter 3 Step 2 Landing");
		TreeItem<String> chapter3Step2TreeItem = new TreeItem<String>("Chapter 3 Step 2");
		chapter3Step2LandingTreeItem.getChildren().add(chapter3Step2TreeItem);		
		chapter3Step2LandingTreeItem.setExpanded(true);
		chapter3TreeViewContent.add(chapter3Step2LandingTreeItem);
		chapter3TreeViewContent.add(chapter3Step2TreeItem);
		
		TreeItem<String> chapter3Step3LandingTreeItem = new TreeItem<String>("Chapter 3 Step 3 Landing");
		TreeItem<String> chapter3Step3TreeItem = new TreeItem<String>("Chapter 3 Step 3");
		chapter3Step3LandingTreeItem.getChildren().add(chapter3Step3TreeItem);		
		chapter3Step3LandingTreeItem.setExpanded(true);
		chapter3TreeViewContent.add(chapter3Step3LandingTreeItem);
		chapter3TreeViewContent.add(chapter3Step3TreeItem);
		
		TreeItem<String> chapter3Step4LandingTreeItem = new TreeItem<String>("Chapter 3 Step 4 Landing");
		TreeItem<String> chapter3Step4TreeItem = new TreeItem<String>("Chapter 3 Step 4");
		chapter3Step4LandingTreeItem.getChildren().add(chapter3Step4TreeItem);		
		chapter3Step4LandingTreeItem.setExpanded(true);
		chapter3TreeViewContent.add(chapter3Step4LandingTreeItem);
		chapter3TreeViewContent.add(chapter3Step4TreeItem);
		
		TreeItem<String> chapter3Step5LandingTreeItem = new TreeItem<String>("Chapter 3 Step 5 Landing");
		TreeItem<String> chapter3Step5TreeItem = new TreeItem<String>("Chapter 3 Step 5");
		chapter3Step5LandingTreeItem.getChildren().add(chapter3Step5TreeItem);
		chapter3Step5LandingTreeItem.setExpanded(true);
		chapter3TreeViewContent.add(chapter3Step5LandingTreeItem);
		chapter3TreeViewContent.add(chapter3Step5TreeItem);
		
		TreeItem<String> chapter3Step6LandingTreeItem = new TreeItem<String>("Chapter 3 Step 6 Landing");
		TreeItem<String> chapter3Step6TreeItem = new TreeItem<String>("Chapter 3 Step 6");
		chapter3Step5LandingTreeItem.getChildren().add(chapter3Step6TreeItem);
		chapter3Step5LandingTreeItem.setExpanded(true);
		chapter3TreeViewContent.add(chapter3Step6LandingTreeItem);
		chapter3TreeViewContent.add(chapter3Step6TreeItem);
		
		chapter3RootTreeItem.getChildren().add(chapter3Step1LandingTreeItem);
		chapter3RootTreeItem.getChildren().add(chapter3Step2LandingTreeItem);
		chapter3RootTreeItem.getChildren().add(chapter3Step3LandingTreeItem);
		chapter3RootTreeItem.getChildren().add(chapter3Step4LandingTreeItem);
		chapter3RootTreeItem.getChildren().add(chapter3Step5LandingTreeItem);
		chapter3RootTreeItem.getChildren().add(chapter3Step6LandingTreeItem);
		chapter3RootTreeItem.setExpanded(true);
		
		rootTreeItem.getChildren().add(chapter3RootTreeItem);
		
		return chapter3TreeViewContent;
	}
	
	public ArrayList<TreeItem<String>> createChapter4TreeViewContent(TreeItem<String> rootTreeItem) {
		ArrayList<TreeItem<String>> chapter4TreeViewContent = new ArrayList<TreeItem<String>>();

		TreeItem<String> chapter4RootTreeItem = new TreeItem<String>("Chapter 4 Landing");
		chapter4TreeViewContent.add(chapter4RootTreeItem);
		
		TreeItem<String> chapter4Step1LandingTreeItem = new TreeItem<String>("Chapter 4 Step 1 Landing");
		TreeItem<String> chapter4Step1TreeItem = new TreeItem<String>("Chapter 4 Step 1");
		chapter4Step1LandingTreeItem.getChildren().add(chapter4Step1TreeItem);
		chapter4Step1LandingTreeItem.setExpanded(true);
		chapter4TreeViewContent.add(chapter4Step1LandingTreeItem);
		chapter4TreeViewContent.add(chapter4Step1TreeItem);

		TreeItem<String> chapter4Step2LandingTreeItem = new TreeItem<String>("Chapter 4 Step 2 Landing");
		TreeItem<String> chapter4Step2TreeItem = new TreeItem<String>("Chapter 4 Step 2");
		chapter4Step2LandingTreeItem.getChildren().add(chapter4Step2TreeItem);	
		chapter4Step2LandingTreeItem.setExpanded(true);
		chapter4TreeViewContent.add(chapter4Step2LandingTreeItem);
		chapter4TreeViewContent.add(chapter4Step2TreeItem);
		
		TreeItem<String> chapter4Step3LandingTreeItem = new TreeItem<String>("Chapter 4 Step 3 Landing");
		TreeItem<String> chapter4Step3TreeItem = new TreeItem<String>("Chapter 4 Step 3");
		chapter4Step3LandingTreeItem.getChildren().add(chapter4Step3TreeItem);	
		chapter4Step3LandingTreeItem.setExpanded(true);
		chapter4TreeViewContent.add(chapter4Step3LandingTreeItem);
		chapter4TreeViewContent.add(chapter4Step3TreeItem);
		
		TreeItem<String> chapter4Step4LandingTreeItem = new TreeItem<String>("Chapter 4 Step 4 Landing");
		TreeItem<String> chapter4Step4TreeItem = new TreeItem<String>("Chapter 4 Step 4");
		chapter4Step4LandingTreeItem.getChildren().add(chapter4Step4TreeItem);
		chapter4Step4LandingTreeItem.setExpanded(true);
		chapter4TreeViewContent.add(chapter4Step4LandingTreeItem);
		chapter4TreeViewContent.add(chapter4Step4TreeItem);
		
		chapter4RootTreeItem.getChildren().add(chapter4Step1LandingTreeItem);
		chapter4RootTreeItem.getChildren().add(chapter4Step2LandingTreeItem);
		chapter4RootTreeItem.getChildren().add(chapter4Step3LandingTreeItem);
		chapter4RootTreeItem.getChildren().add(chapter4Step4LandingTreeItem);		
		chapter4RootTreeItem.setExpanded(true);
		
		rootTreeItem.getChildren().add(chapter4RootTreeItem);
		
		return chapter4TreeViewContent;
	}
	
	public ArrayList<TreeItem<String>> createChapter5TreeViewContent(TreeItem<String> rootTreeItem) {
		ArrayList<TreeItem<String>> chapter5TreeViewContent = new ArrayList<TreeItem<String>>();

		TreeItem<String> chapter5RootTreeItem = new TreeItem<String>("Chapter 5 Landing");
		chapter5TreeViewContent.add(chapter5RootTreeItem);
		
		TreeItem<String> chapter5Step1LandingTreeItem = new TreeItem<String>("Chapter 5 Step 1 Landing");
		TreeItem<String> chapter5Step1TreeItem = new TreeItem<String>("Chapter 5 Step 1");
		chapter5Step1LandingTreeItem.getChildren().add(chapter5Step1TreeItem);
		chapter5Step1LandingTreeItem.setExpanded(true);
		chapter5TreeViewContent.add(chapter5Step1LandingTreeItem);
		chapter5TreeViewContent.add(chapter5Step1TreeItem);
		
		TreeItem<String> chapter5Step2LandingTreeItem = new TreeItem<String>("Chapter 5 Step 2 Landing");
		TreeItem<String> chapter5Step2TreeItem = new TreeItem<String>("Chapter 5 Step 2");
		chapter5Step2LandingTreeItem.getChildren().add(chapter5Step2TreeItem);		
		chapter5Step2LandingTreeItem.setExpanded(true);
		chapter5TreeViewContent.add(chapter5Step2LandingTreeItem);
		chapter5TreeViewContent.add(chapter5Step2TreeItem);
		
		TreeItem<String> chapter5Step3LandingTreeItem = new TreeItem<String>("Chapter 5 Step 3 Landing");
		TreeItem<String> chapter5Step3TreeItem = new TreeItem<String>("Chapter 5 Step 3");
		chapter5Step3LandingTreeItem.getChildren().add(chapter5Step3TreeItem);		
		chapter5Step3LandingTreeItem.setExpanded(true);
		chapter5TreeViewContent.add(chapter5Step3LandingTreeItem);
		chapter5TreeViewContent.add(chapter5Step3TreeItem);
		
		TreeItem<String> chapter5Step4LandingTreeItem = new TreeItem<String>("Chapter 5 Step 4 Landing");
		TreeItem<String> chapter5Step4TreeItem = new TreeItem<String>("Chapter 5 Step 4");
		chapter5Step4LandingTreeItem.getChildren().add(chapter5Step4TreeItem);		
		chapter5Step4LandingTreeItem.setExpanded(true);
		chapter5TreeViewContent.add(chapter5Step4LandingTreeItem);
		chapter5TreeViewContent.add(chapter5Step4TreeItem);
		
		TreeItem<String> chapter5Step5LandingTreeItem = new TreeItem<String>("Chapter 5 Step 5 Landing");
		TreeItem<String> chapter5Step5TreeItem = new TreeItem<String>("Chapter 5 Step 5");
		chapter5Step5LandingTreeItem.getChildren().add(chapter5Step5TreeItem);
		chapter5Step5LandingTreeItem.setExpanded(true);
		chapter5TreeViewContent.add(chapter5Step5LandingTreeItem);
		chapter5TreeViewContent.add(chapter5Step5TreeItem);
		
		chapter5RootTreeItem.getChildren().add(chapter5Step1LandingTreeItem);
		chapter5RootTreeItem.getChildren().add(chapter5Step2LandingTreeItem);
		chapter5RootTreeItem.getChildren().add(chapter5Step3LandingTreeItem);
		chapter5RootTreeItem.getChildren().add(chapter5Step4LandingTreeItem);
		chapter5RootTreeItem.getChildren().add(chapter5Step5LandingTreeItem);		
		chapter5RootTreeItem.setExpanded(true);
		
		rootTreeItem.getChildren().add(chapter5RootTreeItem);
		
		return chapter5TreeViewContent;
	}
}
