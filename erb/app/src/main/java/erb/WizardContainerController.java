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
		if(currentPanelNode != null) {
			Pattern pattern = Pattern.compile("([0-9]\\.)");
			Matcher matcher = pattern.matcher(currentPanelNode.getAccessibleText());
			if(matcher.find()) {
				int chapNum = Integer.parseInt(matcher.group(0).replaceAll("\\.", ""));
				return chapNum;
			}
		}
		return -1;
	}
	
}
