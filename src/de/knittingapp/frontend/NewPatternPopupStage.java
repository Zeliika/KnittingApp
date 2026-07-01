package de.knittingapp.frontend;

import de.knittingapp.dto.KnittingPattern;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewPatternPopupStage extends Stage {

    public NewPatternPopupStage(Stage primaryStage) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(primaryStage);
        createLayout();
    }

    private void createLayout(){
        setTitle("Create new pattern...");
        GridPane gridPane = new GridPane();
        Label rows = new Label("Rows ");
        Label stitches = new Label("Stitches per row ");
        Spinner rowNumberInput = new Spinner(1,256,1);
        rowNumberInput.setEditable(true);
        Spinner stitchNumberInput = new Spinner(1,256,1);
        stitchNumberInput.setEditable(true);
        Label ratioLabel = new Label("Stitch ratio");
        Label width = new Label("X");
        Label height = new Label("Y");
        Spinner widthInput = new Spinner(0.0,100.0,1.0,0.1);
        widthInput.setEditable(true);
        Spinner heightInput = new Spinner(0.0,100.0,1.0,0.1);
        heightInput.setEditable(true);
        Button createButton = new Button("Create Pattern");
        Button cancelButton = new Button("Cancel");
        gridPane.add(rows, 0,0);
        gridPane.add(stitches, 0,1);
        gridPane.add(rowNumberInput, 1,0);
        gridPane.add(stitchNumberInput, 1,1);
        gridPane.add(ratioLabel,0,2,2,1);
        gridPane.add(width, 0,3);
        gridPane.add(height, 0,4);
        gridPane.add(widthInput,1,3);
        gridPane.add(heightInput,1,4);
        gridPane.add(createButton, 0,5);
        gridPane.add(cancelButton,1,5);

        cancelButton.setOnAction(clicked -> close());
        createButton.setOnAction(clicked -> {
            int numberRows = (int)rowNumberInput.getValueFactory().getValue();
            int numberColumns = (int)stitchNumberInput.getValueFactory().getValue();
            double ratio = (double)widthInput.getValueFactory().getValue()/(double)heightInput.getValueFactory().getValue();
            KnittingPattern knittingPattern = new KnittingPattern(numberColumns, numberRows, ratio);
            int index = KnittingApp.knittingProject.getNextFreeId();
            KnittingApp.knittingProject.addKnittingInstruction(knittingPattern);
            TabPaneUtil.addKnittingInstructionsTab(index);
            InstructionButtonsUtil.addButton(index);
            close();
        });

        Scene scene = new Scene(gridPane);
        setScene(scene);
    }

}
