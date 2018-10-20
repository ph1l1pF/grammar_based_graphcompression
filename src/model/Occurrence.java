package model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a basic digram occurrenc, which is an assigned to some digram.
 *
 * An Occurrence is associated to a digram and represents a small structure in the graph.
 * The Occurrence contains two internal node and an edge between this nodes.
 *
 * @author Matthias Duerksen
 * @see Digram
 */
public class Occurrence {

    /**
     * The start node from the occurrence.
     */
    private final GraphNode startnode;

    /**
     * The internal edge from the occurrence.
     */
    private final SimpleEdge edge;

    /**
     * The end node from the occurrence.
     */
    private final GraphNode endnode;

    /**
     * Constructor for the class Occurrence.
     * @param startnode the start node for the occurrence.
     * @param edge the internal edge for the occurrence.
     * @param endnode the end node for the occurrence.
     */
    public Occurrence(GraphNode startnode, SimpleEdge edge, GraphNode endnode) {
        this.startnode = startnode;
        this.edge = edge;
        this.endnode = endnode;
    }

    /**
     * Getter for the internal edge of the occurrence.
     * @return the internal edge.
     */
    public SimpleEdge getEdge() {
        return edge;
    }

    /**
     * Gets both internal nodes of the digram.
     * @return GraphNode Array with two elements for the internal nodes: {startnode,endnode}.
     */
    public GraphNode[] getNodes() {
        GraphNode[] nodes = new GraphNode[2];
        nodes[0] = startnode;
        nodes[1] = endnode;
        return nodes;
    }

    /**
     * Checks if the occurrence has a node which also occurs in the param digram.
     * @param digram the digram for which the check is executed.
     * @return returns true if the occurrence and the digram have a common node, else false.
     */
    public boolean containsSomeNodeOfDigram(Digram digram) {
        for (Map.Entry<Integer, GraphNode> entry : digram.getAllNodes().entrySet()) {
            GraphNode node = entry.getValue();
            if (containsNode(node)) {
                return true;
            }
        }
        return false;
    }

    public GraphNode getStartnode() {
        return startnode;
    }

    public GraphNode getEndnode() {
        return endnode;
    }

    /**
     * Checks if the occurrence has the param node as an internal node.
     * @param node the node for which the check is executed.
     * @return returns true if the node contains in the occurrence, else false.
     */
    private boolean containsNode(GraphNode node) {
        return ((node.getId() == startnode.getId() || node.getId() == endnode.getId()));
    }
}
