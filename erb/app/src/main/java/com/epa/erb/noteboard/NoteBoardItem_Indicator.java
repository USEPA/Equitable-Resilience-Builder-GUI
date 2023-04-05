package com.epa.erb.noteboard;

import com.epa.erb.IndicatorCard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class NoteBoardItem_Indicator extends NoteBoardItemController{

	private IndicatorCard indicatorCard;
	public NoteBoardItem_Indicator(NoteBoardContentController noteBoardContentController, IndicatorCard indicatorCard) {
		super(noteBoardContentController);
		this.indicatorCard = indicatorCard;
	}
	
	protected void setColor() {
		if(indicatorCard.getSystem().contentEquals("Social Environment")) {
			noteBoardItemVBox.setStyle("-fx-background-color: " + indicatorCard.getSocialEnvironmentSystemColor());
			textFlow.setStyle("-fx-background-color: " + indicatorCard.getSocialEnvironmentSystemColor());
		} else if (indicatorCard.getSystem().contentEquals("Built Environment")) {
			noteBoardItemVBox.setStyle("-fx-background-color: " + indicatorCard.getBuiltEnvironmentSystemColor());
			textFlow.setStyle("-fx-background-color: " + indicatorCard.getBuiltEnvironmentSystemColor());
		} else if (indicatorCard.getSystem().contentEquals("Natural Environment")) {
			noteBoardItemVBox.setStyle("-fx-background-color: " + indicatorCard.getNaturalEnvironmentSystemColor());
			textFlow.setStyle("-fx-background-color: " + indicatorCard.getNaturalEnvironmentSystemColor());
		}
	}
	
	protected void setIndicatorCardText() {
		setNoteBoardItemText(getIndicatorCardText());
	}
	
	private String getIndicatorCardText() {
		return indicatorCard.getSystem() + "\n" + indicatorCard.getIndicator();
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
				VBox sourceItemVBox = (VBox) sourceItem;
				VBox targetVBox = (VBox) target;
				Pane parent = (Pane) targetVBox.getParent();
				int targetIndex = parent.getChildren().indexOf(targetVBox);
				parent.getChildren().remove(sourceItemVBox);
				parent.getChildren().add(targetIndex, sourceItemVBox);
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
	
	public IndicatorCard getIndicatorCard() {
		return indicatorCard;
	}
}
