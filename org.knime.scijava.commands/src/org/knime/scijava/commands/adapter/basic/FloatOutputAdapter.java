package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.DoubleValue;
import org.knime.core.data.def.DoubleCell;
import org.knime.scijava.commands.adapter.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Float to {@link DoubleCell}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = OutputAdapter.class)
public class FloatOutputAdapter
		extends AbstractOutputAdapter<Float, DoubleValue> {

	@Override
	public DoubleCell createCell(final Float o) {
		return new DoubleCell(o);
	}

	@Override
	public Class<DoubleValue> getOutputType() {
		return DoubleValue.class;
	}

	@Override
	public Class<Float> getInputType() {
		return Float.class;
	}

}
