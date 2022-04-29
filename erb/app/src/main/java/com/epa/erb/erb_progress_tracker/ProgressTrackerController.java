package com.epa.erb.erb_progress_tracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Chapter;
import com.epa.erb.engagement_action.EngagementActionController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ProgressTrackerController implements Initializable {

	@FXML
	HBox progressContentHBox;
	
	private ArrayList<Chapter> listOfAllChapters;
	private EngagementActionController engagementActionController;
	public ProgressTrackerController(ArrayList<Chapter> listOfAllChapters, EngagementActionController engagementActionController) {
		this.listOfAllChapters = listOfAllChapters;
		this.engagementActionController = engagementActionController;
	}
	
	private Logger logger = LogManager.getLogger(ProgressTrackerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addProgressColumns();
	}
	
	private void addProgressColumns() {
		for(Chapter chapter : listOfAllChapters) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/erb_progress_tracker/ProgressColumn.fxml"));
				ProgressColumnController progressColumnController = new ProgressColumnController(chapter, listOfAllChapters, engagementActionController);
				loader.setController(progressColumnController);
				Parent root = loader.load();
				HBox.setHgrow(root, Priority.ALWAYS);
				progressContentHBox.getChildren().add(root);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	public ArrayList<Chapter> getListOfAllChapters() {
		return listOfAllChapters;
	}

	public void setListOfAllChapters(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}
	
}
