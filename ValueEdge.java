package sol;

import src.ITreeNode;

/**
 * A class that represents the edge of an attribute node in the decision tree
 */
public class ValueEdge {
    // TODO: add more fields if needed
    private String value;
    private ITreeNode child;

    /**
     * This is the valueEdge constructor which holds a value and a node
     * @param value the value of the edge
     * @param child the node it leads to
     */
    public ValueEdge(String value, ITreeNode child) {
        this.value = value;
        this.child = child;
    }

    /**
     * Get value which is called from attribute node when looping through the values
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Get child when a value edge value matches, traverse to its child in attribute node
     * @return the child
     */
    public ITreeNode getChild() {
        return this.child;
    }

}
