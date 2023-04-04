package com.epa.erb.noteboard;

import java.util.ArrayList;

import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.IndicatorCard;
import javafx.scene.layout.VBox;

public class NoteBoard_LinearRanking extends NoteBoardContentController {

	public NoteBoard_LinearRanking(App app, ERBContentItem erbContentItem, ArrayList<IndicatorCard> indicatorCards) {
		super(app, erbContentItem, indicatorCards);
	}
	
	public void setUpNoteBoard(int numberOfRows) {
		removeTopLabelHBox();
		removeBottomLabelHBox();
		createRows(numberOfRows, 1, true);
		createIndicatorCards();
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
