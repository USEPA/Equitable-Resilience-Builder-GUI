package erb;

public class Step {
	
	int stepNum;
	int parentChapter;
	String numericName;
	String genericName;
	String descriptionName;
	
	public Step(int stepNum, int parentChapter, String numericName, String genericName, String descriptionName) {
		this.stepNum = stepNum;
		this.parentChapter = parentChapter;
		this.numericName = numericName;
		this.genericName = genericName;
		this.descriptionName = descriptionName;
	}
	
	public int getStepNum() {
		return stepNum;
	}
	public void setStepNum(int stepNum) {
		this.stepNum = stepNum;
	}
	public int getParentChapter() {
		return parentChapter;
	}
	public void setParentChapter(int parentChapter) {
		this.parentChapter = parentChapter;
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
}
