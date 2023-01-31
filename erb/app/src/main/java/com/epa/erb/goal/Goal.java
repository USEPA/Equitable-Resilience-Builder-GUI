package com.epa.erb.goal;

import java.io.File;
import java.util.ArrayList;
import com.epa.erb.App;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;

public class Goal {
	
	private App app;
	private String goalName;
	private String goalCleanedName;
	private String goalDescription;
	private ArrayList<GoalCategory> listOfSelectedGoalCategories;
	public Goal(App app,String goalName, String goalCleanedName, String goalDescription, ArrayList<GoalCategory> listOfSelectedGoalCategories ) {
		this.app = app;
		this.goalName = goalName;
		this.goalCleanedName = goalCleanedName;
		this.goalDescription = goalDescription;
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
	}
	
	private FileHandler fileHandler = new FileHandler();
	ArrayList<Chapter> chapters = new ArrayList<Chapter>();
	
	public void setChapters(Project project) {
		File goalXMLFile = fileHandler.getGoalMetaXMLFile(project, this);
		if (goalXMLFile != null && goalXMLFile.exists()) {
			XMLManager xmlManager = new XMLManager(app);
			chapters = xmlManager.parseGoalXML(goalXMLFile);
		} else {
			chapters = getAllChapters();
		}
	}
	
	private ArrayList<Chapter> getAllChapters() {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		ArrayList<String> chapNums = new ArrayList<String>();
		for (GoalCategory goalCategory : listOfSelectedGoalCategories) {
			for (Chapter chapter : goalCategory.getGoalChapters()) {
				if(!chapNums.contains(String.valueOf(chapter.getNumber()))) {
					chapNums.add(String.valueOf(chapter.getNumber()));
					chapters.add(chapter);
				}
			}
		}
		return chapters;
	}
	
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public String getGoalCleanedName() {
		return goalCleanedName;
	}

	public void setGoalCleanedName(String goalCleanedName) {
		this.goalCleanedName = goalCleanedName;
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

	public ArrayList<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	}

}
