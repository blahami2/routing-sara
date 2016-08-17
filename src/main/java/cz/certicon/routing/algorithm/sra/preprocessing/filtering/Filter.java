/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.algorithm.sra.preprocessing.filtering;

import cz.certicon.routing.model.graph.Graph;
import cz.certicon.routing.model.graph.preprocessing.FilteredGraph;

/**
 * Reduces graph size while preserving overall structure.
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 */
public interface Filter {

    FilteredGraph filter( Graph graph );
}
