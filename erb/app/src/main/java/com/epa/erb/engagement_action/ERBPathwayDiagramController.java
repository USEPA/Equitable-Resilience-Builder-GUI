package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import com.epa.erb.chapter.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.GaussianBlur;
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
	Circle topLeftCircle;
	@FXML
	Circle centerCircle;
	@FXML
	Circle topRightCircle;
	@FXML
	Circle bottomLeftCircle;
	@FXML
	Circle bottomRightCircle;
	@FXML
	Label topLeftCircleLabel;
	@FXML
	Label topRightCircleLabel;
	@FXML
	Label bottomLeftCircleLabel;
	@FXML
	Label bottomRightCircleLabel;
	@FXML
	Label activityLabel;
	@FXML
	Label centerCircleLabel;

	private Activity activity;
	private Chapter chapter;
	private EngagementActionController engagementActionController;
	public ERBPathwayDiagramController(Activity activity, Chapter chapter, EngagementActionController engagementActionController) {
		this.activity = activity;
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setCircleColor();
		setCircleToolTip();
		hideLeadingLines();
		setCenterCircleLetter();
		setActivityLabelText(activity.getShortName());
	}
	
	private void handleControls() {
		if (activity.getLongName().length() == 0) {
			centerCircleLabel.setVisible(false);
		}
		if (activity.getMaterials().length() == 0) {
			topLeftCircleLabel.setVisible(false);
		}
		if (activity.getDescription().length() == 0) {
			topRightCircleLabel.setVisible(false);
		}
		if (activity.getWho().length() == 0) {
			bottomLeftCircleLabel.setVisible(false);
		}
		if (activity.getTime().length() == 0) {
			bottomRightCircleLabel.setVisible(false);
		}
	}
	
	private void hideLeadingLines() {
		int index = getIndexOfActivityInChapter();
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

	private void setCircleToolTip() {	
		//Center Circle
		setCenterCircleToolTip();
		//Top Left Circle
		createToolTip(splitString(activity.getMaterials().trim()), topLeftCircle, topLeftCircleLabel, constants.getMaterialsColor());
		//Top Right Circle
		createToolTip(splitString(activity.getDescription().trim()), topRightCircle, topRightCircleLabel, constants.getDescriptionColor());
		//Bottom Left Circle
		createToolTip(splitString(activity.getWho()), bottomLeftCircle, bottomLeftCircleLabel, constants.getWhoColor());
		//Bottom Right Circle
		createToolTip(splitString(activity.getTime()), bottomRightCircle, bottomRightCircleLabel, constants.getTimeColor());
	}
	
	private void setCenterCircleToolTip() {
		// Center Circle
		if (activity.getStatus().contentEquals("ready")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel,constants.getReadyStatusColor());
		} else if (activity.getStatus().contentEquals("skipped")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel,constants.getSkippedStatusColor());
		} else if (activity.getStatus().contentEquals("complete")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel,constants.getCompleteStatusColor());
		} else if (activity.getStatus().contentEquals("in progress")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel,constants.getInProgressStatusColor());
		}
	}
	
	private void setCenterCircleLetter() {
		if (activity.getStatus().contentEquals("ready")) {
			centerCircleLabel.setText("R");
		} else if (activity.getStatus().contentEquals("skipped")) {
			centerCircleLabel.setText("S");
		} else if (activity.getStatus().contentEquals("complete")) {
			centerCircleLabel.setText("C");
		} else if (activity.getStatus().contentEquals("in progress")) {
			centerCircleLabel.setText("I");
		}
	}
	
	private void setCircleColor() {
		//Center Circle
		setCenterCircleColor();
		//Top Left Circle
		topLeftCircle.setStyle("-fx-fill: " + constants.getMaterialsColor() + ";");
		//Top Right Circle
		topRightCircle.setStyle("-fx-fill: " + constants.getDescriptionColor() + ";");
		//Bottom Left Circle
		bottomLeftCircle.setStyle("-fx-fill: " + constants.getWhoColor() + ";");
		//Bottom Right Circle
		bottomRightCircle.setStyle("-fx-fill: " + constants.getTimeColor() + ";");
	}
	
	private void setCenterCircleColor() {
		if (activity.getStatus().contentEquals("ready")) {
			centerCircle.setStyle("-fx-fill: " + constants.getReadyStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("skipped")) {
			centerCircle.setStyle("-fx-fill: " + constants.getSkippedStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("complete")) {
			centerCircle.setStyle("-fx-fill: " + constants.getCompleteStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("in progress")) {
			centerCircle.setStyle("-fx-fill: " + constants.getInProgressStatusColor() + ";");
		}
	}
	
	private void createToolTip(String toolTipText, Circle circle, Label circleLabel, String color) {
		Tooltip tooltip = new Tooltip(toolTipText);
		Tooltip.install(circle, tooltip);
		Tooltip.install(circleLabel, tooltip);
		tooltip.setStyle("-fx-background-color: " + color + " ; -fx-text-fill: black;");
		circle.setStyle("-fx-fill: " + color + ";");
	}

	@FXML
	public void bottomRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getActivityID())) {
			engagementActionController.handleAttributePanelGeneration("Time", activity.getTime(), constants.getTimeColor());
		}		
	}
	
	@FXML
	public void bottomLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getActivityID())) {
			engagementActionController.handleAttributePanelGeneration("Who", activity.getWho(), constants.getWhoColor());
		}
	}

	@FXML
	public void topRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getActivityID())) {
			engagementActionController.handleAttributePanelGeneration("Description", activity.getDescription(), constants.getDescriptionColor());
		}
	}

	@FXML
	public void topLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getActivityID())) {
			engagementActionController.handleAttributePanelGeneration("Materials", activity.getMaterials(), constants.getMaterialsColor());
		}
	}

	@FXML
	public void centerCircleClicked() {
		String selectedActivityGUID = activity.getActivityID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == selectedActivityGUID) {
				Chapter treeItemChapter = engagementActionController.getChapter(treeItem.getParent().getValue());
				if (String.valueOf(treeItemChapter.getChapterNum()).contentEquals(activity.getChapterAssignment())) {
					engagementActionController.getTreeView().getSelectionModel().select(treeItem);
					engagementActionController.treeViewClicked(null);
				}
			}
		}
	}
	
	private int getIndexOfActivityInChapter() {
		int count = 0;
		for (Activity a : chapter.getAssignedActivities()) {
			if (a.getActivityID().contentEquals(activity.getActivityID())) {
				return count;
			}
			count++;
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
		if (string.length() > 50) {
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

	public Circle getTopLeftCircle() {
		return topLeftCircle;
	}

	public Circle getCenterCircle() {
		return centerCircle;
	}

	public Circle getTopRightCircle() {
		return topRightCircle;
	}

	public Circle getBottomLeftCircle() {
		return bottomLeftCircle;
	}

	public Circle getBottomRightCircle() {
		return bottomRightCircle;
	}

	public Label getTopLeftCircleLabel() {
		return topLeftCircleLabel;
	}

	public Label getTopRightCircleLabel() {
		return topRightCircleLabel;
	}

	public Label getBottomLeftCircleLabel() {
		return bottomLeftCircleLabel;
	}

	public Label getBottomRightCircleLabel() {
		return bottomRightCircleLabel;
	}

	public Label getActivityLabel() {
		return activityLabel;
	}

	public Label getCenterCircleLabel() {
		return centerCircleLabel;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

}
