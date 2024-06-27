package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import com.epa.erb.utility.Constants;


public class MappingController implements Initializable{

	@FXML
	WebView webView;
	
	private App app;
	public MappingController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadHTMLFileToWebEngine();
		
	}
	
	private void loadHTMLFileToWebEngine() {
		Constants constants = new Constants();
		File mappingDirectory = new File(constants.getPathToERBDirectory() + File.separator + "JavaScript" + File.separator + "Mapping");

		File HTMLFile = new File(mappingDirectory + File.separator + "leaflet.html");
		
		if (HTMLFile.exists()) {
			webView.getEngine().load(HTMLFile.toURI().toString());
			webView.getEngine().reload();
		}
	}

}
