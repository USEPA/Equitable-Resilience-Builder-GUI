package com.epa.erb.noteboard;

import java.util.ArrayList;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class NoteBoard_QuadrantRanking extends NoteBoardContentController {

	ArrayList<NoteBoardRowController> linearRowControllers = new ArrayList<NoteBoardRowController>();
	public NoteBoard_QuadrantRanking(App app, Project project, Goal goal, ERBContentItem erbContentItem, ArrayList<NoteBoardRowController> linearRowControllers) {
		super(app, project, goal, erbContentItem);
		this.linearRowControllers = linearRowControllers;
	}

	public void setUpNoteBoard(int numberOfRows) {
		removeNoteBoardItemVBox();
		HBox rankedItemsHBox = createRankedNoteBoardItemHBox();
		setRankedItemsHBox(rankedItemsHBox);
		createRows(numberOfRows, 4, false);
		fillRankedNoteBoardItemHBox(rankedItemsHBox);
		setNewDrag(rankedItemsHBox);
		contentHBox.getChildren().remove(dropDownVBox);
	}
	
	private void removeNoteBoardItemVBox() {
		if(contentHBox.getChildren().contains(noteBoardItemVBox)) {
			contentHBox.getChildren().remove(noteBoardItemVBox);
		}
	}
	
	private HBox createRankedNoteBoardItemHBox() {
		HBox rankedHBox = new HBox();
		rankedHBox.setMinHeight(250.0);
		rankedHBox.setId("rankedHBox");
		rankedHBox.setSpacing(10.0);
		vBox.getChildren().add(1, rankedHBox);
		return rankedHBox;
	}
	
	private void setNewDrag(HBox rankedItemsHBox) {
		Pane hBox = (Pane) rankedItemsHBox.getChildren().get(0);
		for(Node n: hBox.getChildren() ) {
			Pane p = (Pane) n;
			setDrag_IndicatorCardQuadrant(p);
		}
	}
	
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	protected void setDrag_IndicatorCardQuadrant(Pane p) {
		p.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		p.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				Pane target = p;
				Pane source =  (Pane) event.getGestureSource();
				Pane sourceParent = (Pane) source.getParent();
				Pane targetParent =  (Pane) target.getParent();

				if (sourceParent.getId() != null && (sourceParent.getId().contentEquals("rowHBox"))) {
					if ((targetParent.getId() != null) && targetParent.getId().contentEquals("rowHBox")) {
						//Swap 2 cards: both inside hbox
						int sourceIndex = sourceParent.getChildren().indexOf(source);
						int targetIndex = targetParent.getChildren().indexOf(target);
						sourceParent.getChildren().remove(source);
						targetParent.getChildren().remove(target);
						if(sourceIndex < targetIndex) {
							sourceParent.getChildren().add(sourceIndex, target);
							targetParent.getChildren().add(targetIndex, source);
						}else {
							targetParent.getChildren().add(targetIndex, source);
							sourceParent.getChildren().add(sourceIndex, target);
						}
					} else {
						//Swap 2 cards: one inside hbox, one inside matrix cell
						if(targetParent.getChildren().size()>0) {
							sourceParent.getChildren().add(targetParent.getChildren().get(0));
						}
						targetParent.getChildren().add(source);
					}
				} else {
					//Swap 2 cards: both inside separate cells in matrix
					targetParent.getChildren().clear();
					sourceParent.getChildren().clear();
					targetParent.getChildren().add(source);
					sourceParent.getChildren().add(target);
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
	
	private void fillRankedNoteBoardItemHBox(HBox rankedItemsHBox) {
		if(linearRowControllers.size() == 1) {
			HBox.setHgrow(linearRowControllers.get(0).rowHBox, Priority.ALWAYS);
			rankedItemsHBox.getChildren().add(linearRowControllers.get(0).rowHBox);
		}
	}
}
