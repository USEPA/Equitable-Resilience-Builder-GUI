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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChapterTitledPaneController implements Initializable{

	@FXML
	TitledPane titledPane;
	@FXML
	VBox titledPaneVBox;
	@FXML
	ListView<SelectedActivity> titledPaneListView;
	
	Chapter chapter;
	public ChapterTitledPaneController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Logger logger = LogManager.getLogger(ChapterTitledPaneController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titledPane.setExpanded(true);
		titledPane.setText(chapter.getStringName());
		titledPane.setContextMenu(createPaneContextMenu());
	}
	
	ContextMenu createPaneContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem("Add Description");
		contextMenu.getItems().add(menuItem);
		contextMenu.setOnAction(e-> addChapterDescription());
		return contextMenu;
	}
	
	private Stage descriptionStage;
	void addChapterDescription() {
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
			
	public String getPaneTitle() {
		return titledPane.getText();
	}

	public VBox getTitledPaneVBox() {
		return titledPaneVBox;
	}

	public ListView<SelectedActivity> getTitledPaneListView() {
		return titledPaneListView;
	}		
}
