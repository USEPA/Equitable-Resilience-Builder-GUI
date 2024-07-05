package com.epa.erb.finalReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

public class FinalReportTable {

	private File file;
	private String tableName;
	private String start;
	private String stop;
	private XWPFDocument reportDoc;

	public FinalReportTable(File file, String tableName, String start, String stop, XWPFDocument reportDoc) {
		this.file = file;
		this.tableName = tableName;
		this.start = start;
		this.stop = stop;
		this.reportDoc = reportDoc;
	}

	public void writeTableToReportDoc() {
		try {
			XWPFDocument doc = new XWPFDocument(new FileInputStream(file.getPath()));
			for (XWPFTable table : doc.getTables()) {
				String tableName = findTableAltText(table);
				if (tableName != null && tableName.contentEquals(tableName)) {
					copyTable(table);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String findTableAltText(XWPFTable table) {
		String caption = null;
		if (table != null) {
			CTTbl tableXML = table.getCTTbl();
			String[] xml = tableXML.toString().split(System.lineSeparator());
			for (String x : xml) {
				if (x.contains("w:tblCaption")) {
					caption = x.split("w:val=")[1].replace("/>", "");
					caption = caption.replace("\"", "");
				}
			}
		}
		return caption;
	}

	private void copyTable(XWPFTable table) {

		int startReading = Integer.parseInt(start);
		int stopReading = Integer.parseInt(stop);
		
		int numColumns = stopReading - startReading;

		XWPFTable newTable = reportDoc.createTable();
		int originalNumRows = table.getRows().size();

		for (XWPFTableRow row : table.getRows()) {
			XWPFTableRow newRow = newTable.createRow();

			for (int i = startReading; i < stopReading; i++) {
				XWPFTableCell newCell = newRow.createCell();

				String text = "";
				boolean cellBold = false;
				boolean cellCaps = true;
				String font = "Calibri";
				double fontSize = 12;
				int cellSize = 1000;
				
				if (row.getCell(i) != null) {
					text = row.getCell(i).getText();
					cellSize = row.getCell(i).getWidth();
					if (text.trim().length() > 0) {
						try {
						cellBold = row.getCell(i).getParagraphs().get(0).getRuns().get(0).isBold();
						cellCaps = row.getCell(i).getParagraphs().get(0).getRuns().get(0).isCapitalized();
						font = row.getCell(i).getParagraphs().get(0).getRuns().get(0).getFontFamily();
						if (row.getCell(i).getParagraphs().get(0).getRuns().get(0).getFontSizeAsDouble() != null) {
							fontSize = row.getCell(i).getParagraphs().get(0).getRuns().get(0).getFontSizeAsDouble();
						}
						}catch(Exception e) {
						}
					}
					newCell.setWidth(String.valueOf(cellSize));
				}
				XWPFParagraph paragraph = newRow.getCell(i).addParagraph();
				XWPFRun run = paragraph.createRun();
				run.setText(text);
				run.setBold(cellBold);
				run.setCapitalized(cellCaps);
				run.setFontFamily(font);
				run.setFontSize(fontSize);

			}
		}

		if (newTable.getRows().size() != originalNumRows) {
			newTable.removeRow(0);
		}
		int newTableColumns = newTable.getRow(0).getTableCells().size();
		if(newTableColumns != numColumns) {
			for(XWPFTableRow row: newTable.getRows()) {
				row.removeCell(numColumns);
			}
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public XWPFDocument getReportDoc() {
		return reportDoc;
	}

	public void setReportDoc(XWPFDocument reportDoc) {
		this.reportDoc = reportDoc;
	}

}
