package model.DigramOccurrence;

import model.Graph.Node;
import model.Graph.SimpleEdge;

import java.util.ArrayList;
import java.util.List;

public class BasicDigramOccurrence extends DigramOccurrence{
    /**
     * The internal edge from the occurrence.
     */
    private final SimpleEdge edge;



    /**
     * Constructor for the class DigramOccurrence.
     *
     * @param startnode the start node for the occurrence.
     * @param endnode   the end node for the occurrence.
     */
    public BasicDigramOccurrence(Node startnode, Node endnode, SimpleEdge edge) {
        super(startnode, endnode);
        this.edge = edge;
    }

    /**
     * Getter for the internal edge of the occurrence.
     * @return the internal edge.
     */
    public SimpleEdge getEdge() {
        return edge;
    }

    @Override
    public List<SimpleEdge> getEdges() {
        List<SimpleEdge> list = new ArrayList<>();
        list.add(edge);
        return list;
    }
}
