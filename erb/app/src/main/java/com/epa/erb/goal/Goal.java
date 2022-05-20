package com.epa.erb.goal;

import java.util.ArrayList;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.chapter.Chapter;

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
	
	public void setChapters(ArrayList<Activity> activities) {
		ArrayList<String> chapterNumbers = getAllChapterNumbers(activities);
		for (String chapterNumber : chapterNumbers) {
			if (!chapterNumber.contentEquals("0")) {
				Chapter chapter = new Chapter(Integer.parseInt(chapterNumber), "0" + chapterNumber,"Chapter " + chapterNumber, "", "");
				ArrayList<Activity> activitiesForChapter = getActivitiesForChapter(chapterNumber, activities);
				chapter.setAssignedActivities(activitiesForChapter);
				chapters.add(chapter);
			}
		}
	}
	
	private ArrayList<Activity> getActivitiesForChapter(String chapterNumber, ArrayList<Activity> activities){
		ArrayList<Activity> activitiesForChapter = new ArrayList<Activity>();
		Activity planActivity = app.getActivity("25");
		planActivity.setChapterAssignment(chapterNumber);
		activitiesForChapter.add(cloneActivity(planActivity));
		for(Activity activity : activities) {
			if(activity.getChapterAssignment().contentEquals(chapterNumber) && !activitiesForChapter.contains(activity)) {
				Activity clonedActivity = cloneActivity(activity);
				activitiesForChapter.add(clonedActivity);
			}
		}
		Activity reflectActivity = app.getActivity("26");
		reflectActivity.setChapterAssignment(chapterNumber);
		activitiesForChapter.add(cloneActivity(reflectActivity));
		return activitiesForChapter;
	}
	
	private ArrayList<String> getAllChapterNumbers(ArrayList<Activity> activities){
		ArrayList<String> chapterNumbers = new ArrayList<String>();
		for(Activity activity : activities) {
			if(!chapterNumbers.contains(activity.getChapterAssignment())) {
				chapterNumbers.add(activity.getChapterAssignment());
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
