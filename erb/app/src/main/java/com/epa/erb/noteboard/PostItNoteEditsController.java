package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

public class PostItNoteEditsController implements Initializable{

	@FXML
	TextArea postItNoteTextArea;
	@FXML
	ColorPicker colorPicker;
	@FXML
	Button saveButton;
	
	PostItNoteController postItNoteController;
	
	public PostItNoteEditsController(PostItNoteController postItNoteController) {
		this.postItNoteController = postItNoteController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colorPicker.getStylesheets().add(getClass().getResource("/noteboard/ColorPicker.css").toString());
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
