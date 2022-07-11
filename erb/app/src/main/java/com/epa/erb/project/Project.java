package com.epa.erb.project;

import java.util.ArrayList;
import com.epa.erb.goal.Goal;

public class Project {

	private String projectName;
	private String projectCleanedName;
	public Project(String projectName, String projectCleanedName) {
		this.projectName = projectName;
		this.projectCleanedName = projectCleanedName;
	}
	
	private ArrayList<Goal> projectGoals;
	public Project(String projectName, String projectCleanedName, ArrayList<Goal> projectGoals) {
		this.projectName = projectName;
		this.projectCleanedName = projectCleanedName;
		this.projectGoals = projectGoals;
	}
	
	public boolean isSaved() {
		if (projectGoals != null) {
			for (Goal goal : projectGoals) {
				if (!goal.isSaved()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getProjectCleanedName() {
		return projectCleanedName;
	}

	public void setProjectCleanedName(String projectCleanedName) {
		this.projectCleanedName = projectCleanedName;
	}

	public ArrayList<Goal> getProjectGoals() {
		return projectGoals;
	}
	
	public void setProjectGoals(ArrayList<Goal> projectGoals) {
		this.projectGoals = projectGoals;
	}
	
}
