package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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
	
	Stage editsStage = null;
	
	public PostItNoteController() {

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initPostItNote();		
	}
	
	public void initPostItNote() {
		postItNotePane.setId("postedNote");
		postItNotePane.setStyle("-fx-background-color: #FFFFFF");
		scrollPane.setStyle("-fx-background-color: #FFFFFF");
		textFlow.setStyle("-fx-background-color: #FFFFFF");
		
		postItNotePane.setPrefWidth(0);
	}
	
	public void postItNoteClicked(MouseEvent mouseEvent) {
		if(mouseEvent.getClickCount() ==2) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/PostItNoteEdits.fxml"));
				PostItNoteEditsController postItNoteEditsController = new PostItNoteEditsController(this);
				fxmlLoader.setController(postItNoteEditsController);
				editsStage = new Stage();
				Scene scene = new Scene(fxmlLoader.load());
				if(textFlow.getChildren().size() > 0) { //Already contains content
					Text text = (Text) textFlow.getChildren().get(0);
					postItNoteEditsController.setText(text.getText());
					postItNoteEditsController.setColor(textFlow.getStyle().replace("-fx-background-color: ", ""));
				}
				editsStage.setScene(scene);
				editsStage.showAndWait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setPostItNoteText(String text) {
		Text textToAdd = new Text(text);
		textFlow.getChildren().add(textToAdd);
	}
	
	public void setPostItNoteColor(String color) {
		postItNotePane.setStyle("-fx-background-color: " + "#" + color);
		scrollPane.setStyle("-fx-background-color: " + "#" + color);
		textFlow.setStyle("-fx-background-color: " + "#" + color);
	}
	
	public void closeEditsStage() {
		if(editsStage != null) {
			editsStage.close();
		}
	}

}
