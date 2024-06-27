package com.epa.erb.finalReport;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class FinalReportOrdering implements Initializable{

	private ListView<FinalReportItem> selectedListView;
	private FinalReportSelectionController controller;
	public FinalReportOrdering(ListView<FinalReportItem> selectedListView, FinalReportSelectionController controller) {
		this.selectedListView = selectedListView;
		this.controller = controller;
	}
	
	@FXML
	ListView<FinalReportItem> listView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView.setCellFactory(lv -> createListCell());
		fillListView();
	}
	
	private void fillListView() {
		for(FinalReportItem item: selectedListView.getItems()) {
			listView.getItems().add(item);
		}
	}
	
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	private ListCell<FinalReportItem> createListCell() {
		return new ListCell<FinalReportItem>() {
			@Override
			protected void updateItem(FinalReportItem item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getDisplayName());
				}

				setOnDragDetected(event -> {
					if (!this.isEmpty()) {
						Integer index = this.getIndex();
						Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
						db.setDragView(this.snapshot(null, null));
						ClipboardContent cc = new ClipboardContent();
						cc.put(SERIALIZED_MIME_TYPE, index);
						db.setContent(cc);
						event.consume();
					}
				});

				setOnDragOver(event -> {
					Dragboard db = event.getDragboard();
					if (db.hasContent(SERIALIZED_MIME_TYPE)) {
						if (this.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
							event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
							event.consume();
						}
					}
				});

				setOnDragDropped(event -> {
					Dragboard db = event.getDragboard();
					if (db.hasContent(SERIALIZED_MIME_TYPE)) {
						int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
						FinalReportItem draggedHeatMapController = listView.getItems().remove(draggedIndex);
						int dropIndex;
						if (this.isEmpty()) {
							dropIndex = listView.getItems().size();
						} else {
							dropIndex = this.getIndex();
						}
						listView.getItems().add(dropIndex, draggedHeatMapController);
						event.setDropCompleted(true);
						listView.getSelectionModel().select(dropIndex);
						event.consume();
					}
				});
			}
		};
	}
	
	@FXML
	public void createReportButtonAction() {
		controller.createReport(listView.getItems());
	}

}
