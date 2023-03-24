package com.epa.erb.utility;

import java.util.ArrayList;

public class IdAssignments {
	
	private ArrayList<String> introIdAssignments = new ArrayList<String>();
	private ArrayList<String> chapterIdAssignments = new ArrayList<String>();
	private ArrayList<String> resourceIdAssignments = new ArrayList<String>();
	private ArrayList<String> backgroundIdAssignments = new ArrayList<String>();
	
	public IdAssignments() {
		fillIntroIdAssignments();
		fillChapterIdAssignemnts();
		fillResourceIdAssignments();
		fillBackgroundIdAssignments();
	}
	
	public void fillIntroIdAssignments() {
	}
	
	public void fillResourceIdAssignments() {
		resourceIdAssignments.add("01");
		resourceIdAssignments.add("02");
		resourceIdAssignments.add("03");
		resourceIdAssignments.add("04");
		resourceIdAssignments.add("05");
		resourceIdAssignments.add("06");
		resourceIdAssignments.add("07");
		resourceIdAssignments.add("08");
		resourceIdAssignments.add("09");
		resourceIdAssignments.add("10");
	}
	
	public void fillChapterIdAssignemnts() {
		chapterIdAssignments.add("15");
		chapterIdAssignments.add("16");
		chapterIdAssignments.add("17");
		chapterIdAssignments.add("18");
		chapterIdAssignments.add("19");		
	}
	
	public void fillBackgroundIdAssignments() {
		backgroundIdAssignments.add("11");
		backgroundIdAssignments.add("12");
		backgroundIdAssignments.add("13");
		backgroundIdAssignments.add("14");	
		backgroundIdAssignments.add("114");	
		backgroundIdAssignments.add("118");	
		backgroundIdAssignments.add("121");	
		backgroundIdAssignments.add("123");	

	}
	
	public ArrayList<String> getIntroIdAssignments() {
		return introIdAssignments;
	}
	public ArrayList<String> getResourceIdAssignments() {
		return resourceIdAssignments;
	}

	public ArrayList<String> getChapterIdAssignments() {
		return chapterIdAssignments;
	}

	public ArrayList<String> getBackgroundIdAssignments() {
		return backgroundIdAssignments;
	}
}
