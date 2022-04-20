package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AttributePanelController implements Initializable{

	@FXML
	VBox attributePanelVBox;
	@FXML
	HBox attributeLabelHBox;
	@FXML
	Label attributeLabel;
	@FXML
	TextFlow attributeTextFlow;
	@FXML
	Button closeButton;
	
	private EngagementActionController engagementActionController;
	public AttributePanelController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void closeButtonAction() {
		VBox attributePanel = (VBox) engagementActionController.getAttributeScrollPane().getContent();
		attributePanel.getChildren().remove(attributePanelVBox);
		engagementActionController.removeAttributePanelController(this);
	}
	
	void setAttributeFields(String attributeTextFlowString, String attributeLabelString, String attributeLabelColorString) {
		setAttributeTextFlow(attributeTextFlowString);
		setAttributeLabel(attributeLabelString);
		setAttributeLabelColor(attributeLabelColorString);
		setAttributeCloseButtonColor(attributeLabelColorString);
	}

	private void setAttributeTextFlow(String attributeTextFlowString) {
		Text text = new Text(attributeTextFlowString);
		attributeTextFlow.getChildren().add(text);
	}
	
	private void setAttributeLabel(String attributeLabelString) {
		attributeLabel.setText(attributeLabelString);
	}
	
	private void setAttributeLabelColor(String attributeLabelColorString) {
		attributeLabelHBox.setStyle("-fx-background-color: " + attributeLabelColorString + ";");
	}
	
	private void setAttributeCloseButtonColor(String attributeLabelColorString) {
		closeButton.setStyle("-fx-background-color: " + attributeLabelColorString + ";");
	}
	
	String getAttributeLabelText() {
		return attributeLabel.getText();
	}

}
