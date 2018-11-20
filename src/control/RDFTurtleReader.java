package control;

import model.Graph.Edge;
import model.Graph.Node;
import model.Graph.HyperEdge;
import model.Graph.HyperGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RDFTurtleReader {

    public static HyperGraph extractFromTurtleFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        Map<String, Node> mapLabelToNode = new HashMap<>();
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

            Node startNode = mapLabelToNode.get(triple[0]);
            if(startNode==null){
                startNode = new Node(triple[0]);
                mapLabelToNode.put(triple[0], startNode);
                graph.add(startNode);
            }

            Node endNode = mapLabelToNode.get(triple[2]);
            if(endNode==null){
                endNode = new Node(triple[2]);
                mapLabelToNode.put(triple[2], endNode);
                graph.add(endNode);
            }

            boolean edgeAlreadyContained = false;
            for(Edge edge : graph.getAllEdges().values()){
                HyperEdge hyperEdge = (HyperEdge)edge;
                if(hyperEdge.getStartnodes()[0].equals(startNode)&&hyperEdge.getEndnodes()[0].equals(endNode)){
                    edgeAlreadyContained = true;
                }
            }
            if(!edgeAlreadyContained) {
                graph.add(new HyperEdge(startNode, endNode, triple[1]));
            }
        }


        return graph;
    }

    public static void main(String[] a) throws IOException {
        HyperGraph graph = extractFromTurtleFile("/Users/philipfrerk/Downloads/disambiguations_en.ttl");
        System.out.println(graph.getAllNodes().size());
        System.out.println(graph.getAllEdges().size());

        new CompressionControl(graph).graphCompression(true);



    }
}
