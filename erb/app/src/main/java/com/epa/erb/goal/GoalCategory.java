package com.epa.erb.goal;

import java.util.ArrayList;

public class GoalCategory {
	
	private String categoryName;
	private ArrayList<String> listOfAssignedActivityIDs;
	public GoalCategory(String categoryName, ArrayList<String> listOfAssignedActivityIDs) {
		this.categoryName = categoryName;
		this.listOfAssignedActivityIDs = listOfAssignedActivityIDs;
	}
	
	public String toString() {
		return "Category Name: " + categoryName + "\n" + 
				"List of assigned IDs: " + listOfAssignedActivityIDs;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public ArrayList<String> getListOfAssignedActivityIds() {
		return listOfAssignedActivityIDs;
	}
	public void setListOfAssignedActivities(ArrayList<String> listOfAssignedActivityIDs) {
		this.listOfAssignedActivityIDs = listOfAssignedActivityIDs;
	}

}
