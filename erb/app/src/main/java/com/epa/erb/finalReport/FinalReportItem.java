package com.epa.erb.finalReport;

public class FinalReportItem {

	String id;
	String displayName;
	String start;
	String stop;
	String tableName;

	public FinalReportItem(String id, String displayName, String start, String stop, String tableName) {
		this.id = id;
		this.displayName = displayName;
		this.start = start;
		this.stop = stop;
		this.tableName = tableName;
	}

	public FinalReportItem() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String toString() {
		return "Id: " + id + "\n" +
				"Display Name: " + displayName + "\n" +
				"Start: " + start + "\n" + 
				"Stop: " + stop;
	}

	
}
