package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class NoteBoardContentController implements Initializable{

	@FXML
	VBox mainVBox;
	@FXML
	Pane note;
	
	ArrayList<String> categories = new ArrayList<String>();
	
	public NoteBoardContentController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setDrag(note);
		fillCategories();
		createCategoryRows();
	}
	
	public void fillCategories() {
		categories.add("Flood");
		categories.add("Heat");
		categories.add("Radiological Disease");
		categories.add("Pandemic");
	}
	
	@FXML
	public void noteClicked() {
		
	}
	
	public void createCategoryRows() {
		for (String category : categories) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/CategorySection.fxml"));
				CategorySectionController categorySectionController = new CategorySectionController((category));
				fxmlLoader.setController(categorySectionController);
				HBox catHBox = fxmlLoader.load();
				HBox postItHBox = categorySectionController.getPostItHBox(); 
				setDrag(postItHBox);
				categorySectionController.initCategorySection();
				VBox.setVgrow(catHBox, Priority.ALWAYS);
				mainVBox.getChildren().add(catHBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	int indexToMove = -1;
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	void setDrag(Pane p) {
		p.setOnDragOver(event-> {
			event.acceptTransferModes(TransferMode.MOVE);
			if (event.getTarget() != null) {
				if(event.getTarget().toString().contains("TextFlow")) {
					TextFlow textFlow = (TextFlow) event.getTarget();
					VBox noteVBox = (VBox) textFlow.getParent().getParent().getParent().getParent();
					if(noteVBox.getId().contentEquals("postedNote")) {
						Pane parentPane = (Pane) noteVBox.getParent();
						indexToMove = parentPane.getChildren().indexOf(noteVBox);
					}
				}
			}
			event.consume();
		});
		p.setOnDragDropped(event-> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasString()) {
				Pane pane = p;
				Pane sourcePane = (Pane) event.getGestureSource();
				//Adding a new post it
				if(sourcePane.getId() != null && sourcePane.getId().contentEquals("note") && pane.getId() != null && pane.getId().contentEquals("postItHBox")) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/PostItNote.fxml"));
						PostItNoteController postItNoteController = new PostItNoteController();
						fxmlLoader.setController(postItNoteController);
						Pane postedPane = fxmlLoader.load();	
						setDrag(postedPane);
						pane.getChildren().add(postedPane);
					}catch (Exception e) {
						e.printStackTrace();
					}
					// Moving a post it
				} else if (sourcePane.getId() != null && sourcePane.getId().contentEquals("postedNote") && pane.getId() != null && pane.getId().contentEquals("postItHBox")) {
					if (indexToMove > -1) {
						pane.getChildren().remove(sourcePane);
						pane.getChildren().add(indexToMove, sourcePane);
					}
				//Moving a post it
				} else if(sourcePane.getId() != null && sourcePane.getId().contentEquals("postedNote") && pane.getId() != null && pane.getId().contentEquals("postedNote")) {
					Pane parentPane = (Pane) sourcePane.getParent();
					TextFlow textFlow = (TextFlow) event.getTarget();
					VBox noteVBox = (VBox) textFlow.getParent().getParent().getParent().getParent();
					int targetIndex = parentPane.getChildren().indexOf(noteVBox);
					parentPane.getChildren().remove(sourcePane);
					parentPane.getChildren().add(targetIndex, sourcePane);
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		p.setOnDragDetected(event-> {
			Dragboard dragboard = p.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(p);
			event.consume();
		});
	}

}
