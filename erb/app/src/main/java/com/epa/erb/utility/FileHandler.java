package com.epa.erb.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.ContentPanel;
import com.epa.erb.ERBItem;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;

public class FileHandler {
	
	public FileHandler() {
		
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(FileHandler.class);
	
	//--------------General Directories for Project-------------------------
	
	public File getProjectsDirectory() {
		File projectsDirectory = new File(constants.getPathToERBProjectsFolder());
		return projectsDirectory;
	}
	
	public File getGoalsDirectory(Project project) {
		if(project != null) {
			File goalsDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals");
			return goalsDirectory;
		} else {
			logger.error("Cannot getGoalsDirectory. project = " + project);
			return null;
		}
	}
	public File getActivitiesDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File activitiesDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities_XML");
			return activitiesDirectory;
		} else {
			logger.error("Cannot getActivitiesDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	public File getStepsDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File stepsDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Steps_XML");
			return stepsDirectory;
		} else {
			logger.error("Cannot getStepsDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getSupportingDOCDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File supportingDOCDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Supporting_Docs");
			return supportingDOCDirectory;
		} else {
			logger.error("Cannot getSupportingDOCDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getGUIDDataDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File guidsDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data");
			return guidsDirectory;
		} else {
			logger.error("Cannot getGUIDDataDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	//--------------Specific Directories for Project ----------------------------------
	
	public File getProjectDirectory(Project project) {
		if(project != null) {
			File projectDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName());
			return projectDirectory;
		} else {
			logger.error("Cannot getProjectDirectory. project = " + project);
			return null;
		}
	}
	
	public File getGoalDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File goalDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName());
			return goalDirectory;
		} else {
			logger.error("Cannot getGoalDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}

//	public File getActivityDirectory(Project project, Goal goal, Activity activity) {
//		if (project != null && goal != null && activity != null) {
//			File activityDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities_XML\\" + activity.getId());
//			return activityDirectory;
//		} else {
//			logger.error("Cannot getActivityDirectory. project = " + project + " goal = " + goal + " activity = "
//					+ activity);
//			return null;
//		}
//	}
//	public File getStepDirectory(Project project, Goal goal, Step step) {
//		if (project != null && goal != null && step != null) {
//			File stepDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Steps_XML\\" + step.getId());
//			return stepDirectory;
//		} else {
//			logger.error("Cannot getStepDirectory. project = " + project + " goal = " + goal + " step = " + step);
//			return null;
//		}
//	}
	
	//---------------Meta XML Files for Project -------------------------
	
	public File getProjectMetaXMLFile(Project project) {
		if(project != null) {
			File projectXMLMetaXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Meta.xml");
			return projectXMLMetaXMLFile;
		} else {
			logger.error("Cannot getProjectMetaXMLFile. project = null.");
			return null;
		}
	}
	
	public File getGoalMetaXMLFile(Project project, Goal goal) {
		if(project != null && goal != null) {
			File goalMetaXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Meta.xml");
			return goalMetaXMLFile;
		} else {
			logger.error("Cannot getGoalMetaXMLFile. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	//---------------Data XML Files for Project ----------------------------------
	
//	public File getStepDataXMLFile(Project project, Goal goal, Step step) {
//		if(project != null && goal != null && step != null) {
//			File stepDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + step.getGuid() + "\\Data.xml");
//			return stepDataXMLFile;
//		} else {
//			logger.error("Cannot getStepDataXMLFile. project = " + project + " goal = " + goal + " step = " + step);
//			return null;
//		}
//	}
	
	public File getDataXMLFile(Project project, Goal goal, ContentPanel contentPanel) {
		if(project != null && goal != null && contentPanel != null) {
			File dataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + contentPanel.getGuid() + "\\Data.xml");
			return dataXMLFile;
		} else {
			logger.error("Cannot getDataXMLFile. project = " + project + " goal = " + goal + " contentPanel = " + contentPanel);
			return null;
		}
	}
	
	public File getDynamicActivityDataXMLFile(Project project, Goal goal, String guid) {
		if(project != null && goal != null && guid != null) {
			File dynamicActivityDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + guid + "\\Data.xml");
			return dynamicActivityDataXMLFile;
		} else {
			logger.error("Cannot getDynamicActivityDataXMLFile. project = " + project + " goal = " + goal + " guid = " + guid);
			return null;
		}
	}
	
	//-----------Web View Files for GUID------------------------------------
	
	public File getIndexHTMLFileForInteractiveActivity(Project project, Goal goal, String guid) {
		File HTMLFile = new File(getGUIDDataDirectory(project, goal) + "\\" + guid + "\\index.html");
		return HTMLFile;
	}
	
	public File getJSONPWordCloudFileForInteractiveActivity(Project project, Goal goal, String guid) {
		File jsonpFile = new File(getGUIDDataDirectory(project, goal) + "\\" + guid + "\\wordcloud.jsonp");
		return jsonpFile;
	}
	
	//------------------ Static General ERB Data ----------------------------------------------
	
	public File getStaticSupportingDOCDirectory() {
		File supportingDOCDirectory = new File(constants.getPathToERBStaticDataFolder() + "\\Supporting_Docs");
		return supportingDOCDirectory;
	}
	
	public File getStaticAvailableContentXMLFile() {
		File availableContentFile = new File(constants.getPathToERBStaticDataFolder()+ "\\Available_Content.xml");
		return availableContentFile;
	}
	
	public File getStaticChaptersXMLFile() {
		File chaptersFile = new File(constants.getPathToERBStaticDataFolder() + "\\Chapters.xml");
		return chaptersFile;
	}
	
	public File getStaticGoalCategoriesXMLFile() {
		File goalCategoriesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Goal_Categories.xml");
		return goalCategoriesFile;
	}
	
	public File getStaticAvailableResourcesXMLFile() {
		File availableResourcesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Available_Resources.xml");
		return availableResourcesFile;
	}
	
	public File getStaticSupportingFormTextXML(String id) {
		File formTextXML = new File(constants.getPathToERBStaticDataFolder() + "\\Supporting_Docs\\" + id + "\\form_text.xml");
		return formTextXML;
	}
	
	public File getStaticWordCloudHTML() {
		File wordCloudHTMLFile = new File(constants.getPathToERBStaticDataFolder() + "\\JavaScript\\WordCloud\\index.html");
		return wordCloudHTMLFile;
	}
	public File getStaticIconPNGFile(String id) {
		File iconFile = new File(constants.getPathToERBStaticDataFolder() + "\\Supporting_Docs\\" + id + "\\icon.png");
		return iconFile;
	}
	
	//--------------General File Routines ---------------------------------------
	
	public void openFileOnDesktop(File fileToToOpen) {
		try {
			if (fileToToOpen != null && fileToToOpen.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(fileToToOpen);
			} else {
				logger.error(fileToToOpen.getPath() + " either does not exist or desktop is not supported.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void deleteDirectory(File directory) {
		if (directory != null) {
			File[] allContents = directory.listFiles();
			if (allContents != null) {
				for (File file : allContents) {
					deleteDirectory(file);
				}
			}
			directory.delete();
		} else {
			logger.error("Cannot deleteDirectory. directory = null.");
		}
	}

	public void copyFile(File source, File dest) {
		if (source != null && source.exists() && dest != null) {
			try {
				InputStream is = new FileInputStream(source);
				OutputStream os = new FileOutputStream(dest);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
			} catch (Exception e) {
				logger.error("Cannot copyFile. " + e.getMessage());
			}
		} else {
			logger.error("Cannot copyFile. source = " + source + " dest = " + dest);
		}
	}
	
	public void copyDirectory(File source, File dest) {
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			logger.error("Cannot copyDirectory. source = " + source + " dest = " + dest);		}
	}
	
	//------------------------------------------------------------------------
	
	public void createGUIDDataDirectory(Project project, Goal goal) {
		File GUIDDataDirectory = getGUIDDataDirectory(project, goal);
		if (!GUIDDataDirectory.exists()) {
			GUIDDataDirectory.mkdir();
		}
	}

	public void createGUIDDirectoriesForGoal(Project project, Goal goal, ArrayList<Chapter> uniqueChapters) {
		createGUIDDataDirectory(project, goal);
		for (Chapter chapter : uniqueChapters) {
			File chapterGUIDDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + chapter.getGuid());
			if(!chapterGUIDDirectory.exists()) chapterGUIDDirectory.mkdir();
			ERBItem erbItemToPass = new ERBItem(chapter.getId(), chapter.getGuid(), chapter.getLongName(), chapter.getShortName(), chapter);
			writeGUIDDirectoriesForGoal(erbItemToPass, null, project, goal);
		}
	}
	
	private void writeGUIDDirectoriesForGoal(ERBItem erbItem, ERBItem parentERBItem, Project project, Goal goal) {
		if (erbItem.getObject().toString().contains("Chapter")) { // is chapter
			Chapter chapter = (Chapter) erbItem.getObject();
			for (ContentPanel c : chapter.getContentPanels()) {
				ERBItem erbItemToPass = new ERBItem(c.getId(), c.getGuid(), c.getLongName(), c.getShortName(), c);
				writeGUIDDirectoriesForGoal(erbItemToPass, erbItem, project, goal);
			}
		} else { // is not chapter
			ContentPanel contentPanel = (ContentPanel) erbItem.getObject();
			if (contentPanel.getGuid() != null) {
				File contentPanelDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + contentPanel.getGuid());
				if (!contentPanelDirectory.exists())
					contentPanelDirectory.mkdir();
			}
			if (contentPanel.getNextLayerContentPanels().size() > 0) { // Has children
				for (ContentPanel c2 : contentPanel.getNextLayerContentPanels()) {
					ERBItem erbItemToPass = new ERBItem(c2.getId(), c2.getGuid(), c2.getLongName(), c2.getShortName(), c2);
					writeGUIDDirectoriesForGoal(erbItemToPass, erbItem, project, goal);
				}
			}
		}
	}

}
