package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.StringValue;
import org.knime.knip.scijava.commands.AbstractInputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link StringValue} to Character.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapter.class)
public class CharInputAdapter extends
		AbstractInputAdapter<StringValue, Character> {

	@Override
	public Character getValue(StringValue v) {
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
