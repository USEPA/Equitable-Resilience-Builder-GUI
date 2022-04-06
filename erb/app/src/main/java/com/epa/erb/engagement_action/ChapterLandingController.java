package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChapterLandingController implements Initializable {

	@FXML
	Label headingLabel;
	@FXML
	HBox headingLabelHBox;
	@FXML
	TextFlow aboutTextFlow;
	@FXML
	TextFlow activitiesTextFlow;
	@FXML
	Button getStartedButton;
	
	Chapter chapter;
	EngagementActionController engagementActionController;
	public ChapterLandingController(Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	ArrayList<Chapter> listOfAllChapters;
	public ChapterLandingController(ArrayList<Chapter> listOfAllChapters, EngagementActionController engagementActionController) {
		this.listOfAllChapters = listOfAllChapters;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeStyle();
	}
	
	private void initializeStyle() {
		headingLabelHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		getStartedButton.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	void setHeadingLabel() {
		if(chapter != null) {
			headingLabel.setText("Welcome to " + chapter.getStringName());
		} else {
			headingLabel.setText("Welcome to the ERB");
		}
	}
	
	void setAboutText() {
		Text text = getAboutText();
		text.setFont(Font.font(15));
		aboutTextFlow.getChildren().add(text);
	}
	
	Text getAboutText() {
		if (chapter != null) {
			return new Text(chapter.getDescriptionName());
		} else {
			return new Text("The Equitable Resilience Builder (ERB) is an application that assists communities with resilience planning. ERB engages communities in a guided process to inclusively assess their vulnerability and resilience to disasters and climate change, then use the results to prioritize actions to build resilience in an equitable way.");
		}
	}

	void setActivitiesText() {
		Text text = getActivitiesText();
		text.setFont(Font.font(15));
		activitiesTextFlow.getChildren().add(text);
	}
	
	Text getActivitiesText() {
		StringBuilder stringBuilder = new StringBuilder();
		if (chapter != null) {
			for (Activity activity : chapter.getUserSelectedActivities()) {
				stringBuilder.append(activity.getLongName() + "\n");
			}
		} else {
			for (Chapter chapter : listOfAllChapters) {
				for (Activity activity : chapter.getUserSelectedActivities()) {
					stringBuilder.append(activity.getLongName() + "\n");
				}
			}
		}
		return new Text(stringBuilder.toString());
	}
	
	@FXML
	public void getStartedButtonAction() {
		String activityGUID = chapter.getUserSelectedActivities().get(0).getGUID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == activityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}

}
