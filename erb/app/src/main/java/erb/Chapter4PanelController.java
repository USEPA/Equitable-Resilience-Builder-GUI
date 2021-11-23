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
		addProgressWidget();
		addNavigationPanel();
	}
	
	public void addProgressWidget() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressWidgetPanel.fxml"));
		ProgressWidgetPanelController progressWidgetPanelController= new ProgressWidgetPanelController(erbToolController);
		fxmlLoader.setController(progressWidgetPanelController);
		try {
			Node node = fxmlLoader.load();
			chapter4ContentVBox.getChildren().add(0, node);
		} catch (IOException e) {
		}
	}
	
	public void addNavigationPanel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NavigationPanel.fxml"));
		NavigationPanelController navigationPanelController= new NavigationPanelController(erbToolController);
		fxmlLoader.setController(navigationPanelController);
		try {
			Node node = fxmlLoader.load();
			chapter4VBox.getChildren().add(1, node);
		} catch (IOException e) {
		}
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
