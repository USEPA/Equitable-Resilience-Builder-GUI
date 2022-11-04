package com.epa.erb;

public class ERBItem {
	
	String id;
	String guid;
	String longName;
	String shortName;
	Object object;
	public ERBItem(String id, String guid, String longName,String shortName, Object object) {
		this.id = id;
		this.guid = guid;
		this.longName = longName;
		this.shortName = shortName;
		this.object = object;
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
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
