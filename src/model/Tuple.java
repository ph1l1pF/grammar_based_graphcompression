package model;

/**
 * This is a class for a Tuple.
 *
 * A Tuple is a generic object with for two Elements x and y.
 *
 * @author Matthias Duerksen
 */
public class Tuple<X, Y> {

    /**
     * first element of the tuple with type X.
     */
    public final X x;

    /**
     * second element of the tuple with type Y.
     */
    public final Y y;

    /**
     * Constructor of Tuple.
     * @param x fist element of the tuple.
     * @param y second element of the tuple.
     */
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x.hashCode();
        result = prime * result + y.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return x.equals(((Tuple)obj).x) && y.equals(((Tuple)obj).y);
    }

    @Override
    public String toString() {
        return "("+x.toString()+", "+y.toString()+")";
    }
}
