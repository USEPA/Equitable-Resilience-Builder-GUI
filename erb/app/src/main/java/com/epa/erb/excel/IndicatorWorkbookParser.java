package com.epa.erb.excel;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.epa.erb.indicators.IndicatorCard;

public class IndicatorWorkbookParser extends WorkbookParser{

	public IndicatorWorkbookParser() {
		
	}
	
	private int getNumColumns(Sheet indicatorSheet) {
		ArrayList<String> rowValues = getRowValues(indicatorSheet, 0);
		ArrayList<String> nonEmptyValues = new ArrayList<String>();
		for(String rowVal: rowValues) {
			if(rowVal != null && !rowVal.isBlank() && !rowVal.isEmpty() && rowVal.length()>0) {
				nonEmptyValues.add(rowVal);
			}
		}
		return nonEmptyValues.size();
	}
	
	private int getNumRows(Sheet indicatorSheet) {
		return getColumnValues(indicatorSheet, 0).size();
	}
	
	public ArrayList<IndicatorCard> test() {
		ArrayList<IndicatorCard> indicatorCards = new ArrayList<IndicatorCard>();
		String filePath = "C:\\Users\\awilke06\\Documents\\Eclipse_Repos\\MetroCERI\\metroceri_erb\\erb_supporting_docs\\Code_Resources\\ERB\\Static_Data\\Supporting_DOC\\Indicators_Master_List.xlsx";
		XSSFWorkbook workbook = getXSSFWorkbook(filePath);
		Sheet indicatorSheet = null;
		for(Sheet worksheet: getWorksheets(workbook)) {
			if(worksheet.getSheetName().contentEquals("Combined Indicator Menu")) {
				indicatorSheet = worksheet;
			}
		}
		if(indicatorSheet != null) {
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
			
			for(int i=0; i < getNumColumns(indicatorSheet); i++) {
				String headerVal =  headerValues.get(i);
				if(headerVal.contentEquals("System")) {
					systemColumnIndex = i;
				} else if (headerVal.contentEquals("Indicator")) {
					indicatorColumnIndex=i;
				} else if (headerVal.contains("Definition")) {
					definitionColumnIndex =i;
				} else if (headerVal.contentEquals("Resilience Value")) {
					resilienceValueColumnIndex =i;
				} else if (headerVal.contentEquals("Equity Value")) {
					equityValueColumnIndex =i;
				} else if (headerVal.contains("Local Concern")) {
					localConcernColumnIndex=i;
				} else if (headerVal.contentEquals("Data Questions to Answer")){
					dataQuestionsColumnIndex=i;
				} else if (headerVal.contentEquals("Quantitative Data Sources")) {
					quanDataSourcesColumnIndex=i;
				} else if (headerVal.contentEquals("Quantitative Data Collection Process")) {
					quanDataCollectionColumnIndex=i;
				} else if (headerVal.contentEquals("Qualitative Data Collection Process")) {
					qualDataCollectionColumnIndex=i;
				} else if (headerVal.contains("Additional Details")) {
					additionalInfoColumnIndex=i;
				}  else if (headerVal.contentEquals("Raw Data Collection Notes")) {
					rawDataCollectionColumnIndex=i;
				} else if (headerVal.contains("Data Points")) {
					dataPointsColumnIndex=i;
				} else if (headerVal.contains("Thresholds")) {
					thresholdColumnIndex=i;
				}		
			}
						
			for(int j =1; j < getNumRows(indicatorSheet); j++) {
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
				IndicatorCard indicatorCard = new IndicatorCard(id, s, i, d, rV, eV, lC, q, qnDS, qnDC, qlDC, aI, rdColl, dP, t);
				indicatorCards.add(indicatorCard);
			}
		}
		return indicatorCards;
	}
	
	private ArrayList<String> parseQString(String qString){
		ArrayList<String> dataQuestions = new ArrayList<String>();
		String [] split = qString.split("\n");
		for(String s: split) {
			if(s.length() > 0 && s.trim().length() > 0) {
				dataQuestions.add(s);
			}
		}
		return dataQuestions;
	}
	
}
