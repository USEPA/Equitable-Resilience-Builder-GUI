package com.epa.erb.finalReport;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute.Space;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.engagement_action.MyUploadedItem;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FinalReportSelectionController implements Initializable {

	private Logger logger;
	
	private App app;
	public FinalReportSelectionController(App app) {
		this.app = app;
		
		logger = app.getLogger();
	}

	XMLManager xmlManager;
	ArrayList<MyUploadedItem> uploadedItems;
	FileHandler fileHandler = new FileHandler(app);
	ArrayList<ERBContentItem> erbUniqueContentItems;
	ArrayList<ERBContentItem> erbWorksheetContentItems;
	ArrayList<FinalReportItem> treeItems = new ArrayList<FinalReportItem>();
	ArrayList<FinalReportItem> treeViewSelectedItems = new ArrayList<FinalReportItem>();
	ArrayList<FinalReportItem> listViewSelectedItems = new ArrayList<FinalReportItem>();
	HashMap<FinalReportItem, CheckBox> treeViewCheckBoxMap = new HashMap<FinalReportItem, CheckBox>();
	HashMap<FinalReportItem, CheckBox> listViewCheckBoxMap = new HashMap<FinalReportItem, CheckBox>();
	HashMap<FinalReportItem,ERBContentItem > finalReportItemMap = new HashMap<FinalReportItem, ERBContentItem>();

	@FXML
	Button infoButton;
	@FXML
	ListView<FinalReportItem> listView;
	@FXML
	TreeView<FinalReportItem> availableDataTreeView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		xmlManager = new XMLManager(app);

		//Handle custom check boxes and objects for tree view and list view
		listView.setCellFactory(lv -> createListCell());
		availableDataTreeView.setCellFactory(tv -> createTreeCell());
		
		//List of ERB sections with unique GUIDs
		erbUniqueContentItems = app.getEngagementActionController().getListOfUniqueERBContentItems();
		
		//List of just worksheet ERB items
		erbWorksheetContentItems = getWorksheetsERBItems();
				
		//List of items to include in final report
		ArrayList<FinalReportItem> finalReportItems = xmlManager.parseReportDataXML(fileHandler.getReportDataFileFromResources());
		
		//List of all uploaded items
		uploadedItems = getListOfUserUploadedItems();
		
		populateTreeView(finalReportItems);
		
		availableDataTreeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> treeViewClicked(oldValue, newValue));
	}

	private void populateTreeView(ArrayList<FinalReportItem> finalReportItems) {		
		TreeItem<FinalReportItem> rootTreeItem = createTreeViewRoot();

		TreeItem<FinalReportItem> sectionTreeItem = null;
		for (FinalReportItem finalReportItem: finalReportItems) {			
			ERBContentItem sectionERBContentItem = findERBSectionItem(finalReportItem.getId());
			if (sectionERBContentItem != null) {
				finalReportItemMap.put(finalReportItem, sectionERBContentItem);
				sectionTreeItem = addChildrenToTreeView(finalReportItem, rootTreeItem);
			} else {
				ERBContentItem contentERBContentItem = findContentItem(finalReportItem.getId());
				finalReportItemMap.put(finalReportItem, contentERBContentItem);
				addChildrenToTreeView(finalReportItem, sectionTreeItem);
				
				treeItems.add(finalReportItem); //Only add the actual content items
			}
		}
	
		TreeItem<FinalReportItem> erbUploadsTreeItem = createTreeViewUploads();
		rootTreeItem.getChildren().add(erbUploadsTreeItem);
		
		addUploadsToTreeView(erbUploadsTreeItem);
	}
	
	private TreeItem<FinalReportItem> createTreeViewRoot(){
		ERBContentItem rootERBItem = new ERBContentItem("91", null, "mainForm", null, "ERB Sections","ERB Sections");
		FinalReportItem rootFinalReportItem = new FinalReportItem("91", "ERB Sections", null, null, null);
		finalReportItemMap.put(rootFinalReportItem, rootERBItem);
		
		TreeItem<FinalReportItem> rootTreeItem = new TreeItem<FinalReportItem>(rootFinalReportItem);
		rootTreeItem.setExpanded(true);
		availableDataTreeView.setRoot(rootTreeItem);
		return rootTreeItem;
	}
	
	private TreeItem<FinalReportItem> createTreeViewUploads() {
		ERBContentItem erbUploadsERBContentItem = new ERBContentItem("002", null, "mainForm", null, "Uploads","Uploads");
		FinalReportItem erbUploadsFinalReportItem = new FinalReportItem("002", "Uploads", null, null, null);
		finalReportItemMap.put(erbUploadsFinalReportItem, erbUploadsERBContentItem);
				
		TreeItem<FinalReportItem> erbUploadsTreeItem = new TreeItem<FinalReportItem>(erbUploadsFinalReportItem);
		erbUploadsTreeItem.setExpanded(true);
		
		return erbUploadsTreeItem;
	}
	
	private void addUploadsToTreeView(TreeItem<FinalReportItem> erbUploadsTreeItem) {
		HashMap<String, TreeItem<FinalReportItem>> mapOfUploadLocationTreeItems = new HashMap<String, TreeItem<FinalReportItem>>();

		int uploadedFromId = 1111;
		for (MyUploadedItem uploadedItem : uploadedItems) {
			String uploadedFrom = uploadedItem.getUploadedFrom();
			if (!mapOfUploadLocationTreeItems.keySet().contains(uploadedFrom) && !uploadedFrom.contains("Key Takeaways")) {

				ERBContentItem uploadLocationERBItem = new ERBContentItem(String.valueOf(uploadedFromId), null, "mainForm", null, uploadedFrom, uploadedFrom);
				FinalReportItem uploadLocationFinalReportItem = new FinalReportItem(String.valueOf(uploadedFromId), uploadedFrom, null, null, null);
				finalReportItemMap.put(uploadLocationFinalReportItem, uploadLocationERBItem);

				TreeItem<FinalReportItem> uploadLocationTreeItem = new TreeItem<FinalReportItem>(uploadLocationFinalReportItem);
				uploadLocationTreeItem.setExpanded(true);

				erbUploadsTreeItem.getChildren().add(uploadLocationTreeItem);
				mapOfUploadLocationTreeItems.put(uploadedFrom, uploadLocationTreeItem);

				uploadedFromId++;
			}
		}

		for (MyUploadedItem uploadedItem : uploadedItems) {
			String id = uploadedItem.getFileNumber() + "_upload";
			String displayName = uploadedItem.getFileName().getText();
			String uploadedFrom = uploadedItem.getUploadedFrom();

			ERBContentItem erbContentItem = new ERBContentItem(id, uploadedItem.getFile().toString(), "uploadedItem", uploadedItem.getUploadedFrom(), displayName, displayName);
			FinalReportItem finalReportItem = new FinalReportItem(id, displayName.replaceAll("\\.(.*)", ""), "0", "0", null);

			finalReportItemMap.put(finalReportItem, erbContentItem);
			treeItems.add(finalReportItem);

			TreeItem<FinalReportItem> uploadedTreeItem = new TreeItem<FinalReportItem>(finalReportItem);

			TreeItem<FinalReportItem> uploadLocationTreeItem = mapOfUploadLocationTreeItems.get(uploadedFrom);
			if (uploadLocationTreeItem != null) uploadLocationTreeItem.getChildren().add(uploadedTreeItem);
		}
	}
	
	private ArrayList<MyUploadedItem> getListOfUserUploadedItems(){
		ArrayList<MyUploadedItem> uploadedItems = new ArrayList<MyUploadedItem>();
		File uploadsDirectory = fileHandler.getMyUploadsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
		if (uploadsDirectory != null && uploadsDirectory.exists()) {
			for(File uploadDirectory: uploadsDirectory.listFiles()) {
				int fileNumber = Integer.parseInt(uploadDirectory.getName());
				Text fileName = new Text("");
				String lastModified = "";
				String uploadedFrom = "";
				
				for(File uploadedFile : uploadDirectory.listFiles()) {
					if(uploadedFile.getName().contentEquals("about.txt")) {
						uploadedFrom = parseAboutUploadFile(uploadedFile);
					} else {
						long modifiedLong = uploadedFile.lastModified();
						lastModified = new SimpleDateFormat("MM-dd-yyyy @ HH:mm:ss").format(new Date(modifiedLong));
						fileName = new Text(uploadedFile.getName());
					}
				}
				MyUploadedItem myUploadedItem = new MyUploadedItem(fileNumber, fileName, lastModified, uploadedFrom, app);
				uploadedItems.add(myUploadedItem);
			}
		}
		return uploadedItems;
	}
	
	private String parseAboutUploadFile(File aboutFile) {
		String uploadedFrom = null;
		try {
			Scanner scanner = new Scanner(aboutFile);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String [] split = line.split(":");
				uploadedFrom = split[1].trim().replaceAll("_", " ");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.FINE, "Failed to parse about upload file.");
			logger.log(Level.FINER, "Failed to parse about upload file: " + e.getStackTrace());
		}
		return uploadedFrom;
	}
	
	public TreeItem<FinalReportItem> addChildrenToTreeView(FinalReportItem finalReportItem, TreeItem<FinalReportItem> parentTreeItem) {
		TreeItem<FinalReportItem> treeItem = new TreeItem<FinalReportItem>(finalReportItem);
		treeItem.setExpanded(true);
		parentTreeItem.getChildren().add(treeItem);
		return treeItem;
	}

	public void treeViewClicked(TreeItem<FinalReportItem> oldItem, TreeItem<FinalReportItem> newItem) {
		if (newItem != null) {
			Object itemClicked = newItem.getValue();
			if (itemClicked != null) {
				FinalReportItem clickedFinalReportItem = (FinalReportItem) itemClicked;
				ERBContentItem clickedERBContentItem = finalReportItemMap.get(clickedFinalReportItem);
				//Indicator File
				if(clickedFinalReportItem.getId().contentEquals("193")) {
					File fileToOpen = findIndicatorListFile();
					if(fileToOpen != null) {
						fileHandler.openFileOnDesktop(fileToOpen);
						return;
					}else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("There is no saved data for this item yet.");
						alert.setTitle("Summary Report Item");
						alert.showAndWait();
						return;
					}
				}
				String erbType = clickedERBContentItem.getType();
				if (erbType.contentEquals("outputForm")) {
					File guidDataDir = fileHandler.getGUIDDataDirectory(app.getEngagementActionController().getProject(), app.getEngagementActionController().getCurrentGoal());
					File guidDir = new File(guidDataDir + File.separator + clickedERBContentItem.getGuid());
					File fileToOpen = findOutputFormToOpen(guidDir);
					if (fileToOpen != null) {
						fileHandler.openFileOnDesktop(fileToOpen);
						return;
					} 
				} else if (erbType.contentEquals("supportingDoc")) {
					String docName = clickedERBContentItem.getShortName();
					File supportingDocDir = fileHandler.getSupportingDOCDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
					File fileToOpen = new File(supportingDocDir + File.separator + docName);
					if (fileToOpen != null) {
						fileHandler.openFileOnDesktop(fileToOpen);
						return;
					}				
				} else if (erbType.contentEquals("uploadedItem")) {
					String filePath = clickedERBContentItem.getGuid();
					File fileToOpen = new File(filePath);
					if(fileToOpen != null) {
						fileHandler.openFileOnDesktop(fileToOpen);
						return;
					}
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("There is no saved data for this item yet.");
				alert.setTitle("Summary Report Item");
				alert.showAndWait();
			}
		}
	}
	
	private File findIndicatorListFile() {
		File indicatorsDirectory = fileHandler.getIndicatorsDirectory(app.getSelectedProject(),app.getEngagementActionController().getCurrentGoal());
		File selectedIndicatorsListFile = new File(indicatorsDirectory + File.separator + "Indicators_List.xlsx");
		if(selectedIndicatorsListFile.exists()) {
			return selectedIndicatorsListFile;
		} else {
			return null;
		}
	}

	private File findOutputFormToOpen(File guidDir) {
		if (guidDir.exists()) {
			for (File file : guidDir.listFiles()) {
				if (file.getName().contains(".txt")) {
					return file;
				}
			}
		}
		return null;
	}
	
	public void createReport(ObservableList<FinalReportItem> orderedListViewItems) {
		try {
			File goalDir = fileHandler.getGoalDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());

			String reportDocPath = goalDir + File.separator + app.getEngagementActionController().getProject().getProjectCleanedName() + "_SummaryReport.docx";

			XWPFDocument document = new XWPFDocument();
			FileOutputStream out = new FileOutputStream(new File(reportDocPath));

			// Cover Page
			writeReportDocxCoverPage(document);

			// New Page
			XWPFParagraph paragraph = document.createParagraph();
			paragraph.setPageBreak(true);

			// Content Pages
			for (FinalReportItem finalReportItem : orderedListViewItems) {

				ERBContentItem erbContentItem = finalReportItemMap.get(finalReportItem);

				String paragraphHeaderString = finalReportItem.getDisplayName();
				if (finalReportItem.getId().contains("upload")) {
					paragraphHeaderString = erbContentItem.getStatus();
				}

				XWPFParagraph paragraphHeader = document.createParagraph();
				XWPFRun paragraphHeaderRun = paragraphHeader.createRun();
				paragraphHeaderRun.setFontSize(16);
				paragraphHeaderRun.setBold(true);
				paragraphHeaderRun.setText(paragraphHeaderString);

				if (erbContentItem != null && erbContentItem.getType() != null) {
					if (erbContentItem.getType().contentEquals("outputForm")) {
						File guidDataDir = fileHandler.getGUIDDataDirectory(app.getEngagementActionController().getProject(), app.getEngagementActionController().getCurrentGoal());
						File guidDir = new File(guidDataDir + File.separator + erbContentItem.getGuid());
						File outputFormFile = findOutputFormToOpen(guidDir);
						if (outputFormFile != null) {
							FinalReportKeyTakeaways fRKT = new FinalReportKeyTakeaways(outputFormFile, app);
							XWPFParagraph contentParagraph = document.createParagraph();
							XWPFRun contentRun = contentParagraph.createRun();
							contentRun.setFontSize(14);
							formatTextForDocx(fRKT.getKeyTakeawaysFormattedText(), contentRun);
						}
					} else if (erbContentItem.getType().contentEquals("supportingDoc")) {
						String docName = erbContentItem.getShortName();
						File supportingDocDir = fileHandler.getSupportingDOCDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
						File docFile = new File(supportingDocDir + File.separator + docName);
						if (docFile != null) {
							String start = finalReportItem.getStart();
							String stop = finalReportItem.getStop();
							if (finalReportItem.getTableName() != null && finalReportItem.getTableName().trim().length() > 0) {
								FinalReportTable fRT = new FinalReportTable(docFile, finalReportItem.getTableName(), finalReportItem.getStart(), finalReportItem.getStop(), document, app);
								fRT.writeTableToReportDoc();
							} else {
								FinalReportParagraph fRP = new FinalReportParagraph(docFile, start, stop, app);
								XWPFParagraph contentParagraph = document.createParagraph();
								XWPFRun contentRun = contentParagraph.createRun();
								contentRun.setFontSize(14);
								formatTextForDocx(fRP.readWordParagraph(), contentRun);
							}
						}
					} else if (erbContentItem.getType().contentEquals("uploadedItem")) {
						File docFile = new File(erbContentItem.getGuid());
						if (docFile != null && docFile.exists()) {
							FinalReportUpload fRU = new FinalReportUpload(docFile, erbContentItem.getLongName());
							XWPFParagraph contentParagraph = document.createParagraph();
							fRU.createUploadRun(contentParagraph);
						}
					}
				} else {
					if(finalReportItem.getId().contentEquals("193")) {
						FinalReportExcel fRE = new FinalReportExcel(findIndicatorListFile(), document, app);
						fRE.createTable();
					}
				}
			}

			// Footer
			writeReportDocxFooter(document);

			document.write(out);
			out.close();
			document.close();

		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to create summary report.");
			logger.log(Level.FINER, "Failed to create summary report: " + e.getStackTrace());
		}
	}

	@FXML
	public void continueButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/finalReport/FinalReportOrdering.fxml"));
			FinalReportOrdering controller = new FinalReportOrdering(listView, this);
			fxmlLoader.setController(controller);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Summary Report Ordering");
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			stage.setWidth(app.getPopUpPrefWidth());
			stage.setHeight(app.getPopUpPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load FinalReportOrdering.fxml.");
			logger.log(Level.FINER, "Failed to load FinalReportOrdering.fxml: " + e.getStackTrace());
		}
	}

	private void writeReportDocxCoverPage(XWPFDocument reportDoc) {

		// Report Name
		String projectName = app.getEngagementActionController().getProject().getProjectName();
		String reportName = projectName + " Summary Report";
		
		XWPFParagraph reportNameParagraph = reportDoc.createParagraph();
		reportNameParagraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun reportNameRun = reportNameParagraph.createRun();
		reportNameRun.setFontSize(22);
		reportNameRun.setText(reportName);

		//New Line
		XWPFParagraph blankParagraph = reportDoc.createParagraph();
		blankParagraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun blankRun = blankParagraph.createRun();
		blankRun.setText("\n");

		// Date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.now();
		String date = dtf.format(localDate);
		String dateLabel = "Date:";
		
		XWPFParagraph dateLabelParagraph = reportDoc.createParagraph();
		dateLabelParagraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun dateLabelRun = dateLabelParagraph.createRun();
		dateLabelRun.setFontSize(18);
		dateLabelRun.setText(dateLabel);
		
		XWPFParagraph dateParagraph = reportDoc.createParagraph();
		dateParagraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun dateRun = dateParagraph.createRun();
		dateRun.setFontSize(16);
		dateRun.setText(date);

		//New Line
		XWPFParagraph blankParagraph2 = reportDoc.createParagraph();
		blankParagraph2.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun blankRun2 = blankParagraph2.createRun();
		blankRun2.setText("\n");
		
		// Author
		String authorLabel = "Authors:";
		
		XWPFParagraph authorLabelParagraph = reportDoc.createParagraph();
		authorLabelParagraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun authorLabelRun = authorLabelParagraph.createRun();
		authorLabelRun.setFontSize(18);
		authorLabelRun.setText(authorLabel);

	}

	private void writeReportDocxFooter(XWPFDocument reportDoc) {
		CTSectPr sectPr = reportDoc.getDocument().getBody().addNewSectPr(); 
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(reportDoc, sectPr); 
		// create footer
		CTP ctpFooter = CTP.Factory.newInstance();
		XWPFParagraph[] parsFooter;
		// add style (s.th.)
		CTPPr ctppr = ctpFooter.addNewPPr();
		CTString pst = ctppr.addNewPStyle();
		pst.setVal("style21");
		CTJc ctjc = ctppr.addNewJc();
		ctjc.setVal(STJc.RIGHT);
		ctppr.addNewRPr();
		// Add in word "Page "   
		CTR ctr = ctpFooter.addNewR();
		CTText t = ctr.addNewT();
		t.setStringValue("Page ");
		t.setSpace(Space.PRESERVE);
		// add everything from the footerXXX.xml you need
		ctr = ctpFooter.addNewR();
		ctr.addNewRPr();
		CTFldChar fch = ctr.addNewFldChar();
		fch.setFldCharType(STFldCharType.BEGIN);
		ctr = ctpFooter.addNewR();
		ctr.addNewInstrText().setStringValue(" PAGE ");
		ctpFooter.addNewR().addNewFldChar().setFldCharType(STFldCharType.SEPARATE);
		ctpFooter.addNewR().addNewT().setStringValue("1");
		ctpFooter.addNewR().addNewFldChar().setFldCharType(STFldCharType.END);
		XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, reportDoc);
		parsFooter = new XWPFParagraph[1];
		parsFooter[0] = footerParagraph;
		policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);
	}
	
	private void formatTextForDocx(String text, XWPFRun run) {
		  if (text.contains("\n")) {
              String[] lines = text.split("\n");
              run.setText(lines[0], 0); // set first line into XWPFRun
              for(int i=1;i<lines.length;i++){
                  // add break and insert new text
                  run.addBreak();
                  run.setText(lines[i]);
              }
          } else {
              run.setText(text, 0);
          }
	}

	@FXML
	public void rightArrowClicked() {
		ObservableList<FinalReportItem> listViewI = listView.getItems();
		ArrayList<FinalReportItem> listViewItems = new ArrayList<FinalReportItem>();
		listViewItems.addAll(listViewI);
		
		//treeItems = ordering supposed to be in
		//treeViewSelectedItems = items to add to the list view
		//listViewItems = what is already in the list view
		
		//Add selected tree items into listViewItems
		for (FinalReportItem finalReportItem : treeViewSelectedItems) {
			listViewItems.add(finalReportItem);
		}
		//listViewItems = items in list view after adding new
		
		ArrayList<FinalReportItem> tempList = new ArrayList<FinalReportItem>();
		ArrayList<FinalReportItem> toRemove = new ArrayList<FinalReportItem>();
		tempList.addAll(treeItems);
		
		//tempList = treeItems (correct ordering)
		
		//Remove all from tempList that aren't in list view
		for(FinalReportItem finalReportItem: tempList) {
			if(!listViewItems.contains(finalReportItem)) {
				toRemove.add(finalReportItem);
			}
		}
		for(FinalReportItem finalReportItem: toRemove) {
			tempList.remove(finalReportItem);
		}
		
		//Order listViewItems to match tempList
		Collections.sort(listViewItems, 
			    Comparator.comparing(item -> tempList.indexOf(item)));
		
		listView.getItems().clear();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Add items to list view
				for(FinalReportItem finalReportItem: listViewItems) {
					listView.getItems().add(finalReportItem);
				}				
			}
		});
	
		//Remove check box from treeView
		for (FinalReportItem finalReportItem : treeViewSelectedItems) {
			treeViewCheckBoxMap.get(finalReportItem).setDisable(true);
			treeViewCheckBoxMap.get(finalReportItem).setVisible(false);
			finalReportItem.setShowing(false);
		}
		
		treeViewSelectedItems.clear();

	}

	@FXML
	public void leftArrowClicked() {
		for (FinalReportItem finalReportItem : listViewSelectedItems) {
			listView.getItems().remove(finalReportItem);
			listViewCheckBoxMap.get(finalReportItem).setSelected(false);
			treeViewCheckBoxMap.get(finalReportItem).setDisable(false);
			treeViewCheckBoxMap.get(finalReportItem).setVisible(true);
			treeViewCheckBoxMap.get(finalReportItem).setSelected(false);
			finalReportItem.setShowing(true);
			finalReportItem.setChecked(false);
			
		}
		listViewSelectedItems.clear();
	}

	private TreeCell<FinalReportItem> createTreeCell() {
		return new TreeCell<FinalReportItem>() {
			@Override
			protected void updateItem(FinalReportItem item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					ERBContentItem erbContentItem = finalReportItemMap.get(item);
					if (this.getTreeItem().isLeaf() && !erbUniqueContentItems.contains(erbContentItem) && !item.getId().contentEquals("002") && (item.getId().length() !=4)) {
						CheckBox checkBox = new CheckBox();
						checkBox.setOnAction(e -> treeViewCheckBoxClicked(checkBox, this.getTreeItem()));
						checkBox.setSelected(item.isChecked);
						checkBox.setVisible(item.isShowing);
						treeViewCheckBoxMap.put(item, checkBox);
						setId("custom-tree-cell");
						setGraphic(checkBox);
						setText(item.getDisplayName());
					} else {
						setText(item.getDisplayName());
						setGraphic(null);
						setId(null);
					}
				}
			}
		};
	}

	private ListCell<FinalReportItem> createListCell() {
		return new ListCell<FinalReportItem>() {
			@Override
			protected void updateItem(FinalReportItem item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					if (listViewCheckBoxMap.size() == 0 || !listViewCheckBoxMap.containsKey(item)) {
						CheckBox checkBox = new CheckBox();
						checkBox.setOnAction(e -> listViewCheckBoxClicked(checkBox, item));
						listViewCheckBoxMap.put(item, checkBox);
						setGraphic(checkBox);
						setText(item.getDisplayName());
					} else {
						setGraphic(listViewCheckBoxMap.get(item));
						setText(item.getDisplayName());
					}
				}
			}
		};
	}

	private void treeViewCheckBoxClicked(CheckBox checkBox, TreeItem<FinalReportItem> treeItem) {
		if (checkBox.isSelected()) {
			treeViewSelectedItems.add(treeItem.getValue());
			treeItem.getValue().setChecked(true);
		} else {
			treeViewSelectedItems.remove(treeItem.getValue());
			treeItem.getValue().setChecked(false);
		}
	}

	private void listViewCheckBoxClicked(CheckBox checkBox, FinalReportItem finalReportItem) {
		if (checkBox.isSelected()) {
			listViewSelectedItems.add(finalReportItem);
		} else {
			listViewSelectedItems.remove(finalReportItem);
		}
	}
	
	private ArrayList<ERBContentItem> getWorksheetsERBItems() {
		erbWorksheetContentItems = new ArrayList<ERBContentItem>();
		for(ERBContentItem item: app.getAvailableERBContentItems()) {
			if(item.getType().contentEquals("supportingDoc")) {
				erbWorksheetContentItems.add(item);
			}
		}
		return erbWorksheetContentItems;
	}

	
	private ERBContentItem findERBSectionItem(String reportId) {
		for(ERBContentItem item: erbUniqueContentItems) {
			if(item.getId().contentEquals(reportId)) {
				return item;
			}
		}
		return null;
	}
	
	
	public ERBContentItem findContentItem(String reportId) {
		for(ERBContentItem item: erbWorksheetContentItems) {
			if(item.getId().contentEquals(reportId)) {
				return item;
			}
		}
		for(ERBContentItem item: erbUniqueContentItems) {
			if(item.getChildERBContentItems().size() > 0) {
				for(ERBContentItem child: item.getChildERBContentItems()) {
					if(child.getId().contentEquals(reportId)) {
						return child;
					}
				}
			}
		}
		return null;
	}
}
