package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CategorySectionController implements Initializable {

	@FXML
	HBox categoryHBox;
	@FXML
	Label categoryLabel;
	@FXML
	HBox postItHBox;
	
	private String categoryName;
	public CategorySectionController(String categoryName) {
		this.categoryName = categoryName;
	}
	
	private ArrayList<PostItNoteController> listOfPostItNoteControllers = new ArrayList<PostItNoteController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setCategoryLabelText(categoryName);
	}	
	
	void setCategoryLabelText(String text) {
		categoryLabel.setText(text);
	}

	public Label getCategoryLabel() {
		return categoryLabel;
	}

	public HBox getPostItHBox() {
		return postItHBox;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ArrayList<PostItNoteController> getListOfPostItNoteControllers() {
		Collections.reverse(listOfPostItNoteControllers);
		return listOfPostItNoteControllers;
	}

	public void setListOfPostItNoteControllers(ArrayList<PostItNoteController> listOfPostItNoteControllers) {
		this.listOfPostItNoteControllers = listOfPostItNoteControllers;
	}

	public HBox getCategoryHBox() {
		return categoryHBox;
	}

}
