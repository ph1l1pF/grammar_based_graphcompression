package control;

import model.GraphNode;
import model.HyperEdge;
import model.HyperGraph;
import model.SimpleEdge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RDFTurtleReader {

    public static HyperGraph extractFromTurtleFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        Map<String, GraphNode> mapLabelToNode = new HashMap<>();
        HyperGraph graph = new HyperGraph();
        for (int i = 0; i < lines.size(); i++)
        {
            String[] triple = lines.get(i).split(" ");

            if (triple.length > 4) {
                System.out.println("Strange line: " + lines.get(i));
                continue;
            }
            if(i > 1000){
                break;
            }

            GraphNode startNode = mapLabelToNode.get(triple[0]);
            if(startNode==null){
                startNode = new GraphNode(triple[0]);
                mapLabelToNode.put(triple[0], startNode);
                graph.add(startNode);
            }

            GraphNode endNode = mapLabelToNode.get(triple[2]);
            if(endNode==null){
                endNode = new GraphNode(triple[2]);
                mapLabelToNode.put(triple[2], endNode);
                graph.add(endNode);
            }

            graph.add(new HyperEdge(startNode, endNode, triple[1]));
        }


        return graph;
    }

    public static void main(String[] a) throws IOException {
        HyperGraph graph = extractFromTurtleFile("/Users/philipfrerk/Downloads/disambiguations_en.ttl");
        System.out.println(graph.getAllNodes().size());
        System.out.println(graph.getAllEdges().size());

        HyperGraph transformedGraph = new CompressionControl().transformGraph(graph);
        System.out.println(transformedGraph.getAllNodes().size());
        System.out.println(transformedGraph.getAllEdges().size());

        new CompressionControl().findAllDigrams(transformedGraph);



    }
}
