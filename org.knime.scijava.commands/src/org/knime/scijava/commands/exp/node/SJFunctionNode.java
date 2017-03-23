package org.knime.scijava.commands.exp.node;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

public interface SJFunctionNode<SI extends PortObjectSpec, SO extends PortObjectSpec, I extends PortObject, O extends PortObject>
        extends SJNode {

    SO configure(SI sj);

    O execute(ExecutionContext ctx, I in);

    @SuppressWarnings("unchecked")
    @Override
    default PortObjectSpec[] configure(final PortObjectSpec... objectSpecs) {
        return new PortObjectSpec[] { configure((SI) objectSpecs[0]) };
    }

    @SuppressWarnings("unchecked")
    @Override
    default PortObject[] execute(final ExecutionContext ctx,
            final PortObject... portObjects) {
        return new PortObject[] { execute(ctx, (I) portObjects[0]) };
    }
}
