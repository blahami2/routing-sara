/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.algorithm;

import cz.certicon.routing.model.Route;
import cz.certicon.routing.model.graph.Edge;
import cz.certicon.routing.model.graph.SimpleEdge;
import cz.certicon.routing.model.graph.Graph;
import cz.certicon.routing.model.graph.Metric;
import cz.certicon.routing.model.graph.Node;
import cz.certicon.routing.model.graph.SimpleNode;
import cz.certicon.routing.model.graph.TurnTable;
import cz.certicon.routing.model.graph.UndirectedGraph;
import cz.certicon.routing.model.values.Distance;
import cz.certicon.routing.utils.GraphGeneratorUtils;
import cz.certicon.routing.utils.RandomUtils;
import cz.certicon.routing.utils.ToStringUtils;
import cz.certicon.routing.utils.java8.IteratorStreams;
import cz.certicon.routing.utils.java8.Mappers;
import cz.certicon.routing.utils.java8.Optional;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java8.util.Spliterator;
import java8.util.Spliterators;
import java8.util.function.Function;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
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

    private final Graph<Node, Edge> graph;
    private final Map<Long, Node> nodeMap;
    private final Map<Long, Edge> edgeMap;
    private final Map<TurnTable, TurnTable> turnTables;

    public DijkstraAlgorithmTest() {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.turnTables = new HashMap<>();
        graph = createGraph();
    }

    private Graph<Node, Edge> createGraph() {
        return GraphGeneratorUtils.createGraph( EnumSet.of( Metric.LENGTH ), nodeMap, edgeMap, turnTables );
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        RandomUtils.setSeed( 1 );
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
        Route<Node, Edge> expResult = Route.builder().addAsLast( edgeMap.get( 0L ) ).addAsLast( edgeMap.get( 4L ) ).addAsLast( edgeMap.get( 6L ) ).addAsLast( edgeMap.get( 5L ) ).build();
        Optional<Route<Node, Edge>> result = instance.route( graph, Metric.LENGTH, source, destination );
        assertEquals(  ToStringUtils.toString( expResult ),  ToStringUtils.toString( result.get() ) );
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
        Route<Node, Edge> expResult = Route.builder().addAsLast( edgeMap.get( 1L ) ).addAsLast( edgeMap.get( 0L ) ).addAsLast( edgeMap.get( 4L ) ).addAsLast( edgeMap.get( 6L ) ).addAsLast( edgeMap.get( 5L ) ).build();
        Optional<Route<Node, Edge>> result = instance.route( graph, Metric.LENGTH, source, destination );
        assertEquals(  ToStringUtils.toString( expResult ),  ToStringUtils.toString( result.get() ) );
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
        Route<Node, Edge> expResult = Route.builder().addAsLast( edgeMap.get( 1L ) ).addAsLast( edgeMap.get( 0L ) ).addAsLast( edgeMap.get( 4L ) ).addAsLast( edgeMap.get( 6L ) ).addAsLast( edgeMap.get( 5L ) ).addAsLast( edgeMap.get( 2L ) ).build();
        Optional<Route<Node, Edge>> result = instance.route( graph, Metric.LENGTH, source, destination, toSourceStart, toSourceEnd, toDestinationStart, toDestinationEnd );
        assertEquals( ToStringUtils.toString( expResult ),  ToStringUtils.toString( result.get() ) );
    }


}
