package control;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import model.Digram;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
/**
 * This is a graphical user interface for the simulation of a grammar based compression process for a HyperGraph object.
 *
 *This class has a step-by-step simulation for the compression process and hat a interface for import a HyperGraph from a text file.
 *The graph is displayed using the graphstream library. Therefore we could determine the style using a css stylesheet.
 *
 * @author  Matthias Duerksen
 *
 */
public class MyFrame extends JFrame {

    /**
     * The path for the initial HyperGraph.
     */
    private static final String INITIALE_PATH = "GraphExamples/testGraph.txt";

    /**
     * ContentPane of the user interface.
     */
    private final JPanel contentPane;

    /**
     * The displayed graph on the UI.
     */
    private final Graph graph;

    /**
     * Button for the next step of the step-by-step simulation.
     */
    private final JButton btnNextStep;

    /**
     * TextArea of the UI for the textual description of the current step.
     */
    private final JTextArea textArea;

    /**
     * graph node size of the HyperGraph in the current step.
     */
    private final JLabel lblNodeSize;

    /**
     * graph edge size of the HyperGraph in the current step.
     */
    private final JLabel lblEdgeSize;

    /**
     * Label for the description that reference the current digram.
     */
    private final JLabel lblNextDigram;

    /**
     * Label for the new digram of the current step.
     */
    private final JLabel lblDigram;

    /**
     * Controller for the management of the program.
     */
    private Controller controller;

    /**
     * Current path for the import of a HyperGraph.
     */
    private String currentPath = INITIALE_PATH;

    /**
     * styleSheet for how the graph should be displayed.
     */
    private String styleSheet = "";

    /**
     * initial styleSheet for how the graph should be displayed.
     */
    private static final String INITIAL_STYLE_SHEET = "graphStyleSheet.txt";

    /**
     * Creates the frame for the application.
     */
    private MyFrame() {
        Controller controller = new Controller(this);
        setController(controller);
        controller.start();

        try {
            styleSheet = new String(Files.readAllBytes(Paths.get(INITIAL_STYLE_SHEET)));
        } catch (IOException e) {
            e.printStackTrace();
        }


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1284, 790);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setTitle("Grammar-based Graphcompression");

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBounds(22, 58, 818, 487);


