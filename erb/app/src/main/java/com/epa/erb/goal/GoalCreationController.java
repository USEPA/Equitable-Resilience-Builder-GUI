package com.epa.erb.goal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GoalCreationController implements Initializable{

	@FXML
	VBox goalsVBox;
	@FXML
	TextField goalNameTextField;
	@FXML
	TextArea goalDescriptionTextArea;
	@FXML
	ListView<?> selectedGoalsListView;

	
	public GoalCreationController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void addButtonAction() {
		
	}
	
	@FXML
	public void doneButtonAction() {
		
	}

	public VBox getGoalsVBox() {
		return goalsVBox;
	}

	public TextField getGoalNameTextField() {
		return goalNameTextField;
	}

	public TextArea getGoalDescriptionTextArea() {
		return goalDescriptionTextArea;
	}

	public ListView<?> getSelectedGoalsListView() {
		return selectedGoalsListView;
	}

}
