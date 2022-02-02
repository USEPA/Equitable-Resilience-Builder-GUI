package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CustomizedProjectController implements Initializable{

	Stage mainStage;
	
	public CustomizedProjectController(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
	@FXML
	RadioButton customOption1RadioButton;
	@FXML
	RadioButton customOption2RadioButton;
	@FXML
	RadioButton customOption3RadioButton;
	@FXML
	Button customizedLaunchButton;
	@FXML
	ToggleGroup customOptions;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void customizedLaunchButtonAction() {
		if(customOptions.getSelectedToggle() != null) {
			try {
				Wizard wizard = new Wizard();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WizardContainer.fxml"));
				WizardContainerController wizardContainerController = new WizardContainerController(wizard);
				fxmlLoader.setController(wizardContainerController);
				wizard.setWizardContainerController(wizardContainerController);
				handleSelectedToggle(wizard);
				Parent rootParent = fxmlLoader.load();
				Scene scene = new Scene(rootParent);
				mainStage.setScene(scene);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public void handleSelectedToggle(Wizard wizard) {
		RadioButton selectedRadioButton = (RadioButton) customOptions.getSelectedToggle();
		if(selectedRadioButton.getId().contains("Option1")) {
			wizard.setOnRampOption1Panels();
		}else if (selectedRadioButton.getId().contains("Option2")) {
			wizard.setOnRampOption2Panels();
		}else if (selectedRadioButton.getId().contains("Option3")) {
			wizard.setOnRampOption3Panels();
		}
	}

}
