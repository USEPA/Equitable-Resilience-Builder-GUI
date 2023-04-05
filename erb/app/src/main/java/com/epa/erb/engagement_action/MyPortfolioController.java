package com.epa.erb.engagement_action;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import java.io.PrintWriter;
import java.util.*;

public class MyPortfolioController implements Initializable{

	@FXML
	TableColumn<MyPortfolioItem, Integer> numberColumn;
	@FXML
	TableColumn<MyPortfolioItem, Text> nameColumn;
	@FXML
	TableColumn<MyPortfolioItem, String> modifiedColumn;
	@FXML
	TableColumn<MyPortfolioItem, String> uploadSourceColumn;
	@FXML
	TableView<MyPortfolioItem> tableView;
	
	private EngagementActionController engagementActionController;
	public MyPortfolioController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}
	
	private FileHandler fileHandler = new FileHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void launch() {
		initTableView();
		createMyPortfolioDirectory();
		loadMyPortfolioDocumentsToTableView();
	}
	
	private void loadMyPortfolioDocumentsToTableView() {
		tableView.getItems().clear();
		if (engagementActionController != null) {
			File portfolioDirectory = fileHandler.getMyPortfolioDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
			if (portfolioDirectory != null && portfolioDirectory.exists()) {
				for(File uploadedDir: portfolioDirectory.listFiles()) {
					
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
							fileName.setUnderline(true);
							fileName.setFill(Color.BLUE);
						}
					}
					MyPortfolioItem myPortfolioItem = new MyPortfolioItem(fileNumber, fileName, lastModified, uploadedFrom);
					tableView.getItems().add(myPortfolioItem);
					fileNumber = fileNumber+1;
				}
			}
		}
	}
	
	private void addSinglePortfolioDocumentToTableView(File sourceFile, String uploadSource) {
		if (engagementActionController != null) {
			int fileNumber = tableView.getItems().size() + 1;
			long modifiedLong = sourceFile.lastModified();
			String lastModified = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(modifiedLong));
			Text text = new Text(sourceFile.getName());
			text.setUnderline(true);
			text.setFill(Color.BLUE);
			MyPortfolioItem myPortfolioItem = new MyPortfolioItem(fileNumber, text, lastModified, uploadSource);
			tableView.getItems().add(myPortfolioItem);
			fileNumber = fileNumber + 1;
		}
	}
	
	private void handleNewFile(File sourceFile, String uploadSource) {
		//Create dir
		int fileNumber = tableView.getItems().size() + 1;
		File portfolioDir = fileHandler.getMyPortfolioDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
		File destDir = new File(portfolioDir + "\\" + fileNumber);
		if(!destDir.exists()) destDir.mkdir();
		//Copy file
		File destFile = new File(destDir + "\\" + sourceFile.getName());
		fileHandler.copyFile(sourceFile, destFile);
		//Create about file
		File aboutFile = new File(destDir + "\\" + "about.txt");
		try {
			PrintWriter printWriter = new PrintWriter(aboutFile);
			printWriter.println("uploadSource : " + uploadSource);
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML
	public void uploadFileButtonAction() {
		FileChooser choose = new FileChooser();
		File sourceFile = choose.showOpenDialog(null);
		String uploadedFrom = engagementActionController.getCurrentSelectedERBContentItem().getShortName();
		handleNewFile(sourceFile, uploadedFrom);
		addSinglePortfolioDocumentToTableView(sourceFile,uploadedFrom);
	}
	
	public void createMyPortfolioDirectory() {
		if(engagementActionController != null) {
			File portfolioDirectory = fileHandler.getMyPortfolioDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
			if(portfolioDirectory != null) {
				portfolioDirectory.mkdir();
			}
		}
	}
	
	private void fileNameClicked(Text fileName) {
		File portfolioDirectory = fileHandler.getMyPortfolioDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
		int fileNumber = tableView.getSelectionModel().getSelectedItem().getFileNumber();
		File fileToOpen = new File(portfolioDirectory.getPath() + "\\" + fileNumber + "\\" + fileName.getText());
		if(fileToOpen.exists()) {
			fileHandler.openFileOnDesktop(fileToOpen);
		}
	}
	
	public void initTableView() {		
		numberColumn.setCellValueFactory(
		        new PropertyValueFactory<MyPortfolioItem,Integer>("fileNumber")
		    );
		nameColumn.setCellValueFactory(
		        new PropertyValueFactory<MyPortfolioItem,Text>("fileName")
		    );
		nameColumn.setCellFactory(new Callback<TableColumn<MyPortfolioItem, Text>, TableCell<MyPortfolioItem, Text>>() {
		        public TableCell call(TableColumn param) {
		            return new TableCell<MyPortfolioItem, Text>() {
		                @Override
		                public void updateItem(Text item, boolean empty) {
		                    super.updateItem(item, empty);
		                    if (!isEmpty()) {		                        
		                        this.setOnMouseClicked(e-> fileNameClicked(item));
		                        setGraphic(item);
		                    }
		                }
		            };
		        }
		    });
		
		
		modifiedColumn.setCellValueFactory(
		        new PropertyValueFactory<MyPortfolioItem,String>("modifiedDate")
		    );
		
		uploadSourceColumn.setCellValueFactory(
		        new PropertyValueFactory<MyPortfolioItem,String>("uploadedFrom")
		    );
	}

}
