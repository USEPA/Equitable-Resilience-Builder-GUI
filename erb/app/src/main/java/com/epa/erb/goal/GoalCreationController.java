package com.epa.erb.goal;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class GoalCreationController implements Initializable{

	@FXML
	VBox goalsVBox;
	@FXML
	TextField goalNameTextField;
	@FXML
	TextArea goalDescriptionTextArea;
	@FXML
	ListView<GoalCategory> selectedGoalsListView;

	private App app;
	public GoalCreationController(App app) {
		this.app = app;
	}
	
	private Logger logger = LogManager.getLogger(GoalCreationController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateGoalCategoryCheckBoxes();
		setSelectedGoalsListViewCellFactory();
	}
	
	private void populateGoalCategoryCheckBoxes() {
		ArrayList<GoalCategory> goalCategories = app.getGoalCategories();
		for(GoalCategory goalCategory : goalCategories) {
			CheckBox goalCategoryCheckBox = createGoalCategoryCheckBox(goalCategory);
			goalsVBox.getChildren().add(goalCategoryCheckBox);
		}
	}
	
	private CheckBox createGoalCategoryCheckBox(GoalCategory goalCategory) {
		CheckBox checkBox = new CheckBox(goalCategory.getCategoryName());
		checkBox.setFont(new Font(15.0));
		return checkBox;
	}
	
	private ArrayList<CheckBox> getSelectedGoalCategoryCheckBoxes() {
		ArrayList<CheckBox> selectedGoalCheckBoxes = new ArrayList<CheckBox>();
		for(int i =0; i < goalsVBox.getChildren().size(); i++) {
			CheckBox goalCheckBox = (CheckBox) goalsVBox.getChildren().get(i);
			if(goalCheckBox.isSelected()) {
				selectedGoalCheckBoxes.add(goalCheckBox);
			}
		}
		return selectedGoalCheckBoxes;
	}
	
	@FXML
	public void addButtonAction() {
		ArrayList<CheckBox> selectedGoalCheckBoxes = getSelectedGoalCategoryCheckBoxes();
		if(selectedGoalCheckBoxes.size() > 0) {
			for(CheckBox goalCategoryCheckBox : selectedGoalCheckBoxes) {
				String goalCategoryName = goalCategoryCheckBox.getText();
				GoalCategory goalCategory = getGoalCategory(goalCategoryName);
				addGoalCategoryToSelectedGoalsListView(goalCategory);
			}
		}
	}
		
	@FXML
	public void doneButtonAction() {
		
	}
	
	private void addGoalCategoryToSelectedGoalsListView(GoalCategory goalCategory) {
		if(!selectedGoalsListView.getItems().contains(goalCategory)) {
			selectedGoalsListView.getItems().add(goalCategory);
		}
	}
	
	private GoalCategory getGoalCategory(String goalCategoryName) {
		ArrayList<GoalCategory> goalCategories = app.getGoalCategories();
		for(GoalCategory goalCategory : goalCategories) {
			if(goalCategory.getCategoryName().contentEquals(goalCategoryName)) {
				return goalCategory;
			}
		}
		logger.debug("Goal Category returned is null.");
		return null;
	}
	
	private void setSelectedGoalsListViewCellFactory() {
		selectedGoalsListView.setCellFactory(new Callback<ListView<GoalCategory>, ListCell<GoalCategory>>() {
			@Override
			public ListCell<GoalCategory> call(ListView<GoalCategory> param) {
				ListCell<GoalCategory> cell = new ListCell<GoalCategory>() {
					@Override
					protected void updateItem(GoalCategory item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getCategoryName());
							setContextMenu(createSelectedGoalCategoryContextMenu());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private ContextMenu createSelectedGoalCategoryContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem removeMenuItem = new MenuItem("Remove");
		removeMenuItem.setOnAction(e-> removeSelectedGoalCategory(e));
		contextMenu.getItems().add(removeMenuItem);
		return contextMenu;
	}
	
	private void removeSelectedGoalCategory(Event e) {
		GoalCategory goalCategoryToRemove = getGoalCategoryListViewSelectedGoalCategory();
		selectedGoalsListView.getItems().remove(goalCategoryToRemove);
		setSelectedGoalsListViewCellFactory();
	}
	
	private GoalCategory getGoalCategoryListViewSelectedGoalCategory() {
		return selectedGoalsListView.getSelectionModel().getSelectedItem();
	}

	public VBox getGoalsVBox() {
		return goalsVBox;
	}

	public TextField getGoalNameTextField() {
		return goalNameTextField;
	}

	public TextArea getGoalDescriptionTextArea() {
		return goalDescriptionTextArea;
	}

	public ListView<?> getSelectedGoalsListView() {
		return selectedGoalsListView;
	}

}
