package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NoteBoardRowController implements Initializable {

	@FXML
	HBox mainHBox;
	@FXML
	HBox rowHBox;
	
	HBox rankedItemsHBox;
	public NoteBoardRowController(HBox rankedItemsHBox) {
		this.rankedItemsHBox = rankedItemsHBox;
	}
	
	protected void createColumns(int numberOfColumns) {
		if (numberOfColumns > 1) {
			for(int i =0; i < numberOfColumns; i++) {
				HBox columnHBox = new HBox();
//				columnHBox.setStyle("-fx-border-color: #E4E4E4");
				setDrag_Column(columnHBox);
				HBox.setHgrow(columnHBox, Priority.ALWAYS);
				columnHBox.setPrefHeight(225);
				columnHBox.setPrefWidth(175);
				columnHBox.setAlignment(Pos.CENTER);
				rowHBox.getChildren().add(columnHBox);
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}	
	
	public void turnOnRowDrag() {
		setDrag_Row(rowHBox);
	}
	
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	protected void setDrag_Row(Pane p) {
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
				HBox targetHBox = (HBox) target;
				if(targetHBox.getChildren().contains(sourceItemVBox)) {
					int targetIndex = targetHBox.getChildren().size() -1;
					targetHBox.getChildren().remove(sourceItemVBox);
					targetHBox.getChildren().add(targetIndex, sourceItemVBox);
				} else {
					targetHBox.getChildren().add(sourceItemVBox);
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

	protected void setDrag_Column(Pane p) {
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
				Pane sourceParent = (Pane) source.getParent();	
				
				if (sourceParent.getId() != null && (sourceParent.getId().contentEquals("rowHBox"))) {
					if (target.toString().contains("HBox")) {
						if (target.getChildren().size() > 0) {
							VBox existingVBoxChild = (VBox) target.getChildren().get(0);
							sourceParent.getChildren().add(existingVBoxChild);
						}
						target.getChildren().add(source);
					}
				} else {
					if (target.getChildren().size() > 0) {
						VBox existingVBoxChild = (VBox) target.getChildren().get(0);
						sourceParent.getChildren().clear();
						sourceParent.getChildren().add(existingVBoxChild);
					}
					target.getChildren().clear();
					target.getChildren().add(source);
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

	public HBox getCategoryHBox() {
		return mainHBox;
	}

	public HBox getPostItHBox() {
		return rowHBox;
	}	
}
