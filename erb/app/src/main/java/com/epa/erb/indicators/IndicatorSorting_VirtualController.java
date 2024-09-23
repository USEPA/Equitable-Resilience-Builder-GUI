package com.epa.erb.indicators;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.epa.erb.App;
import com.epa.erb.engagement_action.ExternalFileUploaderController;
import com.epa.erb.utility.FileHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

public class IndicatorSorting_VirtualController implements Initializable {
	
	private Logger logger;
	int numRowsForSorting = 1;
	int numColumnsForSorting = 1;
	private FileHandler fileHandler;
	private Image upArrow;
	private ImageView upArrowImageView;
	
	private Image downArrow;
	private ImageView downArrowImageView;
	
	private App app;
	private IndicatorCard[] cards;
	private IndicatorRanking_VirtualController iRVC;
	public IndicatorSorting_VirtualController(App app, IndicatorCard[] cards, IndicatorRanking_VirtualController iRVC) {
		this.app = app;
		this.iRVC = iRVC;
		this.cards = cards;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}
	
	@FXML
	VBox vBox, rankedVBox;
	@FXML
	Label titleLabel;
	@FXML
	HBox rankedHBox;
	@FXML
	BorderPane borderPane;
	@FXML
	GridPane majorGridPane;
	@FXML
	ScrollPane rankedScrollPane;
	@FXML
	Pane bottomLeftPane, topLeftPane, topRightPane, bottomRightPane;
	@FXML
	Button expandSortedCardsButton;
	@FXML
	ImageView expandSortedCardsGraphic;

	public void closeRequested(WindowEvent e) {
		Optional<ButtonType> result = showNoSaveWarning();
		if (result.get() == ButtonType.OK) {
			iRVC.getVirtualIndicatorSortingStage().close();
		} else {
			e.consume();
		}
	}

