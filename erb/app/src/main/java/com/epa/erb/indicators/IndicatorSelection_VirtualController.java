package com.epa.erb.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IndicatorSelection_VirtualController implements Initializable{

	@FXML
	VBox vBox;
	@FXML
	VBox indicatorVBox;
	@FXML
	HBox dataSelectionHBox;
	
	private IndicatorWorkbookParser iWP;
	private FileHandler fileHandler = new FileHandler();
	private ArrayList<CheckBox> selectedCheckBoxes = new ArrayList<CheckBox>();
	
	private App app;
	public IndicatorSelection_VirtualController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initWBParser();
		fillIndicatorVBox();
	}
	
	private void initWBParser() {
		File indicatorWorkbookFile = new File(fileHandler.getSupportingDOCDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal()) + "\\Indicators_Master_List.xlsx");
		iWP = new IndicatorWorkbookParser(indicatorWorkbookFile);
	}
	
	private void fillIndicatorVBox() {
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<String> selectedIndicatorIds = iSDP.getSavedSelectedIndicatorIds_Virtual();
		for(IndicatorCard iC: iWP.parseForIndicatorCards()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorSelector_Virtual.fxml"));
				IndicatorSelector_VirtualController iSV = new IndicatorSelector_VirtualController();
				fxmlLoader.setController(iSV);
				HBox root = fxmlLoader.load();
				CheckBox cBox = iSV.getIndicatorCheckBox();
				cBox.setId(iC.getId());
				cBox.setOnAction(e-> checkBoxSelected(cBox));
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
	
	private void checkBoxSelected(CheckBox checkBox) {		
		if(checkBox.isSelected()) {
			if(!selectedCheckBoxes.contains(checkBox)) {
				selectedCheckBoxes.add(checkBox);
			}
		} else {
			selectedCheckBoxes.add(checkBox);
		}
	}
	
	@FXML
	public void rankingButtonAction() {
		saveButtonAction();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorRanking_Virtual.fxml"));
			IndicatorRanking_VirtualController iRV = new IndicatorRanking_VirtualController(app);
			fxmlLoader.setController(iRV);
			VBox root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Indicator Ranking");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void saveButtonAction() {
		File indicatorsDir = createIndicatorsDir();
		File virtualCardSelectionFile = new File(indicatorsDir + "\\CardSelection_Virtual.txt");
		writeSelectedIndicators(virtualCardSelectionFile);
	}
	
	public void writeSelectedIndicators(File virtualCardSelectionFile) {
		try {
			PrintWriter printWriter = new PrintWriter(virtualCardSelectionFile);
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

}
