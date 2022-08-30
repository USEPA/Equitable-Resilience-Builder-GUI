package com.epa.erb.form_activities;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MakeProjectPlan01Controller implements Initializable{

	private App app;
	public MakeProjectPlan01Controller(App app) {
		this.app = app;
	}
	
	private FileHandler fileHandler = new FileHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void activity01HyperlinkAction() {
		Activity activity01 = app.getActivityByID("01", app.getActivities());
		File activity01Docx = fileHandler.getGlobalActivityWordDoc(activity01);
		fileHandler.openFile(activity01Docx);
	}
	
	
	@FXML
	public void activity04HyperlinkAction() {
		Activity activity04 = app.getActivityByID("04", app.getActivities());
		File activity04Docx = fileHandler.getGlobalActivityWordDoc(activity04);
		fileHandler.openFile(activity04Docx);
	}
	
	
	

}
