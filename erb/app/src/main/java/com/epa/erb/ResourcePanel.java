package com.epa.erb;

public class ResourcePanel {
	
	String id;
	String longName;
	String type;
	public ResourcePanel(String id, String longName, String type) {
		this.id = id;
		this.longName = longName;
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
