package control;

import model.HyperEdge;
import model.HyperGraph;
import model.GraphNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This is a class to import a HyperGraph out of a text file.
 *
 *The graph in the text file must have a certain syntax. First, the nodes are listed with a new line separated by id':'nodelabel.
 * The id for the node must be unique and a positive integer.
 * The list of nodes and edges is separated by '#' in an extra line and the edges are separated by a new line.
 * An edge is defined by edgelabel':'listOfStartNodes'->'listOfEndnodes.
 * The list for the start  and endnodes are lists of the node id's, separated by ';'.
 *
 *
 * @author Matthias Duerksen
 *@see HyperGraph
 */
class GraphInputControl {


    /**
     * Import of a HyperGraph from the path.
     * @param path path from where the graph should be imported.
     * @return the imported HyperGraph if the import was not successful 'null' will be returned.
     * @throws IOException throws IOException if an error occurs while import the graph form the text file.
     */
    public static HyperGraph getGraph(String path) throws IOException {
        HyperGraph graph = new HyperGraph();
        List<String> lines = Files.readAllLines(Paths.get(path));

        int edgeStartLine = lines.size() - 1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("#")) {
                edgeStartLine = i + 1;
                break;
            }
            if (line.contains(":")) {
                String[] parts = line.split(":");
                GraphNode node = new GraphNode(Integer.parseInt(parts[0]), parts[1]);
                graph.add(node);
            }
        }

        for (int i = edgeStartLine; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(":")) {
                String[] edge = line.split(":");
                String[] parts = edge[1].split("->");
                String[] startnodesIDs = parts[0].split(";");
                String[] endnodesIDs = parts[1].split(";");
                GraphNode[] startnodes = convertToNodes(graph, startnodesIDs);
                GraphNode[] endnode = convertToNodes(graph, endnodesIDs);
                graph.add(new HyperEdge(startnodes, endnode, edge[0]));
            }
        }


        return graph;
    }

    /**
     * Converts the id's from the nodes to the references itself.
     * @param graph graph for which the references are searched.
     * @param labels id's for which the reference are searched.
     * @return the references of the nodes for the id's.
     */
    private static GraphNode[] convertToNodes(HyperGraph graph, String[] labels) {
        GraphNode[] nodes = new GraphNode[labels.length];
        for (int i = 0; i < labels.length; i++) {
            int id = Integer.parseInt(labels[i]);
            nodes[i] = graph.getAllNodes().get(id);
        }

        return nodes;
    }

}
