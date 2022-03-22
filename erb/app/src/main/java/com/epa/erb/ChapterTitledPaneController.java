package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class ChapterTitledPaneController implements Initializable{

	@FXML
	TitledPane titledPane;
	@FXML
	VBox titledPaneVBox;
	@FXML
	ListView<SelectedActivity> titledPaneListView;
	
	String paneTitle;
	public ChapterTitledPaneController(String paneTitle) {
		this.paneTitle = paneTitle;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titledPane.setExpanded(true);
		titledPane.setText(paneTitle);
	}
			
	public String getPaneTitle() {
		return paneTitle;
	}

	public VBox getTitledPaneVBox() {
		return titledPaneVBox;
	}

	public ListView<SelectedActivity> getTitledPaneListView() {
		return titledPaneListView;
	}		
}
