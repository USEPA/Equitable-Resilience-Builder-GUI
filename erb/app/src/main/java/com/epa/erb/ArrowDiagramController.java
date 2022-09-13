package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;

import org.docx4j.model.properties.run.FontColor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public class ArrowDiagramController implements Initializable{

	@FXML
	Button button1;
	@FXML
	Button button2;
	@FXML
	Button button3;
	@FXML
	Button button4;
	@FXML
	Button button5;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		installToolTips();
	}
	
	private void installToolTips() {
		installToolTipForButton1();
		installToolTipForButton2();
		installToolTipForButton3();
		installToolTipForButton4();
		installToolTipForButton5();
	}
	
	private void installToolTipForButton1() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Project Planning");
		tooltip.setFont(new Font(13.0));
		tooltip.setStyle("-fx-background-color: #FAAC2C; -fx-text-fill: black;");
		Tooltip.install(button1, tooltip);
	}
	
	private void installToolTipForButton2() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Built relationships with new groups and organizations in your community");
		tooltip.setFont(new Font(13.0));
		tooltip.setStyle("-fx-background-color: #A6F444; -fx-text-fill: black;");
		Tooltip.install(button2, tooltip);
	}
	
	private void installToolTipForButton3() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Identified who is most vulnerable to different hazards, why and how");
		tooltip.setFont(new Font(13.0));
		tooltip.setStyle("-fx-background-color: #46E062; -fx-text-fill: black;");
		Tooltip.install(button3, tooltip);
	}
	
	private void installToolTipForButton4() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Assessed you community's resilience to disasters and climate change");
		tooltip.setFont(new Font(13.0));
		tooltip.setStyle("-fx-background-color: #46E0C9; -fx-text-fill: black;");
		Tooltip.install(button4, tooltip);
	}
	
	private void installToolTipForButton5() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Identified actions to build community resilience in an equitable way");
		tooltip.setFont(new Font(13.0));
		tooltip.setStyle("-fx-background-color: #468AE0; -fx-text-fill: black;");
		Tooltip.install(button5, tooltip);
	}
	
	

}
