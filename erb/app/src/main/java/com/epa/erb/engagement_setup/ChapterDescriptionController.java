package com.epa.erb.engagement_setup;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ChapterDescriptionController implements Initializable{

	@FXML
	TextArea descriptionTextArea;

	private Chapter chapter;
	private ChapterTitledPaneController chapterTitledPaneController;
	public ChapterDescriptionController(Chapter chapter, ChapterTitledPaneController chapterTitledPaneController) {
		this.chapter = chapter;
		this.chapterTitledPaneController = chapterTitledPaneController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkForExistingDescription();
	}
	
	private void checkForExistingDescription() {
		if(chapter.getDescriptionName() != null && chapter.getDescriptionName().length() > 0) {
			setDescriptionTextAreaText(chapter.getDescriptionName());
		}
	}
	
	@FXML
	public void saveButtonAction() {
		String description = descriptionTextArea.getText();
		chapter.setDescriptionName(description);
		chapterTitledPaneController.closeDescriptionStage();
	}
	
	private void setDescriptionTextAreaText(String text) {
		descriptionTextArea.setText(text);
	}

	public TextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}
	
	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public ChapterTitledPaneController getChapterTitledPaneController() {
		return chapterTitledPaneController;
	}

	public void setChapterTitledPaneController(ChapterTitledPaneController chapterTitledPaneController) {
		this.chapterTitledPaneController = chapterTitledPaneController;
	}
	
}
