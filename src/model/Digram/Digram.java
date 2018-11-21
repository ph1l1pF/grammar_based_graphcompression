package model.Digram;

import model.DigramOccurrence.DigramOccurrence;
import model.Graph.Node;
import model.Tuple;

import java.util.*;

public abstract class Digram {

    /**
     * counter for the identifier of a digram.
     */
    private static int digramCounter = 0;

    protected List<DigramOccurrence> occurrences;
    protected final Map<String, List<Tuple<Integer, Integer>>> mapEquivClasses = new HashMap<>();
    /**
     * the non terminal from the digram.
     */
    protected String nonterminal;
    /**
     * the set of inlined digrams.
     */
    protected final List<Digram> inlinedDigrams = new ArrayList<>();
    /**
     * set of all nodes from the associated occurrences.
     */
    protected final HashMap<Integer, Node> allNodes = new HashMap<>();

    /**
     * indicates whether the digram contain inlinded occurrences.
     */
    protected boolean hasInlined = false;
    /**
     * Indicates whether the digram is applied.
     */
    protected boolean beenApplied = false;

    protected int equivClassCounter = 1;

    public Digram() {
        this.occurrences = new ArrayList<>();
    }


    /**
     * Setter for the Non terminal of the digram.
     */
    public void setNonterminal() {
        nonterminal = "A_" + digramCounter++;

    }

    public abstract int getNumOccurrences(String label) ;

    public abstract String innerToString();

    protected void putTuplesForNodeWithLabel(List<Digram> appliedDigrams, String nodeLabel, boolean isStartNode){
        List<Tuple<Integer, Integer>> lstNewTuples = new ArrayList<>();
        boolean isNonTerminal = false;
        for(Digram appliedDigram : appliedDigrams){
            if(appliedDigram.getNonterminal().equals(nodeLabel)){
                // the startnode is a non terminal, so more complex mapping is needed
                isNonTerminal = true;
                Map<String, List<Tuple<Integer, Integer>>> mapEquivClassesAppliedDigr = appliedDigram.getMapEquivClasses();

                for(List<Tuple<Integer, Integer>> lstTuples : mapEquivClassesAppliedDigr.values()){
                    for(Tuple<Integer, Integer> tuple : lstTuples){
                        lstNewTuples.add(new Tuple<>(tuple.y, equivClassCounter++));
                    }
                }
            }
        }
        if(!isNonTerminal){
            // just a normal node, only one tuple is needed
            lstNewTuples.add(new Tuple<>(1, equivClassCounter++));
        }

        if(isStartNode) {
            mapEquivClasses.put("startNode", lstNewTuples);
        }else{
            mapEquivClasses.put("endNode", lstNewTuples);

        }
    }


    /**
     * Getter for the number of internal Elements in this and all inlined digrams.
     * @return the number of internal elements in this and all inlined digrams.
     */
    public Tuple<Integer, Integer> getNumInternalElements() {
        int numInternalNodes = 2;
        int numInternalEdges = 1;
        for (Digram digram : inlinedDigrams) {
            Tuple<Integer, Integer> tuple = digram.getNumInternalElements();
            numInternalNodes += tuple.x - 1;
            numInternalEdges += tuple.y;
        }
        return new Tuple<>(numInternalNodes, numInternalEdges);
    }

    /**
     * Getter for all associated occurrences.
     * @return all associated occurrences of the digram.
     */
    public HashMap<Integer, Node> getAllNodes() {
        return allNodes;
    }

    public List<DigramOccurrence> getOccurrences() {
        return occurrences;
    }

    public Map<String, List<Tuple<Integer, Integer>>> getMapEquivClasses() {
        return mapEquivClasses;
    }

    /**
     * Gets the number of associated occurrences.
     * @return the number of associate occurrences of the digram.
     */
    public int getSize() {
        return occurrences.size();
    }


    /**
     * Getter for the Non terminal of the digram.
     * @return the Non terminal of the digram.
     */
    public String getNonterminal() {
        return nonterminal;
    }


    /**
     * Getter that indicates that digram was applied.
     * @return true if the digram was applied, else false.
     */
    public boolean hasBeenAplied() {
        return beenApplied;
    }



    /**
     * Sets the digram to applied.
     */
    public void setBeenApplied() {
        beenApplied = true;
    }
}
