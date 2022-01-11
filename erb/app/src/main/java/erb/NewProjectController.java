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

	@FXML
	Button customizeButton;
	@FXML
	Button exploreButton;
	
	Stage mainStage;
	public NewProjectController(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
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
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Explore.fxml"));
			ExploreController exploreController = new ExploreController(mainStage);
			fxmlLoader.setController(exploreController);
			Parent rootParent = fxmlLoader.load();
			Scene scene = new Scene(rootParent);
			mainStage.setScene(scene);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
