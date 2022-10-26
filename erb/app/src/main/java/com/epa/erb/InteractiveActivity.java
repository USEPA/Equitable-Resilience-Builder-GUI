package com.epa.erb;

public class InteractiveActivity {
	
	private String id;
	private String name;
	private String status;
	public InteractiveActivity(String id, String name, String status) {
		this.id = id;
		this.name = name;
		this.status = status;
	}
	
	private String GUID;
	
	public InteractiveActivity cloneDynamicActivity() {
		InteractiveActivity clonedDynamicActivity = new InteractiveActivity(id,name, status);
		clonedDynamicActivity.setGUID(GUID);
		return clonedDynamicActivity;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
