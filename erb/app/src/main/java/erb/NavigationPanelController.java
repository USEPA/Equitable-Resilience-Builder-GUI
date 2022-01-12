package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class NavigationPanelController implements Initializable{

	Wizard wizard;
	WizardContainerController wizardContainerController;

	public NavigationPanelController(WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizard = wizard;
		this.wizardContainerController = wizardContainerController;
	}
	
	@FXML
	HBox navigationHBox;
	@FXML
	Button previousButton;
	@FXML
	Button nextButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void previousButtonAction() {

	}
	
	@FXML
	public void nextButtonAction() {

	}

}
