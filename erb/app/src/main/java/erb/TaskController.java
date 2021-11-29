package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class TaskController implements Initializable{

	@FXML
	VBox taskVBox;
	@FXML
	ProgressIndicator progressIndicator;
	@FXML
	Label label;
	
	ERBTask erbTask;
	public TaskController(ERBTask erbTask) {
		this.erbTask = erbTask;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		progressIndicator.getStylesheets().add("/ProgressIndicator.css");
		label.setText(erbTask.getTitle());
		Tooltip.install(label, new Tooltip(erbTask.getDescription()));
		taskVBox.setId(String.valueOf(erbTask.getId()));
		label.prefWidthProperty().bind(taskVBox.widthProperty());
		label.prefHeightProperty().bind(taskVBox.heightProperty());

	}
}
