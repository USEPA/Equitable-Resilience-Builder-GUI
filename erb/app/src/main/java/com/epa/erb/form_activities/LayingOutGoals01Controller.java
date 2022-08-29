package com.epa.erb.form_activities;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class LayingOutGoals01Controller implements Initializable{

	private App app;
	public LayingOutGoals01Controller(App app) {
		this.app = app;
	}
	
	private FileHandler fileHandler = new FileHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void activity06HyperlinkAction() {
		Activity activity06 = app.getActivityByID("06", app.getActivities());
		File activity06Docx = fileHandler.getGlobalActivityWordDoc(activity06);
		fileHandler.openFile(activity06Docx);
	}
	
	@FXML
	public void activity03HyperlinkAction() {
		Activity activity03 = app.getActivityByID("03", app.getActivities());
		File activity03Docx = fileHandler.getGlobalActivityWordDoc(activity03);
		fileHandler.openFile(activity03Docx);
	}	

	
}
