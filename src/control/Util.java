package control;

import model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a class for standard tasks.
 * @author Matthias Duerksen
 */
class Util {

    public static HyperGraph createOtherGraphFromThesis(){
        HyperGraph graph = new HyperGraph();

        GraphNode[] nodes = new GraphNode[4];
        nodes[0]= new GraphNode("n2");
        nodes[1]= new GraphNode("n1");
        nodes[2]= new GraphNode("n2");
        nodes[3]= new GraphNode("n1");

        SimpleEdge[] edges = new SimpleEdge[3];
        edges[0] = new SimpleEdge(nodes[1], nodes[0]);
        edges[1] = new SimpleEdge(nodes[1], nodes[2]);
        edges[2] = new SimpleEdge(nodes[3], nodes[2]);

        for (GraphNode node : nodes) {
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

        GraphNode[] nodes = new GraphNode[7];
        nodes[0]= new GraphNode("x");
        nodes[1]= new GraphNode("x");
        nodes[2]= new GraphNode("z");
        nodes[3]= new GraphNode("y");
        nodes[4]= new GraphNode("y");
        nodes[5]= new GraphNode("z");
        nodes[6]= new GraphNode("w");

        SimpleEdge[] edges = new SimpleEdge[7];
        edges[0] = new SimpleEdge(nodes[0], nodes[1]);
        edges[1] = new SimpleEdge(nodes[1], nodes[2]);
        edges[2] = new SimpleEdge(nodes[2], nodes[3]);
        edges[3] = new SimpleEdge(nodes[3], nodes[4]);
        edges[4] = new SimpleEdge(nodes[5], nodes[4]);
        edges[5] = new SimpleEdge(nodes[0], nodes[5]);
        edges[6] = new SimpleEdge(nodes[5], nodes[6]);

        for (GraphNode node : nodes) {
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
        createOtherGraphFromThesis();
    }

    /**
     * Create a control HyperGraph.
     * @return a HyperGraph.
     */
    public static HyperGraph createSampleGraph() {
        HyperGraph graph = new HyperGraph();

        GraphNode n6 = new GraphNode("n6");
        graph.add(n6);

        //Left Side
        GraphNode n1 = new GraphNode("n1");
        graph.add(n1);
        GraphNode n2 = new GraphNode("n2");
        graph.add(n2);
        GraphNode n3 = new GraphNode("n3");
        graph.add(n3);
        GraphNode n4 = new GraphNode("n4");
        graph.add(n4);
        GraphNode n5 = new GraphNode("n5");
        graph.add(n5);


        HyperEdge e1 = new HyperEdge(new GraphNode[]{n2}, new GraphNode[]{n1}, "e1");
        graph.add(e1);
        HyperEdge e2 = new HyperEdge(new GraphNode[]{n2}, new GraphNode[]{n3}, "e2");
        graph.add(e2);
        HyperEdge e3 = new HyperEdge(new GraphNode[]{n4}, new GraphNode[]{n3}, "e3");
        graph.add(e3);
        HyperEdge e5 = new HyperEdge(new GraphNode[]{n1}, new GraphNode[]{n5}, "e5");
        graph.add(e5);
        HyperEdge e7 = new HyperEdge(new GraphNode[]{n5}, new GraphNode[]{n6}, "e7");
        graph.add(e7);
        HyperEdge e8 = new HyperEdge(new GraphNode[]{n4}, new GraphNode[]{n6}, "e3");
        graph.add(e8);

        for (int i = 0; i < 4; i++) {
            GraphNode n7 = new GraphNode("n7");
            graph.add(n7);
            GraphNode n8 = new GraphNode("n8");
            graph.add(n8);

            HyperEdge e10 = new HyperEdge(new GraphNode[]{n4}, new GraphNode[]{n7}, "e10");
            graph.add(e10);
            HyperEdge e11 = new HyperEdge(new GraphNode[]{n7}, new GraphNode[]{n8}, "e11");
            graph.add(e11);
            HyperEdge e12 = new HyperEdge(new GraphNode[]{n8}, new GraphNode[]{n8}, "e12");
            graph.add(e12);
        }


        //Right Side
        n1 = new GraphNode("n1");
        graph.add(n1);
        n2 = new GraphNode("n2");
        graph.add(n2);
        n3 = new GraphNode("n3");
        graph.add(n3);
        n4 = new GraphNode("n4");
        graph.add(n4);
        n5 = new GraphNode("n5");
        graph.add(n5);


        e1 = new HyperEdge(new GraphNode[]{n2}, new GraphNode[]{n1}, "e1");
        graph.add(e1);

        e3 = new HyperEdge(new GraphNode[]{n4}, new GraphNode[]{n3}, "e3");
        graph.add(e3);
        HyperEdge e4 = new HyperEdge(new GraphNode[]{n2}, new GraphNode[]{n3}, "e4");
        graph.add(e4);
        HyperEdge e6 = new HyperEdge(new GraphNode[]{n1}, new GraphNode[]{n5}, "e6");
        graph.add(e6);
        e7 = new HyperEdge(new GraphNode[]{n5}, new GraphNode[]{n6}, "e7");
        graph.add(e7);
        HyperEdge e9 = new HyperEdge(new GraphNode[]{n4}, new GraphNode[]{n6}, "e9");
        graph.add(e9);

        for (int i = 0; i < 4; i++) {
            GraphNode n7 = new GraphNode("n7");
            graph.add(n7);
            GraphNode n8 = new GraphNode("n8");
            graph.add(n8);

            HyperEdge e10 = new HyperEdge(new GraphNode[]{n4}, new GraphNode[]{n7}, "e10");
            graph.add(e10);
            HyperEdge e11 = new HyperEdge(new GraphNode[]{n7}, new GraphNode[]{n8}, "e11");
            graph.add(e11);
            HyperEdge e12 = new HyperEdge(new GraphNode[]{n8}, new GraphNode[]{n8}, "e12");
            graph.add(e12);
        }


        return graph;
    }

}
