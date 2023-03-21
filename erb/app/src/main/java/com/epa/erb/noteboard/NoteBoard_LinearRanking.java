package com.epa.erb.noteboard;

import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.IndicatorCard;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NoteBoard_LinearRanking extends NoteBoardContentController {

	public NoteBoard_LinearRanking(App app, Project project, Goal goal, ERBContentItem erbContentItem) {
		super(app, project, goal, erbContentItem);
	}
	
	public void setUpNoteBoard(int numberOfRows) {
		removeTopLabelHBox();
		removeBottomLabelHBox();
		createRows(numberOfRows);
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
		for(IndicatorCard card: app.getIndicatorCards()) {
			VBox cardVBox = (VBox) loadIndicatorCard(card.getIndicator());
			noteBoardItemVBox.getChildren().add(cardVBox);
		}
	}
}
