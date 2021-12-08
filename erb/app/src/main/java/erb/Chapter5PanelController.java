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
		addProgressWidget();
		addNavigationPanel();
		chapter5TreeView.getStylesheets().add("/TreeViewStyle.css");
	}
	
	public void addProgressWidget() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressWidgetPanel.fxml"));
		ProgressWidgetPanelController progressWidgetPanelController= new ProgressWidgetPanelController(chapter5ContentVBox, chapter5HBox, chapter5TreeView,  erbToolController);
		fxmlLoader.setController(progressWidgetPanelController);
		try {
			Node node = fxmlLoader.load();
			chapter5ContentVBox.getChildren().add(0, node);
		} catch (IOException e) {
		}
	}
	
	public void addNavigationPanel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NavigationPanel.fxml"));
		NavigationPanelController navigationPanelController= new NavigationPanelController(erbToolController);
		fxmlLoader.setController(navigationPanelController);
		try {
			Node node = fxmlLoader.load();
			navigationPanelController.nextButton.setVisible(false);
			chapter5VBox.getChildren().add(1, node);
		} catch (IOException e) {
		}
	}
	
	public void fillTreeView() {
		TreeItem<String> root = new TreeItem<String>("Chapter 5");
		root.setExpanded(true);
		root.getChildren().add(new TreeItem<String>("About"));
		root.getChildren().add(new TreeItem<String>("Review & Plan"));
		root.getChildren().add(new TreeItem<String>("Identify Actions"));
		root.getChildren().add(new TreeItem<String>("Prioritize Actions"));
		root.getChildren().add(new TreeItem<String>("Reflection"));

		chapter5TreeView.setRoot(root);
	}

}
