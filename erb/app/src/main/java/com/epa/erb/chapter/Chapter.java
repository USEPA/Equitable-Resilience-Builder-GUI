package com.epa.erb.chapter;

import java.util.ArrayList;
import com.epa.erb.Activity;

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
	
	public Chapter() {
		
	}

	private ArrayList<Activity> assignedActivities = new ArrayList<Activity>();
	
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

	public void addActivity(Activity activity) {
		assignedActivities.add(activity);
	}
	
	public void removeActivity(Activity activity) {
		assignedActivities.remove(activity);
	}
	
	public int getNumberOfAssignedActivities() {
		return assignedActivities.size();
	}
	
	public Activity getPlanActivity() {
		for(Activity activity : assignedActivities) {
			if(activity.getLongName().contentEquals("Plan")) {
				return activity;
			}
		}
		return null;
	}
	
	public Activity getReflectActivity(){
		for(Activity activity : assignedActivities) {
			if(activity.getLongName().contentEquals("Reflect")) {
				return activity;
			}
		}
		return null;
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
	
	public int getNumberOfSkippedActivities() {
		int count = 0;
		for(Activity activity: assignedActivities) {
			if(activity.getStatus().contentEquals("skipped")) {
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
	
	public String toString() {
		return  "----------Chapter---------" + "\n" +
				"Chapter Num: " + chapterNum + "\n" + 
				"Numeric Name: " + numericName + "\n" + 
				"String Name: " + stringName + "\n" + 
				"Description Name: " + descriptionName + "\n" +
				"User Selected Activities: " + assignedActivities.size();
	}
	
}
