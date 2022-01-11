package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.BreadCrumbBar.BreadCrumbActionEvent;

public class BreadCrumbPanelController implements Initializable{

	@FXML
	VBox breadCrumbVBox;
	
	BreadCrumbBar<String> breadCrumbBar;
	
	public BreadCrumbPanelController() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		setBreadCrumbBar();
	}
	
	public void setBreadCrumbBar() {
		Label selectedCrumbLabel = new Label();

		breadCrumbBar = new BreadCrumbBar<>();
		breadCrumbVBox.getChildren().addAll(breadCrumbBar, selectedCrumbLabel);

		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4", "Chapter 5");
		breadCrumbBar.setSelectedCrumb(model);
		breadCrumbBar.setOnCrumbAction(e -> handleBreadCrumbEvent(e));
	}
	
	public void handleBreadCrumbEvent(BreadCrumbActionEvent<String> event) {
		System.out.println("You just clicked on '" + event.getSelectedCrumb() + "'!");
		clearOldBreadCrumbBar();
		setBreadCrumbBar();
	}
	
	public void clearOldBreadCrumbBar() {
		breadCrumbVBox.getChildren().clear();
	}
}
