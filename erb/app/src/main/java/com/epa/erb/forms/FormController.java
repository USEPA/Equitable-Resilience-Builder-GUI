package com.epa.erb.forms;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.ERBItemFinder;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import com.epa.erb.indicators.IndicatorCenterController;
import com.epa.erb.project.Project;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.IdAssignments;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class FormController {
	
	private Logger logger;
	private FileHandler fileHandler;
	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	private IdAssignments idAssignments = new IdAssignments();

	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;
	public FormController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}

	public FormController(App app, EngagementActionController engagementActionController) {
		this.app = app;
		this.engagementActionController = engagementActionController;
	}

	public void handleImageClicked(MouseEvent event, File imageFile, ImageView image, String id) {
		if (event.getButton() == MouseButton.SECONDARY) {
			ContextMenu contextMenu = new ContextMenu();
			MenuItem menuItem = new MenuItem("Save As");
			menuItem.setOnAction(e -> saveImage(imageFile));
			contextMenu.getItems().add(menuItem);
			image.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
				@Override
				public void handle(ContextMenuEvent event) {
					contextMenu.show(image, event.getSceneX(), event.getSceneY());
				}
			});
		} else {
			if (!idAssignments.getIconIdAssignments().contains(id)) {
				loadImagePopUp(id);
			}
		}
	}

	public void loadImagePopUp(String imageId) {
		Stage stage = new Stage();
		stage.getIcons().add(new Image("/bridge_tool_logo.png"));
		stage.setTitle("ERB: Equitable Resilience Builder");
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		ImageView imageView = new ImageView();
		String enlargedId = getEnlargedId(imageId);
		Image imageToLoad = new Image(fileHandler.getStaticIconImageFile(enlargedId).getPath());
		double width = imageToLoad.getWidth();
		double height = imageToLoad.getHeight();
		imageView.setImage(imageToLoad);
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);

		vBox.getChildren().add(imageView);
		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		imageView.fitWidthProperty().bind(vBox.widthProperty());
		imageView.fitHeightProperty().bind(vBox.heightProperty());
		stage.showAndWait();
	}

	private String getEnlargedId(String id) {
		if (id.contentEquals("155")) {
			return "99";
		} else if (id.contentEquals("157")) {
			return "147";
		} else if (id.contentEquals("158")) {
			return "113";
		} else if (id.contentEquals("159")) {
			return "146";
		} else if (id.contentEquals("163")) {
			return "154";
		} else if (id.contentEquals("79")) {
			return "164";
		} else if (id.contentEquals("80")) {
			return "165";
		} else if (id.contentEquals("81")) {
			return "166";
		} else if (id.contentEquals("181")) {
			return "182";
		} else if (id.contentEquals("183")) {
			return "184";
		} else if (id.contentEquals("185")) {
			return "186";
		} else if (id.contentEquals("203")) {
			return "119";
		}else if (id.contentEquals("205")) {
			return "206";
		}
		return id;
	}

	private void saveImage(File origImageFile) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(null);
		fileHandler.copyFile(origImageFile, file);
	}

	public void handleHyperlink(Text text, String linkType, String link, Project project) {
		if (linkType.contentEquals("internal")) {
			if (link.contentEquals("85")) {
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/indicators/IndicatorCenter.fxml"));
					IndicatorCenterController indicatorCenterController = new IndicatorCenterController(app,
							engagementActionController);
					fxmlLoader.setController(indicatorCenterController);
					Parent root = fxmlLoader.load();
					Stage stage = new Stage();
					stage.getIcons().add(new Image("/bridge_tool_logo.png"));
					stage.setWidth(app.getPopUpPrefWidth());
					stage.setHeight(app.getPopUpPrefHeight());
					stage.setTitle("Indicator Center");
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (Exception e) {
					logger.log(Level.FINE, "Failed to handle hyperlink.");
					logger.log(Level.FINER, "Failed to handle hyperlink: " + e.getStackTrace());
				}
			} else {
				internalPanelLinkClicked(link);
			}
		} else if (linkType.contentEquals("email")) {
			handleEmailLink(text, link);
		} else if (linkType.contentEquals("image")) {
			loadImagePopUp(link);
		} else if (linkType.contentEquals("externalDOC")) {
			externalDOCLinkClicked(link, project);
		} else if (linkType.contentEquals("URL")) {
			urlLinkClicked(link);
		} else if (linkType.length() == 0 || link.length() == 0) {
			showLinkNotAvailable();
		}
	}

	private void handleEmailLink(Text text, String link) {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		content.putString(text.getText());
		clipboard.setContent(content);
	}

	public void showLinkNotAvailable() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Link not available");
		alert.setContentText("This link is not available yet.");
		alert.showAndWait();
	}

	public void internalPanelLinkClicked(String link) {
		if (idAssignments.getResourceIdAssignments().contains(link)) {
			internalPopupLinkClicked(link);
		} else if (idAssignments.getFAQIdAssignments().contains(link)) {
			internalPopupLinkClicked(link);
		} else if (idAssignments.getAboutIdAssignments().contains(link)) {
			internalPopupLinkClicked(link);
		} else if (idAssignments.getBackgroundIdAssignments().contains(link)) {
			internalPopupLinkClicked(link);
		} else if (link.contentEquals("MyPortfolio")) {
			engagementActionController.myPortfolioButtonAction();
		} else {
			internalContentLinkClicked(link);
		}
	}

	public void internalPopupLinkClicked(String link) {
		ERBContentItem erbContentItem = app.findERBContentItemForId(link);
		File formContentXMLFile = app.getErbContainerController().getFormContentXML(link);
		Pane root = null;
		if (erbContentItem.getType().contentEquals("mainForm")) {
			root = app.getErbContainerController().loadMainFormContentController(formContentXMLFile);
		} else if (erbContentItem.getType().contentEquals("alternativeForm")) {
			root = app.getErbContainerController().loadAlternativeFormContentController(formContentXMLFile);

		}
		if (root != null) {
			Stage stage = new Stage();
			stage.setTitle(erbContentItem.getLongName());
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			Scene scene = new Scene(root);
			stage.setWidth(app.getPrefWidth());
			stage.setHeight(app.getPrefHeight());
			stage.setScene(scene);
			stage.showAndWait();
		}
	}

	protected ERBContentItem parentERBContentItem = null;

	public void internalContentLinkClicked(String link) {
		if (idAssignments.getChapterIdAssignments().contains(link)) {
			for (ERBContentItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
				if (erbItem.getId() != null && erbItem.getId().contentEquals(link)) {
					TreeItem<ERBContentItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap()
							.get(erbItem);
					engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
					engagementActionController.treeViewClicked(erbTreeItem, erbTreeItem);
				}
			}
		} else {
			parentERBContentItem = null;
			TreeItem<ERBContentItem> currentSelectedTreeItem = engagementActionController.getTreeView()
					.getSelectionModel().getSelectedItem();
			if (currentSelectedTreeItem.getValue().getChildERBContentItems().size() > 0) {
				ERBContentItem childItem = erbItemFinder
						.getERBContentItemById(currentSelectedTreeItem.getValue().getChildERBContentItems(), link);
				if (childItem != null) {
					engagementActionController.ERBContentItemSelected(childItem);
					for (ERBContentItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
						if (erbItem.getGuid() != null && erbItem.getGuid().contentEquals(childItem.getGuid())) {
							TreeItem<ERBContentItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap()
									.get(erbItem);
							engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
							engagementActionController.treeViewClicked(currentSelectedTreeItem, erbTreeItem);
						}
					}
					return;
				}
			}
			// Get parent chapter for current selected item
			getSectionParent(currentSelectedTreeItem);
			if (parentERBContentItem != null) {
				ERBContentItem item = erbItemFinder
						.getERBContentItemById(parentERBContentItem.getChildERBContentItems(), link);
				if (item != null) {
					engagementActionController.ERBContentItemSelected(item);
					for (ERBContentItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
						if (erbItem.getGuid() != null && erbItem.getGuid().contentEquals(item.getGuid())) {
							TreeItem<ERBContentItem> erbTreeItem = engagementActionController.getTreeItemIdTreeMap()
									.get(erbItem);
							engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
							engagementActionController.treeViewClicked(currentSelectedTreeItem, erbTreeItem);
						}
					}
				} else {
					for (TreeItem<ERBContentItem> erbContent : engagementActionController.getTreeView().getRoot()
							.getChildren()) {
						ERBContentItem item2 = erbItemFinder
								.getERBContentItemById(erbContent.getValue().getChildERBContentItems(), link);
						if (item2 != null) {
							engagementActionController.ERBContentItemSelected(item2);
							for (ERBContentItem erbItem : engagementActionController.getTreeItemIdTreeMap().keySet()) {
								if (erbItem.getGuid() != null && erbItem.getGuid().contentEquals(item2.getGuid())) {
									TreeItem<ERBContentItem> erbTreeItem = engagementActionController
											.getTreeItemIdTreeMap().get(erbItem);
									engagementActionController.getTreeView().getSelectionModel().select(erbTreeItem);
									engagementActionController.treeViewClicked(currentSelectedTreeItem, erbTreeItem);
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	protected void getSectionParent(TreeItem<ERBContentItem> currentSelectedTreeItem) {
		if (idAssignments.getChapterIdAssignments().contains(currentSelectedTreeItem.getValue().getId())) {
			parentERBContentItem = currentSelectedTreeItem.getValue();
		} else {
			TreeItem<ERBContentItem> parentTreeItem = currentSelectedTreeItem.getParent();
			if (idAssignments.getChapterIdAssignments().contains(parentTreeItem.getValue().getId())) {
				parentERBContentItem = parentTreeItem.getValue();
			} else {
				getSectionParent(parentTreeItem);
			}
		}
	}

	private void externalDOCLinkClicked(String link, Project project) {
		if (link != null && link.trim().length() > 0) {
			if (engagementActionController != null) {
				FileHandler fileHandler = new FileHandler(app);
				Project currentProject = app.getSelectedProject();
				Goal currentGoal = engagementActionController.getCurrentGoal();
				File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(currentProject, currentGoal);
				File fileToOpen = new File(supportingDOCDirectory + File.separator + link);
				fileHandler.openFileOnDesktop(fileToOpen);
			}
		}
	}

	private Stage projectPopupStage;

	public void closeProjectPopupStage() {
		if (projectPopupStage != null) {
			projectPopupStage.close();
		}
	}

	private void urlLinkClicked(String link) {
		try {
			Desktop.getDesktop().browse(new URL(link).toURI());
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to open URL.");
			logger.log(Level.FINER, "Failed to open URL: " + e.getStackTrace());
		}
	}

	public void loadEngagementActionToContainer(EngagementActionController engagementActionController) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			fxmlLoader.setController(engagementActionController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			app.addNodeToERBContainer(root);
		} catch (Exception e) {
			logger.log(Level.FINE, "Failed to load EngagementAction.fxml.");
			logger.log(Level.FINER, "Failed to load EngagementAction.fxml: " + e.getStackTrace());
		}
	}

	protected void setColor(Pane tP) {
		if (engagementActionController != null) {
			try {
				TreeItem<ERBContentItem> erbContentItemSelected = engagementActionController.getTreeView()
						.getSelectionModel().getSelectedItem();
				getSectionParent(erbContentItemSelected);
			} catch (Exception e) {
				logger.log(Level.FINE, "Failed to setColor.");
				logger.log(Level.FINER, "Failed to setColor: " + e.getStackTrace());
			}
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public File getXmlContentFileToParse() {
		return xmlContentFileToParse;
	}

	public void setXmlContentFileToParse(File xmlContentFileToParse) {
		this.xmlContentFileToParse = xmlContentFileToParse;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}
}
