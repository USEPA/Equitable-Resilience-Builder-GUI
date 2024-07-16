package com.epa.erb.indicators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.epa.erb.App;
import com.epa.erb.excel.WorkbookParser;

public class IndicatorWorkbookParser extends WorkbookParser {

	private Logger logger;
	
	private App app;
	private File workbookFile;
	private Sheet indicatorSheet;
	public IndicatorWorkbookParser(App app, File workbookFile) {
		super(app);
		this.workbookFile = workbookFile;
		
		setIndicatorSheet();
		logger = app.getLogger();
	}

	public void setIndicatorSheet() {
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(workbookFile);
			indicatorSheet = getWorksheet(xssfWorkbook, "Indicator Menu");
		} catch (InvalidFormatException | IOException e) {
			logger.log(Level.FINE, "Failed to set indicator sheet.");
			logger.log(Level.FINER, "Failed to set indicator sheet: " + e.getStackTrace());
		}
	}

	private int getNumColumns() {
		ArrayList<String> rowValues = getRowValues(indicatorSheet, 0);
		ArrayList<String> nonEmptyValues = new ArrayList<String>();
		for (String rowVal : rowValues) {
			if (rowVal != null && !rowVal.isBlank() && !rowVal.isEmpty() && rowVal.length() > 0) {
				nonEmptyValues.add(rowVal);
			}
		}
		return nonEmptyValues.size();
	}

	private int getNumRows() {
		return getColumnValues(indicatorSheet, 0).size();
	}

	public ArrayList<IndicatorCard> parseForIndicatorCards() {
		ArrayList<IndicatorCard> indicatorCards = new ArrayList<IndicatorCard>();
		if (indicatorSheet != null) {
			ArrayList<String> headerValues = getRowValues(indicatorSheet, 0);
			int systemColumnIndex = -1;
			int indicatorColumnIndex = -1;
			int definitionColumnIndex = -1;
			int resilienceValueColumnIndex = -1;
			int equityValueColumnIndex = -1;
			int localConcernColumnIndex = -1;
			int dataQuestionsColumnIndex = -1;
			int quanDataSourcesColumnIndex = -1;
			int quanDataCollectionColumnIndex = -1;
			int qualDataCollectionColumnIndex = -1;
			int additionalInfoColumnIndex = -1;
			int rawDataCollectionColumnIndex = -1;
			int dataPointsColumnIndex = -1;
			int thresholdColumnIndex = -1;

			for (int i = 0; i < getNumColumns(); i++) {
				String headerVal = headerValues.get(i);
				if (headerVal.contentEquals("System")) {
					systemColumnIndex = i;
				} else if (headerVal.contentEquals("Indicator")) {
					indicatorColumnIndex = i;
				} else if (headerVal.contains("Definition")) {
					definitionColumnIndex = i;
				} else if (headerVal.contentEquals("Resilience")) {
					resilienceValueColumnIndex = i;
				} else if (headerVal.contentEquals("Equity")) {
					equityValueColumnIndex = i;
				} else if (headerVal.contains("Local Concern")) {
					localConcernColumnIndex = i;
				} else if (headerVal.contentEquals("Data Questions to Answer")) {
					dataQuestionsColumnIndex = i;
				} else if (headerVal.contentEquals("Data Sources")) {
					quanDataSourcesColumnIndex = i;
				} else if (headerVal.contentEquals("Quantitative Data Collection Process")) {
					quanDataCollectionColumnIndex = i;
				} else if (headerVal.contentEquals("Qualitative Data Collection Process")) {
					qualDataCollectionColumnIndex = i;
				} else if (headerVal.contains("Additional Details")) {
					additionalInfoColumnIndex = i;
				} else if (headerVal.contains("data collection process")) {
					rawDataCollectionColumnIndex = i;
				} else if (headerVal.contains("Data Points")) {
					dataPointsColumnIndex = i;
				} else if (headerVal.contains("threshold value")) {
					thresholdColumnIndex = i;
				} else if (headerVal.contentEquals("(Y/N)")) {
				}
			}

			for (int j = 1; j < getNumRows(); j++) {
				ArrayList<String> rowValues = getRowValues(indicatorSheet, j);
				String id = String.valueOf(j);
				String s = rowValues.get(systemColumnIndex);
				String i = rowValues.get(indicatorColumnIndex);
				String d = rowValues.get(definitionColumnIndex);
				String rV = rowValues.get(resilienceValueColumnIndex);
				String eV = rowValues.get(equityValueColumnIndex);
				String lC = rowValues.get(localConcernColumnIndex);
				String qString = rowValues.get(dataQuestionsColumnIndex);
				ArrayList<String> q = parseQString(qString);
				String qnDS = rowValues.get(quanDataSourcesColumnIndex);
				String qnDC = rowValues.get(quanDataCollectionColumnIndex);
				String qlDC = rowValues.get(qualDataCollectionColumnIndex);
				String aI = rowValues.get(additionalInfoColumnIndex);
				String rdColl = rowValues.get(rawDataCollectionColumnIndex);
				String dP = rowValues.get(dataPointsColumnIndex);
				String t = rowValues.get(thresholdColumnIndex);
				IndicatorCard indicatorCard = new IndicatorCard(id, s, i, d, rV, eV, lC, q, qnDS, qnDC, qlDC, aI,
						rdColl, dP, t, "Y");
				indicatorCards.add(indicatorCard);
			}
		}
		return indicatorCards;
	}

	private ArrayList<String> parseQString(String qString) {
		ArrayList<String> dataQuestions = new ArrayList<String>();
		String[] split = qString.split("\n");
		for (String s : split) {
			if (s.length() > 0 && s.trim().length() > 0) {
				dataQuestions.add(s);
			}
		}
		return dataQuestions;
	}

	public File getWorkbookFile() {
		return workbookFile;
	}

	public Sheet getIndicatorSheet() {
		return indicatorSheet;
	}

	public App getApp() {
		return app;
	}

}
