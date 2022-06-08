package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PostItNoteEditsController implements Initializable{

	@FXML
	ColorPicker colorPicker;
	@FXML
	TextArea postItNoteTextArea;

	private App app;
	private PostItNoteController postItNoteController;
	public PostItNoteEditsController(App app, PostItNoteController postItNoteController) {
		this.app = app;
		this.postItNoteController = postItNoteController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		checkAndSetExistingText();
		checkAndSetExistingColor();
	}
	
	private void handleControls() {
		colorPicker.getStylesheets().add(getClass().getResource("/noteboard/ColorPicker.css").toString());
	}
	
	private void checkAndSetExistingText() {
		if (postItNoteController.getTextFlow().getChildren().size() > 0) {
			Text text = (Text) postItNoteController.getTextFlow().getChildren().get(0);
			if (text != null) {
				String postItString = text.getText();
				postItNoteTextArea.setText(postItString);
			}
		}
	}
	
	private void checkAndSetExistingColor() {
		if(postItNoteController.getTextFlow().getStyle() != null) {
			String color = postItNoteController.getTextFlow().getStyle().replace("-fx-background-color: ", "");
			color = color.replace(";", "");
			setColorPickerColor(color);
		}
	}
	
	@FXML
	public void saveButtonAction() {
		postItNoteController.setPostItNoteText(postItNoteTextArea.getText());
		postItNoteController.setPostItContentsColor(parseColorAsHex(colorPicker.getValue()));
		postItNoteController.closeEditsStage();
		app.setNeedsSaving(true);
	}
	
	private String parseColorAsHex(Color color) {
		String colorAsString = color.toString();
		String colorAsHexString = colorAsString.replaceAll("0x", "");
		colorAsHexString = colorAsHexString.substring(0, colorAsHexString.length()-2);
		return colorAsHexString;
	}
	
	void setColorPickerColor(String color) {
		Color colorToSelect = Color.web(color, 1.0);
		colorPicker.setValue(colorToSelect);
	}
	
	void setPostItNoteTextAreaText(String text) {
		postItNoteTextArea.setText(text);
	}
	
}
