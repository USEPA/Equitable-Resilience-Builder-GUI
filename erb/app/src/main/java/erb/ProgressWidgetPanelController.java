package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ProgressWidgetPanelController implements Initializable{

	@FXML
	HBox widgetHBox;
	@FXML
	ImageView widgetImageView;
	
	public ProgressWidgetPanelController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		widgetImageView.setImage(new Image(getClass().getResourceAsStream("/ProgressWidget.PNG")));
		widgetImageView.setOnMouseClicked(e -> progressWidgetClicked());
		Tooltip.install(widgetImageView, new Tooltip("Click here to view progress."));
	}
	
	public void progressWidgetClicked() {

	}

}
