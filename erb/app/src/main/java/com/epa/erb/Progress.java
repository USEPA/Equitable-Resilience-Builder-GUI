package com.epa.erb;

import java.util.ArrayList;
import com.epa.erb.chapter.Chapter;

public class Progress {
	
	public Progress() {
		
	}
	
	public int getChapterPercentDone(Chapter chapter) {
		double numberOfActivitiesInChapter = 0;
		double numberOfCompletedActivitiesInChapter = 0;
		for (Activity activity : chapter.getAssignedActivities()) {
				numberOfActivitiesInChapter++;
				if (activity.getStatus().contentEquals("complete")) {
					numberOfCompletedActivitiesInChapter++;
				}
		}
		return (int) ((numberOfCompletedActivitiesInChapter / numberOfActivitiesInChapter) * 100);
	}
	
	public int getGoalPercentDone(ArrayList<Chapter> listOfChaptersInGoal) {
		double numberOfActivitiesInGoal = 0;
		double numberOfCompletedActivitiesInGoal = 0;
		for(Chapter chapter: listOfChaptersInGoal) {
			for(Activity activity: chapter.getAssignedActivities()) {
					numberOfActivitiesInGoal++;
					if(activity.getStatus().contentEquals("complete")) {
						numberOfCompletedActivitiesInGoal++;
					}
				}
		}
		return (int) ((numberOfCompletedActivitiesInGoal/numberOfActivitiesInGoal) * 100);
	}
	
	public int getChapterConfidencePercent(Chapter chapter) {
		double max = (chapter.getNumberOfAssignedActivities()) * 100;
		double rating = 0;
		for (Activity activity : chapter.getAssignedActivities()) {
			int activityRating = Integer.parseInt(activity.getRating());
				if (activityRating >= 0) {
					rating = rating + activityRating;
				}
		}
		return (int) (((rating / max) * 100));
	}
	
	public int getGoalConfidencePercent(ArrayList<Chapter> listOfChaptersInGoal) {
		double max = getMaxPercent(listOfChaptersInGoal);
		double rating = 0;
		for (Chapter chapter : listOfChaptersInGoal) {
			for (Activity activity : chapter.getAssignedActivities()) {
					int activityRating = Integer.parseInt(activity.getRating());
					if (activityRating >= 0) {
						rating = rating + activityRating;
					}
			}
		}
		return (int) (((rating / max) * 100));
	}
	
	private double getMaxPercent(ArrayList<Chapter> listOfChaptersInGoal) {
		int numActivities = 0;
		for(Chapter chapter: listOfChaptersInGoal) {
			for(Activity activity : chapter.getAssignedActivities()) {
					numActivities++;
				}
			}
		return numActivities * 100.0;
	}

}
