package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.IntValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link IntValue} to Short.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class ShortInputAdapter implements InputAdapterPlugin<IntValue, Short> {

	@Override
	public Short getValue(IntValue v) {
		return (short) v.getIntValue();
	}

	@Override
	public Class<IntValue> getDataValueType() {
		return IntValue.class;
	}

	@Override
	public Class<Short> getType() {
		return Short.class;
	}

}
