package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.forms.FormController;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.MainPanelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ERBLandingController implements Initializable{

	private App app;
	public ERBLandingController(App app) {
		this.app = app;
	}
	
	@FXML
	VBox vBox;
	@FXML
	Rectangle rectangle2;
	@FXML
	StackPane bottomStackPane;
	@FXML
	Rectangle epaLogRectangle;
	@FXML
	Hyperlink exploreHyperlink;
	@FXML
	ImageView epaLogoImageView, erbMiniImageView;
	@FXML
	ImageView landingImageView1, landingImageView2, landingImageView3;
	@FXML
	VBox landingImageView1VBox, landingImageView2VBox, landingImageView3VBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		app.getErbContainerController().removeHeaderHBox();
		app.getErbContainerController().removeERBAboutHBox();
		
		addImageViewSizeListeners();
		
        rectangle2.widthProperty().bind(vBox.widthProperty());
        epaLogRectangle.widthProperty().bind(vBox.widthProperty().subtract(epaLogoImageView.getFitWidth() + 110));
	}
	
	private void addImageViewSizeListeners() {
		landingImageView1VBox.widthProperty().addListener((obs, oldVal, newVal) -> {
			landingImageView1.setFitWidth(newVal.doubleValue());
		});
		landingImageView1.setSmooth(false);
		landingImageView2VBox.widthProperty().addListener((obs, oldVal, newVal) -> {
			landingImageView2.setFitWidth(newVal.doubleValue());
		});
		landingImageView3VBox.widthProperty().addListener((obs, oldVal, newVal) -> {
			landingImageView3.setFitWidth(newVal.doubleValue());
		});

		landingImageView1VBox.heightProperty().addListener((obs, oldVal, newVal) -> {
			landingImageView1.setFitHeight(newVal.doubleValue());
		});
		landingImageView2VBox.heightProperty().addListener((obs, oldVal, newVal) -> {
			landingImageView2.setFitHeight(newVal.doubleValue());
		});
		landingImageView3VBox.heightProperty().addListener((obs, oldVal, newVal) -> {
			landingImageView3.setFitHeight(newVal.doubleValue());
		});
	}
		
	@FXML
	public void exampleProjectsHyperlinkAction() {
		FileHandler fileHandler = new FileHandler(app);
		File supportingDocDir = fileHandler.getTempDirectory();
		File fileToOpen = new File(supportingDocDir + File.separator + "Examples.pdf");
		fileHandler.openFileOnDesktop(fileToOpen);
	}
	
	@FXML
	public void buttonAction(ActionEvent actionEvent) {
		Button button = (Button) actionEvent.getSource();
		loadContent(button.getId());
	}
	
	@FXML
	public void hyperlinkAction(ActionEvent actionEvent) {
		Hyperlink hyperlink = (Hyperlink) actionEvent.getSource();
		if(hyperlink.getId() !=null) {
			loadContent(hyperlink.getId());
		} else {
			FormController formController = new FormController(app,null,null);
			formController.showLinkNotAvailable();
		}
	}
	
	@FXML
	public void labelAction(MouseEvent mouseEvent) {
		Label label = (Label) mouseEvent.getSource();
		loadContent(label.getId());
	}
	
	@FXML
	public void exploreHyperlinkAction() {
		ProjectSelectionController projectSelectionController = new ProjectSelectionController(app);
		Project exploreProject = app.getExploreProject();
		app.getErbContainerController().addHeaderHBox();
		app.getErbContainerController().addERBAboutHBox();
		if(exploreProject != null) {
			projectSelectionController.loadProject(exploreProject);
		}
	}
	
	private void loadContent(String id) {
		File formContentXMLFile = app.getErbContainerController().getFormContentXML(id);
		MainFormController mainFormController = new MainFormController(app, formContentXMLFile, app.getEngagementActionController());
		mainFormController.internalPopupLinkClicked(id);
	}
	
	@FXML
	public void projectContinueButtonAction() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
		Parent projectSelectionRoot = mainPanelHandler.loadProjectSelectionRoot(app);
		String projectSelectionString = "Project Selection";
		app.updateAvailableProjectsList();
		app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(projectSelectionString, mainPanelHandler.getMainPanelIdHashMap().get(projectSelectionString));
		app.addNodeToERBContainer(projectSelectionRoot);	
		app.getErbContainerController().addHeaderHBox();
		app.getErbContainerController().addERBAboutHBox();
	}
	
	@FXML
	public void projectCreationButtonAction() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
		Parent projectCreationRoot = mainPanelHandler.loadProjectCreationRoot(app);
		String projectCreationString = "Project Creation";
		app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(projectCreationString, mainPanelHandler.getMainPanelIdHashMap().get(projectCreationString));
		app.addNodeToERBContainer(projectCreationRoot);	
		app.getErbContainerController().addHeaderHBox();
		app.getErbContainerController().addERBAboutHBox();
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
	
}
