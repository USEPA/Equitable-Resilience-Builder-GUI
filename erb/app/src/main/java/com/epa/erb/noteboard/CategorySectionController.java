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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	String categoryName;
	public CategorySectionController(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public void initCategorySection() {
		categoryLabel.setText(categoryName);
	}

	public HBox getPostItHBox() {
		return postItHBox;
	}
	
}
