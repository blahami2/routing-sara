/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 */
public class ColorUtils {

    public static ColorSupplier createColorSupplier( int numberOfColors ) {
        return new ColorSupplier( numberOfColors );
    }

    public static class ColorSupplier {

        private final List<Color> colorList = new ArrayList<>();
        private int colorCounter = 0;

        ColorSupplier( int numberOfColors ) {
            float interval = 360 / ( numberOfColors );
            for ( float x = 0; x < 360; x += interval ) {
                Color c = Color.getHSBColor( x / 360, 1, 1 );
                colorList.add( c );
            }
        }

        public Color nextColor() {
            return colorList.get( colorCounter++ % colorList.size() );
        }
    }
}