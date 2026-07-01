package de.knittingapp.dto;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 *     represents a knitting pattern with a number of rows and a number of stitches in a row
 *     the stitches have an aspect ratio for better representation of the real knitting piece
 * </pre>
 */
public class KnittingPattern extends KnittingInstruction implements Serializable {
    /**
     * multidimensional array that represents the stitches in the pattern
     */
    private Stitch[][] pattern;

    /**
     * colors used in the pattern as hexadecimal code strings
     */
    private Color[] colorPallet = new Color[10];

    /**
     * aspect ratio of the stitches
     */
    private double aspectRatioStitch;

    /**
     * color of the background of the knitting pattern for visible gridlines
     */
    private Color backgroundColor;


    /**
     * constructor for creating new pattern
     * @param columns number of stitches in one row
     * @param rows number of rows in the pattern
     * @param ratio aspect ratio of the stitches
     */
    public KnittingPattern(int columns, int rows, double ratio){
        name = "New Pattern";
        instructions = "";
        pattern = new Stitch[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Stitch stitch = new Stitch(0, StitchTypes.RIGHT);
                pattern[i][j] = stitch;
            }
        }
        aspectRatioStitch = ratio;
        colorPallet[0] = Color.LIGHTGREY;
        backgroundColor = Color.BLACK;
    }

    /**
     * constructor for creating pattern from file
     * @param name name of the pattern
     * @param pattern array of stitches
     * @param aspectRatioStitch stitch aspect ratio
     */
    public KnittingPattern(String name, Stitch[][] pattern, double aspectRatioStitch, String text, Color[] colorPallet, Color backgroundColor) {
        super(name,text);
        this.pattern = pattern;
        this.aspectRatioStitch = aspectRatioStitch;
        this.colorPallet = colorPallet;
        this.backgroundColor = backgroundColor;
    }

    public KnittingPattern copy(){
        Stitch[][] patternCopy = new Stitch[pattern.length][pattern[0].length];
        for (int i = 0; i < pattern.length ; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                patternCopy[i][j] = new Stitch(pattern[i][j].getColorIndex(), pattern[i][j].getStitchType());
            }
        }
        return new KnittingPattern(
                name, patternCopy,
                getAspectRatioStitch(),
                getInstructions(),
                Arrays.copyOf(getColorPallet(), getColorPallet().length),
                getBackgroundColor());
    }

    public Stitch[][] getPattern() {
        return pattern;
    }

    public int getStitchColorIndex(int row, int column){
        return pattern[row][column].getColorIndex();
    }

    public StitchTypes getStitchType(int row, int column){
        return pattern[row][column].getStitchType();
    }

    public double getAspectRatioStitch() {
        return aspectRatioStitch;
    }

    public Color[] getColorPallet() {
        return colorPallet;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPattern(Stitch[][] pattern) {
        this.pattern = pattern;
    }

    public void setStitchColorIndex(int row, int column, int index){
        pattern[row][column].setColorIndex(index);
    }

    public void setStitchType(int row, int column, StitchTypes stitchTypes){
        pattern[row][column].setStitchType(stitchTypes);
    }

    public void setAspectRatioStitch(double aspectRatioStitch) {
        this.aspectRatioStitch = aspectRatioStitch;
    }

    @Override
    public String toString() {
        return "KnittingPattern{" +
                "name=" + getName() +
                "instructions=" + getInstructions() +
                "pattern=" + Arrays.toString(pattern) +
                ", colorPallet=" + Arrays.toString(colorPallet) +
                ", aspectRatioStitch=" + aspectRatioStitch +
                '}';
    }
}
