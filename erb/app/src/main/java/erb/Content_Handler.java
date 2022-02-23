package erb;

import java.util.ArrayList;

public class Content_Handler {

	public Content_Handler() {
		
	}
	
	ArrayList<Chapter> chapters = new ArrayList<Chapter>();
	
	public void initContent() {
		initChapter1Content();
		initChapter2Content();
		initChapter3Content();
		initChapter4Content();
		initChapter5Content();
	}
	
	public void initChapter1Content() {
		Chapter chapter1 = new Chapter(1, "1.0", "Chapter 1", "Project Setup");
		chapter1.setSteps(initChapter1Steps());
	}
	
	public ArrayList<Step> initChapter1Steps() {
		ArrayList<Step> steps = new ArrayList<Step>();
		Step step1 = new Step(1, 1, "1.1.1", "Chapter 1 Step 1", "Frame the problem");
		Step step2 = new Step(2, 1, "1.2.1", "Chapter 1 Step 2", "Establish your goals");
		Step step3 = new Step(3, 1, "1.3.1", "Chapter 1 Step 3", "Gather a core team");
		Step step4 = new Step(4, 1, "1.4.1", "Chapter 1 Step 4", "Project plan");
		Step step5 = new Step(5, 1, "1.5.1", "Chapter 1 Step 5", "Reflect");
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		return steps;
	}
	
	public void initChapter2Content() {
		Chapter chapter2 = new Chapter(2, "2.0", "Chapter 2", "Community Engagement");
		chapter2.setSteps(initChapter2Steps());
	}
	
	public ArrayList<Step> initChapter2Steps() {
		ArrayList<Step> steps = new ArrayList<Step>();
		Step step1 = new Step(1, 2, "2.1.1", "Chapter 2 Step 1", "Create a community engagement plan");
		Step step2 = new Step(2, 2, "2.2.1", "Chapter 2 Step 2", "Conduct a stakeholder analysis");
		Step step3 = new Step(3, 2, "2.3.1", "Chapter 2 Step 3", "Plan workshops for chapters 3, 4 and 5");
		Step step4 = new Step(4, 2, "2.4.1", "Chapter 2 Step 4", "Create a relationship trackers for the ERB");
		Step step5 = new Step(5, 2, "2.5.1", "Chapter 2 Step 5", "Reflect");
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		return steps;
	}
	
	public void initChapter3Content() {
		Chapter chapter3 = new Chapter(3, "3.0", "Chapter 3", "Explore Hazards");
		chapter3.setSteps(initChapter3Steps());
	}
	
	public ArrayList<Step> initChapter3Steps() {
		ArrayList<Step> steps = new ArrayList<Step>();
		Step step1 = new Step(1, 3, "3.1.1", "Chapter 3 Step 1", "Gather information about hazards, disasters and threats");
		Step step2 = new Step(2, 3, "3.2.1", "Chapter 3 Step 2", "Gather data and information about equity");
		Step step3 = new Step(3, 3, "3.3.1", "Chapter 3 Step 3", "Engage community members");
		Step step4 = new Step(4, 3, "3.4.1", "Chapter 3 Step 4", "Idenitfy vulnerabilities and impacts of hazards and disasters");
		Step step5 = new Step(5, 3, "3.5.1", "Chapter 3 Step 5", "Discuss root causes of inequities");
		Step step6 = new Step(5, 3, "3.5.1", "Chapter 3 Step 6", "Reflect");
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		steps.add(step6);
		return steps;
	}
	
	public void initChapter4Content() {
		Chapter chapter4 = new Chapter(4, "4.0", "Chapter 4", "Explore Indicators");
		chapter4.setSteps(initChapter4Steps());
	}
	
	public ArrayList<Step> initChapter4Steps() {
		ArrayList<Step> steps = new ArrayList<Step>();
		Step step1 = new Step(1, 4, "4.1.1", "Chapter 4 Step 1", "Review built, natural, and social indicators");
		Step step2 = new Step(2, 4, "4.2.1", "Chapter 4 Step 2", "Select indicators");
		Step step3 = new Step(3, 4, "4.3.1", "Chapter 4 Step 3", "Sort and visualize indicators");
		Step step4 = new Step(4, 4, "4.4.1", "Chapter 4 Step 4", "Reflect");
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		return steps;
	}
	
	public void initChapter5Content() {
		Chapter chapter5 = new Chapter(5, "5.0", "Chapter 5", "Establish Actions");
		chapter5.setSteps(initChapter5Steps());
	}
	
	
	public ArrayList<Step> initChapter5Steps() {
		ArrayList<Step> steps = new ArrayList<Step>();
		Step step1 = new Step(1, 5, "5.1.1", "Chapter 5 Step 1", "Craft 'How might we' statements");
		Step step2 = new Step(2, 5, "5.2.1", "Chapter 5 Step 2", "Prepare for community workshop");
		Step step3 = new Step(3, 5, "5.3.1", "Chapter 5 Step 3", "Run the community workshop");
		Step step4 = new Step(4, 5, "5.4.1", "Chapter 5 Step 4", "Create a list of equitable resilience actions");
		Step step5 = new Step(5, 5, "5.5.1", "Chapter 5 Step 5", "Reflect");
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		return steps;
	}	
}
