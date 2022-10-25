package com.epa.erb.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.DynamicActivity;
import com.epa.erb.Step;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;

public class FileHandler {
	
	public FileHandler() {
		
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(FileHandler.class);
	
	//--------------------------------------------------------------------------
	
	public File getProjectsDirectory() {
		File projectsDirectory = new File(constants.getPathToERBProjectsFolder());
		return projectsDirectory;
	}
	
	public File getProjectDirectory(Project project) {
		if(project != null) {
			File projectDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName());
			return projectDirectory;
		} else {
			logger.error("Cannot getProjectDirectory. project = " + project);
			return null;
		}
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
	
	public File getGoalDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File goalDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName());
			return goalDirectory;
		} else {
			logger.error("Cannot getGoalDirectory. project = " + project + " goal = " + goal);
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

	public File getActivityDirectory(Project project, Goal goal, Activity activity) {
		if (project != null && goal != null && activity != null) {
			File activityDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities_XML\\" + activity.getActivityID());
			return activityDirectory;
		} else {
			logger.error("Cannot getActivityDirectory. project = " + project + " goal = " + goal + " activity = "
					+ activity);
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

	public File getStepDirectory(Project project, Goal goal, Step step) {
		if (project != null && goal != null && step != null) {
			File stepDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Steps_XML\\" + step.getStepID());
			return stepDirectory;
		} else {
			logger.error("Cannot getStepDirectory. project = " + project + " goal = " + goal + " step = " + step);
			return null;
		}
	}
	
	public File getSupportingDOCDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File supportingDOCDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Supporting_DOC");
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
	
	//--------------------------------------------------------------------------
	
	public File getProjectMetaXMLFile(Project project) {
		if(project != null) {
			File projectXMLMetaXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Meta.xml");
			return projectXMLMetaXMLFile;
		} else {
			logger.error("Cannot getProjectMetaXMLFile. project = null.");
			return null;
		}
	}
	
	public File getProjectMetaXMLFile(File projectDir) {
		if(projectDir != null) {
			File projectXMLMetaXMLFile = new File(projectDir+ "\\Meta.xml");
			return projectXMLMetaXMLFile;
		} else {
			logger.error("Cannot getProjectMetaXMLFile. projectDir = null.");
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
	
	public File getActivityDataXMLFile(Project project, Goal goal, Activity activity) {
		if(project != null && goal != null && activity != null) {
			File activityDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + activity.getGUID() + "\\Data.xml");
			return activityDataXMLFile;
		} else {
			logger.error("Cannot getActivityDataXMLFile. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
	
	public File getStepDataXMLFile(Project project, Goal goal, Step step) {
		if(project != null && goal != null && step != null) {
			File stepDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + step.getGUID() + "\\Data.xml");
			return stepDataXMLFile;
		} else {
			logger.error("Cannot getStepDataXMLFile. project = " + project + " goal = " + goal + " step = " + step);
			return null;
		}
	}
	
	public File getDynamicActivityDataXMLFile(Project project, Goal goal, DynamicActivity dynamicActivity) {
		if(project != null && goal != null && dynamicActivity != null) {
			File dynamicActivityDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + dynamicActivity.getGUID() + "\\Data.xml");
			return dynamicActivityDataXMLFile;
		} else {
			logger.error("Cannot getDynamicActivityDataXMLFile. project = " + project + " goal = " + goal + " dynamicActivity = " + dynamicActivity);
			return null;
		}
	}
	
	//--------------------------------------------------------------------------
	
	public File getStaticSupportingDOCDirectory() {
		File supportingDOCDirectory = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Supporting_DOC");
		return supportingDOCDirectory;
	}
	
	public File getStaticAvailableActivitiesXMLFile() {
		File availableActivitiesFile = new File(constants.getPathToERBStaticDataFolder()+ "\\Activities\\Available_Activities.xml");
		return availableActivitiesFile;
	}
	
	public File getStaticAvailableStepsXMLFile() {
		File availableStepsFile = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Available_Steps.xml");
		return availableStepsFile;
	}
	
	public File getStaticActivityFormText(Activity activity) {
		if (activity != null) {
			File activityContentXML = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Activities_XML\\" + activity.getActivityID() + "\\form_text.xml");
			return activityContentXML;
		} else {
			logger.error("Cannot getStaticActivityFormText. activity = null");
			return null;
		}
	}
	
	public File getStaticStepFormText(Step step) {
		if (step != null) {
			File stepContentXMLFile = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Steps_XML\\" + step.getStepID() + "\\form_text.xml");
			return stepContentXMLFile ;
		} else {
			logger.error("Cannot getStaticStepFormText. step = null.");
			return null;
		}
	}
	
	public File getStaticAvailableDynamicActivitiesXMLFile() {
		File availableDynamicActivitiesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Available_Dynamic_Activities.xml");
		return availableDynamicActivitiesFile;
	}
	
	public File getStaticChaptersXMLFile() {
		File chaptersFile = new File(constants.getPathToERBStaticDataFolder() + "\\Chapters\\Chapters.xml");
		return chaptersFile;
	}
	
	public File getStaticChapterFormAbout(Chapter chapter) {
		if (chapter != null) {
			String xmlFileName = chapter.getStringName().replaceAll(" ", "") + "_About.xml";
			File chapterAboutXML = new File(constants.getPathToERBStaticDataFolder() + "\\Chapters\\Chapters_XML\\" + xmlFileName);
			return chapterAboutXML;
		} else {
			logger.error("Cannot getStaticChapterFormAbout. chapter = null");
			return null;
		}
	}
	
	public File getStaticGoalCategoriesXMLFile() {
		File goalCategoriesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Goals\\Goal_Categories.xml");
		return goalCategoriesFile;
	}
	
	public File getStaticAvailableIntroXMLFile() {
		File availableIntroFile = new File(constants.getPathToERBStaticDataFolder() + "\\Intro\\Available_Intro.xml");
		return availableIntroFile;
	}
	
	public File getStaticIntroFormText(String id) {
		if(id != null) {
			File availableIntroFile = new File(constants.getPathToERBStaticDataFolder() + "\\Intro\\" + id + "\\form_text.xml");
			return availableIntroFile;
		} else {
			logger.error("Cannot getStaticIntroFormText. id = " + id);
			return null;
		}
	}
	
	public File getStaticAvailableResourcesXMLFile() {
		File availableResourcesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Resources\\Available_Resources.xml");
		return availableResourcesFile;
	}
	
	public File getStaticResourceFormTextXML(String id) {
		if(id != null) {
			File availableResourcesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Resources\\" + id + "\\form_text.xml");
			return availableResourcesFile;
		} else {
			logger.error("Cannot getStaticResourceFormTextXML. id = " + id);
			return null;
		}
	}
	
	//-------------------------------------------------------------------------
	
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
			File chapterGUIDDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + chapter.getGUID());
			if(!chapterGUIDDirectory.exists()) chapterGUIDDirectory.mkdir();
			for (Activity activity : chapter.getAssignedActivities()) {
				File chapterActivityDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + activity.getGUID());
				if(!chapterActivityDirectory.exists()) chapterActivityDirectory.mkdir();
				for (Step step : activity.getAssignedSteps()) {
					File activityStepDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + step.getGUID());
					if(!activityStepDirectory.exists()) activityStepDirectory.mkdir();
					for (DynamicActivity dynamicActivity : step.getAssignedDynamicActivities()) {
						File stepDynamicActivityDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + dynamicActivity.getGUID());
						if(!stepDynamicActivityDirectory.exists()) stepDynamicActivityDirectory.mkdir();
					}
				}
				for (DynamicActivity dynamicActivity : activity.getAssignedDynamicActivities()) {
					File activityDynamicActivityDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + dynamicActivity.getGUID());
					if(!activityDynamicActivityDirectory.exists()) activityDynamicActivityDirectory.mkdir();
				}
			}
			for (Step step : chapter.getAssignedSteps()) {
				File chapterStepDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + step.getGUID());
				if(!chapterStepDirectory.exists()) chapterStepDirectory.mkdir();
				for (DynamicActivity dynamicActivity : step.getAssignedDynamicActivities()) {
					File stepDynamicActivityDirectory = new File(getGUIDDataDirectory(project, goal) + "\\" + dynamicActivity.getGUID());
					if(!stepDynamicActivityDirectory.exists()) stepDynamicActivityDirectory.mkdir();
				}
			}
		}
	}
	
}
