package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.epa.erb.Activity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ERBPathwayDiagramController implements Initializable {

	@FXML
	VBox diagramVBox;
	@FXML
	Circle topLeftCircle;
	@FXML
	Label topLeftCircleLabel;
	@FXML
	Circle topRightCircle;
	@FXML
	Label topRightCircleLabel;
	@FXML
	Line leftLeadingLine;
	@FXML
	Circle centerCircle;
	@FXML
	Label centerCircleLabel;
	@FXML
	Line rightLeadingLine;
	@FXML
	Circle bottomLeftCircle;
	@FXML
	Label bottomLeftCircleLabel;
	@FXML
	Circle bottomRightCircle;
	@FXML
	Label bottomRightCircleLabel;
	@FXML
	VBox arrowVBox;
	@FXML
	Label activityLabel;
	
	Activity activity;
	EngagementActionController engagementActionController;

	public ERBPathwayDiagramController(Activity activity, EngagementActionController engagementActionController) {
		this.activity = activity;
		this.engagementActionController = engagementActionController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setToolTips();
		activityLabel.setText(activity.getShortName());
	}

	void hideLeftLeadingLine() {
		leftLeadingLine.setVisible(false);
	}

	void hideRightLeadingLine() {
		rightLeadingLine.setVisible(false);
		arrowVBox.setVisible(false);
	}

	private void setToolTips() {
		Tooltip tooltip1 = new Tooltip(activity.getLongName());
		if (activity.getLongName().length() == 0) {
			centerCircleLabel.setVisible(false);
		}
		Tooltip.install(centerCircle, tooltip1);
		Tooltip.install(centerCircleLabel, tooltip1);
		if (activity.getStatus().contentEquals("ready")) {
			tooltip1.setStyle("-fx-background-color:  #EDF7B2; -fx-text-fill: black;");
			centerCircleLabel.setText("R");
		} else if (activity.getStatus().contentEquals("skip")) {
			tooltip1.setStyle("-fx-background-color:  #B2C1F7; -fx-text-fill: black;");
			centerCircleLabel.setText("S");
		} else if (activity.getStatus().contentEquals("complete")) {
			tooltip1.setStyle("-fx-background-color:  #B2F7D1; -fx-text-fill: black;");
			centerCircleLabel.setText("C");
		}

		Tooltip tooltip2 = new Tooltip(splitString(activity.getMaterials().trim()));
		if (activity.getMaterials().length() == 0) {
			topLeftCircleLabel.setVisible(false);
		}
		Tooltip.install(topLeftCircle, tooltip2);
		Tooltip.install(topLeftCircleLabel, tooltip2);
		tooltip2.setStyle("-fx-background-color: #B895EB; -fx-text-fill: black;");

		Tooltip tooltip3 = new Tooltip(splitString(activity.getDescription().trim()));
		if (activity.getDescription().length() == 0) {
			topRightCircleLabel.setVisible(false);
		}
		Tooltip.install(topRightCircle, tooltip3);
		Tooltip.install(topRightCircleLabel, tooltip3);
		tooltip3.setStyle("-fx-background-color: #F7B2E0; -fx-text-fill: black;");

		Tooltip tooltip4 = new Tooltip(splitString(activity.getWho().trim()));
		if (activity.getWho().length() == 0) {
			bottomLeftCircleLabel.setVisible(false);
		}
		Tooltip.install(bottomLeftCircle, tooltip4);
		Tooltip.install(bottomLeftCircleLabel, tooltip4);
		tooltip4.setStyle("-fx-background-color: #EB8787; -fx-text-fill: black;");

		Tooltip tooltip5 = new Tooltip(splitString(activity.getTime().trim()));
		if (activity.getTime().length() == 0) {
			bottomRightCircleLabel.setVisible(false);
		}
		Tooltip.install(bottomRightCircle, tooltip5);
		Tooltip.install(bottomRightCircleLabel, tooltip5);
		tooltip5.setStyle("-fx-background-color: #F7DBB2; -fx-text-fill: black;");
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
	public void bottomRightCircleClicked() {

	}

	@FXML
	public void bottomRightCircleLabelClicked() {
		String selectedGUID = engagementActionController.getSelectedGUID();
		if (selectedGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Time", activity.getTime(), "#F7DBB2");
		}
	}

	@FXML
	public void bottomLeftCircleClicked() {

	}

	@FXML
	public void bottomLeftCircleLabelClicked() {
		String selectedGUID = engagementActionController.getSelectedGUID();
		if (selectedGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Who", activity.getWho(), "#EB8787");
		}
	}

	@FXML
	public void topRightCircleClicked() {

	}

	@FXML
	public void topRightCircleLabelClicked() {
		String selectedGUID = engagementActionController.getSelectedGUID();
		if (selectedGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Description", activity.getDescription(), "#F7B2E0");
		}
	}

	@FXML
	public void topLeftCircleClicked() {

	}

	@FXML
	public void topLeftCircleLabelClicked() {
		String selectedGUID = engagementActionController.getSelectedGUID();
		if (selectedGUID.contentEquals(activity.getGUID())) {
			engagementActionController.loadAttributeInfo("Materials", activity.getMaterials(), "#B895EB");
		}
	}

	@FXML
	public void centerCircleClicked() {
		String activityGUID = activity.getGUID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == activityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}

	@FXML
	public void centerCircleLabelClicked() {
		String activityGUID = activity.getGUID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == activityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}
}
