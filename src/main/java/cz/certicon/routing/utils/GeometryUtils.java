/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.utils;

import cz.certicon.routing.model.values.CartesianCoords;
import cz.certicon.routing.model.values.Coordinate;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A geometry utility class.
 *
 * @author Michael Blaha {@literal <blahami2@gmail.com>}
 */
public class GeometryUtils {

    /**
     * Converts {@link CartesianCoords} to {@link Point}
     *
     * @param coords {@link CartesianCoords} coordinates
     * @return {@link Point} representation
     */
    public static Point toPointFromCartesian( CartesianCoords coords ) {
        return new Point( coords.getXAsInt(), coords.getYAsInt() );
    }

    /**
     * Scales the point based on the range of it's set into the given dimension.
     * For example, for numbers x: 5000, 1000, 9000, where 1000 is minimum, 9000
     * is maximum and the target dimension is 200x200: 1000 would be at x=0,
     * 9000 would be at x=199 and 45646 would be at x=100
     *
     * @param min minimal value in the source set of points
     * @param max maximal value in the source set of points
     * @param actual actual {@link Point} to be computed (placed)
     * @param targetDimension {@link Dimension} the point must be scaled into
     * @return scaled {@link Point}
     */
    public static Point getScaledPoint( Point min, Point max, Point actual, Dimension targetDimension ) {
        int width = Math.abs( min.x - max.x );
        int height = Math.abs( min.y - max.y );
        int x = ( actual.x - Math.min( min.x, max.x ) );
        int y = ( actual.y - Math.min( min.y, max.y ) );
//        System.out.println( "result = (actual - min(min, max))" );
//        System.out.println( y + " = " + actual.y + " - min(" + min.y + ", " + max.y + ")" );
//        System.out.println( y + " = " + actual.y + " - " + Math.min( min.y, max.y ) + "" );
        int scaledX = Math.round( (float) ( x * ( targetDimension.width / (double) width ) ) );
        int scaledY = Math.round( (float) ( y * ( targetDimension.height / (double) height ) ) );
        return new Point( scaledX, scaledY );
    }

    /**
     * Converts string in WKT format into {@link Coordinate}
     *
     * @param point description in WKT format
     * @return converted coordinate
     */
    public static Coordinate toCoordinatesFromWktPoint( String point ) {
        try {
            point = point.substring( "POINT(".length(), point.length() - ")".length() );
        } catch ( StringIndexOutOfBoundsException ex ) {
            System.out.println( "point = '" + point + "'" );
            System.out.println( "substring(" + "POINT(".length() + "," + ( point.length() - ")".length() ) );

            throw ex;
        }
        String[] lonlat = point.split( " " );
        return new Coordinate(
                Double.parseDouble( lonlat[1] ),
                Double.parseDouble( lonlat[0] )
        );
    }

    /**
     * Converts given {@link Coordinate} into string WKT format
     *
     * @param coordinate given coordinate
     * @return string representation in WKT format
     */
    public static String toWktFromCoordinates( Coordinate coordinate ) {
        return "POINT(" + coordinate.getLongitude() + " " + coordinate.getLatitude() + ")";
    }

    /**
     * Converts geometry representation in string WKT format into {@link List}
     * of {@link Coordinate}.
     *
     * @param linestring WKT representation of linestring (geometry)
     * @return list of coordinates
     */
    public static List<Coordinate> toCoordinatesFromWktLinestring( String linestring ) {
        List<Coordinate> coordinates = new ArrayList<>();
        String content = linestring.substring( "LINESTRING(".length(), linestring.length() - ")".length() );
        for ( String cord : content.split( "," ) ) {
            while ( cord.startsWith( " " ) ) {
                cord = cord.substring( 1 );
            }
            Coordinate coord = new Coordinate(
                    Double.parseDouble( cord.split( " " )[1] ),
                    Double.parseDouble( cord.split( " " )[0] )
            );
            coordinates.add( coord );
        }
        return coordinates;
    }

    /**
     * Converts {@link List} of {@link Coordinate} into string WKT
     * representation.
     *
     * @param coordinates list of coordinates
     * @return WKT representation as a linestring
     */
    public static String toWktFromCoordinates( List<Coordinate> coordinates ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "LINESTRING(" );
        for ( Coordinate coordinate : coordinates ) {
            sb.append( coordinate.getLongitude() ).append( " " ).append( coordinate.getLatitude() ).append( "," );
        }
        sb.delete( sb.length() - 1, sb.length() );
        sb.append( ")" );
        return sb.toString();
    }

    public static double pointToLineDistance( int ax, int ay, int bx, int by, int x, int y ) {

//        System.out.println( "first result = " + first);
        float dx = bx - ax;
        float dy = by - ay;
        float cx;
        float cy;
        if ( ( dx == 0 ) && ( dy == 0 ) ) {
            // It's a point not a line segment.
//            cx = ax;
//            cy = ay;
            dx = x - ax;
            dy = y - ay;
            return Math.sqrt( dx * dx + dy * dy );
        }
        // Calculate the t that minimizes the distance.
        float t = ( ( x - ax ) * dx + ( y - ay ) * dy ) / ( dx * dx + dy * dy );
        // See if this represents one of the segment's
        // end points or a point in the middle.
        if ( t < 0 ) {
//            cx = ax;
//            cy = ay;
            dx = x - ax;
            dy = y - ay;
        } else if ( t > 1 ) {
//            cx = bx;
//            cy = by;
            dx = x - bx;
            dy = y - by;
        } else {
            double normalLength = Math.sqrt( ( bx - ax ) * ( bx - ax ) + ( by - ay ) * ( by - ay ) );
            double first = ( Math.abs( ( x - ax ) * ( by - ay ) - ( y - ay ) * ( bx - ax ) ) / normalLength );
            return first;
        }
        double second = Math.sqrt( dx * dx + dy * dy );
        return second;
//        System.out.println( "second result = " + second );
//        return Math.max(first,second);

//        double normalLength = Math.sqrt( ( bx - ax ) * ( bx - ax ) + ( by - ay ) * ( by - ay ) );
//        return Math.abs( ( x - ax ) * ( by - ay ) - ( y - ay ) * ( bx - ax ) ) / normalLength;
    }
}
