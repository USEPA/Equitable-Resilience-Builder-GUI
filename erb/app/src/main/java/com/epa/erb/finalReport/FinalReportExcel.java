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
		if (excelFile != null) {
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
				for (XWPFTableRow row : table.getRows()) {
					if (row.getTableCells().size() == 3) {
						row.removeCell(0);
					}
				}

				workbook.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
