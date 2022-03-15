//package erb_chapter5;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.layout.VBox;
//import old.Panel_Handler;
//import old.Wizard;
//import old.WizardContainerController;
//
//public class Chap5Step5LandingController implements Initializable{
//
//	Wizard wizard;
//	WizardContainerController wizardContainerController;
//	
//	public Chap5Step5LandingController(WizardContainerController wizardContainerController, Wizard wizard) {
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
//
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController, wizard);
//	}
//	
//}
