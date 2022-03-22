package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PostItNoteController implements Initializable{

	@FXML
	VBox postItNoteVBox;
	@FXML
	Label postItLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
				
	}
	
	String postItNoteText;
	String postItNoteColor;
	
	public PostItNoteController(String postItNoteText, String postItNoteColor) {
		this.postItNoteText = postItNoteText;
		this.postItNoteColor = postItNoteColor;
	}
	
	public void initPostItNote() {
		postItLabel.setText(postItNoteText);
		postItNoteVBox.setStyle("-fx-background-color: " + "#" + postItNoteColor);
	}

}
