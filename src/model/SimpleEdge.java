package model;

/**
 * This is a class for SimpleEdge which extends the class Edge.
 *
 * A SimpleEdge object is an edge which has exactly one start node and one end node and has no label.
 *
 * @author Matthias Duerksen
 */
public class SimpleEdge extends Edge {

    /**
     * the Id for the edge.
     */
    private final int id;

    /**
     * the start node of the edge.
     */
    private final GraphNode startnode;

    /**
     * the end node of the edge.
     */
    private final GraphNode endnode;

    /**
     * the equivalence class for the start node.
     */
    private final int eq_start;

    /**
     * the equivalence class for the end node.
     */
    private final int eq_end;


    /**
     * Constructor for SimpleEdge.
     *
     * @param startnode the start node of the edge.
     * @param eq_start the equivalence class from the start node.
     * @param endnode the end node of the edge.
     * @param eq_end the equivalence class from the end node.
     */
    public SimpleEdge(GraphNode startnode, int eq_start, GraphNode endnode, int eq_end) {
        this.startnode = startnode;
        this.eq_start = eq_start;
        this.endnode = endnode;
        this.eq_end = eq_end;
        id = idCounter++;
    }

    /**
     * Constructor for SimpleEdge.
     *
     * @param startnode the start node of the edge.
     * @param endnode the end node of the edge.
     */
    public SimpleEdge(GraphNode startnode,GraphNode endnode) {
        this(startnode,1,endnode,1);
    }

    /**
     * Check if an internal node have the label 'label'.
     * @param label the label for which the check is executed.
     * @return true if an internal node of the edge have the label 'label', else false.
     */
    public boolean containsLabel(String label) {
        return startnode.getLabel().equals(label) || endnode.getLabel().equals(label);
    }

    /**
     * Getter for the equivalence class for the node.
     * @param node the node for which the equivalence class is to be output.
     * @return the equivalence class for node.
     */
    public int getEquivalenceClass(GraphNode node) {
        if (node.equals(startnode)) return eq_start;
        if (node.equals(endnode)) return eq_end;
        return -1;
    }

    /**
     * Getter for the start node.
     * @return the start node of the edge.
     */
    public GraphNode getStartnode() {
        return startnode;
    }

    /**
     * Getter for the end node.
     * @return the end node of the edge.
     */
    public GraphNode getEndnode() {
        return endnode;
    }

    @Override
    public String toString() {
        return startnode + " "+eq_start +"->"+eq_end+" "+endnode;
    }

    /**
     * Getter for the Id of the edge.
     * @return the Id of the edge.
     */
    public int getId() {
        return id;
    }

}
