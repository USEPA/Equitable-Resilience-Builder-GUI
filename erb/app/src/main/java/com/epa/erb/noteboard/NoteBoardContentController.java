package com.epa.erb.noteboard;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.XMLManager;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NoteBoardContentController implements Initializable{

	@FXML
	TextField categoryTextField;
	@FXML
	TitledPane howToTitledPane;
	@FXML
	TextArea howToTextArea;
	@FXML
	Label activityNameLabel;
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
	
	private App app;
	private Project project;
	private Goal goal;
	private Activity activity;
	public NoteBoardContentController(App app,Project project, Goal goal, Activity activity ) {
		this.app = app;
		this.project = project;
		this.goal = goal;
		this.activity = activity;
	}
	
	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(NoteBoardContentController.class);
	private ArrayList<PostItNoteController> postItNoteControllers = new ArrayList<PostItNoteController>();
	private ArrayList<CategorySectionController> categorySectionControllers = new ArrayList<CategorySectionController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		checkForExistingNoteBoardData();
		setActivityNameLabelText(activity.getLongName());
	}
		
	private void handleControls() {
		howToTitledPane.setStyle("-fx-box-border: transparent;");
		howToTextArea.getStylesheets().add("/textArea.css");
		layer1Pane.setStyle("-fx-background-color: " + constants.getLayer1ColorString() + ";");
		layer2Pane.setStyle("-fx-background-color: " + constants.getLayer2ColorString() + ";");
		layer3Pane.setStyle("-fx-background-color: " + constants.getLayer3ColorString() + ";");
		layer4Pane.setStyle("-fx-background-color: " + constants.getLayer4ColorString() + ";");
		note.setStyle("-fx-background-color: " + constants.getLayer5ColorString() + ";");
		setDrag(note, null);
	}

	@FXML
	public void noteClicked() {
		
	}
	
	@FXML
	public void addCategoryButtonAction() {
		String categoryString = categoryTextField.getText();
		if(categoryString != null && categoryString.trim().length() > 0) {
			HBox catHBox = (HBox) loadCategorySection(categoryString);
			mainVBox.getChildren().add(catHBox);
			categoryTextField.setText(null);
			activity.setSaved(false);
		}
	}
	
	private Parent loadCategorySection(String category) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/CategorySection.fxml"));
			CategorySectionController categorySectionController = new CategorySectionController(category);
			fxmlLoader.setController(categorySectionController);
			HBox catHBox = fxmlLoader.load();
			catHBox.setOnContextMenuRequested(e-> showCategoryContextMenu(catHBox, e));
			setDrag(categorySectionController.getPostItHBox(), categorySectionController);
			VBox.setVgrow(catHBox, Priority.ALWAYS);
			categorySectionControllers.add(categorySectionController);
			return catHBox;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private Pane loadPostItNote(CategorySectionController categorySectionController) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/PostItNote.fxml"));
			PostItNoteController postItNoteController = new PostItNoteController(this);
			fxmlLoader.setController(postItNoteController);
			VBox postItNotePane = fxmlLoader.load();
			postItNotePane.setOnContextMenuRequested(e-> showPostItNoteContextMenu(postItNoteController, postItNotePane, e));
			setDrag(postItNotePane, categorySectionController);
			postItNoteControllers.add(postItNoteController);
			return postItNotePane;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private void checkForExistingNoteBoardData() {
		boolean dataExists = noteBoardDataExists(project, goal, activity);
		if (dataExists) {
			generateExistingNoteBoardControls(project, goal, activity);
		}
	}
	
	private void generateExistingNoteBoardControls(Project project, Goal goal, Activity activity) {
		try {
			XMLManager xmlManager = new XMLManager(app);
			ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> listOfCategoryHashMaps = xmlManager.parseNoteboardDataXML(fileHandler.getActivityDataXMLFile(project, goal, activity));
			if(listOfCategoryHashMaps != null) {
			for (int i = 0; i < listOfCategoryHashMaps.size(); i++) {
				HashMap<String, ArrayList<HashMap<String, String>>> categoryHashMap = listOfCategoryHashMaps.get(i);
				for (String categoryName : categoryHashMap.keySet()) {
					HBox catHBox = (HBox) loadCategorySection(categoryName);
					ArrayList<HashMap<String, String>> listOfNoteHashMaps = categoryHashMap.get(categoryName);
					for (int j = 0; j < listOfNoteHashMaps.size(); j++) {
						CategorySectionController categorySectionController = getCategorySectionController(catHBox);
						Pane postItNotePane = loadPostItNote(categorySectionController);
						HashMap<String, String> noteHashMap = listOfNoteHashMaps.get(j);
						String color = noteHashMap.get("color").replaceAll("#", "");
						String content = noteHashMap.get("content");
						String like = noteHashMap.get("likes");
						setPostItNoteProperties((VBox) postItNotePane, color, content, like);
						int index = Integer.parseInt(noteHashMap.get("position"));
						categorySectionController.getPostItHBox().getChildren().add(index, postItNotePane);
					}
					mainVBox.getChildren().add(catHBox);
				}
			}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private boolean noteBoardDataExists(Project project, Goal goal, Activity activity) {
		File activityDataFile = fileHandler.getActivityDataXMLFile(project, goal, activity);
		if(activityDataFile != null && activityDataFile.exists()) {
			return true;
		}
		return false;
	}
	
	ContextMenu categoryContextMenu = null;
	private void showCategoryContextMenu(HBox catHBox, ContextMenuEvent event) {
		if (event != null) {
			if (noteContextMenu == null || !noteContextMenu.isShowing()) {
				categoryContextMenu = new ContextMenu();
				MenuItem menuItem = new MenuItem("Remove Category");
				categoryContextMenu.getItems().add(menuItem);
				menuItem.setOnAction(e -> removeCategoryMenuItemClicked(catHBox));
				categoryContextMenu.show(catHBox, event.getScreenX(), event.getScreenY());
			}
		} else {
			logger.error("Cannot showCategoryContextMenu. event is null.");
		}
	}

	ContextMenu noteContextMenu = null;
	private void showPostItNoteContextMenu(PostItNoteController postItNoteController, VBox postItNotePane,
			ContextMenuEvent event) {
		if (event != null) {
			noteContextMenu = new ContextMenu();
			MenuItem menuItem = new MenuItem("Remove Note");
			noteContextMenu.getItems().add(menuItem);
			menuItem.setOnAction(e -> removeNoteMenuItemClicked(postItNoteController, postItNotePane));
			noteContextMenu.show(postItNotePane, event.getScreenX(), event.getScreenY());
		} else {
			logger.error("Cannot showPostItNoteContextMenu. event is null.");
		}
	}
	
	private void removeCategoryMenuItemClicked(HBox catHBox) {
		removeCategory(catHBox);
		activity.setSaved(false);
	}
	
	private void removeCategory(HBox catHBox) {
		if (catHBox != null) {
			CategorySectionController categorySectionController = getCategorySectionController(catHBox);
			mainVBox.getChildren().remove(catHBox);
			for (PostItNoteController postItNoteController : categorySectionController.getListOfPostItNoteControllers()) {
				postItNoteControllers.remove(postItNoteController);
			}
			categorySectionControllers.remove(categorySectionController);
		} else {
			logger.error("Cannot removeCategory. catHBox is null.");
		}
	}
	
	private void removeNoteMenuItemClicked(PostItNoteController postItNoteController, VBox postItNotePane) {
		removeNote(postItNoteController, postItNotePane);
		getActivity().setSaved(false);
	}
	
	private void removeNote(PostItNoteController postItNoteController, VBox postItNotePane) {
		if (postItNotePane != null) {
			HBox postItHBox = (HBox) postItNotePane.getParent();
			postItHBox.getChildren().remove(postItNotePane);
			removePostItNoteController(postItNoteController);
		} else {
			logger.error("Cannot removeNote. postItNotePane is null.");
		}
	}

	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	private void setDrag(Pane p, CategorySectionController categorySectionController) {
		p.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		p.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				Pane target = p;
				Pane sourceNote = (Pane) event.getGestureSource();
				// Adding a new post it
				if (sourceNote.getId() != null && sourceNote.getId().contentEquals("note") && target.getId() != null && target.getId().contentEquals("postItHBox")) {
					Pane postItNotePane = loadPostItNote(categorySectionController);
					target.getChildren().add(postItNotePane);
					activity.setSaved(false);
					// Moving a post it
				} else if (sourceNote.getId() != null && sourceNote.getId().contentEquals("postedNote") && target.getId() != null && target.getId().contentEquals("postItHBox")) {
					VBox sourcePostedNote = (VBox) sourceNote;
					HBox sourcePostItHBox = (HBox) sourceNote.getParent();
					HBox targetPostItHBox = (HBox) target;
					if (sourcePostItHBox.hashCode() == targetPostItHBox.hashCode()) { // Moving in same cat
						int targetIndex = sourcePostItHBox.getChildren().size() - 1;
						sourcePostItHBox.getChildren().remove(sourcePostedNote);
						sourcePostItHBox.getChildren().add(targetIndex, sourcePostedNote);
					} else { // moving in diff cat
						int targetIndex = targetPostItHBox.getChildren().size();
						sourcePostItHBox.getChildren().remove(sourcePostedNote);
						targetPostItHBox.getChildren().add(targetIndex, sourcePostedNote);
					}
					activity.setSaved(false);
					// Moving a post it
				} else if (sourceNote.getId() != null && sourceNote.getId().contentEquals("postedNote") && target.getId() != null && target.getId().contentEquals("postedNote")) {
					VBox sourcePostedNote = (VBox) sourceNote;
					HBox sourcePostItHBox = (HBox) sourceNote.getParent();
					VBox targetPostedNote = (VBox) target;
					HBox targetPostItHBox = (HBox) targetPostedNote.getParent();
					if (sourcePostItHBox.hashCode() == targetPostItHBox.hashCode()) { // Moving in same cat
						int targetIndex = sourcePostItHBox.getChildren().indexOf(targetPostedNote);
						sourcePostItHBox.getChildren().remove(sourcePostedNote);
						sourcePostItHBox.getChildren().add(targetIndex, sourcePostedNote);
					} else { // moving in diff cat
						int targetIndex = targetPostItHBox.getChildren().indexOf(targetPostedNote);
						sourcePostItHBox.getChildren().remove(sourcePostedNote);
						targetPostItHBox.getChildren().add(targetIndex, sourcePostedNote);
					}
					activity.setSaved(false);
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

	private CategorySectionController getCategorySectionController(HBox categoryHBox) {
		if (categoryHBox != null) {
			for (CategorySectionController categorySectionController : categorySectionControllers) {
				if (categorySectionController.getCategoryHBox().hashCode() == categoryHBox.hashCode()) {
					return categorySectionController;
				}
			}
			logger.debug("CategorySectionController returned is null.");
			return null;
		} else {
			logger.error("Cannot getCategorySectionController. categoryHBox is null.");
			return null;
		}
	}

	private PostItNoteController getPostItNoteController(VBox postItNotePane) {
		if (postItNotePane != null) {
			for (PostItNoteController postItNoteController : postItNoteControllers) {
				if (postItNoteController.getPostItNotePane().hashCode() == postItNotePane.hashCode()) {
					return postItNoteController;
				}
			}
			logger.debug("PostItNoteController returned is null.");
			return null;
		} else {
			logger.error("Cannot getPostItNoteController. postItNotePane is null.");
			return null;
		}
	}
	
	public void setCategoryPostIts() {
		for(CategorySectionController categorySectionController: categorySectionControllers) {
			ArrayList<PostItNoteController> assignedPostItNoteControllers = new ArrayList<PostItNoteController>();
			for(PostItNoteController postItNoteController: postItNoteControllers) {				
				if(postItNoteController.getPostItNotePane().getParent().hashCode() == categorySectionController.getPostItHBox().hashCode()) {
					assignedPostItNoteControllers.add(postItNoteController);
				}
			}
			categorySectionController.setListOfPostItNoteControllers(assignedPostItNoteControllers);
		}
	}
	
	private void setPostItNoteProperties(VBox postItNotePane, String color, String content, String likes) {
		PostItNoteController postItNoteController = getPostItNoteController(postItNotePane);
		postItNoteController.setPostItContentsColor(color);
		postItNoteController.setPostItNoteText(content);
		postItNoteController.setNumberLabelText(likes);
	}
	
	private void setActivityNameLabelText(String text) {
		activityNameLabel.setText(text);
	}
	
	void removePostItNoteController(PostItNoteController postItNoteController) {
		postItNoteControllers.remove(postItNoteController);
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

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ArrayList<PostItNoteController> getPostItNoteControllers() {
		return postItNoteControllers;
	}

	public void setPostItNoteControllers(ArrayList<PostItNoteController> postItNoteControllers) {
		this.postItNoteControllers = postItNoteControllers;
	}

	public ArrayList<CategorySectionController> getCategorySectionControllers() {
		return categorySectionControllers;
	}

	public void setCategorySectionControllers(ArrayList<CategorySectionController> categorySectionControllers) {
		this.categorySectionControllers = categorySectionControllers;
	}

	public TextField getCategoryTextField() {
		return categoryTextField;
	}

	public Label getActivityNameLabel() {
		return activityNameLabel;
	}

	public VBox getMainVBox() {
		return mainVBox;
	}

	public Pane getLayer1Pane() {
		return layer1Pane;
	}

	public Pane getLayer2Pane() {
		return layer2Pane;
	}

	public Pane getLayer3Pane() {
		return layer3Pane;
	}

	public Pane getLayer4Pane() {
		return layer4Pane;
	}

	public Pane getNote() {
		return note;
	}
		
}
