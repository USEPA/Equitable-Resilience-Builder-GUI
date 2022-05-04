package com.epa.erb.goal;

import java.util.ArrayList;

import com.epa.erb.Activity;

public class GoalCategory {
	
	private String categoryName;
	private ArrayList<Activity> listOfAssignedActivities;
	public GoalCategory(String categoryName, ArrayList<Activity> listOfAssignedActivities) {
		this.categoryName = categoryName;
		this.listOfAssignedActivities = listOfAssignedActivities;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public ArrayList<Activity> getListOfAssignedActivities() {
		return listOfAssignedActivities;
	}
	public void setListOfAssignedActivities(ArrayList<Activity> listOfAssignedActivities) {
		this.listOfAssignedActivities = listOfAssignedActivities;
	}

}
