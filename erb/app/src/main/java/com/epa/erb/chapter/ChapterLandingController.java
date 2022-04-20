package com.epa.erb.chapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import com.epa.erb.engagement_action.EngagementActionController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ChapterLandingController implements Initializable {

	@FXML
	Label headingLabel;
	@FXML
	HBox headingLabelHBox;
	@FXML
	TextArea aboutTextArea;
	@FXML
	ListView<Activity> activitiesListView;
	@FXML
	Button getStartedButton;
	
	Chapter chapter;
	EngagementActionController engagementActionController;
	public ChapterLandingController(Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	ArrayList<Chapter> listOfAllChapters;
	public ChapterLandingController(ArrayList<Chapter> listOfAllChapters, EngagementActionController engagementActionController) {
		this.listOfAllChapters = listOfAllChapters;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeStyle();
	}
	
	private void initializeStyle() {
		headingLabelHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		getStartedButton.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	public void setHeadingLabel() {
		if(chapter != null) {
			headingLabel.setText("Welcome to " + chapter.getStringName());
		} else {
			headingLabel.setText("Welcome to the ERB");
		}
	}
	
	public void setAboutText() {
		Text text = getAboutText();
		text.setFont(Font.font(15));
		aboutTextArea.setText(text.getText());
	}
	
	Text getAboutText() {
		if (chapter != null) {
			return new Text(chapter.getDescriptionName());
		} else {
			return new Text("The Equitable Resilience Builder (ERB) is an application that assists communities with resilience planning. ERB engages communities in a guided process to inclusively assess their vulnerability and resilience to disasters and climate change, then use the results to prioritize actions to build resilience in an equitable way.");
		}
	}

	public void setActivitiesListView() {
		cleanActivitiesListView();
		if (chapter != null) {
			for (Activity activity : chapter.getUserSelectedActivities()) {
				activitiesListView.getItems().add(activity);
			}
		} else {
			for (Chapter chapter : listOfAllChapters) {
				for (Activity activity : chapter.getUserSelectedActivities()) {
					if (activity.getShortName().contentEquals("Reflect")|| activity.getShortName().contentEquals("Plan")) {
						activitiesListView.getItems().add(activity);
					} else {
						activitiesListView.getItems().add(activity);
					}
				}
			}
		}
		setActivityListViewCellFactory();
	}
	
	private void setActivityListViewCellFactory() {
		activitiesListView.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
			@Override
			public ListCell<Activity> call(ListView<Activity> param) {
				ListCell<Activity> cell = new ListCell<Activity>() {
					@Override
					protected void updateItem(Activity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							Chapter assignedChapter = engagementActionController.getChapter(item);
							if (assignedChapter != null) {
								setText(assignedChapter.getStringName() + ": " + item.getLongName());
							} else {
								setText(item.getLongName());
							}
							setFont(new Font(14.0));
						}
					}
				};
				cell.setOnMouseClicked(e -> activitySelectedInList());
				return cell;
			}
		});
	}
	
	public void activitySelectedInList() {
		Activity selectedActivity = activitiesListView.getSelectionModel().getSelectedItem();
		HashMap<TreeItem<String>, String> treeMap = engagementActionController.getTreeMap();
		for(TreeItem<String> treeItem : treeMap.keySet()) {
			if(treeItem.getValue().contentEquals(selectedActivity.getLongName())) {
				if(treeMap.get(treeItem).contentEquals(selectedActivity.getGUID())){
					engagementActionController.getTreeView().getSelectionModel().select(treeItem);
					engagementActionController.treeViewClicked();
				}
			}
		}
	}
	
	@FXML
	public void getStartedButtonAction() {
		String activityGUID = chapter.getUserSelectedActivities().get(0).getGUID();
		for (TreeItem<String> treeItem : engagementActionController.getTreeMap().keySet()) {
			if (engagementActionController.getTreeMap().get(treeItem) == activityGUID) {
				engagementActionController.getTreeView().getSelectionModel().select(treeItem);
				engagementActionController.treeViewClicked();
			}
		}
	}
	
	void cleanActivitiesListView() {
		activitiesListView.getItems().clear();
	}

}
