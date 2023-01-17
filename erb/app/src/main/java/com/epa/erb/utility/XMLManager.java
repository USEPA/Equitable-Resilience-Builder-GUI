package com.epa.erb.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.ERBItem;
import com.epa.erb.ERBItemFinder;
import com.epa.erb.InteractiveActivity;
import com.epa.erb.Step;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.noteboard.CategorySectionController;
import com.epa.erb.noteboard.PostItNoteController;
import com.epa.erb.project.Project;
import com.epa.erb.wordcloud.WordCloudItem;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class XMLManager {

	private App app;
	public XMLManager(App app) {
		this.app = app;
	}

	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	private Logger logger = LogManager.getLogger(XMLManager.class);
	
	//Parse Chapters
	public ArrayList<Chapter> parseChaptersXML(File xmlFile) {
		if (xmlFile!= null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<Chapter> chapters = new ArrayList<Chapter>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList chapterNodeList = doc.getElementsByTagName("chapter");
				for (int i = 0; i < chapterNodeList.getLength(); i++) {
					Node chapterNode = chapterNodeList.item(i);
					// Chapter
					if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element chapterElement = (Element) chapterNode;
						String id = chapterElement.getAttribute("id");
						String longName = chapterElement.getAttribute("longName");
						String shortName = chapterElement.getAttribute("shortName");
						String status = chapterElement.getAttribute("status");
						String notes = chapterElement.getAttribute("notes");
						String description = chapterElement.getAttribute("description");
						int number = Integer.parseInt(chapterElement.getAttribute("number"));
						Chapter chapter = new Chapter(id, null, longName, shortName, status, notes, description, number);
						chapters.add(chapter);
					}
				}
				return chapters;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	//Parse AvailableActivities
	public ArrayList<Activity> parseAvailableActivitiesXML(File xmlFile){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<Activity> availableActivities = new ArrayList<Activity>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList activitiesNodeList = doc.getElementsByTagName("activity");
				for (int i = 0; i < activitiesNodeList.getLength(); i++) {
					Node activityNode = activitiesNodeList.item(i);
					// Activity
					if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityElement = (Element) activityNode;
						String activityID = activityElement.getAttribute("id");
						String longName = activityElement.getAttribute("longName");
						String shortName = activityElement.getAttribute("shortName");
						String status = activityElement.getAttribute("status");
						String notes = activityElement.getAttribute("notes");
						String rating = activityElement.getAttribute("rating");
						Activity activity = new Activity(activityID, null, longName, shortName, status, notes, rating);
						availableActivities.add(activity);
					}
				}
				return availableActivities;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableSteps
	public ArrayList<Step> parseAvailableStepsXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<Step> availableSteps = new ArrayList<Step>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList stepsNodeList = doc.getElementsByTagName("step");
				for (int i = 0; i < stepsNodeList.getLength(); i++) {
					Node stepNode = stepsNodeList.item(i);
					// Step
					if (stepNode.getNodeType() == Node.ELEMENT_NODE) {
						Element stepElement = (Element) stepNode;
						String id = stepElement.getAttribute("id");
						String longName = stepElement.getAttribute("longName");
						String shortName = stepElement.getAttribute("shortName");
						String status = stepElement.getAttribute("status");
						String notes = stepElement.getAttribute("notes");
						String rating = stepElement.getAttribute("rating");
						String type = stepElement.getAttribute("type");
						Step step = new Step(id, null, longName, shortName, status, notes, rating, type);
						availableSteps.add(step);
					}
				}
				return availableSteps ;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableInteractiveActivities
	public ArrayList<InteractiveActivity> parseAvailableInteractiveActivitiesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<InteractiveActivity> availableDynamicActivities = new ArrayList<InteractiveActivity>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList dynamicActivitiesNodeList = doc.getElementsByTagName("interactive_activity");
				for (int i = 0; i < dynamicActivitiesNodeList.getLength(); i++) {
					Node dynamicActivityNode = dynamicActivitiesNodeList.item(i);
					// Dynamic Activity
					if (dynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
						Element dynamicActivityElement = (Element) dynamicActivityNode;
						String id = dynamicActivityElement.getAttribute("id");
						String longName = dynamicActivityElement.getAttribute("longName");
						String shortName = dynamicActivityElement.getAttribute("shortName");
						String status = dynamicActivityElement.getAttribute("status");
						InteractiveActivity interactiveActivity = new InteractiveActivity(id, null, longName, shortName, status);
						availableDynamicActivities.add(interactiveActivity);
					}
				}
				return availableDynamicActivities;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableResources
	public HashMap<String, String> parseAvailableResourcesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				HashMap<String, String> availableResources = new HashMap<String, String>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList resourceNodeList = doc.getElementsByTagName("resource");
				for (int i = 0; i < resourceNodeList.getLength(); i++) {
					Node resourceNode = resourceNodeList.item(i);
					// Resource
					if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
						Element resourceElement = (Element) resourceNode;
						String id = resourceElement.getAttribute("id");
						String name = resourceElement.getAttribute("name");
						availableResources.put(id, name);
					}
				}
				return availableResources;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableIntro
	public HashMap<String, String> parseAvailableIntroXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				HashMap<String, String> availableIntroPanels = new HashMap<String, String>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList intoNodeList = doc.getElementsByTagName("introPanel");
				for (int i = 0; i < intoNodeList.getLength(); i++) {
					Node introNode = intoNodeList.item(i);
					// Intro
					if (introNode.getNodeType() == Node.ELEMENT_NODE) {
						Element introElement = (Element) introNode;
						String id = introElement.getAttribute("id");
						String name = introElement.getAttribute("name");
						availableIntroPanels.put(id, name);
					}
				}
				return availableIntroPanels;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse GoalCategories
	public ArrayList<GoalCategory> parseGoalCategoriesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				ArrayList<GoalCategory> goalCategories = new ArrayList<GoalCategory>();
				NodeList goalCategoryNodeList = doc.getElementsByTagName("goalCategory");
				for (int h = 0; h < goalCategoryNodeList.getLength(); h++) {
					Node goalCategoryNode = goalCategoryNodeList.item(h);
					// GOAL CATEGORY
					if (goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element goalCategoryElement = (Element) goalCategoryNode;
						String goalCategoryName = goalCategoryElement.getAttribute("categoryName");
						ArrayList<Chapter> chapters = new ArrayList<Chapter>();
						NodeList chapterNodeList = goalCategoryElement.getElementsByTagName("chapter");
						for (int i = 0; i < chapterNodeList.getLength(); i++) {
							Node chapterNode = chapterNodeList.item(i);
							// CHAPTER
							if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
								Element chapterElement = (Element) chapterNode;
								String chapterID = chapterElement.getAttribute("id");
								Chapter chapter = erbItemFinder.getChapterById(app.getAvailableChapters(), chapterID);
								NodeList cActivityNodeList = chapterElement.getElementsByTagName("ChapterActivity");
								ArrayList<Activity> listOfChapterActivities = new ArrayList<Activity>();
								for (int j = 0; j < cActivityNodeList.getLength(); j++) {
									Node cActivityNode = cActivityNodeList.item(j);
									// CHAPTER -> ACTIVITY
									if (cActivityNode.getNodeType() == Node.ELEMENT_NODE) {
										Element cActivityElement = (Element) cActivityNode;
										String activityID = cActivityElement.getAttribute("id");
										Activity activity = erbItemFinder.getActivityById(app.getAvailableActivities(), activityID);
										if (activity != null) listOfChapterActivities.add(activity);
										NodeList caStepsNodeList = cActivityElement.getElementsByTagName("ChapterActivityStep");
										ArrayList<Step> listOfChapterActivitySteps = new ArrayList<Step>();
										for (int k = 0; k < caStepsNodeList.getLength(); k++) {
											Node caStepNode = caStepsNodeList.item(k);
											// CHAPTER -> ACTIVITY -> STEP
											if (caStepNode.getNodeType() == Node.ELEMENT_NODE) {
												Element caStepElement = (Element) caStepNode;
												String stepID = caStepElement.getAttribute("id");
												Step step = erbItemFinder.getStepById(app.getAvailableSteps(), stepID);
												if (step != null) listOfChapterActivitySteps.add(step);
												NodeList casDynamicActivitiesNodeList = caStepElement.getElementsByTagName("ChapterActivityStepInteractiveActivity");
												ArrayList<InteractiveActivity> listOfChapterActivityStepDynamicActivities = new ArrayList<InteractiveActivity>();
												for (int l = 0; l < casDynamicActivitiesNodeList.getLength(); l++) {
													Node casDynamicActivityNode = casDynamicActivitiesNodeList.item(l);
													// CHAPTER -> ACTIVITY -> STEP -> DYNAMIC ACTIVITY
													if (casDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
														Element casDynamicActivityElement = (Element) casDynamicActivityNode;
														String dynamicActivityID = casDynamicActivityElement.getAttribute("id");
														InteractiveActivity dynamicActivity = erbItemFinder.getInteractiveActivityById(app.getAvailableInteractiveActivities(), dynamicActivityID);
														if (dynamicActivity != null) listOfChapterActivityStepDynamicActivities.add(dynamicActivity);
													} // CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY ELEMENT
												} // CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY
												step.setAssignedDynamicActivities(listOfChapterActivityStepDynamicActivities);
											} // CHAPTER ACTIVITY STEP ELEMENT
										} // CHAPTER ACTIVITY STEP
										activity.setAssignedSteps(listOfChapterActivitySteps);
										NodeList caDynamicActivitesNodeList = cActivityElement.getElementsByTagName("ChapterActivityInteractiveActivity");
										ArrayList<InteractiveActivity> listOfChapterActivityDynamicActivities = new ArrayList<InteractiveActivity>();
										for (int m = 0; m < caDynamicActivitesNodeList.getLength(); m++) {
											Node caDynamicActivityNode = caDynamicActivitesNodeList.item(m);
											// CHAPTER -> ACTIVITY -> DYNAMIC ACTIVITY
											if (caDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
												Element caDynamicActivityElement = (Element) caDynamicActivityNode;
												String dynamicActivityID = caDynamicActivityElement.getAttribute("id");
												InteractiveActivity dynamicActivity = erbItemFinder.getInteractiveActivityById(app.getAvailableInteractiveActivities(), dynamicActivityID);
												if (dynamicActivity != null) listOfChapterActivityDynamicActivities.add(dynamicActivity);
											} // CHAPTER ACTIVITY DYNAMIC ACTIVITY ELEMENT
										} // CHAPTER ACTIVITY DYNAMIC ACTIVITY
										activity.setAssignedDynamicActivities(listOfChapterActivityDynamicActivities);
									} // CHAPTER ACTIVITY ELEMENT
								} // CHAPTER ACTIVITY
								NodeList cStepNodeList = chapterElement.getElementsByTagName("ChapterStep");
								ArrayList<Step> listOfChapterSteps = new ArrayList<Step>();
								for (int n = 0; n < cStepNodeList.getLength(); n++) {
									Node cStepNode = cStepNodeList.item(n);
									// CHAPTER -> STEP
									if (cStepNode.getNodeType() == Node.ELEMENT_NODE) {
										Element cStepElement = (Element) cStepNode;
										String stepID = cStepElement.getAttribute("id");
										Step step = erbItemFinder.getStepById(app.getAvailableSteps(), stepID);
										NodeList csDynamicActivitesNodeList = cStepElement.getElementsByTagName("ChapterStepInteractiveActivity");
										ArrayList<InteractiveActivity> listOfChapterStepDynamicActivities = new ArrayList<InteractiveActivity>();
										for (int o = 0; o < csDynamicActivitesNodeList.getLength(); o++) {
											Node csDynamicActivityNode = csDynamicActivitesNodeList.item(o);
											// CHAPTER -> STEP -> DYNAMIC ACTIVITY
											if (csDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
												Element csDynamicActivityElement = (Element) csDynamicActivityNode;
												String dynamicActivityID = csDynamicActivityElement.getAttribute("id");
												InteractiveActivity dynamicActivity = erbItemFinder.getInteractiveActivityById(app.getAvailableInteractiveActivities(), dynamicActivityID);
												if (dynamicActivity != null) listOfChapterStepDynamicActivities.add(dynamicActivity);
											} // CHAPTER STEP DYNAMIC ACTIVITY ELEMENT
										} // CHAPTER STEP DYNAMIC ACTIVITY
										step.setAssignedDynamicActivities(listOfChapterStepDynamicActivities);
										if (step != null) listOfChapterSteps.add(step);
									} // CHAPTER STEP ELEMENT
								} // CHAPTER STEP
								chapter.setAssignedSteps(listOfChapterSteps);
								chapter.setAssignedActivities(listOfChapterActivities);
								chapters.add(chapter);
							} // CHAPTER ELEMENT
						} // CHAPTER
						GoalCategory goalCategory = new GoalCategory(goalCategoryName, chapters);
						goalCategories.add(goalCategory);
					} // GOAL CATEGORY ELEMENT
				} // GOAL CATEGORY
				return goalCategories;
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	//Parse all projects
	public ArrayList<Project> parseAllProjects(File ERBProjectDirectory, ArrayList<Activity> activities){
		ArrayList<Project> projects = new ArrayList<Project>();
		if(ERBProjectDirectory != null && ERBProjectDirectory.exists()) {
			File [] projectDirectories = ERBProjectDirectory.listFiles();
			for(File projectDirectory : projectDirectories) {
				if(projectDirectory.isDirectory()) {
					File projectMetaFile = new File(projectDirectory.getPath() + "\\Meta.xml");
					if(projectMetaFile.exists()) {
						Project project = parseProjectXML(projectMetaFile, activities);
						projects.add(project);
					}
				}
			}
		}
		return projects;
	}
	
	//Parse Project
	public Project parseProjectXML(File xmlFile, ArrayList<Activity> activities){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList projectNodeList = doc.getElementsByTagName("project");
				for(int i = 0; i < projectNodeList.getLength(); i++) {
					Node projectNode = projectNodeList.item(i);
					//Project
					if(projectNode.getNodeType() == Node.ELEMENT_NODE) {
						Element projectElement = (Element) projectNode;
						String projectName = projectElement.getAttribute("projectName");
						String projectType = projectElement.getAttribute("projectType");
						String projectCleanedName = projectElement.getAttribute("projectCleanedName");
						ArrayList<Goal> listOfGoals = new ArrayList<Goal>();
						NodeList goalsNodeList = projectElement.getElementsByTagName("goal");
						for(int j =0; j < goalsNodeList.getLength(); j++) {
							Node goalNode = goalsNodeList.item(j);
							//Goal
							if(goalNode.getNodeType() == Node.ELEMENT_NODE) {
								Element goalElement = (Element) goalNode;
								String goalName = goalElement.getAttribute("goalName");
								String goalCleanedName = goalElement.getAttribute("goalCleanedName");
								ArrayList<GoalCategory> listOfSelectedGoalCategories = new ArrayList<GoalCategory>();
								String goalDescription = goalElement.getAttribute("goalDescription");
								NodeList goalCategoryNodeList = goalElement.getElementsByTagName("goalCategory");
								for (int k = 0; k < goalCategoryNodeList.getLength(); k++) {
									Node goalCategoryNode = goalCategoryNodeList.item(k);
									//Goal Category
									if(goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
										Element goalCategoryElement = (Element) goalCategoryNode;
										String categoryName = goalCategoryElement.getAttribute("categoryName");
										GoalCategory goalCategory = erbItemFinder.getGoalCategoryByName(app.getAvailableGoalCategories(), categoryName);
										listOfSelectedGoalCategories.add(goalCategory);
									}
								}
								Goal goal = new Goal(app, goalName, goalCleanedName, goalDescription, listOfSelectedGoalCategories);
								listOfGoals.add(goal);
							}
						}
						Project project = new Project(projectName, projectType, projectCleanedName, listOfGoals);
						for(Goal goal: listOfGoals) {
							goal.setChapters(activities, project);
						}
						return project;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;	
	}
	
	// Parse Goal
	public ArrayList<Chapter> parseGoalXML(File xmlFile, ArrayList<Activity> activities) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				ArrayList<Chapter> chapters = new ArrayList<Chapter>();
				NodeList chapterNodeList = doc.getElementsByTagName("Chapter");
				for (int i = 0; i < chapterNodeList.getLength(); i++) {
					Node chapterNode = chapterNodeList.item(i);
					// CHAPTER
					if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element chapterElement = (Element) chapterNode;
						String chapterId = chapterElement.getAttribute("id");
						String chapterGuid = chapterElement.getAttribute("guid");
						String chapterLongName = chapterElement.getAttribute("longName");
						String chapterShortName = chapterElement.getAttribute("shortName");
						String chapterStatus= chapterElement.getAttribute("status");
						String chapterNotes = chapterElement.getAttribute("notes");
						String chapterDescription = chapterElement.getAttribute("description");
						int chapterNumber = Integer.parseInt(chapterElement.getAttribute("number"));
						Chapter chapter = new Chapter(chapterId, chapterGuid, chapterLongName, chapterShortName, chapterStatus, chapterNotes, chapterDescription, chapterNumber);
						NodeList cActivityNodeList = chapterElement.getElementsByTagName("ChapterActivity");
						ArrayList<Activity> listOfChapterActivities = new ArrayList<Activity>();
						for (int j = 0; j < cActivityNodeList.getLength(); j++) {
							Node cActivityNode = cActivityNodeList.item(j);
							// CHAPTER -> ACTIVITY
							if (cActivityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element cActivityElement = (Element) cActivityNode;
								String activityId = cActivityElement.getAttribute("id");
								String activityGuid = cActivityElement.getAttribute("guid");
								String activityLongName = cActivityElement.getAttribute("longName");
								String activityShortName = cActivityElement.getAttribute("shortName");
								String activityStatus = cActivityElement.getAttribute("status");
								String activityNotes = cActivityElement.getAttribute("notes");
								String activityRating = cActivityElement.getAttribute("rating");
								Activity genericActivity = erbItemFinder.getActivityById(app.getAvailableActivities(), activityId);
								if (genericActivity != null) {
									Activity activity = new Activity(activityId, activityGuid, activityLongName, activityShortName, activityStatus, activityNotes, activityRating);
									listOfChapterActivities.add(activity);
									NodeList caStepsNodeList = cActivityElement.getElementsByTagName("ChapterActivityStep");
									ArrayList<Step> listOfChapterActivitySteps = new ArrayList<Step>();
									for (int k = 0; k < caStepsNodeList.getLength(); k++) {
										Node caStepNode = caStepsNodeList.item(k);
										// CHAPTER -> ACTIVITY -> STEP
										if (caStepNode.getNodeType() == Node.ELEMENT_NODE) {
											Element caStepElement = (Element) caStepNode;
											String stepId = caStepElement.getAttribute("id");
											String stepGuid = caStepElement.getAttribute("guid");
											String stepLongName = caStepElement.getAttribute("longName");
											String stepShortName = caStepElement.getAttribute("shortName");
											String stepStatus = caStepElement.getAttribute("status");
											String stepNotes = caStepElement.getAttribute("notes");
											String stepRating = caStepElement.getAttribute("rating");
											String stepType = caStepElement.getAttribute("type");
											Step genericStep = erbItemFinder.getStepById(app.getAvailableSteps(), stepId);
											if (genericStep != null) {
												Step step = new Step(stepId, stepGuid, stepLongName, stepShortName, stepStatus, stepNotes, stepRating, stepType);
												listOfChapterActivitySteps.add(step);
												NodeList casDynamicActivitiesNodeList = caStepElement.getElementsByTagName("ChapterActivityStepDynamicActivity");
												ArrayList<InteractiveActivity> listOfChapterActivityStepDynamicActivities = new ArrayList<InteractiveActivity>();
												for (int l = 0; l < casDynamicActivitiesNodeList.getLength(); l++) {
													Node casDynamicActivityNode = casDynamicActivitiesNodeList.item(l);
													// CHAPTER -> ACTIVITY -> STEP -> DYNAMIC ACTIVITY
													if (casDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
														Element casDynamicActivityElement = (Element) casDynamicActivityNode;
														String interactiveActivityId = casDynamicActivityElement.getAttribute("id");
														String interactiveActivityGuid = casDynamicActivityElement.getAttribute("guid");
														String interactiveActivityLongName = casDynamicActivityElement.getAttribute("longName");
														String interactiveActivityShortName = casDynamicActivityElement.getAttribute("shortName");
														String interactiveActivityStatus = casDynamicActivityElement.getAttribute("status");
														InteractiveActivity genericInteractiveActivity = erbItemFinder.getInteractiveActivityById(app.getAvailableInteractiveActivities(), interactiveActivityId);
														if (genericInteractiveActivity != null) {
															InteractiveActivity interactiveActivity = new InteractiveActivity(interactiveActivityId, interactiveActivityGuid, interactiveActivityLongName, interactiveActivityShortName, interactiveActivityStatus);
															listOfChapterActivityStepDynamicActivities.add(interactiveActivity);
														} // CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY NULL
													} // CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY ELEMENT
												} // CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY
												step.setAssignedDynamicActivities(listOfChapterActivityStepDynamicActivities);
											} // CHAPTER ACTIVITY STEP NULL
										} // CHAPTER ACTIVITY STEP ELEMENT
									} // CHAPTER ACTIVITY STEP
									NodeList caDynamicActivitesNodeList = cActivityElement.getElementsByTagName("ChapterActivityDynamicActivity");
									ArrayList<InteractiveActivity> listOfChapterActivityDynamicActivities = new ArrayList<InteractiveActivity>();
									for (int m = 0; m < caDynamicActivitesNodeList.getLength(); m++) {
										Node caDynamicActivityNode = caDynamicActivitesNodeList.item(m);
										// CHAPTER -> ACTIVITY -> DYNAMIC ACTIVITY
										if (caDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
											Element caDynamicActivityElement = (Element) caDynamicActivityNode;
											String interactiveActivityID = caDynamicActivityElement.getAttribute("id");
											String interactiveActivityGuid = caDynamicActivityElement.getAttribute("guid");
											String interactiveActivityLongName = caDynamicActivityElement.getAttribute("longName");
											String interactiveActivityShortName = caDynamicActivityElement.getAttribute("shortName");
											String interactiveActivityStatus = caDynamicActivityElement.getAttribute("status");
											InteractiveActivity genericInteractiveActivity = erbItemFinder.getInteractiveActivityById(app.getAvailableInteractiveActivities(), interactiveActivityID);
											if (genericInteractiveActivity != null) {
												InteractiveActivity interactiveActivity = new InteractiveActivity(interactiveActivityID, interactiveActivityGuid, interactiveActivityLongName, interactiveActivityShortName, interactiveActivityStatus);
												listOfChapterActivityDynamicActivities.add(interactiveActivity);
											} // CHAPTER ACTIVITY DYNAMIC ACTIVITY NULL
										} // CHAPTER ACTIVITY DYNAMIC ACTIVITY ELEMENT
									} // CHAPTER ACTIVITY DYNAMIC ACTIVITY
									activity.setAssignedDynamicActivities(listOfChapterActivityDynamicActivities);
									activity.setAssignedSteps(listOfChapterActivitySteps);
								} // CHAPTER ACTIVITY NULL
							} // CHAPTER ACTIVITY ELEMENT
						} // CHAPTER ACTIVITY
						NodeList cStepNodeList = chapterElement.getElementsByTagName("ChapterStep");
						ArrayList<Step> listOfChapterSteps = new ArrayList<Step>();
						for (int n = 0; n < cStepNodeList.getLength(); n++) {
							Node cStepNode = cStepNodeList.item(n);
							// CHAPTER -> STEP
							if (cStepNode.getNodeType() == Node.ELEMENT_NODE) {
								Element cStepElement = (Element) cStepNode;
								String stepId = cStepElement.getAttribute("id");
								String stepGuid = cStepElement.getAttribute("guid");
								String stepLongName = cStepElement.getAttribute("longName");
								String stepShortName = cStepElement.getAttribute("shortName");
								String stepStatus = cStepElement.getAttribute("status");
								String stepNotes = cStepElement.getAttribute("notes");
								String stepRating = cStepElement.getAttribute("rating");
								String stepType = cStepElement.getAttribute("type");
								Step genericStep = erbItemFinder.getStepById(app.getAvailableSteps(), stepId);
								if (genericStep != null) {
									Step step = new Step(stepId, stepGuid, stepLongName, stepShortName, stepStatus, stepNotes, stepRating, stepType);
									listOfChapterSteps.add(step);
									NodeList csDynamicActivitesNodeList = cStepElement.getElementsByTagName("ChapterStepDynamicActivity");
									ArrayList<InteractiveActivity> listOfChapterStepDynamicActivities = new ArrayList<InteractiveActivity>();
									for (int o = 0; o < csDynamicActivitesNodeList.getLength(); o++) {
										Node csDynamicActivityNode = csDynamicActivitesNodeList.item(o);
										// CHAPTER -> STEP -> DYNAMIC ACTIVITY
										if (csDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
											Element csDynamicActivityElement = (Element) csDynamicActivityNode;
											String interactiveActivityId = csDynamicActivityElement.getAttribute("id");
											String interactiveActivityGuid = csDynamicActivityElement.getAttribute("guid");
											String interactiveActivityLongName = csDynamicActivityElement.getAttribute("longName");
											String interactiveActivityShortName = csDynamicActivityElement.getAttribute("shortName");
											String interactiveActivityStatus = csDynamicActivityElement.getAttribute("status");
											InteractiveActivity genericInteractiveActivity = erbItemFinder.getInteractiveActivityById(app.getAvailableInteractiveActivities(), interactiveActivityId);
											if (genericInteractiveActivity != null) {
												InteractiveActivity interactiveActivity = new InteractiveActivity(interactiveActivityId, interactiveActivityGuid, interactiveActivityLongName, interactiveActivityShortName, interactiveActivityStatus);
												listOfChapterStepDynamicActivities.add(interactiveActivity);
											} // CHAPTER STEP DYNAMIC ACTIVITY NULL
										} // CHAPTER STEP DYNAMIC ACTIVITY ELEMENT
									} // CHAPTER STEP DYNAMIC ACTIVITY
									step.setAssignedDynamicActivities(listOfChapterStepDynamicActivities);
								} // CHAPTER STEP NULL
							} // CHAPTER STEP ELEMENT
						} // CHAPTER STEP
						chapter.setAssignedSteps(listOfChapterSteps);
						chapter.setAssignedActivities(listOfChapterActivities);
						chapters.add(chapter);
					}
				}
				return chapters;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	public void writeProjectMetaXML(File xmlFile, Project project) {
		if (xmlFile != null && project != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("project");
				document.appendChild(rootElement);
				rootElement.setAttribute("projectName", project.getProjectName());
				rootElement.setAttribute("projectType", project.getProjectType());
				rootElement.setAttribute("projectCleanedName", project.getProjectCleanedName());
				Element goalsElement = document.createElement("goals");
				for (Goal goal : project.getProjectGoals()) {
					Element goalElement = document.createElement("goal");
					goalElement.setAttribute("goalName", goal.getGoalName());
					goalElement.setAttribute("goalCleanedName", goal.getGoalCleanedName());
					goalElement.setAttribute("goalDescription", goal.getGoalDescription());
					Element goalsCategoryElement = document.createElement("goalsCategories");
					for (GoalCategory goalCategory : goal.getListOfSelectedGoalCategories()) {
						Element goalCategoryElement = document.createElement("goalCategory");
						goalCategoryElement.setAttribute("categoryName", goalCategory.getCategoryName());
						goalsCategoryElement.appendChild(goalCategoryElement);
					}
					goalElement.appendChild(goalsCategoryElement);
					goalsElement.appendChild(goalElement);
				}
				rootElement.appendChild(goalsElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeProjectMetaXML. xmlFile or project is null.");
		}
	}
	
	public void writeGoalMetaXML(File xmlFile, ArrayList<Chapter> chapters) {
		if (xmlFile != null && chapters != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("chapters");
				document.appendChild(rootElement);
				// CHAPTER
				for (Chapter chapter : chapters) {
					Element chapterElement = document.createElement("Chapter");
					chapterElement.setAttribute("id", chapter.getId());
					chapterElement.setAttribute("guid", chapter.getGuid());
					chapterElement.setAttribute("longName", chapter.getLongName());
					chapterElement.setAttribute("shortName", chapter.getShortName());
					chapterElement.setAttribute("status", chapter.getStatus());
					chapterElement.setAttribute("notes", chapter.getNotes());
					chapterElement.setAttribute("description", chapter.getDescription());
					chapterElement.setAttribute("number", String.valueOf(chapter.getNumber()));
					Element assignedActivitiesElement = document.createElement("assignedActivities");
					// CHAPTER -> ACTIVITY
					for (Activity activity : chapter.getAssignedActivities()) {
						Element assignedActivityElement = document.createElement("ChapterActivity");
						assignedActivityElement.setAttribute("id", activity.getId());
						assignedActivityElement.setAttribute("guid", activity.getGuid());
						assignedActivityElement.setAttribute("longName", activity.getLongName());
						assignedActivityElement.setAttribute("shortName", activity.getShortName());
						assignedActivityElement.setAttribute("status", activity.getStatus());
						assignedActivityElement.setAttribute("notes", activity.getNotes());
						assignedActivityElement.setAttribute("rating", activity.getRating());
						assignedActivitiesElement.appendChild(assignedActivityElement);
						Element assignedStepsElement = document.createElement("assignedSteps");
						// CHAPTER -> ACTIVITY -> STEP
						for (Step step : activity.getAssignedSteps()) {
							Element assignedActivityStepElement = document.createElement("ChapterActivityStep");
							assignedActivityStepElement.setAttribute("id", step.getId());
							assignedActivityStepElement.setAttribute("guid", step.getGuid());
							assignedActivityStepElement.setAttribute("longName", step.getLongName());
							assignedActivityStepElement.setAttribute("shortName", step.getShortName());
							assignedActivityStepElement.setAttribute("status", step.getStatus());
							assignedActivityStepElement.setAttribute("notes", step.getNotes());
							assignedActivityStepElement.setAttribute("rating", step.getRating());
							assignedActivityStepElement.setAttribute("type", step.getType());
							assignedStepsElement.appendChild(assignedActivityStepElement);
							Element assignedDynamicActivitiesElement = document.createElement("assignedDynamicActivities");
							// CHAPTER -> ACTIVITY -> STEP -> DYNAMIC ACTIVITY
							for (InteractiveActivity dynamicActivity : step.getAssignedDynamicActivities()) {
								Element assignedActivityDynamicActivityElement = document.createElement("ChapterActivityStepDynamicActivity");
								assignedActivityDynamicActivityElement.setAttribute("id", dynamicActivity.getId());
								assignedActivityDynamicActivityElement.setAttribute("guid", dynamicActivity.getGuid());
								assignedActivityDynamicActivityElement.setAttribute("longName", dynamicActivity.getLongName());
								assignedActivityDynamicActivityElement.setAttribute("shortName", dynamicActivity.getShortName());
								assignedActivityDynamicActivityElement.setAttribute("status",dynamicActivity.getStatus());
								assignedDynamicActivitiesElement.appendChild(assignedActivityDynamicActivityElement);
							} // CHAPTER ACTIVITY STEP DYNAMIC ACTIVITY
							assignedActivityStepElement.appendChild(assignedDynamicActivitiesElement);
						} // CHAPTER ACTIVITY STEP
						assignedActivityElement.appendChild(assignedStepsElement);
						Element assignedDynamicActivitiesElement = document.createElement("assignedDynamicActivities");
						// CHAPTER -> ACTIVITY -> DYNAMIC ACTIVITY
						for (InteractiveActivity dynamicActivity : activity.getAssignedDynamicActivities()) {
							Element assignedActivityDynamicActivityElement = document.createElement("ChapterActivityDynamicActivity");
							assignedActivityDynamicActivityElement.setAttribute("id", dynamicActivity.getId());
							assignedActivityDynamicActivityElement.setAttribute("guid", dynamicActivity.getGuid());
							assignedActivityDynamicActivityElement.setAttribute("longName", dynamicActivity.getLongName());
							assignedActivityDynamicActivityElement.setAttribute("shortName", dynamicActivity.getShortName());
							assignedActivityDynamicActivityElement.setAttribute("status", dynamicActivity.getStatus());
							assignedDynamicActivitiesElement.appendChild(assignedActivityDynamicActivityElement);
						} // CHAPTER ACTIVITY DYNAMIC ACTIVITY
						assignedActivityElement.appendChild(assignedDynamicActivitiesElement);
					} // CHAPTER ACTIVITY
					Element assignedStepsElement = document.createElement("assignedSteps");
					// CHAPTER -> STEP
					for (Step step : chapter.getAssignedSteps()) {
						Element assignedStepElement = document.createElement("ChapterStep");
						assignedStepElement.setAttribute("id", step.getId());
						assignedStepElement.setAttribute("guid", step.getGuid());
						assignedStepElement.setAttribute("longName", step.getLongName());
						assignedStepElement.setAttribute("shortName", step.getShortName());
						assignedStepElement.setAttribute("status", step.getStatus());
						assignedStepElement.setAttribute("notes", step.getNotes());
						assignedStepElement.setAttribute("rating", step.getRating());
						assignedStepElement.setAttribute("type", step.getType());
						assignedStepsElement.appendChild(assignedStepElement);
						Element assignedDynamicActivitiesElement = document.createElement("assignedDynamicActivities");
						// CHAPTER -> STEP -> DYNAMIC ACTIVITY
						for (InteractiveActivity dynamicActivity : step.getAssignedDynamicActivities()) {
							Element assignedStepDynamicActivityElement = document.createElement("ChapterStepDynamicActivity");
							assignedStepDynamicActivityElement.setAttribute("id", dynamicActivity.getId());
							assignedStepDynamicActivityElement.setAttribute("guid", dynamicActivity.getGuid());
							assignedStepDynamicActivityElement.setAttribute("longName", dynamicActivity.getLongName());
							assignedStepDynamicActivityElement.setAttribute("shortName", dynamicActivity.getShortName());
							assignedStepDynamicActivityElement.setAttribute("status", dynamicActivity.getStatus());
							assignedDynamicActivitiesElement.appendChild(assignedStepDynamicActivityElement);
						} // CHAPTER STEP DYNAMIC ACTIVITY
						assignedStepElement.appendChild(assignedDynamicActivitiesElement);
					} // CHAPTER STEP
					chapterElement.appendChild(assignedActivitiesElement);
					chapterElement.appendChild(assignedStepsElement);
					rootElement.appendChild(chapterElement);
				} // CHAPTER
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);
				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeGoalMetaXML. xmlFile or chapters is null.");
		}
	}
	
	public void writeWordCloudDataXML(File xmlFile, ArrayList<WordCloudItem> wordCloudItems) {
		if (xmlFile != null && wordCloudItems != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("wordCloudItems");
				document.appendChild(rootElement);
				for (WordCloudItem wordCloudItem : wordCloudItems) {
					Element wordCloudElement = document.createElement("wordCloudItem");
					wordCloudElement.setAttribute("word", wordCloudItem.getPhrase());
					wordCloudElement.setAttribute("size", String.valueOf(wordCloudItem.getSize()));
					wordCloudElement.setAttribute("count", String.valueOf(wordCloudItem.getCount()));
					rootElement.appendChild(wordCloudElement);
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeWordCloudDataXML. xmlFile or categories is null.");
		}
	}
	
	public void writeNoteboardDataXML(File xmlFile, ArrayList<CategorySectionController> categories) {
		if (xmlFile != null && categories != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("categories");
				document.appendChild(rootElement);
				for (CategorySectionController category : categories) {
					Element categoryElement = document.createElement("category");
					categoryElement.setAttribute("name", category.getCategoryName());
					Element notesElement = document.createElement("notes");
					for (PostItNoteController postItNoteController : category.getListOfPostItNoteControllers()) {
						Element noteElement = document.createElement("note");
						noteElement.setAttribute("content", postItNoteController.getPostItNoteText());
						noteElement.setAttribute("color", postItNoteController.getPostItNoteColor());
						noteElement.setAttribute("likes", postItNoteController.getNumberLabel().getText());
						noteElement.setAttribute("position",
								String.valueOf(postItNoteController.getPostItNoteIndex(category)));
						notesElement.appendChild(noteElement);
					}
					categoryElement.appendChild(notesElement);
					rootElement.appendChild(categoryElement);
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeNoteboardDataXML. xmlFile or categories is null.");
		}
	}
	
	public ArrayList<WordCloudItem> parseWordCloudDataXML(File xmlFile){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				
				NodeList wordCloudItemNodeList = doc.getElementsByTagName("wordCloudItem");
				ArrayList<WordCloudItem> wordCloudItems = new ArrayList<WordCloudItem>();
				for(int i = 0; i < wordCloudItemNodeList.getLength(); i++) {
					Node wordCloudItemNode = wordCloudItemNodeList.item(i);
					//Category
					if(wordCloudItemNode.getNodeType() == Node.ELEMENT_NODE) {
						Element wordCloudItemElement = (Element) wordCloudItemNode;
						String word = wordCloudItemElement.getAttribute("word");
						int size = Integer.parseInt(wordCloudItemElement.getAttribute("size"));
						int count = Integer.parseInt(wordCloudItemElement.getAttribute("count"));
						
						WordCloudItem wordCloudItem = new WordCloudItem(false, word, null, null, count, size);
						wordCloudItems.add(wordCloudItem);
					}
				}
				return wordCloudItems;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	public ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> parseNoteboardDataXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				
				NodeList categoryNodeList = doc.getElementsByTagName("category");
				ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> listOfCategoryHashMaps = new ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>>();
				for(int i = 0; i < categoryNodeList.getLength(); i++) {
					Node categoryNode = categoryNodeList.item(i);
					//Category
					if(categoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element categoryElement = (Element) categoryNode;
						String categoryName = categoryElement.getAttribute("name");
						HashMap<String, ArrayList<HashMap<String, String>>> categoryHashMaps = new HashMap<String, ArrayList<HashMap<String,String>>>();
						NodeList noteNodeList = categoryElement.getElementsByTagName("note");
						ArrayList<HashMap<String, String>> listOfNotesHashMaps = new ArrayList<HashMap<String,String>>();
						for(int j =0; j<noteNodeList.getLength(); j++) {
							Node noteNode = noteNodeList.item(j);
							//Note
							if(noteNode.getNodeType() == Node.ELEMENT_NODE) {
								HashMap<String, String> noteHashMap = new HashMap<String, String>();
								Element nodeElement = (Element) noteNode;
								String color = nodeElement.getAttribute("color");
								noteHashMap.put("color", color);
								String content = nodeElement.getAttribute("content");
								noteHashMap.put("content", content);
								String likes = nodeElement.getAttribute("likes");
								noteHashMap.put("likes", likes);
								String position = nodeElement.getAttribute("position");
								noteHashMap.put("position", position);
								listOfNotesHashMaps.add(noteHashMap);
							}
						}
						categoryHashMaps.put(categoryName, listOfNotesHashMaps);
						listOfCategoryHashMaps.add(categoryHashMaps);
					}
				}
				return listOfCategoryHashMaps;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	public HashMap<String, ArrayList<TextFlow>> parseMainFormContentXML(File xmlFile, MainFormController formContentController) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<String, ArrayList<TextFlow>> contentHashMap = new HashMap<String, ArrayList<TextFlow>>();

				NodeList containerNodeList = doc.getElementsByTagName("container");
				for (int i = 0; i < containerNodeList.getLength(); i++) {
					Node containerNode = containerNodeList.item(i);
					if (containerNode.getNodeType() == Node.ELEMENT_NODE) {
						// Container
						Element containerElement = (Element) containerNode;
						String containerId = containerElement.getAttribute("id");
						ArrayList<TextFlow> textBlocks = new ArrayList<TextFlow>();
						NodeList textBlockNodeList = containerElement.getElementsByTagName("textBlock");
						for (int j = 0; j < textBlockNodeList.getLength(); j++) {
							Node textBlockNode = textBlockNodeList.item(j);
							// TextBlock
							if (textBlockNode.getNodeType() == Node.ELEMENT_NODE) {
								TextFlow textFlow = new TextFlow();
								double maxTextLength = 0.0;
								Element textBlockElement = (Element) textBlockNode;
								NodeList textNodeList = textBlockElement.getElementsByTagName("text");
								for (int k = 0; k < textNodeList.getLength(); k++) {
									Node textNode = textNodeList.item(k);
									// Text
									if (textNode.getNodeType() == Node.ELEMENT_NODE) {
										Element textElement = (Element) textNode;
										double size = Double.parseDouble(textElement.getAttribute("size"));
										String fontFamily = textElement.getAttribute("fontFamily");
										String fontStyle = textElement.getAttribute("fontStyle");
										String text = textElement.getAttribute("text");
										Text t = new Text(text);
										if (fontStyle.contentEquals("Hyperlink")) {
											String linkType = textElement.getAttribute("linkType");
											String link = textElement.getAttribute("link");
											t.setStyle("-fx-fill:#4d90bc");
											t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
											t.setOnMouseEntered(e -> t.setUnderline(true));
											t.setOnMouseExited(e -> t.setUnderline(false));
											t.setOnMouseClicked(e-> formContentController.handleHyperlink(t, linkType, link));
										} else {
											if (fontStyle.contentEquals("Bold")) {
												t.setFont(Font.font(fontFamily, FontWeight.BOLD, size));
											} else if (fontStyle.contentEquals("Underlined")) {
												t.setUnderline(true);
												t.setFont(Font.font(fontFamily, size));
											} else {
												t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
											}
										}
										textFlow.getChildren().add(t);
										if(t.getText().length() > maxTextLength) maxTextLength = t.getText().length();
									}
								}
								textFlow.setPrefWidth(maxTextLength * 2);
								textBlocks.add(textFlow);
							}
						}
						contentHashMap.put(containerId, textBlocks);
					}
				}
				return contentHashMap;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}
	
	public HashMap<ArrayList<Text>, String> parseOutputFormContentXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<ArrayList<Text>, String> contentList = new LinkedHashMap<ArrayList<Text>, String>();

				NodeList containerNodeList = doc.getElementsByTagName("container");
				for (int i = 0; i < containerNodeList.getLength(); i++) {
					Node containerNode = containerNodeList.item(i);
					if (containerNode.getNodeType() == Node.ELEMENT_NODE) {
						// Container
						Element containerElement = (Element) containerNode;
						NodeList textBlockNodeList = containerElement.getElementsByTagName("textBlock");
						for (int j = 0; j < textBlockNodeList.getLength(); j++) {
							Node textBlockNode = textBlockNodeList.item(j);
							// TextBlock
							if (textBlockNode.getNodeType() == Node.ELEMENT_NODE) {
								Element textBlockElement = (Element) textBlockNode;
								String textBlockType = textBlockElement.getAttribute("type");
								ArrayList<Text> textList = new ArrayList<Text>();
								NodeList textNodeList = textBlockElement.getElementsByTagName("text");
								for (int k = 0; k < textNodeList.getLength(); k++) {
									Node textNode = textNodeList.item(k);
									// Text
									if (textNode.getNodeType() == Node.ELEMENT_NODE) {
										Element textElement = (Element) textNode;
										double size = Double.parseDouble(textElement.getAttribute("size"));
										String fontFamily = textElement.getAttribute("fontFamily");
										String fontStyle = textElement.getAttribute("fontStyle");
										String text = textElement.getAttribute("text");
										
										Text t = new Text(text);
										t.setFont(Font.font(fontFamily, size));
										t.setId(fontStyle);
										textList.add(t);
									}
								}
								contentList.put(textList, textBlockType);
							}
						}
					}
				}
				return contentList;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}
	
	public void writeOutputFormDataXML(File xmlFile, ArrayList<javafx.scene.Node> outputFormNodes) {
		if (xmlFile != null && outputFormNodes != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("textBlocks");
				document.appendChild(rootElement);
				Element containerElement = document.createElement("container");
				containerElement.setAttribute("id", "formVBox");
				for(javafx.scene.Node node: outputFormNodes) {
					Element textBlockElement = document.createElement("textBlock");
					textBlockElement.setAttribute("type", node.getId());
					
					if(node.toString().contains("TextFlow")) {
						TextFlow textFlow = (TextFlow) node;
						for(int i =0; i < textFlow.getChildren().size(); i++) {
							Text text = (Text) textFlow.getChildren().get(i);
							Element textElement = document.createElement("text");
							textElement.setAttribute("size", String.valueOf(text.getFont().getSize()));
							textElement.setAttribute("fontFamily", text.getFont().getFamily());
							textElement.setAttribute("fontStyle", text.getId());
							textElement.setAttribute("text", text.getText());
							textBlockElement.appendChild(textElement);
						}
					} else if (node.toString().contains("TextArea")) {
						TextArea textArea = (TextArea) node;
						Element textElement = document.createElement("text");
						textElement.setAttribute("size", "16.0");
						textElement.setAttribute("fontFamily", "System");
						if(textArea.getText() != null && textArea.getText().length() > 0) {
							textElement.setAttribute("text", textArea.getText());
							textElement.setAttribute("fontStyle", "Regular");
						} else {
							textElement.setAttribute("text", textArea.getPromptText());
							textElement.setAttribute("fontStyle", "Prompt");
						}
						textBlockElement.appendChild(textElement);
					}
					containerElement.appendChild(textBlockElement);
				}
				rootElement.appendChild(containerElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeOutputFormDataXML. xmlFile or outputFormNodes is null.");
		}
	}

}
