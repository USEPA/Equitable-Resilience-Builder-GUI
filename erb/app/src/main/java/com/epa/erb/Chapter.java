package com.epa.erb;

import java.util.ArrayList;

public class Chapter {
	
	int chapterNum;
	String numericName;
	String stringName;
	String descriptionName;
	public Chapter(int chapterNum, String numericName, String stringName, String descriptionName) {
		this.chapterNum = chapterNum;
		this.numericName = numericName;
		this.stringName = stringName;
		this.descriptionName = descriptionName;
	}	

	ArrayList<Activity> userSelectedActivities = new ArrayList<Activity>();
	
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
	
	public String getUserSelectedActivitiesString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");
		for(Activity activity : userSelectedActivities) {
			stringBuilder.append(activity.toString());
		}
		return stringBuilder.toString();
	}
	
	public String toString() {
		return  "----------Chapter---------" + "\n" +
				"Chapter Num: " + chapterNum + "\n" + 
				"Numeric Name: " + numericName + "\n" + 
				"String Name: " + stringName + "\n" + 
				"Description Name: " + descriptionName + "\n" +
				"--User Selected Activities-- " + getUserSelectedActivitiesString() + "----";
	}
	
}
