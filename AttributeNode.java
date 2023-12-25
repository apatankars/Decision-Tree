package sol;

import java.util.List;
import src.ITreeNode;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
// TODO: Uncomment this once you've implemented the methods in the ITreeNode interface!
public class AttributeNode implements ITreeNode {
    // TODO: add more fields as needed
    private String value;
    private String deflt;
    private List<ValueEdge> outgoingEdges;

    /**
     * This is the constructor for the attribute node. It takes it a value of the attribute. It also
     * stores a default value if none of the value edges match the attribute value of the row. Finally
     * there is a list of value edges which was populated by the training data
     * @param value attribute value
     * @param defaults default value for when no value edge values match
     * @param outgoingEdges list of value edges with a value and a child node
     */
    public AttributeNode(String value, String defaults, List<ValueEdge> outgoingEdges) {
        this.value = value;
        this.deflt = defaults;
        this.outgoingEdges = outgoingEdges;
    }

    /**
     * Recurses through the tree if the row attribute value matches the value of one of the edges. If it does
     * it goes the value edge's child node. If not, it returns the default value.
     * @param forDatum the datum to lookup a decision for
     * @return either recurse to the edge's child node or the default value
     */
    @Override
    public String getDecision(Row forDatum) {
        for(ValueEdge edge:this.outgoingEdges) {
            if(forDatum.getAttributeValue(this.value).equals(edge.getValue())) {
                return edge.getChild().getDecision(forDatum);
            }
        }
        return this.deflt;
    }
}
