package sol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset{
    private AttributeSelection selectionType;
    private List<Row> dataObjects;
    private List<String> attributeList;

    /**
     * Constructor for a Dataset object
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param attributeSelection - an enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection attributeSelection) {
        this.attributeList = new ArrayList<String>(attributeList);
        this.dataObjects = new ArrayList<Row>(dataObjects);
        this.selectionType = attributeSelection;
    }

    /**
     * This method, depending on the enum of the dataset, returns an attribute from the list
     * @return the attribute to split on, from the attribute list which shrinks with each partition
     */
    public String getAttributeToSplitOn() {
        switch (this.selectionType) {
            case ASCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(0);
            }
            case DESCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
            }
            case RANDOM -> {
                Random random = new Random();
                int upperBound = this.attributeList.size();
                int randomNum = random.nextInt(upperBound);
                return this.attributeList.stream().sorted().toList().get(randomNum);
            }
        }
        throw new RuntimeException("Non-Exhaustive Switch Case");
    }

    /**
     * Method to get attribute list
     * @return attribute list
     */
    @Override
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    /**
     * Method to get the data objects
     * @return the list of data objects
     */
    @Override
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    /**
     * Method to get the selection type
     * @return the selection type enum
     */
    @Override
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }

    /**
     * Returns the size of the data sets based on the amount of rows
     * @return the size of the data set
     */
    @Override
    public int size() {return this.dataObjects.size();
    }

    /**
     * From the dataset, given the attibute to get the values of, the method returns the values of the
     * attribute contained in the dataset. If wanted to be distinct, it checks to make sure the list
     * does not already contain the value.
     * @param targetAttribute attribute which you want the values of
     * @param distinct whether you want to list to have repeat values
     * @return the list of values of the attribute (distinct or not)
     */
    public ArrayList<String> getTargetValues(String targetAttribute, Boolean distinct) {
        ArrayList<String> empty = new ArrayList<>();
        for (Row row: this.dataObjects) {
            if (distinct) {
                if(!empty.contains(row.getAttributeValue(targetAttribute))) {
                    empty.add(row.getAttributeValue(targetAttribute));
                }
            } else {
                empty.add(row.getAttributeValue(targetAttribute));
            }
        }
        return empty;
    }

    /**
     * Checks if the list of attributes is empty
     * @return a boolean if the size of the list is zero
     */
    public boolean attributeListEmpty() {
        if (this.attributeList.size() == 0) {
            return true;
        } else
            return false;
    }

    /**
     * Clones the current dataset by returning a new dataset with a new attribute list with the
     * parameter attribute removed
     * @param attributeToRemove the attribute to remove from the attribute list
     * @return the new dataset with the target attribute removed
     */
    public Dataset cloneWithRemoveFromAttribute(String attributeToRemove) {
        ArrayList<String> newAttributeList = new ArrayList<>(this.attributeList);
        newAttributeList.remove(attributeToRemove);
        return new Dataset(newAttributeList, this.dataObjects, this.selectionType);
    }

    /**
     * Method to split the dataset into different datasets based on the distinct values of the attribute
     * to split on
     * @param attributeToSplitOn the attribute by which we want to split the datasets on
     * @return a list of datasets divided into different ones based on the distinct values of the attributes
     */
    public List<Dataset> partition(String attributeToSplitOn) {
        ArrayList<Dataset> empty = new ArrayList<>();
        for (String value : this.getTargetValues(attributeToSplitOn, true)) {
            ArrayList<Row> newOne = new ArrayList<>();
            for (Row row : this.dataObjects) {
                if (row.getAttributeValue(attributeToSplitOn).equals(value)) {
                    newOne.add(row);
                }
            }
            ArrayList<String> newAttributeList = new ArrayList<>(this.attributeList);
            newAttributeList.remove(attributeToSplitOn);
            empty.add(new Dataset(newAttributeList, newOne, this.selectionType));
        }
        return empty;
    }

    /**
     * Calculates which of the targetAttributes appears the most to get the default value by calling
     * my helper method. If the dataset is empty, it throws an IndexOutofBoundsException to
     * indicate the dataset is empty
     * @param targetAttribute the attribute which we are trying to find the most common of
     * @return attribute which is the most common
     */
    public String getDefault(String targetAttribute) {
        if (this.size() == 0) {
            throw new IndexOutOfBoundsException("Dataset is Empty");
        }
        ArrayList<String> attList = this.getTargetValues(targetAttribute, false);
        return this.mostFrequent(attList);
    }

    /**
     * If all the outcomes in a given dataset for the given attribute are the same, it returns true
     * @param targetAttribute the attribute for which to check if all are the same
     * @return if the size of the distinct list is one, the method returns true
     */
    public boolean sameOutcome(String targetAttribute) {
        ArrayList<String> outcomes = this.getTargetValues(targetAttribute, true);
        if (outcomes.size() == 1) {
            return true;
        } else
        return false;
    }

    /**
     * Calculates the most frequent string from a given list. If the list is empty, it throws an exception. First
     * it sorts the list, and then it starts comparing the values by comparing to each past value. It will go through
     * the entire list and if there is a most common value, it returns that value. If the values are the same, it returns
     * one at random.
     * @param strings a list of strings for which to find the most commmon element
     * @return the most common string in the list inputted
     */
    public String mostFrequent(List<String> strings) {
        if (strings.isEmpty()) {
            throw new IndexOutOfBoundsException("List is Empty");
        }
        List<String> sortedStrings = strings.stream().sorted().toList();
        String currentString = sortedStrings.get(0);
        int currentFrequency = 1;
        String mostFrequentString = currentString;
        int highestFrequency = currentFrequency;

        for (int i = 1; i < sortedStrings.size(); i++) {
            String s = sortedStrings.get(i);
            if (s.equals(currentString)) {
                currentFrequency++;
            } else {
                if (currentFrequency > highestFrequency) {
                    highestFrequency = currentFrequency;
                    mostFrequentString = currentString;
                }
                currentString = s;
                currentFrequency = 1;
            }
        }
        if (currentFrequency > highestFrequency) {
            mostFrequentString = currentString;
        }
        return mostFrequentString;
    }

}
