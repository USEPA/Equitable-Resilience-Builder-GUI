package com.epa.erb;

public class ActivityType {
	
	private String longName;
	private String shortName;
	private String description;
	private String fileExtension;
	public ActivityType(String longName, String shortName, String description, String fileExtension) {
		this.longName = longName;
		this.shortName = shortName;
		this.description = description;
		this.fileExtension = fileExtension;
	}
	
	public String getLongName() {
		return longName;
	}
	
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	
}
