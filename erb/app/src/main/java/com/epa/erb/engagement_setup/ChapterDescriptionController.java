package com.epa.erb.engagement_setup;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ChapterDescriptionController implements Initializable{

	@FXML
	TextArea descriptionTextArea;
	@FXML
	Button saveButton;
	
	Chapter chapter;
	ChapterTitledPaneController chapterTitledPaneController;
	public ChapterDescriptionController(Chapter chapter, ChapterTitledPaneController chapterTitledPaneController) {
		this.chapter = chapter;
		this.chapterTitledPaneController = chapterTitledPaneController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkForExistingDescription();
	}
	
	public void checkForExistingDescription() {
		if(chapter.getDescriptionName() != null && chapter.getDescriptionName().length() > 0) {
			descriptionTextArea.setText(chapter.getDescriptionName());
		}
	}
	
	@FXML
	public void saveButtonAction() {
		String description = descriptionTextArea.getText();
		chapter.setDescriptionName(description);
		chapterTitledPaneController.closeDescriptionStage();
	}

}
