package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.noteboard.NoteBoardContentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SampleContentController implements Initializable{

	@FXML
	ImageView exampleContentImageView;
	@FXML
	HBox activityContentHBox;
	@FXML
	TextFlow objectiveTextFlow;
	@FXML
	TextFlow instructionsTextFlow;
	@FXML
	Label activityLabel;
		
	public SampleContentController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadNoteBoard();
		activityLabel.setText("Social Vulnerability Activity Template");
		Text objectiveText = new Text("Identify who is most vulnerable to hazards and disasters, how they are impacted, and what are the root causes of these impacts");
		objectiveTextFlow.getChildren().add(objectiveText);
		Text instructionsText = new Text("1. On a white board, list the hazards of concern to your community in a column down the left side, and draw horizontal lines between them. (see example)" + "\n" + 
		"2. Reflect on the stories and data that were shared at the beginning of the workshop. Who were the groups of people that were mentioned who experienced impacts from hazards and disasters? What were the impacts they experienced?" + "\n" + 
		"3. As you go, add additional who's and what's for current or potential future impacts" + "\n" +
		"4. After about 30 minutes or when the discussion is at a lull, begin discussing why these impacts happen. Give everyone a few minutes to think and write some \"why's\" on pink post-it notes. Then have people place the pink notes near the blue and yellow notes, and share their thoughts with the group" + "\n" +
		"5. After about 15 minutes, introduce the phases of disaster mitigation-response-recovery. Use colored dots to label each of the \"why's\" with one or more phases");
		instructionsTextFlow.getChildren().add(instructionsText);
	}
	
	public void loadNoteBoard() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController();
			fxmlLoader.setController(noteBoardContentController);
			Parent root = fxmlLoader.load();
			activityContentHBox.getChildren().add(1, root);
			HBox.setHgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
