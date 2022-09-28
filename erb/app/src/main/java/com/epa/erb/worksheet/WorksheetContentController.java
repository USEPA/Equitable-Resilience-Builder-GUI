package com.epa.erb.worksheet;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import javax.print.PrintService;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.value.ChangeListener;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;

public class WorksheetContentController implements Initializable{

	@FXML
	WebView webView;
	@FXML
	Label activityNameLabel;
	@FXML
	HBox fileOptionsHBox;

	private Activity activity;
	private Project project;
	private Goal goal;
	private App app;
	public WorksheetContentController(Activity activity, Project project, Goal goal, App app) {
		this.activity = activity;
		this.project = project;
		this.goal = goal;
		this.app = app;
	}
	
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(WorksheetContentController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setActivityNameLabelText();
		//loadPDFToWebView(fileHandler.getActivityPDFDoc(project, goal, activity));
		webView.setContextMenuEnabled(false);
	    createContextMenu(webView);
	}
	
	private void handleControls() {

	}
	
	private void createContextMenu(WebView webView) {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem reload = new MenuItem("Reload");
		reload.setOnAction(e -> reloadClicked());
		contextMenu.getItems().addAll(reload);

		webView.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				contextMenu.show(webView, e.getScreenX(), e.getScreenY());
			} else {
				contextMenu.hide();
			}
		});
	}
	
	private void reloadClicked() {
		//File docxFile = fileHandler.getActivityWordDoc(project, goal, activity);
		//File pdfFile = fileHandler.getActivityPDFDoc(project, goal, activity);
		//fileHandler.convertDocxToPDF2(docxFile, pdfFile.getPath());
		webView.getEngine().reload();
	}
	
	public void hideFileOptionsHBox() {
		fileOptionsHBox.setVisible(false);
	}
	
	@FXML
	public void printButtonAction() {
		Parent printSelectionRoot = loadPrinterSelection();
		showPrinterSelection(printSelectionRoot);
	}
	
	@FXML
	public void openButtonAction() {
		openActivity(activity);
	}

	private void loadPDFToWebView(File pdfFileToLoad) {
		if (pdfFileToLoad != null && pdfFileToLoad.exists()) {
			try {
				WebEngine webEngine = webView.getEngine();
				String url = getClass().getResource("/pdfjs-2.8.335-legacy-dist/web/viewer.html").toExternalForm();
				webEngine.setUserStyleSheetLocation(getClass().getResource("/web.css").toExternalForm());
				webEngine.setJavaScriptEnabled(true);
				webEngine.load(url);

				webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue ov, State oldState, State newState) {
						if (newState == Worker.State.SUCCEEDED) {
							try {
								byte[] data = FileUtils.readFileToByteArray(pdfFileToLoad);
								String base64 = Base64.getEncoder().encodeToString(data);
								webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
							} catch (Exception ex) {
								logger.error(ex.getMessage());
							}
						}
					}
				});
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot loadPDFToWebView. pdfFileToLoad is null.");
		}
	}
	
	private Parent loadPrinterSelection() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/worksheet/PrinterSelection.fxml"));
			PrinterSelectionController printerSelectionController = new PrinterSelectionController(this);
			fxmlLoader.setController(printerSelectionController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private Stage printerSelectionStage = null;
	private void showPrinterSelection(Parent printerSelectionRoot) {
		if (printerSelectionRoot != null) {
			printerSelectionStage = new Stage();
			printerSelectionStage.setTitle("ERB: Printer Selection");
			Scene scene = new Scene(printerSelectionRoot);
			printerSelectionStage.setScene(scene);
			printerSelectionStage.showAndWait();
		} else {
			logger.error("Cannot showPrinterSelection. printerSelectionRoot is null.");
		}
	}
	
	void closePrinterStage() {
		if(printerSelectionStage != null) {
			printerSelectionStage.close();
		}
	}
	
	void printActivity(PrintService printService, File fileToPrint) {
		try {
//			FileInputStream fileInputStream = new FileInputStream(fileToPrint.getPath());
//			PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
//			attributeSet.add(new Copies(1));
//			PDDocument pdDocument = Loader.loadPDF(fileInputStream);
//			PrinterJob printerJob = PrinterJob.getPrinterJob();
//			printerJob.setPrintService(printService);
//			printerJob.setPageable(new PDFPageable(pdDocument));
//			printerJob.print();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void openActivity(Activity activity) {
		if (activity != null) {
			try {
//				File file = fileHandler.getActivityWordDoc(project, goal, activity);
				File file = new File("");
				if (file != null && file.exists() && Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(file);
				} else {
					logger.error(file.getPath() + " either does not exist or desktop is not supported.");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot openActivity. activity is null.");
		}
	}
	
	void printerSelected(PrintService printService) {
		File fileToPrint = getFileToPrint();
		printActivity(printService, fileToPrint);
	}
	
	private File getFileToPrint() {
		return null;
	}
	
	private void setActivityNameLabelText() {
		activityNameLabel.setText(activity.getLongName());
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public WebView getWebView() {
		return webView;
	}

	public Label getActivityNameLabel() {
		return activityNameLabel;
	}

}
