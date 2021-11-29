package erb;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
		
	TaskTracker taskTracker;
	GoalTracker goalTracker;
	
	public ProgressTrackerController(TaskTracker taskTracker, GoalTracker goalTracker) {
		this.taskTracker = taskTracker;
		this.goalTracker = goalTracker;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parseStaticGoals();
		parseStaticTasks();
	}
	
	public void parseStaticGoals() {
		ParserJSON parserJSON = new ParserJSON();
		ArrayList<JSONObject> jsonObjects = parserJSON.parseFile("C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\Dev_Docs\\Eclipse_Repo\\Clone_11_17_2021\\MetroCERI\\erb_supporting_docs\\User\\Sessions\\Session_Example\\Goals\\Goals.json", "goals");
		for(JSONObject jsonObject: jsonObjects) {
			ERBGoal erbGoal = new ERBGoal();
			erbGoal.setId(Integer.parseInt(jsonObject.get("id").toString()));
			erbGoal.setTitle(jsonObject.get("title").toString());
			erbGoal.setStatic(Boolean.parseBoolean(jsonObject.get("isStatic").toString()));
			erbGoal.setDescription(jsonObject.get("description").toString());
			erbGoal.setChapterAssignment(jsonObject.get("chapterAssignment").toString());
			goalTracker.addGoal(erbGoal);
		}
	}
	
	public void parseStaticTasks() {
		ParserJSON parserJSON = new ParserJSON();
		ArrayList<JSONObject> jsonObjects = parserJSON.parseFile("C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\Dev_Docs\\Eclipse_Repo\\Clone_11_17_2021\\MetroCERI\\erb_supporting_docs\\User\\Sessions\\Session_Example\\Tasks\\Tasks.json", "tasks");
		for(JSONObject jsonObject: jsonObjects) {
			ERBTask erbTask = new ERBTask();
			erbTask.setId(Integer.parseInt(jsonObject.get("id").toString()));
			erbTask.setTitle(jsonObject.get("title").toString());
			erbTask.setStatic(Boolean.parseBoolean(jsonObject.get("isStatic").toString()));
			erbTask.setDescription(jsonObject.get("description").toString());
			erbTask.setChapterAssignment(jsonObject.get("chapterAssignment").toString());
			taskTracker.addTask(erbTask);
		}
	}
	
	@FXML
	public void addNewGoalButtonAction() {
		
	}
	
	@FXML
	public void addNewTaskButtonAction() {
		
	}
}
