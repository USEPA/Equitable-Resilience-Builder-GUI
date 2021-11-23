package erb;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
	
	private ERBToolController erbToolController;
	
	public Chapter3PanelController(ERBToolController erbToolController) {
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
		addProgressWidget();
		addNavigationPanel();
	}
	
	public void addProgressWidget() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressWidgetPanel.fxml"));
		ProgressWidgetPanelController progressWidgetPanelController= new ProgressWidgetPanelController(erbToolController);
		fxmlLoader.setController(progressWidgetPanelController);
		try {
			Node node = fxmlLoader.load();
			chapter3ContentVBox.getChildren().add(0, node);
		} catch (IOException e) {
		}
	}
	
	public void addNavigationPanel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NavigationPanel.fxml"));
		NavigationPanelController navigationPanelController= new NavigationPanelController(erbToolController);
		fxmlLoader.setController(navigationPanelController);
		try {
			Node node = fxmlLoader.load();
			chapter3VBox.getChildren().add(1, node);
		} catch (IOException e) {
		}
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
