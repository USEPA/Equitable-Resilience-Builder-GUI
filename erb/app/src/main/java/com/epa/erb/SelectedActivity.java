package com.epa.erb;

public class SelectedActivity {

	String showName; 
	String activityType;
	String activityGUID;
	
	public SelectedActivity(String showName, String activityType) {
		this.showName = showName;
		this.activityType = activityType;
	}
	
	public SelectedActivity(String showName, String activityType, String activityGUID) {
		this.showName = showName;
		this.activityType = activityType;
		this.activityGUID = activityGUID;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityGUID() {
		return activityGUID;
	}

	public void setActivityGUID(String activityGUID) {
		this.activityGUID = activityGUID;
	}
}
