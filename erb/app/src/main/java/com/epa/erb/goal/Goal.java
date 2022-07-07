package com.epa.erb.goal;

import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.XMLManager;
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
	
	private Constants constants = new Constants();
	ArrayList<Chapter> chapters = new ArrayList<Chapter>();
	private Logger logger = LogManager.getLogger(Goal.class);
	private String pathToERBProjectsFolder = constants.getPathToERBProjectsFolder();
	
	public void setChapters(ArrayList<Activity> activities, String projectName) {
		File goalXMLFile = getGoalXMLFile(projectName);
		if (goalXMLFile != null) {
			XMLManager xmlManager = new XMLManager(app);
			chapters = xmlManager.parseGoalXML(goalXMLFile, activities);
		} else {
			ArrayList<String> goalActivityIds = getUniqueActivityIdsForGoal();
			ArrayList<String> chapterNumbers = getAllChapterNumbers(activities, goalActivityIds);
			if (chapterNumbers != null) {
				for (String chapterNumber : chapterNumbers) {
					if (!chapterNumber.contentEquals("0")) {
						Chapter chapter = new Chapter(Integer.parseInt(chapterNumber), "0" + chapterNumber,"Chapter " + chapterNumber, getChapterDescription(chapterNumber), "");
						ArrayList<Activity> activitiesForChapter = getActivitiesForChapter(chapterNumber, activities,goalActivityIds);
						chapter.setAssignedActivities(activitiesForChapter);
						chapters.add(chapter);
					}
				}
			}
		}
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
		if (chapterNumber != null && activities != null && goalActivityIds != null) {
			for (Activity activity : activities) {
				if (activity.getChapterAssignment().contentEquals(chapterNumber)&& !activitiesForChapter.contains(activity)) {
					if (goalActivityIds.contains(activity.getActivityID())) {
						Activity clonedActivity = activity.cloneActivity();
						activitiesForChapter.add(clonedActivity);
					}
				}
			}
		} else {
			logger.error("Cannot getActivitiesForChapter. param is null.");
		}
		return activitiesForChapter;
	}
	
	private ArrayList<String> getAllChapterNumbers(ArrayList<Activity> activities, ArrayList<String> goalActivityIds) {
		ArrayList<String> chapterNumbers = new ArrayList<String>();
		if (activities != null && goalActivityIds != null) {
			for (Activity activity : activities) {
				if (goalActivityIds.contains(activity.getActivityID())) {
					if (!chapterNumbers.contains(activity.getChapterAssignment())) {
						chapterNumbers.add(activity.getChapterAssignment());
					}
				}
			}
		} else {
			logger.error("Cannot getAllChapterNumbers. activites or goalActivityIds is null.");
		}
		return chapterNumbers;
	}
	
	private String getChapterDescription(String chapterNumber) {
		if (chapterNumber != null) {
			for (Chapter chapter : app.getChapters()) {
				if (String.valueOf(chapter.getChapterNum()).contentEquals(chapterNumber)) {
					return chapter.getDescriptionName();
				}
			}
			logger.debug("Chapter description returned is null.");
			return null;
		} else {
			logger.error("Cannot getChapterDescription. chapterNumber is null.");
			return null;
		}
	}
	
	private File getGoalXMLFile(String projectName) {
		if (projectName != null) {
			File goalMetaFile = new File(pathToERBProjectsFolder + "\\" + projectName + "\\Goals\\" + goalName + "\\Meta.xml");
			if (goalMetaFile.exists()) {
				return goalMetaFile;
			}
			logger.debug("Goal XML file returned is null");
			return null;
		} else {
			logger.error("Cannot getGoalXMLFile. projectName is null.");
			return null;
		}
	}
	
	public boolean isSaved() {
		for(Chapter chapter: chapters) {
			if(!chapter.isSaved()) {
				return false;
			}
		}
		return true;
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
