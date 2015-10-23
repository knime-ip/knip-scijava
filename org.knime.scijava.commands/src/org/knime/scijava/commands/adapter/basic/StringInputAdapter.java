package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.StringValue;
import org.knime.scijava.commands.AbstractInputAdapter;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link StringValue} to String.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = InputAdapter.class, priority = Priority.HIGH_PRIORITY)
public class StringInputAdapter
		extends AbstractInputAdapter<StringValue, String> {

	@Override
	public String getValue(final StringValue v) {
		return v.getStringValue();
	}

	@Override
	public Class<StringValue> getInputType() {
		return StringValue.class;
	}

	@Override
	public Class<String> getOutputType() {
		return String.class;
	}
}
