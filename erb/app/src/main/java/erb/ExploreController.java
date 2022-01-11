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

public class ExploreController implements Initializable{

	@FXML
	Button sequentialLaunchButton;
	@FXML
	Button modularLaunchButton;
	
	Stage mainStage;
	public ExploreController(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void sequentialLaunchButtonAction() {
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
	
	@FXML
	public void modularLaunchButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ERB.fxml"));
			ERBController erbController = new ERBController(mainStage);
			fxmlLoader.setController(erbController);
			Parent rootParent = fxmlLoader.load();
			erbController.setLabel("ERB Window that allows users to jump around chapters");
			Scene scene = new Scene(rootParent);
			mainStage.setScene(scene);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
