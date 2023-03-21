package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epa.erb.utility.Constants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class NoteBoardItemController implements Initializable{

	@FXML
	VBox mainVBox;
	@FXML
	HBox plusHBox;
	@FXML
	Label numberLabel;
	@FXML
	TextFlow textFlow;
	@FXML
	VBox noteBoardItemVBox;

	protected NoteBoardContentController noteBoardContentController;
	public NoteBoardItemController(NoteBoardContentController noteBoardContentController ) {
		this.noteBoardContentController = noteBoardContentController;
	}
	
	private Stage editsStage = null;
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(NoteBoardItemController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initPostItNote();	
		handleControls();
	}
	
	private void initPostItNote() {
		noteBoardItemVBox.setPrefWidth(0);
		noteBoardItemVBox.setId("noteBoardItem");
	}
	
	private void handleControls() {
		textFlow.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
		plusHBox.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
		noteBoardItemVBox.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
	}
	
	void setNoteBoardItemText(String text) {
		if (text != null) {
			Text textToAdd = new Text(text);
			textFlow.getChildren().clear();
			textFlow.getChildren().add(textToAdd);
		} else {
			logger.error("Cannot setPostItNoteText. text is null.");
		}
	}
	
	protected void removePlusHBox() {
		if(mainVBox.getChildren().contains(plusHBox)) {
			mainVBox.getChildren().remove(plusHBox);
		}
	}
	
	@FXML
	public void plusClicked() {
//		int numberOfPlus = Integer.parseInt(numberLabel.getText());
//		numberOfPlus++;
//		setNumberLabelText(String.valueOf(numberOfPlus));
//		noteBoardContentController.getActivity().setSaved(false);
	}
	
	@FXML
	public void minusClicked() {
//		int numberOfPlus = Integer.parseInt(numberLabel.getText());
//		numberOfPlus--;
//		setNumberLabelText(String.valueOf(numberOfPlus));
//		noteBoardContentController.getActivity().setSaved(false);
	}
	
	@FXML
	public void postItNoteClicked(MouseEvent mouseEvent) {
//		if(mouseEvent.getClickCount() ==2) {
//			postItNoteDoubleClicked(mouseEvent);
//		} 
	}
	
//	private void postItNoteDoubleClicked(MouseEvent mouseEvent) {
//		Parent postItNoteEditsRoot = loadPostItNoteEdits();
//		showPostItNoteEdits(postItNoteEditsRoot);
//	}
	
//	private Parent loadPostItNoteEdits() {
//		try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/PostItNoteEdits.fxml"));
//			PostItNoteEditsController postItNoteEditsController = new PostItNoteEditsController(this);
//			fxmlLoader.setController(postItNoteEditsController);
//			return fxmlLoader.load();
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return null;
//		}
//	}
	
//	private void showPostItNoteEdits(Parent postItNoteEditsRoot) {
//		if (postItNoteEditsRoot != null) {
//			editsStage = new Stage();
//			Scene scene = new Scene(postItNoteEditsRoot);
//			editsStage.setScene(scene);
//			editsStage.setTitle("ERB: Post It Note Edits");
//			editsStage.showAndWait();
//		} else {
//			logger.error("Cannot showPostItNoteEdits. postItNoteEditsRoot is null.");
//		}
//	}
	
//	void setPostItContentsColor(String color) {
//		if (color != null) {
//			postItNotePane.setStyle("-fx-background-color: " + "#" + color);
//			textFlow.setStyle("-fx-background-color: " + "#" + color);
//			plusHBox.setStyle("-fx-background-color: " + "#" + color);
//		} else {
//			logger.error("Cannot setPostItContentsColor. color is null.");
//		}
//	}
//	
//	void closeEditsStage() {
//		if(editsStage != null) {
//			editsStage.close();
//		}
//	}
	
//	public int getPostItNoteIndex(NoteBoardRowController categorySectionController) {
//		return categorySectionController.getPostItHBox().getChildren().indexOf(getPostItNotePane());
//	}
	
//	public String getPostItNoteText() {
//		String postItText = "";
//		if (getTextFlow().getChildren() != null) {
//			Text text = (Text) getTextFlow().getChildren().get(0);
//			postItText = text.getText();
//		}
//		return postItText;
//	}
	
//	public String getPostItNoteColor() {
//		String style = getPlusHBox().getStyle();
//		style = style.replace("-fx-background-color: ", "");
//		return style.trim();
//	}

	void setNumberLabelText(String text) {
		numberLabel.setText(text);
	}

	public Stage getEditsStage() {
		return editsStage;
	}

	public void setEditsStage(Stage editsStage) {
		this.editsStage = editsStage;
	}

	public HBox getPlusHBox() {
		return plusHBox;
	}

	public Label getNumberLabel() {
		return numberLabel;
	}

//	public VBox getNoteBoardItemVBox() {
//		return noteBoardItemVBox;
//	}

	public TextFlow getTextFlow() {
		return textFlow;
	}
	
//	public NoteBoardContentController getNoteBoardContentController() {
//		return noteBoardContentController;
//	}
//
//	public void setNoteBoardContentController(NoteBoardContentController noteBoardContentController) {
//		this.noteBoardContentController = noteBoardContentController;
//	}

}
