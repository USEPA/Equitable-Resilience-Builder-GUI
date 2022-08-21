package com.epa.erb.form_activities;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.Activity;

import javafx.fxml.Initializable;

public class Controller14 implements Initializable{

	private Activity activity;
	public Controller14(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
}
