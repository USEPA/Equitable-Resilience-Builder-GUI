package com.epa.erb;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
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
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.GoalCategory;

public class XMLManager {

	public XMLManager() {

	}

	private Logger logger = LogManager.getLogger(XMLManager.class);
	
	public ArrayList<GoalCategory> parseGoalCategoriesXML(File xmlFile){
		if (xmlFile.exists() && xmlFile.canRead()) {
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
	
	public ArrayList<ActivityType> parseActivityTypesXML(File xmlFile) {
		if (xmlFile.exists() && xmlFile.canRead()) {
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
	
	public ArrayList<Activity> parseAvailableActivitiesXML(File xmlFile, ArrayList<ActivityType> activityTypes){
		if (xmlFile.exists() && xmlFile.canRead()) {
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
						Activity activity = new Activity(activityType, status, shortName, longName, fileName, directions, objectives, description, materials, time, who, activityID);
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
	
	private ActivityType getActivityType(String activityTypeName, ArrayList<ActivityType> activityTypes) {
		for(ActivityType activityType: activityTypes) {
			if(activityType.getLongName().contentEquals(activityTypeName)) {
				return activityType;
			}
		}
		logger.debug("ActivityType returned is null.");
		return null;
	}

	public ArrayList<Chapter> parseDataXML(File xmlFile) {
		if (xmlFile.exists() && xmlFile.canRead()) {
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
						int chapterNum = Integer.parseInt(chapterElement.getAttribute("chapterNum"));
						String chapterNumericName = chapterElement.getAttribute("numericName");
						String chapterStringName = chapterElement.getAttribute("stringName");
						String chapterDescription = chapterElement.getAttribute("description");
						double planStatus = Double.parseDouble(chapterElement.getAttribute("planStatus"));
						double engageStatus = Double.parseDouble(chapterElement.getAttribute("engageStatus"));
						double reflectStatus = Double.parseDouble(chapterElement.getAttribute("reflectStatus"));
						Chapter chapter = new Chapter(chapterNum, chapterNumericName, chapterStringName, chapterDescription, planStatus, engageStatus, reflectStatus);
						NodeList activityNodeList = chapterNode.getChildNodes();
						for (int j = 0; j < activityNodeList.getLength(); j++) {
							Node activityNode = activityNodeList.item(j);
							// Activity
							if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element activityElement = (Element) activityNode;
								String activityStatus = activityElement.getAttribute("status");
								String activityShortName = activityElement.getAttribute("shortName");
								String activityLongName = activityElement.getAttribute("longName");
								String activityFileName = activityElement.getAttribute("fileName");
								String activityDirections = activityElement.getAttribute("directions");
								String activityObjectives = activityElement.getAttribute("objectives");
								String activityDescription = activityElement.getAttribute("description");
								String activityMaterials = activityElement.getAttribute("materials");
								String activityTime = activityElement.getAttribute("time");
								String activityWho = activityElement.getAttribute("who");
								String activityID = activityElement.getAttribute("activityID");
								String activityGUID = activityElement.getAttribute("guid");

								// Create new activity
								Activity activity = new Activity();
								activity.setStatus(activityStatus);
								activity.setShortName(activityShortName);
								activity.setLongName(activityLongName);
								activity.setFileName(activityFileName);
								activity.setDirections(activityDirections);
								activity.setObjectives(activityObjectives);
								activity.setDescription(activityDescription);
								activity.setMaterials(activityMaterials);
								activity.setTime(activityTime);
								activity.setWho(activityWho);
								activity.setActivityID(activityID);
								activity.setGUID(activityGUID);

								// Add activity type element to activity
								NodeList activityTypeNodeList = activityElement.getElementsByTagName("activityType");
								for (int k = 0; k < activityTypeNodeList.getLength(); k++) {
									Node activityTypeNode = activityTypeNodeList.item(k);
									// ActivityType
									if (activityTypeNode.getNodeType() == Node.ELEMENT_NODE) {
										Element activityTypeElement = (Element) activityTypeNode;
										String activityTypeLongName = activityTypeElement.getAttribute("longName");
										String activityTypeShortName = activityTypeElement.getAttribute("shortName");
										String activityTypeDescription = activityTypeElement.getAttribute("description");
										String activityTypeFileExt = activityTypeElement.getAttribute("fileExt");
										ActivityType activityType = new ActivityType(activityTypeLongName, activityTypeShortName, activityTypeDescription, activityTypeFileExt);
										activity.setActivityType(activityType);
									}
								}
								chapter.addUserSelectedActivity(activity);
							}
						}
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

	public boolean writeDataXML(File xmlFile, ArrayList<Chapter> chapters) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("chapters");
			document.appendChild(rootElement);
			for (Chapter chapter : chapters) {
				Element chapterElement = document.createElement("chapter");
				chapterElement.setAttribute("chapterNum", Integer.toString(chapter.getChapterNum()));
				chapterElement.setAttribute("numericName", chapter.getNumericName());
				chapterElement.setAttribute("stringName", chapter.getStringName());
				chapterElement.setAttribute("description", chapter.getDescriptionName());
				chapterElement.setAttribute("planStatus", String.valueOf(chapter.getPlanStatus()));
				chapterElement.setAttribute("engageStatus", String.valueOf(chapter.getEngageStatus()));
				chapterElement.setAttribute("reflectStatus", String.valueOf(chapter.getReflectStatus()));

				for (Activity activity : chapter.getUserSelectedActivities()) {
					Element activityElement = document.createElement("activity");
					activityElement.setAttribute("status", activity.getStatus());
					activityElement.setAttribute("shortName", activity.getShortName());
					activityElement.setAttribute("longName", activity.getLongName());
					activityElement.setAttribute("fileName", activity.getFileName());
					activityElement.setAttribute("directions", activity.getDirections());
					activityElement.setAttribute("objectives", activity.getObjectives());
					activityElement.setAttribute("description", activity.getDescription());
					activityElement.setAttribute("materials", activity.getMaterials());
					activityElement.setAttribute("time", activity.getTime());
					activityElement.setAttribute("who", activity.getWho());
					activityElement.setAttribute("activityID", activity.getActivityID());
					if(activity.getGUID() == null || activity.getGUID().length() == 0) {
						activityElement.setAttribute("guid", generateGUID());
					} else {
						activityElement.setAttribute("guid", activity.getGUID());
					}

					Element activityTypeElement = document.createElement("activityType");
					activityTypeElement.setAttribute("longName", activity.getActivityType().getLongName());
					activityTypeElement.setAttribute("shortName", activity.getActivityType().getShortName());
					activityTypeElement.setAttribute("description", activity.getActivityType().getDescription());
					activityTypeElement.setAttribute("fileExt", activity.getActivityType().getFileExt());

					activityElement.appendChild(activityTypeElement);
					chapterElement.appendChild(activityElement);
				}
				rootElement.appendChild(chapterElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	private String generateGUID() {
		UUID uuid = UUID.randomUUID();
		return String.valueOf(uuid);
	}

}
