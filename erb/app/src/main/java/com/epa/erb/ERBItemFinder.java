package com.epa.erb;

import java.util.ArrayList;
import com.epa.erb.goal.GoalCategory;

public class ERBItemFinder {


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
			return null;
		}
		return null;
	}
	
	//-------------------------------------------------
	private ERBContentItem found = null;
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
				if(erbContentItem.getChildERBContentItems().size() > 0) {
					for(ERBContentItem c: erbContentItem.getChildERBContentItems()) {
						searchERBContentItemChildrenById(c, id);
					}
				}
			} else {
				found = erbContentItem;
			}
		} 
	}
}
