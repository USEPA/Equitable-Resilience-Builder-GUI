package com.epa.erb.utility;

import java.util.ArrayList;

public class IdAssignments {
	
	private ArrayList<String> faqIdAssignments = new ArrayList<String>();
	private ArrayList<String> iconIdAssignments = new ArrayList<String>();
	private ArrayList<String> aboutIdAssignments = new ArrayList<String>();
	private ArrayList<String> chapterIdAssignments = new ArrayList<String>();
	private ArrayList<String> resourceIdAssignments = new ArrayList<String>();
	private ArrayList<String> backgroundIdAssignments = new ArrayList<String>();
	
	public IdAssignments() {
		fillFAQIdAssignments();
		fillIconIdAssignments();
		fillAboutIdAssignments();
		fillChapterIdAssignemnts();
		fillResourceIdAssignments();
		fillBackgroundIdAssignments();
	}
	
	public void fillFAQIdAssignments() {
		faqIdAssignments.add("26"); //FAQ
		faqIdAssignments.add("23"); //My Portfolio
		faqIdAssignments.add("22"); //Key Takeaways
		faqIdAssignments.add("21"); //Reflection Diary
		faqIdAssignments.add("11"); //Supporting Materials
		faqIdAssignments.add("13"); //Indicator Card Sorting
		faqIdAssignments.add("204"); //Centering Equity Icons
	}
	
	public void fillIconIdAssignments() {
		iconIdAssignments.add("67");
		iconIdAssignments.add("64");
		iconIdAssignments.add("69");
		iconIdAssignments.add("63");
		iconIdAssignments.add("73");
		iconIdAssignments.add("62");
		iconIdAssignments.add("68");
		iconIdAssignments.add("75");
		iconIdAssignments.add("77");
		iconIdAssignments.add("66");
		iconIdAssignments.add("76");
		iconIdAssignments.add("70");
		iconIdAssignments.add("78");
		iconIdAssignments.add("65");
		iconIdAssignments.add("72");
	}
	
	public void fillAboutIdAssignments() {
		aboutIdAssignments.add("09");
		aboutIdAssignments.add("167");
	}
	
	public void fillResourceIdAssignments() {
		resourceIdAssignments.add("01"); //Glossary
		resourceIdAssignments.add("05"); //Data Ethics
		resourceIdAssignments.add("02"); //Storytelling
		resourceIdAssignments.add("04"); //Equitable Resilience
		resourceIdAssignments.add("10"); //Youth Engagement Guide
		resourceIdAssignments.add("201"); //Funding and Finance Guide
		resourceIdAssignments.add("03"); //Trauma-Informed Approach
		resourceIdAssignments.add("06"); //Resilience Indicators Background
		resourceIdAssignments.add("187"); //Equity Principles for Resilience Planning
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
	
	public ArrayList<String> getIconIdAssignments() {
		return iconIdAssignments;
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
