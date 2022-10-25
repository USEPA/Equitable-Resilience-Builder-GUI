package com.epa.erb.goal;

import java.util.ArrayList;

import com.epa.erb.chapter.Chapter;

public class GoalCategory {
	
	private String categoryName;
	private ArrayList<Chapter> goalChapters;
	public GoalCategory(String categoryName, ArrayList<Chapter> goalChapters) {
		this.categoryName = categoryName;
		this.goalChapters = goalChapters;
	}

	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ArrayList<Chapter> getGoalChapters() {
		return goalChapters;
	}

	public void setGoalChapters(ArrayList<Chapter> goalChapters) {
		this.goalChapters = goalChapters;
	}
}
