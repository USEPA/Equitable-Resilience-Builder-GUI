package com.epa.erb.project;

import java.util.ArrayList;
import com.epa.erb.goal.Goal;

public class Project {

	private String projectName;
	private String projectType;
	private String projectCleanedName;
	public Project(String projectName, String projectType, String projectCleanedName) {
		this.projectName = projectName;
		this.projectType = projectType;
		this.projectCleanedName = projectCleanedName;
	}
	
	private ArrayList<Goal> projectGoals;
	public Project(String projectName, String projectType, String projectCleanedName, ArrayList<Goal> projectGoals) {
		this.projectName = projectName;
		this.projectType = projectType;
		this.projectCleanedName = projectCleanedName;
		this.projectGoals = projectGoals;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
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
