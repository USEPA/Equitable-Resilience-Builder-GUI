package erb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ContainerController implements Initializable{

	public ContainerController() {
		
	}

	@FXML
	VBox containerVBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void cleanVBox(VBox vBox) {
		vBox.getChildren().clear();
	}
	
	public void loadContent(Node node) {
		cleanVBox(containerVBox);
		containerVBox.getChildren().add(node);
	}

}
