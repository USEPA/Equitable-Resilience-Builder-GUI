package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chapter4PanelController implements Initializable{

	@FXML
	VBox chapter4VBox;
	@FXML
	HBox chapter4HBox;
	@FXML
	TreeView<String> chapter4TreeView;
	@FXML
	VBox chapter4ContentVBox;
	
	private ERBToolController erbToolController;
	
	public Chapter4PanelController(ERBToolController erbToolController) {
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
	}
	
	public void fillTreeView() {
		TreeItem<String> root = new TreeItem<String>("Chapter 4");
		root.setExpanded(true);
		root.getChildren().add(new TreeItem<String>("Measure Indicators"));
		root.getChildren().add(new TreeItem<String>("Sort Indicators"));
		root.getChildren().add(new TreeItem<String>("Score Indicators"));
		root.getChildren().add(new TreeItem<String>("Visualize Indicators"));
		root.getChildren().add(new TreeItem<String>("Reflection"));

		chapter4TreeView.setRoot(root);
	}

}
