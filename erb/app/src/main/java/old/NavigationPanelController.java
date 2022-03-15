//package old;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.ResourceBundle;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.layout.HBox;
//import javafx.scene.shape.Polygon;
//
//public class NavigationPanelController implements Initializable{
//
//	Wizard wizard;
//	WizardContainerController wizardContainerController;
//
//	public NavigationPanelController(WizardContainerController wizardContainerController, Wizard wizard) {
//		this.wizard = wizard;
//		this.wizardContainerController = wizardContainerController;
//	}
//		
//	@FXML
//	HBox navigationHBox;
//	@FXML
//	Polygon leftFacingPolygon; //Previous
//	@FXML
//	Polygon rightFacingPolygon; //Next
//
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		leftFacingPolygon.setOnMouseClicked(e-> loadPreviousPanel());
//		rightFacingPolygon.setOnMouseClicked(e-> loadNextPanel());
//	}
//	
//	public void loadPreviousPanel() {
//		int currentChapter = wizardContainerController.getCurrentChapter();
//		Node currentNode = wizardContainerController.getCurrentWizardPanel();
//		ArrayList<Node> listOfPanelsForChapter = wizard.getListOfPanels(currentChapter);
//		
//		int indexOfPanelWithinChapter = getIndexOfPanelWithinChapter(currentNode, listOfPanelsForChapter);
//		if (indexOfPanelWithinChapter >= 0) {
//			if (indexOfPanelWithinChapter == 0) {
//				int previousChapter = currentChapter - 1;
//				if (previousChapter > 0) {
//					int numberOfStepsInChapter = wizard.getNumberOfSteps(previousChapter);
//					int numberOfSubStepsInStep = wizard.getNumberOfSubsteps(previousChapter, numberOfStepsInChapter);
//					if(numberOfSubStepsInStep >=1) {
//						String accessibleTextOfPanelToLoad = previousChapter + "." + numberOfStepsInChapter + "." + numberOfSubStepsInStep;
//						wizardContainerController.loadWizardPanel(accessibleTextOfPanelToLoad);
//						wizardContainerController.updateSelectedTreeItem(accessibleTextOfPanelToLoad);
//					}else {
//						String accessibleTextOfPanelToLoad = previousChapter + "." + numberOfStepsInChapter + ".1"; 
//						wizardContainerController.loadWizardPanel(accessibleTextOfPanelToLoad);
//						wizardContainerController.updateSelectedTreeItem(accessibleTextOfPanelToLoad);
//					}
//
//				}else {
//					wizardContainerController.loadWizardPanel("0.0");
//					wizardContainerController.updateSelectedTreeItem("0.0");
//				}
//			} else {
//				Node panelToLoad = listOfPanelsForChapter.get(indexOfPanelWithinChapter - 1);
//				wizardContainerController.loadWizardPanel(panelToLoad.getAccessibleText());
//				wizardContainerController.updateSelectedTreeItem(panelToLoad.getAccessibleText());
//			}
//		}
//	}
//	
//	public void loadNextPanel() {
//		int currentChapter = wizardContainerController.getCurrentChapter();
//		Node currentNode = wizardContainerController.getCurrentWizardPanel();
//		ArrayList<Node> listOfPanelsForChapter = wizard.getListOfPanels(currentChapter);
//		
//		int indexOfPanelWithinChapter = getIndexOfPanelWithinChapter(currentNode, listOfPanelsForChapter);
//		if (indexOfPanelWithinChapter >= 0) {
//			if (indexOfPanelWithinChapter == listOfPanelsForChapter.size()-1) {
//				int nextChapter = currentChapter + 1;
//				if (nextChapter > 0 && nextChapter <= 5) {
//					String accessibleTextOfPanelToLoad = nextChapter + ".0";
//					wizardContainerController.loadWizardPanel(accessibleTextOfPanelToLoad);
//					wizardContainerController.updateSelectedTreeItem(accessibleTextOfPanelToLoad);
//				}else if (nextChapter > 0) {
//					wizardContainerController.loadWizardPanel("0.0.0");
//					wizardContainerController.updateSelectedTreeItem("0.0.0");
//				}
//			} else {
//				Node panelToLoad = listOfPanelsForChapter.get(indexOfPanelWithinChapter + 1);
//				wizardContainerController.loadWizardPanel(panelToLoad.getAccessibleText());
//				wizardContainerController.updateSelectedTreeItem(panelToLoad.getAccessibleText());
//			}
//		}
//	}
//	
//	public int getIndexOfPanelWithinChapter(Node currentNode, ArrayList<Node> listOfPanelsForChapter) {
//		int indexOfPanelWithinChapter = -1;
//		for (int i = 0; i < listOfPanelsForChapter.size(); i++) {
//			Node chapterPanelNode = listOfPanelsForChapter.get(i);
//			if (chapterPanelNode == currentNode) {
//				indexOfPanelWithinChapter = i;
//			}
//		}
//		return indexOfPanelWithinChapter;
//	}
//
//}
