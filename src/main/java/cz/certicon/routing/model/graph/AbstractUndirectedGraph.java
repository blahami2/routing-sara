/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.model.graph;

import cz.certicon.routing.model.values.Coordinate;
import cz.certicon.routing.model.values.Distance;
import cz.certicon.routing.utils.GraphUtils;
import cz.certicon.routing.utils.collections.ImmutableIterator;
import cz.certicon.routing.utils.collections.Iterator;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Michael Blaha {@literal <michael.blaha@gmail.com>}
 * @param <N> node type
 * @param <E> edge type
 */
public abstract class AbstractUndirectedGraph<N extends Node, E extends Edge> implements Graph<N, E> {

    private final TLongObjectMap<N> nodes;
    private final TLongObjectMap<E> edges;
    private final Map<Metric, Map<Edge, Distance>> metricMap;
    private final EnumSet<Metric> metrics;
    private boolean locked = false;
//    @Getter( AccessLevel.NONE )
//    Map<Node, Coordinate> coordinates;

    public AbstractUndirectedGraph() {
        this.nodes = new TLongObjectHashMap<>();
        this.edges = new TLongObjectHashMap<>();
        this.metricMap = new EnumMap<>( Metric.class );
        this.metrics = EnumSet.noneOf( Metric.class );
    }

    public AbstractUndirectedGraph( Set<Metric> metrics ) {
        this.nodes = new TLongObjectHashMap<>();
        this.edges = new TLongObjectHashMap<>();
        this.metricMap = new EnumMap<>( Metric.class );
        this.metrics = EnumSet.copyOf( metrics );
        for ( Metric metric : metrics ) {
            metricMap.put( metric, new HashMap<Edge, Distance>() );
        }
    }

    @Override
    public int getNodesCount() {
        return nodes.size();
    }

    @Override
    public Iterator<N> getNodes() {
        return new ImmutableIterator<>( nodes.valueCollection().iterator() );
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public Iterator<E> getEdges() {
        return new ImmutableIterator<>( edges.valueCollection().iterator() );
    }

    @Override
    public Iterator<E> getIncomingEdges( N node ) {
        return (Iterator<E>) node.getIncomingEdges();
    }

    @Override
    public Iterator<E> getOutgoingEdges( Node node ) {
        return (Iterator<E>) node.getOutgoingEdges();
    }

    @Override
    public N getSourceNode( E edge ) {
        return (N) edge.getSource();
    }

    @Override
    public N getTargetNode( E edge ) {
        return (N) edge.getTarget();
    }

    @Override
    public N getOtherNode( E edge, N node ) {
        return (N) edge.getOtherNode( node );
    }

    @Override
    public Distance getTurnCost( N node, E from, E to ) {
        return node.getTurnDistance( from, to );
    }

    @Override
    public Iterator<E> getEdges( N node ) {
        return (Iterator<E>) node.getEdges();
    }

    @Override
    public Coordinate getNodeCoordinate( N node ) {
//        if ( !nodes.contains( node ) ) {
//            throw new IllegalArgumentException( "Graph does not contain node: " + node );
//        }
        return node.getCoordinate();
//        if ( coordinates == null ) {
//            throw new IllegalStateException( "Coordinates not set" );
//        }
//        if ( !coordinates.containsKey( node ) ) {
//            throw new IllegalArgumentException( "Unknown node: " + node );
//        }
//        return coordinates.get( node );
    }

    @Override
    public Distance getLength( Metric metric, E edge ) {
        if ( !metricMap.containsKey( metric ) ) {
            throw new IllegalArgumentException( "Unknown metric: " + metric );
        }
        Map<Edge, Distance> distanceMap = metricMap.get( metric );
        if ( !distanceMap.containsKey( edge ) ) {
            throw new IllegalArgumentException( "Unknown edge: " + edge );
        }
        return distanceMap.get( edge );
    }

    @Override
    public void setLength( Metric metric, E edge, Distance distnace ) {
        checkLock();
        if ( !metricMap.containsKey( metric ) ) {
//            metricMap.put( metric, new HashMap<Edge, Distance>() );
            throw new IllegalArgumentException( "Unknown metric: " + metric );
        }
        Map<Edge, Distance> distanceMap = metricMap.get( metric );
        distanceMap.put( edge, distnace );
    }

//    @Override
//    public Graph copy() {
//        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public N getNodeById( long id ) {
        return nodes.get( id );
    }

    @Override
    public E getEdgeById( long id ) {
        return edges.get( id );
    }

