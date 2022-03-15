package erb;

public class Form {
	
	String id;
	String name;
	String fileName;
	String description;
	public Form (String id, String name, String fileName, String description) {
		this.id = id;
		this.name = name;
		this.fileName = fileName;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
