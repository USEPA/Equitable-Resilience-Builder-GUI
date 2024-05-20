package com.epa.erb.finalReport;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;
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
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class FinalReportSelectionController implements Initializable {

	private App app;

	public FinalReportSelectionController(App app) {
		this.app = app;
	}

	XMLManager xmlManager;
	FileHandler fileHandler;
	ArrayList<ERBContentItem> erbUniqueContentItems;
	ArrayList<ERBContentItem> erbWorksheetContentItems;
	ArrayList<FinalReportItem> treeItems = new ArrayList<FinalReportItem>();
	ArrayList<FinalReportItem> treeViewSelectedItems = new ArrayList<FinalReportItem>();
	ArrayList<FinalReportItem> listViewSelectedItems = new ArrayList<FinalReportItem>();
	HashMap<FinalReportItem, CheckBox> treeViewCheckBoxMap = new HashMap<FinalReportItem, CheckBox>();
	HashMap<FinalReportItem, CheckBox> listViewCheckBoxMap = new HashMap<FinalReportItem, CheckBox>();
	HashMap<FinalReportItem,ERBContentItem > finalReportItemMap = new HashMap<FinalReportItem, ERBContentItem>();


	HashMap<ERBContentItem, Integer> pageNumMap = new HashMap<ERBContentItem, Integer>();

	@FXML
	Button infoButton;
	@FXML
	ListView<FinalReportItem> listView;
	@FXML
	TreeView<FinalReportItem> availableDataTreeView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fileHandler = new FileHandler(app);
		xmlManager = new XMLManager(app);

		listView.setCellFactory(lv -> createListCell());
		listView.getStylesheets().add(getClass().getResource("/listViewReport.css").toString());

		availableDataTreeView.setCellFactory(tv -> createTreeCell());
		availableDataTreeView.getStylesheets().add(getClass().getResource("/reportTreeView.css").toString());

		erbUniqueContentItems = app.getEngagementActionController().getListOfUniqueERBContentItems();
		erbWorksheetContentItems = getWorksheetsERBItems();
		
		File reportDataXML = fileHandler.getStaticReportDataXMLFile();
		ArrayList<FinalReportItem> finalReportItems = xmlManager.parseReportDataXML(reportDataXML);
		fillTreeViewData(finalReportItems);
		availableDataTreeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> treeViewClicked(oldValue, newValue));
	}

	private void fillTreeViewData(ArrayList<FinalReportItem> finalReportItems) {
		ERBContentItem erbSectionsERBContentItem = new ERBContentItem("91", null, "mainForm", null, "ERB Sections","ERB Sections");
		FinalReportItem erbSectionsFinalReportItem = new FinalReportItem("91", "ERB Sections", null, null, null);
		finalReportItemMap.put(erbSectionsFinalReportItem, erbSectionsERBContentItem);
		
		TreeItem<FinalReportItem> erbSectionsTreeItem = new TreeItem<FinalReportItem>(erbSectionsFinalReportItem);
		erbSectionsTreeItem.setExpanded(true);
		availableDataTreeView.setRoot(erbSectionsTreeItem);

		TreeItem<FinalReportItem> parentTreeItem = null;
		for (FinalReportItem finalReportItem: finalReportItems) {			
			ERBContentItem parentERBContentItem = findERBSectionItem(finalReportItem.getId());
			if (parentERBContentItem != null) {
				finalReportItemMap.put(finalReportItem, parentERBContentItem);
				parentTreeItem = addChildrenToTreeView(finalReportItem, erbSectionsTreeItem);
			} else {
				ERBContentItem childERBContentItem = findContentItem(finalReportItem.getId());
				finalReportItemMap.put(finalReportItem, childERBContentItem);
				addChildrenToTreeView(finalReportItem, parentTreeItem);
				treeItems.add(finalReportItem);
			}
		}
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
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("There is no saved data for this item yet.");
				alert.setTitle("Summary Report Item");
				alert.showAndWait();
			}
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

	@FXML
	public void createReportButtonAction() {
		try {
			File goalDir = fileHandler.getGoalDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());

			String reportDocPath = goalDir + File.separator + app.getEngagementActionController().getProject().getProjectCleanedName() + "_SummaryReport.docx";
			
		    XWPFDocument document = new XWPFDocument();
		    FileOutputStream out = new FileOutputStream(new File(reportDocPath));
						
			//Cover Page
		    writeReportDocxCoverPage(document);
		    
		    //New Page
		    XWPFParagraph paragraph = document.createParagraph();
		    paragraph.setPageBreak(true);
		    
			//Content Pages
			for (FinalReportItem finalReportItem : listView.getItems()) {
				
				ERBContentItem erbContentItem = finalReportItemMap.get(finalReportItem);
				
				String paragraphHeaderString = finalReportItem.getDisplayName();
				
				XWPFParagraph paragraphHeader = document.createParagraph();
				XWPFRun paragraphHeaderRun = paragraphHeader.createRun();
				paragraphHeaderRun.setFontSize(16);
				paragraphHeaderRun.setBold(true);
				paragraphHeaderRun.setText(paragraphHeaderString);
												
				if (erbContentItem.getType().contentEquals("outputForm")) {
					File guidDataDir = fileHandler.getGUIDDataDirectory(app.getEngagementActionController().getProject(), app.getEngagementActionController().getCurrentGoal());
					File guidDir = new File(guidDataDir + File.separator + erbContentItem.getGuid());
					File outputFormFile = findOutputFormToOpen(guidDir);
					if (outputFormFile != null) {
						FinalReportKeyTakeaways fRKT = new FinalReportKeyTakeaways(outputFormFile);
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
							FinalReportTable fRT = new FinalReportTable(docFile, finalReportItem.getTableName(), finalReportItem.getStart(), finalReportItem.getStop(), document);
							fRT.writeTableToReportDoc();
						} else {
							FinalReportParagraph fRP = new FinalReportParagraph(docFile, start, stop);
							XWPFParagraph contentParagraph = document.createParagraph();
							XWPFRun contentRun = contentParagraph.createRun();
							contentRun.setFontSize(14);
							formatTextForDocx(fRP.readWordParagraph(), contentRun);
						}
					}
				}
			}
		    
			//Footer
		    writeReportDocxFooter(document);
		    
		    document.write(out);
		    out.close();
		    document.close();

		} catch (Exception e) {
			e.printStackTrace();
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
		
		for (FinalReportItem finalReportItem : treeViewSelectedItems) {
			listViewItems.add(finalReportItem);
		}
		
		//listViewItems = items in list view after adding new
		
		ArrayList<FinalReportItem> tempList = new ArrayList<FinalReportItem>();
		ArrayList<FinalReportItem> toRemove = new ArrayList<FinalReportItem>();
		tempList.addAll(treeItems);
		
		//Remove all from temp list that arent in list view
		for(FinalReportItem finalReportItem: tempList) {
			if(!listViewItems.contains(finalReportItem)) {
				toRemove.add(finalReportItem);
			}
		}
		for(FinalReportItem finalReportItem: toRemove) {
			tempList.remove(finalReportItem);
		}
		
		//Order list view items
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

					if (this.getTreeItem().isLeaf() && !erbUniqueContentItems.contains(erbContentItem)) {
						CheckBox checkBox = new CheckBox();
						checkBox.setOnAction(e -> treeViewCheckBoxClicked(checkBox, this.getTreeItem()));
						treeViewCheckBoxMap.put(item, checkBox);
						setId("custom-tree-cell");
						setGraphic(checkBox);
						setText(item.getDisplayName());
					} else {
						setText(item.getDisplayName());
						setGraphic(null);
					}
				}
			}
		};
	}

	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

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

				setOnDragDetected(event -> {
					if (!this.isEmpty()) {
						Integer index = this.getIndex();
						Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
						db.setDragView(this.snapshot(null, null));
						ClipboardContent cc = new ClipboardContent();
						cc.put(SERIALIZED_MIME_TYPE, index);
						db.setContent(cc);
						event.consume();
					}
				});

				setOnDragOver(event -> {
					Dragboard db = event.getDragboard();
					if (db.hasContent(SERIALIZED_MIME_TYPE)) {
						if (this.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
							event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
							event.consume();
						}
					}
				});

				setOnDragDropped(event -> {
					Dragboard db = event.getDragboard();
					if (db.hasContent(SERIALIZED_MIME_TYPE)) {
						int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
						FinalReportItem draggedHeatMapController = listView.getItems().remove(draggedIndex);
						int dropIndex;
						if (this.isEmpty()) {
							dropIndex = listView.getItems().size();
						} else {
							dropIndex = this.getIndex();
						}
						listView.getItems().add(dropIndex, draggedHeatMapController);
						event.setDropCompleted(true);
						listView.getSelectionModel().select(dropIndex);
						event.consume();
					}
				});
			}
		};
	}

	private void treeViewCheckBoxClicked(CheckBox checkBox, TreeItem<FinalReportItem> treeItem) {
		if (checkBox.isSelected()) {
			treeViewSelectedItems.add(treeItem.getValue());
		} else {
			treeViewSelectedItems.remove(treeItem.getValue());
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


//	private void createFinalReport() {
//		File goalDir = fileHandler.getGoalDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
//		try {
//			XWPFDocument reportDoc = new XWPFDocument();
//			
//			//1
//			XWPFParagraph paragraph1 = reportDoc.createParagraph();
//			String titleText1 = "Key Takeaways: Assess";
//			createNewTitleRun(paragraph1,titleText1);
//			XWPFRun contentRun1 = createNewContentRun(paragraph1, null);
//			addNewLines(getAssessKeyTakeawaysText(), contentRun1);
//			//2
//			XWPFParagraph paragraph2 = reportDoc.createParagraph();
//			String titleText2 = "Strategy Chart";
//			createNewTitleRun(paragraph2, titleText2);
//			createStrategyChart(reportDoc, goalDir);
//
//			//3
//			XWPFParagraph paragraph3 = reportDoc.createParagraph();
//			String titleText3 = "Reflection Diary";
//			createNewTitleRun(paragraph3, titleText3);
//			XWPFRun contentRun3 = createNewContentRun(paragraph3, null);
//			addNewLines(getReflectionDiaryText(goalDir), contentRun3);
//			
//			//4
//			XWPFParagraph paragraph4 = reportDoc.createParagraph();
//			String titleText4 = "Quadrant Sorting";
//			createNewTitleRun(paragraph4, titleText4);
//			XWPFRun contentRun4 = createNewContentRun(paragraph4, null);
//			addQuadrantDiagram(contentRun4);
//			
//			
//			FileOutputStream out = new FileOutputStream(new File(goalDir + File.separator + "finalReport.docx"));
//			reportDoc.write(out);
//			reportDoc.close();
//			out.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	

//	private String getReflectionDiaryText(File goalDir) {
//		File fileToSearch = new File(goalDir.getPath() +  File.separator + "Supporting_DOC" + File.separator + "Reflection_Diary.docx");
//		FinalReportParagraph fRP = new FinalReportParagraph(fileToSearch, "Plan Your Project Questions", "What to Do Next");
//		String text = fRP.readWordParagraph();
//		return text;
//	}
//	
//	
//	private void createStrategyChart(XWPFDocument reportDoc, File goalDir) {
//		File fileToSearch = new File(goalDir.getPath() + File.separator + "Supporting_DOC" + File.separator +"Strategy_Chart.docx");
//		FinalReportTable ft = new FinalReportTable(fileToSearch, "TableToRead", reportDoc);
//		ft.createTableCopy();
//	}
//	
//	
//	private String getAssessKeyTakeawaysText() {
//		File uploadsDirectory = fileHandler.getMyUploadsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
//		if (uploadsDirectory != null && uploadsDirectory.exists()) {
//			for (File uploadedDir : uploadsDirectory.listFiles()) {
//				for (File uploadFile : uploadedDir.listFiles()) {
//					if (uploadFile.getName().contentEquals("Key_Takeaways__Assess.txt")) {
//						FinalReportKeyTakeaways fRKT = new FinalReportKeyTakeaways(uploadFile);
//						return fRKT.getKeyTakeawaysFormattedText();
//					}
//				}
//			}
//		}
//		return null;
//	}
//	
//	private void addQuadrantDiagram(XWPFRun contentRun) {
//		FinalReportImage fRI = new FinalReportImage("SortingSnapshot", app);
//		fRI.addImageToRun(contentRun);
//	}
	
	
	//////////PDF///////////////////////
	
//	@FXML
//	public void createReportButtonAction() {
//		try {
//			File goalDir = fileHandler.getGoalDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
//
//			String reportDocPath = goalDir + File.separator + app.getEngagementActionController().getProject().getProjectCleanedName() + "_SummaryReport.pdf";
//			PdfWriter writer = new PdfWriter(reportDocPath);
//			PdfDocument pdfDoc = new PdfDocument(writer);
//			pdfDoc.addNewPage();
//			Document document = new Document(pdfDoc, PageSize.A4, false);
//			AreaBreak pageAreaBreak = new AreaBreak();
//
//			
//			//Cover Page
//			writeReportPDFCoverPage(document);
//			document.add(pageAreaBreak);
//
//			//Content Pages
//			for (ERBContentItem erbContentItem : listView.getItems()) {
//				String paragraphHeaderString = erbContentItem.getLongName();
//				Paragraph paragraphHeader = new Paragraph(paragraphHeaderString);
//				paragraphHeader.setFontSize(16);
//				paragraphHeader.setBold();
//				document.add(paragraphHeader);
//				
//				pageNumMap.put(erbContentItem, document.getRenderer().getCurrentArea().getPageNumber());
//				
//				if (erbContentItem.getType().contentEquals("outputForm")) {
//					File guidDataDir = fileHandler.getGUIDDataDirectory(app.getEngagementActionController().getProject(), app.getEngagementActionController().getCurrentGoal());
//					File guidDir = new File(guidDataDir + File.separator + erbContentItem.getGuid());
//					File outputFormFile = findOutputFormToOpen(guidDir);
//					if (outputFormFile != null) {
//						FinalReportKeyTakeaways fRKT = new FinalReportKeyTakeaways(outputFormFile);
//						Paragraph contentParagraph = new Paragraph(fRKT.getKeyTakeawaysFormattedText());
//						contentParagraph.setFontSize(14);
//						document.add(contentParagraph);
//					}
//				}
//			}
//			document.add(pageAreaBreak);
//			
//			//TOC
//			writeReportPDFTableOfContents(pdfDoc, document);
//			document.flush();
//
//			PdfPage page = pdfDoc.getPage(pdfDoc.getNumberOfPages());
//			pdfDoc.movePage(page, 2);
//		
//			//Page numbers
//			writeReportPDFFooter(pdfDoc, document);
//						
//			document.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void writeReportPDFTableOfContents(PdfDocument pdfDoc, Document reportDoc) {
//		
//		Paragraph tocParagraph = new Paragraph("Table of Contents");
//		tocParagraph.setFontSize(16);
//		tocParagraph.setUnderline();
//		tocParagraph.setTextAlignment(TextAlignment.CENTER);
//		reportDoc.add(tocParagraph);
//		
//		Table table = new Table(2);
//		table.useAllAvailableWidth();
//
//		for (ERBContentItem erbContentItem : listView.getItems()) {
//			Cell headerCell = new Cell();
//			headerCell.setBorder(null);
//			Paragraph headerParagraph = new Paragraph(erbContentItem.getLongName());
//			headerParagraph.setFontSize(14);
//			headerParagraph.setBold();
//			headerCell.add(headerParagraph);
//			table.addCell(headerCell);			
//
//			Cell pageCell = new Cell();
//			pageCell.setBorder(null);
//			Paragraph pageParagraph = new Paragraph(String.valueOf(pageNumMap.get(erbContentItem) + 1));
//			pageParagraph.setFontSize(14);
//			pageParagraph.setBold();
//			pageCell.add(pageParagraph);
//			table.addCell(pageCell);
//			
//		}
//		reportDoc.add(table);		
//		
//	}
//
//	private void writeReportPDFCoverPage(Document reportDoc) {
//
//		// Report Name
//		String projectName = app.getEngagementActionController().getProject().getProjectName();
//		String reportName = projectName + " Summary Report";
//
//		Paragraph reportNameParagraph = new Paragraph(reportName);
//		reportNameParagraph.setTextAlignment(TextAlignment.CENTER);
//		reportNameParagraph.setFontSize(22);
//		reportDoc.add(reportNameParagraph);
//
//		Paragraph bP = new Paragraph("\n");
//		reportDoc.add(bP);
//
//		// Date
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//		LocalDate localDate = LocalDate.now();
//		String date = dtf.format(localDate);
//		String dateLabel = "Date:";
//
//		Paragraph dateLabelParagraph = new Paragraph(dateLabel);
//		dateLabelParagraph.setTextAlignment(TextAlignment.CENTER);
//		dateLabelParagraph.setFontSize(18);
//		reportDoc.add(dateLabelParagraph);
//
//		Paragraph dateParagraph = new Paragraph(date);
//		dateParagraph.setTextAlignment(TextAlignment.CENTER);
//		dateParagraph.setFontSize(16);
//		reportDoc.add(dateParagraph);
//
//		reportDoc.add(bP);
//
//		// Author
//		String authorLabel = "Authors:";
//
//		Paragraph authorLabelParagraph = new Paragraph(authorLabel);
//		authorLabelParagraph.setTextAlignment(TextAlignment.CENTER);
//		authorLabelParagraph.setFontSize(18);
//		reportDoc.add(authorLabelParagraph);
//
//	}
//
//	private void writeReportPDFFooter(PdfDocument pdfDoc, Document reportDoc) {
//		int totalPages = reportDoc.getRenderer().getCurrentArea().getPageNumber();
//		for (int i = 1; i <= totalPages; i++) {
//			Paragraph footer = createFooter(totalPages);
//			footer.setFixedPosition(i, 40, 40, 300);
//			reportDoc.add(footer);
//		}
//	}
//
//	private Paragraph createFooter(int totalPages) {
//		Paragraph p = new Paragraph();
//		Text currentPage = new Text("");
//		currentPage.setNextRenderer(new CurrentPageNumberRenderer(currentPage));
//		p.add("Page ").add(currentPage).add(" of ").add(new Text(String.valueOf(totalPages)));
//		return p;
//	}
//
//	private static class CurrentPageNumberRenderer extends TextRenderer {
//		public CurrentPageNumberRenderer(Text textElement) {
//			super(textElement);
//		}
//
//		@Override
//		public LayoutResult layout(LayoutContext layoutContext) {
//			int currentPageNumber = layoutContext.getArea().getPageNumber();
//			setText(String.valueOf(currentPageNumber));
//			return super.layout(layoutContext);
//		}
//
//		@Override
//		public IRenderer getNextRenderer() {
//			return new CurrentPageNumberRenderer((Text) modelElement);
//		}
//	}

}
