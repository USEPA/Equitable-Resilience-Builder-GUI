package com.epa.erb;

import java.util.ArrayList;

public class Step {
	
	private String stepType;
	private String activityAssignment; 
	private String chapterAssignment; 
	private String status;
	private String shortName;
	private String longName;
	private String fileName;
	private String stepID;
	private String notes;
	private String rating;
	private ArrayList<String> assignedDynamicActivityIds;
	public Step(String stepType, String activityAssignment, String chapterAssignment, String status, String shortName, String longName, String fileName, String stepID, String notes, String rating,ArrayList<String> assignedDynamicActivityIds) {
		this.stepType = stepType;
		this.activityAssignment = activityAssignment;
		this.chapterAssignment = chapterAssignment;
		this.status = status;
		this.shortName = shortName;
		this.longName = longName;
		this.fileName = fileName;
		this.stepID = stepID;
		this.notes = notes;
		this.rating = rating;
		this.assignedDynamicActivityIds = assignedDynamicActivityIds;
	}
	
	private ArrayList<DynamicActivity> assignedDynamicActivities = new ArrayList<DynamicActivity>();
	
	private String GUID;
	
	public Step cloneStep() {
		Step clonedStep = new Step(stepType, activityAssignment, chapterAssignment, status, shortName, longName, fileName, stepID, notes, rating, assignedDynamicActivityIds);
		clonedStep.setGUID(GUID);
		clonedStep.setAssignedDynamicActivities(assignedDynamicActivities);
		return clonedStep;
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

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public String getActivityAssignment() {
		return activityAssignment;
	}

	public void setActivityAssignment(String activityAssignment) {
		this.activityAssignment = activityAssignment;
	}

	public String getChapterAssignment() {
		return chapterAssignment;
	}

	public void setChapterAssignment(String chapterAssignment) {
		this.chapterAssignment = chapterAssignment;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String activityID) {
		this.stepID = activityID;
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

	public ArrayList<String> getAssignedDynamicActivityIds() {
		return assignedDynamicActivityIds;
	}

	public void setAssignedDynamicActivityIds(ArrayList<String> assignedDynamicActivityIds) {
		this.assignedDynamicActivityIds = assignedDynamicActivityIds;
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
