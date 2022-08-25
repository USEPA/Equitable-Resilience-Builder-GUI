package com.epa.erb.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.epa.erb.ActivityType;
import com.epa.erb.App;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.noteboard.CategorySectionController;
import com.epa.erb.noteboard.PostItNoteController;
import com.epa.erb.project.Project;

public class XMLManager {

	private App app;
	public XMLManager(App app) {
		this.app = app;
	}

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
						String chapterNum = chapterElement.getAttribute("chapterNum");
						String description = chapterElement.getAttribute("description");
						String notes = chapterElement.getAttribute("notes");
						String numericName = chapterElement.getAttribute("numericName");
						String stringName = chapterElement.getAttribute("stringName");
						Chapter chapter = new Chapter(Integer.parseInt(chapterNum), numericName, stringName, description, notes);
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
	
	//Parse ActivityTypes
	public ArrayList<ActivityType> parseActivityTypesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<ActivityType> activityTypes = new ArrayList<ActivityType>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList activityTypeNodeList = doc.getElementsByTagName("activityType");
				for (int i = 0; i < activityTypeNodeList.getLength(); i++) {
					Node activityTypeNode = activityTypeNodeList.item(i);
					// Activity Type
					if (activityTypeNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityTypeElement = (Element) activityTypeNode;
						String longName = activityTypeElement.getAttribute("longName");
						String shortName = activityTypeElement.getAttribute("shortName");
						String description = activityTypeElement.getAttribute("description");
						String fileExt = activityTypeElement.getAttribute("fileExt");
						ActivityType activityType = new ActivityType(longName, shortName, description, fileExt);
						activityTypes.add(activityType);
					}
				}
				return activityTypes;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	//Parse AvailableActivities
	public ArrayList<Activity> parseAvailableActivitiesXML(File xmlFile, ArrayList<ActivityType> activityTypes){
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
						String activityTypeName = activityElement.getAttribute("activityType");
						ActivityType activityType = getActivityType(activityTypeName, activityTypes);
						String status = activityElement.getAttribute("status");
						String chapterAssignment = activityElement.getAttribute("chapterAssignment");
						String shortName = activityElement.getAttribute("shortName");
						String longName = activityElement.getAttribute("longName");
						String fileName = activityElement.getAttribute("fileName");
						String directions = activityElement.getAttribute("directions");
						String objectives = activityElement.getAttribute("objectives");
						String description = activityElement.getAttribute("description");
						String materials = activityElement.getAttribute("materials");
						String time = activityElement.getAttribute("time");
						String who = activityElement.getAttribute("who");
						String activityID = activityElement.getAttribute("activityID");
						String notes = activityElement.getAttribute("notes");
						String rating = activityElement.getAttribute("rating");
						Activity activity = new Activity(activityType, chapterAssignment, status, shortName, longName, fileName, directions, objectives, description, materials, time, who, activityID, notes, rating);
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
	
	//Parse GoalCategories
	public ArrayList<GoalCategory> parseGoalCategoriesXML(File xmlFile){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<GoalCategory> goalCategories = new ArrayList<GoalCategory>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList goalCategoryNodeList = doc.getElementsByTagName("goalCategory");
				for (int i = 0; i < goalCategoryNodeList.getLength(); i++) {
					Node goalCategoryNode = goalCategoryNodeList.item(i);
					//Goal Category
					if(goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element goalCategoryElement = (Element) goalCategoryNode;
						String categoryName = goalCategoryElement.getAttribute("categoryName");
						NodeList assignedActivityNodeList = goalCategoryElement.getElementsByTagName("assignedActivity");
						ArrayList<String> listOfActivityIDs = new ArrayList<String>();
						for(int j =0; j < assignedActivityNodeList.getLength(); j++) {
							Node assignedActivityNode = assignedActivityNodeList.item(j);
							//Assigned Activity
							if(assignedActivityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element assignedActivityElement = (Element) assignedActivityNode;
								String activityID = assignedActivityElement.getAttribute("activityID");
								listOfActivityIDs.add(activityID);
							}
						}
						GoalCategory goalCategory = new GoalCategory(categoryName, listOfActivityIDs);
						goalCategories.add(goalCategory);
					}
				}
				return goalCategories;
			} catch (Exception e) {
				logger.error(e.getMessage());
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
										ArrayList<String> listOfActivityIDs = new ArrayList<String>();
										NodeList assignedActivityNodeList = goalCategoryElement.getElementsByTagName("assignedActivity");
										for(int l =0; l < assignedActivityNodeList.getLength(); l++) {
											Node assignedActivityNode = assignedActivityNodeList.item(l);
											//Assigned Activity
											if(assignedActivityNode.getNodeType() == Node.ELEMENT_NODE) {
												Element assignedActivityElement = (Element) assignedActivityNode;
												String activityID = assignedActivityElement.getAttribute("activityID");
												listOfActivityIDs.add(activityID);
											}
										}
										GoalCategory goalCategory = new GoalCategory(categoryName, listOfActivityIDs);
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
	
	//Parse Goal
	public ArrayList<Chapter> parseGoalXML(File xmlFile, ArrayList<Activity> activities){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				
				ArrayList<Chapter> chapters = new ArrayList<Chapter>();
				NodeList chapterNodeList = doc.getElementsByTagName("chapter");
				for(int i = 0; i < chapterNodeList.getLength(); i++) {
					Node chapterNode = chapterNodeList.item(i);
					//Chapter
					if(chapterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element chapterElement = (Element) chapterNode;
						int chapterNum = Integer.parseInt(chapterElement.getAttribute("chapterNum"));
						String numericName = chapterElement.getAttribute("numericName");
						String stringName = chapterElement.getAttribute("stringName");
						String description = chapterElement.getAttribute("description");
						String notes = chapterElement.getAttribute("notes");
						String pAudience = chapterElement.getAttribute("pAudience");
						String pGoals = chapterElement.getAttribute("pGoals");
						String pActivities = chapterElement.getAttribute("pActivities");
						String pNotes = chapterElement.getAttribute("pNotes");
						String rAudience = chapterElement.getAttribute("rAudience");
						String rGoals = chapterElement.getAttribute("rGoals");
						String rActivities = chapterElement.getAttribute("rActivities");
						String rNotes = chapterElement.getAttribute("rNotes");
						HashMap<String, String> facilitatorPlanHashMap = new HashMap<String, String>();
						facilitatorPlanHashMap.put("pAudience", pAudience);
						facilitatorPlanHashMap.put("pGoals", pGoals);
						facilitatorPlanHashMap.put("pActivities", pActivities);
						facilitatorPlanHashMap.put("pNotes", pNotes);
						HashMap<String, String> facilitatorReflectHashMap = new HashMap<String, String>();
						facilitatorReflectHashMap.put("rAudience", rAudience);
						facilitatorReflectHashMap.put("rGoals", rGoals);
						facilitatorReflectHashMap.put("rActivities", rActivities);
						facilitatorReflectHashMap.put("rNotes", rNotes);
						Chapter chapter = new Chapter(chapterNum, numericName, stringName, description, notes);
						NodeList assignedActivityNodeList = chapterElement.getElementsByTagName("assignedActivity");
						ArrayList<Activity> listOfChapterActivities = new ArrayList<Activity>();
						for(int j =0; j<assignedActivityNodeList.getLength(); j++) {
							Node assignedActivityNode = assignedActivityNodeList.item(j);
							//AssignedActivity
							if(assignedActivityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element assignedActivityElement = (Element) assignedActivityNode;
								String activityID = assignedActivityElement.getAttribute("activityID");
								String activityStatus = assignedActivityElement.getAttribute("status");
								String activityNotes = assignedActivityElement.getAttribute("notes");
								String activityRating = assignedActivityElement.getAttribute("rating");
								Activity activity = getActivity(activityID, activities);
								if(activity != null) { 									
									activity.setChapterAssignment(String.valueOf(chapter.getChapterNum()));
									activity.setStatus(activityStatus);
									activity.setNotes(activityNotes);
									activity.setRating(activityRating);
									listOfChapterActivities.add(activity);
								}
							}
						}
						chapter.setPlanHashMap(facilitatorPlanHashMap);
						chapter.setReflectHashMap(facilitatorReflectHashMap);
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
						Element assignedActivitesElement = document.createElement("assignedActivities");
						for (String activityID : goalCategory.getListOfAssignedActivityIDs()) {
							Element assignedActivityElement = document.createElement("assignedActivity");
							assignedActivityElement.setAttribute("activityID", activityID);
							assignedActivitesElement.appendChild(assignedActivityElement);
						}
						goalCategoryElement.appendChild(assignedActivitesElement);
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
				for (Chapter chapter : chapters) {
					Element chapterElement = document.createElement("chapter");
					chapterElement.setAttribute("chapterNum", String.valueOf(chapter.getChapterNum()));
					chapterElement.setAttribute("numericName", chapter.getNumericName());
					chapterElement.setAttribute("stringName", chapter.getStringName());
					chapterElement.setAttribute("description", chapter.getDescriptionName());
					chapterElement.setAttribute("notes", chapter.getNotes());
					for(String key: chapter.getPlanHashMap().keySet()) {
						chapterElement.setAttribute(key, chapter.getPlanHashMap().get(key));
					}
					for(String key: chapter.getReflectHashMap().keySet()) {
						chapterElement.setAttribute(key, chapter.getReflectHashMap().get(key));
					}
					Element assignedActivitiesElement = document.createElement("assignedActivities");
					for (Activity activity : chapter.getAssignedActivities()) {
						Element assignedActivityElement = document.createElement("assignedActivity");
						assignedActivityElement.setAttribute("activityID", activity.getActivityID());
						assignedActivityElement.setAttribute("status", activity.getStatus());
						assignedActivityElement.setAttribute("notes", activity.getNotes());
						assignedActivityElement.setAttribute("rating", activity.getRating());
						assignedActivitiesElement.appendChild(assignedActivityElement);
					}
					chapterElement.appendChild(assignedActivitiesElement);
					rootElement.appendChild(chapterElement);
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
			logger.error("Cannot writeGoalMetaXML. xmlFile or chapters is null.");
		}
	}
	
	public void writeActivityMetaXML(File xmlFile, Activity activity) {
		if (xmlFile != null && activity != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("activity");
				document.appendChild(rootElement);
				rootElement.setAttribute("activityID", activity.getActivityID());
				rootElement.setAttribute("fileName", activity.getFileName());
				for(String key: activity.getPlanHashMap().keySet()) {
					rootElement.setAttribute(key, activity.getPlanHashMap().get(key));
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
			logger.error("Cannot writeActivityMetaXML. xmlFile or activity is null.");
		}
	}
	
	public HashMap<String, String> parseActivityMetaXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<String, String> planHashMap = new HashMap<String, String>();
				NodeList activityNodeList = doc.getElementsByTagName("activity");
				for (int i = 0; i < activityNodeList.getLength(); i++) {
					Node activityNode = activityNodeList.item(i);
					// Activity
					if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityElement = (Element) activityNode;
						planHashMap.put("pAudience", activityElement.getAttribute("pAudience"));
						planHashMap.put("pGoals", activityElement.getAttribute("pGoals"));
						planHashMap.put("pNotes", activityElement.getAttribute("pNotes"));
						planHashMap.put("pActivities", activityElement.getAttribute("pActivities"));
					}
				}
				return planHashMap;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
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
	
	private Activity getActivity(String activityID, ArrayList<Activity> activities) {
		if (activityID != null && activities != null) {
			for (Activity activity : activities) {
				if (activity.getActivityID().contentEquals(activityID)) {
					return activity.cloneActivity();
				}
			}
			logger.debug("Activity returned is null.");
			return null;
		} else {
			logger.error("Cannot getActivity. activityID or activities is null.");
			return null;
		}
	}
	
	private ActivityType getActivityType(String activityTypeName, ArrayList<ActivityType> activityTypes) {
		if (activityTypeName != null && activityTypes != null) {
			for (ActivityType activityType : activityTypes) {
				if (activityType.getLongName().contentEquals(activityTypeName)) {
					return activityType;
				}
			}
			logger.debug("ActivityType returned is null.");
			return null;
		} else {
			logger.error("Cannot getActivityType. activityTypeName or activityTypes is null.");
			return null;
		}
	}

}