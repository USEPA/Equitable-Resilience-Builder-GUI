package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.form_activities.FormContentController;
import com.epa.erb.intro_panels.IntroPanelLoader;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ERBContainerController implements Initializable{
	
	@FXML
	HBox backButtonHBox;
	@FXML
	Button backButton;
	@FXML
	VBox welcomeVBox;
	@FXML
	VBox erbVBox;
	@FXML
	VBox erbContainer;
	@FXML
	Label titleLabel;
	@FXML
	Menu resourcesMenu;
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
	}
	
	private IntroPanelLoader introPanelLoader;
	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(ERBContainerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		removeBackButton();
		handleResourceMenu();
		introPanelLoader = new IntroPanelLoader(app);
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");		
	}
	
	private void handleResourceMenu() {
		XMLManager xmlManager = new XMLManager(app);
		File resourceXMLFile = fileHandler.getAvailableResourcesFileToParse();
		HashMap<String, String> resources = xmlManager.parseAvailableResourcesXML(resourceXMLFile);
		for(String idString: resources.keySet()) {
			String name = resources.get(idString);
			MenuItem menuItem = createMenuItem(idString, name);
			addMenuItem(menuItem);
		}
	}
	
	private MenuItem createMenuItem(String id, String name) {
		if (id != null && name != null) {
			MenuItem menuItem = new MenuItem(name);
			menuItem.setId(id);
			menuItem.setOnAction(e -> resourceMenuItemSelected(menuItem));
			return menuItem;
		} else {
			System.out.println("Cannot createMenuItem. id or name is null.");
			return null;
		}
	}
	
	private void addMenuItem(MenuItem menuItem) {
		if(menuItem != null) {
			resourcesMenu.getItems().add(menuItem);
		}
	}
	
	private void resourceMenuItemSelected(MenuItem menuItem) {
		File resourceXML = fileHandler.getResourceFormContentXML(menuItem.getId());
		Parent root = loadFormContentController(resourceXML);
		app.loadNodeToERBContainer(root);
	}
	
	private VBox loadFormContentController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/form_activities/FormContent.fxml"));
			FormContentController formContentController = new FormContentController(app, xmlContentFileToParse, null);
			fxmlLoader.setController(formContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	@FXML
	public void aboutMenuItemAction() {
		app.initSaveHandler(null, null, null, app.getSelectedProject(), null, "projectChange");
		app.launchERBLandingNew2();
	}
	
	@FXML
	public void howERBMadeMenuItemAction() {
		introPanelLoader.loadHowERBMadePanel();
	}
	
	@FXML
	public void howDoesItWorkMenuItemAction() {
		introPanelLoader.loadHowDoesItWorkPanel();
	}
	
	@FXML
	public void whoIsItForMenuItemAction() {
		introPanelLoader.loadWhoAreERBUsersPanel();
	}
	
	@FXML
	public void howOthersAreUsingItMenuItemAction() {
		introPanelLoader.loadHowOthersAreUsingERBPanel();
	}
	
	@FXML
	public void whatMakesERBDifferentMenuItemAction() {
		introPanelLoader.loadWhatMakesERBDifferentPanel();
	}
	
	@FXML
	public void equityAndResilienceMenuItemAction() {
		introPanelLoader.loadEquityAndResiliencePanel();
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
	
	public void removeBackButton() {
		if(erbVBox.getChildren().contains(backButtonHBox)) {
			erbVBox.getChildren().remove(backButtonHBox);
		}
	}
	
	public void addBackButton() {
		if(!erbVBox.getChildren().contains(backButtonHBox)) {
			erbVBox.getChildren().add(1, backButtonHBox);
		}
	}
	
	public Button getBackButton() {
		return backButton;
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
