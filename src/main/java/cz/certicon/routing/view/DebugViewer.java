/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.view;

/**
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 */
public interface DebugViewer {

    public void setStepByInput( boolean stepByInput );

    public void blinkEdge( long edgeId );

    public void displayEdge( long edgeId );

    public void removeEdge( long edgeId );

    public void closeEdge( long edgeId );

    public void displayNode( long nodeId );

    public void removeNode( long nodeId );
}
