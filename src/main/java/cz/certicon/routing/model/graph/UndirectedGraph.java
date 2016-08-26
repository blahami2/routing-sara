/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.model.graph;

import cz.certicon.routing.model.values.Coordinate;
import cz.certicon.routing.model.values.Distance;
import cz.certicon.routing.utils.collections.ImmutableIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/**
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 */
@Value
@Builder
public class UndirectedGraph implements Graph {

    @NonNull
    @Getter( AccessLevel.NONE )
    @Singular
    List<Node> nodes;
    @NonNull
    @Getter( AccessLevel.NONE )
    @Singular
    List<Edge> edges;
//    @Getter( AccessLevel.NONE )
//    Map<Node, Coordinate> coordinates;

    @Override
    public int getNodesCount() {
        return nodes.size();
    }

    @Override
    public Iterator<Node> getNodes() {
        return new ImmutableIterator<>( nodes.iterator() );
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public Iterator<Edge> getEdges() {
        return new ImmutableIterator<>( edges.iterator() );
    }

    @Override
    public Iterator<Edge> getIncomingEdges( Node node ) {
        return node.getIncomingEdges();
    }

    @Override
    public Iterator<Edge> getOutgoingEdges( Node node ) {
        return node.getOutgoingEdges();
    }

    @Override
    public Node getSourceNode( Edge edge ) {
        return edge.getSource();
    }

    @Override
    public Node getTargetNode( Edge edge ) {
        return edge.getTarget();
    }

    @Override
    public Node getOtherNode( Edge edge, Node node ) {
        return edge.getOtherNode( node );
    }

    @Override
    public Distance getTurnCost( Node node, Edge from, Edge to ) {
        return node.getTurnTable().getCost( node.getEdgePosition( from ), node.getEdgePosition( to ) );
    }

    @Override
    public Iterator<Edge> getEdges( Node node ) {
        return node.getEdges();
    }

    @Override
    public Coordinate getNodeCoordinate( Node node ) {
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "UndirectedGraph{" ).append( "nodes=[" );
        for ( Node node : nodes ) {
            sb.append( node.getId() ).append( "," );
        }
        if ( !nodes.isEmpty() ) {
            sb.replace( sb.length() - 1, sb.length(), "]" );
        } else {
            sb.append( "]" );
        }
        sb.append( ",edges=[" );
        for ( Edge edge : edges ) {
            sb.append( edge.getId() ).append( "," );
        }
        if ( !edges.isEmpty() ) {
            sb.replace( sb.length() - 1, sb.length(), "]" );
        } else {
            sb.append( "]" );
        }
        sb.append( ",mapping={" );
        for ( Node node : nodes ) {
            sb.append( node.getId() ).append( "=>[" );
            Iterator<Edge> edgeIterator = node.getEdges();
            while ( edgeIterator.hasNext() ) {
                Edge e = edgeIterator.next();
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
        sb.append( "}" );
        return sb.toString();
    }

}
