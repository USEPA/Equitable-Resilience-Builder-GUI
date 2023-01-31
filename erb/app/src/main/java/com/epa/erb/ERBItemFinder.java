package com.epa.erb;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;

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

	public Chapter getChapterById(ArrayList<Chapter> chapters, String id) {
		if (id != null) {
			for (Chapter chapter : chapters) {
				if (chapter.getId().contentEquals(id)) {
					return chapter;
				}
			}
		} else {
			logger.error("Cannot getChapterById. id is null.");
			return null;
		}
		logger.debug("Cannot getChapterById.Chapter returned is null.");
		return null;
	}
	
	private boolean exists;
	
	public Chapter getChapterForContentPanel(ArrayList<Chapter> chapters, ContentPanel contentPanel) {
		exists = false;
		if(contentPanel != null) {
			for(Chapter chapter: chapters) {
				ERBItem erbItem = new ERBItem(chapter.getId(), chapter.getGuid(), chapter.getLongName(), chapter.getShortName(), chapter);
				checkForGuidInChapter(erbItem, contentPanel.getGuid());
				if(exists) {
					return chapter;
				}
			}
		}
		return null;
	}
	
	public void checkForGuidInChapter(ERBItem erbItem, String guid) {
		ArrayList<ContentPanel> panelsToSearch = null;
		if (erbItem != null) {
			if (erbItem.getObject().toString().contains("Chapter")) {
				Chapter chapter = (Chapter) erbItem.getObject();
				panelsToSearch = chapter.getContentPanels();
			} else {
				ContentPanel contentPanel = (ContentPanel) erbItem.getObject();
				panelsToSearch = contentPanel.getNextLayerContentPanels();
			}
			if (panelsToSearch != null) {
				for (ContentPanel cp : panelsToSearch) {
					if (cp.getGuid() != null) {
						if (cp.getGuid().contentEquals(guid)) {
							exists = true;
						}
					}
					if (cp.getNextLayerContentPanels().size() > 0) {
						ERBItem cpERBItem = new ERBItem(cp.getId(), cp.getGuid(), cp.getLongName(), cp.getShortName(), cp);
						checkForGuidInChapter(cpERBItem, guid);
					}
				}
			}
		}
	}

	public Chapter getChapterForNameInGoal(String chapterName, Goal goal) {
		if (chapterName != null && goal != null) {
			for (Chapter chapter : goal.getChapters()) {
				if (chapter.getLongName().contentEquals(chapterName)) {
					return chapter;
				}
			}
			logger.debug("Chapter returned is null");
			return null;
		} else {
			logger.error("Cannot getChapterForNameInGoal. chapterName or goal is null.");
			return null;
		}
	}
	
	public ContentPanel getContentPanelById(ArrayList<ContentPanel> contentPanels, String id) {
		if (id != null) {
			for (ContentPanel contentPanel : contentPanels) {
				if (contentPanel.getId().contentEquals(id)) {
					return contentPanel;
				}
			}
		} else {
			logger.error("Cannot getContentPanelById. id is null.");
			return null;
		}
		logger.debug("Cannot getContentPanelById. ContentPanel returned is null");
		return null;
	}

	public ContentPanel findContentPanelById(ArrayList<Chapter> chapters, String id) {
		if (id != null) {
			for (Chapter chapter : chapters) {
				for (ContentPanel contentPanel : chapter.getContentPanels()) {
					if (contentPanel.getId().contentEquals(id)) {
						return contentPanel;
					}
				}
			}
		} else {
			logger.error("Cannot findContentPanelById. id is null.");
			return null;
		}
		logger.debug("Cannot findContentPanelById. ContentPanel returned is null");
		return null;
	}
	public int getIndexOfContentPanelInChapter(Chapter chapter, ContentPanel contentPanel) {
		int count = 0;
		if (chapter != null && contentPanel != null) {
			for (ContentPanel c : chapter.getContentPanels()) {
				if (c.getId().contentEquals(contentPanel.getId())) {
					return count;
				}
				count++;
			}
		} else {
			logger.error("Cannot getIndexOfContentPanelInChapter. chapter or contentPanel is null.");
		}
		return -1;
	}

}
