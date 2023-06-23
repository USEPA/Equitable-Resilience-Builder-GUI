package com.epa.erb.indicators;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class IndicatorRanking_Virtual_PreviousDataSelectionController implements Initializable{

	@FXML
	ListView<Text> dataListView;
	
//	private FileHandler fileHandler = new FileHandler();
	
	private App app;
	private IndicatorRanking_VirtualController iRVC;
	public IndicatorRanking_Virtual_PreviousDataSelectionController(App app, IndicatorRanking_VirtualController iRVC) {
		this.app = app;
		this.iRVC = iRVC;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		loadPreviouslySavedData();
//		setListViewFactory();
	}
	
	private void loadPreviouslySavedData() {
//		File indicatorsDir = fileHandler.getIndicatorsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
//		File rankingVirtualDir = new File(indicatorsDir + "\\Ranking_Virtual");
//		for(File guidDir : rankingVirtualDir.listFiles()) {
//			System.out.println("Found = " + guidDir + " " + guidDir.listFiles().length);
//			if(guidDir.listFiles().length ==2) {
//				File file1= guidDir.listFiles()[0];
//				long d1 = file1.lastModified();
//				File file2= guidDir.listFiles()[1];
//				long d2 = file2.lastModified();
//				System.out.println(d1 + " ---- " + d2);
//				if((d1 > d2) || (d1 == d2)) {
//					String formattedTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date(d1));
//					Text text = new Text(formattedTime);
//					text.setId(guidDir.getName());
//					dataListView.getItems().add(text);
//					System.out.println("GUID Dir for list: " + guidDir.getName() + " " + text.getText());
//				} else if (d2 > d1) {
//					String formattedTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date(d2));
//					Text text = new Text(formattedTime);
//					text.setId(guidDir.getName());
//					dataListView.getItems().add(text);
//					System.out.println("GUID Dir for list: " + guidDir.getName() + " " + text.getText());
//				}
//			}
//		}
	}
	
	private void setListViewFactory() {
//		Callback<ListView<Text>, ListCell<Text>> cellFactory = new Callback<ListView<Text>, ListCell<Text>>() {
//		    @Override
//		    public ListCell<Text> call(ListView<Text> l) {
//		        return new ListCell<Text>() {
//
//		            @Override
//		            protected void updateItem(Text item, boolean empty) {
//		                super.updateItem(item, empty);
//		                if (item == null || empty) {
//		                    setGraphic(null);
//		                } else {
//		                    setText(item.getText());
//		                }
//		            }
//		        } ;
//		    }
//		};
//		dataListView.setCellFactory(cellFactory);
	}
	
	@FXML
	public void loadButtonAction() {
//		Text text = dataListView.getSelectionModel().getSelectedItem();
//		iRVC.loadDataFromPreviousSession(text.getId());
	}

}
