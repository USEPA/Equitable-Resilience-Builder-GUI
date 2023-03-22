package com.epa.erb.noteboard;

import java.util.ArrayList;

import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NoteBoard_QuadrantRanking extends NoteBoardContentController {

	private ArrayList<NoteBoardItem_Indicator> items;
	public NoteBoard_QuadrantRanking(App app, Project project, Goal goal, ERBContentItem erbContentItem,ArrayList<NoteBoardItem_Indicator> items ) {
		super(app, project, goal, erbContentItem);
		this.items = items;
	}

	public void setUpNoteBoard(int numberOfRows) {
		removeNoteBoardItemVBox();
		createRows(numberOfRows, 2);
		HBox rankedItemsHBox = createRankedNoteBoardItemHBox();
		fillRankedNoteBoardItemHBox(rankedItemsHBox);
	}
	
	private void removeNoteBoardItemVBox() {
		if(contentHBox.getChildren().contains(noteBoardItemVBox)) {
			contentHBox.getChildren().remove(noteBoardItemVBox);
		}
	}
	
	private HBox createRankedNoteBoardItemHBox() {
		HBox rankedHBox = new HBox();
		rankedHBox.setMinHeight(250.0);
		rankedHBox.setSpacing(10.0);
		vBox.getChildren().add(1, rankedHBox);
		return rankedHBox;
	}
	
	private void fillRankedNoteBoardItemHBox(HBox rankedItemsHBox) {
		for(NoteBoardItem_Indicator noteBoardItem: items) {
			VBox cardVBox = (VBox) loadIndicatorCard(noteBoardItem.getIndicatorCard());
			rankedItemsHBox.getChildren().add(cardVBox);
		}
	}
}
