package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.StringValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link StringValue} to String.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class StringInputAdapter implements
		InputAdapterPlugin<StringValue, String> {

	@Override
	public String getValue(StringValue v) {
		return v.getStringValue();
	}

	@Override
	public Class<StringValue> getDataValueType() {
		return StringValue.class;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

}
