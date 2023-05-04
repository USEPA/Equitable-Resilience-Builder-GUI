package com.epa.erb.project;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.epa.erb.App;
import com.epa.erb.utility.MainPanelHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class ProjectSelectionController implements Initializable{

	@FXML
	VBox vBox;
	@FXML
	Rectangle rectangle1;
	@FXML
	TextField projectNameTextField;
	@FXML
	TextArea descriptionTextArea;
	@FXML
	ListView<Project> projectsListView;
	
	private App app;
	public ProjectSelectionController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setProjectsListViewCellFactory();
		app.updateAvailableProjectsList();
		fillProjectsListView();
        rectangle1.widthProperty().bind(vBox.widthProperty().subtract(5.0));
        descriptionTextArea.getStylesheets().add(getClass().getResource("/textArea.css").toString());		
	}
	
	public void loadEngagementActionToContainer(Project project) {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		Parent engagementActionRoot = mainPanelHandler.loadEngagementActionRoot(app, project);
		app.loadNodeToERBContainer(engagementActionRoot);
	}
	
	private void fillProjectsListView() {
		ArrayList<Project> projects = app.getProjects();
		for (Project project : projects) {
			if(!project.getProjectCleanedName().contentEquals("Explore")) {
			projectsListView.getItems().add(project);
			}
		}
	}
	
	private void setProjectsListViewCellFactory() {
		projectsListView.setCellFactory(new Callback<ListView<Project>, ListCell<Project>>() {
			@Override
			public ListCell<Project> call(ListView<Project> param) {
				ListCell<Project> cell = new ListCell<Project>() {
					@Override
					protected void updateItem(Project item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getProjectName());
							setFont(new Font(14.0));
						}
					}
				};
				cell.setOnMouseClicked(e-> mouseClickedProject(e));
				return cell;
			}
		});
	}
	
	private void mouseClickedProject(MouseEvent mouseEvent) {
		if (mouseEvent == null || mouseEvent.getClickCount() == 2) {
			Project selectedProject = projectsListView.getSelectionModel().getSelectedItem();
			loadProject(selectedProject);
		} else if (mouseEvent.getClickCount() ==1 ) {
			Project selectedProject = projectsListView.getSelectionModel().getSelectedItem();
			descriptionTextArea.setText(selectedProject.getProjectDescription());
		}
	}
	
	public void loadProject(Project project) {
		if (project != null) {
			MainPanelHandler mainPanelHandler = new MainPanelHandler();
			app.setSelectedProject(project);
			app.getErbContainerController().getMyBreadCrumbBar().setProject(project);
			app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(project.getProjectName() + " Dashboard", mainPanelHandler.getMainPanelIdHashMap().get("Engagement"));
			loadEngagementActionToContainer(project);
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public TextField getProjectNameTextField() {
		return projectNameTextField;
	}

	public ListView<Project> getProjectsListView() {
		return projectsListView;
	}
	
}
