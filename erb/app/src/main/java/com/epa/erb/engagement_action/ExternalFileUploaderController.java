package com.epa.erb.engagement_action;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import java.io.PrintWriter;
import java.util.*;

public class ExternalFileUploaderController implements Initializable{

	private Logger logger;
	private FileHandler fileHandler;
	
	private App app;
	private EngagementActionController engagementActionController;
	public ExternalFileUploaderController(App app, EngagementActionController engagementActionController) {
		this.app = app;
		this.engagementActionController = engagementActionController;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}
	
	@FXML
	TableView<MyUploadedItem> tableView;
	@FXML
	TableColumn<MyUploadedItem, Text> nameColumn;
	@FXML
	TableColumn<MyUploadedItem, Integer> numberColumn;
	@FXML
	TableColumn<MyUploadedItem, String> modifiedColumn;
	@FXML
	TableColumn<MyUploadedItem, String> uploadSourceColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void launch() {
		initTableView();
		createMyUploadsDirectory();
		loadMyUploadedDocumentsToTableView();
	}
	
	private void loadMyUploadedDocumentsToTableView() {
		tableView.getItems().clear();
		if (engagementActionController != null) {
			File uploadsDirectory = fileHandler.getMyUploadsDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
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
								logger.log(Level.SEVERE, "Failed to read upload info: " + e.getMessage());
							}
							
						} else {
							long modifiedLong = uploadedFile.lastModified();
							lastModified = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss").format(new Date(modifiedLong));
							fileName = new Text(uploadedFile.getName());
							fileName.setUnderline(true);
							fileName.setFill(Color.BLUE);
						}
					}
					MyUploadedItem myPortfolioItem = new MyUploadedItem(fileNumber, fileName, lastModified, uploadedFrom, app);
					tableView.getItems().add(myPortfolioItem);
					fileNumber = fileNumber+1;
				}
			}
		}
	}
	
	private void addSingleUploadedDocumentToTableView(File sourceFile, String uploadSource) {
		if (engagementActionController != null) {
			int fileNumber = tableView.getItems().size() + 1;
			long modifiedLong = sourceFile.lastModified();
			String lastModified = new SimpleDateFormat("MM-ddS-yyyy HH-mm-ss").format(new Date(modifiedLong));
			Text text = new Text(sourceFile.getName());
			text.setUnderline(true);
			text.setFill(Color.BLUE);
			MyUploadedItem myUploadedItem = new MyUploadedItem(fileNumber, text, lastModified, uploadSource, app);
			tableView.getItems().add(myUploadedItem);
			fileNumber = fileNumber + 1;
		}
	}
	
	public void pushToUploaded(File sourceFile, String uploadSource) {
		if (engagementActionController != null) {
			File uploadDir = fileHandler.getMyUploadsDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
			if(!uploadDir.exists()) uploadDir.mkdir();
			int fileNumber = uploadDir.listFiles().length + 1;			
			
			File destDir = new File(uploadDir + File.separator + fileNumber);
			if(!destDir.exists()) destDir.mkdir();
			//Copy file
			File destFile = new File(destDir + File.separator + sourceFile.getName());
			fileHandler.copyFile(sourceFile, destFile);
			//Create about file
			File aboutFile = new File(destDir + File.separator + "about.txt");
			try {
				PrintWriter printWriter = new PrintWriter(aboutFile);
				printWriter.println("uploadSource : " + uploadSource);
				printWriter.close();
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "Failed to push upload: " + e.getMessage());
			}	
		}
	}
	
	private void handleNewFile(File sourceFile, String uploadSource) {
		//Create dir
		int fileNumber = tableView.getItems().size() + 1;
		File uploadDir = fileHandler.getMyUploadsDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
		File destDir = new File(uploadDir + File.separator + fileNumber);
		if(!destDir.exists()) destDir.mkdir();
		//Copy file
		File destFile = new File(destDir + File.separator + sourceFile.getName());
		fileHandler.copyFile(sourceFile, destFile);
		//Create about file
		File aboutFile = new File(destDir + File.separator + "about.txt");
		try {
			PrintWriter printWriter = new PrintWriter(aboutFile);
			printWriter.println("uploadSource : " + uploadSource);
			printWriter.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Failed to handle new file.: " + e.getMessage());
		}	
	}
	
	@FXML
	public void uploadFileButtonAction() {
		FileChooser choose = new FileChooser();
		File sourceFile = choose.showOpenDialog(null);
		if (sourceFile != null && sourceFile.exists()) {
			String uploadedFrom = engagementActionController.getCurrentSelectedERBContentItem().getShortName();
			handleNewFile(sourceFile, uploadedFrom);
			addSingleUploadedDocumentToTableView(sourceFile, uploadedFrom);
		}
	}
	
	public void createMyUploadsDirectory() {
		if(engagementActionController != null) {
			File portfolioDirectory = fileHandler.getMyUploadsDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
			if(portfolioDirectory != null) {
				portfolioDirectory.mkdir();
			}
		}
	}
	
	private void fileNameClicked(MouseEvent e, Text fileName) {
		File uploadsDirectory = fileHandler.getMyUploadsDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
		int fileNumber = tableView.getSelectionModel().getSelectedItem().getFileNumber();
		File fileToOpen = new File(uploadsDirectory.getPath() + File.separator + fileNumber + File.separator + fileName.getText());
		if (e.getButton() == MouseButton.PRIMARY) {
			if (fileToOpen.exists()) {
				fileHandler.openFileOnDesktop(fileToOpen);
			}
		}
	}
	
	@FXML
	public void removeFileButtonAction() {
		MyUploadedItem uploadItem = tableView.getSelectionModel().getSelectedItem();
		if(uploadItem!=null) {
			File uploadsDirectory = fileHandler.getMyUploadsDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal());
			int fileNumber = tableView.getSelectionModel().getSelectedItem().getFileNumber();
			File dirToDelete = new File(uploadsDirectory.getPath() + File.separator + fileNumber);
			fileHandler.deleteDirectory(dirToDelete);
			tableView.getItems().remove(uploadItem);
			tableView.refresh();
			
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please select a file from the table to remove.");
			alert.setTitle("Remove File");
			alert.showAndWait();
		}
	}
	
	public void initTableView() {		
		numberColumn.setCellValueFactory(
		        new PropertyValueFactory<MyUploadedItem,Integer>("fileNumber")
		    );
		nameColumn.setCellValueFactory(
		        new PropertyValueFactory<MyUploadedItem,Text>("fileName")
		    );
		nameColumn.setCellFactory(new Callback<TableColumn<MyUploadedItem, Text>, TableCell<MyUploadedItem, Text>>() {
		        public TableCell<MyUploadedItem, Text> call(TableColumn<MyUploadedItem, Text> param) {
		            return new TableCell<MyUploadedItem, Text>() {
		                @Override
		                public void updateItem(Text item, boolean empty) {
		                    super.updateItem(item, empty);
		                    if (!isEmpty()) {		                        
		                        this.setOnMouseClicked(e-> fileNameClicked(e,item));
		                        setGraphic(item);
		                    }
		                }
		            };
		        }
		    });
		
		
		modifiedColumn.setCellValueFactory(
		        new PropertyValueFactory<MyUploadedItem,String>("modifiedDate")
		    );
		
		uploadSourceColumn.setCellValueFactory(
		        new PropertyValueFactory<MyUploadedItem,String>("uploadedFrom")
		    );
	}

}
