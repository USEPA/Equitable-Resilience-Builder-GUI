package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void customizeButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CustomizedProject.fxml"));
			CustomizedProjectController customizedExperienceController = new CustomizedProjectController(mainStage);
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
			Wizard wizard = new Wizard(mainStage);
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
