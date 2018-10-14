package control;

import model.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This is a class for the compression of a HyperGraph Object.
 *
 *This class implements a grammar based compression method for graphs. The graph must be an HyperGraph Object. Then the class can execute the compression and produce a compressed HyperGraph with its applied digrams.
 *
 * @author Matthias Duerksen
 * @see HyperGraph
 */
class CompressionControl {
    /**
     * all applied digrams.
     */
    private final LinkedList<Digram> digrams = new LinkedList<>();

    /**
     * the data structure for all active digrams.
     */
    public DigramList digramlist;

    /**
     * list of all intermediate compression results.
     */
    private final LinkedList<Tuple<HyperGraph, LinkedList<Digram>>> allGraphs = new LinkedList<>();

    /**
     * Standard Constructor.
     */
    public CompressionControl(){}

    /**
     * Execution of the whole grammar based compression method.
     * @param untransformedGraph an uncompressed graph.
     * @return all intermediate compression results  and thus also the compressed graph.
     */
    public LinkedList<Tuple<HyperGraph, LinkedList<Digram>>> graphCompression(HyperGraph untransformedGraph) {
        addCurrentGraph(untransformedGraph, digrams);

        HyperGraph graph = untransformedGraph;//transformGraph(untransformedGraph);
        addCurrentGraph(graph, digrams);


        findAllDigrams(graph);
        Digram currentDigram = digramlist.getMaxDigram();

        while (currentDigram != null) {
            replaceNextDigrams(graph, currentDigram);
            digrams.add(currentDigram);
            updateDigramList(graph, currentDigram);
            currentDigram = digramlist.getMaxDigram();

            addCurrentGraph(graph, digrams);
        }

        pruning(graph, digrams);
        addCurrentGraph(graph, digrams);

        graph = edgeOptimization(graph);
        addCurrentGraph(graph, digrams);


        return allGraphs;
    }


    /**
     * Trasformation of the uncompressed graph to another graph model.
     *
     * The graph is transformed to a graph which has no hyperedge and no edge labels.
     * This is realized by transforming the graph in such a way that all incident elements become adjacent elements.
     * @param untransformedGraph the untransformed graph.
     * @return the transformed graph.
     */
    public HyperGraph transformGraph(HyperGraph untransformedGraph) {
        HyperGraph graph = new HyperGraph();
        graph.addAll(untransformedGraph.getAllNodes());
        HashMap<Integer, Edge> oldEdges = untransformedGraph.getAllEdges();

        //Foreach HyperEdge "edge"  in the untransformed HyperGraph
        for (Map.Entry<Integer, Edge> entry : oldEdges.entrySet()) {
            HyperEdge edge = (HyperEdge) entry.getValue();
            GraphNode node = new GraphNode(edge.getLabel());
            graph.add(node);
            for (GraphNode startnode : edge.getStartnodes()) {
                graph.add(new SimpleEdge(startnode, 1, node, 1));
            }
            for (GraphNode endnode : edge.getEndnodes()) {
                graph.add(new SimpleEdge(node, 1, endnode, 1));
            }

        }

        return graph;
    }

    /**
     * This method finds all active basic digrams of a graph and stores them in the DigramList.
     * @param graph the graph for which the digrams will be captured.
     */
    public void findAllDigrams(HyperGraph graph) {

        LinkedList<String> labels = getNecessaryLabels(graph);

        digramlist = new DigramList(labels);
        for (Map.Entry<Integer, Edge> entry : graph.getAllEdges().entrySet()) {
            SimpleEdge edge = (SimpleEdge) entry.getValue();
            checkAndAddEdgeToDigram(edge);
        }
       System.out.println( "digrams: "+digramlist.getAllActiveDigrams().size());

    }

    /**
     * This method finds all labels in the graph which are necessary for the compression.
     *
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

    /**
     * This method checks if the edge overlaps with a digram and perhaps adds a new digram.
     *
     * This method checks whether the corresponding occurrence in the digram contains some of the incident nodes of the edge.
     * If the digram does not contain some of the node, an occurrence for the edge is added to the corresponding digram.
     * @param edge the edge for which the method will be executed.
     */
    private void checkAndAddEdgeToDigram(SimpleEdge edge) {
        Digram digram = digramlist.getDigram(edge);
        if (digram != null && !digram.containsNode(edge.getStartnode().getId()) && !digram.containsNode(edge.getEndnode().getId())) {
            digram.addDigram(edge);
        }
    }

