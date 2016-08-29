/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.algorithm.sara.preprocessing.assembly;

import cz.certicon.routing.algorithm.sara.preprocessing.filtering.NaturalCutsFilter;
import cz.certicon.routing.model.graph.Edge;
import cz.certicon.routing.model.graph.Graph;
import cz.certicon.routing.model.graph.Node;
import cz.certicon.routing.model.graph.TurnTable;
import cz.certicon.routing.model.graph.UndirectedGraph;
import cz.certicon.routing.model.graph.preprocessing.ContractEdge;
import cz.certicon.routing.model.graph.preprocessing.ContractNode;
import cz.certicon.routing.model.graph.preprocessing.FilteredGraph;
import cz.certicon.routing.model.graph.preprocessing.NodePair;
import cz.certicon.routing.model.queue.PriorityQueue;
import cz.certicon.routing.utils.ColorUtils;
import cz.certicon.routing.utils.GraphGeneratorUtils;
import cz.certicon.routing.view.GraphStreamPresenter;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
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
public class GreedyAssemblerTest {

    private UndirectedGraph g;
    private FilteredGraph graph;
    private final Map<Long, Node> nodeMap;
    private final Map<Long, Edge> edgeMap;
    private final Map<TurnTable, TurnTable> turnTables;
    private static final int CELL_SIZE = 10;

    public GreedyAssemblerTest() {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.turnTables = new HashMap<>();
    }

