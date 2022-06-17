package com.epa.erb.worksheet;

import java.net.URL;
import java.util.ResourceBundle;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class PrinterSelectionController implements Initializable{

	@FXML
	ListView<String> printerList;
	
	private WorksheetContentController worksheetContentController;
	public PrinterSelectionController(WorksheetContentController worksheetContentController) {
		this.worksheetContentController = worksheetContentController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillPrinterList();
	}
	
	@FXML
	public void okButtonAction() {
		int selectedPrinterIndex = printerList.getSelectionModel().getSelectedIndex();
		if (selectedPrinterIndex >= 0) {
			worksheetContentController.printerSelected(PrintServiceLookup.lookupPrintServices(null, null)[selectedPrinterIndex]);
			worksheetContentController.closePrinterStage();
		}
	}
	
	private void fillPrinterList() {
		printerList.getItems().clear();
		PrintService [] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for(PrintService printService : printServices) {
			printerList.getItems().add(printService.getName());
		}
	}

	public WorksheetContentController getWorksheetContentController() {
		return worksheetContentController;
	}

	public void setWorksheetContentController(WorksheetContentController worksheetContentController) {
		this.worksheetContentController = worksheetContentController;
	}

	public ListView<String> getPrinterList() {
		return printerList;
	}	
	
}
