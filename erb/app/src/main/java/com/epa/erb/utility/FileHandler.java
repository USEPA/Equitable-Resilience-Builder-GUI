package com.epa.erb.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;

public class FileHandler {

	private Logger logger;
	private Constants constants = new Constants();

	private App app;
	public FileHandler(App app) {
		this.app = app;
		
		logger = app.getLogger();
	}
	
	//--------------LOCATED IN USER.HOME
	
	public File getEquitableResilienceBuilderDirectory() {
		File erbDirectory = new File(constants.getPathToERBDirectory());
		return erbDirectory;
	}
	
	public File getLogsDirectory() {
		File logsDirectory = new File(constants.getPathToERBLogsDirectory());
		return logsDirectory;
	}

	public File getProjectsDirectory() {
		File projectsDirectory = new File(constants.getPathToERBProjectsDirectory());
		return projectsDirectory;
	}
	
	public File getTempDirectory() {
		File tempDirectory = new File(constants.getPathToERBTempDirectory());
		return tempDirectory;
	}
	
	public File getGoalsDirectory(Project project) {
		if (project != null) {
			File goalsDirectory = new File(constants.getPathToERBProjectsDirectory() + File.separator + project.getProjectCleanedName() + File.separator + "Goals");
			return goalsDirectory;
		} else {
			logger.log(Level.WARNING, "getGoalsDirectory returning null.");
			return null;
		}
	}

	public File getProjectDirectory(Project project) {
		if (project != null) {
			File projectDirectory = new File(constants.getPathToERBProjectsDirectory() + File.separator + project.getProjectCleanedName());
			return projectDirectory;
		} else {
			logger.log(Level.WARNING, "getProjectDirectory returning null.");
			return null;
		}
	}

