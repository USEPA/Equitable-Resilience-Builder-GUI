package com.epa.erb.engagement_action;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.epa.erb.utility.IdAssignments;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import java.io.File;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import com.epa.erb.utility.XMLManager;
import javafx.stage.FileChooser;


public class MyPortfolioController implements Initializable {

	@FXML
	TreeView<String> myPortfolioTreeView;
	@FXML
	TableView<MyUploadedItem> tableView;
	@FXML
	TableColumn<MyUploadedItem, Boolean> selectColumn;
	@FXML
	TableColumn<MyUploadedItem, Integer> numberColumn;
	@FXML
	TableColumn<MyUploadedItem, Text> nameColumn;
	@FXML
	TableColumn<MyUploadedItem, String> modifiedColumn;
	
	private HashMap<String, ArrayList<MyUploadedItem>> hash = new HashMap<String, ArrayList<MyUploadedItem>>();
	
	private App app;
	private Project project;
	private Goal goal;
	public MyPortfolioController(App app, Project project, Goal goal) {
		this.app = app;
		this.project = project;
		this.goal = goal;
	}
	
	private FileHandler fileHandler = new FileHandler();
	private XMLManager xmlManager = new XMLManager(app);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		fillTreeView();
		myPortfolioTreeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> treeViewClicked(oldValue, newValue));
		myPortfolioTreeView.getSelectionModel().clearAndSelect(0);
	}
	
	public void treeViewClicked(TreeItem<String> oldItem, TreeItem<String> newItem) {
		if (newItem != null) {			
			String itemClicked = newItem.getValue();
			if (itemClicked != null) {
				tableView.getItems().clear();
				String xmlSection = itemClicked;
				ArrayList<MyUploadedItem> items = hash.get(xmlSection);
				for(MyUploadedItem uT:items) {
					tableView.getItems().add(uT);

				}		
			}
		}
		tableView.refresh();
	}
	
	@FXML
	public void exportButtonAction() {
		HashMap<String, ArrayList<MyUploadedItem>> exportHash = getItemsReadyForExport();
		if(exportHash != null && exportHash.size() > 0) {
			FileChooser fileChooser = new FileChooser();
			File saveFile = fileChooser.showSaveDialog(null);
			if(saveFile != null) {
				if(!saveFile.exists()) saveFile.mkdir();
				for(String section: exportHash.keySet()) {
					File sectionDir = new File(saveFile.getPath() + "\\" + section);
					if(!sectionDir.exists()) sectionDir.mkdir();
					
					for(MyUploadedItem ut: exportHash.get(section)) {
						File sourceFile = null;
						if(section.contentEquals("Uploaded")) {
							sourceFile = new File(fileHandler.getMyUploadsDirectory(project, goal) + "\\" + ut.getFileNumber() + "\\" + ut.getFileName().getText());
						}else {
							sourceFile = new File(fileHandler.getSupportingDOCDirectory(project, goal) + "\\" + ut.getFileName().getText());
						}
						File destFile = new File(sectionDir.getPath() + "\\" + ut.getFileName().getText());
						fileHandler.copyFile(sourceFile, destFile);
					}
				}
			}
		}
	}
	
	private HashMap<String, ArrayList<MyUploadedItem>> getItemsReadyForExport(){
		HashMap<String, ArrayList<MyUploadedItem>> exportHash = new HashMap<String, ArrayList<MyUploadedItem>>();
		for(String section: hash.keySet()) {
			ArrayList<MyUploadedItem> items = hash.get(section);
			ArrayList<MyUploadedItem> readyItems = new ArrayList<MyUploadedItem>();
			for(MyUploadedItem item: items) {
				if(item.isSelectedForExport()) {
					readyItems.add(item);
				}
			}
			if(readyItems.size() >0) exportHash.put(section, readyItems);
		}
		return exportHash;
	}
	
	private void fillTreeView() {
		IdAssignments id = new IdAssignments();
		ERBContentItem rootERBContentItem = new ERBContentItem("91", null, "mainForm", null, "ERB Sections", "ERB Sections");
		ArrayList<String> rootFileNames = xmlManager.parseWorksheetsXML(fileHandler.getStaticWorksheetsXMLFile(), rootERBContentItem.getShortName());			
		hash.put(rootERBContentItem.getShortName(), createListOfUploadedItems(rootFileNames));
		TreeItem<String> rootTreeItem = new TreeItem<String>(rootERBContentItem.getLongName());
		rootTreeItem.setExpanded(true);
		myPortfolioTreeView.setRoot(rootTreeItem);

		for (ERBContentItem contentItem : app.getAvailableERBContentItems()) {
			if (id.getChapterIdAssignments().contains(contentItem.getId())) {
				TreeItem<String> treeItem = new TreeItem<String>(contentItem.getLongName());
				ArrayList<String> fileNames = xmlManager.parseWorksheetsXML(fileHandler.getStaticWorksheetsXMLFile(), contentItem.getShortName());			
				hash.put(contentItem.getShortName(), createListOfUploadedItems(fileNames));		
				rootTreeItem.getChildren().add(treeItem);
			}
		}
		
		ERBContentItem uploadedERBContentItem = new ERBContentItem(null, null, "mainForm", null, "Uploaded", "Uploaded");
		hash.put(uploadedERBContentItem.getShortName(), getListOfUserUploadedItems());
		TreeItem<String> uploadedTreeItem = new TreeItem<String>(uploadedERBContentItem.getShortName());
		rootTreeItem.getChildren().add(uploadedTreeItem);
		
	}
	
	private ArrayList<MyUploadedItem> getListOfUserUploadedItems(){
		ArrayList<MyUploadedItem> uploadedItems = new ArrayList<MyUploadedItem>();
		File uploadsDirectory = fileHandler.getMyUploadsDirectory(project, goal);
		if (uploadsDirectory != null && uploadsDirectory.exists()) {
			for(File uploadedDir: uploadsDirectory.listFiles()) {
				int fileNumber = Integer.parseInt(uploadedDir.getName());
				Text fileName = new Text("");
				String lastModified = "";
				String uploadedFrom = "";
				
				for(File uploadedFile : uploadedDir.listFiles()) {
					if(uploadedFile.getName().contentEquals("about.txt")) {
						try {
							Scanner scanner = new Scanner(uploadedFile);
							while(scanner.hasNextLine()) {
								String line = scanner.nextLine();
								String [] split = line.split(":");
								uploadedFrom = split[1].trim();
							}
							scanner.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						
					} else {
						long modifiedLong = uploadedFile.lastModified();
						lastModified = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(modifiedLong));
						fileName = new Text(uploadedFile.getName());
					}
				}
				MyUploadedItem myUploadedItem = new MyUploadedItem(fileNumber, fileName, lastModified, uploadedFrom);
				uploadedItems.add(myUploadedItem);
			}
		}
		return uploadedItems;
	}
	
	private ArrayList<MyUploadedItem> createListOfUploadedItems(ArrayList<String> fileNames){
		ArrayList<MyUploadedItem> itemsForSection = new ArrayList<MyUploadedItem>();
		int count =1;
		for(String file: fileNames) {
			MyUploadedItem uT = createUploadedItem(file, count);
			itemsForSection.add(uT);
			count++;
		}
		return itemsForSection;
	}
	
	private MyUploadedItem createUploadedItem(String file, int count) {
		File f = new File(fileHandler.getSupportingDOCDirectory(project, goal) + "\\" + file);
		Text fileName = new Text(file);
		long modifiedLong = f.lastModified();
		String lastModified = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(modifiedLong));
		MyUploadedItem uT = new MyUploadedItem(false, count, fileName, lastModified );
		return uT;
	}

	public void initTableView() {
		selectColumn.setCellValueFactory(new PropertyValueFactory("selectedForExport"));
		selectColumn
				.setCellFactory(new Callback<TableColumn<MyUploadedItem, Boolean>, TableCell<MyUploadedItem, Boolean>>() {
					@Override
					public TableCell<MyUploadedItem, Boolean> call(TableColumn<MyUploadedItem, Boolean> arg0) {
						return new TableCheckCell(tableView);
					}
				});
		
		numberColumn.setCellValueFactory(
		        new PropertyValueFactory<MyUploadedItem,Integer>("fileNumber")
		    );
		nameColumn.setCellValueFactory(
		        new PropertyValueFactory<MyUploadedItem,Text>("fileName")
		    );
		nameColumn.setCellFactory(new Callback<TableColumn<MyUploadedItem, Text>, TableCell<MyUploadedItem, Text>>() {
		        public TableCell call(TableColumn param) {
		            return new TableCell<MyUploadedItem, Text>() {
		                @Override
		                public void updateItem(Text item, boolean empty) {
		                    super.updateItem(item, empty);
		                    if (!isEmpty()) {		                        
		                        setGraphic(item);
		                    }
		                }
		            };
		        }
		    });
		
		
		modifiedColumn.setCellValueFactory(
		        new PropertyValueFactory<MyUploadedItem,String>("modifiedDate")
		    );
	}
	
	private class TableCheckCell extends TableCell<MyUploadedItem, Boolean> {
		final CheckBox checkBox = new CheckBox();
		TableCheckCell(final TableView<MyUploadedItem> tblView) {
			checkBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectedIndex = getTableRow().getIndex();
					MyUploadedItem myUploadedItem = tableView.getItems().get(selectedIndex);
					if (checkBox.isSelected()) {
						myUploadedItem.setSelectedForExport(true);
					} else {
						myUploadedItem.setSelectedForExport(false);
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
