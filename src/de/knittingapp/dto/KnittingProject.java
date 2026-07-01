package de.knittingapp.dto;

import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     template for a knitting project
 *     a knitting project can include several patterns as well as text instructions
 * </pre>
 */
public class KnittingProject {
    /** title of the project */
    private String title;

    /** short description of the project */
    private String description;

    /** array of written instructions without pattern */
    private Map<Integer, KnittingInstruction> knittingInstructions;

    /** used as key when adding knitting instructions to map */
    private int nextFreeId;

    public KnittingProject() {
        title = "New project";
        knittingInstructions = new HashMap<>();
    }

//    public KnittingProject(String title, String description, Map<Integer, KnittingInstruction> knittingInstructions) {
//        this.title = title;
//        this.description = description;
//        this.knittingInstructions = knittingInstructions;
//    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Map<Integer, KnittingInstruction> getKnittingInstructions() {
        return Map.copyOf(knittingInstructions);
    }

    public int getNextFreeId() {
        return nextFreeId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** adds new KnittingInstruction to Map with nextFreeId as key and increases nextFreeId */
    public void addKnittingInstruction(KnittingInstruction knittingInstruction){
        knittingInstructions.put(nextFreeId, knittingInstruction);
        nextFreeId++;
    }

    /**
     * removes KnittingInstruction from KnittingInstructions Map
     * @param id key of the KnittingInstruction
     */
    public void removeKnittingInstruction(int id){
        knittingInstructions.remove(id);
    }

    @Override
    public String toString() {
        return "KnittingProject{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", knittingInstructions=" + knittingInstructions +
                ", nextFreeId=" + nextFreeId +
                '}';
    }
}
