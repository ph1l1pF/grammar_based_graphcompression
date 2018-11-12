package control;

import model.*;

import java.util.*;
import java.util.concurrent.Callable;


public class SearchWorker extends Thread {

    private final LinkedList<Digram> appliedDigrams;
    private HyperGraph graph;
    public DigramList digramList;


    public SearchWorker(LinkedList<Digram> appliedDigrams, HyperGraph graph) {
        this.appliedDigrams = appliedDigrams;
        this.graph = graph;
    }

    @Override
    public void run() {
         findAllBaseDigrams();
    }

    /**
     * This method finds all active basic appliedDigrams of a graph and stores them in the DigramList.
     */
    public DigramList findAllBaseDigrams() {

        LinkedList<String> labels = getNecessaryLabels(graph);

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

    /**
     * This method finds all labels in the graph which are necessary for the compression.
     * <p>
     * A label is called necessary iff the label occurs at least twice in the graph.
     *
     * @param graph the graph for which the method will be executed.
     * @return all necessary different labels.
     */
    private LinkedList<String> getNecessaryLabels(HyperGraph graph) {
        HashMap<String, Integer> labelCounter = new HashMap<>();

        //Find All different Labels with more then 2 occurences
        //foreach GraphNode in graph
        for (Map.Entry<Integer, GraphNode> entry : graph.getAllNodes().entrySet()) {
            GraphNode node = entry.getValue();
            if (!labelCounter.containsKey(node.getLabel())) {
                labelCounter.put(node.getLabel(), 0);
            }
            labelCounter.replace(node.getLabel(), labelCounter.get(node.getLabel()) + 1);
        }
        LinkedList<String> labels = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : labelCounter.entrySet()) {
            if (entry.getValue() > 1) {
                labels.add(entry.getKey());
            }
        }
        return labels;
    }
}
