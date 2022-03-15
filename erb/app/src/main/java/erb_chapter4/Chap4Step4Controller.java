//package erb_chapter4;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import old.Panel_Handler;
//import old.Wizard;
//import old.WizardContainerController;
//
//public class Chap4Step4Controller implements Initializable{
//
//	Wizard wizard;
//	WizardContainerController wizardContainerController;
//
//	public Chap4Step4Controller(WizardContainerController wizardContainerController, Wizard wizard) {
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
//	HBox headerHBox;
//	
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController, wizard);
//		chapter_Handler.addProgressWidgetPanel(headerHBox);
//		chapter_Handler.addGlossaryWidgetPanel(headerHBox);
//	}
//}
