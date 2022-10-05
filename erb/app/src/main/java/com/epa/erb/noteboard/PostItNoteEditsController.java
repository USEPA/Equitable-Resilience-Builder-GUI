package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private PostItNoteController postItNoteController;
	public PostItNoteEditsController(PostItNoteController postItNoteController) {
		this.postItNoteController = postItNoteController;
	}
	
	private Logger logger = LogManager.getLogger(PostItNoteEditsController.class);
	
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
//		postItNoteController.getNoteBoardContentController().getActivity().setSaved(false);
	}
	
	private String parseColorAsHex(Color color) {
		if (color != null) {
			String colorAsString = color.toString();
			String colorAsHexString = colorAsString.replaceAll("0x", "");
			colorAsHexString = colorAsHexString.substring(0, colorAsHexString.length() - 2);
			return colorAsHexString;
		} else {
			logger.error("Cannot parseColorAsHex. color is null.");
			return null;
		}
	}

	void setColorPickerColor(String color) {
		if (color != null) {
			Color colorToSelect = Color.web(color, 1.0);
			colorPicker.setValue(colorToSelect);
		} else {
			logger.error("Cannot setColorPickerColor. color is null");
		}
	}
	
	void setPostItNoteTextAreaText(String text) {
		if(text != null) postItNoteTextArea.setText(text);
	}

	public PostItNoteController getPostItNoteController() {
		return postItNoteController;
	}

	public void setPostItNoteController(PostItNoteController postItNoteController) {
		this.postItNoteController = postItNoteController;
	}

	public ColorPicker getColorPicker() {
		return colorPicker;
	}

	public TextArea getPostItNoteTextArea() {
		return postItNoteTextArea;
	}
		
}
