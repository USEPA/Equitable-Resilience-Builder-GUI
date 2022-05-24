package com.epa.erb.goal;

import java.io.File;
import java.util.ArrayList;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.XMLManager;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.project.Project;

public class Goal {
	
	private App app;
	private String goalName;
	private String goalDescription;
	private ArrayList<GoalCategory> listOfSelectedGoalCategories;
	public Goal(App app,String goalName, String goalDescription, ArrayList<GoalCategory> listOfSelectedGoalCategories ) {
		this.app = app;
		this.goalName = goalName;
		this.goalDescription = goalDescription;
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
	}
	
	ArrayList<Chapter> chapters = new ArrayList<Chapter>();
	private Constants constants = new Constants();
	private String pathToERBProjectsFolder = constants.getPathToLocalERBProjectsFolder();
	
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
	
	public void setChapters(ArrayList<Activity> activities, String projectName) {
		File goalXMLFile = getGoalXMLFile(projectName);
		if(goalXMLFile != null) {
			XMLManager xmlManager = new XMLManager(app);
			chapters = xmlManager.parseGoalXML(goalXMLFile, activities);
		} else {
			ArrayList<String> goalActivityIds = getUniqueActivityIdsForGoal();
			ArrayList<String> chapterNumbers = getAllChapterNumbers(activities, goalActivityIds);
			for (String chapterNumber : chapterNumbers) {
				if (!chapterNumber.contentEquals("0")) {
					Chapter chapter = new Chapter(Integer.parseInt(chapterNumber), "0" + chapterNumber,"Chapter " + chapterNumber, "", "");
					ArrayList<Activity> activitiesForChapter = getActivitiesForChapter(chapterNumber, activities, goalActivityIds);
					chapter.setAssignedActivities(activitiesForChapter);
					chapters.add(chapter);
				}
			}
		}
		
	}
	
	private File getGoalXMLFile(String projectName) {
		File goalMetaFile = new File(pathToERBProjectsFolder + "\\" + projectName + "\\Goals\\" + goalName + "\\Meta.xml");
		if(goalMetaFile.exists()) {
			return goalMetaFile;
		}
		return null;
	}
	
	private ArrayList<String> getUniqueActivityIdsForGoal(){
		ArrayList<String> activityIds = new ArrayList<String>();
		for(GoalCategory goalCategory: listOfSelectedGoalCategories) {
			for(String id: goalCategory.getListOfAssignedActivityIDs()) {
				if(!activityIds.contains(id)) {
					activityIds.add(id);
				}
			}
		}
		return activityIds;
	}
	
	private ArrayList<Activity> getActivitiesForChapter(String chapterNumber, ArrayList<Activity> activities,ArrayList<String> goalActivityIds) {
		ArrayList<Activity> activitiesForChapter = new ArrayList<Activity>();
		Activity planActivity = app.getActivity("25");
		Activity planCloneActivity = cloneActivity(planActivity);
		planCloneActivity.setChapterAssignment(chapterNumber);
		activitiesForChapter.add(planCloneActivity);
		for (Activity activity : activities) {
			if (activity.getChapterAssignment().contentEquals(chapterNumber) && !activitiesForChapter.contains(activity)) {
				if (goalActivityIds.contains(activity.getActivityID())) {
					Activity clonedActivity = cloneActivity(activity);
					activitiesForChapter.add(clonedActivity);
				}
			}
		}
		Activity reflectActivity = app.getActivity("26");
		Activity reflectCloneActivity = cloneActivity(reflectActivity);
		reflectCloneActivity.setChapterAssignment(chapterNumber);
		activitiesForChapter.add(reflectCloneActivity);
		return activitiesForChapter;
	}
	
	private ArrayList<String> getAllChapterNumbers(ArrayList<Activity> activities, ArrayList<String> goalActivityIds) {
		ArrayList<String> chapterNumbers = new ArrayList<String>();
		for (Activity activity : activities) {
			if (goalActivityIds.contains(activity.getActivityID())) {
				if (!chapterNumbers.contains(activity.getChapterAssignment())) {
					chapterNumbers.add(activity.getChapterAssignment());
				}
			}
		}
		return chapterNumbers;
	}
	
	private Activity cloneActivity(Activity activity) {
		Activity clonedActivity = new Activity();
		clonedActivity.setActivityType(activity.getActivityType());
		clonedActivity.setChapterAssignment(activity.getChapterAssignment());
		clonedActivity.setStatus(activity.getStatus());
		clonedActivity.setShortName(activity.getShortName());
		clonedActivity.setLongName(activity.getLongName());
		clonedActivity.setFileName(activity.getFileName());
		clonedActivity.setDirections(activity.getDirections());
		clonedActivity.setObjectives(activity.getObjectives());
		clonedActivity.setDescription(activity.getDescription());
		clonedActivity.setMaterials(activity.getMaterials());
		clonedActivity.setTime(activity.getTime());
		clonedActivity.setWho(activity.getWho());
		clonedActivity.setActivityID(activity.getActivityID());
		clonedActivity.setNotes(activity.getNotes());
		clonedActivity.setRating(activity.getRating());
		return clonedActivity;
	}
	
	public ArrayList<Chapter> getChapters(){
		return chapters;
	}

}
