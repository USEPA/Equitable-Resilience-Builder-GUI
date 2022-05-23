package com.epa.erb.goal;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.XMLManager;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class GoalCreationController implements Initializable{

	@FXML
	VBox goalsVBox;
	@FXML
	TextField goalNameTextField;
	@FXML
	TextArea goalDescriptionTextArea;
	@FXML
	ListView<Goal> goalsListView;

	private App app;
	private Project project;
	private ProjectSelectionController projectSelectionController;
	public GoalCreationController(App app, Project project, ProjectSelectionController projectSelectionController) {
		this.app = app;
		this.project = project;
		this.projectSelectionController = projectSelectionController;
	}
	
	private Logger logger = LogManager.getLogger(GoalCreationController.class);
	//private String pathToERBProjectsFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\Projects\\").replace("\\", "\\\\");
	private String pathToERBProjectsFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\Projects";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateGoalCategoryCheckBoxes();
		setGoalsListViewCellFactory();
	}

	@FXML
	public void addButtonAction() {
		if(hasGoalUserInput()) {
			ArrayList<CheckBox> selectedGoalCheckBoxes = getSelectedGoalCategoryCheckBoxes();
			if(selectedGoalCheckBoxes.size() > 0) {
				String goalName = getGoalName();
				String goalDescription = getGoalDescription();
				ArrayList<GoalCategory> selectedGoalCategories = getSelectedGoalCategories();
				Goal goal = new Goal(app,goalName, goalDescription, selectedGoalCategories);
				goal.setChapters(app.getActivities(), project.getProjectName());
				addGoalToGoalsListView(goal);
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
			ArrayList<Goal> createdGoals = getCreatedGoals();
			project.setProjectGoals(createdGoals);
			writeProjectMetaData(project);
			writeGoalsMetaData(createdGoals);
			loadEngagementActionTool(project);
			projectSelectionController.closeGoalCreationStage();
			app.closeProjectSelectionStage();
		}
	}
	
	private void loadEngagementActionTool(Project project) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			EngagementActionController engagementActionController = new EngagementActionController(app, project);
			fxmlLoader.setController(engagementActionController);
			Parent root = fxmlLoader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("ERB");
			stage.show();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void writeProjectMetaData(Project project) {
		XMLManager xmlManager = new XMLManager(app);
		xmlManager.writeProjectMetaXML(getProjectMetaFile(project), project);
	}
	
	private void writeGoalsMetaData(ArrayList<Goal> goals) {
		XMLManager xmlManager = new XMLManager(app);
		createGoalsDirectory();
		for(Goal goal : goals) {
			File goalDirectory = createGoalDirectory(goal);
			File goalMetaFile = new File(goalDirectory.getPath() + "\\Meta.xml");
			ArrayList<Chapter> chapters = goal.getChapters();
			xmlManager.writeGoalMetaXML(goalMetaFile, chapters);
		}
	}
	
	private File createGoalDirectory(Goal goal) {
		File goalDirectory = new File(pathToERBProjectsFolder +  "\\" + project.getProjectName() + "\\Goals\\" + goal.getGoalName());
		if(!goalDirectory.exists()) {
			goalDirectory.mkdir();
		}
		return goalDirectory;
	}
	
	private void createGoalsDirectory() {
		File goalDirectory = new File(pathToERBProjectsFolder +  "\\" + project.getProjectName() + "\\Goals");
		if(!goalDirectory.exists()) {
			goalDirectory.mkdir();
		}
	}
	
	private File getProjectMetaFile(Project project) {
		File projectMetaFile = new File(pathToERBProjectsFolder + "\\" + project.getProjectName() + "\\Meta.xml");
		return projectMetaFile;
	}
	
	private ArrayList<Goal> getCreatedGoals(){
		ArrayList<Goal> createdGoals = new ArrayList<Goal>();
		for(Goal goal: goalsListView.getItems()) {
			createdGoals.add(goal);
		}
		return createdGoals;
	}
	
	private void populateGoalCategoryCheckBoxes() {
		ArrayList<GoalCategory> goalCategories = app.getGoalCategories();
		for(GoalCategory goalCategory : goalCategories) {
			CheckBox goalCategoryCheckBox = createGoalCategoryCheckBox(goalCategory);
			goalsVBox.getChildren().add(goalCategoryCheckBox);
		}
	}
	
	private void cleanGoalUserInputFields() {
		goalNameTextField.setText(null);
		goalDescriptionTextArea.setText(null);
	}
	
	private void uncheckAllGoalCategoryCheckBoxes() {
		for (int i = 0; i < goalsVBox.getChildren().size(); i++) {
			CheckBox goalCheckBox = (CheckBox) goalsVBox.getChildren().get(i);
			goalCheckBox.setWrapText(true);
			goalCheckBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
			goalCheckBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
			goalCheckBox.setSelected(false);
		}
	}
	
	private CheckBox createGoalCategoryCheckBox(GoalCategory goalCategory) {
		CheckBox checkBox = new CheckBox(goalCategory.getCategoryName());
		checkBox.setWrapText(true);
		checkBox.setFont(new Font(15.0));
		return checkBox;
	}
	
	private Optional<ButtonType> showDoneAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText("Creating goals can only be done once. Are you sure you'd like to continue?");
		alert.setTitle("Alert");
		Optional<ButtonType> result = alert .showAndWait();
		return result;
	}
	
	private void addGoalToGoalsListView(Goal goal) {
		if(!goalsListView.getItems().contains(goal)) {
			goalsListView.getItems().add(goal);
		}
		setGoalsListViewCellFactory();
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
	
	private void setGoalsListViewCellFactory() {
		goalsListView.setCellFactory(new Callback<ListView<Goal>, ListCell<Goal>>() {
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
		goalsListView.getItems().remove(goalToRemove);
		setGoalsListViewCellFactory();
	}
	
	private String getGoalName() {
		String goalName = goalNameTextField.getText().trim();
		return goalName;
	}
	
	private String getGoalDescription() {
		String goalDescription = goalDescriptionTextArea.getText();
		return goalDescription;
	}
	
	private Goal getGoalListViewSelectedGoal() {
		return goalsListView.getSelectionModel().getSelectedItem();
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

	public ListView<?> getGoalsListView() {
		return goalsListView;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProjectSelectionController getProjectSelectionController() {
		return projectSelectionController;
	}

	public void setProjectSelectionController(ProjectSelectionController projectSelectionController) {
		this.projectSelectionController = projectSelectionController;
	}

}
