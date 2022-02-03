package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

public class WizardLandingController implements Initializable{

	WizardContainerController wizardContainerController;
	public WizardLandingController(WizardContainerController wizardContainerController) {
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chap1ImageView.setOnMouseClicked(e-> chapterImageSelected(1));
		chap2ImageView.setOnMouseClicked(e-> chapterImageSelected(2));
		chap3ImageView.setOnMouseClicked(e-> chapterImageSelected(3));
		chap4ImageView.setOnMouseClicked(e-> chapterImageSelected(4));
		chap5ImageView.setOnMouseClicked(e-> chapterImageSelected(5));
	}
	
	public void chapterImageSelected(int chapterNum) {

	}

}
