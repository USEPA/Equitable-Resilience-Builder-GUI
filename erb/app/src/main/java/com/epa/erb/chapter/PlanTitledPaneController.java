package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

public class PlanTitledPaneController implements Initializable{

	@FXML
	TitledPane titledPane;
	@FXML
	TextArea audienceTextArea;
	@FXML
	TextArea goalsTextArea;
	@FXML
	TextArea activitiesTextArea;
	@FXML
	TextArea notesTextArea;
	
	private Activity activity;
	private Chapter chapter;
	public PlanTitledPaneController(Activity activity, Chapter chapter) {
		this.activity = activity;
		this.chapter = chapter;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadExistingContent();
	}
	
	private void loadExistingContent() {
		audienceTextArea.setText(chapter.getPlanHashMap().get("pAudience"));
		goalsTextArea.setText(chapter.getPlanHashMap().get("pGoals"));
		activitiesTextArea.setText(chapter.getPlanHashMap().get("pActivities"));
		notesTextArea.setText(chapter.getPlanHashMap().get("pNotes"));
	}
	
	private void handleControls() {
		setTitledPaneText();
		audienceTextArea.textProperty().addListener((v, oldValue, newValue) -> audienceTextAreaTextChanged(newValue));
		goalsTextArea.textProperty().addListener((v, oldValue, newValue) -> goalsTextAreaTextChanged(newValue));
		activitiesTextArea.textProperty().addListener((v, oldValue, newValue) -> activitiesTextAreaTextChanged(newValue));
		notesTextArea.textProperty().addListener((v, oldValue, newValue) -> notesTextAreaTextChanged(newValue));
	}
	
	private void audienceTextAreaTextChanged(String newValue) {
		String origValue = chapter.getPlanHashMap().get("pAudience");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getPlanHashMap().put("pAudience", newValue);
			chapter.setSaved(false);
		}
	}

	private void goalsTextAreaTextChanged(String newValue) {
		String origValue = chapter.getPlanHashMap().get("pGoals");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getPlanHashMap().put("pGoals", newValue);
			chapter.setSaved(false);
		}
	}
	
	private void activitiesTextAreaTextChanged(String newValue) {
		String origValue = chapter.getPlanHashMap().get("pActivities");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getPlanHashMap().put("pActivities", newValue);
			chapter.setSaved(false);
		}
	}
	
	private void notesTextAreaTextChanged(String newValue) {
		String origValue = chapter.getPlanHashMap().get("pNotes");
		if (origValue == null || (origValue != null && !origValue.trim().contentEquals(newValue))) {
			chapter.getPlanHashMap().put("pNotes", newValue);
			chapter.setSaved(false);
		}
	}
	
	public void expand() {
		titledPane.setExpanded(true);
	}
	
	private void setTitledPaneText() {
		if(activity != null) {
			titledPane.setText("Plan: " + activity.getLongName());
		} else {
			titledPane.setText("Plan: " + chapter.getStringName());
		}
	}
		
}
