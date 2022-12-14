package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.MainPanelHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ERBContainerController implements Initializable{
	
	@FXML
	VBox erbVBox;
	@FXML
	VBox erbContainer;
	@FXML
	Label titleLabel;
	@FXML
	Menu introMenu;
	@FXML
	Menu resourcesMenu;
	@FXML
	Menu aboutMenu;
	@FXML
	HBox breadCrumbHBox;
	@FXML
	ImageView icon;
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
	}
	
	private MyBreadCrumbBar myBreadCrumbBar;
	private FileHandler fileHandler = new FileHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		handleResourceMenu();
		handleBreadCrumbBar();
	}
	
	private void handleControls() {
		ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/about_image.jpg")));
		imageView1.setFitWidth(25.0);
		imageView1.setFitHeight(25.0);
		introMenu.setGraphic(imageView1);
		
		ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/resource_image.png")));
		imageView2.setFitWidth(25.0);
		imageView2.setFitHeight(25.0);
		resourcesMenu.setGraphic(imageView2);

		ImageView imageView3 = new ImageView(new Image(getClass().getResourceAsStream("/faq_image.png")));
		imageView3.setFitWidth(25.0);
		imageView3.setFitHeight(25.0);
		aboutMenu.setGraphic(imageView3);
		
		icon.setImage(new Image(getClass().getResourceAsStream("/landing_image1.PNG")));

	}
	
	private void handleResourceMenu() {
		XMLManager xmlManager = new XMLManager(app);
		HandleLandingMenu(xmlManager);
		File resourceXMLFile = fileHandler.getStaticAvailableResourcesXMLFile();
		HashMap<String, String> resources = xmlManager.parseAvailableResourcesXML(resourceXMLFile);
		for(String idString: resources.keySet()) {
			String name = resources.get(idString);
			MenuItem menuItem = createMenuItem(idString, name, true);
			addMenuItem(menuItem, true);
		}
	}
	
	private void handleBreadCrumbBar() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		myBreadCrumbBar = new MyBreadCrumbBar(app);
		myBreadCrumbBar.setStyle("-fx-padding: 3.5 0 0 0");
		String erbLandingString = "ERB Landing";
		myBreadCrumbBar.initMyBreadCrumbBar(erbLandingString, mainPanelHandler.getMainPanelIdHashMap().get(erbLandingString));
		breadCrumbHBox.getChildren().add(myBreadCrumbBar);
	}
	
	private void HandleLandingMenu(XMLManager xmlManager) {
		File introXMLFile = fileHandler.getStaticAvailableIntroXMLFile();
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
			return null;
		}
	}
	
	private void addMenuItem(MenuItem menuItem, boolean isResource) {
		if(menuItem != null) {
			if(isResource) {
				resourcesMenu.getItems().add(menuItem);
			} else {
				introMenu.getItems().add(menuItem);
			}
		}
	}
	
	private void menuItemSelected(MenuItem menuItem, boolean isResource) {
		if (menuItem != null) {
			File formContentXMLFile = getFormContentXML(menuItem.getId(), isResource);
			VBox root = loadMainFormContentController(formContentXMLFile);
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setWidth(1150.0);
			stage.setHeight(750.0);
			stage.setScene(scene);
			stage.setTitle(menuItem.getText());
			stage.showAndWait();
		}
	}
	
	public VBox loadMainFormContentController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/MainForm.fxml"));
			MainFormController formContentController = new MainFormController(app, xmlContentFileToParse, app.getEngagementActionController());

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
			xmlFile = fileHandler.getStaticResourceFormTextXML(id);
		} else {
			xmlFile = fileHandler.getStaticIntroFormText(id);
		}
		return xmlFile;
	}

	public VBox getErbVBox() {
		return erbVBox;
	}

	public VBox getErbContainer() {
		return erbContainer;
	}

	public MyBreadCrumbBar getMyBreadCrumbBar() {
		return myBreadCrumbBar;
	}

}
