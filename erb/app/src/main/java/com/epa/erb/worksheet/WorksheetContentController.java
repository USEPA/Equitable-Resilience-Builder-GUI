package com.epa.erb.worksheet;

import java.awt.Desktop;
//import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.pdfbox.Loader;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.printing.PDFPageable;
import javafx.beans.value.ChangeListener;
import com.epa.erb.Activity;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;

public class WorksheetContentController implements Initializable{

	@FXML
	WebView webView;
	@FXML
	Button saveButton;
	@FXML
	Label activityNameLabel;

	private Activity activity;
	public WorksheetContentController(Activity activity) {
		this.activity = activity;
	}
	
	private Logger logger = LogManager.getLogger(WorksheetContentController.class);
	//private String pathToERBStaticDataFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBStaticDataFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\Static_Data";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setActivityNameLabelText();
		loadPDFToWebView(getPDFFileToLoad());
	}
	
	private void handleControls() {
		saveButton.setDisable(true);
	}
	
	private File getPDFFileToLoad() {
		String fileName = activity.getFileName().replace(".docx", ".pdf");
		File pdfFileToLoad = new File(pathToERBStaticDataFolder + "\\Activities\\ChapterActivities_PDF\\" + fileName);
		return pdfFileToLoad;
	}
	
	private void loadPDFToWebView(File pdfFileToLoad) {
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
								byte[] data = FileUtils.readFileToByteArray(pdfFileToLoad);
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
	
	private void setActivityNameLabelText() {
		activityNameLabel.setText(activity.getLongName());
	}

	@FXML
	public void printButtonAction() {
		loadPrinterSelection();
	}
	
	private Stage printerSelectionStage = null;
	private void loadPrinterSelection() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/worksheet/PrinterSelection.fxml"));
			PrinterSelectionController printerSelectionController = new PrinterSelectionController(this);
			fxmlLoader.setController(printerSelectionController);
			Parent root = fxmlLoader.load();
			printerSelectionStage = new Stage();
			printerSelectionStage.setTitle("Printer Selection");
			Scene scene = new Scene(root);
			printerSelectionStage.setScene(scene);
			printerSelectionStage.showAndWait();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	void closePrinterStage() {
		if(printerSelectionStage != null) {
			printerSelectionStage.close();
		}
	}
	
	void handlePrinterSelected(PrintService printService) {
		File fileToPrint = getFileToPrint();
		printActivity(printService, fileToPrint);
	}
	
	private File getFileToPrint() {
		String fileName = activity.getFileName().replace(".docx", ".pdf");
		File file = new File(pathToERBStaticDataFolder + "\\Activities\\ChapterActivities_PDF\\" + fileName);
		if(file.exists()) {
			return file;
		} else {
			return null;
		}
	}
	
	void printActivity(PrintService printService, File fileToPrint) {
		try {
			FileInputStream fileInputStream = new FileInputStream(fileToPrint.getPath());
			PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
			attributeSet.add(new Copies(1));
//			PDDocument pdDocument = Loader.loadPDF(fileInputStream);
//			PrinterJob printerJob = PrinterJob.getPrinterJob();
//			printerJob.setPrintService(printService);
//			printerJob.setPageable(new PDFPageable(pdDocument));
//			printerJob.print();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
		
	@FXML
	public void saveButtonAction() {
		
	}
	
	@FXML
	public void openButtonAction() {
		openActivity(activity);
	}
	
	public void openActivity(Activity activity) {
		try {
			File file = new File(pathToERBStaticDataFolder + "\\Activities\\ChapterActivities\\" + activity.getFileName());
			if (file.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				logger.error(file.getPath() + " either does not exist or desktop is not supported.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public WebView getWebView() {
		return webView;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Label getActivityNameLabel() {
		return activityNameLabel;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	
}
