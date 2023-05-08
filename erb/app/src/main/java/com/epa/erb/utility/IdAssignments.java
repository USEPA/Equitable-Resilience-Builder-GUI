package com.epa.erb.utility;

import java.util.ArrayList;

public class IdAssignments {
	
	private ArrayList<String> faqIdAssignments = new ArrayList<String>();
	private ArrayList<String> aboutIdAssignments = new ArrayList<String>();
	private ArrayList<String> chapterIdAssignments = new ArrayList<String>();
	private ArrayList<String> resourceIdAssignments = new ArrayList<String>();
	private ArrayList<String> backgroundIdAssignments = new ArrayList<String>();
	
	public IdAssignments() {
		fillFAQIdAssignments();
		fillAboutIdAssignments();
		fillChapterIdAssignemnts();
		fillResourceIdAssignments();
		fillBackgroundIdAssignments();
	}
	
	public void fillFAQIdAssignments() {
		
	}
	
	public void fillAboutIdAssignments() {
		aboutIdAssignments.add("07");
		aboutIdAssignments.add("08");
		aboutIdAssignments.add("09");
		aboutIdAssignments.add("167");
	}
	
	public void fillResourceIdAssignments() {
		resourceIdAssignments.add("01");
		resourceIdAssignments.add("02");
		resourceIdAssignments.add("03");
		resourceIdAssignments.add("04");
		resourceIdAssignments.add("05");
		resourceIdAssignments.add("06");
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
		backgroundIdAssignments.add("27");
		backgroundIdAssignments.add("114");
		backgroundIdAssignments.add("121");
		backgroundIdAssignments.add("123");
		backgroundIdAssignments.add("152");

	}
	
	public ArrayList<String> getFAQIdAssignments() {
		return faqIdAssignments;
	}
	
	public ArrayList<String> getAboutIdAssignments() {
		return aboutIdAssignments;
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
