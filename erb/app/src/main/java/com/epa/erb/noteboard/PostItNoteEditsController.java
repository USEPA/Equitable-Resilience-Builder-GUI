package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PostItNoteEditsController implements Initializable{

	@FXML
	TextArea postItNoteTextArea;
	@FXML
	ColorPicker colorPicker;
	@FXML
	Button saveButton;
	
	private PostItNoteController postItNoteController;
	public PostItNoteEditsController(PostItNoteController postItNoteController) {
		this.postItNoteController = postItNoteController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkForExistingText();
		checkForExistingColor();
		colorPicker.getStylesheets().add(getClass().getResource("/noteboard/ColorPicker.css").toString());
	}
	
	private void checkForExistingText() {
		if (postItNoteController.getTextFlow().getChildren().size() > 0) {
			Text text = (Text) postItNoteController.getTextFlow().getChildren().get(0);
			if (text != null) {
				String postItString = text.getText();
				postItNoteTextArea.setText(postItString);
			}
		}
	}
	
	private void checkForExistingColor() {
		if(postItNoteController.getTextFlow().getStyle() != null) {
			String color = postItNoteController.getTextFlow().getStyle().replace("-fx-background-color: ", "");
			color = color.replace(";", "");
			setColor(color);
		}
	}
	
	@FXML
	public void saveButtonAction() {
		postItNoteController.setPostItNoteText(postItNoteTextArea.getText());
		postItNoteController.setPostItNoteColor(parseColorAsHex(colorPicker.getValue()));
		postItNoteController.closeEditsStage();
	}
	
	private String parseColorAsHex(Color color) {
		String colorAsString = color.toString();
		String colorAsHexString = colorAsString.replaceAll("0x", "");
		colorAsHexString = colorAsHexString.substring(0, colorAsHexString.length()-2);
		return colorAsHexString;
	}
	
	void setColor(String color) {
		Color colorToSelect = Color.web(color, 1.0);
		colorPicker.setValue(colorToSelect);
	}
	
	void setText(String text) {
		postItNoteTextArea.setText(text);
	}

}
