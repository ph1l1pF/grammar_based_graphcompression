package model.DigramOccurrence;

import model.Graph.Node;
import model.Graph.SimpleEdge;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyDigramOccurrence extends DigramOccurrence {

    private SimpleEdge edge1,edge2;
    private Node nodeMiddle;

    /**
     * Constructor for the class DigramOccurrence.
     *  @param startnode the start node for the occurrence.
     * @param endnode   the end node for the occurrence.
     * @param edge1
     * @param edge2
     * @param nodeMiddle
     */
    public AdjacencyDigramOccurrence(Node startnode, Node endnode, SimpleEdge edge1, SimpleEdge edge2, Node nodeMiddle) {
        super(startnode, endnode);
        this.edge1 = edge1;
        this.edge2 = edge2;
        this.nodeMiddle = nodeMiddle;
    }

    @Override
    public List<SimpleEdge> getEdges() {
        List<SimpleEdge> list = new ArrayList<>();
        list.add(edge1);
        list.add(edge2);
        return list;
    }
}
