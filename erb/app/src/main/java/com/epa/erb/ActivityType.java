package com.epa.erb;

public class ActivityType {
	
	String longName;
	String shortName;
	String description;
	String fileExt;
	
	public ActivityType(String longName, String shortName, String description, String fileExt) {
		this.longName = longName;
		this.shortName = shortName;
		this.description = description;
		this.fileExt = fileExt;
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

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	
	public String toString() {
		return  "Long Name: " + longName + "\n" + 
				"Short Name: " + shortName + "\n" + 
				"Description: " + description + "\n" + 
				"File Ext: " + fileExt;
	}
	
}
