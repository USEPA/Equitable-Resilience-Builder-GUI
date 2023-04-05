package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.IndicatorCard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


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
	
	protected App app;
	private ERBContentItem erbContentItem;
	protected ArrayList<IndicatorCard> indicatorCards;
	public NoteBoardContentController(App app, ERBContentItem erbContentItem, ArrayList<IndicatorCard> indicatorCards ) {
		this.app = app;
		this.erbContentItem = erbContentItem;
		this.indicatorCards = indicatorCards;
	}
	
	HBox rankedItemsHBox;
	private Logger logger = LogManager.getLogger(NoteBoardContentController.class);
	protected ArrayList<NoteBoardRowController> rowControllers = new ArrayList<NoteBoardRowController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillIndicatorChoiceBox();
		setTitleText(erbContentItem.getLongName());
		setDrag_NoteBoardItemVBox(noteBoardItemVBox);
		indicatorChoiceBox.getSelectionModel().select(0);
		indicatorChoiceBox.setOnAction(e-> indicatorChoiceBoxSelection());		
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
		
	}
	
	@FXML
	private void nextButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoard_QuadrantRanking noteBoardContentController = new NoteBoard_QuadrantRanking(app, erbContentItem, rowControllers, indicatorCards);
			fxmlLoader.setController(noteBoardContentController);
			VBox root = fxmlLoader.load();
			noteBoardContentController.setUpNoteBoard(4);
			app.getEngagementActionController().cleanContentVBox();
			app.getEngagementActionController().addContentToContentVBox(root, true);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	protected Pane loadIndicatorCard(IndicatorCard indicatorCard) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardItem.fxml"));
			NoteBoardItem_Indicator noteBoardIndicatorItem = new NoteBoardItem_Indicator(this, indicatorCard);
			fxmlLoader.setController(noteBoardIndicatorItem);
			VBox vBox = fxmlLoader.load();
			vBox.setId(indicatorCard.getId());
			noteBoardIndicatorItem.setColor();
			noteBoardIndicatorItem.setIndicatorCardText();
			noteBoardIndicatorItem.setDrag_IndicatorCard(vBox);
			return vBox;
		} catch (Exception e) {
			e.printStackTrace();
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
	
	private void setTitleText(String text) {
		titleLabel.setText(text);
	}
	
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
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
