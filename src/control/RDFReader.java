package control;

import model.Graph.Edge;
import model.Graph.HyperEdge;
import model.Graph.HyperGraph;
import model.Graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RDFReader {

    public static HyperGraph readFromRDFFile(String path) {
        Model model = ModelFactory.createDefaultModel();
        Model model1 = model.read(path);
        ExtendedIterator<Triple> tripleExtendedIterator = model1.getGraph().find();

        Map<String, Node> mapUriToNode = new HashMap<>();
        Set<Edge> edges = new HashSet<>();
        int counter = 0;
        while (tripleExtendedIterator.hasNext()) {
            if (counter >= 10000) {
                break;
            }
            Triple triple = tripleExtendedIterator.next();

            String label = triple.getSubject().getURI();
            Node subject;
            if (mapUriToNode.containsKey(label)) {
                subject = mapUriToNode.get(label);
            } else {
                subject = new Node(label);
                mapUriToNode.put(label, subject);
            }

            label = triple.getSubject().getURI();
            Node object;
            if (mapUriToNode.containsKey(label)) {
                object = mapUriToNode.get(label);
            } else {
                object = new Node(label);
                mapUriToNode.put(label, object);
            }

            edges.add(new HyperEdge(subject, object, triple.getPredicate().getURI()));
            counter++;
        }

        HyperGraph graph = new HyperGraph();
        for (Node node : mapUriToNode.values()) {
            graph.add(node);
        }
        for (Edge edge : edges) {
            graph.add(edge);
        }

        System.out.println(graph.getAllNodes().size());
        System.out.println(graph.getAllEdges().size());

        return graph;

    }

    public static void main(String[] args) {
        HyperGraph graph = readFromRDFFile("/Users/philipfrerk/Downloads/person_graph/outputfile_3.9.ttl");
        new CompressionControl(graph).graphCompression(true);
    }
}
