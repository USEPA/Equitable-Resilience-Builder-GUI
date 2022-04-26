package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class DataSelectionController implements Initializable{

	@FXML
	Label setupModifiedDateLabel;
	@FXML
	RadioButton setupDataRadioButton;
	@FXML
	ToggleGroup dataSelection;
	@FXML
	Label actionModifiedDateLabel;
	@FXML
	RadioButton actionDataRadioButton;
	@FXML
	Button okButton;
	
	private File setupFile;
	private File actionFile;
	private ProjectSelectionController projectSelectionController;
	public DataSelectionController(File setupFile, File actionFile, ProjectSelectionController projectSelectionController) {
		this.setupFile = setupFile;
		this.actionFile = actionFile;
		this.projectSelectionController = projectSelectionController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Date setupDate = new Date(setupFile.lastModified());
		setupModifiedDateLabel.setText(setupDate.toString());
		Date actionDate = new Date(actionFile.lastModified());
		actionModifiedDateLabel.setText(actionDate.toString());
	}
	
	@FXML
	public void okButtonAction() {
		RadioButton selectedRadioButton = getSelectedDataRadioButton();
		if(selectedRadioButton != null) {
			if(selectedRadioButton == setupDataRadioButton) {
				projectSelectionController.setActionDataFile(setupFile);
			} else if (selectedRadioButton == actionDataRadioButton) {
				projectSelectionController.setActionDataFile(actionFile);
			}
			projectSelectionController.closeDataSelectionStage();
		}
	}
	
	public RadioButton getSelectedDataRadioButton() {
		return (RadioButton) dataSelection.getSelectedToggle();
	}

}
