package com.epa.erb.engagement_setup;

public class SelectedActivity {

	String showName; 
	String activityType;
	String activityID;
	
	public SelectedActivity(String showName, String activityType) {
		this.showName = showName;
		this.activityType = activityType;
	}
	
	public SelectedActivity(String showName, String activityType, String activityID) {
		this.showName = showName;
		this.activityType = activityType;
		this.activityID = activityID;
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

	public String getActivityID() {
		return activityID;
	}

	public void setActivityID(String activityID) {
		this.activityID = activityID;
	}
}
