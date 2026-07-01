package de.knittingapp.frontend;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class ButtonListCellFactory {
    public static ListCell<Button> createCell(ListView<Button> listView) {
        return new ListCell<Button>() {
            private final Button deleteButton = new Button("X");

            {
                deleteButton.setOnAction(clicked -> {
                    int id = (int) getItem().getUserData();
                    TabPaneUtil.closeTab(id);
                    KnittingApp.knittingProject.removeKnittingInstruction(id);
                    listView.getItems().remove(getItem());
                });

            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(getItem(), deleteButton);
                    setGraphic(hBox);
                }
            }
        };
    }
}
