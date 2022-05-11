package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ReflectNotesController implements Initializable{

	@FXML
	TextArea notesTextArea;
	
	private Activity activity;
	private ReflectController reflectController;
	public ReflectNotesController(Activity activity, ReflectController reflectController) {
		this.activity = activity;
		this.reflectController = reflectController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkForExistingNotes();
	}
	
	private void checkForExistingNotes() {
		if(activity.getNotes() != null && activity.getNotes().length() > 0) {
			notesTextArea.setText(activity.getNotes());
		}
	}
	
	@FXML
	public void saveNotesAction() {
		activity.setNotes(notesTextArea.getText());
		reflectController.closeReflectNotesStage();
	}

}
