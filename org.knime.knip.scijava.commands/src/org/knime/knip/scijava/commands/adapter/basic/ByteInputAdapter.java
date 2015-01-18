package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.IntValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link IntValue} to String.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class ByteInputAdapter implements
		InputAdapterPlugin<IntValue, Byte> {

	@Override
	public Byte getValue(IntValue v) {
		return (byte)v.getIntValue();
	}

	@Override
	public Class<IntValue> getDataValueType() {
		return IntValue.class;
	}

	@Override
	public Class<Byte> getType() {
		return Byte.class;
	}

}
