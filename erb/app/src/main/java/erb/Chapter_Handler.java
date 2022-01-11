package erb;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
}
