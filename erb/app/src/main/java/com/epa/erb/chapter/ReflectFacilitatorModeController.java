package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class ReflectFacilitatorModeController implements Initializable {

	@FXML
	HBox erbHeading;
	@FXML
	TextArea audienceTextArea;
	@FXML
	TextArea goalsTextArea;
	@FXML
	TextArea activitiesTextArea;
	@FXML
	TextArea notesTextArea;

	private Chapter chapter;
	public ReflectFacilitatorModeController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadExistingContent();
	}
	
	private void loadExistingContent() {
		audienceTextArea.setText(chapter.getFacilitatorReflectHashMap().get("rAudience"));
		goalsTextArea.setText(chapter.getFacilitatorReflectHashMap().get("rGoals"));
		activitiesTextArea.setText(chapter.getFacilitatorReflectHashMap().get("rActivities"));
		notesTextArea.setText(chapter.getFacilitatorReflectHashMap().get("rNotes"));
	}
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		audienceTextArea.textProperty().addListener((v, oldValue, newValue) -> audienceTextAreaTextChanged(newValue));
		goalsTextArea.textProperty().addListener((v, oldValue, newValue) -> goalsTextAreaTextChanged(newValue));
		activitiesTextArea.textProperty().addListener((v, oldValue, newValue) -> activitiesTextAreaTextChanged(newValue));
		notesTextArea.textProperty().addListener((v, oldValue, newValue) -> notesTextAreaTextChanged(newValue));
	}
	
	private void audienceTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorReflectHashMap().get("rAudience");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorReflectHashMap().put("rAudience", newValue);
			chapter.setSaved(false);
		}
	}

	private void goalsTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorReflectHashMap().get("rGoals");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorReflectHashMap().put("rGoals", newValue);
			chapter.setSaved(false);
		}
	}
	
	private void activitiesTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorReflectHashMap().get("rActivities");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorReflectHashMap().put("rActivities", newValue);
			chapter.setSaved(false);
		}
	}
	
	private void notesTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorReflectHashMap().get("rNotes");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorReflectHashMap().put("rNotes", newValue);
			chapter.setSaved(false);
		}
	}

	public HBox getErbHeading() {
		return erbHeading;
	}

}
