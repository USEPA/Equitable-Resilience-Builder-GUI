package com.epa.erb.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import org.apache.poi.ss.usermodel.Sheet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.PrintWriter;
import java.util.Scanner;

public class IndicatorSelection_InPersonController implements Initializable {

	@FXML
	VBox vBox;
	@FXML
	VBox indicatorVBox;
	@FXML
	HBox dataSelectionHBox;
	
	private Sheet indicatorSheet;
	private File indicatorWorkbookFile;
	private ArrayList<IndicatorCard> cards;
	private FileHandler fileHandler = new FileHandler();
		
	private App app;
	public IndicatorSelection_InPersonController(App app) {
		this.app = app;
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initWBFile();
		initSheet();
		initCards();
		fillIndicatorVBox();
	}
	
	private void initWBFile() {
		indicatorWorkbookFile = new File(fileHandler.getSupportingDOCDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal()) + "\\Indicators_Master_List.xlsx");
	}
	
	private void initSheet() {
		IndicatorWorkbookParser iWP = new IndicatorWorkbookParser(indicatorWorkbookFile);
		indicatorSheet = iWP.getIndicatorSheet();
	}
	
	private void initCards() {
		IndicatorWorkbookParser iWP = new IndicatorWorkbookParser(indicatorWorkbookFile);
		cards = iWP.parseForIndicatorCards(indicatorSheet);
	}
	
	private void fillIndicatorVBox() {
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<String> selectedIndicatorIds = iSDP.getSavedSelectedIndicatorIds_InPerson();
		for(IndicatorCard iC: cards) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorSelector_InPerson.fxml"));
				IndicatorSelector_InPersonController iSIP = new IndicatorSelector_InPersonController();
				fxmlLoader.setController(iSIP);
				HBox root = fxmlLoader.load();
				CheckBox cBox = iSIP.getIndicatorCheckBox();
				cBox.setId(iC.getId());
				cBox.setText(iC.getSystem() + " - " + iC.getIndicator());
				if(selectedIndicatorIds != null && selectedIndicatorIds.contains(iC.getId())) {
					cBox.setSelected(true);
				}
				indicatorVBox.getChildren().add(root);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void saveButtonAction() {
		File indicatorsDir = createIndicatorsDir();
		File inPersonCardSelectionFile = new File(indicatorsDir + "\\CardSelection_InPerson.txt");
		writeSelectedIndicators(inPersonCardSelectionFile);
	}
	
	public void writeSelectedIndicators(File inPersonCardSelectionFile) {
		try {
			PrintWriter printWriter = new PrintWriter(inPersonCardSelectionFile);
			for(int i =0; i < indicatorVBox.getChildren().size(); i++) {
				HBox child = (HBox) indicatorVBox.getChildren().get(i);
				CheckBox cBox = (CheckBox) child.getChildren().get(0);
				if(cBox != null && cBox.isSelected()) {
					printWriter.println(cBox.getId());
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
	public void dataSelectionButtonAction() {
		IndicatorWorkbookParser iWP = new IndicatorWorkbookParser(indicatorWorkbookFile);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/DataSelection_InPerson.fxml"));
			DataSelection_InPersonController dSIP = new DataSelection_InPersonController(app, iWP.getRowValues(indicatorSheet, 0));
			fxmlLoader.setController(dSIP);
			VBox root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Data Selection");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
