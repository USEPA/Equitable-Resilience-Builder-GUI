package erb_chapter5;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import erb.Chapter_Handler;
import erb.Wizard;
import erb.WizardContainerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chap5Step2Controller implements Initializable{

	Wizard wizard;
	WizardContainerController wizardContainerController;

	public Chap5Step2Controller(WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizard = wizard;
		this.wizardContainerController = wizardContainerController;
	}
	
	Chapter_Handler chapter_Handler = new Chapter_Handler();
	private Logger logger = LogManager.getLogger(Chap5Step2Controller.class);

	@FXML
	VBox panelVBox;
	@FXML
	VBox contentVBox;
	@FXML
	HBox headerHBox;
	@FXML
	VBox stepContentVBox;
	@FXML
	RadioButton radioButton1;
	@FXML
	RadioButton radioButton2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chapter_Handler.addNavigationPanel(panelVBox, wizardContainerController, wizard);
		chapter_Handler.addProgressWidgetPanel(headerHBox);
		chapter_Handler.addGlossaryWidgetPanel(headerHBox);
		radioButton1.setSelected(true);
		radioButton1Action();
	}
	
	@FXML
	public void radioButton1Action() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SampleStepContent1.fxml"));
			Parent rootParent = fxmlLoader.load();
			stepContentVBox.getChildren().clear();
			stepContentVBox.getChildren().add(rootParent);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void radioButton2Action() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SampleStepContent2.fxml"));
			Parent rootParent = fxmlLoader.load();
			stepContentVBox.getChildren().clear();
			stepContentVBox.getChildren().add(rootParent);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
