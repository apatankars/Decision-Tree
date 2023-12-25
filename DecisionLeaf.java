package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
// TODO: Uncomment this once you've implemented the methods in the ITreeNode interface!
public class DecisionLeaf implements ITreeNode{

    private String value;

    /**
     * Constructor for DecisionLeaf which just takes in a value (Decision)
     * @param value the decision
     */
    public DecisionLeaf(String value) {
        this.value = value;
    }

    /**
     * If a row reaches a leaf, it returns the decision
     * @param forDatum the datum to lookup a decision for
     * @return the decsion
     */
    @Override
    public String getDecision(Row forDatum) {
        return this.value;
    }
}
