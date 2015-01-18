package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.IntValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link IntValue} to Integer.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class IntInputAdapter implements
		InputAdapterPlugin<IntValue, Integer> {

	@Override
	public Integer getValue(IntValue v) {
		return v.getIntValue();
	}

	@Override
	public Class<IntValue> getDataValueType() {
		return IntValue.class;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

}
