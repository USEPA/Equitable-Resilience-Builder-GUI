package com.epa.erb;

import java.util.ArrayList;

public class ContentPanel extends ERBItem {
	
	String id;
	String guid;
	String longName;
	String shortName;
	String status;
	String type;
	public ContentPanel(String id, String guid, String longName, String shortName, String status, String type) {
		super(id, guid, longName, shortName, null);
		this.id = id;
		this.guid = guid;
		this.longName = longName;
		this.shortName = shortName;
		this.status = status;
		this.type = type;
	}
	
	ArrayList<ContentPanel> nextLayerContentPanels = new ArrayList<ContentPanel>();
	
	public void addNextLayerContentPanel(ContentPanel contentPanel) {
		if(contentPanel != null) {
			nextLayerContentPanels.add(contentPanel);
		}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<ContentPanel> getNextLayerContentPanels() {
		return nextLayerContentPanels;
	}
	public void setNextLayerContentPanels(ArrayList<ContentPanel> nextLayerContentPanels) {
		this.nextLayerContentPanels = nextLayerContentPanels;
	}

}
