package control;

import model.*;

import java.util.*;

/**
 * This is a class for the compression of a HyperGraph Object.
 * <p>
 * This class implements a grammar based compression method for graphs. The graph must be an HyperGraph Object. Then the class can execute the compression and produce a compressed HyperGraph with its applied appliedDigrams.
 *
 * @author Matthias Duerksen
 * @see HyperGraph
 */
class CompressionControl {
    /**
     * all applied appliedDigrams.
     */
    private final LinkedList<Digram> appliedDigrams = new LinkedList<>();

    /**
     * the data structure for all active appliedDigrams.
     */
    public DigramList digramlist;

    /**
     * list of all intermediate compression results.
     */
    private final LinkedList<Tuple<HyperGraph, LinkedList<Digram>>> allGraphs = new LinkedList<>();

    /**
     * Standard Constructor.
     */
    public CompressionControl() {
    }

    /**
     * Execution of the whole grammar based compression method.
     *
     * @param untransformedGraph an uncompressed graph.
     * @return all intermediate compression results  and thus also the compressed graph.
     */
    public LinkedList<Tuple<HyperGraph, LinkedList<Digram>>> graphCompression(HyperGraph untransformedGraph) {
        addCurrentGraph(untransformedGraph, appliedDigrams);

        HyperGraph graph = untransformedGraph;//transformGraph(untransformedGraph);
        addCurrentGraph(graph, appliedDigrams);


        findAllDigrams(graph);
        Digram currentDigram = digramlist.getMaxDigram();

        while (currentDigram != null) {
            replaceNextDigrams(graph, currentDigram);
            appliedDigrams.add(currentDigram);
            updateDigramList(graph, currentDigram);
            currentDigram = digramlist.getMaxDigram();

            addCurrentGraph(graph, appliedDigrams);
        }

        pruning(graph, appliedDigrams);
        addCurrentGraph(graph, appliedDigrams);

        graph = edgeOptimization(graph);
        addCurrentGraph(graph, appliedDigrams);


        return allGraphs;
    }


