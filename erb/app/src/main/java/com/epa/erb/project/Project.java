package com.epa.erb.project;

import java.util.ArrayList;

import com.epa.erb.goal.Goal;

public class Project {

	private String projectName;
	public Project(String projectName) {
		this.projectName = projectName;
	}
	
	private ArrayList<Goal> projectGoals;
	public Project(String projectName, ArrayList<Goal> projectGoals) {
		this.projectName = projectName;
		this.projectGoals = projectGoals;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public ArrayList<Goal> getProjectGoals() {
		return projectGoals;
	}
	public void setProjectGoals(ArrayList<Goal> projectGoals) {
		this.projectGoals = projectGoals;
	}
	
}
