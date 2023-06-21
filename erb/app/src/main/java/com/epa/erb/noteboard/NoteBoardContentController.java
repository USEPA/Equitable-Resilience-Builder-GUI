package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.engagement_action.ExternalFileUploaderController;
import com.epa.erb.goal.Goal;
import com.epa.erb.indicators.IndicatorCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.epa.erb.utility.FileHandler;
import java.io.PrintWriter;



public class NoteBoardContentController implements Initializable{

	@FXML
	Label titleLabel;
	@FXML
	VBox mainVBox;
	@FXML
	VBox centerVBox;
	@FXML
	VBox noteBoardItemVBox;
	@FXML
	HBox contentHBox;
	@FXML
	VBox vBox;
	@FXML
	ChoiceBox<String> indicatorChoiceBox;
	@FXML
	VBox dropDownVBox;
	
	//---------
	@FXML
	HBox topLabelHBox;
	@FXML
	HBox bottomLabelHBox;
	@FXML
	VBox rightLabelVBox;
	@FXML
	VBox leftLabelVBox;
	//---------
	
	@FXML
	Button nextButton;
	@FXML
	Button previousButton;
	@FXML
	Button saveBoardButton;
	
	protected File guidDirectory;
	protected File linearDirectory;
	protected File quadrantDirectory;

	
	protected App app;
	protected Goal goal;
	protected ERBContentItem erbContentItem;
	protected ArrayList<IndicatorCard> indicatorCards;
	public NoteBoardContentController(App app, Goal goal, ERBContentItem erbContentItem, ArrayList<IndicatorCard> indicatorCards ) {
		this.app = app;
		this.goal =goal;
		this.erbContentItem = erbContentItem;
		this.indicatorCards = indicatorCards;
		
		if(erbContentItem != null) {
		guidDirectory = new File(fileHandler.getGUIDDataDirectory(app.getSelectedProject(), goal) + "\\" + erbContentItem.getGuid());
		if(!guidDirectory.exists()) guidDirectory.mkdir();
		linearDirectory = new File(guidDirectory.getPath() + "\\linearRanking");
		quadrantDirectory = new File(guidDirectory.getPath() + "\\quadrantRanking");
		}

	}
	
