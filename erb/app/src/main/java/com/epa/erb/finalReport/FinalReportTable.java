package com.epa.erb.finalReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

public class FinalReportTable {
	
	private File file;
	private String tableName;
	private XWPFDocument reportDoc;
	public FinalReportTable(File file, String tableName, XWPFDocument reportDoc) {
		this.file = file;
		this.tableName = tableName;
		this.reportDoc = reportDoc;
	}
	
	public String getTableText() {		 
		try {
			XWPFDocument doc = new XWPFDocument(new FileInputStream(file.getPath()));
			for (XWPFTable table : doc.getTables()) {
				String tableName = findTableAltText(table);
				if(tableName != null && tableName.contentEquals(tableName)) {
					return table.getText();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ArrayList<XWPFTable> getTables() {
		ArrayList<XWPFTable> tableList = new ArrayList<XWPFTable>();
		try {
			XWPFDocument doc = new XWPFDocument(new FileInputStream(file.getPath()));
			for (XWPFTable table : doc.getTables()) {
				tableList.add(table);
			}
		} catch (IOException e) {
			e.printStackTrace();
			tableList = null;
		}
		return tableList;
	}
	
	public String findTableAltText(XWPFTable table) {
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
	
	public void createTableCopy() {
		XWPFTable newTable = reportDoc.createTable();
		try {
			XWPFDocument doc = new XWPFDocument(new FileInputStream(file.getPath()));
			int numRows =0;
			for (XWPFTable table : doc.getTables()) {
				String tableName = findTableAltText(table);
				if(tableName != null && tableName.contentEquals(tableName)) {
					int rowNum = 0;
					numRows = table.getRows().size();
					for(XWPFTableRow row: table.getRows()) {
						newTable.addRow(row, rowNum);
						rowNum++;
					}
				}
			}
			if(newTable.getRows().size() != numRows) {
				newTable.removeRow(numRows);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
