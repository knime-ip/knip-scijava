package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.IntValue;
import org.knime.knip.scijava.commands.AbstractInputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link IntValue} to Integer.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapter.class)
public class IntInputAdapter extends AbstractInputAdapter<IntValue, Integer> {

	@Override
	public Integer getValue(IntValue v) {
		return v.getIntValue();
	}

	@Override
	public Class<IntValue> getInputType() {
		return IntValue.class;
	}

	@Override
	public Class<Integer> getOutputType() {
		return Integer.class;
	}

}
