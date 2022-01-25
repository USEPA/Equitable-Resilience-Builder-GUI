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

public class ERBLandingController implements Initializable{

	Stage mainStage;
	public ERBLandingController(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
	@FXML
	Button launchButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void launchButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
			MainController mainController = new MainController(mainStage);
			fxmlLoader.setController(mainController);
			Parent rootParent = fxmlLoader.load();
			Scene mainScene = new Scene(rootParent);
			mainStage.setScene(mainScene);
			mainStage.show();
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
	}

}
