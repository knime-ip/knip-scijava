package org.knime.scijava.commands.exp.node;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

public interface SJBiFunctionNode<I1S extends PortObjectSpec, I2S extends PortObjectSpec, OS extends PortObjectSpec, I1 extends PortObject, I2 extends PortObject, O extends PortObject>
        extends SJNode {

    OS configure(I1S in1, I2S in2);

    O execute(ExecutionContext ctx, I1 i1, I2 i2);

    @SuppressWarnings("unchecked")
    @Override
    default PortObjectSpec[] configure(final PortObjectSpec... objectSpecs) {
        return new PortObjectSpec[] {
                configure((I1S) objectSpecs[0], (I2S) objectSpecs[1]) };
    }

    @SuppressWarnings("unchecked")
    @Override
    default PortObject[] execute(final ExecutionContext ctx,
            final PortObject... portObjects) {
        return new PortObject[] {
                execute(ctx, (I1) portObjects[0], (I2) portObjects[0]) };
    }

}
