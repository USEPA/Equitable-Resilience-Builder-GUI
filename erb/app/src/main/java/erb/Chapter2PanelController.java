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

public class Chapter2PanelController implements Initializable{
	
	@FXML
	VBox chapter2VBox;
	@FXML
	HBox chapter2HBox;
	@FXML
	TreeView<String> chapter2TreeView;
	@FXML
	VBox chapter2ContentVBox;
	
	private ERBToolController erbToolController;
	
	public Chapter2PanelController(ERBToolController erbToolController) {
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
		addProgressWidget();
		addNavigationPanel();
		chapter2TreeView.getStylesheets().add("/TreeViewStyle.css");
	}
	
	public void addProgressWidget() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressWidgetPanel.fxml"));
		ProgressWidgetPanelController progressWidgetPanelController= new ProgressWidgetPanelController(chapter2ContentVBox, chapter2HBox, chapter2TreeView, erbToolController);
		fxmlLoader.setController(progressWidgetPanelController);
		try {
			Node node = fxmlLoader.load();
			chapter2ContentVBox.getChildren().add(0, node);
		} catch (IOException e) {
		}
	}
	
	public void addNavigationPanel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NavigationPanel.fxml"));
		NavigationPanelController navigationPanelController= new NavigationPanelController(erbToolController);
		fxmlLoader.setController(navigationPanelController);
		try {
			Node node = fxmlLoader.load();
			chapter2VBox.getChildren().add(1, node);
		} catch (IOException e) {
		}
	}
	
	public void fillTreeView() {
		TreeItem<String> root = new TreeItem<String>("Chapter 2");
		root.setExpanded(true);
		root.getChildren().add(new TreeItem<String>("About"));
		root.getChildren().add(new TreeItem<String>("Community Engagement"));
		root.getChildren().add(new TreeItem<String>("Storytelling"));
		root.getChildren().add(new TreeItem<String>("Build Relationships"));
		root.getChildren().add(new TreeItem<String>("Reflection"));

		chapter2TreeView.setRoot(root);
	}

}
