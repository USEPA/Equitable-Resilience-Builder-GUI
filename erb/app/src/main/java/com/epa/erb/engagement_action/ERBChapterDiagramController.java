package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.epa.erb.chapter.Chapter;
import com.epa.erb.utility.Constants;

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
	VBox arrowVBox;
	@FXML
	HBox lineHBox;
	@FXML
	Line leftLeadingLine;
	@FXML
	Line rightLeadingLine;
	@FXML
	Circle centerCircle;
	@FXML
	Label centerCircleLabel;
	
	private Chapter chapter;
	private ArrayList<Chapter> listOfChapters;
	private EngagementActionController engagementActionController;
	public ERBChapterDiagramController(Chapter chapter, ArrayList<Chapter> listOfChapters, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.listOfChapters = listOfChapters;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		hideLeadingLines();
		setCenterCircleLabelText(Integer.toString(chapter.getNumber()));
	}
	
	private void handleControls() {
		initializeStyle();
	}
	
	private void initializeStyle() {
		fillChapterCircleBasedOnStatus();
	}
	
	public void fillChapterCircleBasedOnStatus() {
		String chapterStatus = chapter.getStatus();
		if(chapterStatus.contentEquals("ready")) {
			centerCircle.setStyle("-fx-fill: " + constants.getReadyStatusColor() + ";");
		} else if (chapterStatus.contentEquals("in progress")) {
			centerCircle.setStyle("-fx-fill: " + constants.getInProgressStatusColor() + ";");
		} else if (chapterStatus.contentEquals("complete")) {
			centerCircle.setStyle("-fx-fill: " + constants.getCompleteStatusColor() + ";");
		}
	}
	
	private void hideLeadingLines() {
		int index = getIndexOfChapterInListOfChapters();
		if(listOfChapters.size() == 1 && index == 0) {
			hideLeftLeadingLine();
			hideRightLeadingLine();
		} else if(index == listOfChapters.size()-1) {
			hideRightLeadingLine();
		} else if(index ==0) {
			hideLeftLeadingLine();
		}
	}
	
	private void hideLeftLeadingLine() {
		leftLeadingLine.setVisible(false);
		lineHBox.getChildren().remove(leftLeadingLine);
	}
	
	private void hideRightLeadingLine() {
		rightLeadingLine.setVisible(false);
		lineHBox.getChildren().remove(rightLeadingLine);
		arrowVBox.setVisible(false);
	}
	
	@FXML
	public void centerCircleClicked() {
		TreeItem<String> treeItem = engagementActionController.findTreeItem(chapter.getGuid());
		if (treeItem != null) {
			engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
			engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
		}
	}
		
	private void setCenterCircleLabelText(String text) {
		if(text != null) centerCircleLabel.setText(text);
	}
	
	private int getIndexOfChapterInListOfChapters() {
		return listOfChapters.indexOf(chapter);
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public ArrayList<Chapter> getListOfChapters() {
		return listOfChapters;
	}

	public void setListOfChapters(ArrayList<Chapter> listOfChapters) {
		this.listOfChapters = listOfChapters;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public VBox getArrowVBox() {
		return arrowVBox;
	}

	public Line getLeftLeadingLine() {
		return leftLeadingLine;
	}

	public Line getRightLeadingLine() {
		return rightLeadingLine;
	}

	public Circle getCenterCircle() {
		return centerCircle;
	}

	public Label getCenterCircleLabel() {
		return centerCircleLabel;
	}
	
}
