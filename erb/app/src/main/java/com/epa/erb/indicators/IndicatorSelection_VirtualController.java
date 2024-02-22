package com.epa.erb.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

public class IndicatorSelection_VirtualController implements Initializable {

	private Logger logger;
	private FileHandler fileHandler;
	private IndicatorWorkbookParser iWP;
	private ArrayList<CheckBox> selectedCheckBoxes = new ArrayList<CheckBox>();
	
	private App app;
	private IndicatorCenterController iCC;
	public IndicatorSelection_VirtualController(App app, IndicatorCenterController iCC) {
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
				app.getEngagementActionController().getCurrentGoal()) + File.separator + "Indicators_List.xlsx");
		iWP = new IndicatorWorkbookParser(app,indicatorWorkbookFile);
	}

	private void fillIndicatorVBox() {
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<String> selectedIndicatorIds = iSDP.getSavedSelectedIndicatorIds_Virtual();
		for (IndicatorCard iC : iWP.parseForIndicatorCards()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(
						getClass().getResource("/indicators/IndicatorSelector_Virtual.fxml"));
				IndicatorSelector_VirtualController iSV = new IndicatorSelector_VirtualController();
				fxmlLoader.setController(iSV);
				HBox root = fxmlLoader.load();
				CheckBox cBox = iSV.getIndicatorCheckBox();
				cBox.setId(iC.getId());
				cBox.setOnAction(e -> checkBoxSelected(cBox));
				cBox.setText(iC.getSystem() + " - " + iC.getIndicator());
				if (selectedIndicatorIds != null && selectedIndicatorIds.contains(iC.getId())) {
					cBox.setSelected(true);
				}
				indicatorVBox.getChildren().add(root);
			} catch (Exception e) {
				logger.log(Level.FINE, "Failed to load IndicatorSelector_Virtual.fxml.");
				logger.log(Level.FINER, "Failed to load IndicatorSelector_Virtual.fxml: " + e.getStackTrace());
			}
		}
	}

	private void checkBoxSelected(CheckBox checkBox) {
		if (checkBox.isSelected()) {
			if (!selectedCheckBoxes.contains(checkBox)) {
				selectedCheckBoxes.add(checkBox);
			}
		} else {
			selectedCheckBoxes.add(checkBox);
		}
	}

	private Stage virtualIndicatorRankingStage = null;

	@FXML
	public void rankingButtonAction() {
		save();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorRanking_Virtual.fxml"));
			IndicatorRanking_VirtualController iRV = new IndicatorRanking_VirtualController(app, this);
			fxmlLoader.setController(iRV);
			VBox root = fxmlLoader.load();
			virtualIndicatorRankingStage = new Stage();
			virtualIndicatorRankingStage.getIcons().add(new Image("/bridge_tool_logo.png"));
			virtualIndicatorRankingStage.setWidth(app.getPopUpPrefWidth());
			virtualIndicatorRankingStage.setHeight(app.getPopUpPrefHeight());
			virtualIndicatorRankingStage.setTitle("Indicator Ranking");
			Scene scene = new Scene(root);
			virtualIndicatorRankingStage.setOnCloseRequest(e -> iRV.closeRequested(e));
			virtualIndicatorRankingStage.setScene(scene);
			virtualIndicatorRankingStage.show();
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load IndicatorRanking_Virtual.fxml.");
			logger.log(Level.FINER, "Failed to load IndicatorRanking_Virtual.fxml: " + e.getStackTrace());
		}
		iCC.getVirtualIndicatorSelectionStage().close();
	}

	@FXML
	public void saveButtonAction() {
		save();
		iCC.getVirtualIndicatorSelectionStage().close();
	}

	private void save() {
		File indicatorsDir = createIndicatorsDir();
		File virtualCardSelectionFile = new File(indicatorsDir + File.separator + "CardSelection_Virtual.txt");
		writeSelectedIndicators(virtualCardSelectionFile);
	}

	public void writeSelectedIndicators(File virtualCardSelectionFile) {
		try {
			PrintWriter printWriter = new PrintWriter(virtualCardSelectionFile);
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

	public Stage getVirtualIndicatorRankingStage() {
		return virtualIndicatorRankingStage;
	}
}
