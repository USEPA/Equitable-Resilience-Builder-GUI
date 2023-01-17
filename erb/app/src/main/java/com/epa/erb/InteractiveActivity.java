package com.epa.erb;

public class InteractiveActivity extends ERBItem {
	
	private String id;
	private String guid;
	private String longName;
	private String shortName;
	private String status;
	
	public InteractiveActivity(String id, String guid, String longName, String shortName, String status) {
		super(id, guid, longName, shortName, null);
		this.status = status;
	}
	
	public InteractiveActivity cloneDynamicActivity() {
		InteractiveActivity clonedInteractiveActivity = new InteractiveActivity(id, guid, longName, shortName, status);
		return clonedInteractiveActivity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
