package model.DigramList;

import model.Digram.Digram;

import java.util.*;

/**
 * This class represents the DigramList for basic digrams.
 *
 * The DigramList is a data structure for all active digrams. Each entry in the DigramList is a BasicDigram.
 *
 * @author Matthias Duerksen
 */
public abstract class DigramList {



    /**
     * a set for the necessary different labels of the nodes in the graph.
     */
    protected final List<String> labels;

    /**
     * Constructor for the DigramList.
     * @param labels the necessary different labels of the nodes in the graph.
     */
    public DigramList(List<String> labels) {

        this.labels = labels;
    }



    /**
     * Gets the digram with the maximal associated digrams.
     * @return digram with the maximal associated digrams.
     */
    public Digram getMaxDigram() {
        //TODO: sinnlos das immer wieder zu berechnen, besser einmal abspeichern
        Digram maxDigram = null;
        int currentSize = 1;
        for (Digram digram : getAllActiveDigrams()) {
            if (digram.getSize() > currentSize) {
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
    public abstract List<Digram> getAllActiveDigrams();




}
