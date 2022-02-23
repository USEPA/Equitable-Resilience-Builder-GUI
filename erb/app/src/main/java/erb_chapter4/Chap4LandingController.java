package erb_chapter4;

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

public class Chap4LandingController implements Initializable{

	Wizard wizard;
	WizardContainerController wizardContainerController;
	
	public Chap4LandingController (WizardContainerController wizardContainerController, Wizard wizard) {
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
		"- Use indicators to explore the resilience and equitableness of your community's built, natural, and social support systems" + "\n" +
		"- Sort and visualize indicators to identify priority areas to consider for action and intervention in Chapter 5");
		text.setFont(Font.font ("Georgia", 13));
		goalsTextFlow.getChildren().add(text);
	}
}
