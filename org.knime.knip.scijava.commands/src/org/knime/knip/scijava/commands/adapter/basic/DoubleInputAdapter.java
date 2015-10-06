package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DoubleValue;
import org.knime.knip.scijava.commands.AbstractInputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link DoubleValue} to Double.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapter.class)
public class DoubleInputAdapter extends
		AbstractInputAdapter<DoubleValue, Double> {

	@Override
	public Double getValue(DoubleValue v) {
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
