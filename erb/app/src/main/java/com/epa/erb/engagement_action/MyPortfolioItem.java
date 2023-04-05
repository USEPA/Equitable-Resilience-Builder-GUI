package com.epa.erb.engagement_action;

import javafx.scene.text.Text;

public class MyPortfolioItem {
	
	private int fileNumber;
	private Text fileName;
	private String modifiedDate;
	private String uploadedFrom;
	public MyPortfolioItem(int fileNumber, Text fileName,String modifiedDate, String uploadedFrom) {
		this.fileNumber = fileNumber;
		this.fileName = fileName;
		this.modifiedDate = modifiedDate;
		this.uploadedFrom = uploadedFrom;
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
	public String getUploadedFrom() {
		return uploadedFrom;
	}
	public void setUploadedFrom(String uploadedFrom) {
		this.uploadedFrom = uploadedFrom;
	}
	
}
