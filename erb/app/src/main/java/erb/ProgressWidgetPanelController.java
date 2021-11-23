package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProgressWidgetPanelController implements Initializable{

	@FXML
	HBox widgetHBox;
	@FXML
	ImageView widgetImageView;
	
	VBox contentVBox;
	ERBToolController erbToolController;
	
	public ProgressWidgetPanelController(VBox contentVBox, ERBToolController erbToolController) {
		this.contentVBox = contentVBox;
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		widgetImageView.setImage(new Image(getClass().getResourceAsStream("/ProgressWidget.PNG")));
		Tooltip.install(widgetImageView, new Tooltip("Click here to view progress."));
		widgetImageView.setOnMouseClicked(e -> progressWidgetClicked(e));
	}
	
	public void progressWidgetClicked(Event event) {
		VBox progressTrackerVBox = erbToolController.getProgressTrackerController().progressTrackerVBox;
		if(contentContainsProgressTracker(progressTrackerVBox)) {
			cleanContentVBox();
			Tooltip.install(widgetImageView, new Tooltip("Click here to view progress."));
		}else {
			VBox.setVgrow(progressTrackerVBox, Priority.ALWAYS);
			contentVBox.getChildren().add(1, progressTrackerVBox);
			Tooltip.install(widgetImageView, new Tooltip("Click here to hide progress."));
		}
	}
	
	public void cleanContentVBox() {
		int numChildren = contentVBox.getChildren().size();
		for(int i =0; i < numChildren; i++) {
			Node node = contentVBox.getChildren().get(i);
			if(!node.getId().contentEquals("widgetHBox")) {
				contentVBox.getChildren().remove(node);
			}
		}
	}
	
	public boolean contentContainsProgressTracker(VBox progressTrackerVBox) {
		return contentVBox.getChildren().contains(progressTrackerVBox);
	}

}
