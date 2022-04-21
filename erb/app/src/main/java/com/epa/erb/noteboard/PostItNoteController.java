package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class PostItNoteController implements Initializable{

	@FXML
	VBox postItNotePane;
	@FXML
	TextFlow textFlow;
	@FXML
	ScrollPane scrollPane;
	
	public PostItNoteController() {

	}
	
	private Stage editsStage = null;
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(PostItNoteController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initPostItNote();	
		initializeStyle();
	}
	
	private void initializeStyle() {
		textFlow.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
		postItNotePane.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
		scrollPane.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
		textFlow.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
	}
	
	private void initPostItNote() {
		postItNotePane.setId("postedNote");
		postItNotePane.setPrefWidth(0);
	}
	
	@FXML
	public void postItNoteClicked(MouseEvent mouseEvent) {
		if(mouseEvent.getClickCount() ==2) {
			postItNoteDoubleClicked(mouseEvent);
		} else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			postItNoteRightClicked(mouseEvent);
		}
	}
	
	private void postItNoteDoubleClicked(MouseEvent mouseEvent) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/PostItNoteEdits.fxml"));
			PostItNoteEditsController postItNoteEditsController = new PostItNoteEditsController(this);
			fxmlLoader.setController(postItNoteEditsController);
			editsStage = new Stage();
			Scene scene = new Scene(fxmlLoader.load());
			editsStage.setScene(scene);
			editsStage.setTitle("Post It Note Edits");
			editsStage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void postItNoteRightClicked(MouseEvent mouseEvent) {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem("Remove");
		contextMenu.getItems().add(menuItem);
		scrollPane.setContextMenu(contextMenu);
		menuItem.setOnAction(e -> removePostItNote());
	}
	
	private void removePostItNote() {
		HBox postItHBox = (HBox) postItNotePane.getParent();
		postItHBox.getChildren().remove(postItNotePane);
	}
	
	void setPostItNoteText(String text) {
		Text textToAdd = new Text(text);
		textFlow.getChildren().clear();
		textFlow.getChildren().add(textToAdd);
	}
	
	void setPostItNoteColor(String color) {
		postItNotePane.setStyle("-fx-background-color: " + "#" + color);
		scrollPane.setStyle("-fx-background-color: " + "#" + color);
		textFlow.setStyle("-fx-background-color: " + "#" + color);
	}
	
	void closeEditsStage() {
		if(editsStage != null) {
			editsStage.close();
		}
	}
	
	TextFlow getTextFlow() {
		return textFlow;
	}

}