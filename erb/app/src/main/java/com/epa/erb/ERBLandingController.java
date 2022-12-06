package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.utility.MainPanelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ERBLandingController implements Initializable{

	@FXML
	ToggleGroup modeToggleGroup;
	@FXML
	RadioButton facilitatorModeRadioButton;
	@FXML
	RadioButton goalModeRadioButton;
	@FXML
	ImageView landingImageView1;
	@FXML
	VBox landingImageView1VBox;
	@FXML
	ImageView landingImageView2;
	@FXML
	VBox landingImageView2VBox;
	@FXML
	ImageView landingImageView3;
	@FXML
	VBox landingImageView3VBox;
	
	private App app;
	public ERBLandingController(App app) {
		this.app = app;
	}
	
	private String mode = "Facilitator Mode";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		landingImageView1VBox.widthProperty().addListener((obs, oldVal, newVal) -> {
			landingImageView1.setFitWidth(newVal.doubleValue());
		});
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
	
	private void handleControls() {
		modeToggleGroup.selectedToggleProperty().addListener((changed, oldVal, newVal) -> modeChanged(oldVal, newVal));
	}
	
	private void modeChanged(Toggle oldToggle, Toggle newToggle) {
		RadioButton newRadioButton = (RadioButton) newToggle;
		mode = newRadioButton.getText();
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
		if(isResource) {
			mainFormController.internalResourceLinkClicked(id);
		}else {
			mainFormController.internalIntroLinkClicked(id);

		}
	}
	
	@FXML
	public void projectButtonAction() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		Parent projectSelectionRoot = mainPanelHandler.loadProjectSelectionRoot(app, mode);
		String projectSelectionString = "Project Selection";
		app.getErbContainerController().getMyBreadCrumbBar().setMode(mode);
		app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(projectSelectionString, mainPanelHandler.getMainPanelIdHashMap().get(projectSelectionString));
		app.loadNodeToERBContainer(projectSelectionRoot);			
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
