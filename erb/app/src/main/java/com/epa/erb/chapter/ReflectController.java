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

public class ReflectController implements Initializable{

	@FXML
	HBox erbHeading;
	@FXML
	HBox titleHeading;
	@FXML
	Label titleLabel;
	@FXML
	Circle notesCircle;
	@FXML
	Circle reviewDataCircle;
	@FXML
	Circle updateProgressCircle;
	
	private Chapter chapter;
	public ReflectController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setTitleLabel();
	}
	
	private void handleControls() {
		erbHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		titleHeading.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		notesCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		reviewDataCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		updateProgressCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
	}
	
	private void setTitleLabel() {
		titleLabel.setText("Reflecting on " + chapter.getStringName());
	}
	
	@FXML
	public void notesCircleClicked() {
		
	}
	
	@FXML
	public void reviewDataCircleClicked() {
		
	}
	
	@FXML
	public void updateProgressCircleClicked() {
		
	}

}
