package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.ContentPanel;
import com.epa.erb.ERBItem;
import com.epa.erb.ERBItemFinder;
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
	HBox centerHBox;

	private Chapter chapter;
//	private Activity activity;
	private ContentPanel contentPanel;
	private EngagementActionController engagementActionController;
//	public ERBPathwayDiagramController(Activity activity, Chapter chapter, EngagementActionController engagementActionController) {
//		this.chapter = chapter;
//		this.activity = activity;
//		this.engagementActionController = engagementActionController;
//	}
	
	public ERBPathwayDiagramController(ContentPanel contentPanel, Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.contentPanel = contentPanel;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	private Logger logger = LogManager.getLogger(ERBPathwayDiagramController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		handleControls();
		setCircleColor();
		setCircleToolTips();
		hideLeadingLines();
		setCenterCircleLetter();
		setActivityLabelText(contentPanel.getShortName());
	}
	
	private void handleControls() {
		if (contentPanel.getLongName().length() == 0) {
			centerCircleLabel.setVisible(false);
		}
	}
	
	private void hideLeadingLines() {
//		int index = erbItemFinder.getIndexOfActivityInChapter(chapter, activity);
//		if (chapter.getNumberOfAssignedActivities() == 1) {
//			hideLeftLeadingLine();
//			hideRightLeadingLine();
//		} else if (index == chapter.getNumberOfAssignedActivities() - 1) {
//			hideRightLeadingLine();
//		} else if (index == 0) {
//			hideLeftLeadingLine();
//		}
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
	}
	
	private void setCenterCircleToolTip() {
		// Center Circle
		if (contentPanel.getStatus().contentEquals("ready")) {
			createToolTip(splitString(contentPanel.getLongName()), centerCircle, centerCircleLabel,constants.getReadyStatusColor());
		} else if (contentPanel.getStatus().contentEquals("complete")) {
			createToolTip(splitString(contentPanel.getLongName()), centerCircle, centerCircleLabel,constants.getCompleteStatusColor());
		} else if (contentPanel.getStatus().contentEquals("in progress")) {
			createToolTip(splitString(contentPanel.getLongName()), centerCircle, centerCircleLabel,constants.getInProgressStatusColor());
		}
	}
	
	private void setCenterCircleLetter() {
		if (contentPanel.getStatus().contentEquals("ready")) {
			centerCircleLabel.setText("R");
		} else if (contentPanel.getStatus().contentEquals("complete")) {
			centerCircleLabel.setText("C");
		} else if (contentPanel.getStatus().contentEquals("in progress")) {
			centerCircleLabel.setText("I");
		}
	}
	
	private void setCircleColor() {
		//Center Circle
		setCenterCircleColor();
	}
	
	private void setCenterCircleColor() {
		if (contentPanel.getStatus().contentEquals("ready")) {
			centerCircle.setStyle("-fx-fill: " + constants.getReadyStatusColor() + ";");
		} else if (contentPanel.getStatus().contentEquals("complete")) {
			centerCircle.setStyle("-fx-fill: " + constants.getCompleteStatusColor() + ";");
		} else if (contentPanel.getStatus().contentEquals("in progress")) {
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
		ContentPanel nextActivity = engagementActionController.retrieveNextActivity(this);
		if(nextActivity != null) {
			TreeItem<ERBItem> treeItem = engagementActionController.findTreeItem(nextActivity.getGuid());

			if (treeItem != null) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
				engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
			}
		}
	}

	@FXML
	public void bottomRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentSelectedContentPanel().getId();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(contentPanel.getId())) {
		}		
	}
	
	@FXML
	public void bottomLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentSelectedContentPanel().getId();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(contentPanel.getId())) {
		}
	}

	@FXML
	public void topRightCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentSelectedContentPanel().getId();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(contentPanel.getId())) {
		}
	}

	@FXML
	public void topLeftCircleLabelClicked() {
		String selectedActivityGUID = engagementActionController.getCurrentSelectedContentPanel().getId();
		if (selectedActivityGUID != null && selectedActivityGUID.contentEquals(contentPanel.getId())) {
		}
	}

	@FXML
	public void centerCircleClicked() {
		TreeItem<ERBItem> treeItem = engagementActionController.findTreeItem(contentPanel.getGuid());

		if (treeItem != null) {
			engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
			engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
		}
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

	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	public void setContentPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
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
}
