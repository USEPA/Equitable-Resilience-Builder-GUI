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
	HBox postItHBox;
	
	private String categoryName;
	public CategorySectionController(String categoryName) {
		this.categoryName = categoryName;
	}
	
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

}
