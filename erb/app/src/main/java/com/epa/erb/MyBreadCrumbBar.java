package com.epa.erb;

import java.util.HashMap;
import org.controlsfx.control.BreadCrumbBar;
import com.epa.erb.project.Project;
import com.epa.erb.utility.MainPanelHandler;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;

public class MyBreadCrumbBar extends BreadCrumbBar<String> {
	
	HashMap<TreeItem<String>, String> breadCrumbIdHashMap = new HashMap<TreeItem<String>, String>();
	
	private App app;
	public MyBreadCrumbBar(App app) {
		this.app = app;
		 setOnCrumbAction(e->breadCrumbSelected(e.getSelectedCrumb()));
	}
	
	private Project project;
	
	public void initMyBreadCrumbBar(String crumb, String id) {
		TreeItem<String> breadCrumb = new TreeItem<String> (crumb);
		setSelectedCrumb(breadCrumb);
		breadCrumbIdHashMap.put(breadCrumb, id);
	}
	
	public void addBreadCrumb(String crumb, String id) {
		TreeItem<String> breadCrumb = new TreeItem<String> (crumb);
		getSelectedCrumb().getChildren().add(breadCrumb);
		setSelectedCrumb(breadCrumb);
		breadCrumbIdHashMap.put(breadCrumb, id);
	}
	
	public void breadCrumbSelected(TreeItem<String> breadCrumb) {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		String mainPanelId = breadCrumbIdHashMap.get(breadCrumb);
		//TODO: How to not make static
		if(mainPanelId.contentEquals("86")) {
			Parent root = mainPanelHandler.loadERBLanding(app);
			app.loadNodeToERBContainer(root);
		}else if(mainPanelId.contentEquals("87")) {
			Parent root = mainPanelHandler.loadProjectSelectionRoot(app);
			app.loadNodeToERBContainer(root);
		}else if(mainPanelId.contentEquals("88")) {
			//Don't allow right now
		}else if(mainPanelId.contentEquals("89")) {
			Parent root = mainPanelHandler.loadEngagementActionRoot(app, project);
			app.loadNodeToERBContainer(root);
		}else if(mainPanelId.contentEquals("90")){
			Parent root = mainPanelHandler.loadProjectCreationRoot(app);
			app.loadNodeToERBContainer(root);
		} else {
			
		}
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
