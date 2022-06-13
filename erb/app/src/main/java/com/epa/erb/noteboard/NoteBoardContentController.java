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
import com.epa.erb.Constants;
import com.epa.erb.XMLManager;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
	TextField categoryTextField;
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
	private Logger logger = LogManager.getLogger(NoteBoardContentController.class);
	private String pathToERBProjectsFolder = constants.getPathToLocalERBProjectsFolder();
	private ArrayList<PostItNoteController> postItNoteControllers = new ArrayList<PostItNoteController>();
	private ArrayList<CategorySectionController> categorySectionControllers = new ArrayList<CategorySectionController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		checkForExistingNoteBoardData();
		setActivityNameLabelText(activity.getLongName());
	}
		
	private void handleControls() {
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
	
	private void checkForExistingNoteBoardData() {
		boolean dataExists = noteBoardDataExists(project, goal, activity);
		if (dataExists) {
			try {
				XMLManager xmlManager = new XMLManager(app);
				ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> listOfCategoryHashMaps = xmlManager.parseActivityXML(getActivityDataFile(project, goal, activity));
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
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	private void setPostItNoteProperties(VBox postItNotePane, String color, String content, String likes) {
		PostItNoteController postItNoteController = getPostItNoteController(postItNotePane);
		postItNoteController.setPostItContentsColor(color);
		postItNoteController.setPostItNoteText(content);
		postItNoteController.setNumberLabelText(likes);
	}
	
	private Parent loadCategorySection(String category) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/CategorySection.fxml"));
			CategorySectionController categorySectionController = new CategorySectionController((category));
			fxmlLoader.setController(categorySectionController);
			HBox catHBox = fxmlLoader.load();
			catHBox.setOnContextMenuRequested(e-> createCategoryContextMenu(catHBox).show(mainVBox.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
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
			Pane postItNotePane = fxmlLoader.load();
			setDrag(postItNotePane, categorySectionController);
			postItNoteControllers.add(postItNoteController);
			return postItNotePane;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private boolean noteBoardDataExists(Project project, Goal goal, Activity activity) {
		File activityDataFile = getActivityDataFile(project, goal, activity);
		if(activityDataFile.exists()) {
			return true;
		}
		return false;
	}
	
	private ContextMenu createCategoryContextMenu(HBox catHBox) {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem("Remove");
		contextMenu.getItems().add(menuItem);
		menuItem.setOnAction(e -> removeCategory(catHBox));
		return contextMenu;
	}
	
	private void removeCategory(HBox catHBox) {
		CategorySectionController categorySectionController = getCategorySectionController(catHBox);
		mainVBox.getChildren().remove(catHBox);
		for(PostItNoteController postItNoteController: categorySectionController.getListOfPostItNoteControllers()) {
			postItNoteControllers.remove(postItNoteController);
		}
		categorySectionControllers.remove(categorySectionController);
		activity.setSaved(false);
	}

	private int indexToMove = -1;
	private int sourcePaneHashCode = -1;
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	private void setDrag(Pane p, CategorySectionController categorySectionController) {
		p.setOnDragOver(event-> {
			event.acceptTransferModes(TransferMode.MOVE);
			Pane sourceNote = (Pane) event.getSource();
			Pane sourcePane = (Pane) sourceNote.getParent();
			Pane targetPane = (Pane) event.getTarget();
			if (targetPane != null) {
				if(targetPane.toString().contains("TextFlow")) {
					VBox sourceNoteVBox = (VBox) sourceNote;
					if(sourceNoteVBox.getId().contentEquals("postedNote")) {
						sourcePaneHashCode = sourcePane.hashCode();
						indexToMove = sourcePane.getChildren().indexOf(sourceNoteVBox);
					}
				}
			}
			event.consume();
		});
		p.setOnDragDropped(event-> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasString()) {
				Pane targetPane = p;
				Pane sourceNote = (Pane) event.getGestureSource();
				//Adding a new post it
				if(sourceNote.getId() != null && sourceNote.getId().contentEquals("note") && targetPane.getId() != null && targetPane.getId().contentEquals("postItHBox")) {
						Pane postItNotePane = loadPostItNote(categorySectionController);
						targetPane.getChildren().add(postItNotePane);
						activity.setSaved(false);
						// Moving a post it
				} else if (sourceNote.getId() != null && sourceNote.getId().contentEquals("postedNote") && targetPane.getId() != null && targetPane.getId().contentEquals("postItHBox")) {
					if(sourcePaneHashCode == targetPane.hashCode()) {
						if (indexToMove > -1) {
							if (targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().remove(sourceNote);
							if(!targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().add(indexToMove, sourceNote);
						}
					} else {
						int numChildren = targetPane.getChildren().size();
						if (targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().remove(sourceNote);
						if(!targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().add(numChildren, sourceNote);
					}
					activity.setSaved(false);
				//Moving a post it
				} else if(sourceNote.getId() != null && sourceNote.getId().contentEquals("postedNote") && targetPane.getId() != null && targetPane.getId().contentEquals("postedNote")) {
					Pane sourceParentPane = (Pane) sourceNote.getParent();
					TextFlow textFlow = (TextFlow) event.getTarget();
					VBox noteVBox = (VBox) textFlow.getParent().getParent().getParent().getParent().getParent();
					int targetIndex = sourceParentPane.getChildren().indexOf(noteVBox);
					sourceParentPane.getChildren().remove(sourceNote);
					sourceParentPane.getChildren().add(targetIndex, sourceNote);
					activity.setSaved(false);
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
	
	private CategorySectionController getCategorySectionController(HBox categoryHBox) {
		for(CategorySectionController categorySectionController : categorySectionControllers) {
			if(categorySectionController.getCategoryHBox().hashCode() == categoryHBox.hashCode()) {
				return categorySectionController;
			}
		}
		logger.debug("CategorySectionController returned is null.");
		return null;
	}
	
	private PostItNoteController getPostItNoteController(VBox postItNotePane) {
		for(PostItNoteController postItNoteController: postItNoteControllers) {
			if(postItNoteController.getPostItNotePane().hashCode() == postItNotePane.hashCode()) {
				return postItNoteController;
			}
		}
		logger.debug("PostItNoteController returned is null.");
		return null;
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
	
	private File getActivityDataFile(Project project, Goal goal, Activity activity) {
		return new File(pathToERBProjectsFolder + "\\" + project.getProjectName() + "\\Goals\\" + goal.getGoalName() + "\\Activities\\" + activity.getActivityID() + "\\Data.xml");
	}
	
	void removePostItNoteController(PostItNoteController postItNoteController) {
		postItNoteControllers.remove(postItNoteController);
	}
	
	private void setActivityNameLabelText(String text) {
		activityNameLabel.setText(text);
	}

	public Activity getActivity() {
		return activity;
	}

	public ArrayList<CategorySectionController> getCategorySectionControllers() {
		return categorySectionControllers;
	}
	
}
