package model;



import java.util.HashMap;
import java.util.LinkedList;

/**
 * This is a class for a replacement digram which is called Digram.
 * A Digram is an entry in the DigramList and the digram specifies how a replacement can be performed on the graph.
 * To the digram matching occurrence are stored in the digram.
 *
 * @author Matthias Duerksen
 */
public class Digram {

    /**
     * counter for the identifier of a digram.
     */
    private static int digramCounter = 0;
    /**
     * the labels for the start node.
     */
    private final String startNodeLabel;
    /**
     * the labels for the end node.
     */
    private final String endNodeLabel;

    private final int equivStartNode;

    private final int equivEndNode;

    /**
     * Set of associated occurrence for the digram.
     */
    private final LinkedList<Occurrence> occurrences = new LinkedList<>();
    /**
     * the set of inlined digrams.
     */
    private final LinkedList<Digram> inlinedDigrams = new LinkedList<>();
    /**
     * set of all nodes from the associated occurrences.
     */
    private final HashMap<Integer, GraphNode> allNodes = new HashMap<>();
    /**
     * the mapping function for the digram.
     */
    private final EquivalenceclassMapper equivalenceFunction = new EquivalenceclassMapper();
    /**
     * the non terminal from the digram.
     */
    private String nonterminal;
    /**
     * indicates whether the digram contain inlinded occurrences.
     */
    private boolean hasInlined = false;
    /**
     * Indicates whether the digram is applied.
     */
    private boolean beenApplied = false;

    /**
     * Constructor for the Digram.
     * @param startNodeLabel the label for the start node of the digram.
     * @param endNodeLabel the label for the end node of the digram.
     * @param equivStartNode
     * @param equivEndNode
     */
    public Digram(String startNodeLabel, String endNodeLabel, int equivStartNode, int equivEndNode) {
        this.startNodeLabel = startNodeLabel;
        this.endNodeLabel = endNodeLabel;
        this.equivStartNode = equivStartNode;
        this.equivEndNode = equivEndNode;
    }

    /**
     * Gets the new EquivalenceClass for a node and his old EquivalenceClass.
     * @param node the node which the new Equivalenceclass is to be calculated for.
     * @param oldEquivalenceClass the old Equivalenceclass which the new Equivalenceclass is to be calculated for.
     * @return the new Equivalenceclass for the node and old EquivalenceClass.
     */
    public int getNewEquivalenceClass(GraphNode node, int oldEquivalenceClass) {
        return equivalenceFunction.getNewEquivalenceClass(node, oldEquivalenceClass);
    }

    /**
     * Gets the old EquivalenceClass and the node for an EquivalenceClass.
     *
     * Only required for decompression.
     *
     * @param newEquivalenceClass the EquivalenceClass which the result is to be calculated for.
     * @return the old EquivalenceClass and the node as a Tuple for the newEquivalenceClass.
     */
    public Tuple<GraphNode, Integer> getOldEquivalenceClass(int newEquivalenceClass) {
        return equivalenceFunction.getOldEquivalenceClass(newEquivalenceClass);
    }

    /**
     * Indicates whether there is a node in the associated occurrences with the id 'nodeid'.
     * @param nodeId the ID for the node we are looking for.
     * @return true if the node with nodeId contains in the associated occurrences, else false.
     */
    public boolean containsNode(int nodeId) {
        return allNodes.containsKey(nodeId);
    }

    /**
     * Getter for all associated occurrences.
     * @return all associated occurrences of the digram.
     */
    public HashMap<Integer, GraphNode> getAllNodes() {
        return allNodes;
    }

    /**
     * Adds a new occurrence for the Edge 'edge' to the associated occurrences.
     * @param edge the edge for the new occurrence.
     */
    public void addDigram(SimpleEdge edge) {
        Occurrence digram = new Occurrence(edge.getStartnode(), edge, edge.getEndnode());
        occurrences.add(digram);
        equivalenceFunction.updateFunction(digram);
        for (GraphNode node : digram.getNodes()) {
            allNodes.put(node.getId(), node);
        }


    }

