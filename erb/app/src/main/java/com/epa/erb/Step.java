package com.epa.erb;

import java.util.ArrayList;

public class Step extends ERBItem {

	private String id;
	private String guid;
	private String longName;
	private String shortName;

	private String status;
	private String notes;
	private String rating;
	private String type;

	public Step(String id, String guid, String longName, String shortName, String status, String notes, String rating, String type) {
		super(id, guid, longName, shortName, null);
		this.status = status;
		this.notes = notes;
		this.rating = rating;
		this.type = type;
	}

	private ArrayList<InteractiveActivity> assignedDynamicActivities = new ArrayList<InteractiveActivity>();

	public Step cloneStep() {
		Step clonedStep = new Step(id, guid, longName, shortName, status, notes, rating, type);
		clonedStep.setAssignedDynamicActivities(assignedDynamicActivities);
		return clonedStep;
	}

	public void addDynamicActivity(InteractiveActivity dynamicActivity) {
		if (dynamicActivity != null) {
			assignedDynamicActivities.add(dynamicActivity);
		}
	}

	public void removeDynamicActivity(InteractiveActivity dynamicActivity) {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<InteractiveActivity> getAssignedDynamicActivities() {
		return assignedDynamicActivities;
	}

	public void setAssignedDynamicActivities(ArrayList<InteractiveActivity> assignedDynamicActivities) {
		this.assignedDynamicActivities = assignedDynamicActivities;
	}

}
