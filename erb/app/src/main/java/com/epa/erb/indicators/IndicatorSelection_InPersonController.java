package com.epa.erb.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.PrintWriter;

public class IndicatorSelection_InPersonController implements Initializable {

	private Logger logger;
	private FileHandler fileHandler;
	private IndicatorWorkbookParser iWP;

	private App app;
	private IndicatorCenterController iCC;
	public IndicatorSelection_InPersonController(App app, IndicatorCenterController iCC) {
		this.app = app;
		this.iCC = iCC;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}
	
	@FXML
	VBox vBox;
	@FXML
	VBox indicatorVBox;
	@FXML
	HBox dataSelectionHBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initWBParser();
		fillIndicatorVBox();
	}

	private void initWBParser() {
		File indicatorWorkbookFile = new File(fileHandler.getSupportingDOCDirectory(app.getSelectedProject(),
				app.getEngagementActionController().getCurrentGoal()) + "\\Indicators_List.xlsx");
		iWP = new IndicatorWorkbookParser(app,indicatorWorkbookFile);
	}

	private void fillIndicatorVBox() {
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<String> selectedIndicatorIds = iSDP.getSavedSelectedIndicatorIds_InPerson();
		for (IndicatorCard iC : iWP.parseForIndicatorCards()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(
						getClass().getResource("/indicators/IndicatorSelector_InPerson.fxml"));
				IndicatorSelector_InPersonController iSIP = new IndicatorSelector_InPersonController();
				fxmlLoader.setController(iSIP);
				HBox root = fxmlLoader.load();
				CheckBox cBox = iSIP.getIndicatorCheckBox();
				cBox.setId(iC.getId());
				cBox.setText(iC.getSystem() + " - " + iC.getIndicator());
				if (selectedIndicatorIds != null && selectedIndicatorIds.contains(iC.getId())) {
					cBox.setSelected(true);
				}
				indicatorVBox.getChildren().add(root);
			} catch (Exception e) {
				logger.log(Level.FINE, "Failed to load IndicatorSelector_InPerson.fxml.");
				logger.log(Level.FINER, "Failed to load IndicatorSelector_InPerson.fxml: " + e.getStackTrace());
			}
		}
	}

	@FXML
	public void saveButtonAction() {
		save();
		iCC.getInPersonIndicatorSelectionStage().close();
	}

	private void save() {
		File indicatorsDir = createIndicatorsDir();
		File inPersonCardSelectionFile = new File(indicatorsDir + "\\CardSelection_InPerson.txt");
		writeSelectedIndicators(inPersonCardSelectionFile);
	}

	public void writeSelectedIndicators(File inPersonCardSelectionFile) {
		try {
			PrintWriter printWriter = new PrintWriter(inPersonCardSelectionFile);
			for (int i = 0; i < indicatorVBox.getChildren().size(); i++) {
				HBox child = (HBox) indicatorVBox.getChildren().get(i);
				CheckBox cBox = (CheckBox) child.getChildren().get(0);
				if (cBox != null && cBox.isSelected()) {
					printWriter.println(cBox.getId());
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.FINE, "Failed to write selected indicators.");
			logger.log(Level.FINER, "Failed to write selected indicators: " + e.getStackTrace());
		}

	}

	public File createIndicatorsDir() {
		File indicatorsDir = fileHandler.getIndicatorsDirectory(app.getSelectedProject(),
				app.getEngagementActionController().getCurrentGoal());
		if (!indicatorsDir.exists()) {
			indicatorsDir.mkdir();
		}
		return indicatorsDir;
	}

	private Stage inPersonDataSelectionStage = null;

	@FXML
	public void dataSelectionButtonAction() {
		save();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/DataSelection_InPerson.fxml"));
			DataSelection_InPersonController dSIP = new DataSelection_InPersonController(app,
					iWP.getRowValues(iWP.getIndicatorSheet(), 0), this);
			fxmlLoader.setController(dSIP);
			VBox root = fxmlLoader.load();
			inPersonDataSelectionStage = new Stage();
			inPersonDataSelectionStage.getIcons().add(new Image("/bridge_tool_logo.png"));
			inPersonDataSelectionStage.setWidth(app.getPopUpPrefWidth());
			inPersonDataSelectionStage.setHeight(app.getPopUpPrefHeight());
			inPersonDataSelectionStage.setTitle("Data Selection");
			Scene scene = new Scene(root);
			inPersonDataSelectionStage.setScene(scene);
			inPersonDataSelectionStage.show();
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load DataSelection_InPerson.fxml.");
			logger.log(Level.FINER, "Failed to load DataSelection_InPerson.fxml: " + e.getStackTrace());
		}
		iCC.getInPersonIndicatorSelectionStage().close();
	}

	public Stage getInPersonDataSelectionStage() {
		return inPersonDataSelectionStage;
	}
}
