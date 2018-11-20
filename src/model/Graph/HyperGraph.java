package model.Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This is a class for a Graph called HyperGraph.
 *
 *A Graph consists of two sets, the nodes and edges. On an Object of HyperGraph object the graph compression can be executed.
 *
 * @author Matthias Duerksen
 */
public class HyperGraph {

    /**
     * set of nodes.
     */
    private final HashMap<Integer, Node> nodes;

    /**
     * set of edges.
     */
    private final HashMap<Integer, Edge> edges;

    /**
     * Empty constructor of HyperGraph.
     */
    public HyperGraph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * Constructor of HyperGraph.
     * @param nodes the set of nodes.
     * @param edges the set of edges.
     */
    private HyperGraph(HashMap<Integer, Node> nodes, HashMap<Integer, Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    /**
     * Checks whether the graph contains a node with the label 'l'.
     * @param l the label for which we perform the check.
     * @return true if the graph contains a node with label 'l', else false.
     */
    public boolean containsNode(String l) {
        for (Entry<Integer, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            if (node.getLabel().equals(l)) return true;

        }
        return false;
    }

    /**
     * Add a node to the graph.
     * @param node node which is to be added.
     */
    public void add(Node node) {
        nodes.put(node.getId(), node);
    }

    /**
     * Add an edge to the graph.
     * @param edge edge which is to be added.
     */
    public void add(Edge edge) {
        edges.put(edge.getId(), edge);
    }

    /**
     * Add all new nodes to the graph.
     * @param newNodes nodes which are be added.
     */
    public void addAll(Map<Integer, Node> newNodes) {
        nodes.putAll(newNodes);
    }

    /**
     * Delete a node from the graph.
     * @param node node which should be deleted.
     */
    public void delete(Node node) {
        nodes.remove(node.getId());
    }

    /**
     * Deletes an edge from the graph.
     * @param edge edge which should be deleted.
     */
    public void delete(SimpleEdge edge) {
        edges.remove(edge.getId());
    }

    /**
     * Getter for all nodes.
     * @return all nodes of the graph.
     */
    public HashMap<Integer, Node> getAllNodes() {
        return nodes;
    }

    /**
     * Getter for all edges.
     * @return all edges of the graph.
     */
    public HashMap<Integer, Edge> getAllEdges() {
        return edges;
    }

    public String toString() {
        String string = "HyperGraph: Nodesize: " + nodes.size() + ", Edgesize: " + edges.size() + "\n";
        for (Entry<Integer, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            string += "Node " + node.toString() + ": ";
            for (Entry<Integer, Edge> entry2 : edges.entrySet()) {
                Edge edge = entry2.getValue();
                if (edge instanceof SimpleEdge && ((SimpleEdge) edge).getStartnode().getId() == node.getId()) {
                    string += ((SimpleEdge) edge).getEndnode().toString() + ", ";
                }

            }
            string += "\n";
        }
        return string;

    }

    /**
     * Clones the current graph.
     * @return cloned graph of the current graph.
     */
    public HyperGraph clone() {
        return new HyperGraph((HashMap<Integer, Node>) getAllNodes().clone(), (HashMap<Integer, Edge>) getAllEdges().clone());
    }

    /**
     * Getter for all incident edges of a node.
     * @param node the node for which the request is to be executed.
     * @return all incident edges of the node.
     */
    public LinkedList<SimpleEdge> getAllIncidentEdges(Node node) {
        LinkedList<SimpleEdge> edges = new LinkedList<>();
        for (Entry<Integer, Edge> entry : getAllEdges().entrySet()) {
            SimpleEdge edge = (SimpleEdge) entry.getValue();
            if (edge.getStartnode().equals(node) || edge.getEndnode().equals(node)) {
                edges.add(edge);
            }
        }
        return edges;
    }
}