    /**
     * This method replaces all associated digrams of the digram in the graph.
     * @param graph the graph for which the method will be executed.
     * @param digram the digram for which the method will be executed.
     */
    public void replaceNextDigrams(HyperGraph graph, Digram digram) {
        digram.setNonterminal();
        for (Occurrence occurrence : digram.getAllOccurrences()) {
            GraphNode newNode = new GraphNode(digram.getNonterminal());
            graph.add(newNode);
            graph.delete(occurrence.getEdge());
            for (GraphNode node : occurrence.getNodes()) {
                HashMap<Integer, Edge> copiedEdges = (HashMap<Integer, Edge>) graph.getAllEdges().clone();
                for (Map.Entry<Integer, Edge> entry : copiedEdges.entrySet()) {
                    SimpleEdge edge = (SimpleEdge) entry.getValue();
                    if (edge.getStartnode().equals(node) && edge.getEndnode().equals(node)) {
                        graph.add(new SimpleEdge(newNode, digram.getNewEquivalenceClass(node, edge.getEquivalenceClass(node)), newNode, edge.getEquivalenceClass(node)));
                        graph.delete(edge);
                    } else if (edge.getStartnode().equals(node)) {
                        graph.add(new SimpleEdge(newNode, digram.getNewEquivalenceClass(node, edge.getEquivalenceClass(node)), edge.getEndnode(), edge.getEquivalenceClass(edge.getEndnode())));
                        graph.delete(edge);
                    } else if (edge.getEndnode().equals(node)) {
                        graph.add(new SimpleEdge(edge.getStartnode(), edge.getEquivalenceClass(edge.getStartnode()), newNode, digram.getNewEquivalenceClass(node, edge.getEquivalenceClass(node))));
                        graph.delete(edge);
                    }

                }

                graph.delete(node);
            }
        }
    }

    /**
     * This method updates the digramlist for the applied digram.
     *
     * After applying the digram the digramlist must be adjusted. This is done by this method.
     * @param graph the graph for which the method will be executed.
     * @param appliedDigram the digram for which the method will be executed.
     */
    private void updateDigramList(HyperGraph graph, Digram appliedDigram) {
        for (Digram digram : digramlist.getAllActiveDigrams()) {
            if (!digram.equals(appliedDigram)) digram.deleteDigrams(appliedDigram);
        }

        digramlist.addNewLabel(appliedDigram.getNonterminal());
        for (Map.Entry<Integer, Edge> entry : graph.getAllEdges().entrySet()) {
            SimpleEdge edge = (SimpleEdge) entry.getValue();
            if (edge.containsLabel(appliedDigram.getNonterminal())) {
                checkAndAddEdgeToDigram(edge);
            }
        }


    }

    /**
     * For the applied digrams it is checked whether the digrams can be represented more efficiently.
     *
     * If a digram d is not contained in the graph and only once in one digram s, the digram d will be inlined in the digram s.
     * @param graph the graph for which the method will be executed.
     * @param digrams the digrams for which the method will be executed.
     */
    private void pruning(HyperGraph graph, LinkedList<Digram> digrams) {
        LinkedList<Digram> tmpDigrams = (LinkedList<Digram>) digrams.clone();
        for (Digram digram : tmpDigrams) {
            String nt = digram.getNonterminal();
            Tuple<Digram, Integer> occurrences = getNTOccurrencesDigram(nt);
            if (!graph.containsNode(nt) && occurrences.y == 1) {
                occurrences.x.inlineDigram(digram);
                digrams.remove(digram);
            }
        }
    }

