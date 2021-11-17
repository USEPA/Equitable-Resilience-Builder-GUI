package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chapter1PanelController implements Initializable{
	
	@FXML
	VBox chapter1VBox;
	@FXML
	HBox chapter1HBox;
	@FXML
	TreeView<String> chapter1TreeView;
	@FXML
	VBox chapter1ContentVBox;
	
	private ERBToolController erbToolController;
	
	public Chapter1PanelController(ERBToolController erbToolController) {
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
	}
	
	public void fillTreeView() {
		TreeItem<String> root = new TreeItem<String>("Chapter 1");
		root.setExpanded(true);
		root.getChildren().add(new TreeItem<String>("Create Goals"));
		root.getChildren().add(new TreeItem<String>("Gather Core Team"));
		root.getChildren().add(new TreeItem<String>("Project Plan"));
		root.getChildren().add(new TreeItem<String>("Reflection"));

		chapter1TreeView.setRoot(root);
	}

}
