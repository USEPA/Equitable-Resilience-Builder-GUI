package com.epa.erb.noteboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import com.epa.erb.ERBContentItem;
import com.epa.erb.IndicatorCard;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.PrintWriter;
import java.util.Scanner;

public class IndicatorSetupFormController implements Initializable{

	@FXML
	VBox vBox;
	@FXML
	VBox indicatorListVBox;
	
	private int numberOfAllowedIndicators = 10;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	FileHandler fileHandler = new FileHandler();
	private File guidDirectory;
	
	private EngagementActionController engagementActionController;
	private ERBContentItem erbContentItem;
	public IndicatorSetupFormController(EngagementActionController engagementActionController,ERBContentItem erbContentItem) {
		this.engagementActionController = engagementActionController;
		this.erbContentItem = erbContentItem;
		guidDirectory = new File(fileHandler.getGUIDDataDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal()) + "\\" + erbContentItem.getGuid());
	}
	
	public void setUp() {
		addIndicatorSelections();
		if(areExistingSelectedIndicators()) {
			ArrayList<IndicatorCard> cards = getExistingSelectedIndicators();
			loadExistingIndicators(cards);
		}
	}
	
	@FXML
	public void beginButtonAction() {		
		boolean selectedIndicatorsAreUnique = areSelectedIndicatorsUnique();
		if(selectedIndicatorsAreUnique) {
			if(areExistingSelectedIndicators() && selectedMatchSaved()) {
				Pane root = loadNoteBoard_LinearRankingController(new ArrayList<IndicatorCard>(), false);
				engagementActionController.cleanContentVBox();
				engagementActionController.addContentToContentVBox(root, true);
			} else {
				ArrayList<IndicatorCard> selectedIndicatorCards = getSelectedIndicatorCards();
				writeSelectedIndicators(selectedIndicatorCards);
				Pane root = loadNoteBoard_LinearRankingController(selectedIndicatorCards, true);
				engagementActionController.cleanContentVBox();
				engagementActionController.addContentToContentVBox(root, true);
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please select unique indicator cards.");
			alert.showAndWait();
		}
	}
	
	private boolean selectedMatchSaved() {
		ArrayList<IndicatorCard> current = getSelectedIndicatorCards();
		ArrayList<IndicatorCard> saved = getExistingSelectedIndicators();
		if(current.size() == saved.size()) {
			if(current.containsAll(saved) && saved.containsAll(current)) {
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<IndicatorCard> getExistingSelectedIndicators() {
		ArrayList<IndicatorCard> cards = new ArrayList<IndicatorCard>();
		File selectedIndicatorsFile = new File(guidDirectory.getPath() + "\\selectedIndicatorIds.txt");
		if(selectedIndicatorsFile.exists()) {
			try {
				Scanner scanner = new Scanner(selectedIndicatorsFile);
				while(scanner.hasNextLine()) {
					String id = scanner.nextLine();
					IndicatorCard iC= engagementActionController.getApp().findIndicatorItemForId(id);
					cards.add(iC);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
			}
		}
		return cards;
	}
	
	private void loadExistingIndicators(ArrayList<IndicatorCard> cards) {
		if(cards != null && cards.size() > 0) {
			for(int i =0; i < indicatorListVBox.getChildren().size(); i++) {
				HBox listHBox = (HBox) indicatorListVBox.getChildren().get(i);
				ComboBox<IndicatorCard> comboBox = (ComboBox<IndicatorCard>) listHBox.getChildren().get(0);
				if(i <cards.size()) comboBox.getSelectionModel().select(cards.get(i));
			}
		}
	}
	
	private boolean areExistingSelectedIndicators() {
		File selectedIndicatorsFile = new File(guidDirectory.getPath() + "\\selectedIndicatorIds.txt");
		if(selectedIndicatorsFile.exists()) {
			return true;
		}
		return false;
	}
	
	private void writeSelectedIndicators(ArrayList<IndicatorCard> selectedIndicatorCards) {
		if(selectedIndicatorCards != null && selectedIndicatorCards.size()>0) {
			File selectedIndicatorsFile = new File(guidDirectory.getPath() + "\\selectedIndicatorIds.txt");
			try {
				PrintWriter printWriter = new PrintWriter(selectedIndicatorsFile);
				for(IndicatorCard card: selectedIndicatorCards) {
					printWriter.println(card.getId());
				}
				printWriter.close();
			} catch (FileNotFoundException e) {
			}
		}
	}
	
	private ArrayList<IndicatorCard> getSelectedIndicatorCards(){
		ArrayList<IndicatorCard> cards = new ArrayList<IndicatorCard>();
		for (int i = 0; i < indicatorListVBox.getChildren().size(); i++) {
			HBox child = (HBox) indicatorListVBox.getChildren().get(i);
			ComboBox<IndicatorCard> comboBox = (ComboBox<IndicatorCard>) child.getChildren().get(0);
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
			ComboBox<IndicatorCard> comboBox = (ComboBox<IndicatorCard>) child.getChildren().get(0);
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
	
	private Pane loadNoteBoard_LinearRankingController(ArrayList<IndicatorCard> selectedIndicatorCards, boolean isNew) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoard_LinearRanking noteBoardContentController = new NoteBoard_LinearRanking(engagementActionController.getApp(), engagementActionController.getCurrentGoal() ,engagementActionController.getCurrentSelectedERBContentItem(), selectedIndicatorCards);
			fxmlLoader.setController(noteBoardContentController);
			VBox root = fxmlLoader.load();
			noteBoardContentController.setUpNoteBoard(1);
			if(isNew) {
				noteBoardContentController.loadNoteBoardNew();
			} else {
				noteBoardContentController.loadNoteBoardExisting();
			}
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
