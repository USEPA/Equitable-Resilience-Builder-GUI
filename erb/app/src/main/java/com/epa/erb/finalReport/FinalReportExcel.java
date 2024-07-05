package com.epa.erb.finalReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class FinalReportExcel {

	private File excelFile;
	private XWPFDocument document;

	public FinalReportExcel(File excelFile, XWPFDocument document) {
		this.excelFile = excelFile;
		this.document = document;
	}

	public void createTable() {
		try {
			ZipSecureFile.setMinInflateRatio(0);
			
			XWPFTable table = document.createTable();
			FileInputStream inputStream = new FileInputStream(excelFile.getPath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = null;
			if (workbook.getNumberOfSheets() > 1) {
				sheet = workbook.getSheet("Indicator Menu");
			} else {
				sheet = workbook.getSheet("Indicators");
			}
			
			int indexOfIndicator = 1;
			int indexOfData = 13;

			if (sheet != null) {
				Iterator<Row> rowIterator = sheet.iterator();

				while (rowIterator.hasNext()) {
					Row excelRow = rowIterator.next();
					XWPFTableRow wordRow = table.createRow();

					Cell excelIndicatorCell = excelRow.getCell(indexOfIndicator);
					Cell excelDataCell = excelRow.getCell(indexOfData);
					
					XWPFTableCell wordCellIndicator = wordRow.createCell();
					XWPFTableCell wordCellData = wordRow.createCell();
					
					wordCellIndicator.setText(excelIndicatorCell.getStringCellValue());
					wordCellData.setText(excelDataCell.getStringCellValue());
				}
			}
			table.removeRow(0);
			for(XWPFTableRow row: table.getRows()) {
				if(row.getTableCells().size() == 3) {
					row.removeCell(0);
				}
			}
			
			workbook.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//		for(String idString : ids) {
//			int id = Integer.parseInt(idString);
//			XSSFRow sourceRow= null;
//			if(id == 0) {
//				sourceRow = (XSSFRow) sheet.getRow(id);
//
//			}else {
//				sourceRow = (XSSFRow) sheet.getRow(id+1);
//
//			}
//			XSSFRow newRow = newSheet.createRow(destId);
//			
//			 for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
//		            // Grab a copy of the old/new cell
//		            XSSFCell oldCell = sourceRow.getCell(i);
//		            XSSFCell newCell = newRow.createCell(i);
//		            // If the old cell is null jump to next cell
//		            if (oldCell == null) {
//		                continue;
//		            }
//
//		            // Copy style from old cell and apply to new cell
//		            XSSFCellStyle newCellStyle = newWorkbook.createCellStyle();
//		            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
//		            newCell.setCellStyle(newCellStyle);
//
//		            // If there is a cell comment, copy
//		            if (oldCell.getCellComment() != null) {
//		                newCell.setCellComment(oldCell.getCellComment());
//		            }
//
//		            // If there is a cell hyperlink, copy
//		            if (oldCell.getHyperlink() != null) {
//		            	newCell.setCellValue(oldCell.getHyperlink().toString());
////		                newCell.setHyperlink(oldCell.getHyperlink());
//		            }
//
//		            // Set the cell data type
//		            newCell.setCellType(oldCell.getCellType());
//
//		            // Set the cell data value
//		            switch (oldCell.getCellType()) {
//		            case BLANK:// Cell.CELL_TYPE_BLANK:
//		                newCell.setCellValue(oldCell.getStringCellValue());
//		                break;
//		            case BOOLEAN:
//		                newCell.setCellValue(oldCell.getBooleanCellValue());
//		                break;
//		            case FORMULA:
//		                newCell.setCellFormula(oldCell.getCellFormula());
//		                break;
//		            case NUMERIC:
//		                newCell.setCellValue(oldCell.getNumericCellValue());
//		                break;
//		            case STRING:
//		                newCell.setCellValue(oldCell.getRichStringCellValue());
//		                break;
//		            default:
//		                break;
//		            }
//		        }
//			
//			destId++;
//		}

//	}

}
