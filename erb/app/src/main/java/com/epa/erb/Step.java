package com.epa.erb;

public class Step {
	
	private String stepType;
	private String activityAssignment; 
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
	private String stepID;
	private String notes;
	private String rating;
	private String dynamicActivityID;
	public Step(String stepType, String activityAssignment, String chapterAssignment, String status, String shortName, String longName, String fileName, String directions, String objectives, String description, String materials, String time, String who, String stepID, String notes, String rating, String dynamicActivityID) {
		this.stepType = stepType;
		this.activityAssignment = activityAssignment;
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
		this.stepID = stepID;
		this.notes = notes;
		this.rating = rating;
		this.dynamicActivityID = dynamicActivityID;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public String getActivityAssignment() {
		return activityAssignment;
	}

	public void setActivityAssignment(String activityAssignment) {
		this.activityAssignment = activityAssignment;
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

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String activityID) {
		this.stepID = activityID;
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

	public String getDynamicActivityID() {
		return dynamicActivityID;
	}

	public void setDynamicActivityID(String dynamicActivityID) {
		this.dynamicActivityID = dynamicActivityID;
	}
	
}
