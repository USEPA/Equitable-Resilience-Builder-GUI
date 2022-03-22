package com.epa.erb;

public class ActivityAttribute {
	
	String shortName;
	String longName;
	int value;
	String data;
	String color;
	
	public ActivityAttribute(String shortName, String longName, int value, String data, String color) {
		this.shortName = shortName;
		this.longName = longName;
		this.value = 1000;
		this.data = data;
		this.color = color;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
