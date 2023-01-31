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
import com.epa.erb.App;
import com.epa.erb.ContentPanel;
import com.epa.erb.ERBItemFinder;
import com.epa.erb.ResourcePanel;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.noteboard.CategorySectionController;
import com.epa.erb.noteboard.PostItNoteController;
import com.epa.erb.project.Project;
import com.epa.erb.wordcloud.WordCloudItem;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
	
	public ArrayList<ContentPanel> parseAvailableContentXML(File xmlFile){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<ContentPanel> availableContentPanels = new ArrayList<ContentPanel>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList panelNodeList = doc.getElementsByTagName("panel");
				for (int i = 0; i < panelNodeList.getLength(); i++) {
					Node panelNode = panelNodeList.item(i);
					// Activity
					if (panelNode.getNodeType() == Node.ELEMENT_NODE) {
						Element panelElement = (Element) panelNode;
						String id = panelElement.getAttribute("id");
						String longName = panelElement.getAttribute("longName");
						String shortName = panelElement.getAttribute("shortName");
						String status = panelElement.getAttribute("status");
						String type = panelElement.getAttribute("type");
						ContentPanel contentPanel = new ContentPanel(id, null, longName, shortName, status, type);
						availableContentPanels.add(contentPanel);
					}
				}
				return availableContentPanels;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	public ArrayList<ResourcePanel> parseAvailableResourcesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<ResourcePanel> availableResources = new ArrayList<ResourcePanel>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList resourceNodeList = doc.getElementsByTagName("panel");
				for (int i = 0; i < resourceNodeList.getLength(); i++) {
					Node resourceNode = resourceNodeList.item(i);
					if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
						Element resourceElement = (Element) resourceNode;
						String id = resourceElement.getAttribute("id");
						String name = resourceElement.getAttribute("longName");
						String type = resourceElement.getAttribute("type");
						ResourcePanel resourcePanel = new ResourcePanel(id,name,type);
						availableResources.add(resourcePanel);
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
					if (goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element goalCategoryElement = (Element) goalCategoryNode;
						String goalCategoryName = goalCategoryElement.getAttribute("categoryName");
						ArrayList<Chapter> chapters = new ArrayList<Chapter>();
						NodeList chapterNodeList = goalCategoryElement.getElementsByTagName("chapter");
						for (int i = 0; i < chapterNodeList.getLength(); i++) {
							Node chapterNode = chapterNodeList.item(i);
							if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
								Element chapterElement = (Element) chapterNode;
								String chapterID = chapterElement.getAttribute("id");
								Chapter chapter = erbItemFinder.getChapterById(app.getAvailableChapters(), chapterID);
								parseXMLNode(chapterNode, null, chapter, null);
								chapters.add(chapter);
							}
						}
						GoalCategory goalCategory = new GoalCategory(goalCategoryName, chapters);
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

	private ContentPanel ctp = null;
	public void parseXMLNode(Node node, Node parent, Chapter chapter, ContentPanel contentPanelParam) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element nodeElement = (Element) node;
			if (node.hasChildNodes()) { //has kids
				if (node.getNodeName().contains("layer")) { //is not chapter
					if (parent.getNodeName().contains("layer")) { //parent is not null or chapter
						String id = nodeElement.getAttribute("id");
						ContentPanel layeredContentPanel = erbItemFinder.getContentPanelById(app.getAvailableContentPanels(), id);
						contentPanelParam.addNextLayerContentPanel(layeredContentPanel);
						ctp = layeredContentPanel;
					} else { //parent is chapter
						String id = nodeElement.getAttribute("id");
						ContentPanel newContentPanel  = erbItemFinder.getContentPanelById(app.getAvailableContentPanels(), id);
						chapter.addContentPanel(newContentPanel);
						ctp = newContentPanel;
					}
				}
				NodeList children = node.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					parseXMLNode(children.item(i), node, chapter, ctp);
				}
			} else { //no kids
				if (node.getNodeName().trim().length() > 0) {
					if (node.getNodeName().contains("layer")) {
						String id = nodeElement.getAttribute("id");
						ContentPanel contentPanel = erbItemFinder.getContentPanelById(app.getAvailableContentPanels(), id);
						chapter.addContentPanel(contentPanel);
					}
				}
			}
		}
	}
	
	public ArrayList<Project> parseAllProjects(File ERBProjectDirectory){
		ArrayList<Project> projects = new ArrayList<Project>();
		if(ERBProjectDirectory != null && ERBProjectDirectory.exists()) {
			File [] projectDirectories = ERBProjectDirectory.listFiles();
			for(File projectDirectory : projectDirectories) {
				if(projectDirectory.isDirectory()) {
					File projectMetaFile = new File(projectDirectory.getPath() + "\\Meta.xml");
					if(projectMetaFile.exists()) {
						Project project = parseProjectXML(projectMetaFile);
						projects.add(project);
					}
				}
			}
		}
		return projects;
	}
	
	public Project parseProjectXML(File xmlFile){
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
							goal.setChapters(project);
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
	
	ContentPanel parentContentPanel= null;
	public void parseGoalXMLNode(Node node, Node parent, Chapter chapter, ContentPanel contentPanel) {
		if(node.getNodeType() == Node.ELEMENT_NODE){
			Element nodeElement = (Element) node;
			if(node.getNodeName().contains("Chapter")) {
				if(node.hasChildNodes()) {
					NodeList childrenNodes = node.getChildNodes();
					for(int i =0; i < childrenNodes.getLength(); i++) {
						parseGoalXMLNode(childrenNodes.item(i), node, chapter, contentPanel);
					}
				}
			} else if (node.getNodeName().contains("contentPanel")) {
				String id = nodeElement.getAttribute("id");
				String guid = nodeElement.getAttribute("guid");
				String longName = nodeElement.getAttribute("longName");
				String shortName = nodeElement.getAttribute("shortName");
				String status = nodeElement.getAttribute("status");
				String type = nodeElement.getAttribute("type");
				System.out.println("Handling " + longName);
				if(node.hasChildNodes()) {
					ContentPanel newContentPanel = new ContentPanel(id, guid, longName, shortName, status, type);
					System.out.println("NODE = " + node.getNodeName() + " PARENT = " + parent.getNodeName() + " chapter = " + chapter + " contentPanel = " + contentPanel);
					if(parent.getNodeName().contains("Chapter")) {
						parentContentPanel = newContentPanel;
						System.out.println("Adding " + newContentPanel.getLongName() + " to " + chapter.getShortName());
						if (newContentPanel.getGuid() != null) chapter.addContentPanel(parentContentPanel);
					}else {
						parentContentPanel = newContentPanel;
						System.out.println("Adding " + newContentPanel.getLongName() + " to " + newContentPanel.getLongName());
						if (newContentPanel.getGuid() != null) parentContentPanel.addNextLayerContentPanel(newContentPanel);
					}
	
					NodeList childrenNodes = node.getChildNodes();
					for(int i =0; i < childrenNodes.getLength(); i++) {
						parseGoalXMLNode(childrenNodes.item(i), node, chapter, contentPanel);
					}
				} else {
					ContentPanel newContentPanel = new ContentPanel(id, guid, longName, shortName, status, type);
					if(parent.getNodeName().contains("Chapter")) {
						System.out.println("DOES NOT HAVE CHILDREN: Adding to " + chapter.getShortName());
						if (newContentPanel.getGuid() != null) chapter.addContentPanel(newContentPanel);
					}else {
						System.out.println("DOES NOT HAVE CHILDREN: Adding to " + contentPanel.getLongName());
						if (newContentPanel.getGuid() != null) parentContentPanel.addNextLayerContentPanel(newContentPanel);
					}
				}
			}
		}
	}
	
	// Parse Goal
	public ArrayList<Chapter> parseGoalXML(File xmlFile) {
		System.out.println("Looking for " + xmlFile.getPath());
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
						parseGoalXMLNode(chapterNode, null, chapter, null);
						chapters.add(chapter);
					}
				}
				System.out.println("Number of final chapters = " + chapters.size());
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
					writeXMLNode(chapter, null, null, document, chapterElement);
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
	
	Element ptp = null;
	public void writeXMLNode(Chapter chapter, ContentPanel contentPanel, Element parentElement, Document document, Element chapterElement) {
		if (chapter != null) { // is chapter
			for (ContentPanel chapterContentPanelChild : chapter.getContentPanels()) {
				writeXMLNode(null, chapterContentPanelChild, null, document, chapterElement); // TODO Parent is chapter
			}
		} else { // is not chapter
			if (contentPanel.getGuid() != null) {
				Element contentPanelElement = document.createElement("contentPanel");
				ptp = contentPanelElement;
				contentPanelElement.setAttribute("id", contentPanel.getId());
				contentPanelElement.setAttribute("guid", contentPanel.getGuid());
				contentPanelElement.setAttribute("longName", contentPanel.getLongName());
				contentPanelElement.setAttribute("shortName", contentPanel.getShortName());
				contentPanelElement.setAttribute("status", contentPanel.getStatus());
				contentPanelElement.setAttribute("type", contentPanel.getType());
				if (parentElement != null) {
					parentElement.appendChild(contentPanelElement);
				} else {
					chapterElement.appendChild(contentPanelElement);
				}
			}
			if (contentPanel.getNextLayerContentPanels().size() > 0) {// has children
				for (ContentPanel contentPanelChild : contentPanel.getNextLayerContentPanels()) {
					writeXMLNode(null, contentPanelChild, ptp , document, chapterElement);
				}
			}
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
				FileHandler fileHandler = new FileHandler();
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
								NodeList textBlockChildrenNodeList = textBlockElement.getChildNodes();
								for(int l=0; l< textBlockChildrenNodeList.getLength(); l++) {
									Node childNode = textBlockChildrenNodeList.item(l);
									if(childNode.getNodeType() == Node.ELEMENT_NODE) {
										String nodeName = childNode.getNodeName();
										if(nodeName.contentEquals("text")) {
												Node textNode = childNode;
												// Text
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
													HBox hBox = new HBox();
													hBox.setAlignment(Pos.CENTER_LEFT);
													hBox.getChildren().add(t);
													textFlow.getChildren().add(hBox);
													if(t.getText().length() > maxTextLength) maxTextLength = t.getText().length();
										} else if (nodeName.contentEquals("icon")) {
											Node iconNode = childNode;
											//Icon
											Element iconElement = (Element) iconNode;
											double size = Double.parseDouble(iconElement.getAttribute("size"));
											String id = iconElement.getAttribute("id");
											ImageView imageView = new ImageView();
											imageView.setImage(new Image(fileHandler.getStaticIconPNGFile(id).getPath()));
											imageView.setFitWidth(size);
											imageView.setFitHeight(size);
											textFlow.getChildren().add(imageView);
										}
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
