package erb;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PreviousProject {

	String projectName;	
	String pathToERBFileSystem;
	String pathToProjectDirectory;
	
	public PreviousProject(String projectName, String pathToERBFileSystem) {
		this.projectName = projectName;
		this.pathToERBFileSystem = pathToERBFileSystem;
		this.pathToProjectDirectory = pathToERBFileSystem + "\\User\\Sessions\\" + projectName;
	}
	
	String projectType;
	Document documentToParse;
	int currentWizardPanelIndex;
	
	public void parsePreviousProject() {
		initDOM("Project_Progress.xml");
		NodeList projectNodeList = documentToParse.getElementsByTagName("project");
		for (int i = 0; i < projectNodeList.getLength(); i++) {
			Node projectNode = projectNodeList.item(i);
			if (projectNode.getNodeType() == Node.ELEMENT_NODE && projectNode.hasChildNodes()) {
				Element projectElement = (Element) projectNode;
				setProjectName(projectElement.getAttribute("name"));
				setProjectType(projectElement.getAttribute("type"));
				NodeList chapterNodeList = projectNode.getChildNodes();
				for (int j = 0; j < chapterNodeList.getLength(); j++) {
					Node chapterNode = chapterNodeList.item(j);
					if (chapterNode.getNodeType() == Node.ELEMENT_NODE && chapterNode.hasChildNodes()) {
						NodeList stepNodeList = chapterNode.getChildNodes();
						int index = 0;
						for(int k =0; k < stepNodeList.getLength(); k++) {
							Node stepNode = stepNodeList.item(k);
							if(stepNode.getNodeType() == Node.ELEMENT_NODE) {
								Element stepElement = (Element) stepNode;
								if(stepElement.getAttribute("status").contentEquals("current")) {
									setCurrentWizardPanelIndex(index);
								}
								index++;
							}
						}
					}
				}
			}
		}
	}
		
	public void initDOM(String fileName) {
		try {
			File fileToParse = new File(pathToProjectDirectory + "\\Metadata\\" + fileName);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			documentToParse = documentBuilder.parse(fileToParse);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getCurrentWizardPanelIndex() {
		return currentWizardPanelIndex;
	}

	public void setCurrentWizardPanelIndex(int currentWizardPanelIndex) {
		this.currentWizardPanelIndex = currentWizardPanelIndex;
	}	
	
}
