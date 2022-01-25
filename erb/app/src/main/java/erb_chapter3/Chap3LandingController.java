package erb_chapter3;

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

public class Chap3LandingController implements Initializable{

	Wizard wizard;
	WizardContainerController wizardContainerController;
	
	public Chap3LandingController(WizardContainerController wizardContainerController, Wizard wizard) {
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
		"- Increase understanding of the hazards, disasters, and threats the community may face" + "\n" +
		"- Increase shared understanding of what social vulnerability in your community means and why it exists" + "\n" +
		"- Build/improve relationships between different groups in the community"	+ "\n" +
		"- Build/improve relationships between government and community members");
		text.setFont(Font.font ("Georgia", 13));
		goalsTextFlow.getChildren().add(text);
	}

}
