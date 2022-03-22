package erb;

import java.util.ArrayList;
import java.util.UUID;

public class Activity {
	
	ActivityType activityType;
	String shortName;
	String longName;
	String fileName;
	String directions;
	String objectives;
	String description;
	String GUID;
	
	public Activity(ActivityType activityType, String shortName, String longName, String fileName, String directions, String objectives, String description) {
		this.activityType = activityType;
		this.shortName = shortName;
		this.longName = longName;
		this.fileName = fileName;
		this.directions = directions;
		this.objectives = objectives;
		this.description = description;
		GUID = generateUniqueId();
		setActivityAttributes();
	}
	
	ArrayList<ActivityAttribute> activityAttributes = new ArrayList<ActivityAttribute>();
	
	void setActivityAttributes() {
		activityAttributes.add(new ActivityAttribute("A", "Activity Type", 0, "Activity Type: " + activityType.getLongName(), "FE4040"));
		activityAttributes.add(new ActivityAttribute("S", "Short Name", 0, "Short Name: " + shortName, "FEB040"));
		activityAttributes.add(new ActivityAttribute("L", "Long Name", 0, "Long Name: " + longName, "9AFE40"));
		activityAttributes.add(new ActivityAttribute("F", "File Name", 0, "File Name: " + fileName, "40FEC3"));
		activityAttributes.add(new ActivityAttribute("D1", "Directions", 0, "Directions" + directions, "40D4FE"));
		activityAttributes.add(new ActivityAttribute("O", "Objectives", 0, "Objectives: " + objectives, "C641FF"));
		activityAttributes.add(new ActivityAttribute("D2", "Description", 0, "Description: " + description, "FF41C2"));
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
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

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
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
		
	public String getGUID() {
		return GUID;
	}

	public ArrayList<ActivityAttribute> getActivityAttributes() {
		return activityAttributes;
	}
	
	public String generateUniqueId() {
		UUID uuid = UUID.randomUUID();
		return String.valueOf(uuid);
	}

	public String toString() {
		return  "--ActivityType-- " + "\n" + activityType.toString() +  "\n" + "----" + "\n" +
				"ShortName: " + shortName + "\n" + 
				"LongName: " + longName + "\n" + 
				"FileName: " + fileName + "\n" + 
				"Directions: " + directions + "\n" + 
				"Objectives: " + objectives + "\n" + 
				"Description: " + description + "\n" + 
				"GUID: " + GUID + "\n";
	}
	
}
