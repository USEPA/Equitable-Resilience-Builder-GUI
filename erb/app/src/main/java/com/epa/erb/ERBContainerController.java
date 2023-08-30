package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.forms.AlternativeFormController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.IdAssignments;
import com.epa.erb.utility.MainPanelHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ERBContainerController implements Initializable{
	
	@FXML
	VBox erbVBox;
	@FXML
	VBox erbContainer;
	@FXML
	Label titleLabel;
	@FXML
	Menu faqMenu;
	@FXML
	Menu resourcesMenu;
	@FXML
	Menu aboutMenu;
	@FXML
	MenuBar menuBar;
	@FXML
	HBox breadCrumbHBox;
	@FXML
	ImageView erbMiniImageView;
	@FXML
	HBox headerHBox;
	@FXML
	HBox erbAboutHBox;
	@FXML
	Rectangle rectangle2;
	@FXML
	StackPane erbAboutStackPane;
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
	}
	
	private IdAssignments idAssignments = new IdAssignments();
	private MyBreadCrumbBar myBreadCrumbBar;
	private FileHandler fileHandler = new FileHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillImageViews();
		populateFAQMenu();
		populateAboutMenu();
		populateResourceMenu();
		populateBreadCrumbBar();
		
        rectangle2.widthProperty().bind(erbVBox.widthProperty().subtract(5.0));

		menuBar.getStylesheets().add(getClass().getResource("/menuBar.css").toString());
	}
	
	private void fillImageViews() {
//		ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/about_image_white.png")));
//		imageView1.setFitWidth(25.0);
//		imageView1.setFitHeight(25.0);
//		faqMenu.setGraphic(imageView1);
//		faqMenu.setStyle("-fx-background-color: linear-gradient(#16669A, #1268BE), "
//				+ "linear-gradient(#ffffff, #1268BE),"
//				+ "linear-gradient(#ffffff, #1268BE),"
//				+ "linear-gradient(#ffffff 0%, #1268BE 50%, #1268BE 100%),"
//				+ "linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));"
//				+ "-fx-background-radius: 30;"
//				+ "-fx-background-insets: 0,1,2,3,0;"
//				+ "-fx-text-fill: white;"
//				+ "-fx-font-weight: bold;"
//				+ "-fx-font-size: 14px;"
//				+ "}");
		
//		ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/resources_image_white.png")));
//		imageView2.setFitWidth(25.0);
//		imageView2.setFitHeight(25.0);
//		resourcesMenu.setGraphic(imageView2);
//		resourcesMenu.setStyle("-fx-background-color: linear-gradient(#16669A, #1268BE), "
//				+ "linear-gradient(#ffffff, #1268BE),"
//				+ "linear-gradient(#ffffff, #1268BE),"
//				+ "linear-gradient(#ffffff 0%, #1268BE 50%, #1268BE 100%),"
//				+ "linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));"
//				+ "-fx-background-radius: 30;"
//				+ "-fx-background-insets: 0,1,2,3,0;"
//				+ "-fx-text-fill: white;"
//				+ "-fx-font-weight: bold;"
//				+ "-fx-font-size: 14px;"
//				+ "}");

//		ImageView imageView3 = new ImageView(new Image(getClass().getResourceAsStream("/faq_image_white.png")));
//		imageView3.setFitWidth(25.0);
//		imageView3.setFitHeight(25.0);
//		aboutMenu.setGraphic(imageView3);
//		aboutMenu.setStyle("-fx-background-color: linear-gradient(#16669A, #1268BE), "
//				+ "linear-gradient(#ffffff, #1268BE),"
//				+ "linear-gradient(#ffffff, #1268BE),"
//				+ "linear-gradient(#ffffff 0%, #1268BE 50%, #1268BE 100%),"
//				+ "linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));"
//				+ "-fx-background-radius: 30;"
//				+ "-fx-background-insets: 0,1,2,3,0;"
//				+ "-fx-text-fill: white;"
//				+ "-fx-font-weight: bold;"
//				+ "-fx-font-size: 14px;"
//				+ "}");
		
		erbMiniImageView.setImage(new Image(getClass().getResourceAsStream("/bridge_90_90.png")));
	}
	
	private void populateResourceMenu() {
		for(ERBContentItem erbContentItem: app.getAvailableERBContentItems()) {
			if(idAssignments.getResourceIdAssignments().contains(erbContentItem.getId())){
				String name = erbContentItem.getLongName();
				MenuItem menuItem = createMenuItem(erbContentItem.getId(), name, true);
				resourcesMenu.getItems().add(menuItem);
			}
		}
	}
	
	
	private void populateFAQMenu() {
		for(ERBContentItem erbContentItem: app.getAvailableERBContentItems()) {
			if(idAssignments.getFAQIdAssignments().contains(erbContentItem.getId())){
				String name = erbContentItem.getLongName();
				MenuItem menuItem = createMenuItem(erbContentItem.getId(), name, true);
				faqMenu.getItems().add(menuItem);
			}
		}
	}
	
	private void populateAboutMenu() {
		for(ERBContentItem erbContentItem: app.getAvailableERBContentItems()) {
			if(idAssignments.getAboutIdAssignments().contains(erbContentItem.getId())){
				String name = erbContentItem.getLongName();
				MenuItem menuItem = createMenuItem(erbContentItem.getId(), name, true);
				aboutMenu.getItems().add(menuItem);
			}
		}
	}
	
	
	private void populateBreadCrumbBar() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		myBreadCrumbBar = new MyBreadCrumbBar(app);
		myBreadCrumbBar.getStylesheets().add(getClass().getResource("/breadCrumb.css").toString());
		myBreadCrumbBar.setStyle("-fx-padding: 3.5 0 0 0");
		String erbLandingString = "ERB Home";
		myBreadCrumbBar.initMyBreadCrumbBar(erbLandingString, mainPanelHandler.getMainPanelIdHashMap().get(erbLandingString));
		breadCrumbHBox.getChildren().add(myBreadCrumbBar);
	}

	private MenuItem createMenuItem(String id, String name, boolean isResource) {
		if (id != null && name != null) {
			MenuItem menuItem = new MenuItem(name);
			menuItem.setId(id);
			menuItem.setOnAction(e -> menuItemSelected(menuItem));
			return menuItem;
		} else {
			return null;
		}
	}
	
	private void menuItemSelected(MenuItem menuItem) {
		if (menuItem != null) {
			File formContentXMLFile = getFormContentXML(menuItem.getId());
			VBox root = loadMainFormContentController(formContentXMLFile);
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setWidth(app.getPrefWidth());
			stage.setHeight(app.getPrefHeight());
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
	
	public VBox loadAlternativeFormContentController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/AlternativeForm.fxml"));
			AlternativeFormController formContentController = new AlternativeFormController(app, xmlContentFileToParse, app.getEngagementActionController());
			fxmlLoader.setController(formContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public void removeHeaderHBox() {
		if(erbVBox.getChildren().contains(headerHBox)) {
			erbVBox.getChildren().remove(headerHBox);
		}
	}
	
	public void removeERBAboutHBox() {
		if(erbVBox.getChildren().contains(erbAboutStackPane)) {
			erbVBox.getChildren().remove(erbAboutStackPane);
			erbVBox.getChildren().remove(rectangle2);
		}
	}
	
	public void addHeaderHBox() {
		if(!erbVBox.getChildren().contains(headerHBox)) {
			erbVBox.getChildren().add(0,headerHBox);
		}
	}
	
	public void addERBAboutHBox() {
		if(!erbVBox.getChildren().contains(erbAboutStackPane)) {
			erbVBox.getChildren().add(1, erbAboutStackPane);
			erbVBox.getChildren().add(2, rectangle2);
		}
	}
	
	
	public File getFormContentXML(String id) {
		File xmlFile = fileHandler.getStaticFormContentXML(id);
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
