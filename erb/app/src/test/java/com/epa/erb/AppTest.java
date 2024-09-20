package com.epa.erb;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class AppTest extends ApplicationTest {
	Map<String, Object> namespace;
	
	@Override
    public void start(Stage primaryStage) throws Exception {
		App app = new App();
		app.setupERBFileDirectory();
		
		App.logger = new MyLogger(App.class.getName());
		
		app.sizeScreen(app.getScreenResolution(), app.getScreenSize());
		app.readAndStoreData();
		app.createExploreProject();
		app.showERBToolMain();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLanding.fxml"));
		ERBLandingController erbLandingNew2Controller = new ERBLandingController(app);
		fxmlLoader.setController(erbLandingNew2Controller);
		Parent erbLanding = fxmlLoader.load();
		namespace = fxmlLoader.getNamespace();
    	primaryStage.setScene(new Scene(erbLanding));
		primaryStage.show();
    	primaryStage.toFront();
    }

	@Test
	public void test() {
		Assertions.assertThat((Label)namespace.get("erbLandingLabel"))
			.hasText("Equitable Resilience Builder (ERB)");
	}
}
