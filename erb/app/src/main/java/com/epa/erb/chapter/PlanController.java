package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class PlanController implements Initializable{

	@FXML
	HBox erbHeading;
	@FXML
	HBox titleHeading;
	@FXML
	Label titleLabel;
	@FXML
	Circle thirdOptionCircle;
	@FXML
	Circle gatherSuppliesCircle;
	@FXML
	Circle orderActivitiesCircle;
	
	private Chapter chapter;
	public PlanController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setTitleLabelText("Planning for " + chapter.getStringName());
	}
	
	private void handleControls() {
		thirdOptionCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		gatherSuppliesCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		orderActivitiesCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		titleHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	private void setTitleLabelText(String text) {
		titleLabel.setText(text);
	}
	
	@FXML
	public void orderActivitiesCircleClicked() {
		
	}
	
	@FXML
	public void gatherSuppliesCircleClicked() {
		
	}
	
	@FXML
	public void thirdOptionCircleClicked() {
		
	}

	public HBox getErbHeading() {
		return erbHeading;
	}

	public HBox getTitleHeading() {
		return titleHeading;
	}

	public Label getTitleLabel() {
		return titleLabel;
	}

	public Circle getThirdOptionCircle() {
		return thirdOptionCircle;
	}

	public Circle getGatherSuppliesCircle() {
		return gatherSuppliesCircle;
	}

	public Circle getOrderActivitiesCircle() {
		return orderActivitiesCircle;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

}
