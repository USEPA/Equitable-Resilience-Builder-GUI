package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ERBContainerController implements Initializable{
	
	@FXML
	VBox welcomeVBox;
	@FXML
	VBox erbVBox;
	@FXML
	VBox erbContainer;
	@FXML
	Label titleLabel;
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBContainerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");		
	}
	
	@FXML
	public void glossaryMenuItemAction() {
		Parent glossaryRoot = loadGlossary();
		showGlossary(glossaryRoot);
	}
	
	@FXML
	public void aboutMenuItemAction() {
		app.initSaveHandler(null, null, null, app.getSelectedProject(), null, "projectChange");
		app.launchERBLandingNew2();
	}
	
	private Parent loadGlossary() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/Glossary.fxml"));
			GlossaryController glossaryController = new GlossaryController();
			fxmlLoader.setController(glossaryController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private void showGlossary(Parent glossaryRoot) {
		if (glossaryRoot != null) {
			Stage stage = new Stage();
			Scene scene = new Scene(glossaryRoot);
			stage.setScene(scene);
			stage.setTitle("ERB: Glossary");
			stage.showAndWait();
		} else {
			logger.error("Cannot showGlossary. glossaryRoot is null.");
		}
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
