package com.epa.erb.worksheet;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

public class WorksheetContentController implements Initializable{

	@FXML
	Label activityNameLabel;
	@FXML
	WebView webView;
	@FXML
	Button openButton;
	@FXML
	Button saveButton;
	@FXML
	Button printButton;
	@FXML
	Label whoLabel;
	@FXML
	Label timeLabel;
	@FXML
	Label statusLabel;
		
	Activity activity;
	
	public WorksheetContentController(Activity activity) {
		this.activity = activity;
	}
	
	private Logger logger = LogManager.getLogger(WorksheetContentController.class);
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			File htmlFile = new File(pathToERBFolder + "\\Javascript\\PDFViewer\\index.html");
			webView.getEngine().setOnError(e-> System.out.println("ERROR: " + e.getMessage()));
			webView.getEngine().setOnAlert(e-> System.out.println("ALERT: " + e.getData()));
			webView.getEngine().setJavaScriptEnabled(true);
			webView.getEngine().load(htmlFile.toURI().toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		fillActivityInfo();
	}
	
	private void fillActivityInfo() {
		whoLabel.setText(activity.getWho());
		timeLabel.setText(activity.getTime());
		statusLabel.setText(activity.getStatus());
		activityNameLabel.setText(activity.getLongName());
	}

}
