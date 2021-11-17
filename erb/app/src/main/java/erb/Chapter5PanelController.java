package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chapter5PanelController implements Initializable{

	@FXML
	VBox chapter5VBox;
	@FXML
	HBox chapter5HBox;
	@FXML
	TreeView<String> chapter5TreeView;
	@FXML
	VBox chapter5ContentVBox;
	
	private ERBToolController erbToolController;
	
	public Chapter5PanelController(ERBToolController erbToolController) {
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
	}
	
	public void fillTreeView() {
		TreeItem<String> root = new TreeItem<String>("Chapter 5");
		root.setExpanded(true);
		root.getChildren().add(new TreeItem<String>("Reflect & Plan"));
		root.getChildren().add(new TreeItem<String>("Select Top Concerns"));
		root.getChildren().add(new TreeItem<String>("Identify Actions"));
		root.getChildren().add(new TreeItem<String>("Evaluate Actions"));
		root.getChildren().add(new TreeItem<String>("Reflection"));

		chapter5TreeView.setRoot(root);
	}

}
