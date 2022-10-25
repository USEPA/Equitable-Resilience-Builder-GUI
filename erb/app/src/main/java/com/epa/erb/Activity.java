package com.epa.erb;

import java.util.ArrayList;
import com.epa.erb.noteboard.NoteBoardContentController;

public class Activity {
	
	private String status;
	private String shortName;
	private String longName;
	private String activityID;
	private String notes;
	private String rating;
	public Activity(String status, String shortName, String longName, String activityID, String notes, String rating) {
		this.status = status;
		this.shortName = shortName;
		this.longName = longName;
		this.activityID = activityID;
		this.notes = notes;
		this.rating = rating;
	}
	
	private boolean isSaved = true;
	private NoteBoardContentController noteBoardContentController;
	
	private ArrayList<Step> assignedSteps = new ArrayList<Step>();
	private ArrayList<DynamicActivity> assignedDynamicActivities = new ArrayList<DynamicActivity>();
	
	private String GUID;
		
	public Activity() {
		
	}
	
	public Activity cloneActivity() {
		Activity clonedActivity = new Activity(status, shortName, longName, activityID, notes, rating);
		clonedActivity.setGUID(GUID);
		clonedActivity.setAssignedSteps(assignedSteps);
		clonedActivity.setAssignedDynamicActivities(assignedDynamicActivities);
		return clonedActivity;
	}
	
	public void resetAssignments() {
		assignedSteps.clear();
		assignedDynamicActivities.clear();
	}
	
	public void addStep(Step step) {
		if (step != null) {
			assignedSteps.add(step);
		}
	}
	
	public void removeStep(Step step) {
		if (step != null) {
			assignedSteps.remove(step);
		}
	}
	
	public void addDynamicActivity(DynamicActivity dynamicActivity) {
		if (dynamicActivity != null) {
			assignedDynamicActivities.add(dynamicActivity);
		}
	}
	
	public void removeDynamicActivity(DynamicActivity dynamicActivity) {
		if (dynamicActivity != null) {
			assignedDynamicActivities.remove(dynamicActivity);
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getActivityID() {
		return activityID;
	}

	public void setActivityID(String activityID) {
		this.activityID = activityID;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public NoteBoardContentController getNoteBoardContentController() {
		return noteBoardContentController;
	}

	public void setNoteBoardContentController(NoteBoardContentController noteBoardContentController) {
		this.noteBoardContentController = noteBoardContentController;
	}

	public ArrayList<Step> getAssignedSteps() {
		return assignedSteps;
	}

	public void setAssignedSteps(ArrayList<Step> assignedSteps) {
		this.assignedSteps = assignedSteps;
	}

	public ArrayList<DynamicActivity> getAssignedDynamicActivities() {
		return assignedDynamicActivities;
	}

	public void setAssignedDynamicActivities(ArrayList<DynamicActivity> assignedDynamicActivities) {
		this.assignedDynamicActivities = assignedDynamicActivities;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

}
