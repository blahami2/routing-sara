/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.algorithm.sara.preprocessing.assembly;

import cz.certicon.routing.model.graph.Graph;
import cz.certicon.routing.model.graph.PartitionGraph;
import cz.certicon.routing.model.graph.preprocessing.FilteredGraph;

/**
 * Builds partitions using time-consuming techniques on reduced graph. Uses
 * greedy algorithm, local search heuristics, evolutionary algorithm and
 * combinations.
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 */
public interface Assembler {

    // return more
    // - node info
    PartitionGraph assemble( Graph originalGraph, FilteredGraph graph );
}
