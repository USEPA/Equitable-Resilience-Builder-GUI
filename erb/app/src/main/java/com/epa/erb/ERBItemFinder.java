package com.epa.erb;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.goal.GoalCategory;

import javafx.scene.control.TreeItem;

public class ERBItemFinder {

	private Logger logger = LogManager.getLogger(ERBItemFinder.class);

	public ERBItemFinder() {

	}

	public GoalCategory getGoalCategoryByName(ArrayList<GoalCategory> goalCategories, String goalCategoryName) {
		if (goalCategoryName != null) {
			for (GoalCategory goalCategory : goalCategories) {
				if (goalCategory.getCategoryName().contentEquals(goalCategoryName)) {
					return goalCategory;
				}
			}
		} else {
			logger.error("Cannot getGoalCategoryByName. goalCategoryName is null.");
			return null;
		}
		logger.debug("Cannot getGoalCategoryByName. GoalCategory returned is null");
		return null;
	}
	
	public ERBContentItem getParent(ArrayList<ERBContentItem> uniqueERBContentItems, TreeItem<ERBContentItem> currentSelectedTreeItem) {
		//ERBContentItem currentERBContentItem = currentSelectedTreeItem.getValue();
		return null;
	}
	
	//-------------------------------------------------
	private ERBContentItem found =null;
	public ERBContentItem getERBContentItemById(ArrayList<ERBContentItem> uniqueERBContentItems, String id) {
		findERBContentItemById(uniqueERBContentItems, id);
		return found;
	}
	
	public void findERBContentItemById(ArrayList<ERBContentItem> uniqueERBContentItems, String id) {
		if(uniqueERBContentItems != null) {
			for(ERBContentItem chapERBContentItem: uniqueERBContentItems) {
				searchERBContentItemChildrenById(chapERBContentItem, id);
			}
		}
	}
	
	public void searchERBContentItemChildrenById(ERBContentItem erbContentItem, String id) {
		if(erbContentItem != null) {
			if(!erbContentItem.getId().contentEquals(id)) {
				if(erbContentItem.getChildERBContentItems().size()>0) {
					for(ERBContentItem c: erbContentItem.getChildERBContentItems()) {
						searchERBContentItemChildrenById(c, id);
					}
				}
			} else {
				found = erbContentItem;
			}
		} 
	}
	//--------------------------------------------------
}
