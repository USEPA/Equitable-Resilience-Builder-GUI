package com.epa.erb.form_activities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FormContentController implements Initializable{
	
	@FXML
	TextFlow formTextFlow;
	@FXML
	VBox linksVBox;
	
	private App app;
	private Project project;
	private Goal goal;
	private Activity activity;
	public FormContentController(App app, Project project, Goal goal, Activity activity) {
		this.app = app;
		this.project = project;
		this.goal = goal;
		this.activity = activity;
	}
	
	private FileHandler fileHandler = new FileHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);

		File xmlContentFileToParse = fileHandler.getActivityFormContentXML(project, goal, activity);
		ArrayList<Text> textBlocks = xmlManager.parseFormContentXML(xmlContentFileToParse);
		addContent(textBlocks);
		
		File xmlLinksFileToParse = fileHandler.getActivityFormLinksXML(project, goal, activity);
		ArrayList<Hyperlink> links = xmlManager.parseFormHyperlinkXML(xmlLinksFileToParse);
		addLinks(links);
		
	}
	
	public void addContent(ArrayList<Text> textBlocks) {
		if(textBlocks != null) {
			for(Text textBlock: textBlocks) {
				formTextFlow.getChildren().add(textBlock);
			}
		}
	}
	
	public void addLinks(ArrayList<Hyperlink> links) {
		if(links != null) {
			for(Hyperlink hyperlink: links) {
				linksVBox.getChildren().add(hyperlink);
			}
		}
	}

	public TextFlow getFormTextFlow() {
		return formTextFlow;
	}

}