    /**
     * This method optimizes the number of elements by replacing two edges with an incedent node to a new edge with a label.
     * @param graph the graph for which the method will be executed.
     * @return the optimized graph.
     */
    private HyperGraph edgeOptimization(HyperGraph graph) {
        LinkedList<SimpleEdge> usedEdges = new LinkedList<>();
        LinkedList<Object[]> nodeDigrams = new LinkedList<>();

        //find all nodes for which the edgeOptimization can be applied.

        //find first all nodes which have been edges in the untransformed graph. It results in a better efficiency.
        for (Map.Entry<Integer, GraphNode> entry : graph.getAllNodes().entrySet()) {
            GraphNode node = entry.getValue();
            LinkedList<SimpleEdge> incidentEdges = graph.getAllIncidentEdges(node);
            if (node.getLabel().charAt(0) == 'e' && incidentEdges.size() == 2 && checkCorrectDirection(incidentEdges, node) && !usedEdges.contains(incidentEdges.getFirst()) && !usedEdges.contains(incidentEdges.getLast())) {
                usedEdges.add(incidentEdges.getFirst());
                usedEdges.add(incidentEdges.getLast());
                nodeDigrams.add(new Object[]{incidentEdges.getFirst(), node, incidentEdges.getLast()});
            }
        }

        //find all other necessary nodes.
        for (Map.Entry<Integer, GraphNode> entry : graph.getAllNodes().entrySet()) {
            GraphNode node = entry.getValue();
            LinkedList<SimpleEdge> incidentEdges = graph.getAllIncidentEdges(node);
            if (incidentEdges.size() == 2 && checkCorrectDirection(incidentEdges, node) && !usedEdges.contains(incidentEdges.getFirst()) && !usedEdges.contains(incidentEdges.getLast())) {
                usedEdges.add(incidentEdges.getFirst());
                usedEdges.add(incidentEdges.getLast());
                nodeDigrams.add(new Object[]{incidentEdges.getFirst(), node, incidentEdges.getLast()});
            }
        }


        //execute for all node in nodeDigrams the replacement.
        for (Object[] nodeDigram : nodeDigrams) {
            SimpleEdge incommingEdge = (SimpleEdge) nodeDigram[0];
            SimpleEdge outcommingEdge = (SimpleEdge) nodeDigram[2];
            GraphNode node = (GraphNode) nodeDigram[1];
            graph.add(new HyperEdge(new GraphNode[]{incommingEdge.getStartnode()}, new GraphNode[]{outcommingEdge.getEndnode()}, node.getLabel()));

            graph.delete(incommingEdge);
            graph.delete(outcommingEdge);
            graph.delete(node);
        }
        return graph;
    }

    /**
     * Checks if two edges are incident to the node and one is an incoming edge and the other one is an outgoing edge.
     * @param edges the edges for which the method will be executed.
     * @param node the node for which the method will be executed.
     * @return true if the edges are both incident to the node an one is an incoming and the other one is an outgoing edge, else false.
     */
    private boolean checkCorrectDirection(LinkedList<SimpleEdge> edges, GraphNode node) {
        SimpleEdge enteringEdge = null;
        SimpleEdge leavingEdge = null;
        for (SimpleEdge edge : edges) {
            if (edge.getStartnode().equals(node)) {
                leavingEdge = edge;
            } else if (edge.getEndnode().equals(node)) {
                enteringEdge = edge;
            }

        }
        if (leavingEdge != null) {
            edges.remove(leavingEdge);
            edges.addLast(leavingEdge);
        }


        return (enteringEdge != null && leavingEdge != null);

    }

    /**
     * Counts how often the label is contained in the digrams.
     * @param nt the label for which the method will be executed.
     * @return the number of occurrences in all digrams.
     */
    private Tuple<Digram, Integer> getNTOccurrencesDigram(String nt) {
        Digram currentDigram = null;
        int counter = 0;
        for (Digram digram : digramlist.getAllActiveDigrams()) {
            if (digram.getNumOccurrences(nt) > 0) {
                currentDigram = digram;
                counter += digram.getNumOccurrences(nt);

            }
        }

        return new Tuple<>(currentDigram, counter);
    }

    /**
     * add a graph and its applied digrams to the intermediate results.
     * @param graph the current graph for the compression interstate.
     * @param digrams the current digrams for the compression interstate.
     */
    private void addCurrentGraph(HyperGraph graph, LinkedList<Digram> digrams) {
        allGraphs.add(new Tuple<>(graph.clone(), (LinkedList<Digram>) digrams.clone()));
    }

}