package org.knime.scijava.commands.nodes;

import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeFactory.NodeType;
import org.scijava.Identifiable;
import org.scijava.module.ModuleInfo;
import org.w3c.dom.Element;

/**
 * Dynamic NodeDescription for SciJavaNodeSetFactory nodes.
 *
 * @author Christian Dietz, University of Konstanz
 */
// FIXME ;-)
// FIXME maybe abstract?
public class SciJavaNodeDescription extends NodeDescription {

    private final ModuleInfo info;
    private final NodeType nodeType;

    public SciJavaNodeDescription(final ModuleInfo info, final int numInports,
            final int numOutports) {
        this.info = info;
        this.nodeType = numInports > 0 && numOutports > 0 ? NodeType.Manipulator
                : (numInports == 0 ? NodeType.Sink : NodeType.Source);
    }

    @Override
    public String getIconPath() {
        return info.getIconPath();
    }

    @Override
    public String getInportDescription(final int index) {
        // FIXME
        return "Inport Info";
    }

    @Override
    public String getInportName(final int index) {
        // FIXME
        return "Inport name";
    }

    @Override
    public String getInteractiveViewName() {
        // FIXME will be used by imagej2 integration
        return null;
    }

    @Override
    public String getNodeName() {
        return info.getLabel() != null ? info.getLabel()
                : ((Identifiable) info).getIdentifier();
    }

    @Override
    public String getOutportDescription(final int index) {
        // FIXME
        return "Outport desc";
    }

    @Override
    public String getOutportName(final int index) {
        // FIXME
        return "Outport name";
    }

    @Override
    public NodeType getType() {
        return nodeType;
    }

    @Override
    public int getViewCount() {
        // FIXME will be used by ImageJ integration
        return 0;
    }

    @Override
    public String getViewDescription(final int index) {
        // FIXME
        return null;
    }

    @Override
    public String getViewName(final int index) {
        // FIXME
        return null;
    }

    @Override
    public Element getXMLDescription() {
        // FIXME will comprise all input and output parameters etc
        return null;
    }

}
