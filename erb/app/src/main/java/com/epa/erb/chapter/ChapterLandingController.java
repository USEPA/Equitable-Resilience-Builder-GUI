package com.epa.erb.chapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import com.epa.erb.engagement_action.EngagementActionController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ChapterLandingController implements Initializable {

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
	HBox headingLabelHBox;
	@FXML
	TextArea aboutTextArea;
	@FXML
	ListView<Activity> activitiesListView;
	@FXML
	ListView<Activity> planListView;
	@FXML
	ListView<Activity> reflectListView;
	
	private Chapter chapter;
	private EngagementActionController engagementActionController;
	public ChapterLandingController(Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private ArrayList<Chapter> listOfAllChapters;
	public ChapterLandingController(ArrayList<Chapter> listOfAllChapters, EngagementActionController engagementActionController) {
		this.listOfAllChapters = listOfAllChapters;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setAboutTextAreaText(getAboutText());
		setHeadingLabelText(getHeadingLabelText());
		fillActivitiesListView(getActivitiesForListView());
		fillPlanListView();
		fillReflectListView();
	}
	
	private void handleControls() {
		headingLabelHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
		aboutTextArea.getStylesheets().add("/textArea.css");
		handleControlsShowed();
	}
	
	private void handleControlsShowed() {
		if(listOfAllChapters != null) {
			removePlanPanel();
			removeReflectPanel();
		} else {
			addPlanPanel();
			addReflectPanel();
		}
	}
	
	public void setHeadingLabelText(String text) {
		headingLabel.setText(text);
	}
	
	String getHeadingLabelText() {
		if (chapter != null) {
			return "Welcome to " + chapter.getStringName();
		} else {
			return "ERB Activities";
		}
	}
	
	public void setAboutTextAreaText(Text text) {
		aboutTextArea.setText(text.getText());
	}
	
	Text getAboutText() {
		if (chapter != null) {
			addAboutPanel();
			return new Text(chapter.getDescriptionName());
		} else {
			removeAboutPanel();
			return new Text("The Equitable Resilience Builder (ERB) is an application that assists communities with resilience planning. ERB engages communities in a guided process to inclusively assess their vulnerability and resilience to disasters and climate change, then use the results to prioritize actions to build resilience in an equitable way.");
		}
	}

	public void fillActivitiesListView(ArrayList<Activity> listOfActivities) {
		cleanActivitiesListView();
		for(Activity activity: listOfActivities) {
			activitiesListView.getItems().add(activity);
		}
		setActivityListViewCellFactory();
	}
	
	public void fillPlanListView() {
		cleanPlanListView();
		Activity planActivity = engagementActionController.getActivityForIDInGoal("25", engagementActionController.getCurrentGoal());
		planListView.getItems().add(planActivity);
		setPlanListViewCellFactory();
	}
	
	public void fillReflectListView() {
		cleanReflectListView();
		Activity reflectActivity = engagementActionController.getActivityForIDInGoal("26", engagementActionController.getCurrentGoal());
		reflectListView.getItems().add(reflectActivity);
		setReflectListViewCellFactory();
	}
	
	private ArrayList<Activity> getActivitiesForListView() {
		ArrayList<Activity> activitiesForListView = new ArrayList<Activity>();
		if (chapter != null) { // landing for a single chapter
			for (Activity activity : chapter.getAssignedActivities()) {
				activitiesForListView.add(activity);
			}
		} else { // landing for erb showing all chapters
			for (Chapter chapter : listOfAllChapters) {
				for (Activity activity : chapter.getAssignedActivities()) {
					activitiesForListView.add(activity);
				}
			}
		}
		return activitiesForListView;
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
							setText("Chapter " + item.getChapterAssignment() + ": " + item.getLongName());
							setFont(new Font(14.0));
						}
					}
				};
				cell.setOnMouseClicked(e -> handleActivitySelectedInList());
				return cell;
			}
		});
	}
	
	private void setPlanListViewCellFactory() {
		planListView.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
			@Override
			public ListCell<Activity> call(ListView<Activity> param) {
				ListCell<Activity> cell = new ListCell<Activity>() {
					@Override
					protected void updateItem(Activity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText("Chapter " + chapter.getChapterNum() + ": Plan");
							setFont(new Font(14.0));
						}
					}
				};
				cell.setOnMouseClicked(e -> planSelectedInList());
				return cell;
			}
		});
	}
	
	private void setReflectListViewCellFactory() {
		reflectListView.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
			@Override
			public ListCell<Activity> call(ListView<Activity> param) {
				ListCell<Activity> cell = new ListCell<Activity>() {
					@Override
					protected void updateItem(Activity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText("Chapter " + chapter.getChapterNum() + ": Reflect");
							setFont(new Font(14.0));
						}
					}
				};
				cell.setOnMouseClicked(e -> reflectSelectedInList());
				return cell;
			}
		});
	}
	
	private void handleActivitySelectedInList() {
		Activity selectedActivity = activitiesListView.getSelectionModel().getSelectedItem();
		HashMap<TreeItem<String>, String> treeMap = engagementActionController.getTreeItemActivityIdTreeMap();
		for (TreeItem<String> treeItem : treeMap.keySet()) {
			String treeItemValue = treeItem.getValue();
			if (treeItemValue.contentEquals(selectedActivity.getLongName())) { // if tree item value matches activity name
				String treeItemActivityID = treeMap.get(treeItem);
				if (treeItemActivityID.contentEquals(selectedActivity.getActivityID())) { // if tree item GUID matches
					Chapter treeItemChapter = engagementActionController.getChapterForNameInGoal(treeItem.getParent().getValue(), engagementActionController.getCurrentGoal());
					if (String.valueOf(treeItemChapter.getChapterNum()).contentEquals(selectedActivity.getChapterAssignment())) {
						engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
						engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
					}
				}
			}
		}
	}
	
	private void planSelectedInList() {
		Activity activity = planListView.getSelectionModel().getSelectedItem();
		HashMap<TreeItem<String>, String> treeMap = engagementActionController.getTreeItemActivityIdTreeMap();
		for (TreeItem<String> treeItem : treeMap.keySet()) {
			String treeItemValue = treeItem.getValue();
			if (treeItemValue.contentEquals(activity.getLongName())) { // if tree item value matches activity name
				String treeItemActivityID = treeMap.get(treeItem);
				if (treeItemActivityID.contentEquals(activity.getActivityID())) { // if tree item GUID matches
					Chapter treeItemChapter = engagementActionController.getChapterForNameInGoal(treeItem.getParent().getValue(), engagementActionController.getCurrentGoal());
					if (treeItemChapter.getChapterNum() == chapter.getChapterNum()) {
						engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
						engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
					}
				}
			}
		}
	}
	
	private void reflectSelectedInList() {
		Activity activity = reflectListView.getSelectionModel().getSelectedItem();
		HashMap<TreeItem<String>, String> treeMap = engagementActionController.getTreeItemActivityIdTreeMap();
		for (TreeItem<String> treeItem : treeMap.keySet()) {
			String treeItemValue = treeItem.getValue();
			if (treeItemValue.contentEquals(activity.getLongName())) { // if tree item value matches activity name
				String treeItemActivityID = treeMap.get(treeItem);
				if (treeItemActivityID.contentEquals(activity.getActivityID())) { // if tree item GUID matches
					Chapter treeItemChapter = engagementActionController.getChapterForNameInGoal(treeItem.getParent().getValue(), engagementActionController.getCurrentGoal());
					if (treeItemChapter.getChapterNum() == chapter.getChapterNum()) {
						engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
						engagementActionController.treeViewClicked(null, treeItem); // handle tree item selected
					}
				}
			}
		}
	}
	
	private void removeAboutPanel() {
		if(mainPanel.getChildren().contains(aboutPanel)) {
			mainPanel.getChildren().remove(aboutPanel);
		}
	}
	
	private void addAboutPanel() {
		if(!mainPanel.getChildren().contains(aboutPanel)) {
			mainPanel.getChildren().add(0,aboutPanel);
		}
	}
	
	private void removePlanPanel() {
		if(mainPanel.getChildren().contains(planPanel)) {
			mainPanel.getChildren().remove(planPanel);
		}
	}
	
	private void addPlanPanel() {
		if(!mainPanel.getChildren().contains(planPanel)) {
			int index = mainPanel.getChildren().indexOf(activitiesPanel);
			mainPanel.getChildren().add(index -1, planPanel);
		}
	}
	
	private void removeReflectPanel() {
		if(mainPanel.getChildren().contains(reflectPanel)) {
			mainPanel.getChildren().remove(reflectPanel);
		}
	}
	
	private void addReflectPanel() {
		if(!mainPanel.getChildren().contains(reflectPanel)) {
			int index = mainPanel.getChildren().indexOf(activitiesPanel);
			mainPanel.getChildren().add(index + 1, reflectPanel);
		}
	}
	
	private void cleanActivitiesListView() {
		activitiesListView.getItems().clear();
	}
	
	private void cleanPlanListView() {
		planListView.getItems().clear();
	}

	private void cleanReflectListView() {
		reflectListView.getItems().clear();
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

	public ArrayList<Chapter> getListOfAllChapters() {
		return listOfAllChapters;
	}

	public void setListOfAllChapters(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}

}
