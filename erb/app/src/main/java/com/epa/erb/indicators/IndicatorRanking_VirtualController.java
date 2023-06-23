package com.epa.erb.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class IndicatorRanking_VirtualController implements Initializable {

	@FXML
	VBox vBox;
	@FXML
	Label titleLabel;	
	@FXML
	HBox rankingHBox;
	@FXML
	Button saveButton;
	@FXML
	VBox indicatorCardVBox;
	@FXML
	ScrollPane rankingScrollPane;
	@FXML
	ComboBox<String> indicatorChoiceBox;
	
	private String loadedGuid = new String();
	private FileHandler fileHandler = new FileHandler();
	
	private App app;
	public IndicatorRanking_VirtualController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		indicatorChoiceBox.setOnAction(e-> choiceBoxAction());
		fillIndicatorChoiceBox();
		indicatorChoiceBox.getSelectionModel().select(0);
		
		setDrag_IndicatorCardVBox(indicatorCardVBox);
		setDrag_RankingHBox(rankingHBox);
		
		titleLabel.setText("Indicator Resilience Ranking");
		rankingScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
		    double newWidth = newVal.doubleValue()-10.0;
		    rankingHBox.setMinWidth(newWidth);
		});
	}
	
	public void loadDataFromPreviousSession(String guid) {
		indicatorCardVBox.getChildren().clear();
		rankingHBox.getChildren().clear();
		setLoadedGuid(guid);
		System.out.println("Loaded guid " + guid);
		
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<IndicatorCard> bankedCards = iSDP.getSavedBankedIndicatorCards_VirtualRanking(guid);
		ArrayList<IndicatorCard> rankedCards = iSDP.getSavedRankedIndicatorCards_VirtualRanking(guid);

		for(IndicatorCard card: bankedCards) {
			try {
				Pane cVBox = loadIndicatorCard(card);
				indicatorCardVBox.getChildren().add(cVBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		for(IndicatorCard card: rankedCards) {
			try {
				Pane cVBox = loadIndicatorCard(card);
				rankingHBox.getChildren().add(cVBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	public void loadDataFromIndicatorSelection() {
		setLoadedGuid(new String());
		System.out.println("Indicator guid " + getLoadedGuid());
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<IndicatorCard> cards =  iSDP.getSavedSelectedIndicatorCards_Virtual();
		for(IndicatorCard card: cards) {
			try {
				Pane cVBox = loadIndicatorCard(card);
				indicatorCardVBox.getChildren().add(cVBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	@FXML
	public void loadPreviousDataButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorRanking_Virtual_PreviousDataSelection.fxml"));
			IndicatorRanking_Virtual_PreviousDataSelectionController iRVPDC = new IndicatorRanking_Virtual_PreviousDataSelectionController(app, this);
			fxmlLoader.setController(iRVPDC);
			VBox root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Load previous indicator ranking data");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void choiceBoxAction() {
		String indicatorSystemSelected = indicatorChoiceBox.getSelectionModel().getSelectedItem();
		indicatorCardVBox.getChildren().clear();
		IndicatorSaveDataParser iSDP = new IndicatorSaveDataParser(app);
		ArrayList<IndicatorCard> cards = iSDP.getSavedSelectedIndicatorCards_Virtual();

		ArrayList<String> rankedIds = getIndicatorCardIdsInRanked();
		for (IndicatorCard indicatorCard : cards) {
			if (!rankedIds.contains(indicatorCard.getId())) {
				if (indicatorSystemSelected.contentEquals("All") || indicatorCard.getSystem().toLowerCase().contains(indicatorSystemSelected.toLowerCase())) {
					VBox cardVBox = (VBox) loadIndicatorCard(indicatorCard);
					indicatorCardVBox.getChildren().add(cardVBox);
				}
			}
		}
	}
	
	private void fillIndicatorChoiceBox() {		
		indicatorChoiceBox.getItems().add("All");
		indicatorChoiceBox.getItems().add("Social");
		indicatorChoiceBox.getItems().add("Built");
		indicatorChoiceBox.getItems().add("Natural");

		indicatorChoiceBox.setCellFactory((Callback<ListView<String>, ListCell<String>>) new Callback<ListView<String>, ListCell<String>>() {
		    @Override
		    public ListCell<String> call(ListView<String> p) {
		        return new ListCell<String>() {
		            @Override
		            protected void updateItem(String item, boolean empty) {
		                super.updateItem(item, empty);
		                setText(item);
		                if (item == null || empty) {
		                    setGraphic(null);
		                } else {
	                		Pane pane = new Pane();
	                		pane.setPrefHeight(20.0);
		            		pane.setPrefWidth(20.0);
		                	if(item == "All") {
			            		pane.setStyle("-fx-background-color: #ECECEC");
		                	} else if(item == "Social") {
			            		pane.setStyle("-fx-background-color: #A6D0E2");
		            		} else if (item == "Built") {
			            		pane.setStyle("-fx-background-color: #F0C485");
		            		} else if (item == "Natural") {
			            		pane.setStyle("-fx-background-color: #CBE2A6");
		            		}
		            		setGraphic(pane);
		                }
		            }
		        };
		    }
		});
	}
	
	@FXML
	public void quadrantButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorSorting_Virtual.fxml"));
			IndicatorSorting_VirtualController iSV = new IndicatorSorting_VirtualController(app);
			iSV.initCards(null); //TODO: Fix here
			fxmlLoader.setController(iSV);
			VBox root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Indicator Sorting");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void saveButtonAction() {
		String guid = app.generateGUID();
		if(getLoadedGuid().length()>0) {
			guid = getLoadedGuid();
		}
		setLoadedGuid(guid);
		System.out.println("GUID for saving " + guid);
		File rankingVirtualDir = createIndicatorsRankingVirtualDir();
		File guidDir = new File(rankingVirtualDir + "\\" + guid);
		if(!guidDir.exists()) guidDir.mkdir();
		File virtualRankedCardsFile = new File(guidDir + "\\Ranking_CardsRanked_Virtual.txt");
		File virtualBankedCardsFile = new File(guidDir + "\\Ranking_CardsBanked_Virtual.txt");
		writeRankedIndicatorIds(virtualRankedCardsFile);
		writeBankedIndicatorIds(virtualBankedCardsFile);
	}
		
	private File createIndicatorsRankingVirtualDir() {
		File indicatorsDir = fileHandler.getIndicatorsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
		if(!indicatorsDir.exists()) {
			indicatorsDir.mkdir();
		}
		File rankingVirtualDir = new File(indicatorsDir + "\\Ranking_Virtual");
		if(!rankingVirtualDir.exists()) {
			rankingVirtualDir.mkdir();
		}
		return rankingVirtualDir;
	}

	private Pane loadIndicatorCard(IndicatorCard card) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorCard.fxml"));
			IndicatorCardController iCController = new IndicatorCardController(card);
			fxmlLoader.setController(iCController);
			VBox cVBox = fxmlLoader.load();
			cVBox.setPrefWidth(70);
			cVBox.setMaxWidth(70);
			cVBox.setMinWidth(70);
			cVBox.setPrefHeight(90);
			cVBox.setMinHeight(90);
			cVBox.setMaxHeight(90);
			cVBox.setId(card.getId());
			iCController.turnOnMouseClicked();
			iCController.setDrag_IndicatorCard(cVBox);
			iCController.setColorAndImage();
			iCController.removeImageView();
			iCController.addTextForRanking();
			return cVBox;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private ArrayList<String> getIndicatorCardIdsInBank(){
		ArrayList<String> indicatorIds = new ArrayList<String>();
		for(int i =0; i < indicatorCardVBox.getChildren().size();i++) {
			VBox indicatorVBox = (VBox) indicatorCardVBox.getChildren().get(i);
			if(indicatorVBox!= null) indicatorIds.add(indicatorVBox.getId());
		}
		return indicatorIds;
	}
	
	private ArrayList<String> getIndicatorCardIdsInRanked(){
		ArrayList<String> indicatorIds = new ArrayList<String>();
		if(rankingHBox.getChildren().size() > 0) {
		for(int i =0; i < rankingHBox.getChildren().size();i++) {
			VBox indicatorVBox = (VBox) rankingHBox.getChildren().get(i);
			if(indicatorVBox!= null) indicatorIds.add(indicatorVBox.getId());
		}
		}
		return indicatorIds;
	}
	
	public void writeRankedIndicatorIds(File file) {
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (String id : getIndicatorCardIdsInRanked()) {
				printWriter.println(id);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void writeBankedIndicatorIds(File file) {
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (String id : getIndicatorCardIdsInBank()) {
				printWriter.println(id);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	protected void setDrag_IndicatorCardVBox(Pane p) {
		p.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		p.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				Pane target = p;
				Pane source = (Pane) event.getGestureSource();
				VBox sourceVBox = (VBox) source;
				VBox targetVBox = (VBox) target;
				if (targetVBox.getChildren().contains(sourceVBox)) { // Reorder
					int targetIndex = targetVBox.getChildren().size() - 1;
					targetVBox.getChildren().remove(sourceVBox);
					targetVBox.getChildren().add(targetIndex, sourceVBox);
				} else {
					targetVBox.getChildren().add(0, sourceVBox); // Add
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		p.setOnDragDetected(event -> {
			Dragboard dragboard = p.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(p);
			event.consume();
		});
	}
	
	protected void setDrag_RankingHBox(Pane p) {
		p.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		p.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				Pane target = p;
				Pane source = (Pane) event.getGestureSource();
				VBox sourceVBox = (VBox) source;
				HBox targetHBox = (HBox) target;
				if(targetHBox.getChildren().contains(sourceVBox)) {
					int targetIndex = targetHBox.getChildren().size() -1;
					targetHBox.getChildren().remove(sourceVBox);
					targetHBox.getChildren().add(targetIndex, sourceVBox);
				} else {
					targetHBox.getChildren().add(sourceVBox);
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		p.setOnDragDetected(event -> {
			Dragboard dragboard = p.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(p);
			event.consume();
		});
	}

	public String getLoadedGuid() {
		return loadedGuid;
	}

	public void setLoadedGuid(String loadedGuid) {
		this.loadedGuid = loadedGuid;
	}

}
