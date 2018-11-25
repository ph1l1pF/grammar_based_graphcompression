package control;

import model.Digram.Digram;
import model.DigramList.AdjacencyDigramList;
import model.DigramList.BasicDigramList;
import model.DigramList.DigramList;
import model.Graph.Edge;
import model.Graph.Node;
import model.Graph.HyperGraph;
import model.Graph.SimpleEdge;

import java.util.*;


public class SearchWorker extends Thread {

    private final List<Digram> appliedDigrams;
    private HyperGraph graph;
    public BasicDigramList digramListBasicDigrams;
    public AdjacencyDigramList digramListAdjacencyDigrams;

    private List<String> labels;


    public SearchWorker(List<Digram> appliedDigrams, HyperGraph graph, List<String> labels) {
        this.appliedDigrams = appliedDigrams;
        this.graph = graph;
        this.labels = labels;
    }

    @Override
    public void run() {
         findAllBaseDigrams();
         findAllAdjacencyDigrams();
    }

    /**
     * This method finds all active basic appliedDigrams of a graph and stores them in the DigramList.
     */
    private DigramList findAllBaseDigrams() {

        digramListBasicDigrams = new BasicDigramList(labels);

        // compute random permutation
        List<Edge> edgesCopy = new ArrayList<>(graph.getAllEdges().values());
        List<Edge> edgesInRandomOrder = new ArrayList<>();

        int numEdges = edgesCopy.size();
        while (edgesInRandomOrder.size() < numEdges) {
            int randomIndex = 0;
            if (edgesCopy.size() > 1) {
                randomIndex = new Random().nextInt(edgesCopy.size() - 1);
            }

            edgesInRandomOrder.add(edgesCopy.get(randomIndex));
            edgesCopy.remove(randomIndex);
        }

        for (Edge edge : edgesInRandomOrder) {
            CompressionControl.checkAndAddEdgeToDigram((SimpleEdge) edge, digramListBasicDigrams, appliedDigrams);
        }

        return digramListBasicDigrams;

    }

    private DigramList findAllAdjacencyDigrams(){
        digramListAdjacencyDigrams = new AdjacencyDigramList(labels);

        // compute random permutation
        List<Node> nodesCopy = new ArrayList<>(graph.getAllNodes().values());
        List<Node> nodesInRandomOrder = new ArrayList<>();

        int numNodes = nodesCopy.size();
        while (nodesInRandomOrder.size() < numNodes) {
            int randomIndex = 0;
            if (nodesCopy.size() > 1) {
                randomIndex = new Random().nextInt(nodesCopy.size() - 1);
            }

            nodesInRandomOrder.add(nodesCopy.get(randomIndex));
            nodesCopy.remove(randomIndex);
        }

        for (Node node : nodesInRandomOrder) {
            digramListAdjacencyDigrams.addDigrams(node, appliedDigrams, graph);
        }

        return digramListBasicDigrams;
    }


}
