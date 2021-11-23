package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class ProgressTrackerController implements Initializable{

	@FXML
	VBox progressTrackerVBox;
	@FXML
	ListView<String> goalsListView;
	@FXML
	Button addNewGoalButton;
	@FXML
	Button addNewTaskButton;
	@FXML
	VBox chapter1TasksVBox;
	@FXML
	VBox chapter2TasksVBox;
	@FXML
	VBox chapter3TasksVBox;
	@FXML
	VBox chapter4TasksVBox;
	@FXML
	VBox chapter5TasksVBox;
	
	Scene scene;
	
	TaskTracker taskTracker;
	GoalTracker goalTracker;
	
	public ProgressTrackerController(TaskTracker taskTracker, GoalTracker goalTracker) {
		this.taskTracker = taskTracker;
		this.goalTracker = goalTracker;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scene = new Scene(progressTrackerVBox);
	}
	
	@FXML
	public void addNewGoalButtonAction() {
		
	}
	
	@FXML
	public void addNewTaskButtonAction() {
		
	}
	
	public Scene getScene() {
		return scene;
	}

}
