package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NoteBoardRowController implements Initializable {

	@FXML
	HBox mainHBox;
	@FXML
	HBox rowHBox;
	
	public NoteBoardRowController() {

	}
	
//	private ArrayList<NoteBoardItemController> listOfPostItNoteControllers = new ArrayList<NoteBoardItemController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
				System.out.println("Target row = " + target);
				System.out.println("Source row = " + sourceItem.getId());
				VBox sourceItemVBox = (VBox) sourceItem;
				HBox targetHBox = (HBox) target;
				targetHBox.getChildren().add(sourceItemVBox);
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
	
//	private ArrayList<NoteBoardItemController> orderListOfPostItNoteControllers() {
//		listOfPostItNoteControllers = sortArray(listOfPostItNoteControllers);
//		return listOfPostItNoteControllers;
//	}
	
//	public ArrayList<NoteBoardItemController> sortArray(ArrayList<NoteBoardItemController> inputArray) {
//		for (int i = 1; i < inputArray.size(); i++) {
//			NoteBoardItemController postItNoteController = inputArray.get(i);
//			int key = inputArray.get(i).getPostItNoteIndex(this);
//			for (int j = i - 1; j >= 0; j--) {
//				if (key < inputArray.get(j).getPostItNoteIndex(this)) {
//					inputArray.set(j + 1, inputArray.get(j));
//					if (j == 0) {
//						inputArray.set(0, postItNoteController);
//					}
//				} else {
//					inputArray.set(j + 1, postItNoteController);
//					break;
//				}
//			}
//		}
//		return inputArray;
//	}

	public HBox getCategoryHBox() {
		return mainHBox;
	}

	public HBox getPostItHBox() {
		return rowHBox;
	}

//	public void setListOfPostItNoteControllers(ArrayList<NoteBoardItemController> listOfPostItNoteControllers) {
//		this.listOfPostItNoteControllers = listOfPostItNoteControllers;
//	}
	
//	public ArrayList<NoteBoardItemController> getListOfPostItNoteControllers() {
//		return orderListOfPostItNoteControllers();
//	}	
	
}
