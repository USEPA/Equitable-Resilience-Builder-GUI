package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class PlanFacilitatorModeController implements Initializable{

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
	public PlanFacilitatorModeController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadExistingContent();
	}
	
	private void loadExistingContent() {
		audienceTextArea.setText(chapter.getFacilitatorPlanHashMap().get("pAudience"));
		goalsTextArea.setText(chapter.getFacilitatorPlanHashMap().get("pGoals"));
		activitiesTextArea.setText(chapter.getFacilitatorPlanHashMap().get("pActivities"));
		notesTextArea.setText(chapter.getFacilitatorPlanHashMap().get("pNotes"));
	}
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		audienceTextArea.textProperty().addListener((v, oldValue, newValue) -> audienceTextAreaTextChanged(newValue));
		goalsTextArea.textProperty().addListener((v, oldValue, newValue) -> goalsTextAreaTextChanged(newValue));
		activitiesTextArea.textProperty().addListener((v, oldValue, newValue) -> activitiesTextAreaTextChanged(newValue));
		notesTextArea.textProperty().addListener((v, oldValue, newValue) -> notesTextAreaTextChanged(newValue));
	}
	
	private void audienceTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorPlanHashMap().get("pAudience");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorPlanHashMap().put("pAudience", newValue);
			chapter.setSaved(false);
		}
	}

	private void goalsTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorPlanHashMap().get("pGoals");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorPlanHashMap().put("pGoals", newValue);
			chapter.setSaved(false);
		}
	}
	
	private void activitiesTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorPlanHashMap().get("pActivities");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorPlanHashMap().put("pActivities", newValue);
			chapter.setSaved(false);
		}
	}
	
	private void notesTextAreaTextChanged(String newValue) {
		String origValue = chapter.getFacilitatorPlanHashMap().get("pNotes");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getFacilitatorPlanHashMap().put("pNotes", newValue);
			chapter.setSaved(false);
		}
	}
	
	public HBox getErbHeading() {
		return erbHeading;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

}
