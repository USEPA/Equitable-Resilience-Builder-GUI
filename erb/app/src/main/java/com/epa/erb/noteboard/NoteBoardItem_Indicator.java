package com.epa.erb.noteboard;

import com.epa.erb.indicators.IndicatorCard;
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
	
	public void setColorAndImage() {
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
	
	public void removeFlipHBox() {
		if(noteBoardItemVBox.getChildren().contains(flipHBox)) {
			noteBoardItemVBox.getChildren().remove(flipHBox);
		}
	}
	
	public void resizeImageView() {
		imageView.setFitWidth(50.0);
		imageView.setFitHeight(50.0);
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
	
	public void addTextForPrinting(String field) {
		if(field.contains("Default data")) {
			setTextForPrinting();
		} else if(field.contentEquals("System")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text s = new Text("\n" + indicatorCard.getSystem());
			s.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(s);
			
		} else if (field.contentEquals("Indicator")) {
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text indicatorText = new Text("\n" + indicatorCard.getIndicator());
			indicatorText.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(indicatorText);
			
		} else if (field.contains("Definition")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text definitionText = new Text("\n" + indicatorCard.getDefinition());
			definitionText.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(definitionText);
			
		} else if (field.contentEquals("Resilience Value")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text resilienceText = new Text("\n" + indicatorCard.getResilienceValue());
			resilienceText.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(resilienceText);
			
		} else if (field.contentEquals("Equity Value")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text equityText = new Text("\n" + indicatorCard.getEquityValue());
			equityText.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(equityText);
			
		} else if (field.contains("Local Concern")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text localConcernsText = new Text("\n" + indicatorCard.getLocalConcern());
			localConcernsText.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(localConcernsText);
			
		} else if (field.contentEquals("Data Questions to Answer")){
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text qText = new Text("\n" + indicatorCard.getDataQuestionsToAnswerAsString());
			qText.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(qText);
			
		} else if (field.contentEquals("Quantitative Data Sources")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text qDS = new Text("\n" + indicatorCard.getQuantitativeDataSources());
			qDS.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(qDS);
			
		} else if (field.contentEquals("Quantitative Data Collection Process")) {
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text qDC = new Text("\n" + indicatorCard.getQuantitativeDataCollectionProcess());
			qDC.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(qDC);
			
		} else if (field.contentEquals("Qualitative Data Collection Process")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text qDC = new Text("\n" + indicatorCard.getQualitativeDataCollectionProcess());
			qDC.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(qDC);
			
		} else if (field.contains("Additional Details")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text aD = new Text("\n" + indicatorCard.getAdditionalInformation());
			aD.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(aD);
			
		}  else if (field.contains("data collection process")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text dC = new Text("\n" + indicatorCard.getRawDataCollectionNotes());
			dC.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(dC);
			
		} else if (field.contains("Data Points")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text dataPoints = new Text("\n" + indicatorCard.getDataPoints());
			dataPoints.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(dataPoints);
			
		} else if (field.contains("threshold value")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text threshold = new Text("\n" +indicatorCard.getThresholds());
			threshold.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(threshold);
			
		} else if (field.contentEquals("Data Collection (Y/N)")) {
			
			Text title = new Text("\n" + field + ":");
			title.setFont(Font.font("System", FontWeight.BOLD, 15.0));
			textFlow.getChildren().add(title);
			
			Text dC = new Text("\n" +indicatorCard.getDataCollection_y_n());
			dC.setFont(Font.font("System", FontWeight.NORMAL, 15.0));
			textFlow.getChildren().add(dC);
			
		}
	}
	
	
	public void setTextForPrinting() {
		textFlow.getChildren().clear();

		// Indicator
		Text indicatorText = new Text(indicatorCard.getIndicator());
		indicatorText.setFont(Font.font("System", FontWeight.BOLD, 15.0));
		textFlow.getChildren().add(indicatorText);

		// Definintion
		Text definitionText = new Text("\n" + indicatorCard.getDefinition());
		definitionText.setFont(Font.font("System", FontPosture.ITALIC, 12.0));
		textFlow.getChildren().add(definitionText);

		// Data Points
		Text dataPointsText = new Text("\nData Points");
		dataPointsText.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		textFlow.getChildren().add(dataPointsText);
		
		Text dataPoints = new Text("\n" + indicatorCard.getDataPoints());
		dataPoints.setFont(Font.font("System", FontWeight.NORMAL, 12.0));
		textFlow.getChildren().add(dataPoints);

		// Thresholds/Comparisons:
		Text thresholdsText = new Text("\nThresholds/Comparisons:");
		thresholdsText.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		textFlow.getChildren().add(thresholdsText);
		
		Text threshold = new Text("\n" +indicatorCard.getThresholds());
		threshold.setFont(Font.font("System", FontWeight.NORMAL, 12.0));
		textFlow.getChildren().add(threshold);
		
		Text empty = new Text("\n");		
		textFlow.getChildren().add(empty);
		
		Text backTitle = new Text("\nWhy does this matter?");
		backTitle.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		textFlow.getChildren().add(backTitle);

		// Resilience
		Text resilienceTextTitle = new Text("\nResilience");
		resilienceTextTitle.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		textFlow.getChildren().add(resilienceTextTitle);

		Text resilienceText = new Text("\n" + indicatorCard.getResilienceValue());
		resilienceText.setFont(Font.font("System", FontWeight.NORMAL, 12.0));
		textFlow.getChildren().add(resilienceText);

		// Equity
		Text equityTextTitle = new Text("\nEquity");
		equityTextTitle.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		textFlow.getChildren().add(equityTextTitle);

		Text equityText = new Text("\n" + indicatorCard.getEquityValue());
		equityText.setFont(Font.font("System", FontWeight.NORMAL, 12.0));
		textFlow.getChildren().add(equityText);

		// Local Concerns
		Text localConcernsTextTitle = new Text("\nLocal Concerns");
		localConcernsTextTitle.setFont(Font.font("System", FontWeight.BOLD, 14.0));
		textFlow.getChildren().add(localConcernsTextTitle);

		Text localConcernsText = new Text("\n" + indicatorCard.getLocalConcern());
		localConcernsText.setFont(Font.font("System", FontWeight.NORMAL, 12.0));
		textFlow.getChildren().add(localConcernsText);

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
