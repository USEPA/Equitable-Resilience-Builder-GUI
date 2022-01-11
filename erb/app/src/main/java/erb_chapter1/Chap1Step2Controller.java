package erb_chapter1;

import java.net.URL;
import java.util.ResourceBundle;
import erb.Chapter_Handler;
import erb.WizardContainerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class Chap1Step2Controller implements Initializable{

	@FXML
	VBox panelVBox;
	@FXML
	VBox contentVBox;
	
	WizardContainerController wizardContainerController;
	public Chap1Step2Controller(WizardContainerController wizardContainerController) {
		this.wizardContainerController = wizardContainerController;
	}
	
	Chapter_Handler chapter_Handler = new Chapter_Handler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController);
	}
}
