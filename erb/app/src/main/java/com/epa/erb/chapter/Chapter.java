package com.epa.erb.chapter;

import java.util.ArrayList;
import com.epa.erb.ContentPanel;
import com.epa.erb.ERBItem;
public class Chapter extends ERBItem {
	
	private String id;
	private String guid;
	private String longName;
	private String shortName;
	
	private String status;
	private String notes;
	private String description;
	private int number;

	public Chapter(String id, String guid, String longName, String shortName, String status, String notes, String description, int number) {
		super(id, guid, longName, shortName, null);
		this.status = status;
		this.notes = notes;
		this.description = description;
		this.number = number;
	}
		
	private ArrayList<ContentPanel> contentPanels = new ArrayList<ContentPanel>();
	public Chapter cloneChapter() {
		Chapter clonedChapter = new Chapter(id, guid, longName, shortName, status, notes, description, number);
//		clonedChapter.setAssignedSteps(assignedSteps);
//		clonedChapter.setAssignedActivities(assignedActivities);
		return clonedChapter;
	}
	public void addContentPanel(ContentPanel contentPanel) {
		if(contentPanel != null) {
			contentPanels.add(contentPanel);
		}
	}
	public void removeContentPanel(ContentPanel contentPanel) {
		if(contentPanel != null) {
			contentPanels.remove(contentPanel);
		}
	}

//	public int getNumberOfAssignedActivities() {
//		return assignedActivities.size();
//	}
//		
//	
//	public int getNumberOfReadyActivities() {
//		int count = 0;
//		for(Activity activity: assignedActivities) {
//			if(activity.getStatus().contentEquals("ready")) {
//				count++;
//			}
//		}
//		return count;
//	}
//	
//	public int getNumberOfInProgressActivities() {
//		int count = 0;
//		for(Activity activity: assignedActivities) {
//			if(activity.getStatus().contentEquals("in progress")) {
//				count++;
//			}
//		}
//		return count;
//	}
//	
//	public int getNumberOfCompletedActivities() {
//		int count = 0;
//		for(Activity activity: assignedActivities) {
//			if(activity.getStatus().contentEquals("complete")) {
//				if (!activity.getLongName().contentEquals("Plan") && !activity.getLongName().contentEquals("Reflect")) {
//					count++;
//				}
//			}
//		}
//		return count;
//	}
//	
	public String checkForStatus() {
//		boolean isReady = true; // if all activities are ready
//		boolean isComplete = true; // if all activities are complete
//		if (assignedActivities.size() > 0) {
//			for (Activity activity : assignedActivities) {
//				if (activity.getStatus().contentEquals("ready")) {
//					isComplete = false;
//				} else if (activity.getStatus().contentEquals("in progress")) {
//					isComplete = false;
//					isReady = false;
//				} else if (activity.getStatus().contentEquals("complete")) {
//					isReady = false;
//				}
//			}
//			if (isComplete) {
//				return "complete";
//			} else if (isReady) {
//				return "ready";
//			} else {
//				return "in progress";
//			}
//		} else {
			return "ready";
//		}
	}
	
	

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getStatus() {
		status = checkForStatus();
		return status;
	}

	public ArrayList<ContentPanel> getContentPanels() {
		return contentPanels;
	}
	public void setContentPanels(ArrayList<ContentPanel> contentPanels) {
		this.contentPanels = contentPanels;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
		
}
