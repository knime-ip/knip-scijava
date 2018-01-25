package org.knime.scijava.playground;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.scijava.playground.KerasModelToKerasModelFunction.KerasModelStruct;
import org.scijava.param2.ValidityException;

public class KerasModelToKerasModelFunctionNodeModel
	extends ModelToModelFunctionNodeModel<KerasModelStruct, KerasModelStruct> {

	protected KerasModelToKerasModelFunctionNodeModel(final KerasModelToKerasModelFunction func)
			throws ValidityException {
		super(func, PortObject.TYPE, PortObject.TYPE);
	}

	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		// TODO: dummy
		return inSpecs;
	}

	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
		// TODO: dummy
		return inObjects;
	}
}
