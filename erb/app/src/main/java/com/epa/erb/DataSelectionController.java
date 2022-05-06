package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.epa.erb.project.ProjectSelectionController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class DataSelectionController implements Initializable{

	@FXML
	ToggleGroup dataSelection;
	@FXML
	Label setupModifiedDateLabel;
	@FXML
	Label actionModifiedDateLabel;
	@FXML
	RadioButton setupDataRadioButton;
	@FXML
	RadioButton actionDataRadioButton;
	
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
		handleControls();
	}
	
	private void handleControls() {
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
				projectSelectionController.setDataFileToLoadInActionTool(setupFile);
			} else if (selectedRadioButton == actionDataRadioButton) {
				projectSelectionController.setDataFileToLoadInActionTool(actionFile);
			}
			projectSelectionController.closeDataSelectionStage();
		}
	}
	
	public Label getSetupModifiedDateLabel() {
		return setupModifiedDateLabel;
	}

	public Label getActionModifiedDateLabel() {
		return actionModifiedDateLabel;
	}
	
	public RadioButton getSetupDataRadioButton() {
		return setupDataRadioButton;
	}
	
	public RadioButton getActionDataRadioButton() {
		return actionDataRadioButton;
	}
	
	public RadioButton getSelectedDataRadioButton() {
		return (RadioButton) dataSelection.getSelectedToggle();
	}

	public ToggleGroup getDataSelection() {
		return dataSelection;
	}

	public File getSetupFile() {
		return setupFile;
	}

	public void setSetupFile(File setupFile) {
		this.setupFile = setupFile;
	}

	public File getActionFile() {
		return actionFile;
	}

	public void setActionFile(File actionFile) {
		this.actionFile = actionFile;
	}

	public ProjectSelectionController getProjectSelectionController() {
		return projectSelectionController;
	}

	public void setProjectSelectionController(ProjectSelectionController projectSelectionController) {
		this.projectSelectionController = projectSelectionController;
	}
	
}
