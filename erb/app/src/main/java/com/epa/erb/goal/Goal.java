package com.epa.erb.goal;

import java.util.ArrayList;

public class Goal {
	
	private String name;
	private String description;
	private ArrayList<GoalCategory> listOfSelectedGoalCategories;
	public Goal(String name, String description, ArrayList<GoalCategory> listOfSelectedGoalCategories ) {
		this.name = name;
		this.description = description;
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<GoalCategory> getListOfSelectedGoalCategories() {
		return listOfSelectedGoalCategories;
	}
	public void setListOfSelectedGoalCategories(ArrayList<GoalCategory> listOfSelectedGoalCategories) {
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
	}

}
