package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ERBChapterDiagramController implements Initializable{

	@FXML
	VBox arrowVBox;
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
		setCenterCircleLabelText(Integer.toString(chapter.getChapterNum()));
	}
	
	private void handleControls() {
		initializeStyle();
	}
	
	private void initializeStyle() {
		centerCircle.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
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
	}
	
	private void hideRightLeadingLine() {
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
	
	private void setCenterCircleLabelText(String text) {
		centerCircleLabel.setText(text);
	}
	
	private int getIndexOfChapterInListOfChapters() {
		return listOfChapters.indexOf(chapter);
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
	
}
