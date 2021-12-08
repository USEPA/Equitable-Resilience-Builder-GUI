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
		label.setText(erbTask.getTitle());
		Tooltip.install(label, new Tooltip(erbTask.getDescription()));
		taskVBox.setId(String.valueOf(erbTask.getId()));
		label.prefWidthProperty().bind(taskVBox.widthProperty());
		label.prefHeightProperty().bind(taskVBox.heightProperty());
		setLabelColor();
		setProgressIndicatorId();
		progressIndicator.getStylesheets().add("/ProgressIndicator.css");
	}
	
	public void setLabelColor() {
		if(erbTask.getChapterAssignment() != null) {
			if(erbTask.getChapterAssignment().toLowerCase().contains("chapter1")) {
				label.setStyle("-fx-text-fill: #ef7777");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter2")) {
				label.setStyle("-fx-text-fill: #87b472");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter3")) {
				label.setStyle("-fx-text-fill: #1ccbea");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter4")) {
				label.setStyle("-fx-text-fill: #d699ff");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter5")) {
				label.setStyle("-fx-text-fill: #fc9e2a");
			}
		}
	}
	
	public void setProgressIndicatorId() {
		if(erbTask.getChapterAssignment() != null) {
			if(erbTask.getChapterAssignment().toLowerCase().contains("chapter1")) {
				progressIndicator.setId("chapter1ProgressIndicator");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter2")) {
				progressIndicator.setId("chapter2ProgressIndicator");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter3")) {
				progressIndicator.setId("chapter3ProgressIndicator");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter4")) {
				progressIndicator.setId("chapter4ProgressIndicator");
			}else if(erbTask.getChapterAssignment().toLowerCase().contains("chapter5")) {
				progressIndicator.setId("chapter5ProgressIndicator");
			}
		}
	}
}
