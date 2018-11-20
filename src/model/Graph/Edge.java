package model.Graph;

/**
 * This class is an abstract class for the simple edges and hyper edges.
 *
 * @author Matthias Duerksen
 */
public abstract class Edge {

    /**
     * counter for the identifier of the edges.
     */
    static  int idCounter=0;

    /**
     * Gets the ID from the edge.
     * @return the id for the edge.
     */
    public abstract int getId();
}
