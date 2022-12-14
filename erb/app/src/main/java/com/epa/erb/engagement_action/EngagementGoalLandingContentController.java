package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.chapter.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EngagementGoalLandingContentController implements Initializable {

	@FXML
	VBox mainPanel;
	@FXML
	ImageView goalLandingImageView;

	private ArrayList<Chapter> listOfChaptersInGoal;
	private EngagementActionController engagementActionController;
	public EngagementGoalLandingContentController(ArrayList<Chapter> listOfChaptersInGoal, EngagementActionController engagementActionController) {
		this.listOfChaptersInGoal = listOfChaptersInGoal;
		this.engagementActionController = engagementActionController;
	}

	private Logger logger = LogManager.getLogger(EngagementGoalLandingContentController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setAboutTextAreaText(getAboutText());
	}

	private void handleControls() {
		handleControlsShown();
		goalLandingImageView.setImage(new Image(getClass().getResourceAsStream("/landing_image1.PNG")));
	}

	private void handleControlsShown() {

	}

	String getHeadingLabelText() {
		return "Equitable Resilience Builder Activities";
	}

	public void setAboutTextAreaText(Text text) {
		String string = text.getText();
		string = string.replaceAll("\r", "\n");
	}

	Text getAboutText() {
		return new Text("The Equitable Resilience Builder (ERB) is an application that assists communities with resilience planning. ERB engages communities in a guided process to inclusively assess their vulnerability and resilience to disasters and climate change, then use the results to prioritize actions to build resilience in an equitable way.");
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

	public ArrayList<Chapter> getListOfAllChapters() {
		return listOfChaptersInGoal;
	}

	public void setListOfAllChapters(ArrayList<Chapter> listOfAllChapters) {
		this.listOfChaptersInGoal = listOfAllChapters;
	}

}
