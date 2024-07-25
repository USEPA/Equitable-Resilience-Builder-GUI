package com.epa.erb.indicators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
				app.getEngagementActionController().getCurrentGoal()) + File.separator + "Indicators_List.xlsx");
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
				logger.log(Level.SEVERE, "Failed to load IndicatorSelector_InPerson.fxml: " + e.getMessage());
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
		File inPersonCardSelectionFile = new File(indicatorsDir + File.separator + "CardSelection_InPerson.txt");
		ArrayList<String> ids = writeSelectedIndicators(inPersonCardSelectionFile);
		if(ids != null) createSkimmedCopyOfIndicatorsListXLSX(ids);
	}
	
	private void createSkimmedCopyOfIndicatorsListXLSX(ArrayList<String> ids) {
		try {
			
			ids.add(0, "0");
			String fileName = "Indicators_List.xlsx";
			XSSFWorkbook newWorkbook = new XSSFWorkbook();
			XSSFSheet newSheet = newWorkbook.createSheet("Indicators");
			
			File indicatorWorkbookFile = new File(fileHandler.getSupportingDOCDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal()) + File.separator + "Indicators_List.xlsx");
			FileInputStream inputStream = new FileInputStream(indicatorWorkbookFile.getPath());
			XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheet("Indicator Menu");
			int destId = 0;
			for(String idString : ids) {
				int id = Integer.parseInt(idString);
				XSSFRow sourceRow= null;
				if(id == 0) {
					sourceRow = (XSSFRow) sheet.getRow(id);

				}else {
					sourceRow = (XSSFRow) sheet.getRow(id+1);

				}
				XSSFRow newRow = newSheet.createRow(destId);
				
				 for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			            // Grab a copy of the old/new cell
			            XSSFCell oldCell = sourceRow.getCell(i);
			            XSSFCell newCell = newRow.createCell(i);
			            // If the old cell is null jump to next cell
			            if (oldCell == null) {
			                continue;
			            }

			            // Copy style from old cell and apply to new cell
			            XSSFCellStyle newCellStyle = newWorkbook.createCellStyle();
			            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			            newCell.setCellStyle(newCellStyle);

			            // If there is a cell comment, copy
			            if (oldCell.getCellComment() != null) {
			                newCell.setCellComment(oldCell.getCellComment());
			            }

			            // If there is a cell hyperlink, copy
			            if (oldCell.getHyperlink() != null) {
			            	newCell.setCellValue(oldCell.getHyperlink().toString());
			                //newCell.setHyperlink(oldCell.getHyperlink());
			            }

			            // Set the cell data type
			            newCell.setCellType(oldCell.getCellType());

			            // Set the cell data value
			            switch (oldCell.getCellType()) {
			            case BLANK:// Cell.CELL_TYPE_BLANK:
			                newCell.setCellValue(oldCell.getStringCellValue());
			                break;
			            case BOOLEAN:
			                newCell.setCellValue(oldCell.getBooleanCellValue());
			                break;
			            case FORMULA:
			                newCell.setCellFormula(oldCell.getCellFormula());
			                break;
			            case NUMERIC:
			                newCell.setCellValue(oldCell.getNumericCellValue());
			                break;
			            case STRING:
			                newCell.setCellValue(oldCell.getRichStringCellValue());
			                break;
			            default:
			                break;
			            }
			        }
				
				destId++;
			}
			
			OutputStream os = new FileOutputStream(getIndicatorsDir() + File.separator + fileName);
			newWorkbook.write(os);
			os.close();
			newWorkbook.close();
			inputStream.close();			
			
		} catch (IOException | EncryptedDocumentException e) {
			logger.log(Level.SEVERE, "Failed to create skimmed copy of indicators list: " + e.getMessage());
		}
	}
	
	private File getIndicatorsDir() {
		return fileHandler.getIndicatorsDirectory(app.getSelectedProject(),app.getEngagementActionController().getCurrentGoal());
	}

	public ArrayList<String> writeSelectedIndicators(File inPersonCardSelectionFile) {
		try {
			ArrayList<String> selectedIds = new ArrayList<String>();
			PrintWriter printWriter = new PrintWriter(inPersonCardSelectionFile);
			for (int i = 0; i < indicatorVBox.getChildren().size(); i++) {
				HBox child = (HBox) indicatorVBox.getChildren().get(i);
				CheckBox cBox = (CheckBox) child.getChildren().get(0);
				if (cBox != null && cBox.isSelected()) {
					printWriter.println(cBox.getId());
					selectedIds.add(cBox.getId());
				}
			}
			printWriter.close();
			return selectedIds;
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Failed to write selected indicators: " + e.getMessage());
		}
		return null;

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
			logger.log(Level.SEVERE, "Failed to load DataSelection_InPerson.fxml: " + e.getMessage());
		}
		iCC.getInPersonIndicatorSelectionStage().close();
	}

	public Stage getInPersonDataSelectionStage() {
		return inPersonDataSelectionStage;
	}
}
