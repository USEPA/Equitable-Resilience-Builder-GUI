package com.epa.erb.chapter;

import java.util.ArrayList;
import com.epa.erb.Activity;
import com.epa.erb.Step;

public class Chapter {
	
	private int chapterNum;
	private String numericName;
	private String stringName;
	private String descriptionName;
	private String notes;
	public Chapter(int chapterNum, String numericName, String stringName, String descriptionName, String notes) {
		this.chapterNum = chapterNum;
		this.numericName = numericName;
		this.stringName = stringName;
		this.descriptionName = descriptionName;
		this.notes = notes;
	}
		
	private String GUID;
	private ArrayList<Step> assignedSteps = new ArrayList<Step>();
	private ArrayList<Activity> assignedActivities = new ArrayList<Activity>();

	public Chapter() {
		
	}
	
	public Chapter cloneChapter() {
		Chapter clonedChapter = new Chapter(chapterNum, numericName, stringName, descriptionName, notes);
		clonedChapter.setGUID(GUID);
		clonedChapter.setAssignedSteps(assignedSteps);
		clonedChapter.setAssignedActivities(assignedActivities);
		return clonedChapter;
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

	public void addActivity(Activity activity) {
		if (activity != null) {
			assignedActivities.add(activity);
		}
	}
	
	public void removeActivity(Activity activity) {
		if (activity != null) {
			assignedActivities.remove(activity);
		}
	}

	public int getNumberOfAssignedActivities() {
		return assignedActivities.size();
	}
		
	
	public int getNumberOfReadyActivities() {
		int count = 0;
		for(Activity activity: assignedActivities) {
			if(activity.getStatus().contentEquals("ready")) {
				count++;
			}
		}
		return count;
	}
	
	public int getNumberOfInProgressActivities() {
		int count = 0;
		for(Activity activity: assignedActivities) {
			if(activity.getStatus().contentEquals("in progress")) {
				count++;
			}
		}
		return count;
	}
	
	public int getNumberOfCompletedActivities() {
		int count = 0;
		for(Activity activity: assignedActivities) {
			if(activity.getStatus().contentEquals("complete")) {
				if (!activity.getLongName().contentEquals("Plan") && !activity.getLongName().contentEquals("Reflect")) {
					count++;
				}
			}
		}
		return count;
	}
	
	public String getStatus() {
		boolean isReady = true; // if all activities are ready
		boolean isComplete = true; // if all activities are complete
		if (assignedActivities.size() > 0) {
			for (Activity activity : assignedActivities) {
				if (activity.getStatus().contentEquals("ready")) {
					isComplete = false;
				} else if (activity.getStatus().contentEquals("in progress")) {
					isComplete = false;
					isReady = false;
				} else if (activity.getStatus().contentEquals("complete")) {
					isReady = false;
				}
			}
			if (isComplete) {
				return "complete";
			} else if (isReady) {
				return "ready";
			} else {
				return "in progress";
			}
		} else {
			return "ready";
		}
	}

	public int getChapterNum() {
		return chapterNum;
	}

	public void setChapterNum(int chapterNum) {
		this.chapterNum = chapterNum;
	}

	public String getNumericName() {
		return numericName;
	}

	public void setNumericName(String numericName) {
		this.numericName = numericName;
	}

	public String getStringName() {
		return stringName;
	}

	public void setStringName(String stringName) {
		this.stringName = stringName;
	}

	public String getDescriptionName() {
		return descriptionName;
	}

	public void setDescriptionName(String descriptionName) {
		this.descriptionName = descriptionName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public ArrayList<Step> getAssignedSteps() {
		return assignedSteps;
	}

	public void setAssignedSteps(ArrayList<Step> assignedSteps) {
		this.assignedSteps = assignedSteps;
	}

	public ArrayList<Activity> getAssignedActivities() {
		return assignedActivities;
	}

	public void setAssignedActivities(ArrayList<Activity> assignedActivities) {
		this.assignedActivities = assignedActivities;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}
		
}
