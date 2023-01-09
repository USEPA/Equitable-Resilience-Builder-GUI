package com.epa.wordcloud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.util.Callback;

public class WordCloudController implements Initializable {

	@FXML
	WebView wordCloudWebView;
	@FXML
	TextField inputTextField;
	@FXML
	TableView<WordCloudItem> tableView;
	@FXML
	TableColumn<WordCloudItem, Boolean> mergeTableColumn;
	@FXML
	TableColumn<WordCloudItem, String> phraseTableColumn;
	@FXML
	TableColumn<WordCloudItem, Boolean> plusTableColumn;
	@FXML
	TableColumn<WordCloudItem, Boolean> minusTableColumn;
	@FXML
	TableColumn<WordCloudItem, String> countTableColumn;

	WordCloudController wordCloudController = this;
	ArrayList<WordCloudItem> mergeArrayList = new ArrayList<WordCloudItem>();

	public WordCloudController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		addTextLimiter(inputTextField, 30);
		loadWordCloudHTML();
		inputTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode().equals(KeyCode.ENTER)) {
					addButtonAction();
				}
			}
		});
	}

	public void initTableView() {
		// --------------------
		mergeTableColumn.setCellValueFactory(new PropertyValueFactory("merge"));
		mergeTableColumn
				.setCellFactory(new Callback<TableColumn<WordCloudItem, Boolean>, TableCell<WordCloudItem, Boolean>>() {
					@Override
					public TableCell<WordCloudItem, Boolean> call(TableColumn<WordCloudItem, Boolean> arg0) {
						return new TableCheckCell(tableView);
					}
				});
		// ---------------
		phraseTableColumn.setCellValueFactory(new PropertyValueFactory("phrase"));
		// ---------------
		plusTableColumn
				.setCellFactory(new Callback<TableColumn<WordCloudItem, Boolean>, TableCell<WordCloudItem, Boolean>>() {
					@Override
					public TableCell<WordCloudItem, Boolean> call(TableColumn<WordCloudItem, Boolean> p) {
						return new PlusButtonCell(tableView, wordCloudController);
					}
				});
		// ---------------
		minusTableColumn
				.setCellFactory(new Callback<TableColumn<WordCloudItem, Boolean>, TableCell<WordCloudItem, Boolean>>() {
					@Override
					public TableCell<WordCloudItem, Boolean> call(TableColumn<WordCloudItem, Boolean> p) {
						return new MinusButtonCell(tableView, wordCloudController);
					}
				});
		// ---------------
		countTableColumn.setCellValueFactory(new PropertyValueFactory("count"));
		// ----------------
		tableView.setEditable(true);
	}

	public void updateTableView(WordCloudItem wordCloudItem) {
		tableView.getItems().add(wordCloudItem);
		tableView.refresh();
	}

	public void loadWordCloudHTML() {
		File wordCloudHTMLFile = new File("C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\JavaScript\\index.html");
		if (wordCloudHTMLFile.exists()) {
			wordCloudWebView.getEngine().load(wordCloudHTMLFile.toURI().toString());
		}
	}

	@FXML
	public void addButtonAction() {
		boolean merge = false;
		String phrase = inputTextField.getText();
		Button plusButton = new Button("+");
		Button minusButton = new Button("-");
		int count = 1;

		if (phrase != null && phrase.trim().length() > 0) {
			WordCloudItem existingCloudItem = phraseExists(phrase);
			if (existingCloudItem != null) {
				existingCloudItem.setCount(existingCloudItem.getCount() + 1);
				tableView.refresh();
				inputTextField.clear();
			} else { 
				WordCloudItem wordCloudItem = new WordCloudItem(merge, phrase, plusButton, minusButton, count);
				updateTableView(wordCloudItem);
				inputTextField.clear();
				inputTextField.requestFocus();
			}
		}
	}
	
	public WordCloudItem phraseExists(String phrase) {
		for (WordCloudItem wordCloudItem : tableView.getItems()) {
			if (wordCloudItem.getPhrase().toLowerCase().trim().contentEquals(phrase.toLowerCase().trim())) {
				return wordCloudItem;
			}
		}
		return null;
	}
	
	public void addMergedPhrase(String phrase, int count) {
		boolean merge = false;
		Button plusButton = new Button("+");
		Button minusButton = new Button("-");

		if (phrase != null && phrase.trim().length() > 0) {
			WordCloudItem wordCloudItem = new WordCloudItem(merge, phrase, plusButton, minusButton, count);
			updateTableView(wordCloudItem);
			inputTextField.clear();
			inputTextField.requestFocus();
		}
	}

	public void writeWordCloudJSONPFile(File JSONPFile) {
		try {
			PrintWriter printWriter = new PrintWriter(JSONPFile);
			printWriter.println("wordclouddata = [");
			for (WordCloudItem wordCloudItem : tableView.getItems()) {
				printWriter.println("{");
				printWriter.println("word: \"" + wordCloudItem.getPhrase() + "\",");
				printWriter.println("size: \"" + wordCloudItem.getCount() + "\",");
				printWriter.println("},");
			}
			printWriter.println("]");
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void buildButtonAction() {
		File wordCloudJSONPFile = new File("C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\JavaScript\\wordcloud.jsonp");
		writeWordCloudJSONPFile(wordCloudJSONPFile);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				wordCloudWebView.getEngine().reload();
			}
		});
	}

	@FXML
	public void mergeButtonAction() {
		ButtonType continueMerging = showMergeConfirmation();
		if (continueMerging == ButtonType.OK) {

			int count = 0;
			for (WordCloudItem wordCloudItem : mergeArrayList) {
				if (wordCloudItem.isMerge()) {
					count = count + wordCloudItem.getCount();
				}
			}
			for (WordCloudItem wordCloudItem : mergeArrayList) {
				tableView.getItems().remove(wordCloudItem);
			}
			addMergedPhrase(mergeArrayList.get(0).getPhrase(), count);
			mergeArrayList.clear();
		}
	}
	
	private ButtonType showMergeConfirmation() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText(getMergeConfirmationText());
		Optional<ButtonType> result = alert.showAndWait();
		return result.get();
	}
	
	private String getMergeConfirmationText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Are you sure you'd like to merge the following phrases?");
		for(WordCloudItem wordCloudItem: mergeArrayList) {
			stringBuilder.append("\n" + wordCloudItem.getPhrase());
		}
		return stringBuilder.toString();
	}

	private class TableCheckCell extends TableCell<WordCloudItem, Boolean> {
		final CheckBox checkBox = new CheckBox();

		TableCheckCell(final TableView<WordCloudItem> tblView) {
			checkBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectedIndex = getTableRow().getIndex();
					WordCloudItem wordCloudItem = tableView.getItems().get(selectedIndex);
					if (checkBox.isSelected()) {
						wordCloudItem.setMerge(true);
						mergeArrayList.add(wordCloudItem);
					} else {
						wordCloudItem.setMerge(false);
						mergeArrayList.remove(wordCloudItem);
					}
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			boolean pVal=false;
			if(t != null) {
				pVal = t;
			}

			super.updateItem(t, empty);
			if (!empty) {
				if(pVal) checkBox.setSelected(true);
				setGraphic(checkBox);
				setStyle("-fx-alignment: CENTER");
			}
		}
	}

	private class PlusButtonCell extends TableCell<WordCloudItem, Boolean> {
		final Button cellButton = new Button("+");

		PlusButtonCell(final TableView<WordCloudItem> tblView, WordCloudController wordCloudController) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectedIndex = getTableRow().getIndex();
					WordCloudItem wordCloudItem = tableView.getItems().get(selectedIndex);
					int c = wordCloudItem.getCount();
					wordCloudItem.setCount(c + 1);
					tblView.refresh();
				}
			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				cellButton.setPrefWidth(5);
				cellButton.setPrefHeight(5);
				cellButton.setFont(new Font(8));
				setGraphic(cellButton);
				setStyle("-fx-alignment: CENTER");
			}
		}
	}

	private class MinusButtonCell extends TableCell<WordCloudItem, Boolean> {
		final Button cellButton = new Button("-");

		MinusButtonCell(final TableView<WordCloudItem> tblView, WordCloudController wordCloudController) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectedIndex = getTableRow().getIndex();
					WordCloudItem wordCloudItem = tableView.getItems().get(selectedIndex);
					int c = wordCloudItem.getCount();
					wordCloudItem.setCount(c - 1);
					if (wordCloudItem.getCount() <= 0) {
						tblView.getItems().remove(wordCloudItem);
					}
					tblView.refresh();
				}
			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				cellButton.setPrefWidth(5);
				cellButton.setPrefHeight(5);
				cellButton.setFont(new Font(8));
				setGraphic(cellButton);
				setStyle("-fx-alignment: CENTER");
			}
		}
	}

	public void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

}