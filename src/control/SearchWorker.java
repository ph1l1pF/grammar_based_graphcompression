package control;

import model.*;

import java.util.*;


public class SearchWorker extends Thread {

    private final LinkedList<Digram> appliedDigrams;
    private HyperGraph graph;
    public DigramList digramList;
    private List<String> labels;


    public SearchWorker(LinkedList<Digram> appliedDigrams, HyperGraph graph, List<String> labels) {
        this.appliedDigrams = appliedDigrams;
        this.graph = graph;
        this.labels = labels;
    }

    @Override
    public void run() {
         findAllBaseDigrams();
    }

    /**
     * This method finds all active basic appliedDigrams of a graph and stores them in the DigramList.
     */
    public DigramList findAllBaseDigrams() {

        digramList = new DigramList(labels);

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
            CompressionControl.checkAndAddEdgeToDigram((SimpleEdge) edge, digramList, appliedDigrams);
        }

        return digramList;

    }


}
