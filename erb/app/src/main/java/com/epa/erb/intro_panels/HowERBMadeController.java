package com.epa.erb.intro_panels;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.ERBContainerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class HowERBMadeController implements Initializable{

	private App app;
	private IntroPanelLoader introPanelLoader;
	public HowERBMadeController(App app, IntroPanelLoader introPanelLoader) {
		this.app = app;
		this.introPanelLoader = introPanelLoader;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	@FXML
	public void equitableResilienceHyperlinkClicked() {
		ERBContainerController erbContainerController = new ERBContainerController(app);
		//erbContainerController.glossaryMenuItemAction();
	}
	
	@FXML
	public void equityAndResilienceHyperlinkClicked() {
		introPanelLoader.loadEquityAndResiliencePanel();;
	}

}