    @Override
    public boolean containsNode( long id ) {
        return nodes.containsKey( id );
    }

    @Override
    public boolean containsEdge( long id ) {
        return edges.containsKey( id );
    }

    @Override
    public void removeNode( N node ) {
        checkLock();
        List<E> removeEdges = new ArrayList<>();
        for ( E edge : getEdges( node ) ) {
            removeEdges.add( edge );
        }
        for ( E removeEdge : removeEdges ) {
            removeEdge( removeEdge );
        }
        nodes.remove( node.getId() );
    }

    @Override
    public void removeEdge( E edge ) {
        checkLock();
        edges.remove( edge.getId() );
        for ( Map<Edge, Distance> value : metricMap.values() ) {
            value.remove( edge );
        }
        edge.getSource().removeEdge( edge );
        edge.getTarget().removeEdge( edge );
    }

    @Override
    public void lock() {
        for ( N node : getNodes() ) {
            node.lock();
        }
        for ( Metric metric : metrics ) {
            if ( !metricMap.containsKey( metric ) ) {
                throw new IllegalStateException( "Metric not set but required: " + metric );
            }
            Map<Edge, Distance> distanceMap = metricMap.get( metric );
            for ( E edge : getEdges() ) {
                if ( !distanceMap.containsKey( edge ) ) {
                    throw new IllegalStateException( "Metric not set but required for: " + metric + " -> #" + edge );
                }
            }
        }
        this.locked = true;
    }

    @Override
    public Set<Metric> getMetrics() {
        return metrics.clone();
    }

    @Override
    public Graph<N, E> copy() {
        AbstractUndirectedGraph<N, E> instance = (AbstractUndirectedGraph<N, E>) newInstance( metrics );
        for ( N node : getNodes() ) {
            N newNode = (N) node.copy( instance );
            instance.nodes.put( newNode.getId(), newNode );
        }
        for ( E edge : getEdges() ) {
            E newEdge = (E) edge.copy( instance, instance.getNodeById( edge.getSource().getId() ), instance.getNodeById( edge.getTarget().getId() ) );
            instance.edges.put( newEdge.getId(), newEdge );
            for ( Metric metric : metrics ) {
                newEdge.setLength( metric, getLength( metric, edge ) );
            }
        }
        return instance;
    }

    @Override
    abstract public Graph<N, E> newInstance( Set<Metric> metrics );

    protected void addNode( N node ) {
        checkLock();
        nodes.put( node.getId(), node );
    }

    protected void addEdge( E edge ) {
        checkLock();
        edge.getSource().addEdge( edge );
        edge.getTarget().addEdge( edge );
        edges.put( edge.getId(), edge );
    }

    protected Collection<N> getNodeCollection() {
        return nodes.valueCollection();
    }

    protected Collection<E> getEdgeCollection() {
        return edges.valueCollection();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( getClass().getSimpleName() ).append( "{" ).append( "nodes=[" );
        for ( N node : getNodes() ) {
            sb.append( node.getId() ).append( "," );
        }
        if ( !nodes.isEmpty() ) {
            sb.replace( sb.length() - 1, sb.length(), "]" );
        } else {
            sb.append( "]" );
        }
        sb.append( ",edges=[" );
        for ( E edge : getEdges() ) {
            sb.append( edge.getId() ).append( "," );
        }
        if ( !edges.isEmpty() ) {
            sb.replace( sb.length() - 1, sb.length(), "]" );
        } else {
            sb.append( "]" );
        }
        sb.append( ",mapping={" );
        for ( N node : getNodes() ) {
            sb.append( node.getId() ).append( "=>[" );
            for ( E e : getEdges( node ) ) {
                sb.append( e.getSource().equals( node ) ? "+" : "-" ).append( e.getId() ).append( "," );
            }
            if ( node.getEdges().hasNext() ) {
                sb.replace( sb.length() - 1, sb.length(), "]" );
            } else {
                sb.append( "]" );
            }
            sb.append( "," );
        }
        if ( !nodes.isEmpty() ) {
            sb.replace( sb.length() - 1, sb.length(), "}" );
        } else {
            sb.append( "}" );
        }
        sb.append( additionalToStringData() );
        sb.append( "}" );
        return sb.toString();
    }

    protected String additionalToStringData() {
        return "";
    }

    private void checkLock() {
        if ( locked ) {
            throw new IllegalStateException( "This object is locked against modification." );
        }
    }
}
