package com.epa.erb;

import java.util.HashMap;

import org.controlsfx.control.BreadCrumbBar;

import com.epa.erb.project.Project;
import com.epa.erb.utility.MainPanelHandler;

import javafx.scene.Parent;
import javafx.scene.control.TreeItem;

public class MyBreadCrumbBar extends BreadCrumbBar<String> {
	
	private Project project;
	HashMap<TreeItem<String>, String> breadCrumbIdHashMap = new HashMap<TreeItem<String>, String>();
	
	private App app;
	public MyBreadCrumbBar(App app) {
		this.app = app;
	}
		
	public void initMyBreadCrumbBar(String crumb, String id) {
		setOnCrumbAction(e -> breadCrumbSelected(e.getSelectedCrumb()));
		TreeItem<String> breadCrumb = new TreeItem<String> (crumb.toUpperCase());
		setSelectedCrumb(breadCrumb);
		breadCrumbIdHashMap.put(breadCrumb, id);
	}
	
	public void addBreadCrumb(String crumb, String id) {
		TreeItem<String> breadCrumb = new TreeItem<String> (crumb.toUpperCase());
		getSelectedCrumb().getChildren().add(breadCrumb);
		setSelectedCrumb(breadCrumb);
		breadCrumbIdHashMap.put(breadCrumb, id);
	}
	
	public void breadCrumbSelected(TreeItem<String> breadCrumb) {
		MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
		String mainPanelId = breadCrumbIdHashMap.get(breadCrumb);
		mainPanelHandler.loadPanel(mainPanelId, project);
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
