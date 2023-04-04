package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.IndicatorCard;
import com.epa.erb.engagement_action.EngagementActionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class IndicatorSetupFormController implements Initializable{

	@FXML
	VBox indicatorListVBox;
	
	private int numberOfAllowedIndicators = 10;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	private EngagementActionController engagementActionController;
	public IndicatorSetupFormController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}
	
	@FXML
	public void beginButtonAction() {
		boolean selectedIndicatorsAreUnique = areSelectedIndicatorsUnique();
		if(selectedIndicatorsAreUnique) {
			ArrayList<IndicatorCard> selectedIndicatorCards = getSelectedIndicatorCards();
			
			Pane root = loadNoteBoard_LinearRankingController(selectedIndicatorCards);
			engagementActionController.cleanContentVBox();
			engagementActionController.addContentToContentVBox(root, true);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please select unique indicator cards.");
			alert.showAndWait();
		}
	}
	
	private ArrayList<IndicatorCard> getSelectedIndicatorCards(){
		ArrayList<IndicatorCard> cards = new ArrayList<IndicatorCard>();
		for (int i = 0; i < indicatorListVBox.getChildren().size(); i++) {
			HBox child = (HBox) indicatorListVBox.getChildren().get(i);
			ComboBox<IndicatorCard> comboBox = (ComboBox<IndicatorCard>) child.getChildren().get(1);
			IndicatorCard indicatorCard = comboBox.getSelectionModel().getSelectedItem();
			if (indicatorCard != null) {
				cards.add(indicatorCard);
			}
		}
		return cards;
	}
	
	private boolean areSelectedIndicatorsUnique() {
		ArrayList<String> indicatorIds = new ArrayList<String>();
		for (int i = 0; i < indicatorListVBox.getChildren().size(); i++) {
			HBox child = (HBox) indicatorListVBox.getChildren().get(i);
			ComboBox<IndicatorCard> comboBox = (ComboBox<IndicatorCard>) child.getChildren().get(1);
			IndicatorCard indicatorCard = comboBox.getSelectionModel().getSelectedItem();
			if (indicatorCard != null) {
				if (!indicatorIds.contains(indicatorCard.getId())) {
					indicatorIds.add(indicatorCard.getId());
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	private Pane loadNoteBoard_LinearRankingController(ArrayList<IndicatorCard> selectedIndicatorCards) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoard_LinearRanking noteBoardContentController = new NoteBoard_LinearRanking(engagementActionController.getApp(), engagementActionController.getCurrentSelectedERBContentItem(), selectedIndicatorCards);
			fxmlLoader.setController(noteBoardContentController);
			VBox root = fxmlLoader.load();
			noteBoardContentController.setUpNoteBoard(1);
			return root;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void addIndicatorSelections() {
		for(int i =0; i < numberOfAllowedIndicators; i++) {
			Pane indicatorSelectionRoot = loadIndicatorSelectionController((i+1));
			if(indicatorSelectionRoot != null) indicatorListVBox.getChildren().add(indicatorSelectionRoot);
		}
	}
	
	private Pane loadIndicatorSelectionController(int number) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/IndicatorSelection.fxml"));
			IndicatorSelectionController indicatorSelectionController = new IndicatorSelectionController(number, engagementActionController.getApp());
			fxmlLoader.setController(indicatorSelectionController);
			HBox root = fxmlLoader.load();
			indicatorSelectionController.fillIndicatorChoiceBox();
			return root;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
