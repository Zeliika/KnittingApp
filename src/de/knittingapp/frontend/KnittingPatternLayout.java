package de.knittingapp.frontend;

import de.knittingapp.dto.KnittingPattern;
import de.knittingapp.dto.StitchTypes;
import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;

import java.util.Set;


public class KnittingPatternLayout extends BorderPane {

    /**
     * knitting pattern that should be displayed
     */
    private KnittingPattern knittingPattern;

    /**
     * backup for reset of changes
     */
    private KnittingPattern backup;

    /**
     * sets whether pattern can be edited
     */
    private boolean editModeActive;
    //TODO: if new pattern is created and tab is closed before saving pattern should be deleted from project
    private boolean unsavedNewPattern;

    /**
     * Canvas that displays knitting pattern
     */
    private Canvas knittingPatternGrid;

    private GraphicsContext gc;

    private Rectangle gridBackground;

    private final int cellSize = 16;

    /**
     * Color Picker to select color for stitch coloring
     */
    private ColorPicker colorPicker;

    private ToggleGroup stitchTypeButtons;

    private ToggleGroup colorButtons;

    private GridPane colorChooser;

    /**
     * TextArea to add additional information to pattern
     */
    private TextArea textArea;

    private Button resetEditButton;

    private TextField nameTextField;

    /**
     * constructor for creating knitting pattern layout based on existing pattern
     */
    public KnittingPatternLayout(KnittingPattern pattern) {
        backup = pattern.copy();
        knittingPattern = pattern;
        int rows = knittingPattern.getPattern().length;
        int columns = knittingPattern.getPattern()[0].length;
        double ratio = knittingPattern.getAspectRatioStitch();
        createTop();
        createRight();
        createCenter(rows, columns, ratio);
        createBottom();
        configureEditMode();
        initPattern();
    }

    /**
     * creates layout for bottom part of KnittingPatternLayout
     */
    private void createBottom() {
        textArea = new TextArea();
        textArea.setOnKeyReleased(event -> {
            UnsavedChangesUtil.markUnsavedChanges();
            knittingPattern.setInstructions(textArea.getText());
        });
        textArea.setPromptText("Enter text here...");
        //TODO maybe allow editing of TextFields or TextAreas and set editMode only for pattern
        textArea.setEditable(false);
        setBottom(textArea);
    }

