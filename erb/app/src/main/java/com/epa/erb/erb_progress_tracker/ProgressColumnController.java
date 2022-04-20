package com.epa.erb.erb_progress_tracker;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

public class ProgressColumnController implements Initializable{

	@FXML
	Label chapterLabel;
	@FXML
	Circle planCircle;
	@FXML
	Arc planArc;
	@FXML
	Circle engageCircle;
	@FXML
	Arc engageArc;
	@FXML
	Circle reflectCircle;
	@FXML
	Arc reflectArc;
	
	private Chapter chapter;
	public ProgressColumnController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		handlePlanArc();
		handleEngageArc();
		handleReflectArc();
	}
	
	private void handleControls() {
		chapterLabel.setText(chapter.getStringName());
	}
	
	private void handlePlanArc() {
		setArc75(planArc, planCircle);
	}
	
	private void handleEngageArc() {
		setArc50(engageArc, engageCircle);
	}
	
	private void handleReflectArc() {
		setArc0(reflectArc, reflectCircle);
	}
	
	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}
	
	@FXML
	public void engageClicked() {
		
	}
	
	public void setArc0(Arc arc, Circle circle) {
		//Circle
		circle.setStyle("-fx-fill: White;");
		
		//Arc
		arc.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		arc.setLength(0.0);
		arc.setStartAngle(90.0);
		StackPane.setAlignment(arc, Pos.CENTER_LEFT);
	}
	
	public void setArc25(Arc arc, Circle circle) {
		//Circle
		circle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		
		//Arc
		arc.setStyle("-fx-fill: White;");
		arc.setLength(270.0);
		arc.setStartAngle(90.0);
		StackPane.setAlignment(arc, Pos.CENTER);
	}
	
	public void setArc50(Arc arc, Circle circle) {
		//Circle
		circle.setStyle("-fx-fill: White;");
		
		//Arc
		arc.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		arc.setLength(180.0);
		arc.setStartAngle(90.0);
		StackPane.setAlignment(arc, Pos.CENTER_LEFT);
	}
	
	public void setArc75(Arc arc, Circle circle) {
		//Circle
		circle.setStyle("-fx-fill: White;");
		
		//Arc
		arc.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		arc.setLength(270.0);
		arc.setStartAngle(90.0);
		StackPane.setAlignment(arc, Pos.CENTER);
	}
	
	public void setArc100(Arc arc, Circle circle) {
		//Circle
		circle.setStyle("-fx-fill: White;");
		
		//Arc
		arc.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		arc.setLength(360.0);
		arc.setStartAngle(90.0);
		StackPane.setAlignment(arc, Pos.CENTER);
	}

}
