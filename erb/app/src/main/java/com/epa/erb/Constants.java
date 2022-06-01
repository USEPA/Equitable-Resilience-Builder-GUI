package com.epa.erb;

public class Constants {
	
	public Constants() {
		
	}
	
	//Activity Attributes
	private String activityTypeColor = "";
	//Status
	private String readyStatusColor = "#E3E3E3"; 				//Gray
	private String inProgressStatusColor = "#EDF7B2"; 			//Yellow
	private String completeStatusColor = "#B2F7D1"; 			//Mint Green
	private String skippedStatusColor = "#B2C1F7"; 				//Light Blue
	//--
	private String shortNameColor = "";
	private String longNameColor = "";
	private String fileNameColor = "";
	private String instructionsColor = "#86E596"; 				//Light Green
	private String objectivesColor = "#6B8BFF"; 				//Blue
	private String descriptionColor = "#F7B2E0"; 				//Pink	
	private String materialsColor = "#B895EB";					//Purple
	private String timeColor = "#F7DBB2";						//Orange
	private String whoColor = "#EB8787"; 						//Red
	private String activityIDColor = "";
	private String linksColor = "#E88AFF";						//Magenta

	//Chapters
//	private String allChaptersColor	= "#80BFCD"; 				//Dark Turquoise
	private String allChaptersColor	= "#C3C3C3"; 				//Grey

    //Post-It Notes Stack
		//----Gray
	//private String layer1ColorString = "#555555";
	//private String layer2ColorString = "#666666";
	//private String layer3ColorString = "#777777";
	//private String layer4ColorString = "#888888";
	//private String layer5ColorString = "#999999";

		//----Color
	private String layer1ColorString = "#F7B2E0";				//Pink
	private String layer2ColorString = "#B895EB";				//Purple
	private String layer3ColorString = "#EB8787";				//Red
	private String layer4ColorString = "#B2F7D1";				//Mint Green
	private String layer5ColorString = "#B2C1F7";				//Light Blue
	
	//Post-It Note Single
	private String postItNoteColor = "#FFFFFF"; 				//White
	
	//Local Paths
	private String pathToLocalERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	private String pathToLocalERBProjectsFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\Projects";
	private String pathToLocalERBStaticDataFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\Static_Data";
	
	
	//Dynamic Paths
	private String pathToDynamicERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToDynamicERBProjectsFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\Projects\\").replace("\\", "\\\\");
	private String pathToDynamicERBStaticDataFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\Static_Data\\").replace("\\", "\\\\");


	//-------------------------------------------------------------------------

	public String getReadyStatusColor() {
		return readyStatusColor;
	}

	public String getInProgressStatusColor() {
		return inProgressStatusColor;
	}
	
	public String getCompleteStatusColor() {
		return completeStatusColor;
	}

	public String getSkippedStatusColor() {
		return skippedStatusColor;
	}

	public String getMaterialsColor() {
		return materialsColor;
	}

	public String getDescriptionColor() {
		return descriptionColor;
	}

	public String getObjectivesColor() {
		return objectivesColor;
	}

	public String getInstructionsColor() {
		return instructionsColor;
	}

	public String getWhoColor() {
		return whoColor;
	}

	public String getTimeColor() {
		return timeColor;
	}

	public String getShortNameColor() {
		return shortNameColor;
	}

	public String getLongNameColor() {
		return longNameColor;
	}

	public String getFileNameColor() {
		return fileNameColor;
	}

	public String getActivityTypeColor() {
		return activityTypeColor;
	}
	
	public String getLinksColor() {
		return linksColor;
	}

	public String getActivityIDColor() {
		return activityIDColor;
	}

	public String getAllChaptersColor() {
		return allChaptersColor;
	}

	public String getLayer1ColorString() {
		return layer1ColorString;
	}

	public String getLayer2ColorString() {
		return layer2ColorString;
	}

	public String getLayer3ColorString() {
		return layer3ColorString;
	}

	public String getLayer4ColorString() {
		return layer4ColorString;
	}

	public String getLayer5ColorString() {
		return layer5ColorString;
	}

	public String getPostItNoteColor() {
		return postItNoteColor;
	}

	public String getPathToLocalERBFolder() {
		return pathToLocalERBFolder;
	}

	public String getPathToLocalERBProjectsFolder() {
		return pathToLocalERBProjectsFolder;
	}

	public String getPathToLocalERBStaticDataFolder() {
		return pathToLocalERBStaticDataFolder;
	}

	public String getPathToDynamicERBFolder() {
		return pathToDynamicERBFolder;
	}

	public String getPathToDynamicERBProjectsFolder() {
		return pathToDynamicERBProjectsFolder;
	}

	public String getPathToDynamicERBStaticDataFolder() {
		return pathToDynamicERBStaticDataFolder;
	}
			
}
