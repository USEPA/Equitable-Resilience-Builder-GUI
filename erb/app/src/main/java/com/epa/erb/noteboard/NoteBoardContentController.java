package com.epa.erb.noteboard;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
	Label titleLabel;
	@FXML
	VBox mainVBox;
	@FXML
	VBox centerVBox;
	@FXML
	VBox noteBoardItemVBox;
	
	//---------
	@FXML
	HBox topLabelHBox;
	@FXML
	HBox bottomLabelHBox;
	@FXML
	VBox rightLabelVBox;
	@FXML
	VBox leftLabelVBox;
	//---------
	
	protected App app;
	private Project project;
	private Goal goal;
	private ERBContentItem erbContentItem;
	public NoteBoardContentController(App app,Project project, Goal goal, ERBContentItem erbContentItem ) {
		this.app = app;
		this.project = project;
		this.goal = goal;
		this.erbContentItem = erbContentItem;
	}
	
	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(NoteBoardContentController.class);
	protected ArrayList<NoteBoardItemController> noteBoardItemControllers = new ArrayList<NoteBoardItemController>();
	private ArrayList<NoteBoardRowController> noteBoardRowControllers = new ArrayList<NoteBoardRowController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//checkForExistingNoteBoardData();
		setTitleText(erbContentItem.getLongName());
		setDrag_NoteBoardItemVBox(noteBoardItemVBox);
	}
	
	public void createRows(int numberOfRows) {
		if(numberOfRows>0) {
			for(int i =0; i < numberOfRows; i++) {
				HBox rowHBox = (HBox) loadNoteBoardRow();
				mainVBox.getChildren().add(rowHBox);
			}
		}
	}
	
	private Parent loadNoteBoardRow() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardRow.fxml"));
			NoteBoardRowController noteBoardRowController = new NoteBoardRowController();
			fxmlLoader.setController(noteBoardRowController);
			HBox rowHBox = fxmlLoader.load();
