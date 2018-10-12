package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class represents the DigramList for basic digrams.
 *
 * The DigramList is a data structure for all active digrams. Each entry in the DigramList is a Digram.
 *
 * @author Matthias Duerksen
 */
public class DigramList {

    /**
     * the data structure for DigramList which is realized by a double connected HashMap.
     */
    private final Map<String, HashMap<String, Digram>> digramList = new HashMap<>();

    /**
     * a set for the necessary different labels of the nodes in the graph.
     */
    private final LinkedList<String> labels;

    /**
     * Constructor for the DigramList.
     * @param labels the necessary different labels of the nodes in the graph.
     */
    public DigramList(LinkedList<String> labels) {

        this.labels = labels;
    }

    /**
     * Gets the Digram for the labels label1,label2.
     * @param label1 label for the start node of the digram.
     * @param label2 label for the end node of the digram.
     * @return the digram for the two labels.
     */
    public Digram getDigram(String label1, String label2) {
        if (!labels.contains(label1) || !labels.contains(label2)) {
            return null;
        }

        if (!digramList.containsKey(label1)) {
            digramList.put(label1, new HashMap<>());
        }
        if (!digramList.get(label1).containsKey(label2)) {

            digramList.get(label1).put(label2, new Digram(label1, label2, equivStartNode, equivEndNode));
        }
        return digramList.get(label1).get(label2);
    }

    /**
     * Checks if the digram list has a digram for the two labels.
     * @param label1 start node for the digram.
     * @param label2 end node for the digram.
     * @return true if the digram list contains the digram, else false.
     */
    private boolean containsDigramFor(String label1, String label2) {
        return (digramList.containsKey(label1) && digramList.get(label1).containsKey(label2));
    }

    /**
     * Gets the digram with the maximal associated digrams.
     * @return digram with the maximal associated digrams.
     */
    public Digram getMaxDigram() {
        Digram maxDigram = null;
        int currentSize = 1;
        for (Digram digram : getAllActiveDigrams()) {
            if (digram.getSize() > currentSize && !digram.hasBeenAplied()) {
                maxDigram = digram;
                currentSize = digram.getSize();
                digram.setBeenApplied();
            }
        }
        return maxDigram;
    }

    /**
     * adds a new label for the digram list.
     * @param label the new label for the digram list.
     */
    public void addNewLabel(String label) {
        labels.add(label);
    }

    /**
     * Gets all active digrams of the digram list.
     * @return all active digrams of the digram list in a LinkedList.
     */
    public LinkedList<Digram> getAllActiveDigrams() {
        LinkedList<Digram> digrams = new LinkedList<>();
        for (String label1 : labels) {
            for (String label2 : labels) {
                if (containsDigramFor(label1, label2) && getDigram(label1, label2).getSize() > 1) {
                    digrams.add(getDigram(label1, label2));
                }
            }
        }
        return digrams;
    }


}