        graph = new MultiGraph("testText");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);


        DefaultView view = (DefaultView) viewer.addDefaultView(false);

        view.setBounds(panel.getBounds());
        view.setVisible(true);
        contentPane.add(view);

        btnNextStep = new JButton("Next Step");
        btnNextStep.setEnabled(false);
        btnNextStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calledNextStep();
            }
        });
        btnNextStep.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnNextStep.setBounds(855, 475, 115, 25);
        contentPane.add(btnNextStep);

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setBounds(22, 550, 461, 132);
        //contentPane.add(textArea);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(857, 58, 390, 391);
        contentPane.add(sp);


        contentPane.add(panel);
        contentPane.remove(panel);

        JButton btnStartCompresssion = new JButton("Start Compresssion");
        btnStartCompresssion.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnStartCompresssion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                startCompression();
            }


        });
        btnStartCompresssion.setBounds(22, 16, 162, 23);
        contentPane.add(btnStartCompresssion);

        JSlider slider = new JSlider();
        slider.setBounds(172, 560, 200, 26);
        slider.addChangeListener(e -> view.getCamera().setViewPercent(slider.getValue() / 50.0));
        contentPane.add(slider);

        JLabel lblGraphsize = new JLabel("Graph:");
        lblGraphsize.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblGraphsize.setBounds(77, 637, 60, 25);
        contentPane.add(lblGraphsize);

        lblNodeSize = new JLabel("0");
        lblNodeSize.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNodeSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNodeSize.setBounds(133, 657, 46, 25);
        contentPane.add(lblNodeSize);

        JLabel lblNodes = new JLabel("Nodes");
        lblNodes.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNodes.setBounds(189, 660, 60, 19);
        contentPane.add(lblNodes);

        lblEdgeSize = new JLabel("0");
        lblEdgeSize.setHorizontalAlignment(SwingConstants.RIGHT);
        lblEdgeSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblEdgeSize.setBounds(133, 693, 46, 25);
        contentPane.add(lblEdgeSize);

        JLabel lblEdges = new JLabel("Edges");
        lblEdges.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblEdges.setBounds(189, 696, 60, 19);
        contentPane.add(lblEdges);

        txtPathArray = new JTextField();
        txtPathArray.setText("GraphExamples/testGraph.txt");
        txtPathArray.setBounds(233, 16, 332, 26);
        contentPane.add(txtPathArray);
        txtPathArray.setColumns(10);
        txtPathArray.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                currentPath = txtPathArray.getText();
            }
        });

        JButton btnChoose = new JButton("choose");
        btnChoose.setToolTipText("choose File");
        btnChoose.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnChoose.setBounds(580, 15, 100, 23);
        btnChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                chooseFile();
            }


            private void chooseFile() {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("./GraphExamples"));
                chooser.showOpenDialog(null);
                File f = chooser.getCurrentDirectory();
                currentPath = "" + chooser.getSelectedFile();

                txtPathArray.setText(currentPath);
                HyperGraph hyperGraph = controller.importGraph(currentPath);
                newGraphSelected(hyperGraph);


            }
        });
        contentPane.add(btnChoose);

        lblNextDigram = new JLabel("Next Digram:");
        lblNextDigram.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNextDigram.setBounds(397, 639, 110, 25);
        contentPane.add(lblNextDigram);
        lblNextDigram.setVisible(false);

        lblDigram = new JLabel("digram");
        lblDigram.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDigram.setBounds(449, 693, 100, 25);
        contentPane.add(lblDigram);
        lblDigram.setVisible(false);


        viewer.enableAutoLayout();
        ViewerPipe vp = viewer.newViewerPipe();
        vp.addViewerListener(new ViewerListener() {
            @Override
            public void viewClosed(String viewName) {
                // don't care
            }

            @Override
            public void buttonPushed(String id) {
                Node n = graph.getNode(id);
                String attributes[] = n.getAttributeKeySet().toArray(new String[n.getAttributeKeySet().size()]);

                String attributeToChange = (String) JOptionPane.showInputDialog(null, "Select attibute to modify", "Attribute for " + id, JOptionPane.QUESTION_MESSAGE, null, attributes, attributes[0]);
                String curValue = n.getAttribute(attributeToChange);
                String newValue
                        = JOptionPane.showInputDialog("New Value", curValue);
                n.setAttribute(attributeToChange, newValue);
            }

            @Override
            public void buttonReleased(String id) {
                // don't care
            }
        });


        HyperGraph mygraph=new HyperGraph();
        mygraph = Util.createSampleGraph();
        mygraph = controller.importGraph(INITIALE_PATH);
        newGraphSelected(mygraph);


    }

    /**
     * Method for start the compression and the step-by-step simulation.
     */
    private void startCompression() {
        textArea.setText("");
        Runnable myrunnable = new Runnable() {
            public void run() {
                controller.graphCompression();
            }
        };

        new Thread(myrunnable).start();


    }

    /**
     * TextField for the current Path for the import.
     */
    private final JTextField txtPathArray;

    /**
     * Method to update the UI for the next step of the compression simulation.
     * @param hyperGraph current graph of the new step of the compression.
     * @param text the description text that will be added to the textarea.
     * @param markedElements contains all Elements of the HyperGraph which should be marked (here for all elements which will be replaced in the next step).
     * @param nextDigram   the current digram which is executed in the next step.
     */
    public synchronized void refreshGraph(HyperGraph hyperGraph, String text, Tuple<List<GraphNode>, List<Edge>> markedElements, Digram nextDigram) {
        graph.clear();
        displayGraph(hyperGraph);
        textArea.append(text + "\n");
        lblNodeSize.setText("" + hyperGraph.getAllNodes().size());
        lblEdgeSize.setText("" + hyperGraph.getAllEdges().size());

        if (nextDigram == null) {
            lblNextDigram.setVisible(false);
            lblDigram.setVisible(false);
        } else {
            lblDigram.setText(nextDigram.getStartNodelabel() + " -> " + nextDigram.getEndNodelabel());
            lblNextDigram.setVisible(true);
            lblDigram.setVisible(true);
        }

        markElements(markedElements);
    }

    /**
     * Display the graph on the UI
     * @param hyperGraph the graph which is to be displayed
     */
    private void displayGraph(HyperGraph hyperGraph) {
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.addAttribute("ui.stylesheet", styleSheet);

        displayNodes(graph, hyperGraph.getAllNodes());
        displayEdges(graph, hyperGraph.getAllEdges());

    }

    /**
     * Display the nodes of the graph on the UI.
     * @param graph the graph which is to be displayed.
     * @param nodes the nodes which should be displayed.
     */
    private void displayNodes(Graph graph, HashMap<Integer, GraphNode> nodes) {
        for (Map.Entry<Integer, GraphNode> entry : nodes.entrySet()) {
            GraphNode graphNode = entry.getValue();

            graph.addNode(getIDString(graphNode));
            Node node = graph.getNode(getIDString(graphNode));
            node.setAttribute("ui.label", graphNode.getLabel());

        }

    }

    /**
     * Displays the edges of the graph on the UI.
     * @param graph the graph which is to be displayed.
     * @param edges the edges which should be displayed.
     */
    private void displayEdges(Graph graph, HashMap<Integer, Edge> edges) {
        for (Map.Entry<Integer, Edge> entry : edges.entrySet()) {
            if (entry.getValue() instanceof SimpleEdge) {
                SimpleEdge myEdge = (SimpleEdge) entry.getValue();
                graph.addEdge(getIDString(myEdge), getIDString(myEdge.getStartnode()), getIDString(myEdge.getEndnode()), true);

            } else {
                HyperEdge myEdge = (HyperEdge) entry.getValue();
                if (myEdge.getStartnodes().length!=1||myEdge.getEndnodes().length!=1){
                    graph.addNode("hypernode"+myEdge.getId());
                    Node uiNode = graph.getNode("hypernode"+myEdge.getId());
                    uiNode.setAttribute("ui.label", myEdge.getLabel());
                    uiNode.setAttribute("ui.class", "hypernode");
                    for (int i=0;i<myEdge.getStartnodes().length;i++) {
                        graph.addEdge(getIDString(myEdge)+"s"+i, getIDString(myEdge.getStartnodes()[i]), "hypernode"+myEdge.getId(), true);

                    }
                    for (int j=0;j<myEdge.getEndnodes().length;j++) {
                        graph.addEdge(getIDString(myEdge)+"e"+j, "hypernode"+myEdge.getId(), getIDString(myEdge.getEndnodes()[j]), true);
                    }

                }else {
                    graph.addEdge(getIDString(myEdge), getIDString(myEdge.getStartnodes()[0]), getIDString(myEdge.getEndnodes()[0]), true);
                    org.graphstream.graph.Edge edge = graph.getEdge(getIDString(myEdge));
                    edge.setAttribute("ui.label", myEdge.getLabel());
                }
            }

        }

    }

    /**
     * Constructs an ID for the displayed node.
     * @param node the node which the id is to be generated for.
     * @return the id for the displayed node.
     */
    private String getIDString(GraphNode node) {
        return "n" + node.getId();
    }

    /**
     * Constructs an ID for the displayed edge.
     * @param edge the node which the id is to be generated for.
     * @return the id for the displayed edge.
     */
    private String getIDString(Edge edge) {
        return "e" + edge.getId();
    }

    /**
     * Sets all elements of the parameter in the graph on the style  attribute 'marked'.
     *
     * Here this is used to display which elements will be replaced in the next step.
     * @param markedElements elements that should be marked.
     */
    private void markElements(Tuple<List<GraphNode>, List<Edge>> markedElements) {
        List<GraphNode> nodes = markedElements.x;
        List<Edge> edges = markedElements.y;

        for (GraphNode node : nodes) {
            Node uiNode = graph.getNode(getIDString(node));
            uiNode.setAttribute("ui.class", "marked");
        }

        for (Edge edge : edges) {
            org.graphstream.graph.Edge uiEdge = graph.getEdge(getIDString(edge));
            uiEdge.setAttribute("ui.class", "marked");

        }

    }

    /**
     * Displays all pruned digrams and the size of the digrams in the textarea.
     * @param digrams digrams that should be displayed.
     */
    public void showAllDigrams(List<Digram> digrams) {

        textArea.append("\n");
        for (Digram digram : digrams) {
            textArea.append(digram.toString() + "\n");
            System.out.println(digram.toString());
        }
        textArea.append("\n");
        int numInternalNodes = 0;
        int nunInternalEdges = 0;
        for (Digram digram : digrams) {

            Tuple<Integer, Integer> numInternalElements = digram.getNumInternalElements();
            numInternalNodes += numInternalElements.x;
            nunInternalEdges += numInternalElements.y;

        }
        textArea.append("NumInternalNodesInDigrams: " + numInternalNodes + "\nNumInternalEdgesInDigrams: " + nunInternalEdges);
    }

    /**
     * Executes when the last step of the simulation is done i.e to disable the next step button.
     */
    public void compressionFinished() {
        btnNextStep.setEnabled(false);
    }

    /**
     * Resets the UI for a new compression process for the HyperGraph in the parameter.
     * @param hyperGraph the HyperGraph which will be the next graph for the compression.
     */
    private void newGraphSelected(HyperGraph hyperGraph) {
        graph.clear();
        displayGraph(hyperGraph);
        textArea.setText("");
        lblNodeSize.setText("" + hyperGraph.getAllNodes().size());
        lblEdgeSize.setText("" + hyperGraph.getAllEdges().size());

        btnNextStep.setEnabled(false);
        lblNextDigram.setVisible(false);
        lblDigram.setVisible(false);

    }

    /**
     * Gives the controller the permission for the next step.
     */
    private void calledNextStep() {
        controller.givePermissionForNextStep();
    }

    /**
     * Setter for the possibility to apply the next step button.
     */
    public void setButtonNextStep() {
        btnNextStep.setEnabled(true);
    }

    /**
     * Setter for the Controller.
     * @param controller reference for the controler.
     */
    private void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Launches the application.
     * @param args arguments for the main method, not used here.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyFrame frame = new MyFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
