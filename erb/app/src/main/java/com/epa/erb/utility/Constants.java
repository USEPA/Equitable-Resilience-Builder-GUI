package com.epa.erb.utility;

import java.awt.Dimension;
import java.io.File;

public class Constants {
	
	private int prefHeightForImages = 0;
	private int prefWidthForImages = 0;
	//Activity
	private String activityTypeColor = "";
	//Status
	private String readyStatusColor = "#E3E3E3"; 				//Gray
	private String inProgressStatusColor = "#EDF7B2"; 			//Yellow
	private String completeStatusColor = "#B2F7D1"; 			//Mint Green
	//Attributes
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
	private String allChaptersColor	= "#A7BFE8";				//Blue
	private String chapter1Color = "#F1A858";					//Orange
	private String chapter2Color = "#6FE658";					//Light Green
	private String chapter3Color = "#35AF69";					//Green
	private String chapter4Color = "#42C2D7";					//Light Blue
	private String chapter5Color = "#427ED7";					//Blue
	//Post It Note Stack
	private String layer1ColorString = "#F7B2E0";				//Pink
	private String layer2ColorString = "#B895EB";				//Purple
	private String layer3ColorString = "#EB8787";				//Red
	private String layer4ColorString = "#B2F7D1";				//Mint Green
	private String layer5ColorString = "#B2C1F7";				//Light Blue
	//Post-It Note Single
	private String postItNoteColor = "#FFFFFF"; 				//White
	//Paths
	private String pathToERBFolder;
	private String pathToERBProjectsFolder;
	private String pathToERBStaticDataFolder;
	
	public Constants() {
		//setFilePathsStatic(); //For running tool locally
		setFilePathsDynamic(); //For packaging tool 
		sizeScreen(getScreenResolution(), getScreenSize());
	}
	
	private void setFilePathsStatic() {
		File userDir = new File(System.getProperty("user.dir"));
		File codeResourcesFile = new File(userDir.getParentFile().getParentFile() + "//erb_supporting_docs//Code_Resources");
		
		pathToERBFolder = codeResourcesFile.getPath() +  "\\ERB";
		pathToERBProjectsFolder = codeResourcesFile.getPath() + "\\ERB\\Projects";
		pathToERBStaticDataFolder = codeResourcesFile.getPath() +  "\\ERB\\Static_Data";
	}
	
	private void setFilePathsDynamic() {
		pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\"); //Dynamic
		pathToERBProjectsFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\Projects\\").replace("\\", "\\\\"); //Dynamic
		pathToERBStaticDataFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\Static_Data\\").replace("\\", "\\\\"); //Dynamic
	}

	private int getScreenResolution() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
	}
	
	private Dimension getScreenSize() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize();

	}
	
	private void sizeScreen(int dpiValue, Dimension size) {
		double width = size.getWidth();
		double height = size.getHeight();
		
		int dpiScale = getScaleForDPIValue(dpiValue);
		int maxWidthToSubtract = 850;
		int maxHeightToSubtract = 450;
		int widthToSubtract = maxWidthToSubtract-((maxWidthToSubtract*dpiScale/100)-maxWidthToSubtract);
		int heightToSubtract = maxHeightToSubtract-((maxHeightToSubtract*dpiScale/100)-maxHeightToSubtract);
		
		prefWidthForImages = (int) (width -widthToSubtract);
		prefHeightForImages = (int) (height - heightToSubtract);
	}
	
	public int getScaleForDPIValue(int dpiValue) {
		int lowestDPIValue = 96;
		int lowestDPIScale = 100;
		int highestDPIScale = 500;
		
		for (int i = lowestDPIScale; i <= highestDPIScale; i = i+25) {
			int calculatedDPIValue = (lowestDPIValue * i)/100;
			if(dpiValue == calculatedDPIValue) {
				return i;
			}
		}
		return 100; //Default
	}
	
	//-------------------------------------------------------------------------
	
	public int getPrefHeightForImages() {
		return prefHeightForImages;
	}
	public int getPrefWidthForImages() {
		return prefWidthForImages;
	}
	public String getActivityTypeColor() {
		return activityTypeColor;
	}
	public String getReadyStatusColor() {
		return readyStatusColor;
	}
	public String getInProgressStatusColor() {
		return inProgressStatusColor;
	}
	public String getCompleteStatusColor() {
		return completeStatusColor;
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
	public String getInstructionsColor() {
		return instructionsColor;
	}
	public String getObjectivesColor() {
		return objectivesColor;
	}
	public String getDescriptionColor() {
		return descriptionColor;
	}
	public String getMaterialsColor() {
		return materialsColor;
	}
	public String getTimeColor() {
		return timeColor;
	}
	public String getWhoColor() {
		return whoColor;
	}
	public String getActivityIDColor() {
		return activityIDColor;
	}
	public String getLinksColor() {
		return linksColor;
	}
	public String getAllChaptersColor() {
		return allChaptersColor;
	}	
	public String getChapter1Color() {
		return chapter1Color;
	}

	public String getChapter2Color() {
		return chapter2Color;
	}

	public String getChapter3Color() {
		return chapter3Color;
	}

	public String getChapter4Color() {
		return chapter4Color;
	}

	public String getChapter5Color() {
		return chapter5Color;
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
	public String getPathToERBFolder() {
		return pathToERBFolder;
	}
	public String getPathToERBProjectsFolder() {
		return pathToERBProjectsFolder;
	}
	public String getPathToERBStaticDataFolder() {
		return pathToERBStaticDataFolder;
	}

}
