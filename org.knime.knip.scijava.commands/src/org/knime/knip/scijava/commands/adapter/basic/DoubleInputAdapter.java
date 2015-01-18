package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DoubleValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link DoubleValue} to Double.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class DoubleInputAdapter implements
		InputAdapterPlugin<DoubleValue, Double> {

	@Override
	public Double getValue(DoubleValue v) {
		return v.getDoubleValue();
	}

	@Override
	public Class<DoubleValue> getDataValueType() {
		return DoubleValue.class;
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

}
