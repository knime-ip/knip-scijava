package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.LongValue;
import org.knime.knip.scijava.commands.AbstractInputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link LongValue} to Long.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = InputAdapter.class)
public class LongInputAdapter extends AbstractInputAdapter<LongValue, Long> {

	@Override
	public Long getValue(final LongValue v) {
		return v.getLongValue();
	}

	@Override
	public Class<LongValue> getInputType() {
		return LongValue.class;
	}

	@Override
	public Class<Long> getOutputType() {
		return Long.class;
	}

}
