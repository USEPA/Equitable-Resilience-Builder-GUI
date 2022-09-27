package com.epa.erb.intro_panels;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.App;
import com.epa.erb.ERBContainerController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class HowDoesItWorkController implements Initializable{

	private App app;
	public HowDoesItWorkController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void equitableResilienceHyperlinkClicked() {
		ERBContainerController erbContainerController = new ERBContainerController(app);
		//erbContainerController.glossaryMenuItemAction();
	}

}
