package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class ERBContainer_Draft1Controller implements Initializable{

	@FXML
	VBox erbContainer;
	
	private App app;
	public ERBContainer_Draft1Controller(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	public VBox getErbContainer() {
		return erbContainer;
	}

}
