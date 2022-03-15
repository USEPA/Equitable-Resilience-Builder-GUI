//package old;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.stage.Stage;
//
//public class ERBLandingController implements Initializable{
//
//	Stage mainStage;
//	public ERBLandingController(Stage mainStage) {
//		this.mainStage = mainStage;
//	}
//	
//	private Logger logger = LogManager.getLogger(ERBLandingController.class);
//	
//	@FXML
//	Button launchButton;
//	@FXML
//	Label glossaryLabel;
//	@FXML
//	Label resourcesLabel;
//	
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		glossaryLabel.setOnMouseEntered(e-> handleLabelHighlight(glossaryLabel));
//		glossaryLabel.setOnMouseExited(e-> handleLabelUnHighlight(glossaryLabel));
//		glossaryLabel.setOnMouseClicked(e-> handleLabelClicked(glossaryLabel));
//
//		resourcesLabel.setOnMouseEntered(e-> handleLabelHighlight(resourcesLabel));
//		resourcesLabel.setOnMouseExited(e-> handleLabelUnHighlight(resourcesLabel));
//		resourcesLabel.setOnMouseClicked(e-> handleLabelClicked(resourcesLabel));
//	}
//	
//	public void handleLabelHighlight(Label label) {	
//		//Highlight
//		label.setStyle("-fx-background-color: #A2A2A2");
//	}
//	
//	public void handleLabelUnHighlight(Label label) {	
//		//Un-highlight 
//		label.setStyle("-fx-background-color: transparent");
//	}
//	
//	public void handleLabelClicked(Label label) {
//		
//	}
//	
//	@FXML
//	public void launchButtonAction() {
//		try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProjectSelection.fxml"));
//			ProjectSelectionController mainController = new ProjectSelectionController(mainStage);
//			fxmlLoader.setController(mainController);
//			Parent rootParent = fxmlLoader.load();
//			Scene mainScene = new Scene(rootParent);
//			mainStage.setScene(mainScene);
//			mainStage.show();
//		} catch (Exception e2) {
//			logger.error(e2.getMessage());
//		}
//	}
//}
