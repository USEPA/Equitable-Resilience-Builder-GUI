package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.ERBItem;
import com.epa.erb.chapter.Chapter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class EngagementGoalLandingContentController implements Initializable {

	@FXML
	VBox mainPanel;
	@FXML
	VBox aboutPanel;
	@FXML
	VBox activitiesPanel;
	@FXML
	VBox planPanel;
	@FXML
	VBox reflectPanel;
	@FXML
	Label headingLabel;
	@FXML
	ScrollPane headingLabelScrollPane;
	@FXML
	HBox headingLabelHBox;
	@FXML
	TextArea aboutTextArea;
	@FXML
	ListView<Activity> activitiesListView;
	@FXML
	ListView<Activity> planListView;
	@FXML
	ListView<Activity> reflectListView;

	private ArrayList<Chapter> listOfChaptersInGoal;
	private EngagementActionController engagementActionController;
	public EngagementGoalLandingContentController(ArrayList<Chapter> listOfChaptersInGoal, EngagementActionController engagementActionController) {
		this.listOfChaptersInGoal = listOfChaptersInGoal;
		this.engagementActionController = engagementActionController;
	}

	private Logger logger = LogManager.getLogger(EngagementGoalLandingContentController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setAboutTextAreaText(getAboutText());
		setHeadingLabelText(getHeadingLabelText());
		fillActivitiesListView(getActivitiesForListView());
	}

	private void handleControls() {
		handleControlsShown();
		headingLabelScrollPane.getStylesheets().add("/scrollPane.css");
		aboutTextArea.getStylesheets().add("/textArea.css");
	}

	private void handleControlsShown() {
		removePlanPanel();
		removeAboutPanel();
		removeReflectPanel();
	}

	public void setHeadingLabelText(String text) {
		headingLabel.setText(text);
	}

	String getHeadingLabelText() {
		return "Equitable Resilience Builder Activities";
	}

	public void setAboutTextAreaText(Text text) {
		String string = text.getText();
		string = string.replaceAll("\r", "\n");
		aboutTextArea.setText(string);
	}

	Text getAboutText() {
		return new Text("The Equitable Resilience Builder (ERB) is an application that assists communities with resilience planning. ERB engages communities in a guided process to inclusively assess their vulnerability and resilience to disasters and climate change, then use the results to prioritize actions to build resilience in an equitable way.");
	}

	public void fillActivitiesListView(ArrayList<Activity> listOfActivities) {
		if (listOfActivities != null) {
			cleanActivitiesListView();
			activitiesListView.setMinHeight(listOfActivities.size() * 27.5);
			activitiesListView.setMaxHeight(listOfActivities.size() * 27.5);
			for (Activity activity : listOfActivities) {
				activitiesListView.getItems().add(activity);
			}
			setListViewCellFactory(activitiesListView, "useItem");
		} else {
			logger.error("Cannot fillActivitiesListView. listOfActivites is null.");
		}
	}

	private ArrayList<Activity> getActivitiesForListView() {
		ArrayList<Activity> activitiesForListView = new ArrayList<Activity>();
		for (Chapter chapter : listOfChaptersInGoal) {
			for (Activity activity : chapter.getAssignedActivities()) {
				activitiesForListView.add(activity);
			}
		}
		return activitiesForListView;
	}

	private void setListViewCellFactory(ListView<Activity> listView, String text) {
		if (listView != null) {
			listView.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
				@Override
				public ListCell<Activity> call(ListView<Activity> param) {
					ListCell<Activity> cell = new ListCell<Activity>() {
						@Override
						protected void updateItem(Activity item, boolean empty) {
							super.updateItem(item, empty);
							if (item != null) {
								if (text.contentEquals("useItem")) {
									setText("Chapter " + getChapterNumberForActivity(item.getGuid()) + ": " + item.getLongName());
								} else {
									setText(text);
								}
								setFont(new Font(14.0));
							}
						}
					};
					cell.setOnMouseClicked(e -> handleListViewSelection(listView));
					return cell;
				}
			});
		} else {
			logger.error("Cannot setListViewCellFactory. listView is null.");
		}
	}
	
	private String getChapterNumberForActivity(String activityGUID) {
		if (activityGUID != null) {
			for (Chapter chapter : listOfChaptersInGoal) {
				for(Activity activity: chapter.getAssignedActivities()) {
					if(activity.getGuid().contentEquals(activityGUID)) {
						return String.valueOf(chapter.getNumber());
					}
				}
			}
		} else {
			logger.error("Cannot getChapterNumberForActivity. activityGUID is null");
		}
		return null;
	}

	private void handleListViewSelection(ListView<Activity> listView) {
		if (listView != null) {
			Activity selectedActivity = listView.getSelectionModel().getSelectedItem();
			TreeItem<ERBItem> treeItem = engagementActionController.findTreeItem(selectedActivity.getGuid());

			if (treeItem != null) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
				engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
			}
		} else {
			logger.error("Cannot handleListViewSelection. listView is null.");
		}
	}

	private void removeAboutPanel() {
		if (mainPanel.getChildren().contains(aboutPanel)) {
			mainPanel.getChildren().remove(aboutPanel);
		}
	}

	private void removePlanPanel() {
		if (mainPanel.getChildren().contains(planPanel)) {
			mainPanel.getChildren().remove(planPanel);
		}
	}

	private void removeReflectPanel() {
		if (mainPanel.getChildren().contains(reflectPanel)) {
			mainPanel.getChildren().remove(reflectPanel);
		}
	}

	private void cleanActivitiesListView() {
		activitiesListView.getItems().clear();
	}

	public VBox getMainPanel() {
		return mainPanel;
	}

	public VBox getAboutPanel() {
		return aboutPanel;
	}

	public Label getHeadingLabel() {
		return headingLabel;
	}

	public HBox getHeadingLabelHBox() {
		return headingLabelHBox;
	}

	public TextArea getAboutTextArea() {
		return aboutTextArea;
	}

	public ListView<Activity> getActivitiesListView() {
		return activitiesListView;
	}

	public void setActivitiesListView(ListView<Activity> activitiesListView) {
		this.activitiesListView = activitiesListView;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public ArrayList<Chapter> getListOfAllChapters() {
		return listOfChaptersInGoal;
	}

	public void setListOfAllChapters(ArrayList<Chapter> listOfAllChapters) {
		this.listOfChaptersInGoal = listOfAllChapters;
	}

}
