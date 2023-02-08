package com.epa.erb.utility;

import java.util.ArrayList;

public class IdAssignments {
	
	private ArrayList<String> introIdAssignments = new ArrayList<String>();
	private ArrayList<String> chapterIdAssignments = new ArrayList<String>();
	private ArrayList<String> resourceIdAssignments = new ArrayList<String>();
	public IdAssignments() {
		fillIntroIdAssignments();
		fillChapterIdAssignemnts();
		fillResourceIdAssignments();
	}
	
	public void fillIntroIdAssignments() {
		introIdAssignments.add("09");
	}
	
	public void fillResourceIdAssignments() {
		resourceIdAssignments.add("02");
	}
	
	public void fillChapterIdAssignemnts() {
		chapterIdAssignments.add("15");
		chapterIdAssignments.add("16");
		chapterIdAssignments.add("17");
		chapterIdAssignments.add("18");
		chapterIdAssignments.add("19");		
	}
	public ArrayList<String> getIntroIdAssignments() {
		return introIdAssignments;
	}
	public ArrayList<String> getResourceIdAssignments() {
		return resourceIdAssignments;
	}

}
