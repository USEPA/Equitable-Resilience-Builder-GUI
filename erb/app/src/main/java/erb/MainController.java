package erb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	Stage mainStage;
	
	public MainController(Stage mainStage) {
		this.mainStage = mainStage;
	}

	String pathToERBFileSystem = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\Dev_Docs\\File_System\\erb_supporting_docs";
	
	@FXML
	Button newProjectLaunchButton;
	@FXML
	Button previousProjectLaunchButton;
	@FXML
	ChoiceBox<String> previousProjectChoiceBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillPreviousProjectChoiceBox();
	}
	
	@FXML
	public void newProjectLaunchButtonAction() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NewProject.fxml"));
			NewProjectController newProjectController = new NewProjectController(mainStage);
			fxmlLoader.setController(newProjectController);
			Parent rootParent = fxmlLoader.load();
			Scene scene = new Scene(rootParent);
			mainStage.setScene(scene);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@FXML
	public void previousProjectLaunchButtonAction() {
		if(previousProjectChoiceBox.getSelectionModel().getSelectedIndex() >= 0) {
			try {
				Wizard wizard = new Wizard();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WizardContainer.fxml"));
				WizardContainerController wizardContainerController = new WizardContainerController(wizard);
				fxmlLoader.setController(wizardContainerController);
				wizard.setWizardContainerController(wizardContainerController);
				PreviousProject previousProject = getPreviousProjectSelected();
				wizard.setPanelsFromPreviousProject(wizard, previousProject);
				Parent rootParent = fxmlLoader.load();
				wizardContainerController.loadWizardPanel(previousProject.getCurrentWizardPanelIndex());
				Scene scene = new Scene(rootParent);
				mainStage.setScene(scene);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public PreviousProject getPreviousProjectSelected() {
		PreviousProject previousProject = new PreviousProject(previousProjectChoiceBox.getSelectionModel().getSelectedItem(), pathToERBFileSystem);
		previousProject.parsePreviousProject();
		return previousProject;
	}
		
	public void fillPreviousProjectChoiceBox() {
		ArrayList<String> previousProjectNames = getAllPreviousProjectNames();
		for(String projName: previousProjectNames) {
			previousProjectChoiceBox.getItems().add(projName);
		}
	}
	
	public ArrayList<String> getAllPreviousProjectNames(){
		ArrayList<String> previousProjectNames = new ArrayList<String>();
		File sessionsDirectory = new File(pathToERBFileSystem + "\\User\\Sessions");
		if(sessionsDirectory.isDirectory() && sessionsDirectory.exists()) {
			for (File projectDir: sessionsDirectory.listFiles()) {
				previousProjectNames.add(projectDir.getName().trim());
			}
		}
		return previousProjectNames;
	}

}
