package erb;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.shape.Circle;

public class ERBPathwayDiagramController implements Initializable{

	@FXML
	Circle centerCircle;
	@FXML
	Circle topLeftCircle;
	@FXML
	Circle topRightCircle;
	@FXML
	Circle bottomLeftCircle;
	@FXML
	Circle bottomRightCircle;
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
	
	public void setToolTips() {
		Tooltip tooltip1 = new Tooltip(activity.shortName);
		Tooltip.install(centerCircle, tooltip1);
		tooltip1.setStyle("-fx-background-color: #EDF7B2; -fx-text-fill: black;");
		Tooltip tooltip2 = new Tooltip(splitString(activity.getLongName()));
		Tooltip.install(topLeftCircle, tooltip2);
		tooltip2.setStyle("-fx-background-color: #B2C1F7; -fx-text-fill: black;");
		Tooltip tooltip3 = new Tooltip(splitString(activity.getDescription()));
		Tooltip.install(topRightCircle, tooltip3);
		tooltip3.setStyle("-fx-background-color: #F7B2E0; -fx-text-fill: black;");
		Tooltip tooltip4 = new Tooltip(splitString(activity.getDirections()));
		Tooltip.install(bottomLeftCircle, tooltip4);
		tooltip4.setStyle("-fx-background-color: #B2F7D1; -fx-text-fill: black;");
		Tooltip tooltip5 = new Tooltip(splitString(activity.getObjectives()));
		Tooltip.install(bottomRightCircle, tooltip5);
		tooltip5.setStyle("-fx-background-color: #F7DBB2; -fx-text-fill: black;");
	}
	
	/**
	 * Takes a string and breaks it into a new string with multiple line breaks
	 * 
	 * @param string
	 * @return
	 */
	public String splitString(String string) {
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
		} else {
			return string;
		}
	}
	
	@FXML
	public void bottomRightCircleClicked() {
		
	}
	
	@FXML
	public void bottomLeftCircleClicked() {
		
	}
	
	@FXML
	public void topRightCircleClicked() {
		
	}
	
	@FXML
	public void topLeftCircleClicked() {
		
	}
	
	@FXML
	public void centerCircleClicked() {
		String activityGUID = activity.getGUID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if(engagementActionController.getTreeMap().get(treeItem) == activityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}

}
