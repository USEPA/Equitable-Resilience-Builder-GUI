//package old;
//
//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.ResourceBundle;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ChoiceBox;
//import javafx.scene.control.Label;
//import javafx.stage.Stage;
//
//public class ProjectSelectionController implements Initializable{
//	
//	Stage mainStage;
//	
//	public ProjectSelectionController(Stage mainStage) {
//		this.mainStage = mainStage;
//	}
//
//	String pathToERBFileSystem = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\Dev_Docs\\File_System\\erb_supporting_docs";
//	private Logger logger = LogManager.getLogger(ProjectSelectionController.class);
//	
//	@FXML
//	Button newProjectLaunchButton;
//	@FXML
//	Button previousProjectLaunchButton;
//	@FXML
//	ChoiceBox<String> previousProjectChoiceBox;
//	@FXML
//	Label glossaryLabel;
//	@FXML
//	Label resourcesLabel;
//	@FXML
//	Label erbLandingLabel;
//	
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		fillPreviousProjectChoiceBox();
//		
//		glossaryLabel.setOnMouseEntered(e-> handleLabelHighlight(glossaryLabel));
//		glossaryLabel.setOnMouseExited(e-> handleLabelUnHighlight(glossaryLabel));
//		glossaryLabel.setOnMouseClicked(e-> handleLabelClicked(glossaryLabel));
//
//		resourcesLabel.setOnMouseEntered(e-> handleLabelHighlight(resourcesLabel));
//		resourcesLabel.setOnMouseExited(e-> handleLabelUnHighlight(resourcesLabel));
//		resourcesLabel.setOnMouseClicked(e-> handleLabelClicked(resourcesLabel));
//		
//		erbLandingLabel.setOnMouseEntered(e-> handleLabelHighlight(erbLandingLabel));
//		erbLandingLabel.setOnMouseExited(e-> handleLabelUnHighlight(erbLandingLabel));
//		erbLandingLabel.setOnMouseClicked(e-> handleLabelClicked(erbLandingLabel));
//	}
//	
//	public void handleLabelHighlight(Label label) {	
//		//Highlight
//		label.setStyle("-fx-background-color: #A2A2A2");
//	}
//	
//	public void handleLabelUnHighlight(Label label) {	
//		//Un-highlight 
//		label.setStyle("-fx-background-color: transparent");
//	}
//	
//	public void handleLabelClicked(Label label) {
//		if(label == erbLandingLabel) {
//			try {
//				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ERBLanding.fxml"));
//				ERBLandingController erbLandingController = new ERBLandingController(mainStage);
//				fxmlLoader.setController(erbLandingController);
//				Parent rootParent = fxmlLoader.load();
//				Scene mainScene = new Scene(rootParent);
//				mainStage.setScene(mainScene);
//				mainStage.setTitle("ERB");
//				mainStage.show();
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//			}
//		}
//	}
//	
//	@FXML
//	public void newProjectLaunchButtonAction() {
//		try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NewProject.fxml"));
//			NewProjectController newProjectController = new NewProjectController(mainStage);
//			fxmlLoader.setController(newProjectController);
//			Parent rootParent = fxmlLoader.load();
//			Scene scene = new Scene(rootParent);
//			mainStage.setScene(scene);
//		}catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//	}
//	
//	@FXML
//	public void previousProjectLaunchButtonAction() {
//		if(previousProjectChoiceBox.getSelectionModel().getSelectedIndex() >= 0) {
//			try {
//				Wizard wizard = new Wizard(mainStage);
//				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WizardContainer.fxml"));
//				WizardContainerController wizardContainerController = new WizardContainerController(wizard);
//				fxmlLoader.setController(wizardContainerController);
//				wizard.setWizardContainerController(wizardContainerController);
//				PreviousProject previousProject = getPreviousProjectSelected();
//				wizard.setPanelsFromPreviousProject(wizard, previousProject);
//				Parent rootParent = fxmlLoader.load();
//				wizardContainerController.loadWizardPanel(previousProject.getCurrentWizardPanelIndex());
//				Scene scene = new Scene(rootParent);
//				mainStage.setScene(scene);
//			}catch (Exception e) {
//				logger.error(e.getMessage());
//			}
//		}
//	}
//	
//	public PreviousProject getPreviousProjectSelected() {
//		PreviousProject previousProject = new PreviousProject(previousProjectChoiceBox.getSelectionModel().getSelectedItem(), pathToERBFileSystem);
//		previousProject.parsePreviousProject();
//		return previousProject;
//	}
//		
//	public void fillPreviousProjectChoiceBox() {
//		ArrayList<String> previousProjectNames = getAllPreviousProjectNames();
//		for(String projName: previousProjectNames) {
//			previousProjectChoiceBox.getItems().add(projName);
//		}
//	}
//	
//	public ArrayList<String> getAllPreviousProjectNames(){
//		ArrayList<String> previousProjectNames = new ArrayList<String>();
//		File sessionsDirectory = new File(pathToERBFileSystem + "\\User\\Sessions");
//		if(sessionsDirectory.isDirectory() && sessionsDirectory.exists()) {
//			for (File projectDir: sessionsDirectory.listFiles()) {
//				previousProjectNames.add(projectDir.getName().trim());
//			}
//		}
//		return previousProjectNames;
//	}
//
//}
