package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class WizardContainerController implements Initializable{
	
	@FXML
	VBox wizardVBox;
	
	Wizard wizard;
	public WizardContainerController(Wizard wizard) {
		this.wizard = wizard;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadWizardPanel(0);
	}
	
	public void loadWizardPanel(int wizardPanelIndex) {
		clearContent();
		addContent(wizardPanelIndex);
	}
	
	private void clearContent() {
		wizardVBox.getChildren().clear();
	}
	
	private void addContent(int wizardPanelIndex) {
		wizardVBox.getChildren().add(wizard.getMapOfPanelsInWizard().get(wizardPanelIndex));
		VBox.setVgrow(wizard.getMapOfPanelsInWizard().get(wizardPanelIndex), Priority.ALWAYS);
	}
	
	public int getCurrentWizardPanelIndex() {
		Node currentNode = wizardVBox.getChildren().get(0);
		return wizard.getIndex(currentNode);	
	}
	
	public int getMaxWizardPanelIndex() {
		return wizard.getMaxWizardPanelIndex();
	}
	
}
