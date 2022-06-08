package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.engagement_action.EngagementActionController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ReflectNotesController implements Initializable{

	@FXML
	Label titleLabel;
	@FXML
	TextArea notesTextArea;
	
	private App app;
	private Activity activity;
	private ReflectController reflectController;
	private EngagementActionController engagementActionController;
	public ReflectNotesController(App app, Activity activity, ReflectController reflectController, EngagementActionController engagementActionController) {
		this.app = app;
		this.activity = activity;
		this.reflectController = reflectController;
		this.engagementActionController = engagementActionController;
	}
	
	private Chapter chapter;
	public ReflectNotesController(Chapter chapter, ReflectController reflectController, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.reflectController = reflectController;
		this.engagementActionController = engagementActionController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 if(activity !=null) {
			 checkForExistingActivityNotes();
			 titleLabel.setText(activity.getLongName());
		 }
		 if(chapter !=null) {
			 checkForExistingChapterNotes();
			 titleLabel.setText(chapter.getStringName());
		 }
	}
	
	private void checkForExistingActivityNotes() {
		if(activity.getNotes() != null && activity.getNotes().length() > 0) {
			notesTextArea.setText(activity.getNotes());
		}
	}
	
	private void checkForExistingChapterNotes() {
		if(chapter.getNotes() != null && chapter.getNotes().length() > 0) {
			notesTextArea.setText(chapter.getNotes());
		}
	}
	
	@FXML
	public void saveNotesAction() {
		if(activity != null) activity.setNotes(notesTextArea.getText());
		if(chapter != null) chapter.setNotes(notesTextArea.getText());
		reflectController.closeReflectNotesStage();
		app.setNeedsSaving(true);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ReflectController getReflectController() {
		return reflectController;
	}

	public void setReflectController(ReflectController reflectController) {
		this.reflectController = reflectController;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Label getTitleLabel() {
		return titleLabel;
	}

	public TextArea getNotesTextArea() {
		return notesTextArea;
	}

}