    private FilteredGraph createNewGraph() {
        g = GraphGeneratorUtils.generateGridGraph( nodeMap, edgeMap, turnTables, 5, 5 );
        NaturalCutsFilter instance = new NaturalCutsFilter( 1, 4, CELL_SIZE );
        graph = instance.filter( g );
        return graph;
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
     * Test of assemble method, of class GreedyAssembler.
     */
    @Test
    public void testAssemble() {
        System.out.println( "assemble" );
        createNewGraph();
        Graph originalGraph = GraphGeneratorUtils.generateGridGraph( nodeMap, edgeMap, turnTables, 5, 5 );
        Set<Node> origNodes = new HashSet<>();
        Iterator<Node> nodes = originalGraph.getNodes();
        while ( nodes.hasNext() ) {
            origNodes.add( nodes.next() );
        }
        System.out.println( "orig graph: " + originalGraph );
        createNewGraph();
        GreedyAssembler assembler = new GreedyAssembler( 0.5, 0.5, CELL_SIZE );
        Graph assembled = assembler.assemble( graph );
        nodes = assembled.getNodes();
        while ( nodes.hasNext() ) {
            ContractNode node = (ContractNode) nodes.next();
            for ( Node n : node.getNodes() ) {
                if ( !origNodes.contains( n ) ) {
                    System.out.println( "Graph does not contain node: node = " + n + ", graph = " + assembled );
                }
                assertTrue( origNodes.contains( n ) );
                origNodes.remove( n );
            }
        }
        if ( !origNodes.isEmpty() ) {
            System.out.println( "Orignodes conain more nodes[" + origNodes.size() + "]: " + origNodes );
        }
        assertTrue( origNodes.isEmpty() );
//        System.out.println( assembled );

        Map<ContractNode, Color> colorMap = new HashMap<>();
        ColorUtils.ColorSupplier colorSupplier = ColorUtils.createColorSupplier( assembled.getNodesCount() );
        GraphStreamPresenter presenter = new GraphStreamPresenter();
        presenter.setGraph( assembled );
        nodes = assembled.getNodes();
        while ( nodes.hasNext() ) {
            ContractNode node = (ContractNode) nodes.next();
            Color c = colorSupplier.nextColor();
            colorMap.put( node, c );
            presenter.setNodeColor( node.getId(), c );
        }
        presenter.display();
        System.out.println( "Comparison: orig{nodes=" + graph.getNodesCount() + ",edges=" + graph.getEdgeCount() + "}, filtered{nodes=" + assembled.getNodesCount() + ",edges=" + assembled.getEdgeCount() + "}" );

        System.out.println( assembled );

        createNewGraph();
        presenter.setGraph( originalGraph );
        nodes = assembled.getNodes();
        while ( nodes.hasNext() ) {
            ContractNode node = (ContractNode) nodes.next();
            Color c = colorMap.get( node );
            for ( Node n : node.getNodes() ) {
                presenter.setNodeColor( n.getId(), c );
            }
        }
        presenter.display();

        System.out.println( "Press enter to continue..." );
        new Scanner( System.in ).nextLine();
    }

    /**
     * Test of initQueue method, of class GreedyAssembler.
     */
    @Test
    public void testInitQueue() {
        System.out.println( "initQueue" );
        createNewGraph();
        GreedyAssembler instance = new GreedyAssembler( 0.5, 0.5, CELL_SIZE );
        PriorityQueue<NodePair> result = instance.initQueue( graph );
        Iterator<Node> nodes = graph.getNodes();
        while ( nodes.hasNext() ) {
            ContractNode node = (ContractNode) nodes.next();
            Iterator<Edge> edges = node.getEdges();
            while ( edges.hasNext() ) {
                ContractEdge edge = (ContractEdge) edges.next();
                ContractNode target = (ContractNode) edge.getOtherNode( node );
                NodePair pair = new NodePair( node, target, edge );
                assertTrue( result.contains( pair ) );
            }
        }
    }

    /**
     * Test of clearPairs method, of class GreedyAssembler.
     */
    @Test
    public void testClearPairs() {
        System.out.println( "clearPairs" );
        createNewGraph();
        GreedyAssembler instance = new GreedyAssembler( 0.5, 0.5, CELL_SIZE );
        PriorityQueue<NodePair> queue = instance.initQueue( graph );
        NodePair origPair = queue.extractMin();
        ContractNode nodeA = origPair.nodeA;
        ContractNode nodeB = origPair.nodeB;
        Iterator<Edge> edges;
//        System.out.println( "CLEARING PAIRS FOR: " + nodeA );
        PriorityQueue<NodePair> result = instance.clearPairs( queue, origPair, nodeA );
        Iterator<Node> nodes = graph.getNodes();
        while ( nodes.hasNext() ) {
            ContractNode node = (ContractNode) nodes.next();
            edges = node.getEdges();
            while ( edges.hasNext() ) {
                ContractEdge edge = (ContractEdge) edges.next();
                ContractNode target = (ContractNode) edge.getOtherNode( node );
                NodePair pair = new NodePair( node, target, edge );
                if ( node.equals( nodeA ) || target.equals( nodeA ) ) {
//                    System.out.println( "SHOULD NOT CONTAIN: " + pair );
                    assertFalse( result.contains( pair ) );
                } else {
                    assertTrue( result.contains( pair ) );
                }
            }
        }
        result = instance.clearPairs( queue, origPair, nodeB );
        nodes = graph.getNodes();
        while ( nodes.hasNext() ) {
            ContractNode node = (ContractNode) nodes.next();
            edges = node.getEdges();
            while ( edges.hasNext() ) {
                ContractEdge edge = (ContractEdge) edges.next();
                ContractNode target = (ContractNode) edge.getOtherNode( node );
                NodePair pair = new NodePair( node, target, edge );
                if ( node.equals( nodeA ) || target.equals( nodeA ) || node.equals( nodeB ) || target.equals( nodeB ) ) {
                    assertFalse( result.contains( pair ) );
                } else {
                    assertTrue( result.contains( pair ) );
                }
            }
        }
    }

    /**
     * Test of addPairs method, of class GreedyAssembler.
     */
    @Test
    public void testAddPairs() {
        System.out.println( "addPairs" );
        createNewGraph();
        GreedyAssembler instance = new GreedyAssembler( 0.5, 0.5, CELL_SIZE );
        PriorityQueue<NodePair> queue = instance.initQueue( graph );
        NodePair origPair = queue.extractMin();
        ContractNode nodeA = origPair.nodeA;
        ContractNode nodeB = origPair.nodeB;
        Iterator<Edge> edges;
        Iterator<Node> nodes;
        instance.clearPairs( queue, origPair, nodeA );
        instance.clearPairs( queue, origPair, nodeB );
        instance.addPairs( queue, graph, nodeB );
        nodes = graph.getNodes();
        while ( nodes.hasNext() ) {
            ContractNode node = (ContractNode) nodes.next();
            edges = node.getEdges();
            while ( edges.hasNext() ) {
                ContractEdge edge = (ContractEdge) edges.next();
                ContractNode target = (ContractNode) edge.getOtherNode( node );
                NodePair pair = new NodePair( node, target, edge );
                if ( ( node.equals( nodeA ) || target.equals( nodeA ) ) && !node.equals( nodeB ) && !target.equals( nodeB ) ) {
                    assertFalse( queue.contains( pair ) );
                } else {
                    assertTrue( queue.contains( pair ) );
                }
            }
        }
    }

    private FilteredGraph filter( Graph graph ) {
        NaturalCutsFilter instance = new NaturalCutsFilter( 1, 4, 40 );
        FilteredGraph result = instance.filter( graph );
        return result;
    }

}
