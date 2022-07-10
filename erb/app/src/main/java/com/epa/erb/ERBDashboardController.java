package com.epa.erb;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.worksheet.WorksheetContentController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ERBDashboardController implements Initializable{

	@FXML
	TreeView<String> treeView;
	@FXML
	VBox webViewVBox;
	
	private App app;
	public ERBDashboardController(App app) {
		this.app = app;
	}
	
	private ArrayList<TreeItem<String>> chapterTreeItems = new ArrayList<TreeItem<String>>();
	private ArrayList<TreeItem<String>> activityTreeItems = new ArrayList<TreeItem<String>>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TreeItem<String> rootTreeItem = new TreeItem<String>("ERB");
		rootTreeItem.setExpanded(true);
		treeView.setRoot(rootTreeItem);
		addChapterTreeViewItems(rootTreeItem);
		addActivityTreeViewItems();
		
		treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> treeViewClicked(oldValue, newValue));

	}
	
	private void treeViewClicked(TreeItem<String> oldVal, TreeItem<String> newVal) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String value = newVal.getValue();
				if(!value.contentEquals("ERB")) {
					if(value.contains("Chapter")) {
						Chapter chapter = getChapter(value);
						fillChapterInfo(chapter);
					} else {
						Activity activity = getActivity(value);
						fillActivityInfo(activity);
					}
				}
			}
		});

	}
	
	@FXML
	public void continueButtonAction() {
		app.launchERBLanding(false);
	}
	
	private void addChapterTreeViewItems(TreeItem<String> rootTreeItem) {
		for(Chapter chapter: app.getChapters()) {
			TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
			chapterTreeItem.setExpanded(true);
			chapterTreeItems.add(chapterTreeItem);
			rootTreeItem.getChildren().add(chapterTreeItem);
		}
	}
	
	private void addActivityTreeViewItems() {
		for (Activity activity : app.getActivities()) {
			if (activity.getActivityType().getLongName().contentEquals("Worksheet")) {
				TreeItem<String> activityTreeItem = new TreeItem<String>(activity.getLongName());
				activityTreeItems.add(activityTreeItem);
				TreeItem<String> chapterTreeItem = getChapterTreeItem(activity);
				if (chapterTreeItem != null) {
					chapterTreeItem.getChildren().add(activityTreeItem);
				}
			}
		}
	}
	
	private TreeItem<String> getChapterTreeItem(Activity activity) {
		for(TreeItem<String> chapterTreeItem: chapterTreeItems) {
			if(chapterTreeItem.getValue().contains(activity.getChapterAssignment())) {
				return chapterTreeItem;
			}
		}
		return null;
	}
	
	private Activity getActivity(String activityName) {
		for(Activity activity: app.getActivities()) {
			if(activity.getLongName().contentEquals(activityName)) {
				return activity;
			}
		}
		return null;
	}
	
	private Chapter getChapter(String chapterName) {
		for(Chapter chapter: app.getChapters()) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		return null;
	}
	
	private void fillChapterInfo(Chapter chapter) {
		webViewVBox.getChildren().clear();
	}
	
	private void fillActivityInfo(Activity activity) {
		webViewVBox.getChildren().clear();
		Parent worksheetContentRoot = loadWorksheetContent(activity);
		VBox.setVgrow(worksheetContentRoot, Priority.ALWAYS);
		webViewVBox.getChildren().add(worksheetContentRoot);
		
	}
	
	private Parent loadWorksheetContent(Activity activity) {
		if (activity != null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/worksheet/WorksheetContent.fxml"));
				WorksheetContentController worksheetContentController = new WorksheetContentController(activity);
				fxmlLoader.setController(worksheetContentController);
				VBox root = fxmlLoader.load();
				worksheetContentController.hideFileOptionsHBox();
				return root;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}
	
}
