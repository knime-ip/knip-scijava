package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.BooleanValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link BooleanValue} to Boolean.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class BooleanInputAdapter implements
		InputAdapterPlugin<BooleanValue, Boolean> {

	@Override
	public Boolean getValue(BooleanValue v) {
		return v.getBooleanValue();
	}

	@Override
	public Class<BooleanValue> getDataValueType() {
		return BooleanValue.class;
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}

}
