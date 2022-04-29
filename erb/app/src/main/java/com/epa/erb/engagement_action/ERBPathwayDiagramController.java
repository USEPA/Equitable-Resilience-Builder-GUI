package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
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
	@FXML
	Line leftLeadingLine;
	@FXML
	Line rightLeadingLine;

	private Activity activity;
	private EngagementActionController engagementActionController;
	public ERBPathwayDiagramController(Activity activity, EngagementActionController engagementActionController) {
		this.activity = activity;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		initializeStyle();
		setToolTips();
	}
	
	private void handleControls() {
		activityLabel.setText(activity.getShortName());
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
	
	private void initializeStyle() {
		topLeftCircle.setStyle("-fx-fill: " + constants.getMaterialsColor() + ";");
		topRightCircle.setStyle("-fx-fill: " + constants.getDescriptionColor() + ";");
		centerCircle.setStyle("-fx-fill: " + constants.getReadyStatusColor() + ";");
		bottomLeftCircle.setStyle("-fx-fill: " + constants.getWhoColor() + ";");
		bottomRightCircle.setStyle("-fx-fill: " + constants.getTimeColor() + ";");
	}

	void hideLeftLeadingLine() {
		leftLeadingLine.setVisible(false);
	}

	void hideRightLeadingLine() {
		rightLeadingLine.setVisible(false);
		arrowVBox.setVisible(false);
	}
	
	public void updateStatus() {
		if (activity.getStatus().contentEquals("ready")) {
			centerCircleLabel.setText("R");
			centerCircle.setStyle("-fx-fill: " + constants.getReadyStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("skipped")) {
			centerCircleLabel.setText("S");
			centerCircle.setStyle("-fx-fill: " + constants.getSkippedStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("complete")) {
			centerCircleLabel.setText("C");
			centerCircle.setStyle("-fx-fill: " + constants.getCompleteStatusColor() + ";");
		} else if (activity.getStatus().contentEquals("in progress")) {
			centerCircleLabel.setText("I");
			centerCircle.setStyle("-fx-fill: " + constants.getInProgressStatusColor() + ";");
		}
	}

	void setToolTips() {
		//Center Circle
		if (activity.getStatus().contentEquals("ready")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel, constants.getReadyStatusColor());
			centerCircleLabel.setText("R");
		} else if (activity.getStatus().contentEquals("skipped")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel, constants.getSkippedStatusColor());
			centerCircleLabel.setText("S");
		} else if (activity.getStatus().contentEquals("complete")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel, constants.getCompleteStatusColor());
			centerCircleLabel.setText("C");
		} else if (activity.getStatus().contentEquals("in progress")) {
			createToolTip(splitString(activity.getLongName()), centerCircle, centerCircleLabel, constants.getInProgressStatusColor());
			centerCircleLabel.setText("I");
		}
		
		//Top Left Circle
		createToolTip(splitString(activity.getMaterials().trim()), topLeftCircle, topLeftCircleLabel, constants.getMaterialsColor());
		//Top Right Circle
		createToolTip(splitString(activity.getDescription().trim()), topRightCircle, topRightCircleLabel, constants.getDescriptionColor());
		//Bottom Left Circle
		createToolTip(splitString(activity.getWho()), bottomLeftCircle, bottomLeftCircleLabel, constants.getWhoColor());
		//Bottom Right Circle
		createToolTip(splitString(activity.getTime()), bottomRightCircle, bottomRightCircleLabel, constants.getTimeColor());
	}
	
	private void createToolTip(String toolTipText, Circle circle, Label circleLabel, String color) {
		Tooltip tooltip = new Tooltip(toolTipText);
		Tooltip.install(circle, tooltip);
		Tooltip.install(circleLabel, tooltip);
		tooltip.setStyle("-fx-background-color: " + color + " ; -fx-text-fill: black;");
		circle.setStyle("-fx-fill: " + color + ";");
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

	@FXML
	public void bottomRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Time", activity.getTime(), constants.getTimeColor());
		}		
	}
	
	@FXML
	public void bottomLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Who", activity.getWho(), constants.getWhoColor());
		}
	}

	@FXML
	public void topRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Description", activity.getDescription(), constants.getDescriptionColor());
		}
	}

	@FXML
	public void topLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getSelectedActivityGUID();
		if (selectedActivityGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Materials", activity.getMaterials(), constants.getMaterialsColor());
		}
	}

	@FXML
	public void centerCircleClicked() {
		String selectedActivityGUID = activity.getGUID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == selectedActivityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}

	@FXML
	public void centerCircleLabelClicked() {
		String selectedActivityGUID = activity.getGUID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == selectedActivityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}
	
	void highlightDiagram() {
		diagramVBox.setEffect(null);
	}
	
	void unHighlightDiagram() {
		GaussianBlur gaussianBlur = new GaussianBlur();
		diagramVBox.setEffect(gaussianBlur);
	}
	
	Activity getActivity() {
		return activity;
	}
}
