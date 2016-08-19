/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.algorithm;

import cz.certicon.routing.model.Route;
import cz.certicon.routing.model.graph.Edge;
import cz.certicon.routing.model.graph.Graph;
import cz.certicon.routing.model.graph.Node;
import cz.certicon.routing.model.graph.TurnTable;
import cz.certicon.routing.model.graph.UndirectedGraph;
import cz.certicon.routing.model.values.Distance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 */
public class DijkstraAlgorithmTest {

    private final UndirectedGraph graph;
    private final Map<Long, Node> nodeMap;
    private final Map<Long, Edge> edgeMap;
    private final Map<TurnTable, TurnTable> turnTables;

    public DijkstraAlgorithmTest() {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.turnTables = new HashMap<>();
        graph = createGraph();
    }

    private UndirectedGraph createGraph() {
        List<Node> nodes = new ArrayList<>();
        Node a = createNode( nodes, 0 );
        Node b = createNode( nodes, 1 );
        Node c = createNode( nodes, 2 );
        Node d = createNode( nodes, 3 );
        Node e = createNode( nodes, 4 );
        Node f = createNode( nodes, 5 );
        List<Edge> edges = new ArrayList<>();
        Edge ab = createEdge( edges, 0, false, a, b, 120 );
        Edge ac = createEdge( edges, 1, false, a, c, 184 );
        Edge cd = createEdge( edges, 2, false, c, d, 94 );
        Edge db = createEdge( edges, 3, true, d, b, 159 );
        Edge be = createEdge( edges, 4, false, b, e, 36 );
        Edge df = createEdge( edges, 5, false, d, f, 152 );
        Edge ef = createEdge( edges, 6, true, e, f, 38 );
        for ( Node node : nodes ) {
            int size = node.getDegree();
            Distance[][] dtt = new Distance[size][size];
            for ( int i = 0; i < dtt.length; i++ ) {
                for ( int j = 0; j < dtt[i].length; j++ ) {
                    if ( i != j ) {
                        dtt[i][j] = Distance.newInstance( 0 );
                    } else {
                        dtt[i][j] = Distance.newInfinityInstance();
                    }
                }
            }
            if ( node.getId() == 2 ) {
                dtt[0][1] = Distance.newInfinityInstance();
            }

            TurnTable tt = new TurnTable( dtt );
            if ( !turnTables.containsKey( tt ) ) {
                turnTables.put( tt, tt );
            } else {
                tt = turnTables.get( tt );
            }
            node.setTurnTable( tt );
        }
        for ( Node node : nodes ) {
            node.lock();
        }
        UndirectedGraph g = UndirectedGraph.builder().nodes( nodes ).edges( edges ).build();
        return g;
    }

    private Node createNode( List<Node> nodes, long id ) {
//        Distance[][] tt = new Distance[]
        Node node = new Node( id );
        nodes.add( node );
        nodeMap.put( id, node );
        return node;
    }

    private Edge createEdge( List<Edge> edges, long id, boolean oneway, Node source, Node target, double distance ) {
        Edge edge = new Edge( id, oneway, source, target, Distance.newInstance( distance ) );
        edges.add( edge );
        source.addEdge( edge );
        target.addEdge( edge );
        edgeMap.put( id, edge );
        return edge;
    }

    private Edge createEdge( List<Edge> edges, long id, boolean oneway, Node source, Node target, double distance, boolean addToNode ) {
        Edge edge = new Edge( id, oneway, source, target, Distance.newInstance( distance ) );
        edges.add( edge );
        if ( addToNode ) {
            source.addEdge( edge );
            target.addEdge( edge );
        }
        edgeMap.put( id, edge );
        return edge;
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of route method, of class DijkstraAlgorithm.
     */
    @Test
    public void testRoute_3args_1() {
        System.out.println( "route" );
        Node source = nodeMap.get( 0L );
        Node destination = nodeMap.get( 3L );
        DijkstraAlgorithm instance = new DijkstraAlgorithm();
        Route expResult = Route.builder().addAsLast( edgeMap.get( 0L ) ).addAsLast( edgeMap.get( 4L ) ).addAsLast( edgeMap.get( 6L ) ).addAsLast( edgeMap.get( 5L ) ).build();
        Route result = instance.route( graph, source, destination );
        assertEquals( toString( expResult ), toString( result ) );
    }

    /**
     * Test of route method, of class DijkstraAlgorithm.
     */
    @Test
    public void testRoute_3args_2() {
        System.out.println( "route" );
        Edge source = edgeMap.get( 1L );
        Edge destination = edgeMap.get( 5L );
        DijkstraAlgorithm instance = new DijkstraAlgorithm();
        Route expResult = Route.builder().addAsLast( edgeMap.get( 1L ) ).addAsLast( edgeMap.get( 0L ) ).addAsLast( edgeMap.get( 4L ) ).addAsLast( edgeMap.get( 6L ) ).addAsLast( edgeMap.get( 5L ) ).build();
        Route result = instance.route( graph, source, destination );
        assertEquals( toString( expResult ), toString( result ) );
    }

    /**
     * Test of route method, of class DijkstraAlgorithm.
     */
    @Test
    public void testRoute_7args() {
        System.out.println( "route" );
        Edge source = edgeMap.get( 1L );
        Edge destination = edgeMap.get( 2L );
        Distance toSourceStart = Distance.newInstance( 10 );
        Distance toSourceEnd = Distance.newInstance( 10 );
        Distance toDestinationStart = Distance.newInstance( 10 );
        Distance toDestinationEnd = Distance.newInstance( 10 );
        DijkstraAlgorithm instance = new DijkstraAlgorithm();
        Route expResult = Route.builder().addAsLast( edgeMap.get( 1L ) ).addAsLast( edgeMap.get( 0L ) ).addAsLast( edgeMap.get( 4L ) ).addAsLast( edgeMap.get( 6L ) ).addAsLast( edgeMap.get( 5L ) ).addAsLast( edgeMap.get( 2L ) ).build();
        Route result = instance.route( graph, source, destination, toSourceStart, toSourceEnd, toDestinationStart, toDestinationEnd );
        assertEquals( toString( expResult ), toString( result ) );
    }

    private static String toString( Route route ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "Route{source=" ).append( route.getSource().getId() ).append( ",target=" ).append( route.getTarget().getId() ).append( ",edges=[" );
        for ( Edge edge : route.getEdges() ) {
            sb.append( edge.getId() ).append( "," );
        }
        sb.replace( sb.length() - 1, sb.length(), "]}" );
        return sb.toString();
    }

}
