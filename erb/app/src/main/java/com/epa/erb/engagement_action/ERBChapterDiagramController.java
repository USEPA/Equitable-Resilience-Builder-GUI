package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ERBChapterDiagramController implements Initializable{

	@FXML
	HBox diagramHBox;
	@FXML
	Line leftLeadingLine;
	@FXML
	Circle centerCircle;
	@FXML
	Label centerCircleLabel;
	@FXML
	Line rightLeadingLine;
	@FXML
	VBox arrowVBox;
	
	Chapter chapter;
	
	public ERBChapterDiagramController(Chapter chapter) {
		this.chapter = chapter;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		centerCircleLabel.setText(Integer.toString(chapter.getChapterNum()));
	}
	
	void hideLeftLeadingLine() {
		leftLeadingLine.setVisible(false);
	}
	
	void hideRightLeadingLine() {
		rightLeadingLine.setVisible(false);
		arrowVBox.setVisible(false);
	}

}