	HBox rankedItemsHBox;
	FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(NoteBoardContentController.class);
	protected ArrayList<NoteBoardRowController> rowControllers = new ArrayList<NoteBoardRowController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		if(indicatorCards.size() ==0) {
			setExistingLinearBankedData();
		}
		fillIndicatorChoiceBox();
		setTitleText("Indicators");
		setDrag_NoteBoardItemVBox(noteBoardItemVBox);
		indicatorChoiceBox.getSelectionModel().select(0);
		indicatorChoiceBox.setOnAction(e-> indicatorChoiceBoxSelection());	
	}
	
	private boolean linearDataExists() {
		if(linearDirectory.exists() && linearDirectory.listFiles().length > 0) {
			return true;
		}
		return false;
	}
	
	private void setExistingLinearBankedData(){
		if(linearDataExists()) {
			File bankedIdsFile = new File(linearDirectory.getPath() + "\\bankedIds.txt");
			ArrayList<IndicatorCard> bankedCards = parseForIndicatorIds(bankedIdsFile);
			indicatorCards.addAll(bankedCards);
		} else {
			indicatorCards.addAll(getExistingSelectedIndicators());
		}
		addLinearBankedCards(indicatorCards);
	}
	
	private void addLinearBankedCards(ArrayList<IndicatorCard> bankedCards) {
		for (IndicatorCard card : bankedCards) {
			VBox cardVBox = (VBox) loadIndicatorCard(card);
			noteBoardItemVBox.getChildren().add(cardVBox);
		}
	}
	
	private void indicatorChoiceBoxSelection() {
		String indicatorSystemSelected = indicatorChoiceBox.getSelectionModel().getSelectedItem();
		noteBoardItemVBox.getChildren().clear();
		for(IndicatorCard indicatorCard: indicatorCards) {
			if(indicatorSystemSelected.contentEquals("All") || indicatorCard.getSystem().contentEquals(indicatorSystemSelected)) {
				VBox cardVBox = (VBox) loadIndicatorCard(indicatorCard);
				noteBoardItemVBox.getChildren().add(cardVBox);		
			}
		}
	}
	
	private void fillIndicatorChoiceBox() {
		indicatorChoiceBox.getItems().clear();
		indicatorChoiceBox.getItems().add("All");
		for(IndicatorCard iC: indicatorCards) {
			if(!indicatorChoiceBox.getItems().contains(iC.getSystem())) {
				indicatorChoiceBox.getItems().add(iC.getSystem());
			}
		}
	}
	
	public void createRows(int numberOfRows, int numberOfColumns, boolean setRowDrag) {
		if(numberOfRows>0) {
			for(int i =0; i < numberOfRows; i++) {
				HBox rowHBox = (HBox) loadNoteBoardRow(numberOfColumns, setRowDrag);
				mainVBox.getChildren().add(rowHBox);
			}
		}
	}
	
	private Parent loadNoteBoardRow(int numberOfColumns, boolean setRowDrag) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardRow.fxml"));
			NoteBoardRowController noteBoardRowController = new NoteBoardRowController(rankedItemsHBox);
			fxmlLoader.setController(noteBoardRowController);
			HBox rowHBox = fxmlLoader.load();
			noteBoardRowController.createColumns(numberOfColumns);
			if(setRowDrag) noteBoardRowController.turnOnRowDrag();
			VBox.setVgrow(rowHBox, Priority.ALWAYS);
			rowControllers.add(noteBoardRowController);
			return rowHBox;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	@FXML
	public void saveBoardButtonAction() {
		if(previousButton.isVisible() && nextButton.isVisible()) {
			linearSave();
			File rankedCardsFile = new File(linearDirectory.getPath() + "\\Linear_Ranked_Cards.txt");
			writeCardsFile(getIndicatorIdsInLinearRanked(), rankedCardsFile);
			ExternalFileUploaderController exFileUploader = new ExternalFileUploaderController(app.getEngagementActionController());
			exFileUploader.pushToUploaded(rankedCardsFile, "Indicators_Ranking");
			
		}
		else if(previousButton.isVisible() && !nextButton.isVisible()) {
			quadrantSave();
			File rankedCardsFile = new File(quadrantDirectory.getPath() + "\\Quadrant_Ranked_Cards.txt");
			writeMultiRowCardsFile(getIndicatorIdsInQuadrantRanking(),rankedCardsFile);
			ExternalFileUploaderController exFileUploader = new ExternalFileUploaderController(app.getEngagementActionController());
			exFileUploader.pushToUploaded(rankedCardsFile, "Indicators_Matrix");
		}
	}
	
	private void linearSave() {		
		if(!linearDirectory.exists()) { linearDirectory.mkdir();}
		//Get indicator cards in side
		ArrayList<String> bankedIds = getIndicatorIdsInLinearCardBank();
		File bankedIdsFile = new File(linearDirectory.getPath() + "\\bankedIds.txt");
		writeIdsFile(bankedIds, bankedIdsFile);
		//Get indicator cards ranked
		ArrayList<String> rankedIds = getIndicatorIdsInLinearRanked();
		File rankedIdsFile = new File(linearDirectory.getPath() + "\\rankedIds.txt");
		writeIdsFile(rankedIds, rankedIdsFile);
		
	}
	
	private void quadrantSave() {
		if(!quadrantDirectory.exists()) {quadrantDirectory.mkdir();}
		//Get banked cards
		ArrayList<String> bankedIds = getIndicatorIdsInQuadrantCardBank();
		File bankedIdsFile = new File(quadrantDirectory.getPath() + "\\bankedIds.txt");
		writeIdsFile(bankedIds, bankedIdsFile);
		//Get ranked cards for each row
		ArrayList<ArrayList<String>> rankedIds = getIndicatorIdsInQuadrantRanking();
		File rankedIdsFile = new File(quadrantDirectory.getPath() + "\\rankedIds.txt");
		writeMultiRowIdsFile(rankedIds,rankedIdsFile);
		
	}
	
	private void writeIdsFile(ArrayList<String> ids ,File file ) {
		if(ids != null && ids.size() > 0) {
			try {
				PrintWriter printWriter = new PrintWriter(file);
				for(String id : ids) {
					printWriter.println(id);
				}
				printWriter.close();
			} catch (FileNotFoundException e) {
			}
		} else if (ids != null && ids.size() == 0) {
			file.delete();
		}
	}
	
	private void writeCardsFile(ArrayList<String> ids ,File file) {
		if(ids != null && ids.size() > 0) {
			try {
				PrintWriter printWriter = new PrintWriter(file);
				for(String id : ids) {
					IndicatorCard card = app.findIndicatorItemForId(id);
					if(card!=null) printWriter.println(card.getSystem() + "-" + card.getIndicator());
				}
				printWriter.close();
			} catch (FileNotFoundException e) {
			}
		} else if (ids != null && ids.size() == 0) {
			file.delete();
		}
	}
	
	private void writeMultiRowIdsFile(ArrayList<ArrayList<String>> ids ,File file ) {
		if(ids != null && ids.size() > 0) {
			try {
				PrintWriter printWriter = new PrintWriter(file);
				for(ArrayList<String> row: ids) {
					StringBuilder stringBuilder = new StringBuilder();
					for(String id : row) {
						stringBuilder.append(id + "\t");
					}
					printWriter.println(stringBuilder.toString());
				}
				printWriter.close();
			} catch (FileNotFoundException e) {
			}
		} else if (ids != null && ids.size() == 0) {
			file.delete();
		}
	}
	
	private void writeMultiRowCardsFile(ArrayList<ArrayList<String>> ids ,File file ) {
		if(ids != null && ids.size() > 0) {
			try {
				PrintWriter printWriter = new PrintWriter(file);
				for(ArrayList<String> row: ids) {
					StringBuilder stringBuilder = new StringBuilder();
					for(String id : row) {
						IndicatorCard card = app.findIndicatorItemForId(id);
						if(card != null) {
							stringBuilder.append(card.getSystem() + "-" + card.getIndicator() + "\t");
						}else {
							stringBuilder.append("-" + "\t");
						}
					}
					printWriter.println(stringBuilder.toString());
				}
				printWriter.close();
			} catch (FileNotFoundException e) {
			}
		} else if (ids != null && ids.size() == 0) {
			file.delete();
		}
	}
	
	
	protected ArrayList<IndicatorCard> parseForIndicatorIds(File file){
		ArrayList<IndicatorCard> cards = new ArrayList<IndicatorCard>();
		if(file != null && file.exists()) {
			try {
				Scanner scanner = new Scanner(file);
				while(scanner.hasNextLine()) {
					String id = scanner.nextLine();
					IndicatorCard iC= app.findIndicatorItemForId(id);
					if(iC != null) cards.add(iC);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
			}
		}
		return cards;
	} 
	
	protected ArrayList<ArrayList<IndicatorCard>> parseForMultiRowIndicatorIds(File file){
		ArrayList<ArrayList<IndicatorCard>> cards = new ArrayList<ArrayList<IndicatorCard>>();
		if(file != null && file.exists()) {
			try {
				Scanner scanner = new Scanner(file);
				while(scanner.hasNextLine()) {
					ArrayList<IndicatorCard> rowOfCards = new ArrayList<IndicatorCard>();
					String idLine = scanner.nextLine().trim();
					String [] split = idLine.split("\t");
					for(int i =0; i < split.length;i++) {
						String id = split[i];
						if(id.contentEquals("-")) {
							rowOfCards.add(null);
						} else {
							IndicatorCard iC= app.findIndicatorItemForId(id);
							rowOfCards.add(iC);
						}
					}
					cards.add(rowOfCards);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
			}
		}
		return cards;
	}
	
	private ArrayList<String> getIndicatorIdsInQuadrantCardBank(){
		ArrayList<String> indicatorIds = new ArrayList<String>();
		HBox rankedHBox = (HBox) vBox.getChildren().get(1);
		if(rankedHBox.getId().contentEquals("rankedHBox")) {
			HBox rowHBox = (HBox) rankedHBox.getChildren().get(0);
			for(int i =0; i < rowHBox.getChildren().size();i++) {
				VBox indicatorVBox = (VBox) rowHBox.getChildren().get(i);
				if(indicatorVBox!= null) indicatorIds.add(indicatorVBox.getId());
			}
		}
		return indicatorIds;
	}
	
	private ArrayList<ArrayList<String>> getIndicatorIdsInQuadrantRanking() {
		ArrayList<ArrayList<String>> ids = new ArrayList<ArrayList<String>>();
		// ROW
		for (int i = 0; i < rowControllers.size(); i++) {
			ArrayList<String> indicatorIds = new ArrayList<String>();
			HBox rankedHBox = rowControllers.get(i).rowHBox;
			// CELL
			for (int j = 0; j < rankedHBox.getChildren().size(); j++) {
				HBox cellHBox = (HBox) rankedHBox.getChildren().get(j);
				if (cellHBox.getChildren().size() > 0) {
					VBox indicatorVBox = (VBox) cellHBox.getChildren().get(0);
					if (indicatorVBox != null) {
						indicatorIds.add(indicatorVBox.getId());
					}
				} else {
					indicatorIds.add("-");
				}
			}
			ids.add(indicatorIds);
		}
		return ids;
	}
	
	private ArrayList<String> getIndicatorIdsInLinearCardBank() {
		ArrayList<String> indicatorIds = new ArrayList<String>();
		for(int i =0; i < noteBoardItemVBox.getChildren().size();i++) {
			VBox indicatorVBox = (VBox) noteBoardItemVBox.getChildren().get(i);
			if(indicatorVBox!= null) indicatorIds.add(indicatorVBox.getId());
		}
		return indicatorIds;
	}
	
	private ArrayList<String> getIndicatorIdsInLinearRanked(){
		ArrayList<String> indicatorIds = new ArrayList<String>();
		if(rowControllers.size() == 1) {
			HBox rankedHBox = rowControllers.get(0).rowHBox;
			for(int i =0; i < rankedHBox.getChildren().size(); i++) {
				VBox indicatorVBox = (VBox) rankedHBox.getChildren().get(i);
				if(indicatorVBox!= null) indicatorIds.add(indicatorVBox.getId());
			}
		}
		return indicatorIds;
	}
	
	@FXML
	private void nextButtonAction() {
		if (linearDataExists() && rankedMatchSaved()) {
			try {
				linearSave();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
				NoteBoard_QuadrantRanking noteBoardContentController = new NoteBoard_QuadrantRanking(app, app.getEngagementActionController().getCurrentGoal(), erbContentItem, rowControllers, indicatorCards);
				fxmlLoader.setController(noteBoardContentController);
				VBox root = fxmlLoader.load();
				noteBoardContentController.setUpNoteBoard(4);
				noteBoardContentController.loadNoteBoardExisting();
				app.getEngagementActionController().cleanContentVBox();
				app.getEngagementActionController().addContentToContentVBox(root, true);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			try {
				linearSave();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
				NoteBoard_QuadrantRanking noteBoardContentController = new NoteBoard_QuadrantRanking(app, app.getEngagementActionController().getCurrentGoal(), erbContentItem, rowControllers,indicatorCards);
				fxmlLoader.setController(noteBoardContentController);
				VBox root = fxmlLoader.load();
				noteBoardContentController.setUpNoteBoard(4);
				noteBoardContentController.loadNoteBoardNew();
				app.getEngagementActionController().cleanContentVBox();
				app.getEngagementActionController().addContentToContentVBox(root, true);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	private boolean rankedMatchSaved() {
		ArrayList<String> rankedIds = getIndicatorIdsInLinearRanked();
		ArrayList<IndicatorCard> rankedCards = new ArrayList<IndicatorCard>();
		for(String id: rankedIds) {
			rankedCards.add(app.findIndicatorItemForId(id));
		}

		File rankedIdsFile = new File(linearDirectory.getPath() + "\\rankedIds.txt");
		ArrayList<IndicatorCard> savedCards = parseForIndicatorIds(rankedIdsFile);
		
		if(rankedCards.size() == savedCards.size()) {
			if(rankedCards.containsAll(savedCards) && savedCards.containsAll(rankedCards)) {
				return true;
			}
		}
		return false;
	}
	
	@FXML
	private void previousButtonAction() {
		if(previousButton.isVisible() && nextButton.isVisible()) {
			linearSave();
			Pane root = app.getEngagementActionController().loadIndicatorSetupFormController(erbContentItem);
			app.getEngagementActionController().cleanContentVBox();
			app.getEngagementActionController().addContentToContentVBox(root, true);
			showPreviousAlert("If new and/or different indicator cards are selected, your previously saved resilience and equity rankings will be reset.");
		} else if (previousButton.isVisible() && !nextButton.isVisible()) {
			quadrantSave();
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
				NoteBoard_LinearRanking noteBoardContentController = new NoteBoard_LinearRanking(app, goal ,erbContentItem, new ArrayList<IndicatorCard>());
				fxmlLoader.setController(noteBoardContentController);
				VBox root = fxmlLoader.load();
				noteBoardContentController.setUpNoteBoard(1);
				noteBoardContentController.loadNoteBoardExisting();
				app.getEngagementActionController().cleanContentVBox();
				app.getEngagementActionController().addContentToContentVBox(root, true);
				showPreviousAlert("If indicator cards are rearranged, your previously saved resilience and equity rankings will be reset.");
			} catch (Exception e) {
			}
		}
	}
	
	private void showPreviousAlert(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Indicators Alert");
		alert.setContentText(text);
		alert.showAndWait();
	}

	private ArrayList<IndicatorCard> getExistingSelectedIndicators() {
		ArrayList<IndicatorCard> cards = new ArrayList<IndicatorCard>();
		File selectedIndicatorsFile = new File(guidDirectory.getPath() + "\\selectedIndicatorIds.txt");
		if(selectedIndicatorsFile.exists()) {
			try {
				Scanner scanner = new Scanner(selectedIndicatorsFile);
				while(scanner.hasNextLine()) {
					String id = scanner.nextLine();
					IndicatorCard iC= app.findIndicatorItemForId(id);
					cards.add(iC);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
			}
		}
		return cards;
	}
	
	public Pane loadIndicatorCard(IndicatorCard indicatorCard) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardItem.fxml"));
			NoteBoardItem_Indicator noteBoardIndicatorItem = new NoteBoardItem_Indicator(this, indicatorCard);
			fxmlLoader.setController(noteBoardIndicatorItem);
			VBox vBox = fxmlLoader.load();
			vBox.setId(indicatorCard.getId());
			noteBoardIndicatorItem.setColorAndImage();
			noteBoardIndicatorItem.addFrontIndicatorCardText();
			vBox.setOnMouseClicked(e-> noteBoardIndicatorItem.flipCard(e));
			noteBoardIndicatorItem.setDrag_IndicatorCard(vBox);
			return vBox;
		} catch (Exception e) {
			return null;
		}
	}

	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	protected void setDrag_NoteBoardItemVBox(Pane p) {
		p.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		p.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				Pane target = p;
				Pane sourceItem = (Pane) event.getGestureSource();
				VBox sourceItemVBox = (VBox) sourceItem;
				VBox targetVBox = (VBox) target;
				if (targetVBox.getChildren().contains(sourceItemVBox)) { // Reorder
					int targetIndex = targetVBox.getChildren().size() - 1;
					targetVBox.getChildren().remove(sourceItemVBox);
					targetVBox.getChildren().add(targetIndex, sourceItemVBox);
				} else {
					targetVBox.getChildren().add(0, sourceItemVBox); // Add
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
	
	protected void hideNextButton() {
		nextButton.setVisible(false);
	}
	
	protected void showNextButton() {
		nextButton.setVisible(true);
	}
	
	protected void hidePreviousButton() {
		previousButton.setVisible(false);
	}
	
	protected void showPreviousButton() {
		previousButton.setVisible(true);
	}
	
	private void setTitleText(String text) {
		titleLabel.setText(text);
	}
	
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Label getTitleText() {
		return titleLabel;
	}

	public VBox getMainVBox() {
		return mainVBox;
	}

	public HBox getRankedItemsHBox() {
		return rankedItemsHBox;
	}

	public void setRankedItemsHBox(HBox rankedItemsHBox) {
		this.rankedItemsHBox = rankedItemsHBox;
	}

	public VBox getvBox() {
		return vBox;
	}

	public ArrayList<IndicatorCard> getIndicatorCards() {
		return indicatorCards;
	}

	public void setIndicatorCards(ArrayList<IndicatorCard> indicatorCards) {
		this.indicatorCards = indicatorCards;
	}	

}
