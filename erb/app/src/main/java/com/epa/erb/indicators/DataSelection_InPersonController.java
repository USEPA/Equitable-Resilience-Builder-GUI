package com.epa.erb.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.epa.erb.App;
import com.epa.erb.print.PrinterSelectionController;
import com.epa.erb.utility.FileHandler;

public class DataSelection_InPersonController implements Initializable{

	@FXML
	VBox vBox;
	@FXML
	VBox dataVBox;
	@FXML
	HBox printHBox;
	@FXML
	CheckBox defaultDataCheckBox;
	
	private FileHandler fileHandler = new FileHandler();
	
	private App app;
	private ArrayList<String> dataOptions;
	public DataSelection_InPersonController(App app, ArrayList<String> dataOptions) {
		this.app = app;
		this.dataOptions = dataOptions;
	}
	
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
				e.printStackTrace();
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
		File indicatorsDir = createIndicatorsDir();
		File inPersonDataSelectionFile = new File(indicatorsDir + "\\DataSelection_InPerson.txt");
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
			e.printStackTrace();
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
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorsPrintView.fxml"));
			IndicatorsPrintViewController indicatorsPrintView = new IndicatorsPrintViewController(app);
			fxmlLoader.setController(indicatorsPrintView);
			fxmlLoader.load();
			showPrinters(indicatorsPrintView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showPrinters(IndicatorsPrintViewController indicatorsPrintView) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/print/PrinterSelection.fxml"));
			PrinterSelectionController printerSelectionController = new PrinterSelectionController(indicatorsPrintView);
			fxmlLoader.setController(printerSelectionController);
			VBox root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Printer Selection");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
