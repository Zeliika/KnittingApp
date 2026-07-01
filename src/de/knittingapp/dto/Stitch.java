package de.knittingapp.dto;


import java.io.Serializable;

/**
 * describes a stitch in a knitting pattern
 * a stitch has a symbol depending on the type and a color
 */
public class Stitch implements Serializable {
    /** color of the stitch saved as index of color in colorPallet in knitting pattern */
    private int colorIndex;

    /** type of the stitch */
    private StitchTypes stitchType;

    /**
     * constructor for new stitches when creating new pattern
     * stitches have no color and no stitch type at the beginning
     */
    public Stitch() {
        stitchType = StitchTypes.NONE;
    }

    /**
     * constructor for stitches when creating pattern from file
     * @param colorIndex color of the stitch
     * @param stitchType type of the stitch
     */
    public Stitch(int colorIndex, StitchTypes stitchType){
        this.colorIndex = colorIndex;
        this.stitchType = stitchType;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public StitchTypes getStitchType() {
        return stitchType;
    }

    public void setStitchType(StitchTypes stitchType) {
        this.stitchType = stitchType;
    }

    @Override
    public String toString() {
        return "Stitch{" +
                "color index=" + colorIndex +
                ", stitchType=" + stitchType +
                '}';
    }
}
