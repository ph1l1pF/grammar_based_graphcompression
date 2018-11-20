package control;

import model.*;
import model.Digram.AdjacencyDigram;
import model.Digram.BasicDigram;
import model.Digram.Digram;
import model.DigramList.AdjacencyDigramList;
import model.DigramList.BasicDigramList;
import model.DigramOccurrence.AdjacencyDigramOccurrence;
import model.DigramOccurrence.DigramOccurrence;
import model.Graph.*;

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
    public BasicDigramList basicDigramList;

    private AdjacencyDigramList adjacencyDigramList;

    private HyperGraph graph;

    /**
     * list of all intermediate compression results.
     */
    private final List<Tuple<HyperGraph, List<Digram>>> allGraphs = new ArrayList<>();

    private SearchWorker[] workers;

    /**
     * Standard Constructor.
     */
    public CompressionControl(HyperGraph graph) {
        this.graph = graph;
        workers = new SearchWorker[Runtime.getRuntime().availableProcessors() * 2];

    }

    /**
     * Execution of the whole grammar based compression method.
     *
     * @return all intermediate compression results  and thus also the compressed graph.
     */
    public List<Tuple<HyperGraph, List<Digram>>> graphCompression(boolean transform) {
        addCurrentGraph(graph, appliedDigrams);

        if (transform) {
            graph = transformGraph(graph);
        }
        addCurrentGraph(graph, appliedDigrams);

        while (true) {
            findAllDigrams();
            Digram digramToReplace;
            if ((basicDigramList.getMaxDigram()==null && adjacencyDigramList.getMaxDigram()!=null)
                    ||adjacencyDigramList.getMaxDigram().getSize() > basicDigramList.getMaxDigram().getSize()) {
                digramToReplace = adjacencyDigramList.getMaxDigram();
            } else {
                digramToReplace = basicDigramList.getMaxDigram();
            }
            if (digramToReplace == null) {
                break;
            }
            replaceAllOccurrences(digramToReplace);
            appliedDigrams.add(digramToReplace);

            addCurrentGraph(graph, appliedDigrams);
        }

        pruning(graph, appliedDigrams);
        addCurrentGraph(graph, appliedDigrams);

        graph = edgeOptimization(graph);
        addCurrentGraph(graph, appliedDigrams);


        return allGraphs;
    }

    private void findAllDigrams() {
        // start all search workers
        List<String> labels = getAllDuplicatedLabelsLabels(graph);
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new SearchWorker(appliedDigrams, graph, labels);
        }

        for (SearchWorker worker : workers) {
            worker.run();
        }

        for (SearchWorker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                System.out.println("error");
            }
        }

        // gather and compare the results of the workers
        List<BasicDigramList> digramLists = new ArrayList<>();
        for (SearchWorker worker : workers) {
            digramLists.add(worker.digramListBasicDigrams);
        }

        BasicDigramList bestDigramList = digramLists.get(0);
        int bestNoOcc = 0;
        for (BasicDigramList digramList : digramLists) {
            BasicDigram maxDigram = (BasicDigram) digramList.getMaxDigram();
            if (maxDigram != null) {
                if (maxDigram.getOccurrences().size() > bestNoOcc) {
                    bestDigramList = digramList;
                    bestNoOcc = maxDigram.getOccurrences().size();
                }
            }
        }
        this.basicDigramList = bestDigramList;

        List<AdjacencyDigramList> adjacencyDigramLists = new ArrayList<>();
        for (SearchWorker worker : workers) {
            adjacencyDigramLists.add(worker.digramListAdjacencyDigrams);
        }

        AdjacencyDigramList bestAdjDigramList = adjacencyDigramLists.get(0);
        bestNoOcc = 0;
        for (AdjacencyDigramList digramList : adjacencyDigramLists) {
            AdjacencyDigram maxDigram = (AdjacencyDigram) digramList.getMaxDigram();
            if (maxDigram != null) {
                if (maxDigram.getOccurrences().size() > bestNoOcc) {
                    bestAdjDigramList = digramList;
                    bestNoOcc = maxDigram.getOccurrences().size();
                }
            }
        }
        this.adjacencyDigramList = bestAdjDigramList;
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
            Node node = new Node(edge.getLabel());
            graph.add(node);
            for (Node startnode : edge.getStartnodes()) {
                graph.add(new SimpleEdge(startnode, 1, node, 1));
            }
            for (Node endnode : edge.getEndnodes()) {
                graph.add(new SimpleEdge(node, 1, endnode, 1));
            }

        }

        return graph;
    }


    /**
     * @param edge
     * @param occ
     * @return
     */
    private Node getNodeInEdgeAndOccurrence(SimpleEdge edge, DigramOccurrence occ) {
        for (Node nodeOcc : occ.getNodes()) {
            if (nodeOcc.equals(edge.getStartnode()) || nodeOcc.equals(edge.getEndnode())) {
                return nodeOcc;
            }
        }
        return null;
    }

    /**
     * For the given edge compute a new equiv class at start or end node.
     *
     * @param tuples         Contains all equiv class mapping inner -> outer
     * @param edge           The relevant edge
     * @param endOrStartNode one of the nodes of the edge
     * @return the new equiv class number
     */
    private int getNewEquivClassForEdge(List<Tuple<Integer, Integer>> tuples, SimpleEdge edge, Node endOrStartNode) {
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
     * @param digram the digram for which the method will be executed.
     */
    public void replaceAllOccurrences(Digram digram) {

        digram.setNonterminal();
        for (DigramOccurrence occ : digram.getOccurrences()) {
            Node newNode = new Node(digram.getNonterminal());
            graph.add(newNode);
            List<Edge> allEdges = new ArrayList<>(graph.getAllEdges().values());
            for (int i = 0; i < allEdges.size(); i++) {
                SimpleEdge oldEdge = (SimpleEdge) allEdges.get(i);
                Node nodeOcc = getNodeInEdgeAndOccurrence(oldEdge, occ);
                if (nodeOcc == null) {
                    // in this case the oldEdge is not incident to occ, so nothing to do
                    continue;
                }
                if (occ.getEdges().contains(oldEdge)) {
                    // in this case, the edge is removed and no new edge is inserted

                    if (occ instanceof AdjacencyDigramOccurrence) {
                        AdjacencyDigramOccurrence adjOcc = (AdjacencyDigramOccurrence) occ;
                        Node nodeMiddle;
                        if (nodeOcc.equals(oldEdge.getStartnode())) {
                            nodeMiddle = oldEdge.getEndnode();
                        } else {
                            nodeMiddle = oldEdge.getStartnode();
                        }

                        int equivClassMidNode = digram.getMapEquivClasses().get("midNode").get(0).y;
                        SimpleEdge newEdge = new SimpleEdge(nodeMiddle, oldEdge.getEquivalenceClass(nodeMiddle), newNode, equivClassMidNode);
                        allEdges.remove(i);
                        allEdges.add(i, newEdge);
                    } else {

                        allEdges.remove(i);
                        i--;
                    }
                    continue;
                }


                // now we have an oldEdge incident to occ
                // we want to construct a newEdge to replace oldEdge
                SimpleEdge newEdge = null;
                String mapKey = nodeOcc.equals(occ.getNode1()) ? "startNode" : "endNode";
                List<Tuple<Integer, Integer>> tuples = digram.getMapEquivClasses().get(mapKey);

                if (nodeOcc.equals(oldEdge.getStartnode())) {
                    int newEquivClass = getNewEquivClassForEdge(tuples, oldEdge, oldEdge.getStartnode());
                    newEdge = new SimpleEdge(newNode, newEquivClass, oldEdge.getEndnode(), oldEdge.getEquivalenceClass(oldEdge.getEndnode()));
                } else if (nodeOcc.equals(oldEdge.getEndnode())) {
                    int newEquivClass = getNewEquivClassForEdge(tuples, oldEdge, oldEdge.getEndnode());
                    newEdge = new SimpleEdge(oldEdge.getStartnode(), oldEdge.getEquivalenceClass(oldEdge.getStartnode()),
                            newNode, newEquivClass);
                }

                graph.getAllNodes().remove(nodeOcc.getId(), nodeOcc);
                allEdges.remove(i);
                allEdges.add(i, newEdge);

            }
            graph.getAllEdges().clear();
            for (Edge edge : allEdges) {
                graph.getAllEdges().put(edge.getId(), edge);
            }

        }
    }


//    /**
//     * This method updates the basicDigramList for the applied digram.
//     * <p>
//     * After applying the digram the basicDigramList must be adjusted. This is done by this method.
//     *
//     * @param graph         the graph for which the method will be executed.
//     * @param appliedDigram the digram for which the method will be executed.
//     */
//    private void updateDigramList(HyperGraph graph, BasicDigram appliedDigram) {
//        for (Digram digram : basicDigramList.getAllActiveDigrams()) {
//            if (!digram.equals(appliedDigram)) digram.deleteDigrams(appliedDigram);
//        }
//
//        basicDigramList.addNewLabel(appliedDigram.getNonterminal());
//        for (Map.Entry<Integer, Edge> entry : graph.getAllEdges().entrySet()) {
//            SimpleEdge edge = (SimpleEdge) entry.getValue();
//            if (edge.containsLabel(appliedDigram.getNonterminal())) {
//                checkAndAddEdgeToDigram(edge, basicDigramList, appliedDigrams);
//            }
//        }
//
//
//    }

    /**
     * This method finds all labels in the graph which are necessary for the compression.
     * <p>
     * A label is called necessary iff the label occurs at least twice in the graph.
     *
     * @param graph the graph for which the method will be executed.
     * @return all necessary different labels.
     */
    private LinkedList<String> getAllDuplicatedLabelsLabels(HyperGraph graph) {
        HashMap<String, Integer> labelCounter = new HashMap<>();

        //Find All different Labels with more then 2 occurences
        //foreach Node in graph
        for (Map.Entry<Integer, Node> entry : graph.getAllNodes().entrySet()) {
            Node node = entry.getValue();
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
    public static void checkAndAddEdgeToDigram(SimpleEdge edge, BasicDigramList digramList, LinkedList<Digram> appliedDigrams) {
        BasicDigram digram = digramList.getDigram(edge, appliedDigrams);
        if (digram != null && !digram.containsNode(edge.getStartnode().getId()) && !digram.containsNode(edge.getEndnode().getId())) {
            digram.addOccurrence(edge);
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
            Tuple<BasicDigram, Integer> occurrences = getNTOccurrencesDigram(nt);
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
        for (Map.Entry<Integer, Node> entry : graph.getAllNodes().entrySet()) {
            Node node = entry.getValue();
            LinkedList<SimpleEdge> incidentEdges = graph.getAllIncidentEdges(node);
            if (node.getLabel().charAt(0) == 'e' && incidentEdges.size() == 2 && checkCorrectDirection(incidentEdges, node) && !usedEdges.contains(incidentEdges.getFirst()) && !usedEdges.contains(incidentEdges.getLast())) {
                usedEdges.add(incidentEdges.getFirst());
                usedEdges.add(incidentEdges.getLast());
                nodeDigrams.add(new Object[]{incidentEdges.getFirst(), node, incidentEdges.getLast()});
            }
        }

        //find all other necessary nodes.
        for (Map.Entry<Integer, Node> entry : graph.getAllNodes().entrySet()) {
            Node node = entry.getValue();
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
            Node node = (Node) nodeDigram[1];
            graph.add(new HyperEdge(new Node[]{incommingEdge.getStartnode()}, new Node[]{outcommingEdge.getEndnode()}, node.getLabel()));

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
    private boolean checkCorrectDirection(LinkedList<SimpleEdge> edges, Node node) {
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
    private Tuple<BasicDigram, Integer> getNTOccurrencesDigram(String nt) {
        BasicDigram currentDigram = null;
        int counter = 0;
        for (Digram digram : basicDigramList.getAllActiveDigrams()) {
            if (digram.getNumOccurrences(nt) > 0) {
                currentDigram = (BasicDigram) digram;
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