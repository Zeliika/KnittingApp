package de.knittingapp.frontend;

import de.knittingapp.dto.KnittingInstruction;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class KnittingInstructionLayout extends VBox {
    private TextField name;

    private TextArea writtenInstructions;

    private KnittingInstruction knittingInstruction;

    public KnittingInstructionLayout(KnittingInstruction knittingInstruction) {
        this.knittingInstruction = knittingInstruction;
        createLayout();
        initInstruction();
    }

    private void createLayout(){
        name = new TextField();
        name.setPromptText("Enter name...");
        name.setOnKeyReleased(event -> {
            UnsavedChangesUtil.markUnsavedChanges();
            knittingInstruction.setName(name.getText());
        });

        writtenInstructions = new TextArea();
        writtenInstructions.setPromptText("Enter short project description...");
        writtenInstructions.setOnKeyReleased(event -> {
            UnsavedChangesUtil.markUnsavedChanges();
            knittingInstruction.setInstructions(writtenInstructions.getText());
        });
        getChildren().addAll(name,writtenInstructions);
    }

    private void initInstruction(){
        name.setText(knittingInstruction.getName());
        writtenInstructions.setText(knittingInstruction.getInstructions());
    }
}
