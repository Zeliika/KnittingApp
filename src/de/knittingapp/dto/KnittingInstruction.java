package de.knittingapp.dto;

/**
 * <pre>
 *     template for knitting instruction without pattern
 *     not every knitting can be described as a pattern with symbols or colors
 * </pre>
 */
public class KnittingInstruction {
    /** name for the knitting described in this class */
    protected String name;

    /** complete instructions */
    protected String instructions;

    /** constructor for creating new KnittingInstruction */
    public KnittingInstruction() {
        this.name = "New Instruction";
        this.instructions = "";
    }

    /** constructor for creating KnittingInstruction from file */
    public KnittingInstruction(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "KnittingInstruction{" +
                "name='" + name + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }
}
