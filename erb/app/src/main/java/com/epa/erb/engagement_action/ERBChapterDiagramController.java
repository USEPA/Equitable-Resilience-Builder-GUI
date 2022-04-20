package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
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
	
	private Chapter chapter;
	private EngagementActionController engagementActionController;
	public ERBChapterDiagramController(Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeStyle();
		centerCircleLabel.setText(Integer.toString(chapter.getChapterNum()));
	}
	
	private void initializeStyle() {
		centerCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
	}
	
	void hideLeftLeadingLine() {
		leftLeadingLine.setVisible(false);
	}
	
	void hideRightLeadingLine() {
		rightLeadingLine.setVisible(false);
		arrowVBox.setVisible(false);
	}
	
	@FXML
	public void centerCircleClicked() {
		String activityGUID = chapter.getNumericName();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == activityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}
}
