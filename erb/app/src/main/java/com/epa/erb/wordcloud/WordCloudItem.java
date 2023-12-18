package com.epa.erb.wordcloud;

import javafx.scene.control.Button;

public class WordCloudItem {

	int size;
	int count;
	boolean merge;
	String phrase;
	Button plusButton;
	Button minusButton;
	public WordCloudItem(boolean merge, String phrase, Button plusButton, Button minusButton, int count, int size) {
		this.size = size;
		this.count = count;
		this.merge = merge;
		this.phrase = phrase;
		this.plusButton = plusButton;
		this.minusButton = minusButton;
	}
	
	public boolean isMerge() {
		return merge;
	}
	
	public void setMerge(boolean merge) {
		this.merge = merge;
	}
	
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public Button getPlusButton() {
		return plusButton;
	}
	public void setPlusButton(Button plusButton) {
		this.plusButton = plusButton;
	}
	public Button getMinusButton() {
		return minusButton;
	}
	public void setMinusButton(Button minusButton) {
		this.minusButton = minusButton;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

}
