package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chapter3PanelController implements Initializable{

	@FXML
	VBox chapter3VBox;
	@FXML
	HBox chapter3HBox;
	@FXML
	TreeView<String> chapter3TreeView;
	@FXML
	VBox chapter3ContentVBox;
	
	public Chapter3PanelController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
	}
	
	public void fillTreeView() {
		TreeItem<String> root = new TreeItem<String>("Chapter 3");
		root.setExpanded(true);
		root.getChildren().add(new TreeItem<String>("Describe Hazards"));
		root.getChildren().add(new TreeItem<String>("Gather Data"));
		root.getChildren().add(new TreeItem<String>("Workshop"));
		root.getChildren().add(new TreeItem<String>("Reflection"));

		chapter3TreeView.setRoot(root);
	}

}
