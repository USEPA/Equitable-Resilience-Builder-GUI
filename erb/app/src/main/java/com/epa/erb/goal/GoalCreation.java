package com.epa.erb.goal;

import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
public class GoalCreation{

	private App app;
	private Project project;
	public GoalCreation(App app, Project project) {
		this.app = app;
		this.project = project;
	}
	
	private FileHandler fileHandler = new FileHandler();
	private XMLManager xmlManager = new XMLManager(app);
	private Logger logger = LogManager.getLogger(GoalCreation.class);
	
	public String cleanStringForWindows(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "_");
	}
	
	public void writeProjectMetaData(Project project) {
		File projectMetaFile = fileHandler.getProjectMetaXMLFile(project);
		xmlManager.writeProjectMetaXML(projectMetaFile, project);
	}
	
	public void writeGoalsMetaData(ArrayList<Goal> goals) {
		if (goals != null) {
			createGoalsDirectory();
			for (Goal goal : goals) {
				createGoalDirectory(goal);
				createSupportingDocDirectory(goal);
				File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(project, goal);
				if(supportingDOCDirectory != null && supportingDOCDirectory.exists()) {
					copyGlobalSupportingDocsToGoalSupportingDocs(supportingDOCDirectory);
				}
			}
		} else {
			logger.error("Cannot writeGoalsMetaData. goals is null.");
		}
	}
	
	private void copyGlobalSupportingDocsToGoalSupportingDocs(File goalSupportingDOCDirectory) {
		if(goalSupportingDOCDirectory != null) {
			File staticGlobalSupportingDOCDirectory = fileHandler.getStaticSupportingDOCDirectory();
			for(File sourceFile: staticGlobalSupportingDOCDirectory.listFiles()) {
				File destFile = new File(goalSupportingDOCDirectory + "\\" + sourceFile.getName());
				fileHandler.copyFile(sourceFile, destFile);
			}
		}
	}
	
	private void createSupportingDocDirectory(Goal goal) {
		if (goal != null) {
			File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(project, goal);
			if (supportingDOCDirectory != null && !supportingDOCDirectory.exists()) {
				supportingDOCDirectory.mkdir();
			}
		} else {
			logger.error("Cannot createSupportingDocDirectory. goal is null.");
		}
	}
	
	private void createGoalDirectory(Goal goal) {
		if (goal != null) {
			File goalDirectory = fileHandler.getGoalDirectory(project, goal);
			if (goalDirectory !=null && !goalDirectory.exists()) {
				goalDirectory.mkdir();
			}
		} else {
			logger.error("Cannot createGoalDirectory. goal is null.");
		}
	}
	
	private void createGoalsDirectory() {
		File goalDirectory = fileHandler.getGoalsDirectory(project);
		if(goalDirectory != null && !goalDirectory.exists()) {
			goalDirectory.mkdir();
		}
	}
		
	public GoalCategory getGoalCategory(String goalCategoryName) {
		if (goalCategoryName != null) {
			ArrayList<GoalCategory> goalCategories = app.getAvailableGoalCategories();
			for (GoalCategory goalCategory : goalCategories) {
				if (goalCategory.getCategoryName().contentEquals(goalCategoryName)) {
					return goalCategory;
				}
			}
			logger.debug("Goal Category returned is null.");
			return null;
		} else {
			logger.error("Cannot getGoalCategory. goalCategoryName is null.");
			return null;
		}
	}
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Project getProject() {
		return project;
	}


}
