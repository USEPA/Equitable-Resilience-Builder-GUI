package erb_chapter3;

import java.net.URL;
import java.util.ResourceBundle;
import erb.Chapter_Handler;
import erb.Wizard;
import erb.WizardContainerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chap3Step6Controller implements Initializable{

	@FXML
	VBox panelVBox;
	@FXML
	VBox contentVBox;
	@FXML
	HBox headerHBox;
	
	WizardContainerController wizardContainerController;
	Wizard wizard;
	public Chap3Step6Controller(WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizardContainerController = wizardContainerController;
		this.wizard = wizard;
	}
	
	Chapter_Handler chapter_Handler = new Chapter_Handler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController);
//		chapter_Handler.addProgressWidgetPanel(contentVBox);
//		chapter_Handler.addGlossaryWidgetPanel(contentVBox);
//		chapter_Handler.addBreadCrumbPanel(contentVBox);
		chapter_Handler.addBreadCrumbPanel(headerHBox, 3, wizardContainerController, wizard);
		chapter_Handler.addProgressWidgetPanel(headerHBox);
		chapter_Handler.addGlossaryWidgetPanel(headerHBox);
	}

}
