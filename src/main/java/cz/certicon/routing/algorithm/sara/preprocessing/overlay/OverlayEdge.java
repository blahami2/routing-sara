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
public class OverlayEdge extends AbstractEdge<OverlayNode, OverlayEdge> implements BorderEdge<OverlayNode, OverlayEdge> {

    /**
     * underlaying border SaraEdge
     */
    @Getter
    private ZeroEdge zeroEdge = null;

    private OverlayBorder oBorder;

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

    public OverlayEdge(OverlayGraph graph, OverlayLift lift, OverlayNode source, OverlayNode target) {
        this(graph, lift.getEdgeId(), source, target);

        graph.getLayer().checkEdgeId(this.getId());

        this.zeroEdge = lift.getEdge();
        for (Metric metric : this.getGraph().getMetrics()) {
            Distance distance = lift.getEdge().getLength(metric);
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


    @Override
    public BorderData<OverlayNode, OverlayEdge> getBorder() {
        return this.oBorder;
    }

    @Override
    public void setBorder(BorderData<OverlayNode, OverlayEdge> border) {
        this.oBorder = (OverlayBorder) border;
    }

    public OverlayBorder border() {
        return this.oBorder;
    }
}
