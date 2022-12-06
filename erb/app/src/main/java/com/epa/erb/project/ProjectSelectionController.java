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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class ProjectSelectionController implements Initializable{

	@FXML
	TextField projectNameTextField;
	@FXML
	ListView<Project> projectsListView;
	
	private App app;
	public ProjectSelectionController(App app) {
		this.app = app;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		app.updateAvailableProjectsList();
		setProjectsListViewCellFactory();
		fillProjectsListView();
	}
	
	public void loadEngagementActionToContainer(Project project) {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		Parent engagementActionRoot = mainPanelHandler.loadEngagementActionRoot(app, project);
		app.loadNodeToERBContainer(engagementActionRoot);
	}
	
	private void fillProjectsListView() {
		ArrayList<Project> projects = app.getProjects();
		for (Project project : projects) {
			projectsListView.getItems().add(project);
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
			if (selectedProject != null) {
				MainPanelHandler mainPanelHandler = new MainPanelHandler();
				app.setSelectedProject(selectedProject);
				app.getErbContainerController().getMyBreadCrumbBar().setProject(selectedProject);
				app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(selectedProject.getProjectName() + " Landing", mainPanelHandler.getMainPanelIdHashMap().get("Engagement"));
				loadEngagementActionToContainer(selectedProject);
			}
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
