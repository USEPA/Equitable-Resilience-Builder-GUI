package com.epa.erb.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.epa.erb.App;
import com.epa.erb.print.PrinterSelectionController;
import com.epa.erb.utility.FileHandler;

public class DataSelection_InPersonController implements Initializable{

	private Logger logger;
	private FileHandler fileHandler;
	
	private App app;
	private ArrayList<String> dataOptions;
	private IndicatorSelection_InPersonController iSINC;
	public DataSelection_InPersonController(App app, ArrayList<String> dataOptions, IndicatorSelection_InPersonController iSINC) {
		this.app = app;
		this.iSINC = iSINC;
		this.dataOptions = dataOptions;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}
	
	@FXML
	VBox vBox;
	@FXML
	VBox dataVBox;
	@FXML
	HBox printHBox;
	@FXML
	CheckBox defaultDataCheckBox;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillDataVBox();
	}
	
	@FXML
	public void defaultDataCheckBoxAction() {
		if(defaultDataCheckBox.isSelected()) {
			setDataCheckBoxOptionsDisabled(true);
		} else {
			setDataCheckBoxOptionsDisabled(false);
		}
	}
	
	private void setDataCheckBoxOptionsDisabled(boolean disable) {
		for(int i =0; i < dataVBox.getChildren().size(); i++) {
			HBox child = (HBox) dataVBox.getChildren().get(i);
			CheckBox cBox = (CheckBox) child.getChildren().get(0);
			if(cBox != null) {
				cBox.setDisable(disable);
			}
		}
	}
	
	private void fillDataVBox() {
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<String> selectedData = iSDP.getSavedSelectedData_InPerson();

		for(String dataOption: dataOptions) {
			if(dataOption.trim().length()>0) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorSelector_InPerson.fxml"));
				IndicatorSelector_InPersonController iSIP = new IndicatorSelector_InPersonController();
				fxmlLoader.setController(iSIP);
				HBox root = fxmlLoader.load();
				CheckBox cBox = iSIP.getIndicatorCheckBox();
				cBox.setText(dataOption);
				if(selectedData != null && selectedData.contains(dataOption)) {
					cBox.setSelected(true);
				}
				dataVBox.getChildren().add(root);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Failed to load IndicatorSelector_InPerson.fxml: " + e.getMessage());
				}
			}
		}
		if(selectedData != null && selectedData.contains(defaultDataCheckBox.getText())) {
			defaultDataCheckBox.setSelected(true);
			setDataCheckBoxOptionsDisabled(true);
		} 
	}
	
	@FXML
	public void saveButtonAction() {
		save();
		iSINC.getInPersonDataSelectionStage().close();
	}
	
	private void save() {
		File indicatorsDir = createIndicatorsDir();
		File inPersonDataSelectionFile = new File(indicatorsDir + File.separator + "DataSelection_InPerson.txt");
		writeSelectedData(inPersonDataSelectionFile);
	}
	
	public void writeSelectedData(File file) {
		try {
			PrintWriter printWriter = new PrintWriter(file);
			if (defaultDataCheckBox.isSelected()) {
				printWriter.println(defaultDataCheckBox.getText());
			} else {
				for (int i = 0; i < dataVBox.getChildren().size(); i++) {
					HBox child = (HBox) dataVBox.getChildren().get(i);
					CheckBox cBox = (CheckBox) child.getChildren().get(0);
					if (cBox != null && cBox.isSelected()) {
						printWriter.println(cBox.getText());
					}
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Failed to write selected data: " + e.getMessage());
		}
	}
	
	public File createIndicatorsDir() {
		File indicatorsDir = fileHandler.getIndicatorsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
		if(!indicatorsDir.exists()) {
			indicatorsDir.mkdir();
		}
		return indicatorsDir;
	}
	
	@FXML
	public void printIndicatorCardsButtonAction() {
		save();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorsPrintView.fxml"));
			IndicatorsPrintViewController indicatorsPrintView = new IndicatorsPrintViewController(app);
			fxmlLoader.setController(indicatorsPrintView);
			fxmlLoader.load();
			showPrinters(indicatorsPrintView);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load IndicatorsPrintView.fxml: " + e.getMessage());
		}
		iSINC.getInPersonDataSelectionStage().close();
	}
	
	private Stage printerSelectionStage = null;
	
	private void showPrinters(IndicatorsPrintViewController indicatorsPrintView) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/print/PrinterSelection.fxml"));
			PrinterSelectionController printerSelectionController = new PrinterSelectionController(indicatorsPrintView, this);
			fxmlLoader.setController(printerSelectionController);
			VBox root = fxmlLoader.load();
			printerSelectionStage = new Stage();
			printerSelectionStage.getIcons().add(new Image("/bridge_tool_logo.png"));
			printerSelectionStage.setWidth(app.getPopUpPrefWidth());
			printerSelectionStage.setHeight(app.getPopUpPrefHeight());
			printerSelectionStage.setTitle("Printer Selection");
			Scene scene = new Scene(root);
			printerSelectionStage.setScene(scene);
			printerSelectionStage.show();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load PrinterSelection.fxml: " + e.getMessage());
		}

	}

	public Stage getPrinterSelectionStage() {
		return printerSelectionStage;
	}
	

}
