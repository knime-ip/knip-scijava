package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DoubleValue;
import org.knime.knip.scijava.commands.AbstractInputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link DoubleValue} to Float.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = InputAdapter.class)
public class FloatInputAdapter
		extends AbstractInputAdapter<DoubleValue, Float> {

	@Override
	public Float getValue(final DoubleValue v) {
		return (float) v.getDoubleValue();
	}

	@Override
	public Class<DoubleValue> getInputType() {
		return DoubleValue.class;
	}

	@Override
	public Class<Float> getOutputType() {
		return Float.class;
	}

}
