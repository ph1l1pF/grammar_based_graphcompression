package model;

import java.util.*;

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
    private final Map<Tuple<String,Integer>, HashMap<Tuple<String,Integer>, Digram>> digramList = new HashMap<>();

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
     * @return the digram for the two labels.
     */
    public Digram getDigram(SimpleEdge edge) {
        String label1 = edge.getStartnode().getLabel();
        String label2 = edge.getEndnode().getLabel();

        if (!labels.contains(label1) || !labels.contains(label2)) {
            return null;
        }
        int equiv1 = edge.getEquivalenceClass(edge.getStartnode());
        int equiv2 = edge.getEquivalenceClass(edge.getEndnode());

        Tuple<String, Integer> tuple1 = new Tuple<>(label1, equiv1);
        Tuple<String, Integer> tuple2 = new Tuple<>(label2, equiv2);

        if (!digramList.containsKey(tuple1)) {
            digramList.put(tuple1, new HashMap<>());
        }
        if (!digramList.get(tuple1).containsKey(tuple2)) {

            digramList.get(tuple1).put(tuple2, new Digram(label1, label2, equiv1, equiv2));
        }
        return digramList.get(tuple1).get(tuple2);
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
            if (digram.getSize() >= currentSize && !digram.hasBeenAplied()) {
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
    public List<Digram> getAllActiveDigrams() {
//        List<Digram> digrams = new ArrayList<>();
//        for (String label1 : labels) {
//            for (String label2 : labels) {
//                if (containsDigramFor(label1, label2) && getDigram(label1, label2).getSize() > 1) {
//                    digrams.add(getDigram(label1, label2));
//                }
//            }
//        }
        List<Digram> allDigrams = new ArrayList<>();
        for(HashMap<Tuple<String,Integer>, Digram> value : digramList.values()){
            for(Digram digram : value.values()){
                allDigrams.add(digram);
            }
        }
        return allDigrams;
    }


}
