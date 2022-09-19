package com.epa.erb;

import java.util.ArrayList;
import java.util.HashMap;

import com.epa.erb.chapter.PlanFacilitatorModeController;
import com.epa.erb.chapter.PlanGoalModeController;
import com.epa.erb.chapter.ReflectFacilitatorModeController;
import com.epa.erb.chapter.ReflectGoalModeController;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.worksheet.WorksheetContentController;

public class Activity {
	
	private ActivityType activityType;
	private String chapterAssignment; 
	private String status;
	private String shortName;
	private String longName;
	private String fileName;
	private String instructions;
	private String objectives;
	private String description;
	private String materials;
	private String time;
	private String who;
	private String activityID;
	private String notes;
	private String rating;
	public Activity(ActivityType activityType, String chapterAssignment, String status, String shortName, String longName, String fileName, String directions, String objectives, String description, String materials, String time, String who, String activityID, String notes, String rating) {
		this.activityType = activityType;
		this.chapterAssignment = chapterAssignment;
		this.status = status;
		this.shortName = shortName;
		this.longName = longName;
		this.fileName = fileName;
		this.instructions = directions;
		this.objectives = objectives;
		this.description = description;
		this.materials = materials;
		this.time = time;
		this.who = who;
		this.activityID = activityID;
		this.notes = notes;
		this.rating = rating;
	}
	
	private ArrayList<Step> steps = new ArrayList<Step>();
	
	private boolean isSaved = true;
	private PlanGoalModeController planGoalModeController;
	private PlanFacilitatorModeController planFacilitatorModeController;
	private ReflectGoalModeController reflectGoalModeController;
	private ReflectFacilitatorModeController reflectFacilitatorModeController;
	private WorksheetContentController worksheetContentController;
	private NoteBoardContentController noteBoardContentController;
		
	private HashMap<String, String> planHashMap;
	
	public Activity() {
		
	}
	
	public void setHashMap() {
		setPlanHashMapDefaults();
	}
	
	public void assignSteps(ArrayList<Step> allSteps) {
		ArrayList<Step> activitySteps = new ArrayList<Step>();
		for(Step step: allSteps) {
			if(step.getActivityAssignment().contentEquals(activityID)) {
				int sizeOfActivityList = activitySteps.size();
				activitySteps.add(sizeOfActivityList, step);
			}
		}
		setSteps(activitySteps);
	}
	
	private void setPlanHashMapDefaults() {
		planHashMap = new HashMap<String, String>();
		planHashMap.put("pAudience", "");
		planHashMap.put("pGoals", "");
		planHashMap.put("pActivities", "");
		planHashMap.put("pNotes", "");
	}
	
	public ArrayList<String> getSplitMaterials(){
		ArrayList<String> listOfMaterials = new ArrayList<String>();
		String [] splitStrings = materials.split("\n");
		for(String s: splitStrings) {
			listOfMaterials.add(s);
		}
		return listOfMaterials;
	}
	
	public Activity cloneActivity() {
		Activity clonedActivity = new Activity(activityType, chapterAssignment, status, shortName, longName, fileName, instructions, objectives, description, materials, time, who, activityID, notes, rating);
		clonedActivity.setSteps(steps);
		return clonedActivity;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getChapterAssignment() {
		return chapterAssignment;
	}

	public void setChapterAssignment(String chapterAssignment) {
		this.chapterAssignment = chapterAssignment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMaterials() {
		return materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getActivityID() {
		return activityID;
	}

	public void setActivityID(String activityID) {
		this.activityID = activityID;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public PlanGoalModeController getPlanGoalModeController() {
		return planGoalModeController;
	}

	public void setPlanGoalModeController(PlanGoalModeController planController) {
		this.planGoalModeController = planController;
	}

	public PlanFacilitatorModeController getPlanFacilitatorModeController() {
		return planFacilitatorModeController;
	}

	public void setPlanFacilitatorModeController(PlanFacilitatorModeController planFacilitatorModeController) {
		this.planFacilitatorModeController = planFacilitatorModeController;
	}

	public ReflectGoalModeController getReflectGoalModeController() {
		return reflectGoalModeController;
	}

	public void setReflectGoalModeController(ReflectGoalModeController reflectController) {
		this.reflectGoalModeController = reflectController;
	}

	public ReflectFacilitatorModeController getReflectFacilitatorModeController() {
		return reflectFacilitatorModeController;
	}

	public void setReflectFacilitatorModeController(ReflectFacilitatorModeController reflectFacilitatorModeController) {
		this.reflectFacilitatorModeController = reflectFacilitatorModeController;
	}

	public WorksheetContentController getWorksheetContentController() {
		return worksheetContentController;
	}

	public void setWorksheetContentController(WorksheetContentController worksheetContentController) {
		this.worksheetContentController = worksheetContentController;
	}

	public NoteBoardContentController getNoteBoardContentController() {
		return noteBoardContentController;
	}

	public void setNoteBoardContentController(NoteBoardContentController noteBoardContentController) {
		this.noteBoardContentController = noteBoardContentController;
	}

	public HashMap<String, String> getPlanHashMap() {
		return planHashMap;
	}

	public void setPlanHashMap(HashMap<String, String> planHashMap) {
		this.planHashMap = planHashMap;
	}

	public ArrayList<Step> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}

}
