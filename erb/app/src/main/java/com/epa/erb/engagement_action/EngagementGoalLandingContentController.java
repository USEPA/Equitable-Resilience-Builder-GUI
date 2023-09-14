package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class EngagementGoalLandingContentController implements Initializable {

	@FXML
	VBox vBox;
	
	
	private EngagementActionController engagementActionController;
	public EngagementGoalLandingContentController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}

	private void handleControls() {
		ImageView imageView = new ImageView();
		vBox.getChildren().add(imageView);
//		imageView.fitWidthProperty().bind(vBox.widthProperty().subtract(50));
//		imageView.fitHeightProperty().bind(vBox.heightProperty().subtract(50));
		imageView.setImage(new Image(getClass().getResourceAsStream("/projectPage.png")));

	}

}