    /**
     * creates layout for right part of KnittingPatternLayout
     */
    private void createRight() {
        colorPicker = new ColorPicker();
        colorPicker.setVisible(false);
        Button changeGridBackgroundButton = new Button("Change gridline color");
        Rectangle rectangle = new Rectangle(cellSize, cellSize, knittingPattern.getBackgroundColor());
        changeGridBackgroundButton.setGraphic(rectangle);
        changeGridBackgroundButton.setOnAction(clicked -> {
            colorPicker.show();
            colorPicker.setOnAction(colorPicked -> {
                Color color = colorPicker.getValue();
                rectangle.setFill(color);
                gridBackground.setFill(color);
                knittingPattern.setBackgroundColor(color);
            });
        });

        GridPane stitchTypeChooser = new GridPane();
        stitchTypeButtons = new ToggleGroup();
        for (int i = 0; i < StitchTypes.values().length; i++) {
            ToggleButton stitchTypeButton = new ToggleButton();
            stitchTypeButton.setUserData(StitchTypes.values()[i]);
            Canvas canvas = new Canvas(cellSize, cellSize);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StitchTypes.values()[i].draw(gc, 0, 0, cellSize, cellSize, 2);
            stitchTypeButton.setGraphic(canvas);
            stitchTypeButtons.getToggles().add(stitchTypeButton);
            stitchTypeChooser.add(stitchTypeButton, i % 2, i / 2);
        }
        stitchTypeButtons.selectToggle(stitchTypeButtons.getToggles().getFirst());
        stitchTypeButtons.selectedToggleProperty().addListener((observable, oldStitchType, newStitchType) -> {
            if (newStitchType == null) {
                stitchTypeButtons.selectToggle(stitchTypeButtons.getToggles().getFirst());
            }
        });

        colorChooser = new GridPane();
        colorButtons = new ToggleGroup();
        for (int i = 0; i < knittingPattern.getColorPallet().length; i++) {
            ToggleButton colorButton = new ToggleButton();
            colorButton.getStylesheets().add("file:src/de/knittingapp/resources/styles.css");
            colorButton.setPrefSize(30, 30);
            if (knittingPattern.getColorPallet()[i] != null) {
                Color color = knittingPattern.getColorPallet()[i];
                colorButton.setUserData(color);
                colorButton.setStyle("-fx-base-color: " + convertColorToString(color) + ";");
            }
            colorButtons.getToggles().add(colorButton);
            colorChooser.add(colorButton, i % 2, i / 2);
            colorButton.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    colorPicker.show();
                    colorPicker.setOnAction(colorPicked -> {
                        Color color = colorPicker.getValue();
                        int index = colorButtons.getToggles().indexOf(colorButton);
                        knittingPattern.getColorPallet()[index] = color;
                        colorButton.setUserData(color);
                        colorButton.setStyle("-fx-base-color: " + convertColorToString(color) + ";");
                        for (int row = 0; row < knittingPattern.getPattern().length; row++) {
                            for (int column = 0; column < knittingPattern.getPattern()[0].length; column++) {
                                if (knittingPattern.getStitchColorIndex(row, column) == index) {
                                    drawCell(row, column, color, knittingPattern.getStitchType(row, column));
                                }
                            }
                        }
                        UnsavedChangesUtil.markUnsavedChanges();
                    });
                }
            });

        }
        colorButtons.selectToggle(colorButtons.getToggles().getFirst());
        colorButtons.selectedToggleProperty().addListener((observable, oldColor, newColor) -> {
            if (newColor == null || newColor.getUserData() == null) {
                colorButtons.selectToggle(oldColor);
            }
        });

        VBox vBox = new VBox(colorPicker, changeGridBackgroundButton, stitchTypeChooser, colorChooser);
        setRight(vBox);
        getRight().setVisible(false);
    }

    /**
     * creates layout for top part of KnittingPatternLayout
     */
    private void createTop() {
        nameTextField = new TextField();
        nameTextField.setOnKeyReleased(event -> {
            UnsavedChangesUtil.markUnsavedChanges();
            knittingPattern.setName(nameTextField.getText());
        });
        nameTextField.setPromptText("Enter a name for the pattern...");
        ToggleButton activateEditModeButton = new ToggleButton("Edit");
        resetEditButton = new Button("Reset");
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(activateEditModeButton, resetEditButton);
        HBox top = new HBox(nameTextField, buttonBar);
        setTop(top);

        resetEditButton.setVisible(false);

        activateEditModeButton.setOnAction(toggled -> {
            editModeActive = !editModeActive;
            configureEditMode();
        });
        resetEditButton.setOnAction(clicked -> drawAll(backup));
    }

    /**
     * creates layout for center of KnittingPatternLayout
     */
    private void createCenter(int rows, int columns, double ratio) {
        StackPane gridStack = new StackPane();
        gridBackground = new Rectangle();
        knittingPatternGrid = new Canvas(columns * cellSize, rows * cellSize);
        gridBackground.widthProperty().bind(knittingPatternGrid.widthProperty());
        gridBackground.heightProperty().bind(knittingPatternGrid.heightProperty());
        gc = knittingPatternGrid.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        gridStack.getChildren().addAll(gridBackground, knittingPatternGrid);
        knittingPatternGrid.setOnMouseClicked(event -> paintStitch(event.getX(), event.getY(), event.getButton()));
        knittingPatternGrid.setOnMouseDragged(event -> paintStitch(event.getX(), event.getY(), event.getButton()));

        Group group = new Group(gridStack);
        ScrollPane scrollPane = new ScrollPane(group);
        scrollPane.setId("scrollPane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);

        double scaleFactor = 2.0;

        Pane cornerPlaceholder = new Pane();
        cornerPlaceholder.setMinWidth(scaleFactor*cellSize);
        cornerPlaceholder.setBorder(null);
        cornerPlaceholder.setBackground(Background.fill(Color.RED));

        if (ratio > 1) {
            knittingPatternGrid.getTransforms().add(new Scale(scaleFactor * ratio, scaleFactor));
            gridBackground.getTransforms().add(new Scale(scaleFactor * ratio, scaleFactor));
        } else {
            knittingPatternGrid.getTransforms().add(new Scale(scaleFactor, scaleFactor / ratio));
            gridBackground.getTransforms().add(new Scale(scaleFactor, scaleFactor / ratio));
        }


        GridPane rowNumberGrid = new GridPane();
        for (int i = 0; i < rows; i++) {
            Label rowNumber = new Label(String.valueOf(i+1));
            rowNumberGrid.add(rowNumber,0,i);
            rowNumber.setTextAlignment(TextAlignment.RIGHT);
            rowNumberGrid.getRowConstraints().add(new RowConstraints(cellSize*scaleFactor));
            ColumnConstraints test = new ColumnConstraints();
            test.setHalignment(HPos.RIGHT);
            rowNumberGrid.getColumnConstraints().add(test);
        }
        GridPane stitchNumberGrid = new GridPane();
        stitchNumberGrid.setGridLinesVisible(true);
        stitchNumberGrid.setAlignment(Pos.BOTTOM_CENTER);
        for (int i = 0; i < rows; i++) {
            Label stitchNumber = new Label(String.valueOf(i+1));
            stitchNumber.setTextAlignment(TextAlignment.CENTER);
            stitchNumberGrid.add(stitchNumber,i,0);
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(cellSize*scaleFactor);
            columnConstraints.setMaxWidth(cellSize*scaleFactor);
            columnConstraints.setHalignment(HPos.CENTER);
            stitchNumberGrid.getColumnConstraints().add(columnConstraints);
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setValignment(VPos.BOTTOM);


        }

        rowNumberGrid.minWidth(cellSize);
        ScrollPane rowNumbersScrollPane = new ScrollPane(rowNumberGrid);
        rowNumbersScrollPane.setStyle("-fx-background-color:green;");
        rowNumbersScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        rowNumbersScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rowNumbersScrollPane.setMinWidth(cellSize * scaleFactor);
        rowNumbersScrollPane.setMaxWidth(cellSize * scaleFactor);
        rowNumbersScrollPane.setPannable(false);
        rowNumbersScrollPane.setMouseTransparent(true);

        ScrollPane stitchNumbersScrollPane = new ScrollPane(stitchNumberGrid);
        stitchNumbersScrollPane.setStyle("-fx-background-color:blue;");
        stitchNumbersScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        stitchNumbersScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        stitchNumbersScrollPane.setMaxHeight(20);
        stitchNumbersScrollPane.setMinHeight(20);
        stitchNumbersScrollPane.setPrefHeight(20);
        stitchNumbersScrollPane.setPannable(false);
        stitchNumbersScrollPane.setMouseTransparent(true);
        stitchNumbersScrollPane.setMaxHeight(Region.USE_COMPUTED_SIZE);
        scrollPane.hvalueProperty().bindBidirectional(stitchNumbersScrollPane.hvalueProperty());
        scrollPane.vvalueProperty().bindBidirectional(rowNumbersScrollPane.vvalueProperty());

        HBox topRow = new HBox();
        topRow.setBorder(null);
        topRow.getChildren().addAll(cornerPlaceholder, stitchNumbersScrollPane);
//        topRow.setCenter(stitchNumbersScrollPane);


        // Outer layout
        BorderPane borderPane = new BorderPane();
        borderPane.setBorder(null);
        borderPane.setTop(topRow);
        borderPane.setCenter(scrollPane);
        borderPane.setLeft(rowNumbersScrollPane);
        scrollPane.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
        });
        setCenter(borderPane);

    }

    /**
     * paints Stitch when MouseEvent on knittingPatternGrid occurs
     * changes of Stitch layout depend on selected colorButton and StitchTypeButton and on responsible MouseButton
     *
     * @param x           horizontal position of MouseEvent
     * @param y           vertical position of MouseEvent
     * @param mouseButton button of MouseEvent
     */
    private void paintStitch(double x, double y, MouseButton mouseButton) {
        if (editModeActive) {
            if (x < 0 || x >= knittingPatternGrid.getWidth() || y < 0 || y >= knittingPatternGrid.getHeight()) {
                return;
            }
            int row = (int) (y / cellSize);
            int column = (int) (x / cellSize);
            if (mouseButton == MouseButton.PRIMARY) {
                int colorIndex = knittingPattern.getStitchColorIndex(row, column);
                Color color = knittingPattern.getColorPallet()[colorIndex];
                StitchTypes stitch = (StitchTypes) ((ToggleButton) stitchTypeButtons.getSelectedToggle()).getUserData();
                knittingPattern.setStitchType(row, column, stitch);
                drawCell(row, column, color, stitch);
            } else if (mouseButton == MouseButton.SECONDARY) {
                ToggleButton activeColor = (ToggleButton) colorButtons.getSelectedToggle();
//                Color color = (Color)activeColor.getUserData();
                int colorIndex = colorChooser.getChildren().indexOf(activeColor);
                knittingPattern.setStitchColorIndex(row, column, colorIndex);
                StitchTypes stitch = knittingPattern.getStitchType(row, column);
                drawCell(row, column, knittingPattern.getColorPallet()[colorIndex], stitch);
            }
            UnsavedChangesUtil.markUnsavedChanges();

        }
    }
