package com.epa.erb.goal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
	ListView<Goal> selectedGoalsListView;

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
	

	
	@FXML
	public void addButtonAction() {
		if(hasGoalUserInput()) {
			ArrayList<CheckBox> selectedGoalCheckBoxes = getSelectedGoalCategoryCheckBoxes();
			if(selectedGoalCheckBoxes.size() > 0) {
				String goalName = getGoalName();
				String goalDescription = getGoalDescription();
				ArrayList<GoalCategory> selectedGoalCategories = getSelectedGoalCategories();
				Goal goal = new Goal(goalName, goalDescription, selectedGoalCategories);
				addGoalToSelectedGoalsListView(goal);
				cleanGoalUserInputFields();
				uncheckAllGoalCategoryCheckBoxes();
			}
		} else {
			showGoalUserInputNeededAlert();
		}
	}
		
	@FXML
	public void doneButtonAction() {
		Optional<ButtonType> result = showDoneAlert();
		if(result.get() == ButtonType.OK) {
			//TODO: Store all goals in project
			
		}
	}
	
	private void cleanGoalUserInputFields() {
		goalNameTextField.setText(null);
		goalDescriptionTextArea.setText(null);
	}
	
	private void uncheckAllGoalCategoryCheckBoxes() {
		for (int i = 0; i < goalsVBox.getChildren().size(); i++) {
			CheckBox goalCheckBox = (CheckBox) goalsVBox.getChildren().get(i);
			goalCheckBox.setSelected(false);
		}
	}
	
	private Optional<ButtonType> showDoneAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText("Selecting goals can only be done once. Are you sure you'd like to continue?");
		alert.setTitle("Alert");
		Optional<ButtonType> result = alert .showAndWait();
		return result;
	}
	
	private void addGoalToSelectedGoalsListView(Goal goal) {
		if(!selectedGoalsListView.getItems().contains(goal)) {
			selectedGoalsListView.getItems().add(goal);
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
	
	private ArrayList<GoalCategory> getSelectedGoalCategories() {
		ArrayList<GoalCategory> selectedGoalCategories = new ArrayList<GoalCategory>();
		ArrayList<CheckBox> selectedGoalCheckBoxes = getSelectedGoalCategoryCheckBoxes();
		for (CheckBox goalCategoryCheckBox : selectedGoalCheckBoxes) {
			String goalCategoryName = goalCategoryCheckBox.getText();
			GoalCategory goalCategory = getGoalCategory(goalCategoryName);
			selectedGoalCategories.add(goalCategory);
		}
		return selectedGoalCategories;
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
	
	private void setSelectedGoalsListViewCellFactory() {
		selectedGoalsListView.setCellFactory(new Callback<ListView<Goal>, ListCell<Goal>>() {
			@Override
			public ListCell<Goal> call(ListView<Goal> param) {
				ListCell<Goal> cell = new ListCell<Goal>() {
					@Override
					protected void updateItem(Goal item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getGoalName());
							setContextMenu(createGoalContextMenu());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private boolean hasGoalUserInput() {
		String goalName = goalNameTextField.getText();
		String goalDescription = goalDescriptionTextArea.getText();
		if(goalName != null && goalDescription != null) {
			if(goalName.trim().length() > 0 && goalDescription.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}
	
	private void showGoalUserInputNeededAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText("Please enter a valid goal name and description.");
		alert.setTitle("Alert");
		alert.showAndWait();
	}
	
	private ContextMenu createGoalContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem removeMenuItem = new MenuItem("Remove");
		removeMenuItem.setOnAction(e-> removeSelectedGoal(e));
		contextMenu.getItems().add(removeMenuItem);
		return contextMenu;
	}
	
	private void removeSelectedGoal(Event e) {
		Goal goalToRemove = getGoalListViewSelectedGoal();
		selectedGoalsListView.getItems().remove(goalToRemove);
		setSelectedGoalsListViewCellFactory();
	}
	
	private String getGoalName() {
		String goalName = goalNameTextField.getText();
		return goalName;
	}
	
	private String getGoalDescription() {
		String goalDescription = goalDescriptionTextArea.getText();
		return goalDescription;
	}
	
	private Goal getGoalListViewSelectedGoal() {
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
