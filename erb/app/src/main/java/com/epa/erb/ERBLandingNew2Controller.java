package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.project.ProjectSelectionController;
import com.epa.erb.utility.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ERBLandingNew2Controller implements Initializable{

	@FXML
	HBox welcomeHBox;
	@FXML
	VBox arrowVBox;
	@FXML
	ToggleGroup modeToggleGroup;
	@FXML
	RadioButton facilitatorModeRadioButton;
	@FXML
	RadioButton goalModeRadioButton;
	
	private App app;
	public ERBLandingNew2Controller(App app) {
		this.app = app;
	}
	
	private String mode = "Facilitator Mode";
	private Constants constants = new Constants();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		loadArrowDiagram();
	}
	
	private void handleControls() {
		app.getErbContainerController().removeHeaderPanel();
		welcomeHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");		
		modeToggleGroup.selectedToggleProperty().addListener((changed, oldVal, newVal) -> modeChanged(oldVal, newVal));
	}
	
	private void modeChanged(Toggle oldToggle, Toggle newToggle) {
		RadioButton newRadioButton = (RadioButton) newToggle;
		mode = newRadioButton.getText();
	}
	
	@FXML
	public void equitableResilienceHyperlinkClicked(ActionEvent actionEvent) {
		Hyperlink hyperlink = (Hyperlink) actionEvent.getSource();
		loadContent(hyperlink.getId(), true);
	}
	
	@FXML
	public void buttonAction(ActionEvent actionEvent) {
		Button button = (Button) actionEvent.getSource();
		loadContent(button.getId(), false);
	}
	
	@FXML
	public void hyperlinkAction(ActionEvent actionEvent) {
		Hyperlink hyperlink = (Hyperlink) actionEvent.getSource();
		loadContent(hyperlink.getId(), false);
	}
	
	@FXML
	public void labelAction(MouseEvent mouseEvent) {
		Label label = (Label) mouseEvent.getSource();
		loadContent(label.getId(), false);
	}
	
	private void loadContent(String id, boolean isResource) {
		File formContentXMLFile = app.getErbContainerController().getFormContentXML(id, isResource);
		MainFormController mainFormController = new MainFormController(app, formContentXMLFile, app.getEngagementActionController());
		mainFormController.internalIntroLinkClicked(id);
	}
	
	private void loadArrowDiagram() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ArrowDiagram.fxml"));
			ArrowDiagramController arrowDiagramController = new ArrowDiagramController();
			fxmlLoader.setController(arrowDiagramController);
			Parent arrowDiagramRoot = fxmlLoader.load();
			arrowVBox.getChildren().add(arrowDiagramRoot);
		} catch (Exception e) {
			
		}
	}
	
	@FXML
	public void projectButtonAction() {
		Parent projectSelectionRoot = loadProjectSelectionToContainer();
		app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb("Project Selection");
		app.loadNodeToERBContainer(projectSelectionRoot);			
	}
	
	private Parent loadProjectSelectionToContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectSelection.fxml"));
			ProjectSelectionController projectSelectionController = new ProjectSelectionController(app, false, mode);
			fxmlLoader.setController(projectSelectionController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			return root;
		} catch (Exception e) {
			return null;
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
