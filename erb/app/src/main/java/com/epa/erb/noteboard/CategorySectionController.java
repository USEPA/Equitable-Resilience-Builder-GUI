package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CategorySectionController implements Initializable {

	@FXML
	Label categoryLabel;
	@FXML
	HBox categoryHBox;
	@FXML
	HBox postItHBox;
	
	private String categoryName;
	public CategorySectionController(String categoryName) {
		this.categoryName = categoryName;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	void initCategorySection() {
		categoryLabel.setText(categoryName);
	}

   HBox getPostItHBox() {
		return postItHBox;
	}
	
}
