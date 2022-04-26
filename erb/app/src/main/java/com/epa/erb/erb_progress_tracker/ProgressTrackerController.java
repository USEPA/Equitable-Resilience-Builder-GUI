package com.epa.erb.erb_progress_tracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class ProgressTrackerController implements Initializable {

	@FXML
	HBox progressContentHBox;
	
	private ArrayList<Chapter> listOfAllChapters;
	public ProgressTrackerController(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}
	
	private Logger logger = LogManager.getLogger(ProgressTrackerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addProgressColumns();
	}
	
	public void addProgressColumns() {
		for(Chapter chapter : listOfAllChapters) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/erb_progress_tracker/ProgressColumn.fxml"));
				ProgressColumnController progressColumnController = new ProgressColumnController(chapter, listOfAllChapters);
				loader.setController(progressColumnController);
				Parent root = loader.load();
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
