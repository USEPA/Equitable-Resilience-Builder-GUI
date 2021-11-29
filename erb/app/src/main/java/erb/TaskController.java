package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;

public class TaskController implements Initializable{

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

	}
}
