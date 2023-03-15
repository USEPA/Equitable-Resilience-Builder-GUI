package com.epa.erb.engagement_action;

import javafx.scene.text.Text;

public class MyPortfolioItem {
	
	private int fileNumber;
	private Text fileName;
	private String modifiedDate;
	public MyPortfolioItem(int fileNumber, Text fileName,String modifiedDate) {
		this.fileNumber = fileNumber;
		this.fileName = fileName;
		this.modifiedDate = modifiedDate;
	}
	public int getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}
	public Text getFileName() {
		return fileName;
	}
	public void setFileName(Text fileName) {
		this.fileName = fileName;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
