package model.DigramOccurrence;

import model.Digram.BasicDigram;
import model.Digram.Digram;
import model.Graph.Node;
import model.Graph.SimpleEdge;

import java.util.List;
import java.util.Map;

/**
 * This class represents a basic digram occurrenc, which is an assigned to some digram.
 *
 * An DigramOccurrence is associated to a digram and represents a small structure in the graph.
 * The DigramOccurrence contains two internal node and an edge between this nodes.
 *
 * @author Matthias Duerksen
 * @see BasicDigram
 */
public abstract class DigramOccurrence {

    /**
     * The start node from the occurrence.
     */
    private final Node node1;

    /**
     * The end node from the occurrence.
     */
    private final Node node2;

    /**
     * Constructor for the class DigramOccurrence.
     * @param startnode the start node for the occurrence.
     * @param endnode the end node for the occurrence.
     */
    public DigramOccurrence(Node startnode, Node endnode) {
        this.node1 = startnode;

        this.node2 = endnode;
    }


    public abstract List<SimpleEdge> getEdges();

    /**
     * Gets both internal nodes of the digram.
     * @return Node Array with two elements for the internal nodes: {node1,node2}.
     */
    public Node[] getNodes() {
        Node[] nodes = new Node[2];
        nodes[0] = node1;
        nodes[1] = node2;
        return nodes;
    }

    /**
     * Checks if the occurrence has a node which also occurs in the param digram.
     * @param digram the digram for which the check is executed.
     * @return returns true if the occurrence and the digram have a common node, else false.
     */
    public boolean containsSomeNodeOfDigram(Digram digram) {
        for (Map.Entry<Integer, Node> entry : digram.getAllNodes().entrySet()) {
            Node node = entry.getValue();
            if (containsNode(node)) {
                return true;
            }
        }
        return false;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    /**
     * Checks if the occurrence has the param node as an internal node.
     * @param node the node for which the check is executed.
     * @return returns true if the node contains in the occurrence, else false.
     */
    private boolean containsNode(Node node) {
        return ((node.getId() == node1.getId() || node.getId() == node2.getId()));
    }
}
