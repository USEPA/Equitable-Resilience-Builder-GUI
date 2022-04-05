package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.noteboard.NoteBoardContentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SampleContentController implements Initializable{

	@FXML
	ImageView exampleContentImageView;
	@FXML
	HBox activityContentHBox;
		
	public SampleContentController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadNoteBoard();
	}
	
	public void loadNoteBoard() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController();
			fxmlLoader.setController(noteBoardContentController);
			Parent root = fxmlLoader.load();
			activityContentHBox.getChildren().add(1, root);
			HBox.setHgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