	public File getGoalDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File goalDirectory = new File(constants.getPathToERBProjectsDirectory() + File.separator + project.getProjectCleanedName() + File.separator + "Goals" + File.separator + goal.getGoalCleanedName());
			return goalDirectory;
		} else {
			logger.log(Level.WARNING, "getGoalDirectory returning null.");
			return null;
		}
	}

	public File getSupportingDOCDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File supportingDOCDirectory = new File(getGoalDirectory(project,goal) + File.separator + "Supporting_DOC");
			return supportingDOCDirectory;
		} else {
			logger.log(Level.WARNING, "getSupportingDOCDirectory returning null.");
			return null;
		}
	}

	public File getGUIDDataDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File guidsDirectory = new File(getGoalDirectory(project, goal) +  File.separator + "GUID_Data");
			return guidsDirectory;
		} else {
			logger.log(Level.WARNING, "getGUIDDataDirectory returning null.");
			return null;
		}
	}

	public File getIndicatorsDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File indicatorsDirectory = new File(getGoalDirectory(project, goal) + File.separator + "Indicators");
			return indicatorsDirectory;
		} else {
			logger.log(Level.WARNING, "getIndicatorsDirectory returning null.");
			return null;
		}
	}

	public File getMyUploadsDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File myUploadsDirectory = new File(getGoalDirectory(project, goal) +  File.separator + "MyUploads");
			return myUploadsDirectory;
		} else {
			logger.log(Level.WARNING, "getMyUploadsDirectory returning null.");
			return null;
		}
	}

	public File getMyPortfolioDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File myPortfolioDirectory = new File(getGoalDirectory(project, goal) + File.separator + "MyPortfolio");
			return myPortfolioDirectory;
		} else {
			logger.log(Level.WARNING, "getMyPortfolioDirectory returning null.");
			return null;
		}
	}

	public File getProjectMetaXMLFile(Project project) {
		if (project != null) {
			File projectXMLMetaXMLFile = new File(getProjectDirectory(project) + File.separator + "Meta.xml");
			return projectXMLMetaXMLFile;
		} else {
			logger.log(Level.WARNING, "getProjectMetaXMLFile returning null.");
			return null;
		}
	}

	public File getGoalMetaXMLFile(Project project, Goal goal) {
		if (project != null && goal != null) {
			File goalMetaXMLFile = new File(getGoalDirectory(project, goal) + File.separator + "Meta.xml");
			return goalMetaXMLFile;
		} else {
			logger.log(Level.WARNING, "getGoalMetaXMLFile returning null.");
			return null;
		}
	}
	
	public File getDataXMLFile(Project project, Goal goal, ERBContentItem erbContentItem) {
		if (project != null && goal != null && erbContentItem != null) {
			File dataXMLFile = new File(getGUIDDataDirectory(project, goal) + File.separator + erbContentItem.getGuid() + File.separator + "Data.xml");
			return dataXMLFile;
		} else {
			logger.log(Level.WARNING, "getDataXMLFile returning null.");
			return null;
		}
	}

	public File getJSONPWordCloudFileForInteractiveActivity(Project project, Goal goal, String guid, String webViewId) {
		File jsonpFile = new File(getGUIDDataDirectory(project, goal) + File.separator + guid + File.separator + webViewId + File.separator + "wordcloud.jsonp");
		return jsonpFile;
	}
	
	public File getStaticWordCloudDirectory() {
		File wordCloudDirectory = new File(constants.getPathToERBDirectory() + File.separator + "JavaScript" + File.separator + "WordCloud");
		return wordCloudDirectory;
	}

	// -------LOCATED IN RESOURCES
	
	public File getSupportingDocFromResources(String worksheetName) {
		String resourcePath = "/staticData/supportingDocs/" + worksheetName;
		File supportingDocFile = copyResourceFileToTempDir(resourcePath, worksheetName);
		return supportingDocFile;
	}

	public File getAvailableContentFileFromResources() {
		String resourcePath = "/staticData/availableContent.xml";
		File availableContentXMLFile = copyResourceFileToTempDir(resourcePath, "availableContent.xml");
		return availableContentXMLFile;
	}

	public File getWorksheetsFileFromResources() {
		String resourcePath = "/staticData/worksheets.xml";
		File worksheetsXMLFile = copyResourceFileToTempDir(resourcePath, "worksheets.xml");
		return worksheetsXMLFile;
	}

	public File getIndicatorCardsFileFromResources() {
		String resourcePath = "/staticData/indicatorCards.xml";
		File indicatorCardsXMLFile = copyResourceFileToTempDir(resourcePath, "indicatorCards.xml");
		return indicatorCardsXMLFile;
	}

	public File getGoalCategoriesFileFromResources() {
		String resourcePath = "/staticData/goalCategories.xml";
		File goalCategoriesFile = copyResourceFileToTempDir(resourcePath, "goalCategories.xml");
		return goalCategoriesFile;
	}
	
	public File getReportDataFileFromResources() {
		String resourcePath = "/staticData/reportData.xml";
		File reportDataFile = copyResourceFileToTempDir(resourcePath, "reportData.xml");
		return reportDataFile;
	}

	public File getFormContentFileFromResources(String id) {
		if (id != null) {
			String resourcePath = "/staticData/contentXMLs/" + id + "/form_text.xml";
			String fileName = "form_text_" + id + ".xml";
			File formContentFile = copyResourceFileToTempDir(resourcePath, fileName);
			return formContentFile;
		} else {
			logger.log(Level.WARNING, "getFormContentFileFromResources returning null.");
			return null;
		}
	}

	public File getIconFileFromResources(String id) {
		String resourcePath = "/staticData/contentXMLs/" + id + "/icon.png";
		File iconFile = new File(getClass().getResource(resourcePath).getFile());
		return iconFile;
	}
	
	public String getIconFilePathFromResources(String id) {
		String resourcePath = "/staticData/contentXMLs/" + id + "/icon.png";
		return resourcePath;
	}

	// ------------------------------------------------------------------------

	public void createGUIDDataDirectory(Project project, Goal goal) {
		File GUIDDataDirectory = getGUIDDataDirectory(project, goal);
		if (!GUIDDataDirectory.exists()) {
			GUIDDataDirectory.mkdir();
		}
	}

	public void createGUIDDirectoriesForGoal2(Project project, Goal goal, ArrayList<ERBContentItem> uniqueERBContentItems) {
		createGUIDDataDirectory(project, goal);
		for (ERBContentItem erbContentItem : uniqueERBContentItems) {
			createDirectory(project, goal, erbContentItem);
		}
	}

	private void createDirectory(Project p, Goal g, ERBContentItem e) {
		File guidDir = new File(getGUIDDataDirectory(p, g) + File.separator + e.getGuid());
		if (!guidDir.exists()) guidDir.mkdir();

		if (e.getChildERBContentItems().size() > 0) {
			for (ERBContentItem child : e.getChildERBContentItems()) {
				createDirectory(p, g, child);
			}
		}
	}

	// -------------------------------------------------------------------------

	private File copyResourceFileToTempDir(String pathToResourceFile, String fileName) {
		File tempDirectory = getTempDirectory();
		if(!tempDirectory.exists()) {
			tempDirectory.mkdir();
		}
		
		try {
			File outputFile = new File(constants.getPathToERBTempDirectory() + File.separator + fileName);
			InputStream inputStream = getClass().getResourceAsStream(pathToResourceFile);
			OutputStream outputStream = new FileOutputStream(outputFile);
			IOUtils.copy(inputStream, outputStream);
			return outputFile;
		} catch(IOException e) {
			logger.log(Level.SEVERE, "Failed to copy resource file to temp dir: " + e.getMessage());
		}
		return null;
	}
	
	//---------------------------------------------------------------------------
	
	public void openFileOnDesktop(File fileToOpen) {
		try {
			if (fileToOpen != null && fileToOpen.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(fileToOpen);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to open file on desktop: " + e.getMessage());
		}
	}

	public void deleteDirectory(File directory) {
		try {
			if (directory != null) {
				File[] allContents = directory.listFiles();
				if (allContents != null) {
					for (File file : allContents) {
						deleteDirectory(file);
					}
				}
				directory.delete();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to delete directory: " + e.getMessage());
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
				logger.log(Level.SEVERE, "Failed to copy file: " + e.getMessage());
			}
		}
	}
	
	public void copyDirectory(File source, File dest) {
		if (source != null && source.exists() && dest != null) {
			try {
			    FileUtils.copyDirectory(source, dest);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Failed to copy directory: " + e.getMessage());
			}
		}
	}

	public App getApp() {
		return app;
	}

}
