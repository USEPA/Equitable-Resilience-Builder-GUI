package com.epa.erb.chapter;

import java.util.ArrayList;
import com.epa.erb.Activity;

public class Chapter {
	
	private int chapterNum;
	private String numericName;
	private String stringName;
	private String descriptionName;
	private double planStatus;
	private double engageStatus;
	private double reflectStatus;
	public Chapter(int chapterNum, String numericName, String stringName, String descriptionName, double planStatus, double engageStatus, double reflectStatus) {
		this.chapterNum = chapterNum;
		this.numericName = numericName;
		this.stringName = stringName;
		this.descriptionName = descriptionName;
		this.planStatus = planStatus;
		this.engageStatus = engageStatus;
		this.reflectStatus = reflectStatus;
	}	

	private ArrayList<Activity> userSelectedActivities = new ArrayList<Activity>();
	
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
	
	public double getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(double planStatus) {
		this.planStatus = planStatus;
	}

	public double getEngageStatus() {
		return engageStatus;
	}

	public void setEngageStatus(double engageStatus) {
		this.engageStatus = engageStatus;
	}

	public double getReflectStatus() {
		return reflectStatus;
	}

	public void setReflectStatus(double reflectStatus) {
		this.reflectStatus = reflectStatus;
	}

	public ArrayList<Activity> getUserSelectedActivities() {
		return userSelectedActivities;
	}
	
	public void setUserSelectedActivities(ArrayList<Activity> userSelectedActivities) {
		this.userSelectedActivities = userSelectedActivities;
	}

	public void addUserSelectedActivity(Activity activity) {
		userSelectedActivities.add(activity);
	}
	
	public void removeUserSelectedActivity(Activity activity) {
		userSelectedActivities.remove(activity);
	}
	
	public int getNumberOfUserSelectedActivities() {
		return userSelectedActivities.size();
	}
	
	public Activity getPlanActivity() {
		for(Activity activity : userSelectedActivities) {
			if(activity.getLongName().contentEquals("Plan")) {
				return activity;
			}
		}
		return null;
	}
	
	public Activity getReflectActivity(){
		for(Activity activity : userSelectedActivities) {
			if(activity.getLongName().contentEquals("Reflect")) {
				return activity;
			}
		}
		return null;
	}
	
	public int getNumberOfReadyActivities() {
		int count = 0;
		for(Activity activity: userSelectedActivities) {
			if(activity.getStatus().contentEquals("ready")) {
				count++;
			}
		}
		return count;
	}
	
	public int getNumberOfInProgressActivities() {
		int count = 0;
		for(Activity activity: userSelectedActivities) {
			if(activity.getStatus().contentEquals("in progress")) {
				count++;
			}
		}
		return count;
	}
	
	public int getNumberOfSkippedActivities() {
		int count = 0;
		for(Activity activity: userSelectedActivities) {
			if(activity.getStatus().contentEquals("skipped")) {
				count++;
			}
		}
		return count;
	}
	
	public int getNumberOfCompletedActivities() {
		int count = 0;
		for(Activity activity: userSelectedActivities) {
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
				"User Selected Activities: " + userSelectedActivities.size();
	}
	
}
