package com.epa.erb.noteboard;

import com.epa.erb.IndicatorCard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class NoteBoardItem_Indicator extends NoteBoardItemController{

	private IndicatorCard indicatorCard;
	public NoteBoardItem_Indicator(NoteBoardContentController noteBoardContentController, IndicatorCard indicatorCard) {
		super(noteBoardContentController);
		this.indicatorCard = indicatorCard;
	}
	
	protected void setColorAndImage() {
		if(indicatorCard.getSystem().contentEquals("Social Environment")) {
			noteBoardItemVBox.setStyle("-fx-background-color: " + indicatorCard.getSocialEnvironmentSystemColor());
			textFlow.setStyle("-fx-background-color: " + indicatorCard.getSocialEnvironmentSystemColor());
			scrollPane.setStyle("-fx-background: " + indicatorCard.getSocialEnvironmentSystemColor());
			imageView.setImage(new Image(getClass().getResourceAsStream("/socialEnvironment.png")));
		} else if (indicatorCard.getSystem().contentEquals("Built Environment")) {
			noteBoardItemVBox.setStyle("-fx-background-color: " + indicatorCard.getBuiltEnvironmentSystemColor());
			textFlow.setStyle("-fx-background-color: " + indicatorCard.getBuiltEnvironmentSystemColor());
			scrollPane.setStyle("-fx-background: " + indicatorCard.getBuiltEnvironmentSystemColor());
			imageView.setImage(new Image(getClass().getResourceAsStream("/builtEnvironment.png")));
		} else if (indicatorCard.getSystem().contentEquals("Natural Environment")) {
			noteBoardItemVBox.setStyle("-fx-background-color: " + indicatorCard.getNaturalEnvironmentSystemColor());
			textFlow.setStyle("-fx-background-color: " + indicatorCard.getNaturalEnvironmentSystemColor());
			scrollPane.setStyle("-fx-background: " + indicatorCard.getNaturalEnvironmentSystemColor());
			imageView.setImage(new Image(getClass().getResourceAsStream("/naturalEnvironment.png")));
		}
	}
	
	protected void flipCard(MouseEvent e) {
		if(e.getClickCount() == 1) {
			if(textFlow.getId().contentEquals("FRONT")) {
				addBackIndicatorCardText();
			} else if (textFlow.getId().contentEquals("BACK")) {
				addFrontIndicatorCardText();
			}
		}
	}
	
	protected void addFrontIndicatorCardText() {	
		textFlow.getChildren().clear();
		textFlow.setId("FRONT");
		
		//Indicator
		Text indicatorText = new Text(indicatorCard.getIndicator());
		indicatorText.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		
		textFlow.getChildren().add(indicatorText);
		
		//Definintion
		Text definitionText = new Text("\n" + indicatorCard.getDefinition());
		definitionText.setFont(Font.font("System", FontPosture.ITALIC, 10.0));
		
		textFlow.getChildren().add(definitionText);
		
		//Data Points
		Text dataPointsText = new Text("\nData Points");
		dataPointsText.setFont(Font.font("System", FontWeight.BOLD, 12.0));
		
		textFlow.getChildren().add(dataPointsText);
		
		//Thresholds/Comparisons:
		Text thresholdsText = new Text("\nThresholds/Comparisons:");
		thresholdsText.setFont(Font.font("System", FontWeight.BOLD, 12.0));
		
		textFlow.getChildren().add(thresholdsText);		
	}
	
	protected void addBackIndicatorCardText() {	
		textFlow.getChildren().clear();
		textFlow.setId("BACK");

		Text backTitle = new Text("Why does this matter?");
		backTitle.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		
		textFlow.getChildren().add(backTitle);
		
		//Resilience
		Text resilienceTextTitle = new Text("\nResilience");
		resilienceTextTitle.setFont(Font.font("System", FontWeight.BOLD, 12.0));
		
		textFlow.getChildren().add(resilienceTextTitle);

		Text resilienceText = new Text("\n" + indicatorCard.getResilienceValue());
		resilienceText.setFont(Font.font("System", FontWeight.NORMAL, 10.0));
		
		textFlow.getChildren().add(resilienceText);

		//Equity
		Text equityTextTitle = new Text("\nEquity");
		equityTextTitle.setFont(Font.font("System", FontWeight.BOLD, 12.0));
		
		textFlow.getChildren().add(equityTextTitle);

		Text equityText = new Text("\n" + indicatorCard.getEquityValue());
		equityText.setFont(Font.font("System", FontWeight.NORMAL, 10.0));
		
		textFlow.getChildren().add(equityText);
		
		//Local Concerns
		Text localConcernsTextTitle = new Text("\nLocal Concerns");
		localConcernsTextTitle.setFont(Font.font("System", FontWeight.BOLD, 12.0));
		
		textFlow.getChildren().add(localConcernsTextTitle);

		Text localConcernsText = new Text("\n" + indicatorCard.getLocalConcern());
		localConcernsText.setFont(Font.font("System", FontWeight.NORMAL, 10.0));
		
		textFlow.getChildren().add(localConcernsText);
		
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
