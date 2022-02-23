package erb_chapter4;

import java.net.URL;
import java.util.ResourceBundle;
import erb.Chapter_Handler;
import erb.Wizard;
import erb.WizardContainerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chap4Step3Controller implements Initializable{

	Wizard wizard;
	WizardContainerController wizardContainerController;

	public Chap4Step3Controller(WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizard = wizard;
		this.wizardContainerController = wizardContainerController;
	}
	
	Chapter_Handler chapter_Handler = new Chapter_Handler();
	
	@FXML
	VBox panelVBox;
	@FXML
	VBox contentVBox;
	@FXML
	HBox headerHBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController, wizard);
		chapter_Handler.addProgressWidgetPanel(headerHBox);
		chapter_Handler.addGlossaryWidgetPanel(headerHBox);
	}
}