//			rowHBox.setOnContextMenuRequested(e-> showNoteBoardRowContextMenu(rowHBox, e));
			//setDrag(noteBoardRowController.getPostItHBox(), noteBoardRowController);
			VBox.setVgrow(rowHBox, Priority.ALWAYS);
			noteBoardRowControllers.add(noteBoardRowController);
			return rowHBox;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	@FXML
	public void saveBoardButtonAction() {
		XMLManager xmlManager = new XMLManager(app);
//		setCategoryPostIts();
		ArrayList<NoteBoardRowController> categories = getRowControllers();
		xmlManager.writeNoteboardDataXML(fileHandler.getDataXMLFile(project, goal, erbContentItem), categories);
	}
	
	protected Pane loadIndicatorCard(String cardText) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardItem.fxml"));
			NoteBoardItem_Indicator noteBoardIndicatorItem = new NoteBoardItem_Indicator(this);
			fxmlLoader.setController(noteBoardIndicatorItem);
			VBox vBox = fxmlLoader.load();
			noteBoardIndicatorItem.setNoteBoardItemText(cardText);
			noteBoardIndicatorItem.setDrag_IndicatorCard(vBox);
			noteBoardItemControllers.add(noteBoardIndicatorItem);
			return vBox;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	protected void setDrag_NoteBoardItemVBox(Pane p) {
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
				if (targetVBox.getChildren().contains(sourceItemVBox)) { // Reorder
					int targetIndex = targetVBox.getChildren().size() - 1;
					targetVBox.getChildren().remove(sourceItemVBox);
					targetVBox.getChildren().add(targetIndex, sourceItemVBox);
				} else {
					targetVBox.getChildren().add(0, sourceItemVBox); // Add
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
	
//	private Pane loadNoteBoardItem(NoteBoardRowController categorySectionController) {
//		try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardItem.fxml"));
//			NoteBoardItemController postItNoteController = new NoteBoardItemController(this);
//			fxmlLoader.setController(postItNoteController);
//			VBox postItNotePane = fxmlLoader.load();
////			postItNotePane.setOnContextMenuRequested(e-> showPostItNoteContextMenu(postItNoteController, postItNotePane, e));
//			//setDrag(postItNotePane, categorySectionController);
//			noteBoardItemControllers.add(postItNoteController);
//			return postItNotePane;
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return null;
//		}
//	}
	
//	private void checkForExistingNoteBoardData() {
//		boolean dataExists = noteBoardDataExists(project, goal);
//		if (dataExists) {
//			generateExistingNoteBoardControls(project, goal);
//		}
//	}
	
//	private void generateExistingNoteBoardControls(Project project, Goal goal) {
//		try {
//			XMLManager xmlManager = new XMLManager(app);
//			ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> listOfCategoryHashMaps = xmlManager.parseNoteboardDataXML(fileHandler.getDataXMLFile(project, goal, erbContentItem));
//			if(listOfCategoryHashMaps != null) {
//			for (int i = 0; i < listOfCategoryHashMaps.size(); i++) {
//				HashMap<String, ArrayList<HashMap<String, String>>> categoryHashMap = listOfCategoryHashMaps.get(i);
//				for (String categoryName : categoryHashMap.keySet()) {
//					HBox catHBox = (HBox) loadNoteBoardRow(categoryName);
//					ArrayList<HashMap<String, String>> listOfNoteHashMaps = categoryHashMap.get(categoryName);
//					for (int j = 0; j < listOfNoteHashMaps.size(); j++) {
//						NoteBoardRowController categorySectionController = getCategorySectionController(catHBox);
//						Pane postItNotePane = loadPostItNote(categorySectionController);
//						HashMap<String, String> noteHashMap = listOfNoteHashMaps.get(j);
//						String color = noteHashMap.get("color").replaceAll("#", "");
//						String content = noteHashMap.get("content");
//						String like = noteHashMap.get("likes");
//						setPostItNoteProperties((VBox) postItNotePane, color, content, like);
//						int index = Integer.parseInt(noteHashMap.get("position"));
//						categorySectionController.getPostItHBox().getChildren().add(index, postItNotePane);
//					}
//					mainVBox.getChildren().add(catHBox);
//				}
//			}
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//	}
	
//	private boolean noteBoardDataExists(Project project, Goal goal) {
//		File activityDataFile = fileHandler.getDataXMLFile(project, goal, erbContentItem);
//		if(activityDataFile != null && activityDataFile.exists()) {
//			return true;
//		}
//		return false;
//	}
	
//	ContextMenu rowContextMenu = null;
//	private void showNoteBoardRowContextMenu(HBox catHBox, ContextMenuEvent event) {
//		if (event != null) {
//			if (noteContextMenu == null || !noteContextMenu.isShowing()) {
//				rowContextMenu = new ContextMenu();
//				MenuItem menuItem = new MenuItem("Remove Row");
//				rowContextMenu.getItems().add(menuItem);
//				menuItem.setOnAction(e -> removeRowMenuItemClicked(catHBox));
//				rowContextMenu.show(catHBox, event.getScreenX(), event.getScreenY());
//			}
//		} else {
//			logger.error("Cannot showCategoryContextMenu. event is null.");
//		}
//	}

//	ContextMenu noteContextMenu = null;
//	private void showPostItNoteContextMenu(NoteBoardItemController postItNoteController, VBox postItNotePane,
//			ContextMenuEvent event) {
//		if (event != null) {
//			noteContextMenu = new ContextMenu();
//			MenuItem menuItem = new MenuItem("Remove Note");
//			noteContextMenu.getItems().add(menuItem);
//			menuItem.setOnAction(e -> removeNoteMenuItemClicked(postItNoteController, postItNotePane));
//			noteContextMenu.show(postItNotePane, event.getScreenX(), event.getScreenY());
//		} else {
//			logger.error("Cannot showPostItNoteContextMenu. event is null.");
//		}
//	}
	
//	private void removeRowMenuItemClicked(HBox catHBox) {
//		removeRow(catHBox);
//	}
	
//	private void removeRow(HBox catHBox) {
//		if (catHBox != null) {
//			NoteBoardRowController categorySectionController = getCategorySectionController(catHBox);
//			mainVBox.getChildren().remove(catHBox);
//			for (NoteBoardItemController postItNoteController : categorySectionController.getListOfPostItNoteControllers()) {
//				noteBoardItemControllers.remove(postItNoteController);
//			}
//			noteBoardRowControllers.remove(categorySectionController);
//		} else {
//			logger.error("Cannot removeCategory. catHBox is null.");
//		}
//	}
	
//	private void removeNoteMenuItemClicked(NoteBoardItemController postItNoteController, VBox postItNotePane) {
//		removeNote(postItNoteController, postItNotePane);
//	}
	
//	private void removeNote(NoteBoardItemController postItNoteController, VBox postItNotePane) {
//		if (postItNotePane != null) {
//			HBox postItHBox = (HBox) postItNotePane.getParent();
//			postItHBox.getChildren().remove(postItNotePane);
//			removePostItNoteController(postItNoteController);
//		} else {
//			logger.error("Cannot removeNote. postItNotePane is null.");
//		}
//	}
	
//	private static final String TAB_DRAG_KEY = "pane";
//	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
//	protected void setDrag(Pane p, NoteBoardRowController categorySectionController) {
//		p.setOnDragOver(event -> {
//			event.acceptTransferModes(TransferMode.MOVE);
//			event.consume();
//		});
//		p.setOnDragDropped(event -> {
//			Dragboard db = event.getDragboard();
//			boolean success = false;
//			if (db.hasString()) {
//				Pane target = p;
//				Pane sourceItem = (Pane) event.getGestureSource();
//				// Adding a new post it
//				if (sourceItem.getId() != null && sourceItem.getId().contentEquals("note") && target.getId() != null && target.getId().contentEquals("rowHBox")) {
//					Pane postItNotePane = loadPostItNote(categorySectionController);
//					target.getChildren().add(postItNotePane);
//					// Moving a post it
//				} else if (sourceItem.getId() != null && sourceItem.getId().contentEquals("postedNote") && target.getId() != null && target.getId().contentEquals("rowHBox")) {
//					VBox sourceItemVBox = (VBox) sourceItem;
//					HBox sourcePostItHBox = (HBox) sourceItem.getParent();
//					HBox targetHBox = (HBox) target;
//					if (sourcePostItHBox.hashCode() == targetHBox.hashCode()) { // Moving in same cat
//						int targetIndex = sourcePostItHBox.getChildren().size() - 1;
//						sourcePostItHBox.getChildren().remove(sourceItemVBox);
//						sourcePostItHBox.getChildren().add(targetIndex, sourceItemVBox);
//					} else { // moving in diff cat
//						int targetIndex = targetHBox.getChildren().size();
//						sourcePostItHBox.getChildren().remove(sourceItemVBox);
//						targetHBox.getChildren().add(targetIndex, sourceItemVBox);
//					}
//					// Moving a post it
//				} else if (sourceItem.getId() != null && sourceItem.getId().contentEquals("postedNote") && target.getId() != null && target.getId().contentEquals("postedNote")) {
//					VBox sourcePostedNote = (VBox) sourceItem;
//					HBox sourcePostItHBox = (HBox) sourceItem.getParent();
//					VBox targetPostedNote = (VBox) target;
//					HBox targetPostItHBox = (HBox) targetPostedNote.getParent();
//					if (sourcePostItHBox.hashCode() == targetPostItHBox.hashCode()) { // Moving in same cat
//						int targetIndex = sourcePostItHBox.getChildren().indexOf(targetPostedNote);
//						sourcePostItHBox.getChildren().remove(sourcePostedNote);
//						sourcePostItHBox.getChildren().add(targetIndex, sourcePostedNote);
//					} else { // moving in diff cat
//						int targetIndex = targetPostItHBox.getChildren().indexOf(targetPostedNote);
//						sourcePostItHBox.getChildren().remove(sourcePostedNote);
//						targetPostItHBox.getChildren().add(targetIndex, sourcePostedNote);
//					}
//				}
//				success = true;
//			}
//			event.setDropCompleted(success);
//			event.consume();
//		});
//		p.setOnDragDetected(event -> {
//			Dragboard dragboard = p.startDragAndDrop(TransferMode.MOVE);
//			ClipboardContent clipboardContent = new ClipboardContent();
//			clipboardContent.putString(TAB_DRAG_KEY);
//			dragboard.setContent(clipboardContent);
//			draggingTab.set(p);
//			event.consume();
//		});
//	}

//	private NoteBoardRowController getCategorySectionController(HBox categoryHBox) {
//		if (categoryHBox != null) {
//			for (NoteBoardRowController categorySectionController : noteBoardRowControllers) {
//				if (categorySectionController.getCategoryHBox().hashCode() == categoryHBox.hashCode()) {
//					return categorySectionController;
//				}
//			}
//			logger.debug("CategorySectionController returned is null.");
//			return null;
//		} else {
//			logger.error("Cannot getCategorySectionController. categoryHBox is null.");
//			return null;
//		}
//	}

//	private NoteBoardItemController getPostItNoteController(VBox postItNotePane) {
//		if (postItNotePane != null) {
//			for (NoteBoardItemController postItNoteController : noteBoardItemControllers) {
//				if (postItNoteController.getPostItNotePane().hashCode() == postItNotePane.hashCode()) {
//					return postItNoteController;
//				}
//			}
//			logger.debug("PostItNoteController returned is null.");
//			return null;
//		} else {
//			logger.error("Cannot getPostItNoteController. postItNotePane is null.");
//			return null;
//		}
//	}
	
//	public void setCategoryPostIts() {
//		for(NoteBoardRowController categorySectionController: noteBoardRowControllers) {
//			ArrayList<NoteBoardItemController> assignedPostItNoteControllers = new ArrayList<NoteBoardItemController>();
//			for(NoteBoardItemController postItNoteController: noteBoardItemControllers) {				
//				if(postItNoteController.getPostItNotePane().getParent().hashCode() == categorySectionController.getPostItHBox().hashCode()) {
//					assignedPostItNoteControllers.add(postItNoteController);
//				}
//			}
//			categorySectionController.setListOfPostItNoteControllers(assignedPostItNoteControllers);
//		}
//	}
	
//	private void setPostItNoteProperties(VBox postItNotePane, String color, String content, String likes) {
//		NoteBoardItemController postItNoteController = getPostItNoteController(postItNotePane);
//		postItNoteController.setPostItContentsColor(color);
//		postItNoteController.setPostItNoteText(content);
//		postItNoteController.setNumberLabelText(likes);
//	}
//	
	private void setTitleText(String text) {
		titleLabel.setText(text);
	}
	
//	void removePostItNoteController(NoteBoardItemController postItNoteController) {
//		noteBoardItemControllers.remove(postItNoteController);
//	}

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

	public ArrayList<NoteBoardItemController> getNoteBoardItemControllers() {
		return noteBoardItemControllers;
	}

//	public void setPostItNoteControllers(ArrayList<NoteBoardItemController> postItNoteControllers) {
//		this.noteBoardItemControllers = postItNoteControllers;
//	}

	public ArrayList<NoteBoardRowController> getRowControllers() {
		return noteBoardRowControllers;
	}

//	public void setCategorySectionControllers(ArrayList<NoteBoardRowController> categorySectionControllers) {
//		this.noteBoardRowControllers = categorySectionControllers;
//	}

	public Label getTitleText() {
		return titleLabel;
	}

	public VBox getMainVBox() {
		return mainVBox;
	}
		
}
