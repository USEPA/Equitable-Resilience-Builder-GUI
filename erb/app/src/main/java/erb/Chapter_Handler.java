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
}
