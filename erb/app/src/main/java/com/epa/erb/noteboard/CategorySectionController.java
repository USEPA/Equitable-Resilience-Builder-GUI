package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
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
	
	private ArrayList<PostItNoteController> orderListOfPostItNoteControllers() {
		listOfPostItNoteControllers = sortArray(listOfPostItNoteControllers);
		return listOfPostItNoteControllers;
	}
	
	public ArrayList<PostItNoteController> sortArray(ArrayList<PostItNoteController> inputArray) {
		for (int i = 1; i < inputArray.size(); i++) {
			PostItNoteController postItNoteController = inputArray.get(i);
			int key = inputArray.get(i).getPostItNoteIndex(this);
			for (int j = i - 1; j >= 0; j--) {
				if (key < inputArray.get(j).getPostItNoteIndex(this)) {
					inputArray.set(j + 1, inputArray.get(j));
					if (j == 0) {
						inputArray.set(0, postItNoteController);
					}
				} else {
					inputArray.set(j + 1, postItNoteController);
					break;
				}
			}
		}
		return inputArray;
	}

	void setCategoryLabelText(String text) {
		categoryLabel.setText(text);
	}

	public HBox getCategoryHBox() {
		return categoryHBox;
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

	public void setListOfPostItNoteControllers(ArrayList<PostItNoteController> listOfPostItNoteControllers) {
		this.listOfPostItNoteControllers = listOfPostItNoteControllers;
	}
	
	public ArrayList<PostItNoteController> getListOfPostItNoteControllers() {
		return orderListOfPostItNoteControllers();
	}	
	
}
