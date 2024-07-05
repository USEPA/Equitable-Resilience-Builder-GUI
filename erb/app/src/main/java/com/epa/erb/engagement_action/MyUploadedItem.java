package com.epa.erb.engagement_action;

import java.io.File;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import javafx.scene.text.Text;

public class MyUploadedItem {
	
	private App app;
	
	private Text fileName;
	private int fileNumber;
	private String modifiedDate;
	private String uploadedFrom;
	public MyUploadedItem(int fileNumber, Text fileName,String modifiedDate, String uploadedFrom, App app) {
		this.fileName = fileName;
		this.fileNumber = fileNumber;
		this.modifiedDate = modifiedDate;
		this.uploadedFrom = uploadedFrom;
		
		this.app = app;
	}
	
	private boolean selectedForExport;
	public MyUploadedItem(boolean selectedForExport, int fileNumber, Text fileName,String modifiedDate, App app) {
		this.fileName = fileName;
		this.fileNumber = fileNumber;
		this.modifiedDate = modifiedDate;
		this.selectedForExport = selectedForExport;
		
		this.app = app;
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
	public String toString() {
		return "File Name: " + fileName + "\n" + 
				"File Number: " + fileNumber + "\n" +
				"Modified Date: " + modifiedDate + "\n" + 
				"Selected for Export: " + selectedForExport + "\n" +
				"Uploaded From: " + uploadedFrom;
	}
	
	public File getFile() {
		FileHandler fileHandler = new FileHandler();
		return new File(fileHandler.getMyUploadsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal()) + File.separator + getFileNumber() + File.separator + getFileName().getText());
	}
	
}
