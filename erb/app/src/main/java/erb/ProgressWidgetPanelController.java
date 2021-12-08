package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProgressWidgetPanelController implements Initializable{

	@FXML
	HBox widgetHBox;
	@FXML
	ImageView widgetImageView;
	
	VBox contentVBox;
	HBox contentHBox;
	TreeView<String> treeView;
	ERBToolController erbToolController;
	
	public ProgressWidgetPanelController(VBox contentVBox, HBox contentHBox, TreeView<String> treeView, ERBToolController erbToolController) {
		this.contentVBox = contentVBox;
		this.contentHBox = contentHBox;
		this.treeView = treeView;
		this.erbToolController = erbToolController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		widgetImageView.setImage(new Image(getClass().getResourceAsStream("/ProgressWidget.PNG")));
		Tooltip.install(widgetImageView, new Tooltip("Click here to view progress."));
		widgetImageView.setOnMouseClicked(e -> progressWidgetClicked(e));
	}
	
//	public void progressWidgetClicked(Event event) {
//		VBox progressTrackerVBox = erbToolController.getProgressTrackerController().progressTrackerVBox;
//		if(contentContainsProgressTracker(progressTrackerVBox)) {
//			cleanContentVBox();
//			Tooltip.install(widgetImageView, new Tooltip("Click here to view progress."));
//		}else {
//			VBox.setVgrow(progressTrackerVBox, Priority.ALWAYS);
//			contentVBox.getChildren().add(contentVBox.getChildren().size(), progressTrackerVBox);
//			Tooltip.install(widgetImageView, new Tooltip("Click here to hide progress."));
//		}
//	}
	
	public void progressWidgetClicked(MouseEvent event) {
		VBox progressTrackerVBox = erbToolController.getProgressTrackerController().progressTrackerVBox;
		if (event.getButton() == MouseButton.PRIMARY) {
			if (contentHBoxContainsProgressTracker(progressTrackerVBox)) {
				cleanContentHBox();
				Tooltip.install(widgetImageView, new Tooltip("Click here to view progress."));
				widgetImageView.setOnContextMenuRequested(null);
			} else if (contentVBoxContainsProgressTracker(progressTrackerVBox)) {
				cleanContentVBox();
				Tooltip.install(widgetImageView, new Tooltip("Click here to view progress."));
				widgetImageView.setOnContextMenuRequested(null);
			} else {
				contentHBox.getChildren().add(2, progressTrackerVBox);
				Tooltip.install(widgetImageView, new Tooltip("Click here to hide progress."));
			}
		} else if (event.getButton() == MouseButton.SECONDARY) {
			if (contentHBoxContainsProgressTracker(progressTrackerVBox)) {
				ContextMenu contextMenu = new ContextMenu();
				MenuItem menuItem = new MenuItem("Expand");
				contextMenu.getItems().add(menuItem);
				widgetImageView.setOnContextMenuRequested(e -> { contextMenu.setY(e.getScreenY()); contextMenu.setX(e.getScreenX()); contextMenu.show(widgetImageView.getScene().getWindow()); });
				menuItem.setOnAction(e -> expandProgressTracker(progressTrackerVBox));
			}
		}
	}
	
	public void expandProgressTracker(VBox progressTrackerVBox) {
		cleanContentHBox();
		Tooltip.install(widgetImageView, new Tooltip("Click here to hide progress."));
		contentVBox.getChildren().add(contentVBox.getChildren().size(), progressTrackerVBox);
		widgetImageView.setOnContextMenuRequested(null);
		VBox.setVgrow(progressTrackerVBox, Priority.ALWAYS);
	}
	
	public void cleanContentVBox() {
		int numChildren = contentVBox.getChildren().size();
		for(int i =0; i < numChildren; i++) {
			Node node = contentVBox.getChildren().get(i);
			if(!node.getId().contentEquals("widgetHBox")) {
				contentVBox.getChildren().remove(node);
			}
		}
	}
	
	public void cleanContentHBox() {
		int numChildren = contentHBox.getChildren().size();
		for(int i =0; i < numChildren; i++) {
			Node node = contentHBox.getChildren().get(i);
			if(!node.getId().contentEquals(contentVBox.getId()) && !node.getId().contentEquals(treeView.getId())) {
				contentHBox.getChildren().remove(node);
			}
		}
	}
	
	public boolean contentHBoxContainsProgressTracker(VBox progressTrackerVBox) {
		return contentHBox.getChildren().contains(progressTrackerVBox);
//		return contentVBox.getChildren().contains(progressTrackerVBox);
	}
	
	public boolean contentVBoxContainsProgressTracker(VBox progressTrackerVBox) {
		return contentVBox.getChildren().contains(progressTrackerVBox);
	}

}
