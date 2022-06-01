package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.project.ProjectSelectionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ERBLandingController implements Initializable {

	@FXML
	Circle circle1;
	@FXML
	Circle circle2;
	@FXML
	Circle circle3;
	@FXML
	ScrollPane scrollPane1;
	@FXML
	ScrollPane scrollPane2;
	@FXML
	ScrollPane scrollPane3;
	@FXML
	VBox vBox1;
	@FXML
	VBox vBox2;
	@FXML
	VBox vBox3;

	private App app;

	public ERBLandingController(App app) {
		this.app = app;
	}

	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBLandingController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}

	private void handleControls() {
		circle1.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		circle2.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		circle3.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		hideScrollPane1();
		hideScrollPane2();
		hideScrollPane3();
	}

	@FXML
	public void beginERBProcessButtonClicked() {
		loadProjectSelectionToContainer();
	}
	
	private void loadProjectSelectionToContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectSelection.fxml"));
			ProjectSelectionController projectSelectionController = new ProjectSelectionController(app);
			fxmlLoader.setController(projectSelectionController);
			app.loadContent(fxmlLoader.load());			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void circle1Clicked() {
		if(isCircle1ContentShowing()) {
			hideScrollPane1();
		} else {
			showScrollPane1();
		}
	}
	
	@FXML
	public void circle2Clicked() {
		if(isCircle2ContentShowing()) {
			hideScrollPane2();
		} else {
			showScrollPane2();
		}
	}
	
	@FXML
	public void circle3Clicked() {
		if(isCircle3ContentShowing()) {
			hideScrollPane3();
		} else {
			showScrollPane3();
		}
	}
	
	private void hideScrollPane1() {
		if(vBox1.getChildren().contains(scrollPane1)) {
			vBox1.getChildren().remove(scrollPane1);
		}
	}
	
	private void showScrollPane1() {
		if(!vBox1.getChildren().contains(scrollPane1)) {
			vBox1.getChildren().add(1,scrollPane1);
		}
	}
	
	private boolean isCircle1ContentShowing() {
		if(vBox1.getChildren().contains(scrollPane1)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isCircle2ContentShowing() {
		if(vBox2.getChildren().contains(scrollPane2)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isCircle3ContentShowing() {
		if(vBox3.getChildren().contains(scrollPane3)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void hideScrollPane2() {
		if(vBox2.getChildren().contains(scrollPane2)) {
			vBox2.getChildren().remove(scrollPane2);
		}
	}
	
	private void showScrollPane2() {
		if(!vBox2.getChildren().contains(scrollPane2)) {
			vBox2.getChildren().add(1,scrollPane2);
		}
	}
	
	private void hideScrollPane3() {
		if(vBox3.getChildren().contains(scrollPane3)) {
			vBox3.getChildren().remove(scrollPane3);
		}
	}
	
	private void showScrollPane3() {
		if(!vBox3.getChildren().contains(scrollPane3)) {
			vBox3.getChildren().add(1,scrollPane3);
		}
	}

}
