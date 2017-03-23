package org.knime.scijava.commands.exp.node;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.scijava.plugin.SciJavaPlugin;

public interface SJNode extends SciJavaPlugin {

    PortObjectSpec[] configure(PortObjectSpec... objectSpecs);

    PortObject[] execute(ExecutionContext ctx, PortObject... portObjects);

    int numInports();

    int numOutports();

}
