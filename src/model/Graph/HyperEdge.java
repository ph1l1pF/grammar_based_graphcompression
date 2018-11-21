package model.Graph;

/**
 * This is a class for a HyperEdge which extends the class Edge.
 *
 * A HyperEdge is a edge which may have more than one start node/end node and has a label.
 *
 * @author Matthias Duerksen
 */
public class HyperEdge extends Edge {

    /**
     * the Id for the edge.
     */
    private final int id;

    /**
     * the start nodes of the edge.
     */
    private final Node[] startnodes;

    /**
     * the end nodes of the edge.
     */
    private final Node[] endnodes;

    /**
     * the label of the edge.
     */
    private final String label;


    /**
     * Constructor for the HyperEdge.
     * @param startnodes the start nodes of the edge.
     * @param endnodes  the end nodes of the edge.
     * @param label the label of the edge.
     */
    public HyperEdge(Node[] startnodes, Node[] endnodes, String label) {
        this.startnodes = startnodes;
        this.endnodes = endnodes;
        this.label = label;
        id = idCounter++;
    }

    /**
     * Constructor for the HyperEdge.
     * @param startnode the start node of the edge.
     * @param endnode  the end node of the edge.
     * @param label the label of the edge.
     */
    public HyperEdge(Node startnode, Node endnode, String label) {
       this(new Node[]{startnode}, new Node[]{endnode}, label);
    }

    /**
     * Getter for the start nodes.
     * @return start nodes of the edge.
     */
    public Node[] getStartnodes() {
        return startnodes;
    }

    /**
     * Getter for the end nodes.
     * @return end nodes of the edge.
     */
    public Node[] getEndnodes() {
        return endnodes;
    }

    /**
     * Getter for the Id of the edge.
     * @return Id of the edge.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the label.
     * @return label of the edge.
     */
    public String getLabel() {
        return label;
    }
}
