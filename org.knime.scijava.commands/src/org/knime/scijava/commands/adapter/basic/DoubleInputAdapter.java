package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.DoubleValue;
import org.knime.scijava.commands.AbstractInputAdapter;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link DoubleValue} to Double.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = InputAdapter.class, priority=Priority.HIGH_PRIORITY)
public class DoubleInputAdapter
		extends AbstractInputAdapter<DoubleValue, Double> {

	@Override
	public Double getValue(final DoubleValue v) {
		return v.getDoubleValue();
	}

	@Override
	public Class<DoubleValue> getInputType() {
		return DoubleValue.class;
	}

	@Override
	public Class<Double> getOutputType() {
		return Double.class;
	}
}
