package model;

/**
 * This is a class for a Node called GraphNode.
 *
 * A GraphNode is a node of a Graph an has a label.
 *
 * @author Matthias Duerksen
 */
public class GraphNode {

    /**
     * counter for the identifiers of the nodes.
     */
    private static  int idCounter=0;

    /**
     * label of the node.
     */
    private final String label;

    /**
     * id of the node.
     */
    private final int id;

    /**
     * Constructor of GraphNode.
     * @param label the label of the node.
     */
    public GraphNode(String label) {
        this.label = label;
        id = idCounter++;
    }

    /**
     * Constructor of GraphNode.
     *
     * The identifier have to be unique.
     *
     * @param id identifier of the node.
     * @param label label of the node.
     */
    public GraphNode(int id, String label) {
        this.label = label;
        this.id = id;
    }

    /**
     * Getter for the label.
     * @return label of the node.
     */
    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((GraphNode)obj).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Getter for the id.
     * @return Id of the node.
     */
    public int getId() {
        return id;
    }


    public String toString() {
        return "(" + getLabel() + ", " + getId() + ")";
    }
}