//TODO: end edit mode after saving? or button for leaving edit mode / change Edit button if in editMode

    /**
     * changes layout from viewing to editing and the other way around
     */
    private void configureEditMode() {
        getRight().setVisible(editModeActive);
        resetEditButton.setVisible(editModeActive);
        textArea.setEditable(editModeActive);
    }


    /**
     * converts Color to String for saving and css styling
     *
     * @param color color that should be converted to String
     * @return color as hexadecimal code String
     */
    private String convertColorToString(Color color) {
        return color.toString().replace("0x", "#");
    }

    /**
     * checks whether color is light or dark and adjusts color of symbol
     *
     * @param color color to check
     * @return true if color is dark
     */
    private boolean isColorDark(Color color) {
        double darkness = 1 - (0.299 * color.getRed() +
                0.587 * color.getGreen() +
                0.114 * color.getBlue());
        return darkness >= 0.5;
    }

    //TODO: parameter needed or use attribute knitting pattern -> depends on reset

    /**
     * retrieve color and text of knittingPatternGrid buttons
     * retrieves name and text from textField and textArea
     * updates knittingPattern according to retrieved data
     *
     * @param pattern
     */
    private void drawAll(KnittingPattern pattern) {
        int rows = pattern.getPattern().length;
        int columns = pattern.getPattern()[0].length;
        gridBackground.setFill(pattern.getBackgroundColor());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Color stitchColor = pattern.getColorPallet()[pattern.getStitchColorIndex(i, j)];
                drawCell(i, j, stitchColor, pattern.getStitchType(i, j));
            }

        }
    }

    /**
     * draws cell in canvas with given color and stitch type symbol
     *
     * @param row        row of Stitch in knittingPattern
     * @param column     column of stitch in knittingPattern
     * @param color      color of the stitch
     * @param stitchType type of the stitch
     */
    private void drawCell(int row, int column, Color color, StitchTypes stitchType) {
        double inset = 0.5;
        double x = column * cellSize;
        double y = row * cellSize;
        double inner = cellSize - 2 * inset;

        gc.setFill(color);
        gc.fillRect(x + inset, y + inset, inner, inner);
        if (isColorDark(color)) {
            gc.setStroke(Color.WHITE);
        } else {
            gc.setStroke(Color.BLACK);
        }
        gc.setLineWidth(2);
        stitchType.draw(gc, x, y, cellSize, cellSize, 2);
    }

    /**
     * changes layout depending on attributes of knittingPattern displayed by instance of this class
     */
    public void initPattern() {
        nameTextField.setText(knittingPattern.getName());
        textArea.setText(knittingPattern.getInstructions());
        drawAll(knittingPattern);
    }
}
