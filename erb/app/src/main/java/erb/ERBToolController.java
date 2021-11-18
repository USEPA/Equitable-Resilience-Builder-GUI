package erb;

import java.net.URL;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ERBToolController implements Initializable {

	@FXML
	VBox erbVBox;
	@FXML
	HBox menuHBox;
	@FXML
	Label panel1Label; // Welcome
	@FXML
	Label panel2Label; // Chapter 1
	@FXML
	Label panel3Label; // Chapter 2
	@FXML
	Label panel4Label; // Chapter 3
	@FXML
	Label panel5Label; // Chapter 4
	@FXML
	Label panel6Label; // Chapter 5

	private Label selectedPanelLabel;
	private ArrayList<Label> listOfAllPanelLabels;

	public ERBToolController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listOfAllPanelLabels = createListOfAllPanelLabels();
		setAllPanelLabelsHoverAction();
		
		panel1LabelAction(); //Loads the first panel as default
	}

	@FXML
	public void panel1LabelAction() {
		selectedPanelLabel = panel1Label;
		unHighlightAllMenuLabels();
		highlightNode(panel1Label);

	}

	@FXML
	public void panel2LabelAction() {
		selectedPanelLabel = panel2Label;
		unHighlightAllMenuLabels();
		highlightNode(panel2Label);

	}

	@FXML
	public void panel3LabelAction() {
		selectedPanelLabel = panel3Label;
		unHighlightAllMenuLabels();
		highlightNode(panel3Label);

	}

	@FXML
	public void panel4LabelAction() {
		selectedPanelLabel = panel4Label;
		unHighlightAllMenuLabels();
		highlightNode(panel4Label);

	}

	@FXML
	public void panel5LabelAction() {
		selectedPanelLabel = panel5Label;
		unHighlightAllMenuLabels();
		highlightNode(panel5Label);

	}

	@FXML
	public void panel6LabelAction() {
		selectedPanelLabel = panel6Label;
		unHighlightAllMenuLabels();
		highlightNode(panel6Label);

	}

	public void setAllPanelLabelsHoverAction() {
		for (Label panelLabel : listOfAllPanelLabels) {
			if (panelLabel != null) {
				panelLabel.setOnMouseEntered(e -> highlightNode(panelLabel));
				panelLabel.setOnMouseExited(e -> unHighlightNode(panelLabel));
			}
		}
	}

	public void unHighlightNode(Node node) {
		if (node != selectedPanelLabel) {
			node.setStyle(null);
		}
	}

	public void highlightNode(Node node) {
		node.setStyle("-fx-background-color: #D8D8D8");
	}

	public void unHighlightAllMenuLabels() {
		for (Label label : listOfAllPanelLabels) {
			unHighlightNode(label);
		}
	}
	
	/**
	 * Remove all children from the erbVBox, besides the menu bar
	 */
	public void removeERBVBoxChildren() {
		int numberOfChildren = erbVBox.getChildren().size();
		for (int i = 0; i < numberOfChildren; i++) {
			Node node = erbVBox.getChildren().get(i);
			if (!node.getId().contentEquals("menuHBox")) {
				erbVBox.getChildren().remove(i);
			}
		}
	}
	
	public void removeERBVBoxChild(Node child) {
		erbVBox.getChildren().remove(child);
	}
	
	public void addERBVBoxChild(Node child, int index) {
		erbVBox.getChildren().add(index, child);
	}

	public ArrayList<Label> createListOfAllPanelLabels() {
		ArrayList<Label> listOfAllPanelLabels = new ArrayList<Label>();
		listOfAllPanelLabels.add(panel1Label);
		listOfAllPanelLabels.add(panel2Label);
		listOfAllPanelLabels.add(panel3Label);
		listOfAllPanelLabels.add(panel4Label);
		listOfAllPanelLabels.add(panel5Label);
		listOfAllPanelLabels.add(panel6Label);
		return listOfAllPanelLabels;
	}

}
