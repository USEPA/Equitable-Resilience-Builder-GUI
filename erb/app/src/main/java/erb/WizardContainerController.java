package erb;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class WizardContainerController implements Initializable{
	
	Wizard wizard;
	
	public WizardContainerController(Wizard wizard) {
		this.wizard = wizard;
	}
	
	@FXML
	VBox wizardVBox;

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
	
	public int getCurrentChapter() {
		Node currentNode = wizardVBox.getChildren().get(0);
		Pattern pattern = Pattern.compile("([0-9]\\.)");
		Matcher matcher = pattern.matcher(currentNode.getAccessibleText());
		if(matcher.find()) {
			int chapNum = Integer.parseInt(matcher.group(0).replaceAll("\\.", ""));
			return chapNum;
		}
		return -1;
	}
	
	public int getMaxWizardPanelIndex() {
		return wizard.getMaxWizardPanelIndex();
	}
	
}
