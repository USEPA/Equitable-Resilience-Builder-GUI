package com.epa.erb.intro_panels;

import com.epa.erb.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class IntroPanelLoader {
	
	private App app;
	public IntroPanelLoader(App app) {
		this.app = app;
	}
	
	
	
	public void loadEquityAndResiliencePanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/EquityAndResilience.fxml"));
			EquityAndResilienceController equityAndResilienceController = new EquityAndResilienceController(app);
			fxmlLoader.setController(equityAndResilienceController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadHowDoesItWorkPanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/HowDoesItWork.fxml"));
			HowDoesItWorkController howDoesItWorkController = new HowDoesItWorkController(app);
			fxmlLoader.setController(howDoesItWorkController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadHowERBMadePanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/HowERBMade.fxml"));
			HowERBMadeController howERBMadeController = new HowERBMadeController(app, this);
			fxmlLoader.setController(howERBMadeController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadHowOthersAreUsingERBPanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/HowOthersAreUsingERB.fxml"));
			HowOthersAreUsingERBController howOthersAreUsingERB = new HowOthersAreUsingERBController(app);
			fxmlLoader.setController(howOthersAreUsingERB);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadExplorePanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/ExploreERB.fxml"));
			ExploreERBController exploreERBController = new ExploreERBController(app);
			fxmlLoader.setController(exploreERBController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadWhoAreERBUsersPanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/WhoAreERBUsers.fxml"));
			WhoAreERBUsersController whoAreERBUsersController = new WhoAreERBUsersController(app);
			fxmlLoader.setController(whoAreERBUsersController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadWhatMakesERBDifferentPanel() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro_panels/WhatMakesERBDifferent.fxml"));
			WhatMakesERBDifferentController whatMakesERBDifferentController = new WhatMakesERBDifferentController(app);
			fxmlLoader.setController(whatMakesERBDifferentController);
			Parent vBox = fxmlLoader.load();
			app.loadNodeToERBContainer(vBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public App getApp() {
		return app;
	}
	
	public void setApp(App app) {
		this.app = app;
	}

}
