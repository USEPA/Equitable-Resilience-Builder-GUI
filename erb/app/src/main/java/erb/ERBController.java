package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ERBController implements Initializable{

	@FXML
	Label descriptionLabel;
	
	Stage mainStage;
	public ERBController(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void setLabel(String string) {
		descriptionLabel.setText(string);
	}
}
