package com.epa.erb.intro_panels;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.App;
import com.epa.erb.ERBContainerController;
import com.epa.erb.ERBLandingNew2Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class HowERBMadeController implements Initializable{

	private App app;
	private ERBLandingNew2Controller erbLandingNew2Controller;
	public HowERBMadeController(App app, ERBLandingNew2Controller erbLandingNew2Controller) {
		this.app = app;
		this.erbLandingNew2Controller = erbLandingNew2Controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	@FXML
	public void equitableResilienceHyperlinkClicked() {
		ERBContainerController erbContainerController = new ERBContainerController(app);
		erbContainerController.glossaryMenuItemAction();
	}
	
	@FXML
	public void equityAndResilienceHyperlinkClicked() {
		erbLandingNew2Controller.equityAndResilienceHyperlinkClicked();
	}

}
