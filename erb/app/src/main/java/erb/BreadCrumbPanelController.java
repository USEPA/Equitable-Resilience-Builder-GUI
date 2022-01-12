package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.BreadCrumbBar.BreadCrumbActionEvent;

public class BreadCrumbPanelController implements Initializable{

	@FXML
	VBox breadCrumbVBox;
	
	BreadCrumbBar<String> breadCrumbBar;
	
	int chapterNum;
	public BreadCrumbPanelController(int chapterNum) {
		this.chapterNum = chapterNum;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		setBreadCrumbBar();
	}
	
	public void setBreadCrumbBar() {
		Label selectedCrumbLabel = new Label();
		breadCrumbBar = new BreadCrumbBar<>();
		breadCrumbVBox.getChildren().addAll(breadCrumbBar, selectedCrumbLabel);
		if(chapterNum == 1) {
			breadCrumbBar.setSelectedCrumb(getChapter1BreadCrumbs());
		}else if (chapterNum ==2) {
			breadCrumbBar.setSelectedCrumb(getChapter2BreadCrumbs());
		}else if (chapterNum ==3) {
			breadCrumbBar.setSelectedCrumb(getChapter3BreadCrumbs());
		}else if (chapterNum == 4) {
			breadCrumbBar.setSelectedCrumb(getChapter4BreadCrumbs());
		}else if (chapterNum == 5) {
			breadCrumbBar.setSelectedCrumb(getChapter5BreadCrumbs());
		}
		breadCrumbBar.setOnCrumbAction(e -> handleBreadCrumbEvent(e));
	}
	
	public TreeItem<String> getChapter1BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Chapter 1", "Step 1", "Step 2", "Step 3", "Step 4", "Reflect");
		return model;
	}
	
	public TreeItem<String> getChapter2BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Chapter 2", "Step 1", "Step 2", "Step 3", "Step 4", "Reflect");
		return model;
	}
	
	public TreeItem<String> getChapter3BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Chapter 3", "Step 1", "Step 2", "Step 3", "Step 4", "Step 5", "Reflect");
		return model;
	}
	
	public TreeItem<String> getChapter4BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Chapter 4", "Step 1", "Step 2", "Step 3", "Reflect");
		return model;
	}
	
	public TreeItem<String> getChapter5BreadCrumbs() {
		TreeItem<String> model = BreadCrumbBar.buildTreeModel("Chapter 5", "Step 1", "Step 2", "Step 3", "Step 4", "Reflect");
		return model;
	}
	
	public void handleBreadCrumbEvent(BreadCrumbActionEvent<String> event) {
		System.out.println("You just clicked on '" + event.getSelectedCrumb() + "'!");
		clearOldBreadCrumbBar();
		setBreadCrumbBar();
	}
	
	public void clearOldBreadCrumbBar() {
		breadCrumbVBox.getChildren().clear();
	}
}
