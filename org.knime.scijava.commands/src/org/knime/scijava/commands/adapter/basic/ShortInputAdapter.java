package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.IntValue;
import org.knime.scijava.commands.adapter.AbstractInputAdapter;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link IntValue} to Short.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @author Christian Dietz, University of Konstanz
 *
 */
@Plugin(type = InputAdapter.class, priority=Priority.LOW_PRIORITY)
public class ShortInputAdapter extends AbstractInputAdapter<IntValue, Short> {

	@Override
	public Short getValue(final IntValue v) {
		return (short) v.getIntValue();
	}

	@Override
	public Class<IntValue> getInputType() {
		return IntValue.class;
	}

	@Override
	public Class<Short> getOutputType() {
		return Short.class;
	}

}
