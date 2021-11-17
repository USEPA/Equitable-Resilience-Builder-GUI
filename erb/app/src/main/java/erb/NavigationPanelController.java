package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class NavigationPanelController implements Initializable {

	@FXML
	HBox navigationHBox;
	@FXML
	Button backButton;
	@FXML
	Button nextButton;
	
	private ERBToolController erbToolController;
	
	public NavigationPanelController(ERBToolController erbToolController) {
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void backButtonAction() {
		String sourceId = getSourceIdForButtonAction();
		if (sourceId.toLowerCase().contains("panel1".toLowerCase())) {
			//No back option
		} else if (sourceId.toLowerCase().contains("panel2".toLowerCase())) {
			erbToolController.panel1LabelAction();
		} else if (sourceId.toLowerCase().contains("panel3".toLowerCase())) {
			erbToolController.panel2LabelAction();
		} else if (sourceId.toLowerCase().contains("panel4".toLowerCase())) {
			erbToolController.panel3LabelAction();
		} else if (sourceId.toLowerCase().contains("panel5".toLowerCase())) {
			erbToolController.panel4LabelAction();
		} else if (sourceId.toLowerCase().contains("panel6".toLowerCase())) {
			erbToolController.panel5LabelAction();
		}
	}
	
	@FXML
	public void nextButtonAction() {
		String sourceId = getSourceIdForButtonAction();
		if (sourceId.toLowerCase().contains("panel1".toLowerCase())) {
			erbToolController.panel2LabelAction();
		} else if (sourceId.toLowerCase().contains("panel2".toLowerCase())) {
			erbToolController.panel3LabelAction();
		} else if (sourceId.toLowerCase().contains("panel3".toLowerCase())) {
			erbToolController.panel4LabelAction();
		} else if (sourceId.toLowerCase().contains("panel4".toLowerCase())) {
			erbToolController.panel5LabelAction();
		} else if (sourceId.toLowerCase().contains("panel5".toLowerCase())) {
			erbToolController.panel6LabelAction();
		} else if (sourceId.toLowerCase().contains("panel6".toLowerCase())) {
			//No next option
		}
	}

	private String getSourceIdForButtonAction() {
		return navigationHBox.getParent().getId();
	}
}
