package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class ChapterTitledPaneController implements Initializable{

	@FXML
	TitledPane titledPane;
	@FXML
	VBox titledPaneVBox;
	@FXML
	ListView<SelectedActivity> titledPaneListView;
	
	String paneTitle;
	public ChapterTitledPaneController(String paneTitle) {
		this.paneTitle = paneTitle;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titledPane.setExpanded(true);
		titledPane.setText(paneTitle);
	}
		
	public void addLabel(String labelText) {
		Label label = new Label(labelText);
		titledPaneVBox.getChildren().add(label);
	}
	
	public void addLabel(String labelText, String labelId) {
		Label label = new Label(labelText);
		label.setId(labelId);
		titledPaneVBox.getChildren().add(label);
	}
	
	public void removeLabel(String labelId) {
		Label labelToRemove = null;
		for (int i = 0; i < titledPaneVBox.getChildren().size(); i++) {
			Label label = (Label) titledPaneVBox.getChildren().get(i);
			if (label.getId().contentEquals(labelId)) {
				labelToRemove = label;
			}
		}
		if (labelToRemove != null) {
			titledPaneVBox.getChildren().remove(labelToRemove);
		}
	}
	
	public String getPaneTitle() {
		return paneTitle;
	}

	public VBox getTitledPaneVBox() {
		return titledPaneVBox;
	}

	public ListView<SelectedActivity> getTitledPaneListView() {
		return titledPaneListView;
	}		
}
