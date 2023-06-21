package com.epa.erb.excel;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookParser {
	
	public WorkbookParser() {
		
	}
	
	public XSSFWorkbook getXSSFWorkbook(String excelWorkbookPath) {
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelWorkbookPath);
			return xssfWorkbook;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Sheet> getWorksheets(XSSFWorkbook xssfWorkbook){
		ArrayList<Sheet> allSheets = new ArrayList<Sheet>();
		if(xssfWorkbook != null) {
			Iterator<Sheet> sheetIterator = xssfWorkbook.sheetIterator();
			while(sheetIterator.hasNext()) {
				Sheet sheet = sheetIterator.next();
				allSheets.add(sheet);
			}
		}
		return allSheets;
	}
	
	public Sheet getWorksheet(XSSFWorkbook xssfWorkbook, String sheetName){
		ArrayList<Sheet> allSheets = getWorksheets(xssfWorkbook);
		if(allSheets.size()> 0) {
			for(Sheet sheet: allSheets) {
				if(sheet.getSheetName().contentEquals(sheetName)) {
					return sheet;
				}
			}
		}
		return null;
	}
	
	public ArrayList<String> getWorksheetNames(XSSFWorkbook xssfWorkbook){
		ArrayList<String> allSheetNames = new ArrayList<String>();
		ArrayList<Sheet> allSheets = getWorksheets(xssfWorkbook);
		if(allSheets.size()> 0) {
			for(Sheet sheet: allSheets) {
				allSheetNames.add(sheet.getSheetName());
			}
		}
		return allSheetNames;
	}
	
	public int getRowIndex(Sheet sheetToSearch, int columnNumberToSearch, String rowStringToSearchFor) {
		if (sheetToSearch != null) {
			int rowCount = 0;
			Iterator<Row> rowIterator = sheetToSearch.rowIterator();
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Cell cell = row.getCell(columnNumberToSearch);
				if(cell.getStringCellValue().contentEquals(rowStringToSearchFor)) {
					return rowCount;
				}
				rowCount++;
			}
		}
		return -1;
	}
	
	public int getColumnIndex(Sheet sheetToSearch, int rowNumberToSearch, String columnStringToSearchFor) {
		if(sheetToSearch != null) {
			Row row = sheetToSearch.getRow(rowNumberToSearch);
			if(row != null) {
				int columnCount =0;
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if(cell.getStringCellValue().contentEquals(columnStringToSearchFor)) {
						return columnCount;
					}
					columnCount++;
				}
			}
		}
		return -1;
	}
	
	public ArrayList<String> getRowValues(Sheet sheetToSearch, int rowNumber){
		ArrayList<String> rowValues = new ArrayList<String>();
		if(sheetToSearch != null) {
			Row row = sheetToSearch.getRow(rowNumber);
			if(row!= null) {
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String cellValue = cell.getStringCellValue();
					rowValues.add(cellValue);
				}
			}
		}
		return rowValues;
	}
	
	public ArrayList<String> getColumnValues (Sheet sheetToSearch, int columnNumber){
		ArrayList<String> columnValues = new ArrayList<String>();
		if(sheetToSearch != null) {
			Iterator<Row> rowIterator = sheetToSearch.rowIterator();
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String cellValue = row.getCell(columnNumber).getStringCellValue();
				if(cellValue != null && !cellValue.isBlank() && !cellValue.isEmpty() && cellValue.length()>0) {
					columnValues.add(cellValue);
				}
			}
		}
		return columnValues;
	} 

}
