/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.algorithm.sara.preprocessing.overlay;

import cz.certicon.routing.model.graph.AbstractEdge;
import cz.certicon.routing.model.graph.Graph;
import cz.certicon.routing.model.graph.Metric;
import cz.certicon.routing.model.graph.SaraEdge;
import cz.certicon.routing.model.graph.TurnTable;
import cz.certicon.routing.model.values.Distance;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author Blahoslav Potoček <potocek@merica.cz> Edge for the OverlayGraph.
 * Always directed, no turn constrains.
 */
public class OverlayEdge extends AbstractEdge<OverlayNode, OverlayEdge> {

    /**
     * underlaying border SaraEdge
     */
    @Getter
    SaraEdge saraEdge = null;

    /**
     * devel: in memory storage (in L1) of the caluclated shortcut in L0
     */
    public List<SaraEdge> saraWay;

    /**
     * devel: in memory storage (in LN2+) of the caluclated shortcut in LN-1
     */
    public List<OverlayEdge> overWay;

    public OverlayEdge(OverlayGraph graph, long id, OverlayNode source, OverlayNode target) {
        super(graph, id, true, source, target, -1, -1);
    }

    public OverlayEdge(OverlayGraph graph, OverlayEdge edge) {
        this(graph, edge.getId(), graph.getNodeById(edge.getSource().getId()), graph.getNodeById(edge.getTarget().getId()));
        if (edge.saraEdge != null) {
            this.setLink(edge.saraEdge);
        }
    }

    /**
     * sets the link to the underlying SaraEdge
     *
     * @param edge
     */
    public void setLink(SaraEdge edge) {
        this.saraEdge = edge;
        for (Metric metric : this.getGraph().getMetrics()) {
            Distance distance = edge.getLength(metric);
            this.setLength(metric, distance);
        }
    }

    @Override
    public Distance getTurnDistance(OverlayNode node, TurnTable turnTable, OverlayEdge targetEdge) {
        return Distance.newInstance(0);
    }

    @Override
    protected OverlayEdge newInstance(Graph<OverlayNode, OverlayEdge> newGraph, long id, boolean oneway, OverlayNode newSource, OverlayNode newTarget, int sourceIndex, int targetIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}