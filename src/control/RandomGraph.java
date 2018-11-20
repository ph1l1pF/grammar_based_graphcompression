package control;

import java.util.Random;

import model.Graph.Node;
import model.Graph.HyperEdge;
import model.Graph.HyperGraph;

/**
 * This is a class to create a random HyperGraph.
 *
 *
 * @author Matthias Duerksen
 *@see HyperGraph
 */
class RandomGraph {


    /**
     * Create a random HyperGraph for a set of node label and a set edge labels.
     * @param numNodes number of nodes for the graph.
     * @param numEdges number of edges for the graph.
     * @param numNodeLabels number of possible different node labels for the graph.
     * @param numEdgeLabels number of possible different node labels for the graph.
     * @return a random constructed HyperGraph.
     */
    public static HyperGraph constructRandomGraph(int numNodes, int numEdges, int numNodeLabels, int numEdgeLabels) {
        if (numNodeLabels==0)numNodeLabels=1;
        if (numEdgeLabels==0)numEdgeLabels=1;

        HyperGraph graph = new HyperGraph();
        createNodes(graph, numNodes, numNodeLabels);
        createEdges(graph, numEdges, numEdgeLabels);

        return graph;
    }

    /**
     * Add a set of nodes to the graph.
     * @param graph the graph for which the method will be executed.
     * @param numNodes number of how many nodes are generated.
     * @param numNodeLabels number of how many possible different node labels are generated.
     */
    private static void createNodes(HyperGraph graph, int numNodes, int numNodeLabels) {
        String[] nodeLabels = createNodeLabels(numNodeLabels);
        for (int i = 0; i < numNodes; i++) {
            graph.add(new Node(getRandomLabel(nodeLabels)));
        }

    }

    /**
     * Create a set of node labels.
     * @param numNodeLabels number of how many node labels are generated.
     * @return the set of node labels.
     */
    private static String[] createNodeLabels(int numNodeLabels) {
        String[] nodeLabels = new String[numNodeLabels];
        for (int i = 0; i < numNodeLabels; i++) {
            nodeLabels[i] = "n" + i;
        }
        return nodeLabels;
    }


    /**
     * Add a set of edges to the graph.
     * @param graph the graph for which the method will be executed.
     * @param numEdges number of how many edges are generated.
     * @param numEdgeLabels number of how many possible different edge labels are generated.
     */
    private static void createEdges(HyperGraph graph, int numEdges, int numEdgeLabels) {
        String[] edgeLabels = createEdgeLabels(numEdgeLabels);
        for (int i = 0; i < numEdges; i++) {
            Random generator = new Random();

            Object[] values = graph.getAllNodes().values().toArray();
            Node startnode = (Node) values[generator.nextInt(values.length)];
            Node endnode = (Node) values[generator.nextInt(values.length)];


            graph.add(new HyperEdge(new Node[]{startnode}, new Node[]{endnode}, getRandomLabel(edgeLabels)));
        }
    }

    /**
     * Create a set of edge labels.
     * @param numEdgeLabels number of how many edge labels are generated.
     * @return the set of edge labels.
     */
    private static String[] createEdgeLabels(int numEdgeLabels) {
        String[] edgeLabels = new String[numEdgeLabels];

        for (int i = 0; i < numEdgeLabels; i++) {
            edgeLabels[i] = "e" + i;
        }
        return edgeLabels;
    }

    /**
     * Getter for a random label of the parameter set.
     * @param labels set of labels.
     * @return a random label of the set.
     */
    private static String getRandomLabel(String[] labels) {
        int idx = new Random().nextInt(labels.length);
        return labels[idx];

    }


    /**
     * Compress a random Graph.
     * @param numNodes the number of nodes from the graph.
     * @param numEdges the number of edges from the graph.
     * @param numNodeLabels the number of different node labels from the graph.
     * @param numEdgeLabels the number of different edge labels from the graph.
     * @see CompressionControl
     */
    private static void compressRandomGraph(int numNodes, int numEdges, int numNodeLabels, int numEdgeLabels) {
//        HyperGraph graph= RandomGraph.constructRandomGraph(numNodes,numEdges,numNodeLabels,numEdgeLabels);
//        CompressionControl compressionControl = new CompressionControl();
//
//        List<Tuple<HyperGraph, List<BasicDigram>>> tuples = compressionControl.graphCompression(graph,true);
//
//
//        HyperGraph compressedGraph= tuples.getLast().x;
//        LinkedList<BasicDigram> usedDigrams= tuples.getLast().y;
//        System.out.println("Compression finished for a graph with the parameters: "+numNodes+" nodes, "+numEdges+" edges, "+numNodeLabels+" different node labels, "+ numEdgeLabels+ " different edge labels\n");
//        System.out.println("Compressed Graph: "+compressedGraph.getAllNodes().size()+" nodes, "+compressedGraph.getAllEdges().size()+" edges");
//
//        int digramsSize = 0;
//        for (BasicDigram digram : usedDigrams) {
//
//            Tuple<Integer, Integer> numInternalElements = digram.getNumInternalElements();
//            digramsSize += numInternalElements.x+numInternalElements.y;
//
//        }
//
//        System.out.println(usedDigrams.size()+" digrams ("+ tuples.get(tuples.size()-3).y.size()+ " unpruned digrams) applied with a size of "+ digramsSize);
//        System.out.println("Graph compressed from a size of " + (numNodes+numEdges)+" to a size of " + (compressedGraph.getAllNodes().size()+compressedGraph.getAllEdges().size()+digramsSize));
    }


    /**
     * Launches the class.
     * @param args arguments for the main method, not used here.
     */
    public static void main(String[] args) {
        int numberOfNodes=500;
        int numberOfEdges=500;
        int numberOfDifferentNodeLabels=2;
        int numberOfDifferentEdgeLabels=4;

        compressRandomGraph(numberOfNodes,numberOfEdges,numberOfDifferentNodeLabels,numberOfDifferentEdgeLabels);



    }

}
