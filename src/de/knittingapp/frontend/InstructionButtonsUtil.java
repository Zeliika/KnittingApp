package de.knittingapp.frontend;

import de.knittingapp.dto.KnittingInstruction;
import de.knittingapp.dto.KnittingPattern;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * TODO
 */
public class InstructionButtonsUtil {

//    private static ListView<Button> knittingInstructions;
//
//    private static ListView<Button> knittingPatterns;
//
//    public static void init(ListView<Button> knittingInstructions, ListView<Button> knittingPatterns){
//        InstructionButtonsUtil.knittingInstructions = knittingInstructions;
//        InstructionButtonsUtil.knittingPatterns = knittingPatterns;
//    }
    private static ObservableList<Button> knittingInstructions;

    private static ObservableList<Button> knittingPatterns;

    public static void init(ObservableList<Button> knittingInstructions, ObservableList<Button> knittingPatterns){
        InstructionButtonsUtil.knittingInstructions = knittingInstructions;
        InstructionButtonsUtil.knittingPatterns = knittingPatterns;
    }

    /**
     * adds button with name of KnittingInstruction to KnittingProjectLayout
     * sets action when button clicked
     * @param id id of the KnittingInstruction that will be connected with Button
     */
    public static void addButton(int id){
        KnittingInstruction knittingInstruction = KnittingApp.knittingProject.getKnittingInstructions().get(id);
        Button button = new Button(knittingInstruction.getName());
        button.setUserData(id);
        button.setOnAction(clicked -> TabPaneUtil.addKnittingInstructionsTab(id));
        if (knittingInstruction instanceof KnittingPattern){
            knittingPatterns.add(button);
        } else {
            knittingInstructions.add(button);
        }
    }

//    /**
//     *  removes button from KnittingPatternLayout if userData equals given id
//     * @param id id of the KnittingInstruction connected with Button
//     * @param isKnittingPattern whether KnittingInstruction is instance of KnittingPattern
//     */
//    public static void removeButton(int id, boolean isKnittingPattern) {
//        if (isKnittingPattern){
//            for (Node node : knittingPatterns.getItems()) {
//                if ((int) node.getUserData() == id) {
//                    knittingPatterns.getItems().remove(node);
//                    return;
//                }
//            }
//        } else {
//            for (Node node : knittingInstructions.getChildren()) {
//                if ((int) node.getUserData() == id) {
//                    knittingInstructions.getChildren().remove(node);
//                    return;
//                }
//            }
//        }
//    }

    /** updates text of all buttons in the vBoxes knittingInstructions and knittingPatterns */
    public static void updateButtonNames(){
        int id;
        for (Button button : knittingInstructions){
            if (button.getUserData() instanceof Integer) {
                id = (int) button.getUserData();
                button.setText(KnittingApp.knittingProject.getKnittingInstructions().get(id).getName());
            }
        }for (Button button : knittingPatterns){
            if (button.getUserData() instanceof Integer) {
                id = (int) button.getUserData();
                button.setText(KnittingApp.knittingProject.getKnittingInstructions().get(id).getName());
            }
        }
    }
}
