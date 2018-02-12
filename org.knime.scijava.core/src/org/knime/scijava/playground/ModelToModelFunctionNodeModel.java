package org.knime.scijava.playground;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortType;
import org.knime.scijava.base.node.DefaultNodeStructInstance;
import org.knime.scijava.base.node.NodeStructInstance;
import org.scijava.param2.ParameterStructs;
import org.scijava.param2.ValidityException;

public class ModelToModelFunctionNodeModel<I, O> extends NodeModel {

	protected final NodeStructInstance<Function<I, O>> m_funcInstance;

	protected ModelToModelFunctionNodeModel(final ModelToModelFunction<I, O> func, final PortType inPortType,
			final PortType outPortType) throws ValidityException {
		super(new PortType[] { inPortType }, new PortType[] { outPortType });
		m_funcInstance = new DefaultNodeStructInstance<>(ParameterStructs.structOf(func.getClass()), func);
	}

	@Override
	protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// no op
	}

	@Override
	protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// no op
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		try {
			m_funcInstance.saveSettingsTo(settings);
		} catch (final InvalidSettingsException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO: no op?
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_funcInstance.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {
		// no op
	}
}
