package com.epa.erb.indicators;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.epa.erb.App;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.noteboard.NoteBoardItem_Indicator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IndicatorsPrintViewController implements Initializable {

	private Logger logger;
	ArrayList<HBox> listOfCardHBoxes = new ArrayList<HBox>();
	
	private App app;
	public IndicatorsPrintViewController(App app) {
		this.app = app;
		
		logger = app.getLogger();
	}
	
	@FXML
	VBox cardVBox;
			
	private void addCardsToHBox(ArrayList<IndicatorCard> cards) {
		NoteBoardContentController noteBoardContentController = new NoteBoardContentController(app, app.getEngagementActionController().getCurrentGoal(), app.getEngagementActionController().getCurrentSelectedERBContentItem(), app.getIndicatorCards());
		int count = 0;
		HBox cardHBox = null;
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<String> selectedData = iSDP.getSavedSelectedData_InPerson();
		for (IndicatorCard indicatorCard : cards) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardItem.fxml"));
				NoteBoardItem_Indicator noteBoardIndicatorItem = new NoteBoardItem_Indicator(noteBoardContentController, indicatorCard);
				fxmlLoader.setController(noteBoardIndicatorItem);
				VBox cVBox = fxmlLoader.load();
				cVBox.setPrefWidth(350);
				cVBox.setPrefHeight(500);
				cVBox.setMinWidth(350);
				cVBox.setMinHeight(500);
				noteBoardIndicatorItem.removeFlipHBox();
				noteBoardIndicatorItem.resizeImageView();
				noteBoardIndicatorItem.setColorAndImage();
				if(selectedData != null) {
					for(String data: selectedData) {
						noteBoardIndicatorItem.addTextForPrinting(data);
					}
				}

				if (count == 0) {
					cardHBox = new HBox();
					cardHBox.setSpacing(25.0);
					listOfCardHBoxes.add(cardHBox);
					count++;
				} else {
					count = 0;
				}
				cardHBox.getChildren().add(cVBox);
			} catch (Exception e) {
				logger.log(Level.FINE, "Failed to load NoteBoardItem.fxml.");
				logger.log(Level.FINER, "Failed to load NoteBoardItem.fxml: " + e.getStackTrace());
				}
		}
	}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<IndicatorCard> cards = iSDP.getSavedSelectedIndicatorCards_InPerson();
		addCardsToHBox(cards);
	}
	
	@FXML
	public void printButtonAction() {
		
	}

	public VBox getCardVBox() {
		return cardVBox;
	}

	public ArrayList<HBox> getListOfCardHBoxes() {
		return listOfCardHBoxes;
	}

	public App getApp() {
		return app;
	}
	
	
	
}
