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

public class WelcomePanelController implements Initializable{

	@FXML
	VBox welcomeVBox;
	@FXML
	HBox welcomeHBox;
	@FXML
	TreeView<String> welcomeTreeView;
	@FXML
	VBox welcomeContentVBox;
	
	private ERBToolController erbToolController;
	
	public WelcomePanelController(ERBToolController erbToolController) {
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillTreeView();
		addProgressWidget();
		addNavigationPanel();
		welcomeTreeView.getStylesheets().add("/TreeViewStyle.css");
	}
	
	public void addProgressWidget() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressWidgetPanel.fxml"));
		ProgressWidgetPanelController progressWidgetPanelController = new ProgressWidgetPanelController(welcomeContentVBox, welcomeHBox, welcomeTreeView, erbToolController);
		fxmlLoader.setController(progressWidgetPanelController);
		try {
			Node node = fxmlLoader.load();
			welcomeContentVBox.getChildren().add(0, node);
		}catch (Exception e) {
		}
	}
	
	public void addNavigationPanel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NavigationPanel.fxml"));
		NavigationPanelController navigationPanelController= new NavigationPanelController(erbToolController);
		fxmlLoader.setController(navigationPanelController);
		try {
			Node node = fxmlLoader.load();
			welcomeVBox.getChildren().add(1, node);
		} catch (IOException e) {
		}
	}
	
	public void fillTreeView() {
		TreeItem<String> root = new TreeItem<String>("Welcome");
		root.setExpanded(true);
		root.getChildren().add(new TreeItem<String>("About ERB"));
		root.getChildren().add(new TreeItem<String>("Index"));
		root.getChildren().add(new TreeItem<String>("Glossary"));
		root.getChildren().add(new TreeItem<String>("Resources"));

		welcomeTreeView.setRoot(root);
	}

}
