package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.IntValue;
import org.knime.knip.scijava.commands.AbstractInputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link IntValue} to String.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapter.class)
public class ByteInputAdapter extends AbstractInputAdapter<IntValue, Byte> {

	@Override
	public Byte getValue(IntValue v) {
		return (byte) v.getIntValue();
	}

	@Override
	public Class<IntValue> getInputType() {
		return IntValue.class;
	}

	@Override
	public Class<Byte> getOutputType() {
		return Byte.class;
	}

}
