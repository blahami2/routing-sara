/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.data.basic;

/**
 *
 * @deprecated java osm parsing not supported anymore, use database or other external application
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 */
public interface TemporaryMemory {

    /**
     * Returns {@link DataDestination} as a memory for runtime storage
     * 
     * @return memory for runtime storage
     */
    public DataDestination getMemoryAsDestination();

    public DataSource getMemoryAsSource();
}
