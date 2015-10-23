package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.StringValue;
import org.knime.scijava.commands.AbstractInputAdapter;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link StringValue} to Character.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = InputAdapter.class, priority = Priority.LOW_PRIORITY)
public class CharInputAdapter
		extends AbstractInputAdapter<StringValue, Character> {

	@Override
	public Character getValue(final StringValue v) {
		return v.getStringValue().charAt(0);
	}

	@Override
	public Class<StringValue> getInputType() {
		return StringValue.class;
	}

	@Override
	public Class<Character> getOutputType() {
		return Character.class;
	}

}
