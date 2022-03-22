package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class WhiteBoardController implements Initializable{

	@FXML
	ScrollPane scrollPane;
	@FXML
	VBox scrollPaneVBox;
	@FXML
	HBox mainHBox;
	@FXML
	TextField textField;
	@FXML
	ColorPicker colorPicker;
	@FXML
	Button createButton;
	@FXML
	VBox mainVBox;


		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colorPicker.getStylesheets().add(getClass().getResource("/noteboard/ColorPicker.css").toString());
		fillCategories();
		createCategoryRows();
	}
	
	ArrayList<String> categories = new ArrayList<String>();
	
	public WhiteBoardController() {
	
	}
	
	@FXML
	public void createButtonAction() {
		String postItText = getPostItText();
		if(postItText != null) {
			Color selectedColor = colorPicker.getValue();
			String colorAsHex = parseColorAsHex(selectedColor);
			createNewPostItNote(postItText, colorAsHex);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(null);
			alert.setContentText("Please enter text for your post-it note.");
			alert.showAndWait();
		}
	}
	
	public void fillCategories() {
		categories.add("Flood");
		categories.add("Heat");
		categories.add("Radiological Disease");
		categories.add("Pandemic");
	}
	
	public void createCategoryRows() {
		for (String category : categories) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/CategorySection.fxml"));
				CategorySectionController categorySectionController = new CategorySectionController((category + ":"));
				fxmlLoader.setController(categorySectionController);
				HBox catHBox = fxmlLoader.load();
				HBox postItHBox = (HBox) catHBox.getChildren().get(1);
				setDrag(postItHBox);
				categorySectionController.initCategorySection();
				VBox.setVgrow(catHBox, Priority.ALWAYS);
				mainVBox.getChildren().add(catHBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createNewPostItNote(String postItText, String colorAsHex) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/PostItNote.fxml"));
			PostItNoteController postItNoteController = new PostItNoteController(postItText, colorAsHex);
			fxmlLoader.setController(postItNoteController);
			VBox postItNoteVBox = fxmlLoader.load();
			setDrag(postItNoteVBox);
			postItNoteController.initPostItNote();
			mainVBox.getChildren().add(0, postItNoteVBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getPostItText() {
		String postItString = textField.getText();
		if(postItString == null || postItString.length() <=0) {
			return null;
		}
		return postItString;
	}
	
	public String parseColorAsHex(Color color) {
		String colorAsString = color.toString();
		String colorAsHexString = colorAsString.replaceAll("0x", "");
		colorAsHexString = colorAsHexString.substring(0, colorAsHexString.length()-2);
		return colorAsHexString;
	}
	
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	
	void setDrag(Pane p) {
		p.setOnDragOver(event-> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		p.setOnDragDropped(event-> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasString()) {		
				System.out.println("Pane: " + p);
				
				Pane parent = (Pane) p.getParent();
				System.out.println("Parent: " + parent);
				
				Object source = event.getGestureSource();
				System.out.println("Source: " + source);
				
				int sourceIndex = parent.getChildren().indexOf(source);		
				System.out.println("Source index: " + sourceIndex);
				
				int targetIndex = parent.getChildren().indexOf(p);				
				System.out.println("Target index: " + targetIndex);
				
				List<Node> nodes = new ArrayList<>(parent.getChildren());
				if (sourceIndex >= 0) {
					if (sourceIndex < targetIndex) {
						Collections.rotate(nodes.subList(sourceIndex, targetIndex + 1), -1);
					} else {
						Collections.rotate(nodes.subList(targetIndex, sourceIndex + 1), 1);
					}
				} else {
					p.getChildren().add((VBox) source);
				}
				parent.getChildren().clear();
				parent.getChildren().addAll(nodes);
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		p.setOnDragDetected(event-> {
			System.out.println("HERE 1");
			Dragboard dragboard = p.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(p);
			event.consume();
		});
	}
}
