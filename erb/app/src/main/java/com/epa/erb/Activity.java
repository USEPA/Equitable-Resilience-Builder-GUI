package com.epa.erb;

import java.util.ArrayList;
import com.epa.erb.noteboard.NoteBoardContentController;

public class Activity {
	
	private String chapterAssignment; 
	private String status;
	private String shortName;
	private String longName;
	private String fileName;
	private String activityID;
	private String notes;
	private String rating;
	private ArrayList<String> assignedDynamicActivityIds;
	private ArrayList<String> assignedStepIds;
	public Activity(String chapterAssignment, String status, String shortName, String longName, String fileName, String activityID, String notes, String rating, ArrayList<String> assignedDynamicActivityIds, ArrayList<String> assignedStepIds) {
		this.chapterAssignment = chapterAssignment;
		this.status = status;
		this.shortName = shortName;
		this.longName = longName;
		this.fileName = fileName;
		this.activityID = activityID;
		this.notes = notes;
		this.rating = rating;
		this.assignedDynamicActivityIds = assignedDynamicActivityIds;
		this.assignedStepIds = assignedStepIds;
	}
	
	private boolean isSaved = true;
	private NoteBoardContentController noteBoardContentController;
	
	private ArrayList<Step> assignedSteps = new ArrayList<Step>();
	private ArrayList<DynamicActivity> assignedDynamicActivities = new ArrayList<DynamicActivity>();
	
	private String GUID;
		
	public Activity() {
		
	}
	
	public Activity cloneActivity() {
		Activity clonedActivity = new Activity(chapterAssignment, status, shortName, longName, fileName, activityID, notes, rating, assignedDynamicActivityIds, assignedStepIds);
		clonedActivity.setGUID(GUID);
		clonedActivity.setAssignedSteps(assignedSteps);
		clonedActivity.setAssignedDynamicActivities(assignedDynamicActivities);
		return clonedActivity;
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

	public ArrayList<String> getAssignedStepIds() {
		return assignedStepIds;
	}

	public void setAssignedStepIds(ArrayList<String> assignedStepIds) {
		this.assignedStepIds = assignedStepIds;
	}

	public ArrayList<String> getAssignedDynamicActivityIds() {
		return assignedDynamicActivityIds;
	}

	public void setAssignedDynamicActivityIds(ArrayList<String> assignedDynamicActivityIds) {
		this.assignedDynamicActivityIds = assignedDynamicActivityIds;
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
