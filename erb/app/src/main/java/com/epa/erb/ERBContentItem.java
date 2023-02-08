package com.epa.erb;

public class ERBContentItem {
	
	String id;
	String guid;
	String type;
	String status;
	String longName;
	String shortName;
	public ERBContentItem(String id, String guid, String type, String status, String longName, String shortName) {
		this.id = id;
		this.guid = guid;
		this.type = type;
		this.status = status;
		this.longName = longName;
		this.shortName = shortName;
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

}
