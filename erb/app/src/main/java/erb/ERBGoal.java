package erb;

public class ERBGoal {

	int id;
	String title;
	boolean isStatic;
	String description;
	String chapterAssignment;
	
	public ERBGoal() {
		
	}
	
	public ERBGoal(int id, String title, boolean isStatic, String description) {
		this.id = id;
		this.title = title;
		this.isStatic = isStatic;
		this.description = description;
	}
	
	public ERBGoal(int id, String title, boolean isStatic, String description, String chapterAssignment) {
		this.id = id;
		this.title = title;
		this.isStatic = isStatic;
		this.description = description;
		this.chapterAssignment = chapterAssignment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChapterAssignment() {
		return chapterAssignment;
	}

	public void setChapterAssignment(String chapterAssignment) {
		this.chapterAssignment = chapterAssignment;
	}
		
}
