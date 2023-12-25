package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {
    private ITreeNode root;

    /**
     * This is the generateTree method from the ITreeGenerator interface. This method creates a copy of the
     * dataset passed in an removes the attribute as to not mutate the trainingData
     * @param trainingData    the dataset to train on
     * @param targetAttribute the attribute to predict
     */
    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
        Dataset copyData = trainingData.cloneWithRemoveFromAttribute(targetAttribute);
        //remove the target from the attribute list
        this.root = this.generateTreeHelper(copyData, targetAttribute);
    }

    /**
     * This is the generateTreeHelper method which first calculates a default value and then checks if a
     * leaf is needed (either all have same outcome or the attributeList is empty. If not, then an attribute
     * is chosen to split on and a dataset is partitioned on the attribute. Then a list of edgevalues is
     * created. We then look through all the partitioned sets and add to the value edges list and call the gth
     * method recursively using the new smaller subset. Finally, we return an attribute node with the edge
     * list as its last parameter
     * @param subset the dataset to build off of
     * @param targetAttribute the attribute we are looking to determine on
     * @return either the new leaf or attribute node
     */
    public ITreeNode generateTreeHelper(Dataset subset, String targetAttribute) {
        String defaultValue = subset.getDefault(targetAttribute);
        if (subset.sameOutcome(targetAttribute) || subset.attributeListEmpty()) {
            return new DecisionLeaf(defaultValue);
        } else {
            String attribute = subset.getAttributeToSplitOn();
            List<Dataset> newData = subset.partition(attribute);
            ArrayList<ValueEdge> edgeList = new ArrayList<>();
            for (Dataset p: newData) {
                edgeList.add(new ValueEdge(p.getDataObjects().get(0).getAttributeValue(attribute),
                        this.generateTreeHelper(p, targetAttribute)));
            }
            return new AttributeNode(attribute, defaultValue, edgeList);
        }
    }

    /**
     * This calls get decision on the root which either calls the attribute node's or the decision leaf's
     * getDecision method
     * @param datum the datum to lookup a decision for
     * @return the string which is found by recursing through the tree till it reaches a leaf or a default
     */
    @Override
    public String getDecision(Row datum) {
        return this.root.getDecision(datum);
    }


}
