//package erb_chapter2;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import old.Panel_Handler;
//import old.Wizard;
//import old.WizardContainerController;
//
//public class Chap2LandingController implements Initializable{
//
//	Wizard wizard;
//	WizardContainerController wizardContainerController;
//	
//	public Chap2LandingController(WizardContainerController wizardContainerController, Wizard wizard) {
//		this.wizard = wizard;
//		this.wizardContainerController = wizardContainerController;
//	}
//	
//	Panel_Handler chapter_Handler = new Panel_Handler();
//	
//	@FXML
//	VBox panelVBox;
//	@FXML
//	VBox contentVBox;
//	@FXML
//	TextFlow goalsTextFlow;
//	
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController, wizard);
//		setGoalsTextFlow();
//	}
//	
//	public void setGoalsTextFlow() {
//		Text text = new Text(
//		"- Learn and reflect on which community groups and coalitions already exist and learn how to best engage different members of the community" + "\n" +
//		"- Create a plan to engage with community members throughout the ERB process" + "\n" +
//		"- Plan the workshops for activities in chapters 3,4, and 5"	+ "\n" +
//		"- Begin the process of tracking a relationship throughout the process in the relationship tracker");
//		text.setFont(Font.font ("Georgia", 13));
//		goalsTextFlow.getChildren().add(text);
//	}
//
//}
