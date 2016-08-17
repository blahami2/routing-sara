/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.data.basic;

import java.io.IOException;

/**
 * A generic reader interface for additional consistency.
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 * @param <Out> type to be written
 * @param <OutData> additional data
 */
public interface Writer<Out, OutData> {

    /**
     * Opens the writer for writing.
     *
     * @throws IOException exception when opening
     */
    public void open() throws IOException;

    /**
     * Closes the writer.
     *
     * @throws IOException exception when closing
     */
    public void close() throws IOException;

    /**
     * Reads the input and returns the output
     *
     * @param out output to be written
     * @param outData additional data
     * @throws IOException exception when writing
     */
    public void write( Out out, OutData outData ) throws IOException;

    /**
     * Returns true if the writer is open, false otherwise
     *
     * @return true or false
     */
    public boolean isOpen();
}
