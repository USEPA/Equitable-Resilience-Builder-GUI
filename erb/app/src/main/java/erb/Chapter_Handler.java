package erb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chapter_Handler {

	public Chapter_Handler() {
		
	}
	
	private Logger logger = LogManager.getLogger(Chapter_Handler.class);
	
	public void addNavigationPanel(VBox contentVBox, WizardContainerController wizardContainerController, Wizard wizard) {
		Parent navigationPanelParent = loadNavigationPanel(wizardContainerController, wizard);
		if(navigationPanelParent != null) {
			int numChildren = contentVBox.getChildren().size();
			contentVBox.getChildren().add(numChildren, navigationPanelParent);
		}
	}
	
	private Parent loadNavigationPanel(WizardContainerController wizardContainerController, Wizard wizard) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NavigationPanel.fxml"));
			NavigationPanelController navigationPanelController = new NavigationPanelController(wizardContainerController, wizard);
			fxmlLoader.setController(navigationPanelController);
			Parent rootParent = fxmlLoader.load();
			return rootParent;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
		
	public void addProgressWidgetPanel(HBox hBox) {
		Parent progressWidgetPanelParent = loadProgressWidgetPanel();
		if(progressWidgetPanelParent != null) {
			hBox.getChildren().add(0, progressWidgetPanelParent);
		}
	}
	
	private Parent loadProgressWidgetPanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressWidgetPanel.fxml"));
			ProgressWidgetPanelController progressWidgetPanelController = new ProgressWidgetPanelController();
			fxmlLoader.setController(progressWidgetPanelController);
			Parent rootParent = fxmlLoader.load();
			return rootParent;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
		
	public void addGlossaryWidgetPanel(HBox hBox) {
		Parent glossaryWidgetPanelParent = loadGlossaryWidgetPanel();
		if(glossaryWidgetPanelParent != null) {
			hBox.getChildren().add(1, glossaryWidgetPanelParent);
		}
	}
	
	private Parent loadGlossaryWidgetPanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GlossaryWidgetPanel.fxml"));
			GlossaryWidgetPanelController glossaryWidgetPanelController = new GlossaryWidgetPanelController();
			fxmlLoader.setController(glossaryWidgetPanelController);
			Parent rootParent = fxmlLoader.load();
			return rootParent;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
		
	public void addBreadCrumbPanel(HBox hBox, int chapterNum, WizardContainerController wizardContainerController, Wizard wizard) {
		Parent breadCrumbPanelParent = loadBreadCrumbPanel(chapterNum, wizardContainerController, wizard);
		if(breadCrumbPanelParent != null) {
			hBox.getChildren().add(0, breadCrumbPanelParent);
		}
	}
	
	private Parent loadBreadCrumbPanel(int chapterNum, WizardContainerController wizardContainerController, Wizard wizard) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BreadCrumbPanel.fxml"));
			BreadCrumbPanelController breadCrumbPanelController = new BreadCrumbPanelController(chapterNum, wizardContainerController, wizard);
			fxmlLoader.setController(breadCrumbPanelController);
			Parent rootParent = fxmlLoader.load();
			return rootParent;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
}
