package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DoubleValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link DoubleValue} to Float.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class FloatInputAdapter implements
		InputAdapterPlugin<DoubleValue, Float> {

	@Override
	public Float getValue(DoubleValue v) {
		return (float)v.getDoubleValue();
	}

	@Override
	public Class<DoubleValue> getDataValueType() {
		return DoubleValue.class;
	}

	@Override
	public Class<Float> getType() {
		return Float.class;
	}

}
