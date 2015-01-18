package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.StringValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link StringValue} to Character.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class CharInputAdapter implements
		InputAdapterPlugin<StringValue, Character> {

	@Override
	public Character getValue(StringValue v) {
		return v.getStringValue().charAt(0);
	}

	@Override
	public Class<StringValue> getDataValueType() {
		return StringValue.class;
	}

	@Override
	public Class<Character> getType() {
		return Character.class;
	}

}
