package model;

import java.util.LinkedList;

/**
 * This class is a helper class for the mapping function of a digram.
 *
 * Each Digram needs a function which maps form a node and an old equivalenceClass to a new equivalenceClass. This function is implemented by this class.
 *
 * @author Matthias Duerksen
 * @see Digram
 */
class EquivalenceclassMapper {

    /**
     * the mapping function of the class
     */
    private final LinkedList<Tuple<GraphNode, Integer>> function = new LinkedList<>();


    /**
     * Gets the new EquivalenceClass for a node an its EquivalenceClass.
     * @param node the node which the new Equivalence class is to be calculated for.
     * @param oldEquivalenceClass the old Equivalence class which the new Equivalence class is to be calculated for.
     * @return the new Equivalence class for the node and old Equivalence class.
     */
    public int getNewEquivalenceClass(GraphNode node, int oldEquivalenceClass) {
        return functionIndexOf(node, oldEquivalenceClass)+1;
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
        return function.get(newEquivalenceClass-1);
    }

    /**
     * Does all necessary updates on the mapping function for a new occurrence.
     * @param occurrence the new occurrence.
     */
    public void updateFunction(Occurrence occurrence){
        SimpleEdge edge = occurrence.getEdge();
        if (functionIndexOf(edge.getStartnode(), edge.getEquivalenceClass(edge.getStartnode()))==-1){
            function.add(new Tuple<>(edge.getStartnode(), edge.getEquivalenceClass(edge.getStartnode())));
        }

        if (functionIndexOf(edge.getEndnode(), edge.getEquivalenceClass(edge.getEndnode()))==-1){
            function.add(new Tuple<>(edge.getEndnode(), edge.getEquivalenceClass(edge.getEndnode())));
        }
    }

    /**
     * Gets the local index in the mapping function for the node and the equivalenceclass.
     * @param node the node which the index is to be calculated for.
     * @param equivalenceclass the equivalenceclass which the index is to be calculated for.
     * @return the index of the local mapping function which simultaneously corresponds to the new equivalenceClass of the node and the equivalenceClass.
     */
    private int functionIndexOf(GraphNode node, int equivalenceclass) {
        for (int i = 0; i < function.size() - 1; i++) {
            if (function.get(i).x.getId() == node.getId() && function.get(i).y == equivalenceclass) {
                return i;
            }
        }
        return -1;
    }




}

