package com.epa.erb.activity_progress_tracker;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.chapter.Chapter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProgressColumnController implements Initializable{

	@FXML
	Label chapterLabel;
	@FXML
	VBox progressColumn;
	
	private Chapter chapter;
	public ProgressColumnController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Logger logger = LogManager.getLogger(ProgressColumnController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillProgressColumn();
		setChapterLabelText(chapter.getStringName());
	}
	
	private void fillProgressColumn() {
		for(Activity activity : chapter.getAssignedActivities()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/activity_progress_tracker/ProgressActivity.fxml"));
				ProgressActivityController progressActivityController = new ProgressActivityController(activity);
				fxmlLoader.setController(progressActivityController);
				Parent root = fxmlLoader.load();
				progressColumn.getChildren().add(root);
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	private void setChapterLabelText(String text) {
		chapterLabel.setText(text);
	}

	public Label getChapterLabel() {
		return chapterLabel;
	}

	public VBox getProgressColumn() {
		return progressColumn;
	}	
	
	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}
	
}
