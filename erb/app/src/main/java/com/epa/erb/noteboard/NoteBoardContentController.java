package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Constants;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
	Pane layer1Pane;
	@FXML
	Pane layer2Pane;
	@FXML
	Pane layer3Pane;
	@FXML
	Pane layer4Pane;
	@FXML
	Pane note; //layer 5
	
	public NoteBoardContentController() {
		
	}
	
	private ArrayList<String> categories = new ArrayList<String>();
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(NoteBoardContentController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setDrag(note);
		fillCategories();
		createCategoryRows();
		initializeStyle();
	}
	
	private void initializeStyle() {
		layer1Pane.setStyle("-fx-background-color: " + constants.getLayer1ColorString() + ";");
		layer2Pane.setStyle("-fx-background-color: " + constants.getLayer2ColorString() + ";");
		layer3Pane.setStyle("-fx-background-color: " + constants.getLayer3ColorString() + ";");
		layer4Pane.setStyle("-fx-background-color: " + constants.getLayer4ColorString() + ";");
		note.setStyle("-fx-background-color: " + constants.getLayer5ColorString() + ";");
	}
	
	private void fillCategories() {
		categories.add("Flood");
		categories.add("Heat");
		categories.add("Radiological Disease");
		categories.add("Pandemic");
	}
	
	@FXML
	public void noteClicked() {
		
	}
	
	private void createCategoryRows() {
		for (String category : categories) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/CategorySection.fxml"));
				CategorySectionController categorySectionController = new CategorySectionController((category));
				fxmlLoader.setController(categorySectionController);
				HBox catHBox = fxmlLoader.load();
				setDrag(categorySectionController.getPostItHBox());
				categorySectionController.initCategorySection();
				VBox.setVgrow(catHBox, Priority.ALWAYS);
				mainVBox.getChildren().add(catHBox);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	private int indexToMove = -1;
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	private void setDrag(Pane p) {
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
