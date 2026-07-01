package de.knittingapp.dto;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

/**
 * <pre>
 *      template for stitch type objects
 * </pre>
 */
public enum StitchTypes implements Serializable {
    NONE( "no stitch here"){
        @Override
        public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
        }},
    RIGHT("knit"){
            @Override
            public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
                gc.strokeLine(x + width/2, y + gap, x + width/2, y + height - gap);

    }},
    LEFT("purl"){
        @Override
        public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
            gc.strokeLine(x + gap, y + height / 2, x + width - gap, y + height / 2);
        }},
    CROSS_RIGHT("slip slip knit"){
        @Override
        public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
            gc.strokeLine(x + gap, y + gap, x + width - gap, y + width - gap);
        }},
    CROSS_LEFT("knit two stitches together"){
        @Override
        public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
            gc.strokeLine(x + width - gap, y + gap, x + gap, y + height - gap);

        }},
    YARN_OVER("yarn over needle to add stitch"){
        @Override
        public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
            gc.strokeOval(x + gap, y + gap, width - 2 * gap, height - 2 * gap);
        }},
    CABLE_KNIT_RIGHT("cable knit left in front of right"){
        @Override
        public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
            gc.strokeLine(x + gap, y + height / 2, x + width - gap, y + height / 2);
            gc.strokeLine(x + width - gap, y + height / 2, x + width * 0.6, y + height*0.2);
            gc.strokeLine(x + width - gap, y + height / 2, x + width * 0.6, y + height*0.8);
        }},
    CABLE_KNIT_LEFT("cable-knit right in front of left"){
        @Override
        public void draw(GraphicsContext gc, double x, double y, double width, double height, double gap){
            gc.strokeLine(x + gap, y + height / 2, x + width - gap, y + height / 2);
            gc.strokeLine(x + gap, y + height / 2, x + width * 0.4, y + height*0.2);
            gc.strokeLine(x + gap, y + height / 2, x + width * 0.4, y + height*0.8);
        }};


    /** explains meaning of the symbol in a knittingPattern */
    private String explanation;

    public abstract void draw(GraphicsContext gc, double x, double y, double width, double height, double gap);

    StitchTypes(String explanation) {
        this.explanation = explanation;
    }


    public String getExplanation() {
        return explanation;
    }



}
