package com.epa.erb.chapter;

import java.util.ArrayList;
import java.util.HashMap;
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
		
	private boolean saved = true;
	private ArrayList<Step> steps = new ArrayList<Step>();
	private ArrayList<Activity> assignedActivities = new ArrayList<Activity>();
	private HashMap<String, String> planHashMap;	
	private HashMap<String, String> reflectHashMap;
	
	public Chapter() {
		
	}
	
	public void assignSteps(ArrayList<Step> allSteps) {
		ArrayList<Step> chapterSteps = new ArrayList<Step>();
		if (allSteps != null) {
			for (Step step : allSteps) {
				if (step.getActivityAssignment().contentEquals(chapterNum + "0")) {
					int sizeOfChapterList = chapterSteps.size();
					chapterSteps.add(sizeOfChapterList, step);
				}
			}
		}
		setSteps(chapterSteps);
	}
	
	public void setHashMaps() {
		setPlanHashMapDefaults();
		setReflectHashMapDefaults();
	}
	
	private void setPlanHashMapDefaults() {
		planHashMap = new HashMap<String, String>();
		planHashMap.put("pAudience", "");
		planHashMap.put("pGoals", "");
		planHashMap.put("pActivities", "");
		planHashMap.put("pNotes", "");
	}
	
	private void setReflectHashMapDefaults() {
		reflectHashMap = new HashMap<String, String>();
		reflectHashMap.put("rAudience", "");
		reflectHashMap.put("rGoals", "");
		reflectHashMap.put("rActivities", "");
		reflectHashMap.put("rNotes", "");
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
		
	public boolean isActivitiesSaved() {
		for(Activity activity: assignedActivities) {
			if(!activity.isSaved()) {
				return false;
			}
		}
		return true;
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

	public ArrayList<Activity> getAssignedActivities() {
		return assignedActivities;
	}

	public void setAssignedActivities(ArrayList<Activity> assignedActivities) {
		this.assignedActivities = assignedActivities;
	}
	
	public boolean isSaved() {
		return saved && isActivitiesSaved();
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public HashMap<String, String> getPlanHashMap() {
		return planHashMap;
	}

	public void setPlanHashMap(HashMap<String, String> planHashMap) {
		this.planHashMap = planHashMap;
	}

	public HashMap<String, String> getReflectHashMap() {
		return reflectHashMap;
	}

	public void setReflectHashMap(HashMap<String, String> reflectHashMap) {
		this.reflectHashMap = reflectHashMap;
	}
	
	public ArrayList<Step> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}
		
}
