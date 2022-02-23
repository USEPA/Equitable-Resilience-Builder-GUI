package erb_chapter1;

import java.net.URL;
import java.util.ResourceBundle;
import erb.Panel_Handler;
import erb.Wizard;
import erb.WizardContainerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Chap1LandingController implements Initializable{

	Wizard wizard;
	WizardContainerController wizardContainerController;
	
	public Chap1LandingController(WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizard = wizard;
		this.wizardContainerController = wizardContainerController;
	}
	
	Panel_Handler chapter_Handler = new Panel_Handler();
	
	@FXML
	VBox panelVBox;
	@FXML
	VBox contentVBox;
	@FXML
	TextFlow goalsTextFlow;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController, wizard);
		setGoalsTextFlow();
	}
	
	public void setGoalsTextFlow() {
		Text text = new Text(
		"- Familiarize yourself with the ERB process" + "\n" +
		"- Complete your equitable resilience project planning");
		text.setFont(Font.font ("Georgia", 13));
		goalsTextFlow.getChildren().add(text);
	}

}
