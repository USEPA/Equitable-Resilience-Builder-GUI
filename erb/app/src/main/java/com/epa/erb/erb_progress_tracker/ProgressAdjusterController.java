package com.epa.erb.erb_progress_tracker;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class ProgressAdjusterController implements Initializable{

	@FXML
	Slider slider;
	@FXML
	Label headingLabel;
	@FXML
	Label progressPercentLabel;

	private String headerString;
	private ProgressColumnController progressColumnController;
	public ProgressAdjusterController(String headerString, ProgressColumnController progressColumnController) {
		this.headerString = headerString;
		this.progressColumnController = progressColumnController;
	}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setExistingSliderValue();
		setHeadingLabelText(headerString);
	}

	private void handleControls() {
		slider.valueProperty().addListener(e -> sliderValueChanged(e));
	}
	
	private void setExistingSliderValue() {
		if (headerString.contentEquals("Plan")) {
			setSliderValue(progressColumnController.getPlanProgress()*100);
		} else if (headerString.contentEquals("Reflect")) {
			setSliderValue(progressColumnController.getReflectProgress() * 100.0);
		}
	}
	
	private void sliderValueChanged(Observable event) {
		int percent = (int) slider.getValue();
		progressPercentLabel.setText("%" + String.valueOf(percent));
	}
	
	@FXML
	public void okButtonAction() {
		if(headerString.contentEquals("Plan")) {
			progressColumnController.handlePlanProgressChange(slider.getValue()/100.0);
		} else if (headerString.contentEquals("Reflect")) {
			progressColumnController.handleReflectProgressChange(slider.getValue()/100.0);
		}
		progressColumnController.closeProgressAdjusterStage();
	}
	
	private void setSliderValue(double percent) {
		slider.setValue(percent);
	}
	
	private void setHeadingLabelText(String text) {
		headingLabel.setText(text);
	}

	public Slider getSlider() {
		return slider;
	}

	public Label getHeadingLabel() {
		return headingLabel;
	}

	public Label getProgressPercentLabel() {
		return progressPercentLabel;
	}

	public String getHeaderString() {
		return headerString;
	}

	public void setHeaderString(String headerString) {
		this.headerString = headerString;
	}

	public ProgressColumnController getProgressColumnController() {
		return progressColumnController;
	}

	public void setProgressColumnController(ProgressColumnController progressColumnController) {
		this.progressColumnController = progressColumnController;
	}
	
}
