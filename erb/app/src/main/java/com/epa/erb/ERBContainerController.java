package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.form_activities.FormContentController;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class ERBContainerController implements Initializable{
	
	@FXML
	VBox welcomeVBox;
	@FXML
	VBox erbVBox;
	@FXML
	VBox erbContainer;
	@FXML
	Label titleLabel;
	@FXML
	Menu erbLandingMenu;
	@FXML
	Menu resourcesMenu;
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
	}
	
	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(ERBContainerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		handleResourceMenu();
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");		
	}
	
	private void handleResourceMenu() {
		XMLManager xmlManager = new XMLManager(app);
		HandleLandingMenu(xmlManager);
		File resourceXMLFile = fileHandler.getAvailableResourcesFileToParse();
		HashMap<String, String> resources = xmlManager.parseAvailableResourcesXML(resourceXMLFile);
		for(String idString: resources.keySet()) {
			String name = resources.get(idString);
			MenuItem menuItem = createMenuItem(idString, name, true);
			addMenuItem(menuItem, true);
		}
	}
	
	private void HandleLandingMenu(XMLManager xmlManager) {
		File introXMLFile = fileHandler.getAvailableIntroFileToParse();
		HashMap<String, String> introPanels = xmlManager.parseAvailableIntroXML(introXMLFile);
		for(String idString: introPanels.keySet()) {
			String name = introPanels.get(idString);
			MenuItem menuItem = createMenuItem(idString, name, false);
			addMenuItem(menuItem,false);
		}
	}
	
	private MenuItem createMenuItem(String id, String name, boolean isResource) {
		if (id != null && name != null) {
			MenuItem menuItem = new MenuItem(name);
			menuItem.setId(id);
			menuItem.setOnAction(e -> menuItemSelected(menuItem, isResource));
			return menuItem;
		} else {
			System.out.println("Cannot createMenuItem. id or name is null.");
			return null;
		}
	}
	
	private void addMenuItem(MenuItem menuItem, boolean isResource) {
		if(menuItem != null) {
			if(isResource) {
				resourcesMenu.getItems().add(menuItem);
			} else {
				erbLandingMenu.getItems().add(menuItem);
			}
		}
	}
	
	private void menuItemSelected(MenuItem menuItem, boolean isResource) {
		File formContentXMLFile = getFormContentXML(menuItem.getId(), isResource);
		Parent root = loadFormContentController(formContentXMLFile);
		app.loadNodeToERBContainer(root);
	}
	
	public VBox loadFormContentController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/form_activities/FormContent.fxml"));
			FormContentController formContentController = new FormContentController(app, xmlContentFileToParse, null);
			fxmlLoader.setController(formContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			return null;
		}
	}
	
	public File getFormContentXML(String id, boolean isResource) {
		File xmlFile;
		if (isResource) {
			xmlFile = fileHandler.getResourceFormContentXML(id);
		} else {
			xmlFile = fileHandler.getIntroFormContentXML(id);
		}
		return xmlFile;
	}
	
	@FXML
	public void aboutMenuItemAction() {
		app.initSaveHandler(null, null, null, app.getSelectedProject(), null, "projectChange");
		app.launchERBLandingNew2();
	}
	
	public void setTitleLabelText(String text) {
		titleLabel.setText(text);
	}
	
	public void removeHeaderPanel() {
		if(erbVBox.getChildren().contains(welcomeVBox)) {
			erbVBox.getChildren().remove(welcomeVBox);
		}
	}
	
	public void addHeaderPanel() {
		if(!erbVBox.getChildren().contains(welcomeVBox)) {
			erbVBox.getChildren().add(1, welcomeVBox);
		}
	}

	public VBox getWelcomeVBox() {
		return welcomeVBox;
	}

	public VBox getErbVBox() {
		return erbVBox;
	}

	public VBox getErbContainer() {
		return erbContainer;
	}

}
