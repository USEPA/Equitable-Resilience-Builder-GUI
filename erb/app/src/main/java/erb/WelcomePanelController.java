package erb;

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
	}
	
	public void addProgressWidget() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressWidgetPanel.fxml"));
		ProgressWidgetPanelController progressWidgetPanelController = new ProgressWidgetPanelController();
		fxmlLoader.setController(progressWidgetPanelController);
		try {
			Node node = fxmlLoader.load();
			welcomeContentVBox.getChildren().add(0, node);
		}catch (Exception e) {
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
