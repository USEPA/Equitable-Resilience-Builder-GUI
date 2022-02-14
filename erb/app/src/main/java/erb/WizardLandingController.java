package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class WizardLandingController implements Initializable{

	Stage mainStage;
	WizardContainerController wizardContainerController;
	public WizardLandingController(WizardContainerController wizardContainerController, Stage mainStage) {
		this.mainStage = mainStage;
		this.wizardContainerController = wizardContainerController;
	}
	
	@FXML
	ImageView chap1ImageView;
	@FXML
	ImageView chap2ImageView;
	@FXML
	ImageView chap3ImageView;
	@FXML
	ImageView chap4ImageView;
	@FXML
	ImageView chap5ImageView;
	@FXML
	Label glossaryLabel;
	@FXML
	Label resourcesLabel;
	@FXML
	Label projectSelectionLabel;
	@FXML
	Label erbLandingLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chap1ImageView.setOnMouseClicked(e-> chapterImageSelected(1));
		chap2ImageView.setOnMouseClicked(e-> chapterImageSelected(2));
		chap3ImageView.setOnMouseClicked(e-> chapterImageSelected(3));
		chap4ImageView.setOnMouseClicked(e-> chapterImageSelected(4));
		chap5ImageView.setOnMouseClicked(e-> chapterImageSelected(5));
		
		glossaryLabel.setOnMouseEntered(e-> handleLabelHighlight(glossaryLabel));
		glossaryLabel.setOnMouseExited(e-> handleLabelUnHighlight(glossaryLabel));
		glossaryLabel.setOnMouseClicked(e-> handleLabelClicked(glossaryLabel));

		resourcesLabel.setOnMouseEntered(e-> handleLabelHighlight(resourcesLabel));
		resourcesLabel.setOnMouseExited(e-> handleLabelUnHighlight(resourcesLabel));
		resourcesLabel.setOnMouseClicked(e-> handleLabelClicked(resourcesLabel));
		
		projectSelectionLabel.setOnMouseEntered(e-> handleLabelHighlight(projectSelectionLabel));
		projectSelectionLabel.setOnMouseExited(e-> handleLabelUnHighlight(projectSelectionLabel));
		projectSelectionLabel.setOnMouseClicked(e-> handleLabelClicked(projectSelectionLabel));
		
		erbLandingLabel.setOnMouseEntered(e-> handleLabelHighlight(erbLandingLabel));
		erbLandingLabel.setOnMouseExited(e-> handleLabelUnHighlight(erbLandingLabel));
		erbLandingLabel.setOnMouseClicked(e-> handleLabelClicked(erbLandingLabel));
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
		}else if (label == erbLandingLabel) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ERBLanding.fxml"));
				ERBLandingController erbLandingController = new ERBLandingController(mainStage);
				fxmlLoader.setController(erbLandingController);
				Parent rootParent = fxmlLoader.load();
				Scene mainScene = new Scene(rootParent);
				mainStage.setScene(mainScene);
				mainStage.setTitle("ERB");
				mainStage.show();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public void chapterImageSelected(int chapterNum) {
		if(chapterNum == 1) {
			wizardContainerController.loadWizardPanel("1.0");
		} else if (chapterNum == 2) {
			wizardContainerController.loadWizardPanel("2.0");
		} else if (chapterNum == 3) {
			wizardContainerController.loadWizardPanel("3.0");
		} else if (chapterNum == 4) {
			wizardContainerController.loadWizardPanel("4.0");
		} else if (chapterNum == 5) {
			wizardContainerController.loadWizardPanel("5.0");
		}
	}

}
