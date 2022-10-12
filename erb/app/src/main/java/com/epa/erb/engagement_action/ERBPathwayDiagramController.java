package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.utility.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ERBPathwayDiagramController implements Initializable {

	@FXML
	Line leftLeadingLine;
	@FXML
	Line rightLeadingLine;
	@FXML
	VBox diagramVBox;
	@FXML
	VBox arrowVBox;
	@FXML
	Circle centerCircle;
	@FXML
	Label activityLabel;
	@FXML
	Label centerCircleLabel;
	@FXML
	VBox holderVBox;
	@FXML
	HBox topLinesHBox;
	@FXML
	HBox bottomLinesHBox;
	@FXML
	HBox topCirclesHBox;
	@FXML
	HBox bottomCirclesHBox;
	@FXML
	HBox centerHBox;
	
//Keep this code to add spokes back into pathway diagram
//	@FXML
//	Circle topLeftCircle;
//	@FXML
//	Circle topRightCircle;
//	@FXML
//	Circle bottomLeftCircle;
//	@FXML
//	Circle bottomRightCircle;
//	@FXML
//	Label topLeftCircleLabel;
//	@FXML
//	Label topRightCircleLabel;
//	@FXML
//	Label bottomLeftCircleLabel;
//	@FXML
//	Label bottomRightCircleLabel;

	private Chapter chapter;
	private Activity activity;
	private EngagementActionController engagementActionController;
	public ERBPathwayDiagramController(Activity activity, Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.activity = activity;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBPathwayDiagramController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hideDetails(); //Default to remove spokes
		
		handleControls();
		setCircleColor();
		setCircleToolTips();
		hideLeadingLines();
		setCenterCircleLetter();
		setActivityLabelText(activity.getShortName());
	}
	
	private void handleControls() {
		if (activity.getLongName().length() == 0) {
			centerCircleLabel.setVisible(false);
		}
		engagementActionController.getShowDetailsCheckBox().setSelected(true);
		
//Keep this code to add spokes back into pathway diagram
//		if (activity.getMaterials().length() == 0) {
//			topLeftCircleLabel.setVisible(false);
//		}
//		if (activity.getDescription().length() == 0) {
//			topRightCircleLabel.setVisible(false);
//		}
//		if (activity.getWho().length() == 0) {
//			bottomLeftCircleLabel.setVisible(false);
//		}
//		if (activity.getTime().length() == 0) {
//			bottomRightCircleLabel.setVisible(false);
//		}
	}
	
	void hideDetails() {
		holderVBox.getChildren().remove(topLinesHBox);
		holderVBox.getChildren().remove(topCirclesHBox);
		holderVBox.getChildren().remove(bottomLinesHBox);
		holderVBox.getChildren().remove(bottomCirclesHBox);
	}
	
	void showDetails() {
		if(!holderVBox.getChildren().contains(topCirclesHBox)) {
			holderVBox.getChildren().add(0, topCirclesHBox);
		}
		if(!holderVBox.getChildren().contains(topLinesHBox)) {
			holderVBox.getChildren().add(1, topLinesHBox);
		}
		if(!holderVBox.getChildren().contains(bottomLinesHBox)) {
			holderVBox.getChildren().add(3, bottomLinesHBox);
		}
		if(!holderVBox.getChildren().contains(bottomCirclesHBox)) {
			holderVBox.getChildren().add(4, bottomCirclesHBox);
		}
	}
	
	private void hideLeadingLines() {
		int index = getIndexOfActivityInChapter(chapter, activity);
		if (chapter.getNumberOfAssignedActivities() == 1) {
			hideLeftLeadingLine();
			hideRightLeadingLine();
		} else if (index == chapter.getNumberOfAssignedActivities() - 1) {
			hideRightLeadingLine();
		} else if (index == 0) {
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
	
	public void updateStatus() {
		setCenterCircleLetter();
		setCenterCircleToolTip();
		setCenterCircleColor();
	}

	private void setCircleToolTips() {	
		//Center Circle
		setCenterCircleToolTip();
		
//Keep this code to add spokes back into pathway diagram
//		//Top Left Circle
//		createToolTip(splitString(activity.getMaterials().trim()), topLeftCircle, topLeftCircleLabel, constants.getMaterialsColor());
//		//Top Right Circle
//		createToolTip(splitString(activity.getDescription().trim()), topRightCircle, topRightCircleLabel, constants.getDescriptionColor());
//		//Bottom Left Circle
//		createToolTip(splitString(activity.getWho()), bottomLeftCircle, bottomLeftCircleLabel, constants.getWhoColor());
//		//Bottom Right Circle
//		createToolTip(splitString(activity.getTime()), bottomRightCircle, bottomRightCircleLabel, constants.getTimeColor());
	}
	
	private void setCenterCircleToolTip() {
		// Center Circle
		if (activity.getStatus().contentEquals("ready")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel,constants.getReadyStatusColor());
		} else if (activity.getStatus().contentEquals("complete")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel,constants.getCompleteStatusColor());
		} else if (activity.getStatus().contentEquals("in progress")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel,constants.getInProgressStatusColor());
		}
	}
	
	private void setCenterCircleLetter() {
		if (activity.getStatus().contentEquals("ready")) {
			centerCircleLabel.setText("R");
		} else if (activity.getStatus().contentEquals("complete")) {
			centerCircleLabel.setText("C");
		} else if (activity.getStatus().contentEquals("in progress")) {
			centerCircleLabel.setText("I");
		}
	}
	
	private void setCircleColor() {
		//Center Circle
		setCenterCircleColor();
		
//Keep this code to add spokes back into pathway diagram
//		//Top Left Circle
//		topLeftCircle.setStyle("-fx-fill: " + constants.getMaterialsColor() + ";");
//		//Top Right Circle
//		topRightCircle.setStyle("-fx-fill: " + constants.getDescriptionColor() + ";");
//		//Bottom Left Circle
//		bottomLeftCircle.setStyle("-fx-fill: " + constants.getWhoColor() + ";");
//		//Bottom Right Circle
//		bottomRightCircle.setStyle("-fx-fill: " + constants.getTimeColor() + ";");
	}
	
	private void setCenterCircleColor() {
		if (activity.getStatus().contentEquals("ready")) {
			centerCircle.setStyle("-fx-fill: " + constants.getReadyStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("complete")) {
			centerCircle.setStyle("-fx-fill: " + constants.getCompleteStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("in progress")) {
			centerCircle.setStyle("-fx-fill: " + constants.getInProgressStatusColor() + ";");
		}
	}
	
	private void createToolTip(String toolTipText, Circle circle, Label circleLabel, String color) {
		if (toolTipText != null && circle != null && circleLabel != null && color != null) {
			Tooltip tooltip = new Tooltip(toolTipText);
			Tooltip.install(circle, tooltip);
			Tooltip.install(circleLabel, tooltip);
			tooltip.setStyle("-fx-background-color: " + color + " ; -fx-text-fill: black;");
			circle.setStyle("-fx-fill: " + color + ";");
		} else {
			logger.error("Cannot createToolTip. a param is null.");
		}
	}
	
	@FXML
	public void arrowClicked() {
		Activity nextActivity = engagementActionController.retrieveNextActivity(this);
		if(nextActivity != null) {
			TreeItem<String> treeItem = engagementActionController.findTreeItem(nextActivity);
			if (treeItem != null) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
				engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
			}
		}
	}

	@FXML
	public void bottomRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentActivity().getActivityID();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(activity.getActivityID())) {
		}		
	}
	
	@FXML
	public void bottomLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentActivity().getActivityID();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(activity.getActivityID())) {
		}
	}

	@FXML
	public void topRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentActivity().getActivityID();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(activity.getActivityID())) {
		}
	}

	@FXML
	public void topLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentActivity().getActivityID();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(activity.getActivityID())) {
		}
	}

	@FXML
	public void centerCircleClicked() {
		String selectedActivityGUID = activity.getActivityID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
			if (engagementActionController.getTreeItemIdTreeMap().get(treeItem) == selectedActivityGUID) {
				Chapter treeItemChapter = engagementActionController.getChapterForNameInGoal(treeItem.getParent().getValue(), engagementActionController.getCurrentGoal());
				if (treeItemChapter != null) {
					if (String.valueOf(treeItemChapter.getChapterNum()).contentEquals(activity.getChapterAssignment())) {
						engagementActionController.getTreeView().getSelectionModel().select(treeItem);
						engagementActionController.treeViewClicked(null, treeItem);
					}
				}
			}
		}
	}
	
	private int getIndexOfActivityInChapter(Chapter chapter, Activity activity) {
		int count = 0;
		if (chapter != null && activity != null) {
			for (Activity a : chapter.getAssignedActivities()) {
				if (a.getActivityID().contentEquals(activity.getActivityID())) {
					return count;
				}
				count++;
			}
		} else {
			logger.error("Cannot getIndexOfActivityInChapter. chapter or activity is null.");
		}
		return -1;
	}
	
	/**
	 * Takes a string and breaks it into a new string with multiple line breaks
	 * 
	 * @param string
	 * @return
	 */
	private String splitString(String string) {
		if (string != null && string.length() > 50) {
			String regex = ".{1,50}\\s";
			Matcher m = Pattern.compile(regex).matcher(string);
			ArrayList<String> lines = new ArrayList<>();
			while (m.find()) {
				lines.add(m.group());
				string = string.replace(m.group(), "");
			}
			String lastString = lines.get(lines.size() - 1) + string;
			lines.remove(lines.size() - 1);
			lines.add(lastString);
			return String.join("\n", lines);
		} else if (string.length() <= 0) {
			return "None Defined";
		} else {
			return string;
		}
	}
	
	void highlightDiagram() {
		diagramVBox.setEffect(null);
	}
	
	void unHighlightDiagram() {
		GaussianBlur gaussianBlur = new GaussianBlur();
		diagramVBox.setEffect(gaussianBlur);
	}
	
	private void setActivityLabelText(String text) {
		activityLabel.setText(text);
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public Line getLeftLeadingLine() {
		return leftLeadingLine;
	}

	public Line getRightLeadingLine() {
		return rightLeadingLine;
	}

	public VBox getDiagramVBox() {
		return diagramVBox;
	}

	public VBox getArrowVBox() {
		return arrowVBox;
	}

	public Circle getCenterCircle() {
		return centerCircle;
	}
	
	public Label getActivityLabel() {
		return activityLabel;
	}

	public Label getCenterCircleLabel() {
		return centerCircleLabel;
	}
	
//--------------------Keep this code to add spokes back into pathway diagram-----------------------
//	public Circle getTopLeftCircle() {
//	    return topLeftCircle;
//  }	
//	public Circle getTopRightCircle() {
//		return topRightCircle;
//	}
//	public Circle getBottomLeftCircle() {
//		return bottomLeftCircle;
//	}
//	public Circle getBottomRightCircle() {
//		return bottomRightCircle;
//	}
//	public Label getTopLeftCircleLabel() {
//		return topLeftCircleLabel;
//	}
//	public Label getTopRightCircleLabel() {
//		return topRightCircleLabel;
//	}
//	public Label getBottomLeftCircleLabel() {
//		return bottomLeftCircleLabel;
//	}
//	public Label getBottomRightCircleLabel() {
//		return bottomRightCircleLabel;
//	}
	
}
