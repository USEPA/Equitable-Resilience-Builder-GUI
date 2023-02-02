package com.epa.erb.wordcloud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import com.epa.erb.App;
import com.epa.erb.InteractiveActivity;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;
import javafx.util.Callback;
import javafx.concurrent.Worker.State;

public class WordCloudController extends InteractiveActivity implements Initializable {

	@FXML
	VBox vBox;
	@FXML
	Button mergeButton;
	@FXML
	Button buildButton;
	@FXML
	Button shortAddButton;
	@FXML
	Button longAddButton;
	@FXML
	WebView wordCloudWebView;
	@FXML
	TextField inputTextField;
	@FXML
	TextArea inputTextArea;
	@FXML
	CheckBox excludeCommonCheckBox;
	@FXML
	ImageView imageView;
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
	private FileHandler fileHandler = new FileHandler();
	private HashSet<String> excludedWordSet = new HashSet<String>();
	ArrayList<WordCloudItem> mergeArrayList = new ArrayList<WordCloudItem>();

	private App app;
	private Project project;
	private Goal goal;
	public WordCloudController(String id, String guid, String longName, String shortName, String status, App app, Project project, Goal goal) {
		super(id, guid, longName, shortName, status);
		this.app = app;
		this.project = project;
		this.goal = goal;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		excludeCommonCheckBox.setSelected(true);
		setExcludedWordSet();
		wordCloudWebView.getEngine().setJavaScriptEnabled(true);
		initTableView();
		if(project.getProjectName().contentEquals("Explore")) {
			setUnEditable();
			File wordCloudHTMLFile =  new File(fileHandler.getGUIDDataDirectory(project, goal).getPath() + "\\" + getGuid() + "\\index.html");
			if (wordCloudHTMLFile.exists()) {
				try {
					wordCloudWebView.getEngine().load(wordCloudHTMLFile.toURI().toURL().toString());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			return;
		}
		
		checkForExistingWordCloudData();
		addTextLimiter(inputTextField, 30);
		File jsonpFile = fileHandler.getJSONPWordCloudFileForInteractiveActivity(project, goal, getGuid());
		XMLManager xmlManager = new XMLManager(app);
		if(!jsonpFile.getParentFile().exists()) {
			xmlManager.writeGoalMetaXML(fileHandler.getGoalMetaXMLFile(project, goal), app.getEngagementActionController().getListOfUniqueChapters());
			fileHandler.createGUIDDirectoriesForGoal(project, goal, app.getEngagementActionController().getListOfUniqueChapters());
		}
		copyWordCloudFilesToGUIDDirectory();
		
		inputTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode().equals(KeyCode.ENTER)) {
					shortAddButtonAction();
				}
			}
		});
		
		wordCloudWebView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							snapshotWordCloud();
						}
					});
				}
			}
		});
	}
	
	private void setUnEditable() {
		inputTextField.setDisable(true);
		mergeButton.setDisable(true);
		shortAddButton.setDisable(true);
		longAddButton.setDisable(true);
		buildButton.setDisable(true);
	}
	
	@FXML
	public void saveButtonAction() {
		File dataXML = fileHandler.getDynamicActivityDataXMLFile(project, goal, getGuid());
		if (dataXML != null) {
			XMLManager xmlManager = new XMLManager(app);
			ArrayList<WordCloudItem> wordCloudItems = new ArrayList<WordCloudItem>(tableView.getItems());
			xmlManager.writeWordCloudDataXML(dataXML, wordCloudItems);
		}
	}
	
	private void checkForExistingWordCloudData() {
		if(wordCloudDataExists()) {
			XMLManager xmlManager = new XMLManager(app);
			File dataXML = fileHandler.getDynamicActivityDataXMLFile(project, goal, getGuid());
			ArrayList<WordCloudItem> parsedWordCloudItems = xmlManager.parseWordCloudDataXML(dataXML);
			if(parsedWordCloudItems != null) {
				for(WordCloudItem wordCloudItem: parsedWordCloudItems) {
					createWordCloudItem(wordCloudItem.merge, wordCloudItem.getPhrase(), wordCloudItem.getCount(), wordCloudItem.getSize());
				}
			}
		}
	}
	
	private boolean wordCloudDataExists() {
		File jsonpFile = fileHandler.getJSONPWordCloudFileForInteractiveActivity(project, goal, getGuid());
		if(jsonpFile.exists()) {
			return true;
		} 
		return false;
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
	
	public void adjustCountToSize() {
		int newMax = 50;
		int newMin =10;
		
		for(WordCloudItem wordCloudItem: tableView.getItems()) {
			int maxCount = getMaxCountInTable();
			int minCount = getMinCountInTable();
						
			if(wordCloudItem.getCount() == 1) {
				wordCloudItem.setSize(newMin);
			} else {
				if(tableView.getItems().size() > 1) {
				
				//int v = ((count -minCount)/(maxCount - minCount))*(newMax - newMin) + newMin;

				double r1 = wordCloudItem.getCount() - minCount; 
				double r2 = maxCount - minCount; 
				double r3 = r1/r2; 
				double r4 = newMax - newMin; 
				double r5 = (r3 * r4); 
				int r6= (int) (r5 + newMin); 
						
				wordCloudItem.setSize(r6);
				}else {
					wordCloudItem.setSize(newMin);
				}
			}
		}
	}
	
	private int getMaxCountInTable() {
		int maxCount = tableView.getItems().get(0).getCount();
		for (WordCloudItem wordCloudItem : tableView.getItems()) {
			if (wordCloudItem.getCount() > maxCount) {
				maxCount = wordCloudItem.getCount();
			}
		}
		return maxCount;
	}
	
	private int getMinCountInTable() {
		int minCount = tableView.getItems().get(0).getCount();
		for (WordCloudItem wordCloudItem : tableView.getItems()) {
			if (wordCloudItem.getCount() < minCount) {
				minCount = wordCloudItem.getCount();
			}
		}
		return minCount;
	}

	@FXML
	public void shortAddButtonAction() {
		boolean merge = false;
		String phrase = inputTextField.getText();
		int count = 1;
		int size = count;
		createWordCloudItem(merge, phrase, count, size);
		adjustCountToSize();
	}
	
	@FXML
	public void longAddButtonAction() {
		boolean merge = false;
		String phrase = inputTextArea.getText();
		boolean excludeWords = excludeCommonCheckBox.isSelected();
		HashMap<String, Integer> countMap = countWordOccurrences(excludeWords, phrase);
		for(String word: countMap.keySet()) {
			boolean isCleaned = false;
			String cleanedWord = word;
			while(!isCleaned) {
				cleanedWord = cleanWord(cleanedWord);
				isCleaned = wordIsClean(word);
			}
			int count = countMap.get(cleanedWord);
			int size = count;
			createWordCloudItem(merge, cleanedWord, count, size);
		}
		adjustCountToSize();
	}
	
	@FXML
	public void excludeCommonCheckBoxAction() {
		//Maybe do something here?
	}
	
	public boolean wordIsClean(String word) {
		if (word != null) {
			Pattern lettersAndNumbers = Pattern.compile("([A-Za-z0-9])");
			word = word.trim();
			if (word.length() >= 2) {
				String firstChar = word.substring(0, 1);
				Matcher matcher1 = lettersAndNumbers.matcher(firstChar);

				String lastChar = word.substring(word.length() - 1, word.length());
				Matcher matcher2 = lettersAndNumbers.matcher(lastChar);

				boolean firstCharOK = matcher1.matches();
				boolean lastCharOK = matcher2.matches();

				if (!firstCharOK || !lastCharOK) {
					return false;
				}
			} else if(word.length() > 0) {
				Matcher matcher = lettersAndNumbers.matcher(word);
				return matcher.matches();
			}
		}
		return true;
	}
	
	public String cleanWord(String word) {
		if (word != null) {
			Pattern lettersAndNumbers = Pattern.compile("([A-Za-z0-9])");
			word = word.trim();
			if(word.length() >= 2) {
				String firstChar = word.substring(0, 1);
				Matcher matcher1 = lettersAndNumbers.matcher(firstChar);

				String lastChar = word.substring(word.length() - 1, word.length());
				Matcher matcher2 = lettersAndNumbers.matcher(lastChar);

				boolean firstCharOK = matcher1.matches();
				boolean lastCharOK = matcher2.matches();
				
				if(!firstCharOK) {
					word = word.substring(1, word.length());
				}
				
				if(!lastCharOK) {
					word = word.substring(0, word.length()-1);
				}
				
			} else if (word.length() > 0) {
				return "";
			}
		}
		return word;
	}
	
	public HashMap<String, Integer> countWordOccurrences(boolean excludeCommonWords, String string) {
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		if(string !=null && string.length() > 0) {
			String newLinesRemoved = string.replaceAll("\n", "");
			String [] words = newLinesRemoved.split(" ");
			for(String word: words) {
				String cleanedWord = cleanWord(word);
				if(excludeCommonWords) { //If check box to exclude is selected
					if(excludedWordSet.contains(cleanedWord.toLowerCase().trim())) { //If is an excluded word
						continue; //skip
					} else { //If not an excluded word
						increaseCountOfWord(countMap, cleanedWord);
					}
				} else { //If check box to exclude is NOT selected
					increaseCountOfWord(countMap, cleanedWord);
				}
			}
		}
		return countMap;
	}
	
	public void increaseCountOfWord(HashMap<String, Integer> countMap, String word) {
		if (countMap.get(word) != null) {
			int count = countMap.get(word);
			count = count + 1;
			countMap.put(word, count);
		} else {
			int count = 1;
			countMap.put(word, count);
		}
	}
	
	public void createWordCloudItem(boolean merge, String phrase, int count, int size) {
		Button plusButton = new Button("+");
		Button minusButton = new Button("-");
		if (phrase != null && phrase.trim().length() > 0) {
			WordCloudItem existingCloudItem = phraseExists(phrase);
			if (existingCloudItem != null) {
				existingCloudItem.setCount(existingCloudItem.getCount() + 1);
				tableView.refresh();
				inputTextField.clear();
				inputTextArea.clear();
			} else { 
				WordCloudItem wordCloudItem = new WordCloudItem(merge, phrase, plusButton, minusButton, count, size);
				updateTableView(wordCloudItem);
				inputTextField.clear();
				inputTextArea.clear();
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
	
	public void addMergedPhrase(String phrase, int count, int size) {
		boolean merge = false;
		Button plusButton = new Button("+");
		Button minusButton = new Button("-");

		if (phrase != null && phrase.trim().length() > 0) {
			WordCloudItem wordCloudItem = new WordCloudItem(merge, phrase, plusButton, minusButton, count, size);
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
				printWriter.println("size: \"" + wordCloudItem.getSize() + "\",");
				printWriter.println("count: \"" + wordCloudItem.getCount() + "\",");
				printWriter.println("},");
			}
			printWriter.println("],");
			printWriter.println("sizedata = [");
			printWriter.println("{");
			int numberOfWords = tableView.getItems().size();
			int size = ((numberOfWords/30) *100) +300;
			printWriter.println("height: \"" + size + "\",");
			printWriter.println("width: \"" + size + "\",");
			printWriter.println("},");
			printWriter.println("]");
			
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void buildButtonAction() {
		if(!vBox.getChildren().contains(wordCloudWebView)) {
			vBox.getChildren().clear();
			vBox.getChildren().add(wordCloudWebView);
		}
		File jsonpFile = fileHandler.getJSONPWordCloudFileForInteractiveActivity(project, goal, getGuid());
		writeWordCloudJSONPFile(jsonpFile);
		String webEngineLocation = wordCloudWebView.getEngine().getLocation();
		if (webEngineLocation != null) {
			wordCloudWebView.getEngine().reload();
		} else {
			File wordCloudHTMLFile =  new File(fileHandler.getGUIDDataDirectory(project, goal).getPath() + "\\" + getGuid() + "\\index.html");
			System.out.println(wordCloudHTMLFile.getPath());
			if (wordCloudHTMLFile.exists()) {
				try {
					wordCloudWebView.getEngine().load(wordCloudHTMLFile.toURI().toURL().toString());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void snapshotWordCloud() {
		WritableImage writableImage = new WritableImage((int) wordCloudWebView.getWidth(), (int) wordCloudWebView.getHeight());
		wordCloudWebView.snapshot(null, writableImage);
		File saveFile = new File(fileHandler.getGUIDDataDirectory(project, goal) + "\\" + getGuid() + "\\wordCloudImage.png");
		if (saveFile.exists()) saveFile.delete();
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		ImageView imageView = new ImageView();
//		imageView.setFitWidth(wordCloudWebView.getWidth());
//		imageView.setFitHeight(wordCloudWebView.getHeight());
//		Image image = new Image(saveFile.getPath());
//		imageViewClicked(imageView);
//		imageView.setImage(image);
//		vBox.getChildren().clear();
//		vBox.getChildren().add(imageView);
	}
	
	public void imageViewClicked(ImageView imageView) {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem("Save as");
		menuItem.setOnAction(e -> saveImage(imageView, e));
		contextMenu.getItems().add(menuItem);
		imageView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				contextMenu.show(imageView, event.getSceneX(), event.getSceneY());
			}
		});

	}
	
	public void saveImage(ImageView imageView, ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(null);
		if(file != null) {
			File saveFile = new File(fileHandler.getGUIDDataDirectory(project, goal) + "\\" + getGuid() + "\\wordCloudImage.png");
			fileHandler.copyFile(saveFile, file);
		}
	}
	
	public void copyWordCloudFilesToGUIDDirectory() {
		File staticWordCloudDirectory = fileHandler.getStaticWordCloudDirectory();
		
		File guidDataDirectory = fileHandler.getGUIDDataDirectory(project, goal);
		File guidDirectory = new File(guidDataDirectory.getPath() + "\\" + getGuid());
		
		for(File sourceFile: staticWordCloudDirectory.listFiles()) {
			File destFile = new File(guidDirectory.getPath() + "\\" + sourceFile.getName());
			fileHandler.copyFile(sourceFile, destFile);
		}		
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
			int size = count;
			addMergedPhrase(mergeArrayList.get(0).getPhrase(), count, size);
			adjustCountToSize();
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
					int c = (wordCloudItem.getCount() + 1);
					wordCloudItem.setCount(c);
					tblView.refresh();
					adjustCountToSize();
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
					int c = (wordCloudItem.getCount() -1);
					wordCloudItem.setCount(c);
					if (wordCloudItem.getCount() <= 0) {
						tblView.getItems().remove(wordCloudItem);
					}
					tblView.refresh();
					adjustCountToSize();
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

	public void setExcludedWordSet() {
		excludedWordSet.addAll(Arrays.asList("all", "just", "being", "over", "both", "through", "yourselves", "its", "before", "herself", "had", "should", "to", "only", "under", "ours", "has", "do", "them", "his", "very", "they", "not", "during", "now", "him", "nor", "did", "this", "she", "each", "further", "where", "few", "because", "doing", "some", "are", "our", "ourselves", "out", "what", "for", "while", "does", "above", "between", "t", "be", "we", "who", "were", "here", "hers", "by", "on", "about", "of", "against", "s", "or", "own", "into", "yourself", "down", "your", "from", "her", "their", "there", "been", "whom", "too", "themselves", "was", "until", "more", "himself", "that", "but", "don", "with", "than", "those", "he", "me", "myself", "these", "up", "will", "below", "can", "theirs", "my", "and", "then", "is", "am", "it", "an", "as", "itself", "at", "have", "in", "any", "if", "again", "no", "when", "same", "how", "other", "which", "you", "after", "most", "such", "why", "a", "off", "i", "yours", "so", "the", "having", "once"));
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

}