    /**
     * Deletes all associated occurrences of the Digram 'digram' in the associated occurrences.
     * @param digram the digram for which occurrences the deletion is executed.
     */
    public void deleteDigrams(Digram digram) {
        LinkedList<Occurrence> copiedDigrams = (LinkedList<Occurrence>) occurrences.clone();
        for (Occurrence occurrence : copiedDigrams) {
            if (occurrence.containsSomeNodeOfDigram(digram)) {
                occurrences.remove(occurrence);
            }
        }
    }

    /**
     * Getter for the number of internal Elements in this and all inlined digrams.
     * @return the number of internal elements in this and all inlined digrams.
     */
    public Tuple<Integer, Integer> getNumInternalElements() {
        int numInternalNodes = 2;
        int numInternalEdges = 1;
        for (Digram digram : inlinedDigrams) {
            Tuple<Integer, Integer> tuple = digram.getNumInternalElements();
            numInternalNodes += tuple.x - 1;
            numInternalEdges += tuple.y;
        }
        return new Tuple<>(numInternalNodes, numInternalEdges);
    }

    /**
     * Getter for the Non terminal of the digram.
     * @return the Non terminal of the digram.
     */
    public String getNonterminal() {
        return nonterminal;
    }


    /**
     * Setter for the Non terminal of the digram.
     */
    public void setNonterminal() {
        nonterminal = "A_" + digramCounter++;

    }

    /**
     * Getter for all associated occurrences.
     * @return all associated occurrences of the digram.
     */
    public LinkedList<Occurrence> getAllOccurrences() {
        return occurrences;
    }

    /**
     * Counts how often the label occurs in this digram and all inlined digrams.
     * @param label the label for which the count is executed.
     * @return the number of occurrences of this label in the digrams.
     */
    public int getNumOccurrences(String label) {
        int counter = 0;
        if (startNodeLabel.equals(label)) counter++;
        if (endNodeLabel.equals(label)) counter++;

        for (Digram digram : inlinedDigrams) {
            counter += digram.getNumOccurrences(label);
        }
        return counter;

    }

    /**
     * Getter for start node label.
     * @return the start node label of the digram.
     */
    public String getStartNodelabel() {
        return startNodeLabel;
    }

    /**
     * Getter for end node label.
     * @return the end node label of the digram.
     */
    public String getEndNodelabel() {
        return endNodeLabel;
    }

    /**
     * Gets the number of associated occurrences.
     * @return the number of associate occurrences of the digram.
     */
    public int getSize() {
        return occurrences.size();
    }


    /**
     * Getter that indicates that digram was applied.
     * @return true if the digram was applied, else false.
     */
    public boolean hasBeenAplied() {
        return beenApplied;
    }



    /**
     * Sets the digram to applied.
     */
    public void setBeenApplied() {
        beenApplied = true;
    }

    /**
     * Sets a digram to the inlined digrams.
     * @param digram the digram which should be inlined.
     */
    public void inlineDigram(Digram digram) {
        hasInlined = true;
        inlinedDigrams.add(digram);

    }

    public String toString() {
        String string = nonterminal + ": ";
        string += innerToString();
        return string;
    }

    /**
     * toString for the inner part of the digram.
     * @return a String for the inner part of the digram.
     */
    private String innerToString() {
        if (!hasInlined) {

            return startNodeLabel + "->" + endNodeLabel;// + " Size: " + occurrences.size();
        } else {
            String text = "";
            boolean added = false;
            for (Digram digram : inlinedDigrams) {
                if (digram.getNonterminal().equals(startNodeLabel)) {
                    text += "(" + digram.innerToString() + ")->";
                    added = true;
                }
            }
            if (!added) {
                text += startNodeLabel + "->";
            }

            added = false;
            for (Digram digram : inlinedDigrams) {
                if (digram.getNonterminal().equals(endNodeLabel)) {
                    text += "(" + digram.innerToString() + ")";
                    added = true;
                }
            }
            if (!added) {
                text += endNodeLabel;
            }
            return text;
        }
    }

    /**
     * toString method for the inner part of the digram without possible inlined digrams.
     * @return a String for the inner part of the digram.
     */
    public String toStringUnpruned() {
        String string = nonterminal + ": ";
        string += startNodeLabel + "->" + endNodeLabel;
        return string;
    }
}
