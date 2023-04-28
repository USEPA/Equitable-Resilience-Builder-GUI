package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class EngagementGoalLandingContentController implements Initializable {

	@FXML
	TextFlow textFlow;
	@FXML
	VBox mainPanel;
	@FXML
	ImageView goalLandingImageView;

	private EngagementActionController engagementActionController;
	public EngagementGoalLandingContentController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setAboutTextAreaText(getAboutText());
	}

	private void handleControls() {
		handleControlsShown();
		goalLandingImageView.setImage(new Image(getClass().getResourceAsStream("/bridge_500_500.png")));
	}

	private void handleControlsShown() {

	}

	String getHeadingLabelText() {
		return "Equitable Resilience Builder Activities";
	}

	public void setAboutTextAreaText(Text text) {
		textFlow.getChildren().add(text);
		
	}

	Text getAboutText() {
		Text text = new Text();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("There are 5 sections to this toolkit:");
		stringBuilder.append("\n\t");
		stringBuilder.append("1. Plan your project");
		stringBuilder.append("\n\t");
		stringBuilder.append("2. Engage your community");
		stringBuilder.append("\n\t");
		stringBuilder.append("3. Assess hazards, equity and resilience");
		stringBuilder.append("\n\t");
		stringBuilder.append("4. Strategize actions");
		stringBuilder.append("\n\t");
		stringBuilder.append("5. Wrap up and move forward");
		stringBuilder.append("\n");
		stringBuilder.append("You can navigate the pages in the sections using the tree on the left. Each section contains a series of activities that make up the ERB toolkit. There is one page per activity. Each page contains an overview of the activity, with more information provided in documents attached on upper right side of the page, under the header \"What you'll need.\" On the lower right side of the page, you'll see the header \"Centering equity,\" which contains icons and tips for how to carry out the activities in an inclusive way");

		text.setFont(new Font(20.0));

		text.setText(stringBuilder.toString());
		return text;
	}
	
	public VBox getMainPanel() {
		return mainPanel;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

}
