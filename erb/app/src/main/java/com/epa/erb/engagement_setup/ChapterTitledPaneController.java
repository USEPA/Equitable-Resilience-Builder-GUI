package com.epa.erb.engagement_setup;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class ChapterTitledPaneController implements Initializable{

	@FXML
	TitledPane titledPane;
	@FXML
	ListView<SelectedActivity> titledPaneListView;
	
	private Chapter chapter;
	public ChapterTitledPaneController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Logger logger = LogManager.getLogger(ChapterTitledPaneController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titledPane.setExpanded(true);
		setTitledPaneText(chapter.getStringName());
		titledPane.setContextMenu(createPaneContextMenu());
	}
	
	private ContextMenu createPaneContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem("Add Description");
		contextMenu.getItems().add(menuItem);
		contextMenu.setOnAction(e-> addChapterDescription());
		return contextMenu;
	}
	
	private Stage descriptionStage;
	private void addChapterDescription() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_setup/ChapterDescription.fxml"));
			ChapterDescriptionController chapterDescriptionController = new ChapterDescriptionController(chapter, this);
			fxmlLoader.setController(chapterDescriptionController);
			Parent root = fxmlLoader.load();
			descriptionStage = new Stage();
			Scene scene = new Scene(root);
			descriptionStage.setScene(scene);
			descriptionStage.setTitle(chapter.getStringName() + " Description");
			descriptionStage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	void closeDescriptionStage() {
		descriptionStage.close();
	}
	
	void setTitledPaneText(String text) {
		titledPane.setText(text);
	}
			
	String getTitledPaneText() {
		return titledPane.getText();
	}

	public TitledPane getTitledPane() {
		return titledPane;
	}

	public ListView<SelectedActivity> getTitledPaneListView() {
		return titledPaneListView;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Stage getDescriptionStage() {
		return descriptionStage;
	}

	public void setDescriptionStage(Stage descriptionStage) {
		this.descriptionStage = descriptionStage;
	}

}
