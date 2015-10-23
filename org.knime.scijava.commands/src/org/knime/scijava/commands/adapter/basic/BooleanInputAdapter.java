package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.BooleanValue;
import org.knime.scijava.commands.AbstractInputAdapter;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link BooleanValue} to Boolean.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = InputAdapter.class)
public class BooleanInputAdapter
		extends AbstractInputAdapter<BooleanValue, Boolean> {

	@Override
	public Boolean getValue(final BooleanValue v) {
		return v.getBooleanValue();
	}

	@Override
	public Class<BooleanValue> getInputType() {
		return BooleanValue.class;
	}

	@Override
	public Class<Boolean> getOutputType() {
		return Boolean.class;
	}
}