	private Optional<ButtonType> showNoSaveWarning() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Warning");
		alert.setContentText(
				"Closing this window will result in a reset of indicator quadrants. Please use the save button if you wish to save a snapshot of your indicator quadrants to My Portfolio.");
		return alert.showAndWait();
	}
	
	private Image getImageResource(Boolean up) {
		return up ? new Image(getClass().getResourceAsStream("/arrow-up.png")) : new Image(getClass().getResourceAsStream("/arrow-down.png"));
	}
	
	private void setExpandCards(Boolean expand) {
		expandSortedCardsGraphic.setImage(getImageResource(expand));
		rankedScrollPane.setVisible(expand);
		if (expand) {
			rankedVBox.getChildren().add(rankedScrollPane);
			rankedVBox.setPrefHeight(200.0);
		} else {
			rankedVBox.getChildren().remove(rankedScrollPane);
			rankedVBox.setPrefHeight(25.0);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillRankedHBox();
		rankedScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
			double newWidth = newVal.doubleValue() - 10.0;
			rankedHBox.setMinWidth(newWidth);
		});
		setDrag_RankingHBox(rankedHBox);
		handleQuadrantPanes();
		titleLabel.setText("Indicator Quadrant Sorting");
		expandSortedCardsButton.setAlignment(Pos.TOP_LEFT);
		expandSortedCardsButton.setText("Cards to sort");
		expandSortedCardsButton.setOnAction(e -> setExpandCards(!rankedScrollPane.isVisible()));
	}

	private void handleQuadrantPanes() {
		int numCards = cards.length;
		int maxRows = 2;
		int maxColumns = 4;
		int minRows = 1;
		int minColumns = 3;
		int numGrids = 4;

		outerLoop: {
			for (int i = 1; i <= maxColumns; i++) {
				for (int j = 1; j <= maxRows; j++) {
					int numSlots = numGrids * i * j;
					if (numSlots >= (numCards + 3)) {
						numRowsForSorting = j;
						numColumnsForSorting = i;
						break outerLoop;
					}
				}
			}
		}
		if (numRowsForSorting < minRows)
			numRowsForSorting = minRows;
		if (numColumnsForSorting < minColumns)
			numColumnsForSorting = minColumns;

		GridPane gridPane1 = createRankingGridPane(numRowsForSorting, numColumnsForSorting);
		majorGridPane.add(gridPane1, 0, 0);
		gridPane1.setStyle("-fx-border-color: #FFE400; -fx-background-color: #FFF6AD");
		GridPane gridPane2 = createRankingGridPane(numRowsForSorting, numColumnsForSorting);
		majorGridPane.add(gridPane2, 0, 1);
		gridPane2.setStyle("-fx-border-color: #FF1A1A; -fx-background-color: #E3C6C6");
		GridPane gridPane3 = createRankingGridPane(numRowsForSorting, numColumnsForSorting);
		majorGridPane.add(gridPane3, 1, 0);
		gridPane3.setStyle("-fx-border-color: #15CA15; -fx-background-color: #C2E9C2");
		GridPane gridPane4 = createRankingGridPane(numRowsForSorting, numColumnsForSorting);
		majorGridPane.add(gridPane4, 1, 1);
		gridPane4.setStyle("-fx-border-color: #FFE400; -fx-background-color: #FFF6AD");
	}

	private GridPane createRankingGridPane(int numberOfRows, int numberOfColumns) {
		GridPane gridPane = new GridPane();
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				VBox gridVBox = new VBox();
				gridVBox.setAlignment(Pos.CENTER);
				gridPane.add(gridVBox, j, i);
				gridPane.widthProperty().addListener((obs, oldVal, newVal) -> {
					double newWidth = newVal.doubleValue() - 5;
					gridVBox.setPrefWidth(newWidth / (numberOfColumns));
					gridVBox.setMinWidth(newWidth / (numberOfColumns));

				});

				gridPane.heightProperty().addListener((obs, oldVal, newVal) -> {
					double newHeight = newVal.doubleValue() - 5;
					gridVBox.setPrefHeight(newHeight / (numberOfRows));
					gridVBox.setMinHeight(newHeight / (numberOfRows));

				});
				setDrag_GridVBox(gridVBox);
			}
		}
		return gridPane;
	}

	private void fillRankedHBox() {
		for (IndicatorCard card : cards) {
			try {
				Pane cVBox = loadIndicatorCard(card);
				rankedHBox.getChildren().add(cVBox);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Failed to fill ranked hBox: " + e.getMessage());
			}
		}
	}

	private Pane loadIndicatorCard(IndicatorCard card) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorCard.fxml"));
			IndicatorCardController iCController = new IndicatorCardController(card, app);
			fxmlLoader.setController(iCController);
			VBox cVBox = fxmlLoader.load();
			cVBox.setPrefWidth(70);
			cVBox.setMaxWidth(70);
			cVBox.setMinWidth(70);
			cVBox.setPrefHeight(90);
			cVBox.setMinHeight(90);
			cVBox.setMaxHeight(90);
			cVBox.setId(card.getId());
			iCController.turnOnMouseClicked();
			iCController.setDrag_IndicatorCard(cVBox);
			iCController.setColorAndImage();
			iCController.removeImageView();
			iCController.addTextForRanking();
			return cVBox;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load IndicatorCard.fxml: " + e.getMessage());
			return null;
		}
	}

	@FXML
	public void saveButtonAction() {
		if (borderPane.getWidth() > 0 && borderPane.getHeight() > 0) {
			WritableImage writableImage = new WritableImage((int) borderPane.getWidth(), (int) borderPane.getHeight());
			borderPane.snapshot(null, writableImage);
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM_dd_yyyy_HH.mm.ss");
				LocalDateTime now = LocalDateTime.now();
				File sortingVirtualDir = createIndicatorsSortingVirtualDir();
				File virtualSortingSnapshotSave = new File(
						sortingVirtualDir + File.separator + "SortingSnapshot_" + dtf.format(now) + ".png");
				ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", virtualSortingSnapshotSave);
				ExternalFileUploaderController exFileUploader = new ExternalFileUploaderController(app,
						app.getEngagementActionController());
				exFileUploader.pushToUploaded(virtualSortingSnapshotSave, "Indicator Center");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Failed to save: " + e.getMessage());
			}
		}
	}

	private File createIndicatorsSortingVirtualDir() {
		File indicatorsDir = fileHandler.getIndicatorsDirectory(app.getSelectedProject(),
				app.getEngagementActionController().getCurrentGoal());
		if (!indicatorsDir.exists()) {
			indicatorsDir.mkdir();
		}
		File sortingVirtualDir = new File(indicatorsDir + File.separator + "Sorting_Virtual");
		if (!sortingVirtualDir.exists()) {
			sortingVirtualDir.mkdir();
		}
		return sortingVirtualDir;
	}

	protected void setDrag_RankingHBox(Pane p) {
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
				VBox sourceVBox = (VBox) source;
				HBox targetHBox = (HBox) target;
				if (targetHBox.getChildren().contains(sourceVBox)) {
					int targetIndex = targetHBox.getChildren().size() - 1;
					targetHBox.getChildren().remove(sourceVBox);
					targetHBox.getChildren().add(targetIndex, sourceVBox);
				} else {
					targetHBox.getChildren().add(sourceVBox);
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

	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();

	protected void setDrag_GridVBox(Pane p) {
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
				if (target.getChildren().size() == 0) {
					target.getChildren().add(source);
				} else {
					GridPane gP = (GridPane) target.getParent();
					int children = 0;
					for (Object pa : gP.getChildren()) {
						VBox child = (VBox) pa;
						if (child.getChildren().size() > 0) {
							children++;
						}
					}
					if (children == 6) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setTitle("Quadrant Full");
						alert.setContentText(
								"This quadrant is full. Please remove an indicator card from this quadrant to add a new one.");
						alert.showAndWait();
					}
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

}
