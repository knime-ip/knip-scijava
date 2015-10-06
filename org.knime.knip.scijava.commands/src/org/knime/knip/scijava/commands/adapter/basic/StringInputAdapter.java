package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.StringValue;
import org.knime.knip.scijava.commands.AbstractInputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link StringValue} to String.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapter.class, priority = 0.0)
public class StringInputAdapter extends
		AbstractInputAdapter<StringValue, String> {

	@Override
	public String getValue(StringValue v) {
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
