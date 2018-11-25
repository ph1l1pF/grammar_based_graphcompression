package Digram;

import model.Digram.AdjacencyDigram;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AdjacencyDigramTest {

    @Test
    public void testHashCode() {
        AdjacencyDigram digram1 = new AdjacencyDigram("a", "b", 1, 2,
                AdjacencyDigram.EdgeDirection.INGOING, AdjacencyDigram.EdgeDirection.OUTGOING, new ArrayList<>());

        AdjacencyDigram digram2 = new AdjacencyDigram("b", "a", 2, 1,
                AdjacencyDigram.EdgeDirection.OUTGOING, AdjacencyDigram.EdgeDirection.INGOING, new ArrayList<>());


        AdjacencyDigram digram3 = new AdjacencyDigram("b", "a", 1, 2,
                AdjacencyDigram.EdgeDirection.INGOING, AdjacencyDigram.EdgeDirection.OUTGOING, new ArrayList<>());

        Assert.assertEquals(digram1, digram2);
        Assert.assertNotEquals(digram1, digram3);
        Assert.assertNotEquals(digram2, digram3);
    }
}
