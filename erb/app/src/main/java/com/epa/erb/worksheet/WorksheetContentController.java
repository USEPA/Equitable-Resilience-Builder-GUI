package com.epa.erb.worksheet;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.value.ChangeListener;
import com.epa.erb.Activity;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;

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
		
	Activity activity;
	public WorksheetContentController(Activity activity) {
		this.activity = activity;
	}
	
	private Logger logger = LogManager.getLogger(WorksheetContentController.class);
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPDFToWebView();
		fillActivityInfo();
		handleControls();
	}
	
	private void handleControls() {
		saveButton.setDisable(true);
	}
	
	private void loadPDFToWebView() {
		try {
			WebEngine webEngine = webView.getEngine();
			String url = getClass().getResource("/pdfjs-2.8.335-dist/web/viewer.html").toExternalForm();
			webEngine.setJavaScriptEnabled(true);
			webEngine.load(url);
			webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
				@Override
				public void changed(ObservableValue ov, State oldState, State newState) {
					if (newState == Worker.State.SUCCEEDED) {
						try {
							if(activity.getFileName() != null && activity.getFileName().length() > 0) {
								String fileName = activity.getFileName().replace(".docx", ".pdf");
								byte[] data = FileUtils.readFileToByteArray(new File(pathToERBFolder + "\\Activities\\ChapterActivities_PDF\\" + fileName));
								String base64 = Base64.getEncoder().encodeToString(data);
								webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void fillActivityInfo() {
		activityNameLabel.setText(activity.getLongName());
	}

	@FXML
	public void printButtonAction() {
//		PrinterJob printerJob = PrinterJob.createPrinterJob(Printer.getDefaultPrinter());
//		File file = new File(pathToERBFolder + "\\Activities\\ChapterActivities\\3_Mapping.docx");
	}
	
	@FXML
	public void saveButtonAction() {
		
	}
	
	@FXML
	public void openButtonAction() {
		try {
			String fileName = activity.getFileName();
			File file = new File(pathToERBFolder + "\\Activities\\ChapterActivities\\" + fileName);
			if (file.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				logger.error(file.getPath() + " either does not exist or desktop is not supported.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
