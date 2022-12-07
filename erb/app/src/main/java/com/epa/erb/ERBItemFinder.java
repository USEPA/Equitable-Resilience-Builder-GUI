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

	public Chapter getChapterForActivity(ArrayList<Chapter> chapters, Activity activity) {
		if (activity != null) {
			for (Chapter chapter : chapters) {
				for (Activity chapterActivity : chapter.getAssignedActivities()) {
					if (chapterActivity.getGuid().contentEquals(activity.getGuid())) {
						return chapter;
					}
				}
			}
		} else {
			logger.error("Cannot getChapterForActivity. activity is null.");
			return null;

		}
		logger.error("Cannot getChapterForActivity. Chapter returned is null.");
		return null;
	}

	public Chapter getChapterForStep(ArrayList<Chapter> chapters, Step step) {
		if (step != null) {
			for (Chapter chapter : chapters) {
				for (Step chapterStep : chapter.getAssignedSteps()) {
					if (chapterStep.getGuid().contentEquals(step.getGuid())) {
						return chapter;
					}
				}
			}
		} else {
			logger.error("Cannot getChapterForStep. step is null.");
			return null;
		}
		logger.error("Cannot getChapterForStep. Chapter returned is null.");
		return null;
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

	public Chapter findChapterForGuid(ArrayList<Chapter> chapters, String guid) {
		if (guid != null) {
			for (Chapter chapter : chapters) {
				if (chapter.getGuid().contentEquals(guid)) {
					return chapter;
				}
				for (Activity activity : chapter.getAssignedActivities()) {
					if (activity.getGuid().contentEquals(guid)) {
						return chapter;
					}
					for (Step step : activity.getAssignedSteps()) {
						if (step.getGuid().contentEquals(guid)) {
							return chapter;
						}
						for (InteractiveActivity interactiveActivity : step.getAssignedDynamicActivities()) {
							if (interactiveActivity.getGuid().contentEquals(guid)) {
								return chapter;
							}
						}
					}
					for (InteractiveActivity interactiveActivity : activity.getAssignedDynamicActivities()) {
						if (interactiveActivity.getGuid().contentEquals(guid)) {
							return chapter;
						}
					}
				}

				for (Step step : chapter.getAssignedSteps()) {
					if (step.getGuid().contentEquals(guid)) {
						return chapter;
					}
					for (InteractiveActivity interactiveActivity : step.getAssignedDynamicActivities()) {
						if (interactiveActivity.getGuid().contentEquals(guid)) {
							return chapter;
						}
					}
				}
			}
		} else {
			logger.error("Cannot findChapterForGuid. guid is null.");
			return null;
		}
		logger.error("Cannot findChapterForGuid. Chapter returned is null.");
		return null;
	}

	public Activity getActivityById(ArrayList<Activity> activities, String id) {
		if (id != null) {
			for (Activity activity : activities) {
				if (activity.getId().contentEquals(id)) {
					return activity;
				}
			}
		} else {
			logger.error("Cannot getActivityById. id is null.");
			return null;
		}
		logger.debug("Cannot getActivityById. Activity returned is null");
		return null;
	}

	public Activity findActivityById(ArrayList<Chapter> chapters, String id) {
		if (id != null) {
			for (Chapter chapter : chapters) {
				for (Activity activity : chapter.getAssignedActivities()) {
					if (activity.getId().contentEquals(id)) {
						return activity;
					}
				}
			}
		} else {
			logger.error("Cannot getActivityById. id is null.");
			return null;
		}
		logger.debug("Cannot getActivityById. Activity returned is null");
		return null;
	}

	public Activity getActivityForStep(ArrayList<Chapter> chapters, Step step) {
		if (step != null) {
			for (Chapter chapter : chapters) {
				for (Activity chapterActivity : chapter.getAssignedActivities()) {
					for (Step activityStep : chapterActivity.getAssignedSteps()) {
						if (activityStep.getGuid().contentEquals(step.getGuid())) {
							return chapterActivity;
						}
					}
				}
			}
		} else {
			logger.error("Cannot getActivityForStep. step is null.");
			return null;
		}
		logger.error("Cannot getActivityForStep. Activity returned is null.");
		return null;
	}

	public Activity getActivityForInteractiveActivity(ArrayList<Chapter> chapters,
			InteractiveActivity interactiveActivity) {
		if (interactiveActivity != null) {
			for (Chapter chapter : chapters) {
				for (Activity chapterActivity : chapter.getAssignedActivities()) {
					for (InteractiveActivity activityDynamicActivity : chapterActivity.getAssignedDynamicActivities()) {
						if (activityDynamicActivity.getGuid().contentEquals(interactiveActivity.getGuid())) {
							return chapterActivity;
						}
					}
				}
			}
		} else {
			logger.error("Cannot getActivityForInteractiveActivity. interactiveActivity is null.");
			return null;
		}
		logger.error("Cannot getActivityForInteractiveActivity. Activity returned is null.");
		return null;
	}

	public Step getStepForInteractiveActivity(ArrayList<Chapter> chapters, InteractiveActivity interactiveActivity) {
		if (interactiveActivity != null) {
			for (Chapter chapter : chapters) {
				for (Activity chapterActivity : chapter.getAssignedActivities()) {
					for (Step activityStep : chapterActivity.getAssignedSteps()) {
						for (InteractiveActivity stepDynamicActivity : activityStep.getAssignedDynamicActivities()) {
							if (stepDynamicActivity.getGuid().contentEquals(interactiveActivity.getGuid())) {
								return activityStep;
							}
						}
					}
				}
			}
		} else {
			logger.error("Cannot getStepForInteractiveActivity. interactiveActivity is null.");
			return null;
		}
		logger.error("Cannot getStepForInteractiveActivity. Step returned is null.");
		return null;
	}

	public Step getStepById(ArrayList<Step> steps, String id) {
		if (id != null) {
			for (Step step : steps) {
				if (step.getId().contentEquals(id)) {
					return step;
				}
			}
		} else {
			logger.error("Cannot getStepById. id is null.");
			return null;
		}
		logger.debug("Cannot getStepById. Step returned is null");
		return null;
	}

	public Step findStepById(ArrayList<Chapter> chapters, String id) {
		if (id != null) {
			for (Chapter chapter : chapters) {
				for (Step chapterStep : chapter.getAssignedSteps()) {
					if (chapterStep.getId().contentEquals(id)) {
						return chapterStep;
					}
				}
				for (Activity activity : chapter.getAssignedActivities()) {
					for (Step activityStep : activity.getAssignedSteps()) {
						if (activityStep.getId().contentEquals(id)) {
							return activityStep;
						}
					}
				}
			}
		} else {
			logger.error("Cannot getStepById. id is null.");
			return null;
		}
		logger.debug("Cannot getStepById. Step returned is null");
		return null;
	}

	public InteractiveActivity getInteractiveActivityById(ArrayList<InteractiveActivity> interactiveActivities,
			String id) {
		if (id != null) {
			for (InteractiveActivity interactiveActivity : interactiveActivities) {
				if (interactiveActivity.getId().contentEquals(id)) {
					return interactiveActivity;
				}
			}
		} else {
			logger.error("Cannot getInteractiveActivityById. id is null.");
			return null;
		}
		logger.debug("Cannot getInteractiveActivityById. InteractiveActivity returned is null");
		return null;
	}

	public int getIndexOfActivityInChapter(Chapter chapter, Activity activity) {
		int count = 0;
		if (chapter != null && activity != null) {
			for (Activity a : chapter.getAssignedActivities()) {
				if (a.getId().contentEquals(activity.getId())) {
					return count;
				}
				count++;
			}
		} else {
			logger.error("Cannot getIndexOfActivityInChapter. chapter or activity is null.");
		}
		return -1;
	}

}
