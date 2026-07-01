package de.knittingapp.frontend;

import de.knittingapp.dto.KnittingProject;
import de.knittingapp.middletier.KnittingProjectService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class KnittingApp extends Application {
    private Stage primaryStage;

    private FileChooser fileChooser = new FileChooser();

    private TabPane tabPane;

    private Menu fileMenu;

    private MenuItem saveProjectMenuItem;

    private MenuItem saveProjectAsMenuItem;

    public static KnittingProject knittingProject;

    public static KnittingProjectService knittingProjectService = new KnittingProjectService();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        VBox root = new VBox();
        fileMenu = new Menu("File");
        MenuItem newProjectMenuItem = new MenuItem("New...");
        MenuItem openProjectMenuItem = new MenuItem("Open...");
        saveProjectMenuItem = new MenuItem("Save");
        saveProjectAsMenuItem = new MenuItem("Save as...");
        fileMenu.getItems().addAll(newProjectMenuItem, openProjectMenuItem, saveProjectMenuItem, saveProjectAsMenuItem);
        Menu menu2 = new Menu("Help");
        MenuItem symbolLegendMenuItem = new MenuItem("Show symbol legend");
        menu2.getItems().add(symbolLegendMenuItem);
        MenuBar menuBar = new MenuBar(fileMenu,menu2);

        tabPane = new TabPane();
        TabPaneUtil.initTabPane(tabPane);
        newProjectMenuItem.setOnAction(clicked -> createNewProject());
        openProjectMenuItem.setOnAction(clicked -> openProject());
        saveProjectMenuItem.setOnAction(clicked -> saveProject());
        saveProjectAsMenuItem.setOnAction(clicked -> saveProjectAs());
        symbolLegendMenuItem.setOnAction(clicked -> showSymbolLegend());
        saveProjectMenuItem.setDisable(true);
        saveProjectAsMenuItem.setDisable(true);
        fileChooser.initialFileNameProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()){
                saveProjectMenuItem.setDisable(false);
            }
        });
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.jspn)", "*.json"));
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "KnittingApp/patterns"));
        root.getChildren().addAll(menuBar, tabPane);

        Scene mainScene = new Scene(root, 500, 500);
        primaryStage.setScene(mainScene);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            if(!tabPane.getTabs().isEmpty()){
                if (UnsavedChangesUtil.checkForUnsavedChanges()) {
                    Alert discardUnsavedChanges = new Alert(Alert.AlertType.CONFIRMATION, "Discard unsaved changes?");
                    Optional<ButtonType> result = discardUnsavedChanges.showAndWait();
                    if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
                        return;
                    } else {
                        primaryStage.close();
                    }
                }
            }
            primaryStage.close();
        });
        primaryStage.show();
    }

    /**
     * checks whether there's already a project displayed by checking if there are Tabs in the TabPane
     * asks user in this case if user really wants to create a new project
     * if yes or if there is no project displayed:
     * creates new KnittingProjectObject and KnittingProjectLayout
     * calls createMainTab method
     */
    private void createNewProject() {
        if(!discardOpenProjectIfExisting()){
            return;
        }
        knittingProject = new KnittingProject();
        KnittingProjectLayout newProject = new KnittingProjectLayout(primaryStage);
        createMainTab(newProject);
        saveProjectMenuItem.setDisable(true);
        saveProjectAsMenuItem.setDisable(false);
    }

    /**
     * checks whether a project is displayed
     * asks user if existing project should be discarded
     * clears tabs if answer is yes
     * @return true if there is no project or if project is discarded
     */
    private boolean discardOpenProjectIfExisting(){
        if (!tabPane.getTabs().isEmpty()){
            Alert discardProjectChoice = new Alert(Alert.AlertType.CONFIRMATION,
                    "Another project ist already open. Continuing closes the old project and unsaved " +
                            "changes will be discarded.");
            Optional<ButtonType> result = discardProjectChoice.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                tabPane.getTabs().clear();
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /** creates Tab for KnittingProject, sets its content and adds tab to TabPane */
    private void createMainTab(KnittingProjectLayout layout){
        Tab mainTab = new Tab();
        mainTab.setContent(layout);
        mainTab.setClosable(false);
        mainTab.setText(knittingProject.getTitle());
        UnsavedChangesUtil.init(mainTab);
        tabPane.getTabs().add(mainTab);
    }

    /**
     * opens new window with explanation of symbols in knitting patterns
     */
    private void showSymbolLegend() {
        SymbolLegendPopupStage legend = new SymbolLegendPopupStage(primaryStage);
        legend.show();
    }

    /**
     * opens dialog to choose path for saving
     * if path is selected KnittingProjectService is ordered to
     *      deliver KnittingProject object and path to backend for saving
     */
    private void saveProjectAs() {
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            knittingProjectService.save(knittingProject, file);
            UnsavedChangesUtil.removeMarker();
            fileChooser.setInitialFileName(file.getName());
            fileChooser.setInitialDirectory(new File(file.getParent()));
            InstructionButtonsUtil.updateButtonNames();
            TabPaneUtil.updateTabTitles();
        }
    }

    /**
     * saves project if path is already defined
     */
    private void saveProject() {
        File file = new File(fileChooser.getInitialDirectory() + "\\" + fileChooser.getInitialFileName());
        knittingProjectService.save(knittingProject,file);
        UnsavedChangesUtil.removeMarker();
        InstructionButtonsUtil.updateButtonNames();
        TabPaneUtil.updateTabTitles();
    }

    /**
     * opens fileChooser and lets user select project file path
     * if path is selected KnittingProjectService is ordered to get KnittingProject object from backend
     */
    private void openProject() {
        if(!discardOpenProjectIfExisting()){
            return;
        }
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            knittingProject = knittingProjectService.getKnittingProject(file);
            fileChooser.setInitialFileName(file.getName());
            fileChooser.setInitialDirectory(new File(file.getParent()));
            KnittingProjectLayout layout = new KnittingProjectLayout(primaryStage);
            createMainTab(layout);
            saveProjectAsMenuItem.setDisable(false);
        }
    }
}
