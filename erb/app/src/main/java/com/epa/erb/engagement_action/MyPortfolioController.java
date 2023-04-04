package com.epa.erb.engagement_action;

import java.io.File;
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

public class MyPortfolioController implements Initializable{

	@FXML
	TableColumn<MyPortfolioItem, Integer> numberColumn;
	@FXML
	TableColumn<MyPortfolioItem, Text> nameColumn;
	@FXML
	TableColumn<MyPortfolioItem, String> modifiedColumn;
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
				int fileNumber = 1;
				for(File uploadedFile: portfolioDirectory.listFiles()) {
					long modifiedLong = uploadedFile.lastModified();
					String lastModified = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(modifiedLong));
					Text text = new Text(uploadedFile.getName());
					text.setUnderline(true);
					text.setFill(Color.BLUE);
					MyPortfolioItem myPortfolioItem = new MyPortfolioItem(fileNumber, text, lastModified);
					tableView.getItems().add(myPortfolioItem);
					fileNumber = fileNumber+1;
				}
			}
		}
	}
	
	@FXML
	public void uploadFileButtonAction() {
		FileChooser choose = new FileChooser();
		File sourceFile = choose.showOpenDialog(null);
		File destFile = new File(fileHandler.getMyPortfolioDirectory(engagementActionController.getProject(), engagementActionController.getCurrentGoal()) + "\\" + sourceFile.getName());
		fileHandler.copyFile(sourceFile, destFile);
		loadMyPortfolioDocumentsToTableView();
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
		File fileToOpen = new File(portfolioDirectory.getPath() + "\\" + fileName.getText());
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
	}

}