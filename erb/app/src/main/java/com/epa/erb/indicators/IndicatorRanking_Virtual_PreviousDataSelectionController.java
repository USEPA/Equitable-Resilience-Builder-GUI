package com.epa.erb.indicators;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class IndicatorRanking_Virtual_PreviousDataSelectionController implements Initializable{
	
	public IndicatorRanking_Virtual_PreviousDataSelectionController(App app, IndicatorRanking_VirtualController iRVC) {
		
	}
	
	@FXML
	ListView<Text> dataListView;		

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void loadButtonAction() {

	}

}
