package com.epa.erb.forms;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.PrintWriter;
import com.epa.erb.App;
import com.epa.erb.engagement_action.ExternalFileUploaderController;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Separator;

public class OutputFormController extends FormController implements Initializable {

	private Logger logger;
	private XMLManager xmlManager;
	private FileHandler fileHandler;
	
	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;
	public OutputFormController(String id, String guid, String longName, String shortName, String status,App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		super(app, xmlContentFileToParse, engagementActionController);
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
		
		logger = app.getLogger();
		xmlManager = new XMLManager(app);
		fileHandler = new FileHandler(app);
	}
	
	@FXML
	VBox vBox;
	@FXML
	Pane lP, tP, rP, bP;
	@FXML
	VBox nodeVBox, formVBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		boolean hasExistingContent = checkForExisitingContent();
		if(!hasExistingContent) {
			HashMap<ArrayList<Text>, String> contentHashMap = xmlManager.parseOutputFormContentXML(xmlContentFileToParse);
			addContent(contentHashMap);
		} else {
			File dataXMLFile = fileHandler.getDataXMLFile(engagementActionController.getProject(), engagementActionController.getCurrentGoal(), engagementActionController.getCurrentSelectedERBContentItem());
			HashMap<ArrayList<Text>, String> contentHashMap = xmlManager.parseOutputFormContentXML(dataXMLFile);
			addContent(contentHashMap);
		}
		setColor(tP);
		if(engagementActionController != null) {
			if(engagementActionController.getProject().getProjectName().contentEquals("Explore")) {
				setUnEditable();
			}
		}
	}
	
	private boolean checkForExisitingContent() {
		File dataXMLFile = fileHandler.getDataXMLFile(engagementActionController.getProject(), engagementActionController.getCurrentGoal(), engagementActionController.getCurrentSelectedERBContentItem());
		if(dataXMLFile != null && dataXMLFile.exists()) {
			return true;
		}
		return false;
	}
	
	private void addContent(HashMap<ArrayList<Text>, String> contentHashMap) {
		for(ArrayList<Text> textBlock: contentHashMap.keySet()) {
			String type = contentHashMap.get(textBlock);
			if(type.contentEquals("block")) {
				TextFlow textFlow = createTextFlow(textBlock);
				formVBox.getChildren().add(textFlow);
			} else if (type.contentEquals("area")) {
				TextArea textArea = createTextArea(textBlock);
				formVBox.getChildren().add(textArea);
			} else if (type.contentEquals("dynamic area")) {
				handleDynamicArea(textBlock, formVBox.getChildren().size(), false);
			}
		}
	}
	private void handleDynamicArea(ArrayList<Text> textBlock, int indexToAddDynamicAreaVBox, boolean cleanCopy) {
		VBox dynamicAreaVBox = new VBox();
		dynamicAreaVBox.setPadding(new Insets(10,10,10,10));
		dynamicAreaVBox.setSpacing(5);
		for(Text text: textBlock) {
			HashMap<String, String> textAndResponse = parseTextAndResponse(text.getText());
			Text copyText =copyText(text);
			copyText.setText(textAndResponse.get("prompt"));
			copyText.setWrappingWidth(250);
			HBox textHBox = new HBox();
			VBox.setVgrow(textHBox, Priority.ALWAYS);
			TextArea textArea = new TextArea();
			if(!cleanCopy) textArea.setText(textAndResponse.get("response"));
			HBox.setHgrow(textArea, Priority.ALWAYS);
			textHBox.getChildren().add(copyText);
			textHBox.getChildren().add(textArea);
			textHBox.setSpacing(20.0);
			textArea.setWrapText(true);
			dynamicAreaVBox.getChildren().add(textHBox);
		}
		dynamicAreaVBox.setStyle("-fx-border-color: gray");
		Separator separator = new Separator();
		separator.setHalignment(HPos.LEFT);
		dynamicAreaVBox.getChildren().add(separator);
		HBox buttonHBox = new HBox();
		buttonHBox.setAlignment(Pos.CENTER_RIGHT);
		Button addButton = new Button ("Add Action Area");
		addButton.getStylesheets().add(getClass().getResource("/button.css").toString());
		buttonHBox.getChildren().add(addButton);
		dynamicAreaVBox.getChildren().add(buttonHBox);
		dynamicAreaVBox.setId("dynamic area");
		formVBox.getChildren().add(indexToAddDynamicAreaVBox, dynamicAreaVBox);
		addButton.setOnAction(e-> handleDynamicArea(textBlock, indexToAddDynamicAreaVBox + 1, true));
	}
	
	private HashMap<String, String> parseTextAndResponse(String text) {
		HashMap<String, String> results = new HashMap<String, String>();
		Pattern promptPattern = Pattern.compile("(\\[(.*?)\\])");
		Matcher matcher = promptPattern.matcher(text);
		if(matcher.find()) {
			String prompt = matcher.group(2);
			results.put("prompt", prompt);
			
			String leftOverString = text.replace(matcher.group(1), "").trim();
			if(leftOverString.length() > 0) {
				results.put("response", leftOverString);
			}
		}
		return results;	
	}
	
	private Text copyText(Text text) {
		Text copyText = new Text();
		copyText.setText(text.getText());
		copyText.setFont(text.getFont());
		copyText.setId(text.getId());
		return copyText;
	}
	
	private TextFlow createTextFlow(ArrayList<Text> textBlock) {
		TextFlow textFlow = new TextFlow();
		for(Text text: textBlock) {
			textFlow.getChildren().add(text);
		}
		textFlow.setId("block");
		return textFlow;
	}
	
	private TextArea createTextArea(ArrayList<Text> textBlock) {
		TextArea textArea = new TextArea();
		textArea.setWrapText(true);
		String textString = "";
		String textType = null;
		for(Text text: textBlock) {
			textString = textString + text.getText();
			textType = text.getId();
		}
		
		if(textType != null) {
			if(textType.contentEquals("Prompt")) {
				textArea.setPromptText(textString);
			} else if (textType.contentEquals("Regular")) {
				textArea.setText(textString);
			}
		}
		textArea.setId("area");
		textArea.setWrapText(true);
		textArea.setMinHeight(100);
		return textArea;
	}
	
	@FXML
	public void saveButtonAction() {
		ArrayList<Node> listOfChildren = new ArrayList<Node>();
		for(int i =0; i < formVBox.getChildren().size(); i++) {
			listOfChildren.add(formVBox.getChildren().get(i));
		}
		File saveLocation = fileHandler.getDataXMLFile(engagementActionController.getProject(), engagementActionController.getCurrentGoal(), engagementActionController.getCurrentSelectedERBContentItem());
		if(!saveLocation.getParentFile().exists()) {
			xmlManager.writeGoalMetaXML(fileHandler.getGoalMetaXMLFile(engagementActionController.getProject(), engagementActionController.getCurrentGoal()), engagementActionController.getListOfUniqueERBContentItems());
			fileHandler.createGUIDDirectoriesForGoal2(engagementActionController.getProject(), engagementActionController.getCurrentGoal(), engagementActionController.getListOfUniqueERBContentItems());

		}
		xmlManager.writeOutputFormDataXML(saveLocation, listOfChildren);
		String fileName = engagementActionController.getCurrentSelectedERBContentItem().getShortName().replaceAll(" ", "_").replaceAll(":", "_");
		File outputTextFile = new File(fileHandler.getGUIDDataDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal())+"\\" + engagementActionController.getCurrentSelectedERBContentItem().getGuid() + "\\" + fileName + ".txt");
		writeOutputFormToTextFile(listOfChildren,outputTextFile);
		ExternalFileUploaderController exFileUploader = new ExternalFileUploaderController(app,engagementActionController);
		exFileUploader.pushToUploaded(outputTextFile, "Key Takeaways");
	}
	
	private void writeOutputFormToTextFile(ArrayList<javafx.scene.Node> outputFormNodes, File file) {
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (javafx.scene.Node node : outputFormNodes) {
				if (node.toString().contains("TextFlow")) {
					TextFlow textFlow = (TextFlow) node;
					StringBuilder stringBuilder = new StringBuilder();
					for (int i = 0; i < textFlow.getChildren().size(); i++) {
						Text text = (Text) textFlow.getChildren().get(i);
						stringBuilder.append(text.getText());
					}
					printWriter.println(stringBuilder.toString());
				} else if (node.toString().contains("TextArea")) {
					TextArea textArea = (TextArea) node;
					String text = textArea.getText();
					printWriter.println(text);
					printWriter.println();
				} else if (node.toString().contains("VBox")) {
					VBox vBox = (VBox) node;
					for (javafx.scene.Node child : vBox.getChildren()) {
						if (child.toString().contains("HBox")) {
							HBox hbox = (HBox) child;
							if (hbox.getChildren().size() == 2) {
								Text promptText = (Text) hbox.getChildren().get(0);
								TextArea responseTextArea = (TextArea) hbox.getChildren().get(1);
								printWriter.println("Prompt: " + promptText.getText());
								if(responseTextArea.getText()!= null) {
									printWriter.println("Response: " + responseTextArea.getText());
								} else {
									printWriter.println("Response: ");
								}
							}
						}
					}
					printWriter.println("---");
					printWriter.println();
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.FINE, "Failed to write output form to text.");
			logger.log(Level.FINER, "Failed to write output form to text: " + e.getStackTrace());
		}
	}
	
	public void setUnEditable() {
		for(int i =0; i < formVBox.getChildren().size(); i++) {
			Object object = formVBox.getChildren().get(i);
			if(object.toString().contains("TextArea")) {
				TextArea textArea = (TextArea) object;
				textArea.setEditable(false);
			}
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public File getXmlContentFileToParse() {
		return xmlContentFileToParse;
	}

	public void setXmlContentFileToParse(File xmlContentFileToParse) {
		this.xmlContentFileToParse = xmlContentFileToParse;
	}


}
