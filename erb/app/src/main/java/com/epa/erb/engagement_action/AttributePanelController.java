package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	
	
	public AttributePanelController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void setAttributeTextFlow(String attributeTextFlowString) {
		Text text = new Text(attributeTextFlowString);
		attributeTextFlow.getChildren().add(text);
	}
	
	public void setAttributeLabel(String attributeLabelString) {
		attributeLabel.setText(attributeLabelString);
	}
	
	public void setAttributeLabelColor(String attributeLabelColorString) {
		attributeLabelHBox.setStyle("-fx-background-color: " + attributeLabelColorString + ";");
	}
	
	public String getAttributeLabelText() {
		return attributeLabel.getText();
	}

}
