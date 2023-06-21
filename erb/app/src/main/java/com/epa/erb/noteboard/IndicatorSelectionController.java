package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.indicators.IndicatorCard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class IndicatorSelectionController implements Initializable{

	@FXML
	ComboBox<IndicatorCard> indicatorComboBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	private int number;
	private App app;
	public IndicatorSelectionController(int number, App app) {
		this.number = number;
		this.app = app;
	}
	
	public void fillIndicatorChoiceBox() {
		setChoiceBoxCellFactory();
		for(IndicatorCard indicatorCard: app.getIndicatorCards()) {
			indicatorComboBox.getItems().add(indicatorCard);
		}
	}
	
	private void setChoiceBoxCellFactory() {
		Callback<ListView<IndicatorCard>, ListCell<IndicatorCard>> cellFactory = new Callback<ListView<IndicatorCard>, ListCell<IndicatorCard>>() {
		    @Override
		    public ListCell<IndicatorCard> call(ListView<IndicatorCard> l) {
		        return new ListCell<IndicatorCard>() {

		            @Override
		            protected void updateItem(IndicatorCard item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item == null || empty) {
		                    setGraphic(null);
		                } else {
		                    setText(item.getSystem() + " - " + item.getIndicator());
		                }
		            }
		        } ;
		    }
		};
		indicatorComboBox.setCellFactory(cellFactory);
		indicatorComboBox.setButtonCell(cellFactory.call(null));

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
	
}
