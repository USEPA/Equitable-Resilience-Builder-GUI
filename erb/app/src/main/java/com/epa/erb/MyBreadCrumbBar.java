package com.epa.erb;

import java.util.HashMap;

import org.controlsfx.control.BreadCrumbBar;
import javafx.scene.control.TreeItem;

public class MyBreadCrumbBar extends BreadCrumbBar<String> {
	
	HashMap<TreeItem<String>, String> breadCrumbIdHashMap = new HashMap<TreeItem<String>, String>();
	
	public MyBreadCrumbBar() {
		 setOnCrumbAction(e->breadCrumbSelected(e.getSelectedCrumb()));
	}
	
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
		System.out.println("Selected: " + breadCrumb.getValue() + ":" + breadCrumbIdHashMap.get(breadCrumb));
		
	}

}
