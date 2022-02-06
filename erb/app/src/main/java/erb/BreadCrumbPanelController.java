package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class BreadCrumbPanelController implements Initializable{

	Wizard wizard;
	int chapterNum;
	WizardContainerController wizardContainerController;

	public BreadCrumbPanelController(int chapterNum, WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizard = wizard;
		this.chapterNum = chapterNum;
		this.wizardContainerController = wizardContainerController;
	}
		
	@FXML
	HBox breadCrumbHBox;
	@FXML
	Button step1Button;
	@FXML
	Button step2Button;
	@FXML
	Button step3Button;
	@FXML
	Button step4Button;
	@FXML
	Button step5Button;
	@FXML
	Button step6Button;
	@FXML
	Label arrowLabel1;
	@FXML
	Label arrowLabel2;
	@FXML
	Label arrowLabel3;
	@FXML
	Label arrowLabel4;
	@FXML
	Label arrowLabel5;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(chapterNum == 1) {
			setChapter1Controls();
		}else if(chapterNum == 2) {
			setChapter2Controls();
		}else if (chapterNum == 3) {
			setChapter3Controls();
		}else if (chapterNum == 4) {
			setChapter4Controls();
		}else if (chapterNum == 5) {
			setChapter5Controls();
		}
		
		breadCrumbHBox.getStylesheets().add("/BreadCrumb.css");
	}
	
	@FXML
	public void step1ButtonAction() {
		String panelAccessibleTextId = chapterNum + ".1.0";
		wizardContainerController.loadWizardPanel(panelAccessibleTextId);
	}
	
	@FXML
	public void step2ButtonAction() {
		String panelAccessibleTextId = chapterNum + ".2.0";
		wizardContainerController.loadWizardPanel(panelAccessibleTextId);
	}
	
	@FXML
	public void step3ButtonAction() {
		String panelAccessibleTextId = chapterNum + ".3.0";
		wizardContainerController.loadWizardPanel(panelAccessibleTextId);
	}
	
	@FXML
	public void step4ButtonAction() {
		String panelAccessibleTextId = chapterNum + ".4.0";
		wizardContainerController.loadWizardPanel(panelAccessibleTextId);
	}
	
	@FXML
	public void step5ButtonAction() {
		String panelAccessibleTextId = chapterNum + ".5.0";
		wizardContainerController.loadWizardPanel(panelAccessibleTextId);
	}
	
	@FXML
	public void step6ButtonAction() {
		String panelAccessibleTextId = chapterNum + ".6.0";
		wizardContainerController.loadWizardPanel(panelAccessibleTextId);
	}
	
	public void setChapter1Controls() {
		breadCrumbHBox.getChildren().remove(step6Button);
		breadCrumbHBox.getChildren().remove(arrowLabel5);
	}
	
	public void setChapter2Controls() {
		breadCrumbHBox.getChildren().remove(step6Button);
		breadCrumbHBox.getChildren().remove(arrowLabel5);
	}
	
	public void setChapter3Controls() {
		//Leave as is
	}
	
	public void setChapter4Controls() {
		breadCrumbHBox.getChildren().remove(step5Button);
		breadCrumbHBox.getChildren().remove(arrowLabel4);
		breadCrumbHBox.getChildren().remove(step6Button);
		breadCrumbHBox.getChildren().remove(arrowLabel5);
	}
	
	public void setChapter5Controls() {
		breadCrumbHBox.getChildren().remove(step6Button);
		breadCrumbHBox.getChildren().remove(arrowLabel5);
	}
	
//	public TreeItem<String> getChapter1BreadCrumbs() {
//		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5");
//		return model;
//	}
//	
//	public TreeItem<String> getChapter2BreadCrumbs() {
//		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5");
//		return model;
//	}
//	
//	public TreeItem<String> getChapter3BreadCrumbs() {
//		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5", "Step 6");
//		return model;
//	}
//	
//	public TreeItem<String> getChapter4BreadCrumbs() {
//		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4");
//		return model;
//	}
//	
//	public TreeItem<String> getChapter5BreadCrumbs() {
//		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5");
//		return model;
//	}
	
}
