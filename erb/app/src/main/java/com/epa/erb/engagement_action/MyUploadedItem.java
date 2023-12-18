package com.epa.erb.engagement_action;

import javafx.scene.text.Text;

public class MyUploadedItem {
	
	private Text fileName;
	private int fileNumber;
	private String modifiedDate;
	private String uploadedFrom;
	public MyUploadedItem(int fileNumber, Text fileName,String modifiedDate, String uploadedFrom) {
		this.fileName = fileName;
		this.fileNumber = fileNumber;
		this.modifiedDate = modifiedDate;
		this.uploadedFrom = uploadedFrom;
	}
	
	private boolean selectedForExport;
	public MyUploadedItem(boolean selectedForExport, int fileNumber, Text fileName,String modifiedDate) {
		this.fileName = fileName;
		this.fileNumber = fileNumber;
		this.modifiedDate = modifiedDate;
		this.selectedForExport = selectedForExport;
	}
	
	public boolean isSelectedForExport() {
		return selectedForExport;
	}
	public void setSelectedForExport(boolean selectedForExport) {
		this.selectedForExport = selectedForExport;
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
