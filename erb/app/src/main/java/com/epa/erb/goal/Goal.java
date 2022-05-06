package com.epa.erb.goal;

import java.util.ArrayList;

public class Goal {
	
	private String goalName;
	private String goalDescription;
	private ArrayList<GoalCategory> listOfSelectedGoalCategories;
	public Goal(String goalName, String goalDescription, ArrayList<GoalCategory> listOfSelectedGoalCategories ) {
		this.goalName = goalName;
		this.goalDescription = goalDescription;
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
	}
	
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public String getGoalDescription() {
		return goalDescription;
	}
	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
	}
	public ArrayList<GoalCategory> getListOfSelectedGoalCategories() {
		return listOfSelectedGoalCategories;
	}
	public void setListOfSelectedGoalCategories(ArrayList<GoalCategory> listOfSelectedGoalCategories) {
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
	}

}
