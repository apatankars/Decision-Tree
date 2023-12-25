package sol;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.w3c.dom.Attr;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {
    String trainingPath = "data/fruits-and-vegetables.csv"; // TODO: replace with your own input file
    String targetAttribute = "foodType"; // TODO: replace with your own target attribute
    List<String> attributeList;
    List<Row> dataObjects;
    Dataset training;
    TreeGenerator testGenerator;

    /**
     *  The method runs before every test to set up the dataset based on on the CSV file
     */
    @Before
    public void buildDataset() {
        this.dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        this.attributeList = new ArrayList<>(this.dataObjects.get(0).getAttributes());
        this.training = new Dataset(this.attributeList, this.dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();
        this.testGenerator.generateTree(this.training, this.targetAttribute);
    }

    /**
     * This tests get attribute list to make sure they are equal and then also checks what happens if a
     * an empty list is passed in
     */
    @Test
    public void testGetAttributeList() {
        Assert.assertEquals(this.attributeList, this.training.getAttributeList());

        //Testing if passed in an empty list
        Dataset dset = new Dataset(new ArrayList<>(), new ArrayList<>(), AttributeSelection.RANDOM);
        ArrayList list = new ArrayList();
        Assert.assertEquals(dset.getAttributeList(), list);
    }

    /**
     * This method tests size to make sure the size of the dataset is equal to the amount of the rows it has.
     * It also checks if size works on a empty dataset
     */
    @Test
    public void testSize() {
        Assert.assertEquals(this.training.size(), 7);

        //Testing on an empty Dataset
        Dataset dset = new Dataset(new ArrayList<>(), new ArrayList<>(), AttributeSelection.RANDOM);
        Assert.assertEquals(dset.size(), 0);
    }

    /**
     * Tests get default on both an attribute and the target attribute. I also check to make sure that if
     * the dataset is empty, it throws an exception.
     */
    @Test
    public void testGetDefault() {
        Assert.assertEquals(this.training.getDefault("color"), "green");
        Assert.assertEquals(this.training.partition("color").get(0).getDefault(this.targetAttribute),
                "vegetable");

        Dataset dset = new Dataset(new ArrayList<>(), new ArrayList<>(), AttributeSelection.RANDOM);
        Assert.assertThrows(IndexOutOfBoundsException.class, ()-> {dset.getDefault(this.targetAttribute);});
    }

    /**
     * This method makes sure partion works properly. I do this by checking size and also checking the attribute
     * value of one of the partitioned sets. I also make sure, if the dataset is empty, its size is still zero.
     */
    @Test
    public void testPartition() {
        Assert.assertEquals(this.training.partition("color").size(), 3);
        Assert.assertEquals(this.training.partition(this.targetAttribute).size(), 2);
        Assert.assertEquals(this.training.partition("color").
                get(0).getDataObjects().get(0).getAttributeValue("color"),
                "green");

        Dataset dset = new Dataset(new ArrayList<>(), new ArrayList<>(), AttributeSelection.RANDOM);
        Assert.assertEquals(dset.partition(this.targetAttribute).size(), 0);
    }

    /**
     * This tests my most frequent method which is used in my getDefault method. I create a list and find
     * the most common string. If the list is empty, it throws an exception.
     */
    @Test
    public void testMostFrequent() {
        List newList = new ArrayList();
        newList.add("dad");
        newList.add("mom");
        newList.add("mom");
        newList.add("mom");
        newList.add("mom");
        newList.add("dad");

        Assert.assertEquals(this.training.mostFrequent(newList), "mom");

        List empty = new ArrayList();
        Assert.assertThrows(IndexOutOfBoundsException.class, ()-> {
            this.training.mostFrequent(empty);});
    }

    /**
     * This tests my allSameOutcome method which chekcs if all the rows in the dataset have the same outcome
     * for the attribute. I do so by first splitting my datasets and then calling it to assertTrue
     */
    @Test
    public void testAllSame() {
        Assert.assertTrue(this.training.partition("calories").get(0).
                sameOutcome(this.targetAttribute));
        Assert.assertTrue(this.training.partition("color").get(2).
                sameOutcome(this.targetAttribute));
    }

    /**
     * This tests getTargetValues which depending on the boolean distinct, will return a list of the values
     * of attribute passed in. I use size since it is hard to compare lists directly.
     */
    @Test
    public void testGetTargetValues() {
        Assert.assertEquals(this.training.getTargetValues("color", true).size(),
                3);
        Assert.assertEquals(this.training.getTargetValues("highProtein", true).size(),
                2);
        Assert.assertEquals(this.training.getTargetValues("color", false).size(),
                7);
    }

    /**
     * This is test nodes which tests the constructors and getDecision by essentially hard coding a decisionTree
     * with its attribute and leaf nodes and a list of value of edges. Therefore, when I call get decision on my
     * newly created row, I know the value and I can see the constructors and method are working properly.
     */
    @Test
    public void testNodes() {
        DecisionLeaf cal = new DecisionLeaf("vegetable");
        List valueEdgesCalories = new ArrayList();
        valueEdgesCalories.add(new ValueEdge("high", cal));
        AttributeNode calories = new AttributeNode("calories", "vegetable",
                valueEdgesCalories);
        List valueEdgesColor = new ArrayList();
        valueEdgesColor.add(new ValueEdge("green", calories));
        AttributeNode color = new AttributeNode("color", "vegetable",
                valueEdgesColor);

        Row cucumber = new Row("test Row (cucumber)");
        cucumber.setAttributeValue("color", "green");
        cucumber.setAttributeValue("calories", "high");

        Assert.assertEquals("vegetable", color.getDecision(cucumber));

    }

    /**
     * This tests tree generator by creating two arbitrary lists one which matches a set from the training
     * data and another which relies on ona default value. This ensures that the tree traverses properly
     * and can use default properly
     */
    @Test
    public void testTreeGenerator() {
        Row tangerine = new Row("test row (tangerine)");
        tangerine.setAttributeValue("color", "orange");
        tangerine.setAttributeValue("highProtein", "false");
        tangerine.setAttributeValue("calories", "high");
        Assert.assertEquals("fruit", this.testGenerator.getDecision(tangerine));

        Dataset highCal = this.training.partition("calories").get(1);
        Assert.assertEquals(highCal.getDefault(this.targetAttribute), "fruit");
        Row avocado = new Row("test row (avocado)");
        avocado.setAttributeValue("color", "green");
        avocado.setAttributeValue("highProtein", "false");
        avocado.setAttributeValue("calories", "high");
        Assert.assertEquals("fruit", this.testGenerator.getDecision(avocado));
    }


}
