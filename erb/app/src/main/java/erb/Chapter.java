package erb;

import java.util.ArrayList;

public class Chapter {
	
	int chapterNum;
	String numericName;
	String genericName;
	String descriptionName;
	
	public Chapter(int chapterNum, String numericName, String genericName, String descriptionName) {
		this.chapterNum = chapterNum;
		this.numericName = numericName;
		this.genericName = genericName;
		this.descriptionName = descriptionName;
	}	

	ArrayList<Step> steps = new ArrayList<Step>();
	
	public int getChapterNum() {
		return chapterNum;
	}
	public void setChapterNum(int chapterNum) {
		this.chapterNum = chapterNum;
	}
	public String getNumericName() {
		return numericName;
	}
	public void setNumericName(String numericName) {
		this.numericName = numericName;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public String getDescriptionName() {
		return descriptionName;
	}
	public void setDescriptionName(String descriptionName) {
		this.descriptionName = descriptionName;
	}
	public ArrayList<Step> getSteps() {
		return steps;
	}
	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}
	public void addStep(Step step) {
		steps.add(step);
	}
	public int getNumberOfSteps() {
		return steps.size();
	}
	public Step getStep(int stepNum) {
		for(Step step: steps) {
			if(step.getStepNum() == stepNum) {
				return step;
			}
		}
		return null;
	}
}
