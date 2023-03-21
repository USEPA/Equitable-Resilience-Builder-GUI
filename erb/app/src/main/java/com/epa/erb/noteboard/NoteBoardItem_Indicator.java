package com.epa.erb.noteboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NoteBoardItem_Indicator extends NoteBoardItemController{

	public NoteBoardItem_Indicator(NoteBoardContentController noteBoardContentController) {
		super(noteBoardContentController);
		// TODO Auto-generated constructor stub
	}
	
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	protected void setDrag_IndicatorCard(Pane p) {
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
				System.out.println("Target item = " + target);
				System.out.println("Source item = " + sourceItem.getId());
				VBox sourceItemVBox = (VBox) sourceItem;
				VBox targetVBox = (VBox) target;
				VBox parentVBox = (VBox) targetVBox.getParent();
				System.out.println("Parent = " + parentVBox);
				int targetIndex = parentVBox.getChildren().indexOf(targetVBox);
				System.out.println("Target Index = " + targetIndex);
				parentVBox.getChildren().remove(sourceItemVBox);
				parentVBox.getChildren().add(targetIndex, sourceItemVBox);
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
}
