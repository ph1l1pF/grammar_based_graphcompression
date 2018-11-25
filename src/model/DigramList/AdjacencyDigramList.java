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


    public void addDigrams(Node midNode, List<Digram> appliedDigrams, HyperGraph graph) {
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


                Node otherNode1;
                AdjacencyDigram.EdgeDirection dir1;
                if(edge1.getStartnode().equals(midNode)){
                    otherNode1 = edge1.getEndnode();
                    dir1 = AdjacencyDigram.EdgeDirection.INGOING;
                }else{
                    otherNode1 = edge1.getStartnode();
                    dir1 = AdjacencyDigram.EdgeDirection.OUTGOING;

                }
                Node otherNode2;
                AdjacencyDigram.EdgeDirection dir2;

                if(edge2.getStartnode().equals(midNode)){
                    otherNode2 = edge2.getEndnode();
                    dir2 = AdjacencyDigram.EdgeDirection.INGOING;

                }else{
                    otherNode2 = edge2.getStartnode();
                    dir2 = AdjacencyDigram.EdgeDirection.OUTGOING;

                }

                if (!labels.contains(otherNode1.getLabel()) || !labels.contains(otherNode2.getLabel())) {
                    continue;
                }
                AdjacencyDigram foundDigram = new AdjacencyDigram(otherNode1.getLabel(), otherNode2.getLabel(), edge1.getEquivalenceClass(otherNode1)
                        ,edge2.getEquivalenceClass(otherNode2), dir1,dir2, appliedDigrams);

                AdjacencyDigram digram = digrams.get(foundDigram);
                if (digram == null) {
                    digram = foundDigram;
                }

                // TODO: here the correct mapping from node in occurrence to node in digram must be done. Not sure if this is correct
                AdjacencyDigramOccurrence occ;
                if (otherNode1.getLabel().equals(digram.getLabel1()) && edge1.getEquivalenceClass(otherNode1) == digram.getEquivClass1()) {
                    occ = new AdjacencyDigramOccurrence(otherNode1, otherNode2, edge1, edge2, midNode);
                } else {
                    occ = new AdjacencyDigramOccurrence(otherNode2, otherNode1, edge2, edge1, midNode);
                }


                boolean overlappingWithOtherOccurrence = false;
                for (DigramOccurrence occOld : digram.getOccurrences()) {
                    if(occOld.getNode1().equals(otherNode1) || occOld.getNode1().equals(otherNode2)||occOld.getNode2().equals(otherNode1) || occOld.getNode2().equals(otherNode2)){
                        overlappingWithOtherOccurrence=true;
                        break;
                    }
                }
                if(!overlappingWithOtherOccurrence){
                    digram.getOccurrences().add(occ);
                }
                digrams.put(digram, digram);
                if (getMaxDigram() == null || getMaxDigram().getSize() < digram.getSize()) {
                    maxDigram = digram;
                }
            }
        }


    }


}
