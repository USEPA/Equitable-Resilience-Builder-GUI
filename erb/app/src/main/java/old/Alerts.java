//package old;
//
//import java.util.Optional;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.ButtonType;
//
//public class Alerts {
//	
//	public Alerts () {
//		
//	}
//	
//	public Optional<ButtonType> showReturnToMainMenuAlert() {
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setOnCloseRequest(e -> alert.close());
//		alert.setHeaderText(null);
//		alert.setContentText("Would you like to return to the main page?");
//		alert.setTitle("Confirmation");
//		Optional<ButtonType> result = alert.showAndWait();
//		return result;
//	}
//}
