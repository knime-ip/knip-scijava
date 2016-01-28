package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.LongValue;
import org.knime.scijava.commands.adapter.AbstractInputAdapter;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link LongValue} to Long.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = InputAdapter.class, priority = Priority.LOW_PRIORITY)
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
