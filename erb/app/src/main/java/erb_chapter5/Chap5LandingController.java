package erb_chapter5;

import java.net.URL;
import java.util.ResourceBundle;
import erb.Chapter_Handler;
import erb.Wizard;
import erb.WizardContainerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Chap5LandingController implements Initializable{

	Wizard wizard;
	WizardContainerController wizardContainerController;
	
	public Chap5LandingController(WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizard = wizard;
		this.wizardContainerController = wizardContainerController;
	}
	
	Chapter_Handler chapter_Handler = new Chapter_Handler();
	
	@FXML
	VBox panelVBox;
	@FXML
	VBox contentVBox;
	@FXML
	TextFlow goalsTextFlow;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chapter_Handler.addNavigationPanel(contentVBox, wizardContainerController, wizard);
		setGoalsTextFlow();
	}
	
	public void setGoalsTextFlow() {
		Text text = new Text(
		"- Empower community to take action on equitable resilience" + "\n" +
		"- Co-create a short list of equitable resilience actions that are most [relevant (achievable, appropriate, meaningful, impactful] to your community" + "\n" +
		"- Build relationships among social actors and institutions to advance resilience"	+ "\n" +
		"- Make the case for investing in resilience" + "\n" + 
		"- Obtain necessary input for your immediate task (e.g. update hazard mitigation plan)" + "\n" + 
		"- Build capacity for long term resilience progress" + "\n" + 
		"- Feel prepared to take the next steps towards implementation of resilience actions");
		text.setFont(Font.font ("Georgia", 13));
		goalsTextFlow.getChildren().add(text);
	}

}
