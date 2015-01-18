package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.LongValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link LongValue} to Long.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class LongInputAdapter implements
		InputAdapterPlugin<LongValue, Long> {

	@Override
	public Long getValue(LongValue v) {
		return v.getLongValue();
	}

	@Override
	public Class<LongValue> getDataValueType() {
		return LongValue.class;
	}

	@Override
	public Class<Long> getType() {
		return Long.class;
	}

}
