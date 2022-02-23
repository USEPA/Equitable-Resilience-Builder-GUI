package erb;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CustomizedProjectController implements Initializable{

	Stage mainStage;
	
	public CustomizedProjectController(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
	private Logger logger = LogManager.getLogger(CustomizedProjectController.class);
	
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
	@FXML
	Label glossaryLabel;
	@FXML
	Label resourcesLabel;
	@FXML
	Label projectSelectionLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		glossaryLabel.setOnMouseEntered(e-> handleLabelHighlight(glossaryLabel));
		glossaryLabel.setOnMouseExited(e-> handleLabelUnHighlight(glossaryLabel));
		glossaryLabel.setOnMouseClicked(e-> handleLabelClicked(glossaryLabel));

		resourcesLabel.setOnMouseEntered(e-> handleLabelHighlight(resourcesLabel));
		resourcesLabel.setOnMouseExited(e-> handleLabelUnHighlight(resourcesLabel));
		resourcesLabel.setOnMouseClicked(e-> handleLabelClicked(resourcesLabel));
		
		projectSelectionLabel.setOnMouseEntered(e-> handleLabelHighlight(projectSelectionLabel));
		projectSelectionLabel.setOnMouseExited(e-> handleLabelUnHighlight(projectSelectionLabel));
		projectSelectionLabel.setOnMouseClicked(e-> handleLabelClicked(projectSelectionLabel));
	}
	
	public void handleLabelHighlight(Label label) {	
		//Highlight
		label.setStyle("-fx-background-color: #A2A2A2");
	}
	
	public void handleLabelUnHighlight(Label label) {	
		//Un-highlight 
		label.setStyle("-fx-background-color: transparent");
	}
	
	public void handleLabelClicked(Label label) {
		if(label == projectSelectionLabel) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProjectSelection.fxml"));
				ProjectSelectionController mainController = new ProjectSelectionController(mainStage);
				fxmlLoader.setController(mainController);
				Parent rootParent = fxmlLoader.load();
				Scene mainScene = new Scene(rootParent);
				mainStage.setScene(mainScene);
				mainStage.show();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	@FXML
	public void customizedLaunchButtonAction() {
		if(customOptions.getSelectedToggle() != null) {
			try {
				Wizard wizard = new Wizard(mainStage);
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WizardContainer.fxml"));
				WizardContainerController wizardContainerController = new WizardContainerController(wizard);
				fxmlLoader.setController(wizardContainerController);
				wizard.setWizardContainerController(wizardContainerController);
				handleSelectedToggle(wizard);
				Parent rootParent = fxmlLoader.load();
				Scene scene = new Scene(rootParent);
				mainStage.setScene(scene);
			}catch (Exception e) {
				logger.error(e.getMessage());
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
