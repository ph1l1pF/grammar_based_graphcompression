package model.Digram;



import model.DigramOccurrence.BasicDigramOccurrence;
import model.DigramOccurrence.DigramOccurrence;
import model.Graph.Node;
import model.Graph.SimpleEdge;

import java.util.*;

/**
 * This is a class for a replacement digram which is called BasicDigram.
 * A BasicDigram is an entry in the DigramList and the digram specifies how a replacement can be performed on the graph.
 * To the digram matching occurrence are stored in the digram.
 *
 * @author Matthias Duerksen
 */
public class BasicDigram extends Digram{
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
     * Constructor for the BasicDigram.
     * @param startNodeLabel the label for the start node of the digram.
     * @param endNodeLabel the label for the end node of the digram.
     * @param equivStartNode
     * @param equivEndNode
     */
    public BasicDigram(String startNodeLabel, String endNodeLabel, int equivStartNode, int equivEndNode, List<Digram> appliedDigrams) {
        this.startNodeLabel = startNodeLabel;
        this.endNodeLabel = endNodeLabel;
        this.equivStartNode = equivStartNode;
        this.equivEndNode = equivEndNode;

        // establish the mapping of equiv functions
        putTuplesForNodeWithLabel(appliedDigrams, startNodeLabel, true);
        putTuplesForNodeWithLabel(appliedDigrams, endNodeLabel, false);
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
     * Adds a new occurrence for the Edge 'edge' to the associated occurrences.
     * @param edge the edge for the new occurrence.
     */
    public void addOccurrence(SimpleEdge edge) {
        DigramOccurrence occ = new BasicDigramOccurrence(edge.getStartnode(), edge.getEndnode() ,edge);
        occurrences.add(occ);
        for (Node node : occ.getNodes()) {
            allNodes.put(node.getId(), node);
        }


    }

    /**
     * Deletes all associated occurrences of the BasicDigram 'digram' in the associated occurrences.
     * @param digram the digram for which occurrences the deletion is executed.
     */
    public void deleteDigrams(BasicDigram digram) {
        LinkedList<DigramOccurrence> copiedDigrams = new LinkedList<>(getOccurrences());
        for (DigramOccurrence occurrence : copiedDigrams) {
            if (occurrence.containsSomeNodeOfDigram(digram)) {
                occurrences.remove(occurrence);
            }
        }
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
    @Override
    public String innerToString() {
        if (!hasInlined) {

            return startNodeLabel + " " + equivStartNode + " -> " + equivEndNode + " " + endNodeLabel;
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
