package control;

import model.GraphNode;
import model.HyperEdge;
import model.HyperGraph;
import model.SimpleEdge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RDFTurtleReader {

    public static HyperGraph extractFromTurtleFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        HyperGraph graph = new HyperGraph();
        for (int i = 0; i < lines.size(); i++)
        {
            String[] parts = lines.get(i).split(" ");

            if (parts.length > 4) {
                System.out.println("Strange line: " + lines.get(i));
                continue;
            }
            if(i > 500000){
                break;
            }

            GraphNode startNode = new GraphNode(parts[0]);
            GraphNode endNode = new GraphNode(parts[2]);
            HyperEdge edge = new HyperEdge(startNode, endNode, parts[1]);
            graph.add(new GraphNode(parts[0]));
            graph.add(new GraphNode(parts[2]));
            graph.add(edge);
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
    }
}
