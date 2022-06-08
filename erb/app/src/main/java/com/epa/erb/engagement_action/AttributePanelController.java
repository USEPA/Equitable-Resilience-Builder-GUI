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
	Button closeButton;
	@FXML
	Label attributeLabel;
	@FXML
	VBox attributePanelVBox;
	@FXML
	HBox attributeLabelHBox;
	@FXML
	TextFlow attributeTextFlow;

	private String attributeTitle;
	private String attributeContent;
	private String attributeColor;
	private EngagementActionController engagementActionController;
	public AttributePanelController(String attributeTitle, String attributeContent, String attributeColor, EngagementActionController engagementActionController) {
		this.attributeTitle = attributeTitle;
		this.attributeContent = attributeContent;
		this.attributeColor = attributeColor;
		this.engagementActionController = engagementActionController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setAttributeFields(attributeTitle, attributeContent, attributeColor);
	}
	
	@FXML
	public void closeButtonAction() {
		VBox attributePanel = (VBox) engagementActionController.getAttributePanelScrollPane().getContent();
		attributePanel.getChildren().remove(attributePanelVBox);
		engagementActionController.removeAttributePanelController(this);
	}
	
	void setAttributeFields(String attributeTitle, String attributeContent, String attributeLabelColor) {
		setAttributeTextFlowText(attributeContent);
		setAttributeLabelText(attributeTitle);
		setAttributeLabelColor(attributeLabelColor);
		setAttributeCloseButtonColor(attributeLabelColor);
	}

	private void setAttributeTextFlowText(String attributeText) {
		Text text = new Text(attributeText);
		attributeTextFlow.getChildren().add(text);
	}
	
	private void setAttributeLabelText(String text) {
		attributeLabel.setText(text);
	}
	
	private void setAttributeLabelColor(String attributeLabelColor) {
		attributeLabelHBox.setStyle("-fx-background-color: " + attributeLabelColor + ";");
	}
	
	private void setAttributeCloseButtonColor(String attributeLabelColor) {
		closeButton.setStyle("-fx-background-color: " + attributeLabelColor + ";");
	}
	
	String getAttributeLabelText() {
		return attributeLabel.getText();
	}

}
