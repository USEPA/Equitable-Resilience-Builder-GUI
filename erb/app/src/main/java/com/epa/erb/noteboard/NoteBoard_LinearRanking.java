package com.epa.erb.noteboard;

import java.io.File;
import java.util.ArrayList;

import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.goal.Goal;
import com.epa.erb.indicators.IndicatorCard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NoteBoard_LinearRanking extends NoteBoardContentController {

	public NoteBoard_LinearRanking(App app, Goal goal, ERBContentItem erbContentItem, ArrayList<IndicatorCard> indicatorCards) {
		super(app, goal, erbContentItem, indicatorCards);
	}
	
	public void loadNoteBoardNew() {
		createIndicatorCards();
		deleteExistingLinearData();
	}
	
	public void loadNoteBoardExisting() {
		setExistingLinearRankedData();
	}
	
	private void deleteExistingLinearData() {
		fileHandler.deleteDirectory(linearDirectory);
	}
	
	protected void setExistingLinearRankedData() {
		if(linearDirectory.exists()) {
			File rankedIdsFile = new File(linearDirectory.getPath() + "\\rankedIds.txt");
			ArrayList<IndicatorCard> rankedCards = parseForIndicatorIds(rankedIdsFile);
			addLinearRankedCards(rankedCards);
		}
	}
	
	private void addLinearRankedCards(ArrayList<IndicatorCard> rankedCards) {
		for (IndicatorCard card : rankedCards) {
			VBox cardVBox = (VBox) loadIndicatorCard(card);
			if(rowControllers.size() == 1) {
				HBox rankedHBox = rowControllers.get(0).rowHBox;
				rankedHBox.getChildren().add(cardVBox);
			}
		}
	}
	
	public void setUpNoteBoard(int numberOfRows) {
		removeTopLabelHBox();
		removeBottomLabelHBox();
		createRows(numberOfRows, 1, true);
		showPreviousButton();
		showNextButton();
	}
	
	private void removeTopLabelHBox() {
		if(centerVBox.getChildren().contains(topLabelHBox)) {
			centerVBox.getChildren().remove(topLabelHBox);
		}
	}
	
	private void removeBottomLabelHBox() {
		if(centerVBox.getChildren().contains(bottomLabelHBox)) {
			centerVBox.getChildren().remove(bottomLabelHBox);
		}
	}
	
	private void createIndicatorCards() {
		for(IndicatorCard card: indicatorCards) {
			VBox cardVBox = (VBox) loadIndicatorCard(card);
			noteBoardItemVBox.getChildren().add(cardVBox);
		}
	}

	public ArrayList<IndicatorCard> getIndicatorCards() {
		return indicatorCards;
	}

	public void setIndicatorCards(ArrayList<IndicatorCard> indicatorCards) {
		this.indicatorCards = indicatorCards;
	}
	
	
}
