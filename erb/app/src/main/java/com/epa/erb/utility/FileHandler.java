package com.epa.erb.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.epa.erb.Activity;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;

public class FileHandler {
	
	public FileHandler() {
		
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(FileHandler.class);
	
	public void openFile(File fileToToOpen) {
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
	
	public void convertDocxToPDF2(File docxFileToConvert, String pathToConvertedPDF) {
	 try {
         InputStream templateInputStream = new FileInputStream(docxFileToConvert);
         WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);
         MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

         FileOutputStream os = new FileOutputStream(pathToConvertedPDF);
         Docx4J.toPDF(wordMLPackage,os);
         os.flush();
         os.close();
     } catch (Throwable e) {

         e.printStackTrace();
     } 
	}
	
	public void convertDocxToPDF(File docxFileToConvert, String pathToConvertedPDF) {
		if (docxFileToConvert != null && docxFileToConvert.exists() && pathToConvertedPDF != null) {
			try {
				Document documentToConvert = new Document(docxFileToConvert.getPath());
				documentToConvert.save(pathToConvertedPDF, SaveFormat.PDF);
			} catch (Exception e) {
				logger.error("Cannot convertDocxToPDF. " + e.getMessage());
			}
		} else {
			logger.error("Cannot convertDocxToPDF. docxFileToConvert = " + docxFileToConvert + " pathToConvertedPDF = " + pathToConvertedPDF);
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
			File activityDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities\\" + activity.getActivityID() + "\\Data.xml");
			return activityDataXMLFile;
		} else {
			logger.error("Cannot getActivityDataXMLFile. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
	
	public File getActivityMetaXMLFile(Project project, Goal goal, Activity activity) {
		if(project != null && goal != null && activity != null) {
			File activityDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities\\" + activity.getActivityID() + "\\Meta.xml");
			return activityDataXMLFile;
		} else {
			logger.error("Cannot getActivityMetaXMLFile. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
	
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
		if(project != null && goal != null) {
			File activitiesDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities");
			return activitiesDirectory;
		} else {
			logger.error("Cannot getActivitiesDirectory. project = " + project + " goal = " + goal);
			return null;
		}
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
	
	public File getGoalDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File goalDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName());
			return goalDirectory;
		} else {
			logger.error("Cannot getGoalDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getActivityDirectory(Project project, Goal goal, Activity activity) {
		if(project != null && goal != null && activity != null) {
			File activityDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities\\" + activity.getActivityID());
			return activityDirectory;
		} else {
			logger.error("Cannot getActivityDirectory. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
	
	public File getChaptersFileToParse() {
		File chaptersFile = new File(constants.getPathToERBFolder() + "\\Static_Data\\Chapters\\Chapters.xml");
		return chaptersFile;
	}
	
	public File getActivityTypesFileToParse() {
		File activityTypesFile = new File(constants.getPathToERBFolder() + "\\Static_Data\\Activities\\Activity_Types.xml");
		return activityTypesFile;
	}
	
	public File getAvailableActivitiesFileToParse() {
		File availableActivitiesFile = new File(constants.getPathToERBFolder() + "\\Static_Data\\Activities\\Available_Activities.xml");
		return availableActivitiesFile;
	}
	
	public File getGoalCategoriesFileToParse() {
		File goalCategoriesFile = new File(constants.getPathToERBFolder() + "\\Static_Data\\Goals\\Goal_Categories.xml");
		return goalCategoriesFile;
	}
	
	public File getGlobalActivityWordDoc(Activity activity) {
		if(activity != null) {
			File activityDocx = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\ChapterActivities_DOC\\" + activity.getFileName());
			return activityDocx;
		} else {
			logger.error("Cannot getGlobalActivityWordDoc. activity = null.");
			return null;
		}
	}
	
	public File getGlobalActivityPDFDoc(Activity activity) {
		if(activity != null) {
			String pdfFileName = activity.getFileName().replace(".docx", ".pdf");
			File activityDocx = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\ChapterActivities_PDF\\" + pdfFileName);
			return activityDocx;
		} else {
			logger.error("Cannot getGlobalActivityPDFDoc. activity = null.");
			return null;
		}
	}
	
	public File getActivityWordDoc(Project project, Goal goal, Activity activity) {
		if(project != null && goal != null && activity != null) {
			File activityPDF = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities\\" + activity.getActivityID() + "\\" + activity.getFileName());
			return activityPDF;
		} else {
			logger.error("Cannot getActivityWordDoc. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
	
	public File getActivityPDFDoc(Project project, Goal goal, Activity activity) {
		if (project != null && goal != null && activity != null) {
			String pdfFileName = activity.getFileName().replace(".docx", ".pdf");
			File activityPDF = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName()+ "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities\\" + activity.getActivityID() + "\\"+ pdfFileName);
			return activityPDF;
		} else {
			logger.error("Cannot getActivityPDFDoc. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
	
	public File getActivityFormContentXML(Project project, Goal goal, Activity activity) {
		if (project != null && goal != null && activity != null) {
			File activityContentXML = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Activities_XML\\" + activity.getActivityID() + "\\form_text.xml");
			return activityContentXML;
		} else {
			logger.error("Cannot activityContentXML. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
	
	public File getActivityFormLinksXML(Project project, Goal goal, Activity activity) {
		if (project != null && goal != null && activity != null) {
			File activityLinksXML = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Activities_XML\\" + activity.getActivityID() + "\\form_links.xml");
			return activityLinksXML;
		} else {
			logger.error("Cannot getActivityFormLinksXML. project = " + project + " goal = " + goal + " activity = " + activity);
			return null;
		}
	}
}
