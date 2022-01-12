package erb;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;

public class NavigationPanelController implements Initializable{

	@FXML
	HBox navigationHBox;
	@FXML
	Button previousButton;
	@FXML
	Button nextButton;
	
	WizardContainerController wizardContainerController;
	Wizard wizard;
	public NavigationPanelController(WizardContainerController wizardContainerController, Wizard wizard) {
		this.wizardContainerController = wizardContainerController;
		this.wizard = wizard;
	}
	
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
