package erb;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chapter_Handler {

	public Chapter_Handler() {
		
	}
	
	public void addNavigationPanel(VBox contentVBox, WizardContainerController wizardContainerController) {
		Parent navigationPanelParent = loadNavigationPanel(wizardContainerController);
		if(navigationPanelParent != null) {
			int numChildren = contentVBox.getChildren().size();
			contentVBox.getChildren().add(numChildren, navigationPanelParent);
		}
	}
	
	private Parent loadNavigationPanel(WizardContainerController wizardContainerController) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NavigationPanel.fxml"));
			NavigationPanelController navigationPanelController = new NavigationPanelController(wizardContainerController);
			fxmlLoader.setController(navigationPanelController);
			Parent rootParent = fxmlLoader.load();
			return rootParent;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public void addProgressWidgetPanel(VBox contentVBox) {
		Parent progressWidgetPanelParent = loadProgressWidgetPanel();
		if(progressWidgetPanelParent != null) {
			contentVBox.getChildren().add(0, progressWidgetPanelParent);
		}
	}
	
	public void addProgressWidgetPanel(HBox hBox) {
		Parent progressWidgetPanelParent = loadProgressWidgetPanel();
		if(progressWidgetPanelParent != null) {
			hBox.getChildren().add(1, progressWidgetPanelParent);
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
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public void addGlossaryWidgetPanel(VBox contentVBox) {
		Parent glossaryWidgetPanelParent = loadGlossaryWidgetPanel();
		if(glossaryWidgetPanelParent != null) {
			contentVBox.getChildren().add(1, glossaryWidgetPanelParent);
		}
	}
	
	public void addGlossaryWidgetPanel(HBox hBox) {
		Parent glossaryWidgetPanelParent = loadGlossaryWidgetPanel();
		if(glossaryWidgetPanelParent != null) {
			hBox.getChildren().add(2, glossaryWidgetPanelParent);
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
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public void addBreadCrumbPanel(VBox contentVBox, int chapterNum, WizardContainerController wizardContainerController, Wizard wizard) {
		Parent breadCrumbPanelParent = loadBreadCrumbPanel(chapterNum, wizardContainerController, wizard);
		if(breadCrumbPanelParent != null) {
			contentVBox.getChildren().add(2, breadCrumbPanelParent);
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
			// TODO: handle exception
			return null;
		}
	}
}