    /**
     * Trasformation of the uncompressed graph to another graph model.
     * <p>
     * The graph is transformed to a graph which has no hyperedge and no edge labels.
     * This is realized by transforming the graph in such a way that all incident elements become adjacent elements.
     *
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
     * This method finds all active basic appliedDigrams of a graph and stores them in the DigramList.
     *
     * @param graph the graph for which the appliedDigrams will be captured.
     */
    public void findAllDigrams(HyperGraph graph) {

        LinkedList<String> labels = getNecessaryLabels(graph);

        digramlist = new DigramList(labels);
        for (Map.Entry<Integer, Edge> entry : graph.getAllEdges().entrySet()) {
            SimpleEdge edge = (SimpleEdge) entry.getValue();
            checkAndAddEdgeToDigram(edge);
        }
        System.out.println("appliedDigrams: " + digramlist.getAllActiveDigrams().size());

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

    /**
     * This method checks if the edge overlaps with a digram and perhaps adds a new digram.
     * <p>
     * This method checks whether the corresponding occurrence in the digram contains some of the incident nodes of the edge.
     * If the digram does not contain some of the node, an occurrence for the edge is added to the corresponding digram.
     *
     * @param edge the edge for which the method will be executed.
     */
    private void checkAndAddEdgeToDigram(SimpleEdge edge) {
        Digram digram = digramlist.getDigram(edge, appliedDigrams);
        if (digram != null && !digram.containsNode(edge.getStartnode().getId()) && !digram.containsNode(edge.getEndnode().getId())) {
            digram.addDigram(edge);
        }
    }

    /**
     *
     * @param edge
     * @param occ
     * @return
     */
    private GraphNode getNodeInEdgeAndOccurrence(SimpleEdge edge, Occurrence occ) {
        for (GraphNode nodeOcc : occ.getNodes()) {
            if (nodeOcc.equals(edge.getStartnode()) || nodeOcc.equals(edge.getEndnode())) {
                return nodeOcc;
            }
        }
        return null;
    }

    /**
     * For the given edge compute a new equiv class at start or end node.
     * @param tuples Contains all equiv class mapping inner -> outer
     * @param edge The relevant edge
     * @param endOrStartNode one of the nodes of the edge
     * @return the new equiv class number
     */
    private int getNewEquivClassForEdge(List<Tuple<Integer, Integer>> tuples, SimpleEdge edge , GraphNode endOrStartNode){
        for (Tuple<Integer, Integer> equivTuple : tuples) {
            if (equivTuple.x == edge.getEquivalenceClass(endOrStartNode)) {
                return equivTuple.y;
            }
        }
        // this never happens
        throw new RuntimeException("unable to compute new equiv class");
    }


    /**
     * This method replaces all associated appliedDigrams of the digram in the graph.
     *
     * @param graph  the graph for which the method will be executed.
     * @param digram the digram for which the method will be executed.
     */
    public void replaceNextDigrams(HyperGraph graph, Digram digram) {

        digram.setNonterminal();
        for (Occurrence occ : digram.getAllOccurrences()) {
            GraphNode newNode = new GraphNode(digram.getNonterminal());
            graph.add(newNode);
            List<Edge> allEdges = new ArrayList<>(graph.getAllEdges().values());
            for (int i = 0; i < allEdges.size(); i++) {
                SimpleEdge oldEdge = (SimpleEdge) allEdges.get(i);
                GraphNode nodeOcc = getNodeInEdgeAndOccurrence(oldEdge, occ);
                if (nodeOcc == null) {
                    // in this case the oldEdge is not incident to occ, so nothing to do
                    continue;
                }
                if (oldEdge.equals(occ.getEdge())) {
                    // in this case, the edge is removed and no new edge is inserted
                    allEdges.remove(i);
                    i--;
                    continue;
                }

                // now we have an oldEdge incident to occ
                // we want to construct a newEdge to replace oldEdge
                SimpleEdge newEdge = null;
                String mapKey = nodeOcc.equals(occ.getStartnode()) ? "startNode" : "endNode";
                List<Tuple<Integer, Integer>> tuples = digram.getMapEquivClasses().get(mapKey);

                if (nodeOcc.equals(oldEdge.getStartnode())) {
                    int newEquivClass = getNewEquivClassForEdge(tuples, oldEdge, oldEdge.getStartnode());
                    newEdge = new SimpleEdge(newNode, newEquivClass, oldEdge.getEndnode(), oldEdge.getEquivalenceClass(oldEdge.getEndnode()));
                } else if (nodeOcc.equals(oldEdge.getEndnode())) {
                    int newEquivClass = getNewEquivClassForEdge(tuples, oldEdge, oldEdge.getEndnode());
                    newEdge = new SimpleEdge(oldEdge.getStartnode(), oldEdge.getEquivalenceClass(oldEdge.getStartnode()),
                            newNode, newEquivClass);
                }

                allEdges.remove(i);
                allEdges.add(i, newEdge);

            }
            graph.getAllEdges().clear();
            for (Edge edge : allEdges) {
                graph.getAllEdges().put(edge.getId(), edge);
            }

        }
    }



    /**
     * This method updates the digramlist for the applied digram.
     * <p>
     * After applying the digram the digramlist must be adjusted. This is done by this method.
     *
     * @param graph         the graph for which the method will be executed.
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
     * For the applied appliedDigrams it is checked whether the appliedDigrams can be represented more efficiently.
     * <p>
     * If a digram d is not contained in the graph and only once in one digram s, the digram d will be inlined in the digram s.
     *
     * @param graph   the graph for which the method will be executed.
     * @param digrams the appliedDigrams for which the method will be executed.
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
     *
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
     *
     * @param edges the edges for which the method will be executed.
     * @param node  the node for which the method will be executed.
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
     * Counts how often the label is contained in the appliedDigrams.
     *
     * @param nt the label for which the method will be executed.
     * @return the number of occurrences in all appliedDigrams.
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
     * add a graph and its applied appliedDigrams to the intermediate results.
     *
     * @param graph   the current graph for the compression interstate.
     * @param digrams the current appliedDigrams for the compression interstate.
     */
    private void addCurrentGraph(HyperGraph graph, LinkedList<Digram> digrams) {
        allGraphs.add(new Tuple<>(graph.clone(), (LinkedList<Digram>) digrams.clone()));
    }

}