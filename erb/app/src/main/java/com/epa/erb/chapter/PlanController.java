package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.Chapter;
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
	Circle orderActivitiesCircle;
	@FXML
	Circle gatherSuppliesCircle;
	@FXML
	Circle thirdOptionCircle;
	
	Chapter chapter;
	public PlanController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setTitleLabel();
	}
	
	public void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		titleHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		orderActivitiesCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		gatherSuppliesCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		thirdOptionCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
	}
	
	public void setTitleLabel() {
		titleLabel.setText("Planning for " + chapter.getStringName());
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

}
