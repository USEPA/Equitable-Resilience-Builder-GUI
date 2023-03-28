package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.utility.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class NoteBoardItemController implements Initializable{

	@FXML
	VBox mainVBox;
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

	void setNumberLabelText(String text) {
		numberLabel.setText(text);
	}

	public Stage getEditsStage() {
		return editsStage;
	}

	public void setEditsStage(Stage editsStage) {
		this.editsStage = editsStage;
	}

	public Label getNumberLabel() {
		return numberLabel;
	}

	public TextFlow getTextFlow() {
		return textFlow;
	}

}
