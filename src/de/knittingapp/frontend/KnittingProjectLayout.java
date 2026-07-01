package de.knittingapp.frontend;

import de.knittingapp.dto.KnittingInstruction;
import de.knittingapp.dto.KnittingPattern;
import de.knittingapp.dto.KnittingProject;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

/**
 * <pre>
 *     template for knitting project that consists of several knitting patterns
 * </pre>
 */
public class KnittingProjectLayout extends BorderPane {

    private TextField projectTitle;

    private TextArea description;

    private ListView<Button> textInstructions;

    private ListView<Button> knittingPatterns;

    private boolean unsaved;

    public KnittingProjectLayout(Stage primaryStage){
        createLayout(primaryStage);
        initProject(KnittingApp.knittingProject);
    }

    /** creates layout for KnittingProject tab in KnittingApp */
    public void createLayout(Stage primaryStage){
        projectTitle = new TextField();
        projectTitle.setPromptText("Enter title...");
        projectTitle.setOnKeyReleased(event -> {
            UnsavedChangesUtil.markUnsavedChanges();
            KnittingApp.knittingProject.setTitle(projectTitle.getText());
        });
        setTop(projectTitle);

        description = new TextArea();
        description.setPromptText("Enter short project description...");
        description.setOnKeyReleased(event -> {
            UnsavedChangesUtil.markUnsavedChanges();
            KnittingApp.knittingProject.setDescription(description.getText());
        });
        Button addInstructions = new Button("Add text instructions");
        Button addPatternButton = new Button("Add pattern");
        ObservableList<Button> textInstructionButtons = FXCollections.observableArrayList();
        textInstructions = new ListView<>(textInstructionButtons);
        textInstructions.setPrefHeight(Region.USE_COMPUTED_SIZE);
        textInstructions.setCellFactory(ButtonListCellFactory::createCell);
        ObservableList<Button> patternButtons = FXCollections.observableArrayList();
        knittingPatterns = new ListView<>(patternButtons);
        knittingPatterns.setCellFactory(ButtonListCellFactory::createCell);
        InstructionButtonsUtil.init(textInstructionButtons,patternButtons);
        VBox vbox1 = new VBox(addInstructions,textInstructions);
        VBox vbox2 = new VBox(addPatternButton,knittingPatterns);
        HBox hBox = new HBox(vbox1,vbox2);

        VBox center = new VBox(description,hBox);
        addInstructions.setOnAction(clicked ->{
            KnittingInstruction knittingInstruction = new KnittingInstruction();
            int index = KnittingApp.knittingProject.getNextFreeId();
            KnittingApp.knittingProject.addKnittingInstruction(knittingInstruction);
            TabPaneUtil.addKnittingInstructionsTab(index);
            InstructionButtonsUtil.addButton(index);
            System.out.println("List size: " + textInstructionButtons.size() +
                    ", Cell count: " + textInstructions.getItems().size());


        });
        addPatternButton.setOnAction(clicked -> createNewPattern(primaryStage));
        setCenter(center);
        getStylesheets().add("file:src/de/knittingapp/resources/knittingProjectLayout.css");
    }

    /**
     * opens popup with configurations for pattern creation
     * @param primaryStage for connection between stages
     */
    private void createNewPattern(Stage primaryStage) {
        Stage popUpStage = new NewPatternPopupStage(primaryStage);
        popUpStage.showAndWait();
    }

    /** updates layout based on loaded KnittingProject object attributes */
    public void initProject(KnittingProject knittingProject){
        projectTitle.setText(knittingProject.getTitle());
        description.setText(knittingProject.getDescription());
        for (Map.Entry<Integer, KnittingInstruction> entry : knittingProject.getKnittingInstructions().entrySet()){
            InstructionButtonsUtil.addButton(entry.getKey());
//            Button instructionButton = new Button(entry.getValue().getName());
//            instructionButton.setUserData(entry.getKey());
//            instructionButton.setOnAction(clicked -> TabPaneUtil.addKnittingInstructionsTab((int)instructionButton.getUserData()));
//            if (entry.getValue() instanceof KnittingPattern){
//                knittingPatterns.getItems().add(instructionButton);
//            } else {
//                textInstructions.getItems().add(instructionButton);
//            }
        }
    }

}
