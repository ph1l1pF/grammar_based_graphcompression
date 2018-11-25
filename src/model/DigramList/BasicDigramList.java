package model.DigramList;

import model.Digram.BasicDigram;
import model.Digram.Digram;
import model.Graph.SimpleEdge;
import model.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicDigramList extends DigramList{

    /**
     * the data structure for DigramList which is realized by a double connected HashMap.
     */
    protected Map<Tuple<String,Integer>, Map<Tuple<String,Integer>, BasicDigram>> digramList = new HashMap<>();

    /**
     * Constructor for the DigramList.
     *
     * @param labels the necessary different labels of the nodes in the graph.
     */
    public BasicDigramList(List<String> labels) {
        super(labels);
    }

    @Override
    public List<Digram> getAllActiveDigrams() {
        List<Digram> allDigrams = new ArrayList<>();
        for(Map<Tuple<String,Integer>, BasicDigram> value : digramList.values()){
            for(Digram digram : value.values()){
                allDigrams.add(digram);
            }
        }
        return allDigrams;
    }

    /**
     * Gets the BasicDigram for the labels label1,label2.
     * @return the digram for the two labels.
     */
    public BasicDigram getDigram(Object pivot, List<Digram> appliedDigrams) {

        if(!(pivot instanceof SimpleEdge)){
            throw new IllegalArgumentException();
        }
        SimpleEdge edge = (SimpleEdge) pivot;

        String label1 = edge.getStartnode().getLabel();
        String label2 = edge.getEndnode().getLabel();

        if (!labels.contains(label1) || !labels.contains(label2)) {
            return null;
        }
        int equiv1 = edge.getEquivalenceClass(edge.getStartnode());
        int equiv2 = edge.getEquivalenceClass(edge.getEndnode());

        Tuple<String, Integer> tuple1 = new Tuple<>(label1, equiv1);
        Tuple<String, Integer> tuple2 = new Tuple<>(label2, equiv2);

        if (!digramList.containsKey(tuple1)) {
            digramList.put(tuple1, new HashMap<>());
        }
        if (!digramList.get(tuple1).containsKey(tuple2)) {

            BasicDigram basicDigram = new BasicDigram(label1, label2, equiv1, equiv2, appliedDigrams);
            digramList.get(tuple1).put(tuple2, basicDigram);

            if (getMaxDigram() == null || getMaxDigram().getSize() < basicDigram.getSize()) {
                maxDigram = basicDigram;
            }
        }
        return digramList.get(tuple1).get(tuple2);
    }
}
