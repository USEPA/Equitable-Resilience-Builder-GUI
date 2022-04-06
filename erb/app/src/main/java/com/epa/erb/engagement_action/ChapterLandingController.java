package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.epa.erb.Activity;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
	
	Chapter chapter;
	
	public ChapterLandingController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	ArrayList<Chapter> listOfAllChapters;
	
	public ChapterLandingController(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		headingLabelHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	public void setHeadingLabel() {
		if(chapter != null) {
			headingLabel.setText("Welcome to " + chapter.getStringName());
		} else {
			headingLabel.setText("Welcome to the ERB");
		}
	}
	
	public void setAboutText() {
		if (chapter != null) {
			Text text = new Text(chapter.getDescriptionName());
			text.setFont(Font.font(15));
			aboutTextFlow.getChildren().add(text);
		} else {
			Text text = new Text("The Equitable Resilience Builder (ERB) is an application that assists communities with resilience planning. ERB engages communities in a guided process to inclusively assess their vulnerability and resilience to disasters and climate change, then use the results to prioritize actions to build resilience in an equitable way.");
			text.setFont(Font.font(15));
			aboutTextFlow.getChildren().add(text);
		}
	}

	public void setActivitiesText() {
		if (chapter != null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Activity activity : chapter.getUserSelectedActivities()) {
				stringBuilder.append(activity.getLongName() + "\n");
			}
			Text text = new Text(stringBuilder.toString());
			text.setFont(Font.font(15));
			activitiesTextFlow.getChildren().add(text);
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			for(Chapter chapter : listOfAllChapters) {
				for (Activity activity : chapter.getUserSelectedActivities()) {
					stringBuilder.append(activity.getLongName() + "\n");
				}
			}
			Text text = new Text(stringBuilder.toString());
			text.setFont(Font.font(15));
			activitiesTextFlow.getChildren().add(text);
		}
	}

}
