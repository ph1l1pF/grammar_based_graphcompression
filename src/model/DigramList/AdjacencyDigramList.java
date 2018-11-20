package model.DigramList;

import model.Digram.AdjacencyDigram;
import model.Digram.Digram;
import model.DigramOccurrence.AdjacencyDigramOccurrence;
import model.DigramOccurrence.DigramOccurrence;
import model.Graph.Edge;
import model.Graph.Node;
import model.Graph.HyperGraph;
import model.Graph.SimpleEdge;

import java.util.*;

public class AdjacencyDigramList extends DigramList {


    private final Map<AdjacencyDigram,AdjacencyDigram> digrams = new HashMap<>();

    /**
     * Constructor for the DigramList.
     *
     * @param labels the necessary different labels of the nodes in the graph.
     */
    public AdjacencyDigramList(List<String> labels) {
        super(labels);
    }

    @Override
    public List<Digram> getAllActiveDigrams() {
        return new ArrayList<>(digrams.keySet());
    }


    public void addDigrams(Object pivot, List<Digram> appliedDigrams, HyperGraph graph) {

        if (!(pivot instanceof Node)) {
            throw new IllegalArgumentException();
        }

        Node midNode = (Node) pivot;

        List<SimpleEdge> incidentEdges = new ArrayList<>();
        for (Edge edge : graph.getAllEdges().values()) {
            SimpleEdge simpleEdge = (SimpleEdge) edge;
            if (simpleEdge.getStartnode().equals(midNode)||simpleEdge.getEndnode().equals(midNode)) {
                incidentEdges.add(simpleEdge);
            }
        }

        for (int i = 0; i < incidentEdges.size(); i++) {
            for (int k = i+1; k < incidentEdges.size(); k++) {

                SimpleEdge edge1 = incidentEdges.get(i);
                SimpleEdge edge2= incidentEdges.get(k);


                Node otherNode1 = null;
                AdjacencyDigram.EdgeDirection dir1 =null;
                if(edge1.getStartnode().equals(midNode)){
                    otherNode1 = edge1.getEndnode();
                    dir1 = AdjacencyDigram.EdgeDirection.INGOING;
                }else{
                    otherNode1 = edge1.getStartnode();
                    dir1 = AdjacencyDigram.EdgeDirection.OUTGOING;

                }
                Node otherNode2 = null;
                AdjacencyDigram.EdgeDirection dir2 =null;

                if(edge2.getStartnode().equals(midNode)){
                    otherNode2 = edge2.getEndnode();
                    dir2 = AdjacencyDigram.EdgeDirection.INGOING;

                }else{
                    otherNode2 = edge2.getStartnode();
                    dir2 = AdjacencyDigram.EdgeDirection.OUTGOING;

                }

                AdjacencyDigram digram = new AdjacencyDigram(otherNode1.getLabel(), otherNode2.getLabel(), edge1.getEquivalenceClass(otherNode1)
                        ,edge2.getEquivalenceClass(otherNode2), dir1,dir2, appliedDigrams);

                AdjacencyDigram adjacencyDigram = digrams.get(digram);
                if(adjacencyDigram==null){
                    adjacencyDigram = digram;
                }
                AdjacencyDigramOccurrence occ = new AdjacencyDigramOccurrence(otherNode1,otherNode2, edge1,edge2,midNode);
                boolean overlappingWithOtherOccurrence = false;
                for(DigramOccurrence occOld : adjacencyDigram.getOccurrences()){
                    if(occOld.getNode1().equals(otherNode1) || occOld.getNode1().equals(otherNode2)||occOld.getNode2().equals(otherNode1) || occOld.getNode2().equals(otherNode2)){
                        overlappingWithOtherOccurrence=true;
                        break;
                    }
                }
                if(!overlappingWithOtherOccurrence){
                    adjacencyDigram.getOccurrences().add(occ);
                }
                digrams.put(adjacencyDigram,adjacencyDigram);
            }
        }


    }


}
