/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.data;

import java.io.IOException;

/**
 *
 * @author Michael Blaha {@literal <michael.blaha@gmail.com>}
 */
public interface GraphDataUpdater {

    public void deleteIsolatedAreas( GraphDeleteMessenger graphDeleteMessenger) throws IOException;
}
