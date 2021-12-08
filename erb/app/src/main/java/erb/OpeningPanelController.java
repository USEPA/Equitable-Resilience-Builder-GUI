package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

public class OpeningPanelController implements Initializable{

	public OpeningPanelController() {
		
	}
	
	@FXML
	VBox openingVBox;
	@FXML
	Button newSessionButton;
	@FXML
	ChoiceBox<String> previousSessionChoiceBox;
	@FXML
	ProgressIndicator chapter1ProgressIndicator;
	@FXML
	ProgressIndicator chapter2ProgressIndicator;
	@FXML
	ProgressIndicator chapter3ProgressIndicator;
	@FXML
	ProgressIndicator chapter4ProgressIndicator;
	@FXML
	ProgressIndicator chapter5ProgressIndicator;
	@FXML
	Button goButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
