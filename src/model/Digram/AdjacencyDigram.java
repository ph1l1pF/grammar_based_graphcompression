package model.Digram;

import model.Tuple;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class AdjacencyDigram extends Digram {

    private String label1, label2;
    private int equivClass1, equivClass2;
    private EdgeDirection direction1, direction2;

    public AdjacencyDigram(String label1, String label2, int equivClass1, int equivClass2, EdgeDirection direction1, EdgeDirection direction2, List<Digram> appliedDigrams) {
        this.label1 = label1;
        this.label2 = label2;
        this.equivClass1 = equivClass1;
        this.equivClass2 = equivClass2;
        this.direction1 = direction1;
        this.direction2 = direction2;

        putTuplesForNodeWithLabel(appliedDigrams, label1, true);
        putTuplesForNodeWithLabel(appliedDigrams, label2, false);


        // TODO: This could be done nicer, but it should work
        List<Tuple<Integer,Integer>> list = new ArrayList<>();
        list.add(new Tuple<>(1,equivClassCounter++));
        mapEquivClasses.put("midNode", list);


    }

    @Override
    public int getNumOccurrences(String label) {
        throw  new RuntimeException();
    }

    @Override
    public String innerToString() {

        String string = label1;
        string +=" "+equivClass1;
        if(direction1==EdgeDirection.INGOING){
            string+= "<- ";
        }else{
            string+="-> ";
        }

        if(direction2==EdgeDirection.INGOING){
            string+= " ->";
        }else{
            string+=" <-";
        }
        string+=equivClass2;
        string+=label2;
        return string;

    }

    @Override
    public String toString() {
        return innerToString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AdjacencyDigram)) {
            return false;
        }
        AdjacencyDigram otherDigram = (AdjacencyDigram) obj;
        return hashCode() == otherDigram.hashCode();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        int iLabel1 = label1.hashCode();
        int iLabel2 = label2.hashCode();

        int min = Math.min(iLabel1, iLabel2);
        int max = Math.max(iLabel1, iLabel2);

        result = prime * result + min;
        result = prime * result + max;

        if (min == iLabel1) {
            result = prime * result + equivClass1;
            result = prime * result + equivClass2;
            result = prime * result + direction1.toInt();
            result = prime * result + direction2.toInt();
        } else {
            result = prime * result + equivClass2;
            result = prime * result + equivClass1;
            result = prime * result + direction2.toInt();
            result = prime * result + direction1.toInt();
        }
        return result;
    }

    public enum EdgeDirection {
        INGOING, OUTGOING;

        private int toInt() {
            if (this == INGOING) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public String getLabel1() {
        return label1;
    }

    public String getLabel2() {
        return label2;
    }

    public int getEquivClass1() {
        return equivClass1;
    }

    public int getEquivClass2() {
        return equivClass2;
    }

    public EdgeDirection getDirection1() {
        return direction1;
    }

    public EdgeDirection getDirection2() {
        return direction2;
    }
}
