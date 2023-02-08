package com.epa.erb.wordcloud;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class WordCloudSaveController implements Initializable{

	@FXML
	TextField wordCloudNameTextField;
	@FXML
	HBox errorHBox;
	
	private Stage stage;
	private String wordCloudSaveName="";
	private FileHandler fileHandler = new FileHandler();

	
	private Project project;
	private Goal goal;
	private String guid;
	public WordCloudSaveController(Project project, Goal goal, String guid) {
		this.project = project;
		this.goal = goal;
		this.guid = guid;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		errorHBox.setVisible(false);
	}
	
	@FXML
	public void okButtonAction() {
		String userInput = wordCloudNameTextField.getText();
		boolean nameExists = nameAlreadyExists(userInput);
		if(nameExists) {
			errorHBox.setVisible(true);
		} else {
			errorHBox.setVisible(false);
			wordCloudSaveName = userInput;
			stage.close();
		}
	}
	
	public boolean nameAlreadyExists(String name) {
		File guidDataDirectory = fileHandler.getGUIDDataDirectory(project, goal);
		File guidDirectory = new File(guidDataDirectory.getPath() + "\\" + guid);
		for(File dir : guidDirectory.listFiles()) {
			if(dir.isDirectory()) {
				if(dir.getName().contentEquals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String getWordCloudSaveName() {
		return wordCloudSaveName;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}	

}
