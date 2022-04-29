package com.epa.erb.erb_progress_tracker;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class ProgressAdjusterController implements Initializable{

	@FXML
	Label headingLabel;
	@FXML
	Label progressPercentLabel;
	@FXML
	Slider slider;
	@FXML
	Button okButton;

	private String headerString;
	private ProgressColumnController progressColumnController;
	public ProgressAdjusterController(String headerString, ProgressColumnController progressColumnController) {
		this.headerString = headerString;
		this.progressColumnController = progressColumnController;
	}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}

	private void handleControls() {
		slider.valueProperty().addListener(e -> sliderValueChanged(e));
		if (headerString.contentEquals("Plan")) {
			slider.setValue(progressColumnController.getPlanProgress() * 100.0);
		} else if (headerString.contentEquals("Reflect")) {
			slider.setValue(progressColumnController.getReflectProgress() * 100.0);
		}
		headingLabel.setText(headerString);
	}
	
	private void sliderValueChanged(Observable event) {
		int percent = (int) slider.getValue();
		progressPercentLabel.setText("%" + String.valueOf(percent));
	}
	
	@FXML
	public void okButtonAction() {
		if(headerString.contentEquals("Plan")) {
			progressColumnController.handlePlanProgress(slider.getValue()/100.0);
			progressColumnController.closeProgressPlanAdjusterStage();
		} else if (headerString.contentEquals("Reflect")) {
			progressColumnController.handleReflectProgress(slider.getValue()/100.0);
			progressColumnController.closeProgressReflectAdjusterStage();
		}

	}

}
