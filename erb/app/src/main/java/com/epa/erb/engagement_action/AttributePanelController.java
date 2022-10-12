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
		HBox attributePanel = (HBox) engagementActionController.getAttributePanelHBox();
		attributePanel.getChildren().remove(attributePanelVBox);
	}
	
	void setAttributeFields(String attributeTitle, String attributeContent, String attributeLabelColor) {
		setAttributeTextFlowText(attributeContent);
		setAttributeLabelText(attributeTitle);
		setAttributeLabelColor(attributeLabelColor);
		setAttributeCloseButtonColor(attributeLabelColor);
	}

	private void setAttributeTextFlowText(String attributeText) {
		if (attributeText != null) {
			Text text = new Text(attributeText);
			attributeTextFlow.getChildren().add(text);
		}
	}
	
	String getAttributeLabelText() {
		return attributeLabel.getText();
	}
	
	private void setAttributeLabelText(String text) {
		if(text != null)attributeLabel.setText(text);
	}
	
	private void setAttributeLabelColor(String attributeLabelColor) {
		if(attributeLabelColor!=null) attributeLabelHBox.setStyle("-fx-background-color: " + attributeLabelColor + ";");
	}
	
	private void setAttributeCloseButtonColor(String attributeLabelColor) {
		if(attributeLabelColor != null) closeButton.setStyle("-fx-background-color: " + attributeLabelColor + ";");
	}

	public String getAttributeTitle() {
		return attributeTitle;
	}

	public void setAttributeTitle(String attributeTitle) {
		this.attributeTitle = attributeTitle;
	}

	public String getAttributeContent() {
		return attributeContent;
	}

	public void setAttributeContent(String attributeContent) {
		this.attributeContent = attributeContent;
	}

	public String getAttributeColor() {
		return attributeColor;
	}

	public void setAttributeColor(String attributeColor) {
		this.attributeColor = attributeColor;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public Button getCloseButton() {
		return closeButton;
	}

	public Label getAttributeLabel() {
		return attributeLabel;
	}

	public VBox getAttributePanelVBox() {
		return attributePanelVBox;
	}

	public HBox getAttributeLabelHBox() {
		return attributeLabelHBox;
	}

	public TextFlow getAttributeTextFlow() {
		return attributeTextFlow;
	}

}
