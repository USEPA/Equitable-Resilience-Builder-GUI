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
	}
	
	private void handleControls() {
		setTitledPaneText();
	}
	
	private void setTitledPaneText() {
		if(activity != null) {
			titledPane.setText("Plan: " + activity.getLongName());
		} else {
			titledPane.setText("Plan: " + chapter.getStringName());
		}
	}
		
}
