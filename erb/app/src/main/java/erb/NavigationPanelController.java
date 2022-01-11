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
	public NavigationPanelController(WizardContainerController wizardContainerController) {
		this.wizardContainerController = wizardContainerController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void previousButtonAction() {
		int currentPanelIndex = wizardContainerController.getCurrentWizardPanelIndex();
		if(currentPanelIndex >= 0) {
			int previousPanelIndex = currentPanelIndex -1 ;
			if(previousPanelIndex >= 0) {
				wizardContainerController.loadWizardPanel(previousPanelIndex);
			}else {
				Alerts alerts = new Alerts();
				Optional<ButtonType> result = alerts.showReturnToMainMenuAlert();
				System.out.println(result.get());
			}
		}
	}
	
	@FXML
	public void nextButtonAction() {
		int currentPanelIndex = wizardContainerController.getCurrentWizardPanelIndex();
		if(currentPanelIndex >= 0) {
			int nextPanelIndex = currentPanelIndex + 1;
			if(nextPanelIndex <= wizardContainerController.getMaxWizardPanelIndex()) {
				wizardContainerController.loadWizardPanel(nextPanelIndex);
			}
		}
	}

}
