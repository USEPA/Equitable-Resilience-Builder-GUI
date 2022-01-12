package erb;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.BreadCrumbBar.BreadCrumbActionEvent;

public class BreadCrumbPanelController implements Initializable{

	Wizard wizard;
	int chapterNum;
	WizardContainerController wizardContainerController;

	public BreadCrumbPanelController(int chapterNum, WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizard = wizard;
		this.chapterNum = chapterNum;
		this.wizardContainerController = wizardContainerController;
	}
	
	BreadCrumbBar<String> breadCrumbBar;
	
	@FXML
	VBox breadCrumbVBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBreadCrumbBar();
	}
	
	public void setBreadCrumbBarCrumb(int index) {
		
	}
	
	public void setBreadCrumbBar() {
		Label selectedCrumbLabel = new Label();
		breadCrumbBar = new BreadCrumbBar<>();
		breadCrumbBar.setAutoNavigationEnabled(false);
		breadCrumbBar.getStylesheets().add("/BreadCrumbBar.css");
		breadCrumbVBox.getChildren().addAll(breadCrumbBar, selectedCrumbLabel);
		if(chapterNum == 1) {
			breadCrumbBar.setSelectedCrumb(getChapter1BreadCrumbs());
		}else if (chapterNum ==2) {
			breadCrumbBar.setSelectedCrumb(getChapter2BreadCrumbs());
		}else if (chapterNum ==3) {
			breadCrumbBar.setSelectedCrumb(getChapter3BreadCrumbs());
		}else if (chapterNum == 4) {
			breadCrumbBar.setSelectedCrumb(getChapter4BreadCrumbs());
		}else if (chapterNum == 5) {
			breadCrumbBar.setSelectedCrumb(getChapter5BreadCrumbs());
		}
		breadCrumbBar.setOnCrumbAction(e -> handleBreadCrumbEvent(e));
	}
	
	public TreeItem<String> getChapter1BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5");
		return model;
	}
	
	public TreeItem<String> getChapter2BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5");
		return model;
	}
	
	public TreeItem<String> getChapter3BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5", "Step 6");
		return model;
	}
	
	public TreeItem<String> getChapter4BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4");
		return model;
	}
	
	public TreeItem<String> getChapter5BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Step 1", "Step 2", "Step 3", "Step 4", "Step 5");
		return model;
	}
	
	public void handleBreadCrumbEvent(BreadCrumbActionEvent<String> event) {
		String selectedCrumbPanelId = getSelectedBreadCrumbBarId(event);
		int panelId = wizard.getIndex(selectedCrumbPanelId);
		wizardContainerController.loadWizardPanel(panelId);
	}
	
	public String getSelectedBreadCrumbBarId(BreadCrumbActionEvent<String> event) {
		String selectedCrumbText = event.getSelectedCrumb().getValue();
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(selectedCrumbText);
		if(matcher.find()) {
			String selectedCrumbStepNumber = matcher.group(0);
			return chapterNum + "." + selectedCrumbStepNumber;
		}
		return null;
	}
	
	public void clearOldBreadCrumbBar() {
		breadCrumbVBox.getChildren().clear();
	}
}
