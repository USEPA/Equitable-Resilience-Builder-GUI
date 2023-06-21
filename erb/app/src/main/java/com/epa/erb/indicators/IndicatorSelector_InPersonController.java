package com.epa.erb.indicators;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class IndicatorSelector_InPersonController implements Initializable{

	@FXML
	HBox indicatorHBox;
	@FXML
	CheckBox indicatorCheckBox;
	
	public IndicatorSelector_InPersonController( ) {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public CheckBox getIndicatorCheckBox() {
		return indicatorCheckBox;
	}

}
