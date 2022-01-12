package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NewProjectController implements Initializable{

	Stage mainStage;
	
	public NewProjectController(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
	@FXML
	Button customizeButton;
	@FXML
	Button exploreButton;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	@FXML
	public void customizeButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CustomizedExperience.fxml"));
			CustomizedExperienceController customizedExperienceController = new CustomizedExperienceController(mainStage);
			fxmlLoader.setController(customizedExperienceController);
			Parent rootParent = fxmlLoader.load();
			Scene scene = new Scene(rootParent);
			mainStage.setScene(scene);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@FXML
	public void exploreButtonAction() {
		try {
			Wizard wizard = new Wizard();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WizardContainer.fxml"));
			WizardContainerController wizardContainerController = new WizardContainerController(wizard);
			fxmlLoader.setController(wizardContainerController);
			wizard.setWizardContainerController(wizardContainerController);
			wizard.setSequentialPanels();
			Parent rootParent = fxmlLoader.load();
			Scene scene = new Scene(rootParent);
			mainStage.setScene(scene);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
