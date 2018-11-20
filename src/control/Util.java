package control;

import model.Graph.Node;
import model.Graph.HyperEdge;
import model.Graph.HyperGraph;
import model.Graph.SimpleEdge;

/**
 * This class is a class for standard tasks.
 * @author Matthias Duerksen
 */
class Util {

    public static HyperGraph createAdjacencyGraphFromThesis(){
        HyperGraph graph = new HyperGraph();

        Node[] nodes = new Node[8];
        nodes[0]= new Node("u");
        nodes[1]= new Node("v1");
        nodes[2]= new Node("w");
        nodes[3]= new Node("u");
        nodes[4]= new Node("w");
        nodes[5]= new Node("v2");
        nodes[6]= new Node("w");
        nodes[7]= new Node("u");



        SimpleEdge[] edges = new SimpleEdge[7];
        edges[0] = new SimpleEdge(nodes[0], nodes[1]);
        edges[1] = new SimpleEdge(nodes[1], nodes[2]);
        edges[2] = new SimpleEdge(nodes[3], nodes[1]);
        edges[3] = new SimpleEdge(nodes[3], nodes[5]);
        edges[4] = new SimpleEdge(nodes[5], nodes[4]);
        edges[5] = new SimpleEdge(nodes[5], nodes[6]);
        edges[6] = new SimpleEdge(nodes[7], nodes[5]);

        for (Node node : nodes) {
            graph.add(node);
        }
        for (SimpleEdge edge : edges) {
            graph.add(edge);
        }
        CompressionControl control = new CompressionControl(graph);
        control.graphCompression(false);

        return graph;
    }

    public static HyperGraph createOtherGraphFromThesis(){
        HyperGraph graph = new HyperGraph();

        Node[] nodes = new Node[4];
        nodes[0]= new Node("n2");
        nodes[1]= new Node("n1");
        nodes[2]= new Node("n2");
        nodes[3]= new Node("n1");

        SimpleEdge[] edges = new SimpleEdge[3];
        edges[0] = new SimpleEdge(nodes[1], nodes[0]);
        edges[1] = new SimpleEdge(nodes[1], nodes[2]);
        edges[2] = new SimpleEdge(nodes[3], nodes[2]);

        for (Node node : nodes) {
            graph.add(node);
        }
        for (SimpleEdge edge : edges) {
            graph.add(edge);
        }
        CompressionControl control = new CompressionControl(graph);
        control.graphCompression(false);

        return graph;
    }

    public static HyperGraph createGraphFromThesis(){
        HyperGraph graph = new HyperGraph();

        Node[] nodes = new Node[7];
        nodes[0]= new Node("x");
        nodes[1]= new Node("x");
        nodes[2]= new Node("z");
        nodes[3]= new Node("y");
        nodes[4]= new Node("y");
        nodes[5]= new Node("z");
        nodes[6]= new Node("w");

        SimpleEdge[] edges = new SimpleEdge[7];
        edges[0] = new SimpleEdge(nodes[0], nodes[1]);
        edges[1] = new SimpleEdge(nodes[1], nodes[2]);
        edges[2] = new SimpleEdge(nodes[2], nodes[3]);
        edges[3] = new SimpleEdge(nodes[3], nodes[4]);
        edges[4] = new SimpleEdge(nodes[5], nodes[4]);
        edges[5] = new SimpleEdge(nodes[0], nodes[5]);
        edges[6] = new SimpleEdge(nodes[5], nodes[6]);

        for (Node node : nodes) {
            graph.add(node);
        }
        for (SimpleEdge edge : edges) {
            graph.add(edge);
        }
        CompressionControl control = new CompressionControl(graph);
        control.graphCompression(false);

        return graph;
    }

    public static void main(String[] a){
        createAdjacencyGraphFromThesis();
    }

    /**
     * Create a control HyperGraph.
     * @return a HyperGraph.
     */
    public static HyperGraph createSampleGraph() {
        HyperGraph graph = new HyperGraph();

        Node n6 = new Node("n6");
        graph.add(n6);

        //Left Side
        Node n1 = new Node("n1");
        graph.add(n1);
        Node n2 = new Node("n2");
        graph.add(n2);
        Node n3 = new Node("n3");
        graph.add(n3);
        Node n4 = new Node("n4");
        graph.add(n4);
        Node n5 = new Node("n5");
        graph.add(n5);


        HyperEdge e1 = new HyperEdge(new Node[]{n2}, new Node[]{n1}, "e1");
        graph.add(e1);
        HyperEdge e2 = new HyperEdge(new Node[]{n2}, new Node[]{n3}, "e2");
        graph.add(e2);
        HyperEdge e3 = new HyperEdge(new Node[]{n4}, new Node[]{n3}, "e3");
        graph.add(e3);
        HyperEdge e5 = new HyperEdge(new Node[]{n1}, new Node[]{n5}, "e5");
        graph.add(e5);
        HyperEdge e7 = new HyperEdge(new Node[]{n5}, new Node[]{n6}, "e7");
        graph.add(e7);
        HyperEdge e8 = new HyperEdge(new Node[]{n4}, new Node[]{n6}, "e3");
        graph.add(e8);

        for (int i = 0; i < 4; i++) {
            Node n7 = new Node("n7");
            graph.add(n7);
            Node n8 = new Node("n8");
            graph.add(n8);

            HyperEdge e10 = new HyperEdge(new Node[]{n4}, new Node[]{n7}, "e10");
            graph.add(e10);
            HyperEdge e11 = new HyperEdge(new Node[]{n7}, new Node[]{n8}, "e11");
            graph.add(e11);
            HyperEdge e12 = new HyperEdge(new Node[]{n8}, new Node[]{n8}, "e12");
            graph.add(e12);
        }


        //Right Side
        n1 = new Node("n1");
        graph.add(n1);
        n2 = new Node("n2");
        graph.add(n2);
        n3 = new Node("n3");
        graph.add(n3);
        n4 = new Node("n4");
        graph.add(n4);
        n5 = new Node("n5");
        graph.add(n5);


        e1 = new HyperEdge(new Node[]{n2}, new Node[]{n1}, "e1");
        graph.add(e1);

        e3 = new HyperEdge(new Node[]{n4}, new Node[]{n3}, "e3");
        graph.add(e3);
        HyperEdge e4 = new HyperEdge(new Node[]{n2}, new Node[]{n3}, "e4");
        graph.add(e4);
        HyperEdge e6 = new HyperEdge(new Node[]{n1}, new Node[]{n5}, "e6");
        graph.add(e6);
        e7 = new HyperEdge(new Node[]{n5}, new Node[]{n6}, "e7");
        graph.add(e7);
        HyperEdge e9 = new HyperEdge(new Node[]{n4}, new Node[]{n6}, "e9");
        graph.add(e9);

        for (int i = 0; i < 4; i++) {
            Node n7 = new Node("n7");
            graph.add(n7);
            Node n8 = new Node("n8");
            graph.add(n8);

            HyperEdge e10 = new HyperEdge(new Node[]{n4}, new Node[]{n7}, "e10");
            graph.add(e10);
            HyperEdge e11 = new HyperEdge(new Node[]{n7}, new Node[]{n8}, "e11");
            graph.add(e11);
            HyperEdge e12 = new HyperEdge(new Node[]{n8}, new Node[]{n8}, "e12");
            graph.add(e12);
        }


        return graph;
    }

}
